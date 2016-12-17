
package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTTags;
import noppes.npcs.util.ValueUtil;

public class DataInventory implements IInventory, INPCInventory {
	public Map<Integer, IItemStack> drops;
	public Map<Integer, Integer> dropchance;
	public Map<Integer, IItemStack> weapons;
	public Map<Integer, IItemStack> armor;
	private int minExp;
	private int maxExp;
	public int lootMode;
	private EntityNPCInterface npc;
	public ItemStack renderOffhand;

	public DataInventory(EntityNPCInterface npc) {
		drops = new HashMap<Integer, IItemStack>();
		dropchance = new HashMap<Integer, Integer>();
		weapons = new HashMap<Integer, IItemStack>();
		armor = new HashMap<Integer, IItemStack>();
		minExp = 0;
		maxExp = 0;
		lootMode = 0;
		renderOffhand = null;
		this.npc = npc;
	}

	@Override
	public void clear() {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		int i = 0;
		Map<Integer, IItemStack> var3;
		if (par1 >= 7) {
			var3 = drops;
			par1 -= 7;
		} else if (par1 >= 4) {
			var3 = weapons;
			par1 -= 4;
			i = 1;
		} else {
			var3 = armor;
			i = 2;
		}
		ItemStack var4 = null;
		if (var3.get(par1) != null) {
			if (var3.get(par1).getMCItemStack().stackSize <= par2) {
				var4 = var3.get(par1).getMCItemStack();
				var3.put(par1, null);
			} else {
				var4 = var3.get(par1).getMCItemStack().splitStack(par2);
				if (var3.get(par1).getMCItemStack().stackSize == 0) {
					var3.put(par1, null);
				}
			}
		}
		if (i == 1) {
			weapons = var3;
		}
		if (i == 2) {
			armor = var3;
		}
		return var4;
	}

	public void dropStuff(Entity entity, DamageSource damagesource) {
		ArrayList<EntityItem> list = new ArrayList<EntityItem>();
		for (int i : drops.keySet()) {
			IItemStack item = drops.get(i);
			if (item == null) {
				continue;
			}
			int dchance = 100;
			if (dropchance.containsKey(i)) {
				dchance = dropchance.get(i);
			}
			int chance = npc.worldObj.rand.nextInt(100) + dchance;
			if (chance < 100) {
				continue;
			}
			EntityItem e = getEntityItem(item.getMCItemStack().copy());
			if (e == null) {
				continue;
			}
			list.add(e);
		}
		int enchant = 0;
		if (damagesource.getSourceOfDamage() instanceof EntityPlayer) {
			enchant = EnchantmentHelper.getLootingModifier((EntityLivingBase) damagesource.getSourceOfDamage());
		}
		if (!ForgeHooks.onLivingDrops(npc, damagesource, list, enchant, true)) {
			for (EntityItem item2 : list) {
				if ((lootMode == 1) && (entity instanceof EntityPlayer)) {
					EntityPlayer player = (EntityPlayer) entity;
					item2.setPickupDelay(2);
					npc.worldObj.spawnEntityInWorld(item2);
					ItemStack stack = item2.getEntityItem();
					int j = stack.stackSize;
					if (!player.inventory.addItemStackToInventory(stack)) {
						continue;
					}
					npc.worldObj.playSoundAtEntity(item2, "random.pop", 0.2f,
							(((npc.getRNG().nextFloat() - npc.getRNG().nextFloat()) * 0.7f) + 1.0f) * 2.0f);
					player.onItemPickup(item2, j);
					if (stack.stackSize > 0) {
						continue;
					}
					item2.setDead();
				} else {
					npc.worldObj.spawnEntityInWorld(item2);
				}
			}
		}
		int exp = getExpRNG();
		while (exp > 0) {
			int var2 = EntityXPOrb.getXPSplit(exp);
			exp -= var2;
			if ((lootMode == 1) && (entity instanceof EntityPlayer)) {
				npc.worldObj.spawnEntityInWorld(
						new EntityXPOrb(entity.worldObj, entity.posX, entity.posY, entity.posZ, var2));
			} else {
				npc.worldObj.spawnEntityInWorld(new EntityXPOrb(npc.worldObj, npc.posX, npc.posY, npc.posZ, var2));
			}
		}
	}

