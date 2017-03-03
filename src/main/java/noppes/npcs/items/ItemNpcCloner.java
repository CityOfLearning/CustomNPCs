
package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;
import noppes.npcs.util.NoppesUtilServer;

public class ItemNpcCloner extends Item implements IPermission {
	public ItemNpcCloner() {
		maxStackSize = 1;
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return 9127187;
	}

	@Override
	public boolean isAllowed(EnumPacketServer e) {
		return (e == EnumPacketServer.CloneList) || (e == EnumPacketServer.SpawnMob)
				|| (e == EnumPacketServer.MobSpawner) || (e == EnumPacketServer.ClonePreSave)
				|| (e == EnumPacketServer.CloneRemove) || (e == EnumPacketServer.CloneSave);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos,
			EnumFacing par7, float par8, float par9, float par10) {
		if (!par3World.isRemote) {
			NoppesUtilServer.sendOpenGui(par2EntityPlayer, EnumGuiType.MobSpawner, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
		return true;
	}

	@Override
	public Item setUnlocalizedName(String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
