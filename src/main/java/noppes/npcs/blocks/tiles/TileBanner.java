//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

public class TileBanner extends TileColorable {
	public ItemStack icon;
	public long time;

	public TileBanner() {
		time = 0L;
	}

	public boolean canEdit() {
		return (System.currentTimeMillis() - time) < 10000L;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		icon = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("BannerIcon"));
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (icon != null) {
			compound.setTag("BannerIcon", icon.writeToNBT(new NBTTagCompound()));
		}
	}
}
