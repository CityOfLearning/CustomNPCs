
package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.companion.CompanionFarmer;
import noppes.npcs.roles.companion.CompanionFoodStats;
import noppes.npcs.roles.companion.CompanionGuard;
import noppes.npcs.roles.companion.CompanionJobInterface;
import noppes.npcs.roles.companion.CompanionTrader;
import noppes.npcs.util.NoppesUtilServer;

public class RoleCompanion extends RoleInterface {
	public NpcMiscInventory inventory;
	public String uuid;
	public String ownerName;
	public Map<EnumCompanionTalent, Integer> talents;
	public boolean canAge;
	public long ticksActive;
	public EnumCompanionStage stage;
	public EntityPlayer owner;
	public int companionID;
	public EnumCompanionJobs job;
	public CompanionJobInterface jobInterface;
	public boolean hasInv;
	public boolean defendOwner;
	public CompanionFoodStats foodstats;
	private int eatingTicks;
	private IItemStack eating;
	private int eatingDelay;
	public int currentExp;

	public RoleCompanion(EntityNPCInterface npc) {
		super(npc);
		uuid = "";
		ownerName = "";
		talents = new TreeMap<>();
		canAge = true;
		ticksActive = 0L;
		stage = EnumCompanionStage.FULLGROWN;
		owner = null;
		job = EnumCompanionJobs.NONE;
		jobInterface = null;
		hasInv = true;
		defendOwner = true;
		foodstats = new CompanionFoodStats();
		eatingTicks = 20;
		eating = null;
		eatingDelay = 0;
		currentExp = 0;
		inventory = new NpcMiscInventory(12);
	}

	public void addExp(int exp) {
		if (canAddExp(exp)) {
			currentExp += exp;
		}
	}

	public void addMovementStat(double x, double y, double z) {
		int i = Math.round(MathHelper.sqrt_double((x * x) + (y * y) + (z * z)) * 100.0f);
		if (npc.isAttacking()) {
			foodstats.addExhaustion(0.04f * i * 0.01f);
		} else {
			foodstats.addExhaustion(0.02f * i * 0.01f);
		}
	}

	public void addTalentExp(EnumCompanionTalent talent, int exp) {
		if (talents.containsKey(talent)) {
			exp += talents.get(talent);
		}
		talents.put(talent, exp);
	}

	@Override
	public boolean aiShouldExecute() {
		EntityPlayer prev = owner;
		owner = getOwner();
		if ((jobInterface != null) && jobInterface.isSelfSufficient()) {
			return true;
		}
		if ((owner == null) && !uuid.isEmpty()) {
			npc.isDead = true;
		} else if ((prev != owner) && (owner != null)) {
			ownerName = owner.getDisplayNameString();
			PlayerData data = PlayerDataController.instance.getPlayerData(owner);
			if (data.companionID != companionID) {
				npc.isDead = true;
			}
		}
		return owner != null;
	}

	@Override
	public void aiUpdateTask() {
		if ((owner != null) && ((jobInterface == null) || !jobInterface.isSelfSufficient())) {
			foodstats.onUpdate(npc);
		}
		if (foodstats.getFoodLevel() >= 18) {
			npc.stats.healthRegen = 0;
			npc.stats.combatRegen = 0;
		}
		if (foodstats.needFood() && isSitting()) {
			if (eatingDelay > 0) {
				--eatingDelay;
				return;
			}
			IItemStack prev = eating;
			eating = getFood();
			if ((prev != null) && (eating == null)) {
				npc.setRoleDataWatcher("");
			}
			if ((prev == null) && (eating != null)) {
				npc.setRoleDataWatcher("eating");
				eatingTicks = 20;
			}
			if (isEating()) {
				doEating();
			}
		} else if ((eating != null) && !isSitting()) {
			eating = null;
			eatingDelay = 20;
			npc.setRoleDataWatcher("");
		}
		++ticksActive;
		if (canAge && (stage != EnumCompanionStage.FULLGROWN)) {
			if ((stage == EnumCompanionStage.BABY) && (ticksActive > EnumCompanionStage.CHILD.matureAge)) {
				matureTo(EnumCompanionStage.CHILD);
			} else if ((stage == EnumCompanionStage.CHILD) && (ticksActive > EnumCompanionStage.TEEN.matureAge)) {
				matureTo(EnumCompanionStage.TEEN);
			} else if ((stage == EnumCompanionStage.TEEN) && (ticksActive > EnumCompanionStage.ADULT.matureAge)) {
				matureTo(EnumCompanionStage.ADULT);
			} else if ((stage == EnumCompanionStage.ADULT) && (ticksActive > EnumCompanionStage.FULLGROWN.matureAge)) {
				matureTo(EnumCompanionStage.FULLGROWN);
			}
		}
	}