	@Override
	public IItemStack getArmor(int slot) {
		return armor.get(slot);
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public IItemStack getDropItem(int slot) {
		if ((slot < 0) || (slot > 8)) {
			throw new CustomNPCsException("Bad slot number: " + slot, new Object[0]);
		}
		IItemStack item = npc.inventory.drops.get(slot);
		if (item == null) {
			return null;
		}
		return new ItemStackWrapper(item.getMCItemStack());
	}

	public EntityItem getEntityItem(ItemStack itemstack) {
		if (itemstack == null) {
			return null;
		}
		EntityItem entityitem = new EntityItem(npc.worldObj, npc.posX,
				(npc.posY - 0.30000001192092896) + npc.getEyeHeight(), npc.posZ, itemstack);
		entityitem.setPickupDelay(40);
		float f2 = npc.getRNG().nextFloat() * 0.5f;
		float f3 = npc.getRNG().nextFloat() * 3.141593f * 2.0f;
		entityitem.motionX = -MathHelper.sin(f3) * f2;
		entityitem.motionZ = MathHelper.cos(f3) * f2;
		entityitem.motionY = 0.20000000298023224;
		return entityitem;
	}

	@Override
	public int getExpMax() {
		return npc.inventory.maxExp;
	}

	@Override
	public int getExpMin() {
		return npc.inventory.minExp;
	}

	@Override
	public int getExpRNG() {
		int exp = minExp;
		if ((maxExp - minExp) > 0) {
			exp += npc.worldObj.rand.nextInt(maxExp - minExp);
		}
		return exp;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public IItemStack getLeftHand() {
		return weapons.get(2);
	}

	@Override
	public String getName() {
		return "NPC Inventory";
	}

	@Override
	public IItemStack getProjectile() {
		return weapons.get(1);
	}

	@Override
	public IItemStack getRightHand() {
		return weapons.get(0);
	}

	@Override
	public int getSizeInventory() {
		return 15;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i < 4) {
			return ItemStackWrapper.MCItem(getArmor(i));
		}
		if (i < 7) {
			return ItemStackWrapper.MCItem(weapons.get(i - 4));
		}
		return ItemStackWrapper.MCItem(drops.get(i - 7));
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		minExp = nbttagcompound.getInteger("MinExp");
		maxExp = nbttagcompound.getInteger("MaxExp");
		drops = NBTTags.getIItemStackMap(nbttagcompound.getTagList("NpcInv", 10));
		armor = NBTTags.getIItemStackMap(nbttagcompound.getTagList("Armor", 10));
		weapons = NBTTags.getIItemStackMap(nbttagcompound.getTagList("Weapons", 10));
		dropchance = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("DropChance", 10));
		lootMode = nbttagcompound.getInteger("LootMode");
	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		int i = 0;
		Map<Integer, IItemStack> var2;
		if (par1 >= 7) {
			var2 = drops;
			par1 -= 7;
		} else if (par1 >= 4) {
			var2 = weapons;
			par1 -= 4;
			i = 1;
		} else {
			var2 = armor;
			i = 2;
		}
		if (var2.get(par1) != null) {
			ItemStack var3 = var2.get(par1).getMCItemStack();
			var2.put(par1, null);
			if (i == 1) {
				weapons = var2;
			}
			if (i == 2) {
				armor = var2;
			}
			return var3;
		}
		return null;
	}

	@Override
	public void setArmor(int slot, IItemStack item) {
		armor.put(slot, item);
		npc.updateClient = true;
	}

	@Override
	public void setDropItem(int slot, IItemStack item, int chance) {
		if ((slot < 0) || (slot > 8)) {
			throw new CustomNPCsException("Bad slot number: " + slot, new Object[0]);
		}
		chance = ValueUtil.CorrectInt(chance, 1, 100);
		if (item == null) {
			dropchance.remove(slot);
			drops.remove(slot);
		} else {
			dropchance.put(slot, chance);
			drops.put(slot, item);
		}
	}

	@Override
	public void setExp(int min, int max) {
		minExp = Math.min(minExp, maxExp);
		npc.inventory.minExp = minExp;
		npc.inventory.maxExp = maxExp;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		int i = 0;
		Map<Integer, IItemStack> var3;
		if (par1 >= 7) {
			var3 = drops;
			par1 -= 7;
		} else if (par1 >= 4) {
			var3 = weapons;
			par1 -= 4;
			i = 1;
		} else {
			var3 = armor;
			i = 2;
		}
		if (par2ItemStack == null) {
			var3.put(par1, null);
		} else {
			var3.put(par1, new ItemStackWrapper(par2ItemStack));
		}
		if (i == 1) {
			weapons = var3;
		}
		if (i == 2) {
			armor = var3;
		}
	}

	@Override
	public void setLeftHand(IItemStack item) {
		weapons.put(2, item);
		npc.updateClient = true;
	}

	@Override
	public void setProjectile(IItemStack item) {
		weapons.put(1, item);
		npc.updateAI = true;
	}

	@Override
	public void setRightHand(IItemStack item) {
		weapons.put(0, item);
		npc.updateClient = true;
	}

	public NBTTagCompound writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("MinExp", minExp);
		nbttagcompound.setInteger("MaxExp", maxExp);
		nbttagcompound.setTag("NpcInv", NBTTags.nbtIItemStackMap(drops));
		nbttagcompound.setTag("Armor", NBTTags.nbtIItemStackMap(armor));
		nbttagcompound.setTag("Weapons", NBTTags.nbtIItemStackMap(weapons));
		nbttagcompound.setTag("DropChance", NBTTags.nbtIntegerIntegerMap(dropchance));
		nbttagcompound.setInteger("LootMode", lootMode);
		return nbttagcompound;
	}
}
