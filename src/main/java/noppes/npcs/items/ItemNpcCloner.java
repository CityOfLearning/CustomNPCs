//

//

package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

public class ItemNpcCloner extends Item implements IPermission {
	public ItemNpcCloner() {
		maxStackSize = 1;
		setCreativeTab(CustomItems.tab);
	}

	@Override
	public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
		return 9127187;
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return (e == EnumPacketServer.CloneList) || (e == EnumPacketServer.SpawnMob)
				|| (e == EnumPacketServer.MobSpawner) || (e == EnumPacketServer.ClonePreSave)
				|| (e == EnumPacketServer.CloneRemove) || (e == EnumPacketServer.CloneSave);
	}

	@Override
	public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World,
			final BlockPos pos, final EnumFacing par7, final float par8, final float par9, final float par10) {
		if (!par3World.isRemote) {
			NoppesUtilServer.sendOpenGui(par2EntityPlayer, EnumGuiType.MobSpawner, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
		return true;
	}

	@Override
	public Item setUnlocalizedName(final String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
