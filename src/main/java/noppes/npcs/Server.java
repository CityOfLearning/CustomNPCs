//

//

package noppes.npcs;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.util.CustomNPCsScheduler;

public class Server {
	public static boolean fillBuffer(final ByteBuf buffer, final Enum enu, final Object... obs) throws IOException {
		buffer.writeInt(enu.ordinal());
		for (final Object ob : obs) {
			if (ob != null) {
				if (ob instanceof Map) {
					final Map<String, Integer> map = (Map<String, Integer>) ob;
					buffer.writeInt(map.size());
					for (final String key : map.keySet()) {
						final int value = map.get(key);
						buffer.writeInt(value);
						writeString(buffer, key);
					}
				} else if (ob instanceof MerchantRecipeList) {
					((MerchantRecipeList) ob).writeToBuf(new PacketBuffer(buffer));
				} else if (ob instanceof List) {
					final List<String> list = (List<String>) ob;
					buffer.writeInt(list.size());
					for (final String s : list) {
						writeString(buffer, s);
					}
				} else if (ob instanceof Enum) {
					buffer.writeInt(((Enum) ob).ordinal());
				} else if (ob instanceof Integer) {
					buffer.writeInt((Integer) ob);
				} else if (ob instanceof Boolean) {
					buffer.writeBoolean((Boolean) ob);
				} else if (ob instanceof String) {
					writeString(buffer, (String) ob);
				} else if (ob instanceof Float) {
					buffer.writeFloat((Float) ob);
				} else if (ob instanceof Long) {
					buffer.writeLong((Long) ob);
				} else if (ob instanceof Double) {
					buffer.writeDouble((Double) ob);
				} else if (ob instanceof NBTTagCompound) {
					writeNBT(buffer, (NBTTagCompound) ob);
				}
			}
		}
		if (buffer.array().length >= 32767) {
			LogWriter.error("Packet " + enu + " was too big to be send");
			return false;
		}
		return true;
	}

	public static NBTTagCompound readNBT(final ByteBuf buffer) throws IOException {
		final byte[] bytes = new byte[buffer.readShort()];
		buffer.readBytes(bytes);
		final DataInputStream datainputstream = new DataInputStream(
				new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes))));
		try {
			return CompressedStreamTools.read(datainputstream, new NBTSizeTracker(2097152L));
		} finally {
			datainputstream.close();
		}
	}

	public static String readString(final ByteBuf buffer) {
		try {
			final byte[] bytes = new byte[buffer.readShort()];
			buffer.readBytes(bytes);
			return new String(bytes, Charsets.UTF_8);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}

	public static void sendAssociatedData(final Entity entity, final EnumPacketClient enu, final Object... obs) {
		final List<EntityPlayerMP> list = entity.worldObj.getEntitiesWithinAABB((Class) EntityPlayerMP.class,
				entity.getEntityBoundingBox().expand(160.0, 160.0, 160.0));
		if (list.isEmpty()) {
			return;
		}
		CustomNPCsScheduler.runTack(new Runnable() {
			@Override
			public void run() {
				final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
				try {
					if (!Server.fillBuffer((ByteBuf) buffer, enu, obs)) {
						return;
					}
					for (final EntityPlayerMP player : list) {
						CustomNpcs.Channel.sendTo(new FMLProxyPacket(buffer, "CustomNPCs"), player);
					}
				} catch (IOException e) {
					LogWriter.except(e);
				}
			}
		});
	}

	public static void sendData(final EntityPlayerMP player, final EnumPacketClient enu, final Object... obs) {
		sendDataDelayed(player, enu, 0, obs);
	}

	public static boolean sendDataChecked(final EntityPlayerMP player, final EnumPacketClient enu,
			final Object... obs) {
		final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		try {
			if (!fillBuffer((ByteBuf) buffer, enu, obs)) {
				return false;
			}
			CustomNpcs.Channel.sendTo(new FMLProxyPacket(buffer, "CustomNPCs"), player);
		} catch (IOException e) {
			LogWriter.except(e);
		}
		return true;
	}

	public static void sendDataDelayed(final EntityPlayerMP player, final EnumPacketClient enu, final int delay,
			final Object... obs) {
		CustomNPCsScheduler.runTack(new Runnable() {
			@Override
			public void run() {
				final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
				try {
					if (!Server.fillBuffer((ByteBuf) buffer, enu, obs)) {
						return;
					}
					CustomNpcs.Channel.sendTo(new FMLProxyPacket(buffer, "CustomNPCs"), player);
				} catch (IOException e) {
					LogWriter.except(e);
				}
			}
		}, delay);
	}

	public static void sendToAll(final EnumPacketClient enu, final Object... obs) {
		final List<EntityPlayerMP> list = new ArrayList<EntityPlayerMP>(
				MinecraftServer.getServer().getConfigurationManager().playerEntityList);
		CustomNPCsScheduler.runTack(new Runnable() {
			@Override
			public void run() {
				final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
				try {
					if (!Server.fillBuffer((ByteBuf) buffer, enu, obs)) {
						return;
					}
					for (final EntityPlayerMP player : list) {
						CustomNpcs.Channel.sendTo(new FMLProxyPacket(buffer, "CustomNPCs"), player);
					}
				} catch (IOException e) {
					LogWriter.except(e);
				}
			}
		});
	}

	public static void writeNBT(final ByteBuf buffer, final NBTTagCompound compound) throws IOException {
		final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		final DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));
		try {
			CompressedStreamTools.write(compound, dataoutputstream);
		} finally {
			dataoutputstream.close();
		}
		final byte[] bytes = bytearrayoutputstream.toByteArray();
		buffer.writeShort((short) bytes.length);
		buffer.writeBytes(bytes);
	}

	public static void writeString(final ByteBuf buffer, final String s) {
		final byte[] bytes = s.getBytes(Charsets.UTF_8);
		buffer.writeShort((short) bytes.length);
		buffer.writeBytes(bytes);
	}
}
