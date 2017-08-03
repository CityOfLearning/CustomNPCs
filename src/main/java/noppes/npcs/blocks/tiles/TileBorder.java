
package noppes.npcs.blocks.tiles;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ITickable;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.controllers.Availability;

public class TileBorder extends TileNpcEntity implements Predicate, ITickable {
	private Availability availability;
	private AxisAlignedBB boundingbox;
	private int rotation;
	private int height;
	private String message;

	public TileBorder() {
		availability = new Availability();
		rotation = 0;
		height = 10;
		message = "availability.areaNotAvailble";
	}

	@Override
	public boolean apply(Object ob) {
		return isEntityApplicable((Entity) ob);
	}

	public Availability getAvailability() {
		return availability;
	}

	public AxisAlignedBB getBoundingbox() {
		return boundingbox;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);
	}

	public int getHeight() {
		return height;
	}

	public String getMessage() {
		return message;
	}

	public int getRotation() {
		return rotation;
	}

	public boolean isEntityApplicable(Entity var1) {
		return (var1 instanceof EntityPlayerMP) || (var1 instanceof EntityEnderPearl);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		availability.readFromNBT(compound.getCompoundTag("BorderAvailability"));
		rotation = compound.getInteger("BorderRotation");
		height = compound.getInteger("BorderHeight");
		message = compound.getString("BorderMessage");
		if (getWorld() != null) {
			getWorld().setBlockState(getPos(),
					CustomItems.border.getDefaultState().withProperty(BlockBorder.ROTATION, rotation));
		}
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public void setBoundingbox(AxisAlignedBB boundingbox) {
		this.boundingbox = boundingbox;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	@Override
	public void update() {
		if (worldObj.isRemote) {
			return;
		}
		AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1,
				pos.getY() + height + 1, pos.getZ() + 1);
		List<Entity> list = worldObj.getEntitiesWithinAABB(Entity.class, box, this);
		for (Entity entity : list) {
			if (entity instanceof EntityEnderPearl) {
				EntityEnderPearl pearl = (EntityEnderPearl) entity;
				if (!(pearl.getThrower() instanceof EntityPlayer)
						|| availability.isAvailable((EntityPlayer) pearl.getThrower())) {
					continue;
				}
				entity.isDead = true;
			} else {
				EntityPlayer player = (EntityPlayer) entity;
				if (availability.isAvailable(player)) {
					continue;
				}
				BlockPos pos2 = new BlockPos(pos);
				if (rotation == 0) {
					pos2 = pos2.south();
				} else if (rotation == 2) {
					pos2 = pos2.north();
				} else if (rotation == 1) {
					pos2 = pos2.east();
				} else if (rotation == 3) {
					pos2 = pos2.west();
				}
				while (!worldObj.isAirBlock(pos2)) {
					pos2 = pos2.up();
				}
				player.setPositionAndUpdate(pos2.getX() + 0.5, pos2.getY(), pos2.getZ() + 0.5);
				if (message.isEmpty()) {
					continue;
				}
				player.addChatComponentMessage(new ChatComponentTranslation(message, new Object[0]));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("BorderAvailability", availability.writeToNBT(new NBTTagCompound()));
		compound.setInteger("BorderRotation", rotation);
		compound.setInteger("BorderHeight", height);
		compound.setString("BorderMessage", message);
	}
}
