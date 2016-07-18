//

//

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class ContainerNPCFollower extends ContainerNpcInterface {
	public InventoryNPC currencyMatrix;
	public RoleFollower role;

	public ContainerNPCFollower(final EntityNPCInterface npc, final EntityPlayer player) {
		super(player);
		role = (RoleFollower) npc.roleInterface;
		currencyMatrix = new InventoryNPC("currency", 1, this);
		addSlotToContainer(new SlotNpcMercenaryCurrency(role, currencyMatrix, 0, 26, 9));
		for (int j1 = 0; j1 < 9; ++j1) {
			addSlotToContainer(new Slot(player.inventory, j1, 8 + (j1 * 18), 142));
		}
	}

	@Override
	public void onContainerClosed(final EntityPlayer entityplayer) {
		super.onContainerClosed(entityplayer);
		if (!entityplayer.worldObj.isRemote) {
			final ItemStack itemstack = currencyMatrix.removeStackFromSlot(0);
			if ((itemstack != null) && !entityplayer.worldObj.isRemote) {
				entityplayer.entityDropItem(itemstack, 0.0f);
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int i) {
		return null;
	}
}
