
package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.ai.roles.RoleFollower;
import noppes.npcs.entity.EntityNPCInterface;

public class ContainerNPCFollower extends ContainerNpcInterface {
	public InventoryNPC currencyMatrix;
	public RoleFollower role;

	public ContainerNPCFollower(EntityNPCInterface npc, EntityPlayer player) {
		super(player);
		role = (RoleFollower) npc.roleInterface;
		currencyMatrix = new InventoryNPC("currency", 1, this);
		addSlotToContainer(new SlotNpcMercenaryCurrency(role, currencyMatrix, 0, 26, 9));
		for (int j1 = 0; j1 < 9; ++j1) {
			addSlotToContainer(new Slot(player.inventory, j1, 8 + (j1 * 18), 142));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer) {
		super.onContainerClosed(entityplayer);
		if (!entityplayer.worldObj.isRemote) {
			ItemStack itemstack = currencyMatrix.removeStackFromSlot(0);
			if ((itemstack != null) && !entityplayer.worldObj.isRemote) {
				entityplayer.entityDropItem(itemstack, 0.0f);
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return null;
	}
}
