
package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.util.NoppesUtilPlayer;

public class ContainerNPCTrader extends ContainerNpcInterface {
	public RoleTrader role;
	private EntityNPCInterface npc;

	public ContainerNPCTrader(EntityNPCInterface npc, EntityPlayer player) {
		super(player);
		this.npc = npc;
		role = (RoleTrader) npc.roleInterface;
		for (int i = 0; i < 18; ++i) {
			int x = 53;
			x += (i % 3) * 72;
			int y = 7;
			y += (i / 3) * 21;
			ItemStack item = role.inventoryCurrency.items.get(i);
			ItemStack item2 = role.inventoryCurrency.items.get(i + 18);
			if (item == null) {
				item = item2;
				item2 = null;
			}
			addSlotToContainer(new Slot(role.inventorySold, i, x, y));
		}
		for (int i2 = 0; i2 < 3; ++i2) {
			for (int l1 = 0; l1 < 9; ++l1) {
				addSlotToContainer(new Slot(player.inventory, l1 + (i2 * 9) + 9, 32 + (l1 * 18), 140 + (i2 * 18)));
			}
		}
		for (int j1 = 0; j1 < 9; ++j1) {
			addSlotToContainer(new Slot(player.inventory, j1, 32 + (j1 * 18), 198));
		}
	}

	public boolean canBuy(ItemStack currency, ItemStack currency2, EntityPlayer player) {
		if ((currency == null) && (currency2 == null)) {
			return true;
		}
		if (currency == null) {
			currency = currency2;
			currency2 = null;
		}
		if (NoppesUtilPlayer.compareItems(currency, currency2, role.ignoreDamage, role.ignoreNBT)) {
			ItemStack copy;
			currency = (copy = currency.copy());
			copy.stackSize += currency2.stackSize;
			currency2 = null;
		}
		if (currency2 == null) {
			return NoppesUtilPlayer.compareItems(player, currency, role.ignoreDamage, role.ignoreNBT);
		}
		return NoppesUtilPlayer.compareItems(player, currency, role.ignoreDamage, role.ignoreNBT)
				&& NoppesUtilPlayer.compareItems(player, currency2, role.ignoreDamage, role.ignoreNBT);
	}

	private boolean canGivePlayer(ItemStack item, EntityPlayer entityplayer) {
		ItemStack itemstack3 = entityplayer.inventory.getItemStack();
		if (itemstack3 == null) {
			return true;
		}
		if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
			int k1 = item.stackSize;
			if ((k1 > 0) && ((k1 + itemstack3.stackSize) <= itemstack3.getMaxStackSize())) {
				return true;
			}
		}
		return false;
	}

	private void givePlayer(ItemStack item, EntityPlayer entityplayer) {
		ItemStack itemstack3 = entityplayer.inventory.getItemStack();
		if (itemstack3 == null) {
			entityplayer.inventory.setItemStack(item);
		} else if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
			int k1 = item.stackSize;
			if ((k1 > 0) && ((k1 + itemstack3.stackSize) <= itemstack3.getMaxStackSize())) {
				ItemStack itemStack = itemstack3;
				itemStack.stackSize += k1;
			}
		}
	}

	@Override
	public ItemStack slotClick(int i, int j, int par3, EntityPlayer entityplayer) {
		if (par3 == 6) {
			par3 = 0;
		}
		if ((i < 0) || (i >= 18)) {
			return super.slotClick(i, j, par3, entityplayer);
		}
		if (j == 1) {
			return null;
		}
		Slot slot = inventorySlots.get(i);
		if ((slot == null) || (slot.getStack() == null)) {
			return null;
		}
		ItemStack item = slot.getStack();
		if (!canGivePlayer(item, entityplayer)) {
			return null;
		}
		ItemStack currency = role.inventoryCurrency.getStackInSlot(i);
		ItemStack currency2 = role.inventoryCurrency.getStackInSlot(i + 18);
		if (!canBuy(currency, currency2, entityplayer)) {
			return null;
		}
		RoleEvent.TraderEvent event = new RoleEvent.TraderEvent(entityplayer, npc.wrappedNPC, item, currency,
				currency2);
		if (EventHooks.onNPCRole(npc, event)) {
			return null;
		}
		if (event.currency1 != null) {
			currency = event.currency1.getMCItemStack();
		}
		if (event.currency2 != null) {
			currency2 = event.currency2.getMCItemStack();
		}
		if (!canBuy(currency, currency2, entityplayer)) {
			return null;
		}
		NoppesUtilPlayer.consumeItem(entityplayer, currency, role.ignoreDamage, role.ignoreNBT);
		NoppesUtilPlayer.consumeItem(entityplayer, currency2, role.ignoreDamage, role.ignoreNBT);
		ItemStack soldItem = null;
		if (event.sold != null) {
			soldItem = event.sold.getMCItemStack();
			givePlayer(soldItem, entityplayer);
		}
		return soldItem;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return null;
	}
}
