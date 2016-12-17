package noppes.npcs.blocks.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

public class TileBanner extends TileColorable {
	private ItemStack icon;
	private long time = 0L;

	public boolean canEdit() {
		return (System.currentTimeMillis() - time) < 10000L;
	}

	public ItemStack getIcon() {
		return icon;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
	}

	public long getTime() {
		return time;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		icon = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("BannerIcon"));
	}

	public void setIcon(ItemStack is) {
		icon = is;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public void setColor(int color) {
		super.setColor(color);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (icon != null) {
			compound.setTag("BannerIcon", icon.writeToNBT(new NBTTagCompound()));
		}
	}
}