	public float applyArmorCalculations(DamageSource source, float damage) {
		if (!hasInv || (getTalentLevel(EnumCompanionTalent.ARMOR) <= 0)) {
			return damage;
		}
		if (!source.isUnblockable()) {
			damageArmor(damage);
			int i = 25 - getTotalArmorValue();
			float f1 = damage * i;
			damage = f1 / 25.0f;
		}
		return damage;
	}

	public void attackedEntity(Entity entity) {
		IItemStack weapon = npc.inventory.getRightHand();
		gainExp((weapon == null) ? 8 : 4);
		if (weapon == null) {
			return;
		}
		weapon.getMCItemStack().damageItem(1, npc);
		if (weapon.getMCItemStack().stackSize <= 0) {
			npc.inventory.setRightHand(null);
		}
	}

	public boolean canAddExp(int exp) {
		int newExp = currentExp + exp;
		return (newExp >= 0) && (newExp < getMaxExp());
	}

	public boolean canWearArmor(ItemStack item) {
		int level = getTalentLevel(EnumCompanionTalent.ARMOR);
		if ((item == null) || !(item.getItem() instanceof ItemArmor) || (level <= 0)) {
			return false;
		}
		if (level >= 5) {
			return true;
		}
		ItemArmor armor = (ItemArmor) item.getItem();
		int reduction = (Integer) ObfuscationReflectionHelper.getPrivateValue((Class) ItemArmor.ArmorMaterial.class,
				armor.getArmorMaterial(), 6);
		return ((reduction <= 5) && (level >= 1)) || ((reduction <= 7) && (level >= 2))
				|| ((reduction <= 15) && (level >= 3)) || ((reduction <= 33) && (level >= 4));
	}

	public boolean canWearSword(IItemStack item) {
		int level = getTalentLevel(EnumCompanionTalent.SWORD);
		return (item != null) && (item.getMCItemStack().getItem() instanceof ItemSword) && (level > 0)
				&& ((level >= 5) || ((getSwordDamage(item) - level) < 4.0));
	}

	public boolean canWearWeapon(IItemStack stack) {
		if ((stack == null) || (stack.getMCItemStack().getItem() == null)) {
			return false;
		}
		Item item = stack.getMCItemStack().getItem();
		if (item instanceof ItemSword) {
			return canWearSword(stack);
		}
		if (item instanceof ItemBow) {
			return getTalentLevel(EnumCompanionTalent.RANGED) > 2;
		}
		return (item == Item.getItemFromBlock(Blocks.cobblestone)) && (getTalentLevel(EnumCompanionTalent.RANGED) > 1);
	}

	@Override
	public void clientUpdate() {
		if (npc.getRoleDataWatcher().equals("eating")) {
			eating = getFood();
			if (isEating()) {
				doEating();
			}
		} else if (eating != null) {
			eating = null;
		}
	}

	private void damageArmor(float damage) {
		damage /= 4.0f;
		if (damage < 1.0f) {
			damage = 1.0f;
		}
		boolean hasArmor = false;
		Iterator<Map.Entry<Integer, IItemStack>> ita = npc.inventory.armor.entrySet().iterator();
		while (ita.hasNext()) {
			Map.Entry<Integer, IItemStack> entry = ita.next();
			IItemStack item = entry.getValue();
			if (item != null) {
				if (!(item.getMCItemStack().getItem() instanceof ItemArmor)) {
					continue;
				}
				hasArmor = true;
				item.getMCItemStack().damageItem((int) damage, npc);
				if (item.getStackSize() > 0) {
					continue;
				}
				ita.remove();
			}
		}
		gainExp(hasArmor ? 4 : 8);
	}

