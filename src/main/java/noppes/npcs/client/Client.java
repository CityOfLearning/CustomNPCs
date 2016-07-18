//

//

package noppes.npcs.client;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.CustomNPCsScheduler;

public class Client {
	public static void sendData(final EnumPacketServer enu, final Object... obs) {
		CustomNPCsScheduler.runTack(new Runnable() {
			@Override
			public void run() {
				final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
				try {
					if (!Server.fillBuffer((ByteBuf) buffer, enu, obs)) {
						return;
					}
					CustomNpcs.Channel.sendToServer(new FMLProxyPacket(buffer, "CustomNPCs"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
