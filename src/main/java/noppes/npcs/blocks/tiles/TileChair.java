package noppes.npcs.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityChairMount;

public class TileChair extends TileColorable {

	private EntityChairMount mount;

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);
	}

	public EntityChairMount getMount() {
		return mount;
	}

	public void killMount() {
		if ((mount != null) && !mount.isDead) {
			mount.setDead();
		}
	}

	public void mount(World world, BlockPos pos, EntityPlayer player) {
		if (world.isRemote) {
			return;
		}
		if ((mount == null) || mount.isDead) {
			mount = new EntityChairMount(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5D,
					((2 + getRotation()) % 4) * 90);
			world.spawnEntityInWorld(mount);
			world.markBlockForUpdate(pos);
			markDirty();
		}
		if (!mount.getPosition().equals(pos)) {
			mount.setPosition(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5D);
			mount.rotationYaw = ((2 + getRotation()) % 4) * 90;
			if (mount.getEntityWorld() == null) {
				world.spawnEntityInWorld(mount);
			}
			world.markBlockForUpdate(pos);
			markDirty();
		}
		if ((mount != null) && !mount.isDead && (mount instanceof EntityChairMount)) {
			player.mountEntity(mount);
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("entity")) {
			mount = new EntityChairMount(worldObj);
			mount.readEntityFromNBT(compound.getCompoundTag("entity"));
		}
	}

	public void setMount(EntityChairMount mount) {
		this.mount = mount;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (mount != null) {
			NBTTagCompound entityTag = new NBTTagCompound();
			mount.writeEntityToNBT(entityTag);
			compound.setTag("entity", entityTag);
		}
	}

}
