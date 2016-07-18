//

//

package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class ItemScriptedDoor extends ItemDoor implements IPermission {
	public ItemScriptedDoor(final Block block) {
		super(block);
		maxStackSize = 1;
		setCreativeTab(CustomItems.tab);
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return e == EnumPacketServer.ScriptDoorDataSave;
	}

	@Override
	public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn,
			final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final boolean res = super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
		if (res && !worldIn.isRemote) {
			final BlockPos newPos = pos.up();
			NoppesUtilServer.sendOpenGui(playerIn, EnumGuiType.ScriptDoor, null, newPos.getX(), newPos.getY(),
					newPos.getZ());
			return true;
		}
		return res;
	}

	@Override
	public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
		return stack;
	}

	@Override
	public Item setUnlocalizedName(final String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
