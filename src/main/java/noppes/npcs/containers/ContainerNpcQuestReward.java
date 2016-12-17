
package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.util.NoppesUtilServer;

public class ContainerNpcQuestReward extends Container {
	public ContainerNpcQuestReward(EntityPlayer player) {
		Quest quest = NoppesUtilServer.getEditingQuest(player);
		if (player.worldObj.isRemote) {
			quest = GuiNPCManageQuest.quest;
		}
		for (int l = 0; l < 3; ++l) {
			for (int k1 = 0; k1 < 3; ++k1) {
				addSlotToContainer(new Slot(quest.rewardItems, k1 + (l * 3), 105 + (k1 * 18), 17 + (l * 18)));
			}
		}
		for (int i1 = 0; i1 < 3; ++i1) {
			for (int l2 = 0; l2 < 9; ++l2) {
				addSlotToContainer(new Slot(player.inventory, l2 + (i1 * 9) + 9, 8 + (l2 * 18), 84 + (i1 * 18)));
			}
		}
		for (int j1 = 0; j1 < 9; ++j1) {
			addSlotToContainer(new Slot(player.inventory, j1, 8 + (j1 * 18), 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return null;
	}
}
