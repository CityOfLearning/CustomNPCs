package noppes.npcs.client;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.CustomNPCsScheduler;

public class Client {

   public static void sendData(final EnumPacketServer enu, final Object ... obs) {
      CustomNPCsScheduler.runTack(new Runnable() {
         public void run() {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

            try {
               if(!Server.fillBuffer(buffer, enu, obs)) {
                  return;
               }

               CustomNpcs.Channel.sendToServer(new FMLProxyPacket(buffer, "CustomNPCs"));
            } catch (IOException var3) {
               var3.printStackTrace();
            }

         }
      });
   }
}
