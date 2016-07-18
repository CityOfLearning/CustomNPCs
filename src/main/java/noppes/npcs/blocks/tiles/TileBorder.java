//

//

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
	public Availability availability;
	public AxisAlignedBB boundingbox;
	public int rotation;
	public int height;
	public String message;

	public TileBorder() {
		availability = new Availability();
		rotation = 0;
		height = 10;
		message = "availability.areaNotAvailble";
	}

	@Override
	public boolean apply(final Object ob) {
		return isEntityApplicable((Entity) ob);
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Rotation", rotation);
		final S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	public boolean isEntityApplicable(final Entity var1) {
		return (var1 instanceof EntityPlayerMP) || (var1 instanceof EntityEnderPearl);
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		final NBTTagCompound compound = pkt.getNbtCompound();
		rotation = compound.getInteger("Rotation");
	}

	public void readExtraNBT(final NBTTagCompound compound) {
		availability.readFromNBT(compound.getCompoundTag("BorderAvailability"));
		rotation = compound.getInteger("BorderRotation");
		height = compound.getInteger("BorderHeight");
		message = compound.getString("BorderMessage");
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		readExtraNBT(compound);
		if (getWorld() != null) {
			getWorld().setBlockState(getPos(),
					CustomItems.border.getDefaultState().withProperty(BlockBorder.ROTATION, rotation));
		}
	}

	@Override
	public void update() {
		if (worldObj.isRemote) {
			return;
		}
		final AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1,
				pos.getY() + height + 1, pos.getZ() + 1);
		final List<Entity> list = worldObj.getEntitiesWithinAABB(Entity.class, box, this);
		for (final Entity entity : list) {
			if (entity instanceof EntityEnderPearl) {
				final EntityEnderPearl pearl = (EntityEnderPearl) entity;
				if (!(pearl.getThrower() instanceof EntityPlayer)
						|| availability.isAvailable((EntityPlayer) pearl.getThrower())) {
					continue;
				}
				entity.isDead = true;
			} else {
				final EntityPlayer player = (EntityPlayer) entity;
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

	public void writeExtraNBT(final NBTTagCompound compound) {
		compound.setTag("BorderAvailability", availability.writeToNBT(new NBTTagCompound()));
		compound.setInteger("BorderRotation", rotation);
		compound.setInteger("BorderHeight", height);
		compound.setString("BorderMessage", message);
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeExtraNBT(compound);
	}
}
