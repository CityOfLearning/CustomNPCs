//

//

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;

public class ContainerCarpentryBench extends Container {
	public InventoryCrafting craftMatrix;
	public IInventory craftResult;
	private EntityPlayer player;
	private World worldObj;
	private BlockPos pos;

	public ContainerCarpentryBench(final InventoryPlayer par1InventoryPlayer, final World par2World,
			final BlockPos pos) {
		craftMatrix = new InventoryCrafting(this, 4, 4);
		craftResult = new InventoryCraftResult();
		worldObj = par2World;
		this.pos = pos;
		player = par1InventoryPlayer.player;
		addSlotToContainer(new SlotNpcCrafting(par1InventoryPlayer.player, craftMatrix, craftResult, 0, 133, 41));
		for (int var6 = 0; var6 < 4; ++var6) {
			for (int var7 = 0; var7 < 4; ++var7) {
				addSlotToContainer(new Slot(craftMatrix, var7 + (var6 * 4), 17 + (var7 * 18), 14 + (var6 * 18)));
			}
		}
		for (int var6 = 0; var6 < 3; ++var6) {
			for (int var7 = 0; var7 < 9; ++var7) {
				addSlotToContainer(
						new Slot(par1InventoryPlayer, var7 + (var6 * 9) + 9, 8 + (var7 * 18), 98 + (var6 * 18)));
			}
		}
		for (int var6 = 0; var6 < 9; ++var6) {
			addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + (var6 * 18), 156));
		}
		onCraftMatrixChanged(craftMatrix);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
		return (worldObj.getBlockState(pos).getBlock() == CustomItems.carpentyBench)
				&& (par1EntityPlayer.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0);
	}

	@Override
	public boolean canMergeSlot(final ItemStack p_94530_1_, final Slot p_94530_2_) {
		return (p_94530_2_.inventory != craftResult) && super.canMergeSlot(p_94530_1_, p_94530_2_);
	}

	public int getType() {
		return worldObj.getBlockState(pos).getValue(BlockCarpentryBench.TYPE);
	}

	@Override
	public void onContainerClosed(final EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		if (!worldObj.isRemote) {
			for (int var2 = 0; var2 < 16; ++var2) {
				final ItemStack var3 = craftMatrix.removeStackFromSlot(var2);
				if (var3 != null) {
					par1EntityPlayer.dropPlayerItemWithRandomChoice(var3, false);
				}
			}
		}
	}

	@Override
	public void onCraftMatrixChanged(final IInventory par1IInventory) {
		if (!worldObj.isRemote) {
			final RecipeCarpentry recipe = RecipeController.instance.findMatchingRecipe(craftMatrix);
			ItemStack item = null;
			if ((recipe != null) && recipe.availability.isAvailable(player)) {
				item = recipe.getCraftingResult(craftMatrix);
			}
			craftResult.setInventorySlotContents(0, item);
			final EntityPlayerMP plmp = (EntityPlayerMP) player;
			plmp.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(windowId, 0, item));
		}
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par1) {
		ItemStack var2 = null;
		final Slot var3 = inventorySlots.get(par1);
		if ((var3 != null) && var3.getHasStack()) {
			final ItemStack var4 = var3.getStack();
			var2 = var4.copy();
			if (par1 == 0) {
				if (!mergeItemStack(var4, 17, 53, true)) {
					return null;
				}
				var3.onSlotChange(var4, var2);
			} else if ((par1 >= 17) && (par1 < 44)) {
				if (!mergeItemStack(var4, 44, 53, false)) {
					return null;
				}
			} else if ((par1 >= 44) && (par1 < 53)) {
				if (!mergeItemStack(var4, 17, 44, false)) {
					return null;
				}
			} else if (!mergeItemStack(var4, 17, 53, false)) {
				return null;
			}
			if (var4.stackSize == 0) {
				var3.putStack((ItemStack) null);
			} else {
				var3.onSlotChanged();
			}
			if (var4.stackSize == var2.stackSize) {
				return null;
			}
			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}
		return var2;
	}
}
