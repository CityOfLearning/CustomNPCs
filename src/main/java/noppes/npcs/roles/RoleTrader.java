
package noppes.npcs.roles;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.entity.data.role.IRoleTrader;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTJsonUtil;

public class RoleTrader extends RoleInterface implements IRoleTrader {
	private static File getFile(String name) {
		File dir = new File(CustomNpcs.getWorldSaveDirectory(), "markets");
		if (!dir.exists()) {
			dir.mkdir();
		}
		return new File(dir, name.toLowerCase() + ".json");
	}

	public static void load(RoleTrader role, String name) {
		if (role.npc.worldObj.isRemote) {
			return;
		}
		File file = getFile(name);
		if (!file.exists()) {
			return;
		}
		try {
			role.readNBT(NBTJsonUtil.LoadFile(file));
		} catch (Exception ex) {
		}
	}

	public static void save(RoleTrader r, String name) {
		if (name.isEmpty()) {
			return;
		}
		File file = getFile(name + "_new");
		File file2 = getFile(name);
		try {
			NBTJsonUtil.SaveFile(file, r.writeNBT(new NBTTagCompound()));
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
		} catch (Exception ex) {
		}
	}

	public static void setMarket(EntityNPCInterface npc, String marketName) {
		if (marketName.isEmpty()) {
			return;
		}
		if (!getFile(marketName).exists()) {
			save((RoleTrader) npc.roleInterface, marketName);
		}
		load((RoleTrader) npc.roleInterface, marketName);
	}

	public String marketName;
	public NpcMiscInventory inventoryCurrency;

	public NpcMiscInventory inventorySold;

	public boolean ignoreDamage;

	public boolean ignoreNBT;

	public boolean toSave;

	public RoleTrader(EntityNPCInterface npc) {
		super(npc);
		marketName = "";
		ignoreDamage = false;
		ignoreNBT = false;
		toSave = false;
		inventoryCurrency = new NpcMiscInventory(36);
		inventorySold = new NpcMiscInventory(18);
	}

	@Override
	public IItemStack getCurrency1(int slot) {
		ItemStack item = inventoryCurrency.getStackInSlot(slot);
		if (item == null) {
			return null;
		}
		return new ItemStackWrapper(item);
	}

	@Override
	public IItemStack getCurrency2(int slot) {
		ItemStack item = inventoryCurrency.getStackInSlot(slot + 18);
		if (item == null) {
			return null;
		}
		return new ItemStackWrapper(item);
	}

	@Override
	public String getMarket() {
		return marketName;
	}

	@Override
	public IItemStack getSold(int slot) {
		ItemStack item = inventorySold.getStackInSlot(slot);
		if (item == null) {
			return null;
		}
		return new ItemStackWrapper(item);
	}

	public boolean hasCurrency(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}
		for (ItemStack item : inventoryCurrency.items.values()) {
			if ((item != null) && NoppesUtilPlayer.compareItems(item, itemstack, ignoreDamage, ignoreNBT)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void interact(EntityPlayer player) {
		npc.say(player, npc.advanced.getInteractLine());
		try {
			load(this, marketName);
		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		}
		NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTrader, npc);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		marketName = nbttagcompound.getString("TraderMarket");
		readNBT(nbttagcompound);
	}

	public void readNBT(NBTTagCompound nbttagcompound) {
		inventoryCurrency.setFromNBT(nbttagcompound.getCompoundTag("TraderCurrency"));
		inventorySold.setFromNBT(nbttagcompound.getCompoundTag("TraderSold"));
		ignoreDamage = nbttagcompound.getBoolean("TraderIgnoreDamage");
		ignoreNBT = nbttagcompound.getBoolean("TraderIgnoreNBT");
	}

	@Override
	public void remove(int slot) {
		if ((slot >= 18) || (slot < 0)) {
			throw new CustomNPCsException("Invalid slot: " + slot, new Object[0]);
		}
		inventoryCurrency.items.remove(slot);
		inventoryCurrency.items.remove(slot + 18);
		inventorySold.items.remove(slot);
	}

	@Override
	public void set(int slot, IItemStack currency, IItemStack currency2, IItemStack sold) {
		if (sold == null) {
			throw new CustomNPCsException("Sold item was null", new Object[0]);
		}
		if ((slot >= 18) || (slot < 0)) {
			throw new CustomNPCsException("Invalid slot: " + slot, new Object[0]);
		}
		if (currency == null) {
			currency = currency2;
			currency2 = null;
		}
		if (currency != null) {
			inventoryCurrency.items.put(slot, currency.getMCItemStack());
		} else {
			inventoryCurrency.items.remove(slot);
		}
		if (currency2 != null) {
			inventoryCurrency.items.put(slot + 18, currency2.getMCItemStack());
		} else {
			inventoryCurrency.items.remove(slot + 18);
		}
		inventorySold.items.put(slot, sold.getMCItemStack());
	}

	@Override
	public void setMarket(String name) {
		load(this, marketName = name);
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("TraderCurrency", inventoryCurrency.getToNBT());
		nbttagcompound.setTag("TraderSold", inventorySold.getToNBT());
		nbttagcompound.setBoolean("TraderIgnoreDamage", ignoreDamage);
		nbttagcompound.setBoolean("TraderIgnoreNBT", ignoreNBT);
		return nbttagcompound;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("TraderMarket", marketName);
		writeNBT(nbttagcompound);
		if (toSave && !npc.isRemote()) {
			save(this, marketName);
		}
		toSave = false;
		return nbttagcompound;
	}
}