	@Override
	public boolean defendOwner() {
		return defendOwner && (owner != null) && (stage != EnumCompanionStage.BABY)
				&& ((jobInterface == null) || !jobInterface.isSelfSufficient());
	}

	private void doEating() {
		if (eating == null) {
			return;
		}
		ItemStack eating = this.eating.getMCItemStack();
		if (npc.worldObj.isRemote) {
			Random rand = npc.getRNG();
			for (int j = 0; j < 2; ++j) {
				Vec3 vec3 = new Vec3((rand.nextFloat() - 0.5) * 0.1, (Math.random() * 0.1) + 0.1, 0.0);
				vec3.rotateYaw((-npc.rotationPitch * 3.1415927f) / 180.0f);
				vec3.rotatePitch((-npc.renderYawOffset * 3.1415927f) / 180.0f);
				Vec3 vec4 = new Vec3((rand.nextFloat() - 0.5) * 0.3, (-rand.nextFloat() * 0.6) - 0.3,
						(npc.width / 2.0f) + 0.1);
				vec4.rotateYaw((-npc.rotationPitch * 3.1415927f) / 180.0f);
				vec4.rotatePitch((-npc.renderYawOffset * 3.1415927f) / 180.0f);
				vec4 = vec4.addVector(npc.posX, npc.posY + npc.height + 0.1, npc.posZ);
				Item.getIdFromItem(eating.getItem());
				if (eating.getHasSubtypes()) {
					npc.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec4.xCoord, vec4.yCoord, vec4.zCoord,
							vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord,
							new int[] { Item.getIdFromItem(eating.getItem()), eating.getMetadata() });
				} else {
					npc.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec4.xCoord, vec4.yCoord, vec4.zCoord,
							vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord,
							new int[] { Item.getIdFromItem(eating.getItem()) });
				}
			}
		} else {
			--eatingTicks;
			if (eatingTicks <= 0) {
				if (inventory.decrStackSize(eating, 1)) {
					ItemFood food = (ItemFood) eating.getItem();
					foodstats.onFoodEaten(food, eating);
					npc.playSound("random.burp", 0.5f, (npc.getRNG().nextFloat() * 0.1f) + 0.9f);
				}
				eatingDelay = 20;
				npc.setRoleDataWatcher("");
				eating = null;
			} else if ((eatingTicks > 3) && ((eatingTicks % 2) == 0)) {
				Random rand = npc.getRNG();
				npc.playSound("random.eat", 0.5f + (0.5f * rand.nextInt(2)),
						((rand.nextFloat() - rand.nextFloat()) * 0.2f) + 1.0f);
			}
		}
	}

	public void gainExp(int chance) {
		if (npc.getRNG().nextInt(chance) == 0) {
			addExp(1);
		}
	}

	public int getExp(EnumCompanionTalent talent) {
		if (talents.containsKey(talent)) {
			return talents.get(talent);
		}
		return -1;
	}

	private IItemStack getFood() {
		List<ItemStack> food = new ArrayList<>(inventory.items.values());
		Iterator<ItemStack> ite = food.iterator();
		int i = -1;
		while (ite.hasNext()) {
			ItemStack is = ite.next();
			if ((is == null) || !(is.getItem() instanceof ItemFood)) {
				ite.remove();
			} else {
				int amount = ((ItemFood) is.getItem()).getDamage(is);
				if ((i != -1) && (amount >= i)) {
					continue;
				}
				i = amount;
			}
		}
		for (ItemStack is2 : food) {
			if (((ItemFood) is2.getItem()).getDamage(is2) == i) {
				return new ItemStackWrapper(is2);
			}
		}
		return null;
	}

	public IItemStack getHeldItem() {
		if (eating != null) {
			return eating;
		}
		return npc.inventory.getRightHand();
	}

	public int getMaxExp() {
		return 500 + (getTotalLevel() * 200);
	}

	public Integer getNextLevel(EnumCompanionTalent talent) {
		if (!talents.containsKey(talent)) {
			return 0;
		}
		int exp = talents.get(talent);
		if (exp < 400) {
			return 400;
		}
		if (exp < 1000) {
			return 700;
		}
		if (exp < 1700) {
			return 1700;
		}
		if (exp < 3000) {
			return 3000;
		}
		return 5000;
	}

	public EntityPlayer getOwner() {
		if ((uuid == null) || uuid.isEmpty()) {
			return null;
		}
		try {
			UUID id = UUID.fromString(uuid);
			if (id != null) {
				return NoppesUtilServer.getPlayer(id);
			}
		} catch (IllegalArgumentException ex) {
		}
		return null;
	}

	private double getSwordDamage(IItemStack item) {
		if ((item == null) || !(item.getMCItemStack().getItem() instanceof ItemSword)) {
			return 0.0;
		}
		Multimap<String, AttributeModifier> map = item.getMCItemStack().getAttributeModifiers();
		for (Map.Entry<String, AttributeModifier> entry : map.entries()) {
			if (entry.getKey().equals(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())) {
				AttributeModifier mod = entry.getValue();
				return mod.getAmount();
			}
		}
		return 0.0;
	}

	public int getTalentLevel(EnumCompanionTalent talent) {
		if (!talents.containsKey(talent)) {
			return 0;
		}
		int exp = talents.get(talent);
		if (exp >= 5000) {
			return 5;
		}
		if (exp >= 3000) {
			return 4;
		}
		if (exp >= 1700) {
			return 3;
		}
		if (exp >= 1000) {
			return 2;
		}
		if (exp >= 400) {
			return 1;
		}
		return 0;
	}

	public int getTotalArmorValue() {
		int armorValue = 0;
		for (IItemStack armor : npc.inventory.armor.values()) {
			if ((armor != null) && (armor.getMCItemStack().getItem() instanceof ItemArmor)) {
				armorValue += ((ItemArmor) armor.getMCItemStack().getItem()).damageReduceAmount;
			}
		}
		return armorValue;
	}

	public int getTotalLevel() {
		int level = 0;
		for (EnumCompanionTalent talent : talents.keySet()) {
			level += getTalentLevel(talent);
		}
		return level;
	}

	public boolean hasInv() {
		return hasInv && (hasTalent(EnumCompanionTalent.INVENTORY) || hasTalent(EnumCompanionTalent.ARMOR)
				|| hasTalent(EnumCompanionTalent.SWORD));
	}

	public boolean hasOwner() {
		return !uuid.isEmpty();
	}

	public boolean hasTalent(EnumCompanionTalent talent) {
		return getTalentLevel(talent) > 0;
	}

	@Override
	public void interact(EntityPlayer player) {
		if ((player != null) && (job == EnumCompanionJobs.SHOP)) {
			((CompanionTrader) jobInterface).interact(player);
		}
		if ((player != owner) || !npc.isEntityAlive() || npc.isAttacking()) {
			return;
		}
		if (player.isSneaking()) {
			openGui(player);
		} else {
			setSitting(!isSitting());
		}
	}

	public boolean isEating() {
		return eating != null;
	}

	public boolean isFollowing() {
		return ((jobInterface == null) || !jobInterface.isSelfSufficient()) && (owner != null) && !isSitting();
	}

	public boolean isSitting() {
		return npc.ai.animationType == 1;
	}

	public void levelSword() {
		if (!talents.containsKey(EnumCompanionTalent.SWORD)) {
			return;
		}
	}

	public void levelTalent(EnumCompanionTalent talent, int exp) {
		if (!talents.containsKey(EnumCompanionTalent.SWORD)) {
			return;
		}
		talents.put(talent, exp + talents.get(talent));
	}

	public void matureTo(EnumCompanionStage stage) {
		this.stage = stage;
		EntityCustomNpc npc = (EntityCustomNpc) this.npc;
		npc.ai.animationType = stage.animation;
		if (stage == EnumCompanionStage.BABY) {
			npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(0.5f, 0.5f, 0.5f);
			npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(0.5f, 0.5f, 0.5f);
			npc.modelData.getPartConfig(EnumParts.BODY).setScale(0.5f, 0.5f, 0.5f);
			npc.modelData.getPartConfig(EnumParts.HEAD).setScale(0.7f, 0.7f, 0.7f);
			npc.ai.onAttack = 1;
			npc.ai.setWalkingSpeed(3);
			if (!talents.containsKey(EnumCompanionTalent.INVENTORY)) {
				talents.put(EnumCompanionTalent.INVENTORY, 0);
			}
		}
		if (stage == EnumCompanionStage.CHILD) {
			npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(0.6f, 0.6f, 0.6f);
			npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(0.6f, 0.6f, 0.6f);
			npc.modelData.getPartConfig(EnumParts.BODY).setScale(0.6f, 0.6f, 0.6f);
			npc.modelData.getPartConfig(EnumParts.HEAD).setScale(0.8f, 0.8f, 0.8f);
			npc.ai.onAttack = 0;
			npc.ai.setWalkingSpeed(4);
			if (!talents.containsKey(EnumCompanionTalent.SWORD)) {
				talents.put(EnumCompanionTalent.SWORD, 0);
			}
		}
		if (stage == EnumCompanionStage.TEEN) {
			npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8f, 0.8f, 0.8f);
			npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(0.8f, 0.8f, 0.8f);
			npc.modelData.getPartConfig(EnumParts.BODY).setScale(0.8f, 0.8f, 0.8f);
			npc.modelData.getPartConfig(EnumParts.HEAD).setScale(0.9f, 0.9f, 0.9f);
			npc.ai.onAttack = 0;
			npc.ai.setWalkingSpeed(5);
			if (!talents.containsKey(EnumCompanionTalent.ARMOR)) {
				talents.put(EnumCompanionTalent.ARMOR, 0);
			}
		}
		if ((stage == EnumCompanionStage.ADULT) || (stage == EnumCompanionStage.FULLGROWN)) {
			npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(1.0f, 1.0f, 1.0f);
			npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(1.0f, 1.0f, 1.0f);
			npc.modelData.getPartConfig(EnumParts.BODY).setScale(1.0f, 1.0f, 1.0f);
			npc.modelData.getPartConfig(EnumParts.HEAD).setScale(1.0f, 1.0f, 1.0f);
			npc.ai.onAttack = 0;
			npc.ai.setWalkingSpeed(5);
		}
	}

	private void openGui(EntityPlayer player) {
		NoppesUtilServer.sendOpenGui(player, EnumGuiType.Companion, npc);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.setFromNBT(compound.getCompoundTag("CompanionInventory"));
		uuid = compound.getString("CompanionOwner");
		ownerName = compound.getString("CompanionOwnerName");
		companionID = compound.getInteger("CompanionID");
		stage = EnumCompanionStage.values()[compound.getInteger("CompanionStage")];
		currentExp = compound.getInteger("CompanionExp");
		canAge = compound.getBoolean("CompanionCanAge");
		ticksActive = compound.getLong("CompanionAge");
		hasInv = compound.getBoolean("CompanionHasInv");
		defendOwner = compound.getBoolean("CompanionDefendOwner");
		foodstats.readNBT(compound);
		NBTTagList list = compound.getTagList("CompanionTalents", 10);
		Map<EnumCompanionTalent, Integer> talents = new TreeMap<>();
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound c = list.getCompoundTagAt(i);
			EnumCompanionTalent talent = EnumCompanionTalent.values()[c.getInteger("Talent")];
			talents.put(talent, c.getInteger("Exp"));
		}
		this.talents = talents;
		setJob(compound.getInteger("CompanionJob"));
		if (jobInterface != null) {
			jobInterface.setNBT(compound.getCompoundTag("CompanionJobData"));
		}
		setStats();
	}

	public void setExp(EnumCompanionTalent talent, int exp) {
		talents.put(talent, exp);
	}

	private void setJob(int i) {
		job = EnumCompanionJobs.values()[i];
		if (job == EnumCompanionJobs.SHOP) {
			jobInterface = new CompanionTrader();
		} else if (job == EnumCompanionJobs.FARMER) {
			jobInterface = new CompanionFarmer();
		} else if (job == EnumCompanionJobs.GUARD) {
			jobInterface = new CompanionGuard();
		} else {
			jobInterface = null;
		}
		if (jobInterface != null) {
			jobInterface.npc = npc;
		}
	}

	public void setOwner(EntityPlayer player) {
		uuid = player.getUniqueID().toString();
	}

	public void setSelfsuficient(boolean bo) {
		if ((owner == null) || ((jobInterface != null) && (bo == jobInterface.isSelfSufficient()))) {
			return;
		}
		PlayerData data = PlayerDataController.instance.getPlayerData(owner);
		if (!bo && data.hasCompanion()) {
			return;
		}
		data.setCompanion(bo ? null : npc);
		if (job == EnumCompanionJobs.GUARD) {
			((CompanionGuard) jobInterface).isStanding = bo;
		} else if (job == EnumCompanionJobs.FARMER) {
			((CompanionFarmer) jobInterface).isStanding = bo;
		}
	}

	public void setSitting(boolean sit) {
		if (sit) {
			npc.ai.animationType = 1;
			npc.ai.onAttack = 3;
			npc.ai.setStartPos(new BlockPos(npc));
			npc.getNavigator().clearPathEntity();
			npc.setPositionAndUpdate(npc.getStartXPos(), npc.posY, npc.getStartZPos());
		} else {
			npc.ai.animationType = stage.animation;
			npc.ai.onAttack = 0;
		}
		npc.updateAI = true;
	}

	public void setStats() {
		IItemStack weapon = npc.inventory.getRightHand();
		npc.stats.melee.setStrength((int) (1.0 + getSwordDamage(weapon)));
		npc.stats.healthRegen = 0;
		npc.stats.combatRegen = 0;
		int ranged = getTalentLevel(EnumCompanionTalent.RANGED);
		if ((ranged > 0) && (weapon != null)) {
			Item item = weapon.getMCItemStack().getItem();
			if ((ranged > 0) && (item == Item.getItemFromBlock(Blocks.cobblestone))) {
				npc.inventory.setProjectile(weapon);
			}
			if ((ranged > 0) && (item instanceof ItemBow)) {
				npc.inventory.setProjectile(new ItemStackWrapper(new ItemStack(Items.arrow)));
			}
		}
		inventory.setSize(2 + (getTalentLevel(EnumCompanionTalent.INVENTORY) * 2));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("CompanionInventory", inventory.getToNBT());
		compound.setString("CompanionOwner", uuid);
		compound.setString("CompanionOwnerName", ownerName);
		compound.setInteger("CompanionID", companionID);
		compound.setInteger("CompanionStage", stage.ordinal());
		compound.setInteger("CompanionExp", currentExp);
		compound.setBoolean("CompanionCanAge", canAge);
		compound.setLong("CompanionAge", ticksActive);
		compound.setBoolean("CompanionHasInv", hasInv);
		compound.setBoolean("CompanionDefendOwner", defendOwner);
		foodstats.writeNBT(compound);
		compound.setInteger("CompanionJob", job.ordinal());
		if (jobInterface != null) {
			compound.setTag("CompanionJobData", jobInterface.getNBT());
		}
		NBTTagList list = new NBTTagList();
		for (EnumCompanionTalent talent : talents.keySet()) {
			NBTTagCompound c = new NBTTagCompound();
			c.setInteger("Talent", talent.ordinal());
			c.setInteger("Exp", talents.get(talent));
			list.appendTag(c);
		}
		compound.setTag("CompanionTalents", list);
		return compound;
	}
}
