//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TilePedestal extends TileNpcContainer {
	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		compound.removeTag("ExtraData");
		final S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public String getName() {
		return "tile.npcPedestal.name";
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public int powerProvided() {
		return (getStackInSlot(0) == null) ? 0 : 15;
	}
}
