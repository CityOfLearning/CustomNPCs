package noppes.npcs.blocks.tiles;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileWeaponRack extends TileNpcContainer {
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		compound.removeTag("ExtraData");
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public String getName() {
		return "tile.npcWeaponRack.name";
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack itemstack) {
		if ((itemstack != null) && ((itemstack.getItem() instanceof ItemBlock))) {
			return false;
		}
		return super.isItemValidForSlot(var1, itemstack);
	}

	@Override
	public int powerProvided() {
		int power = 0;
		for (int i = 0; i < 3; i++) {
			if (getStackInSlot(i) != null) {
				power += 5;
			}
		}
		return power;
	}
}
