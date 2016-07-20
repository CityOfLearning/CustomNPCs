//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;

public class TileDoor extends TileNpcEntity implements ITickable {
	public int ticksExisted;
	public Block blockModel;
	public boolean needsClientUpdate;
	public TileEntity renderTile;
	public boolean renderTileErrored;
	public ITickable renderTileUpdate;

	public TileDoor() {
		ticksExisted = 0;
		blockModel = CustomItems.scriptedDoor;
		needsClientUpdate = false;
		renderTileErrored = true;
		renderTileUpdate = null;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		getDoorNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	public NBTTagCompound getDoorNBT(NBTTagCompound compound) {
		compound.setString("ScriptDoorBlockModel", Block.blockRegistry.getNameForObject(blockModel) + "");
		return compound;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		setDoorNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setDoorNBT(compound);
	}

	public void setDoorNBT(NBTTagCompound compound) {
		blockModel = Block.blockRegistry.getObject(new ResourceLocation(compound.getString("ScriptDoorBlockModel")));
		if ((blockModel == null) || !(blockModel instanceof BlockDoor)) {
			blockModel = CustomItems.scriptedDoor;
		}
		renderTileUpdate = null;
		renderTile = null;
		renderTileErrored = false;
	}

	public void setItemModel(Block block) {
		if ((block == null) || !(block instanceof BlockDoor)) {
			block = CustomItems.scriptedDoor;
		}
		if (blockModel == block) {
			return;
		}
		blockModel = block;
		needsClientUpdate = true;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void update() {
		if (renderTileUpdate != null) {
			try {
				renderTileUpdate.update();
			} catch (Exception e) {
				renderTileUpdate = null;
			}
		}
		++ticksExisted;
		if (ticksExisted >= 10) {
			ticksExisted = 0;
			if (needsClientUpdate) {
				worldObj.markBlockForUpdate(pos);
				needsClientUpdate = false;
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		getDoorNBT(compound);
	}
}
