package noppes.npcs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import noppes.npcs.NPCSpawning;
import noppes.npcs.client.AnalyticsTracking;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.entity.data.DataScenes;

public class ServerTickHandler {

   public int ticks = 0;
   private String serverName = null;


   @SubscribeEvent
   public void onServerTick(WorldTickEvent event) {
      if(event.phase == Phase.START) {
         NPCSpawning.findChunksForSpawning((WorldServer)event.world);
      }

   }

   @SubscribeEvent
   public void onServerTick(ServerTickEvent event) {
      if(event.phase == Phase.START && this.ticks++ >= 20) {
         SchematicController.Instance.updateBuilding();
         MassBlockController.Update();
         this.ticks = 0;
         Iterator var2 = DataScenes.StartedScenes.values().iterator();

         while(var2.hasNext()) {
            DataScenes.SceneState entry = (DataScenes.SceneState)var2.next();
            if(!entry.paused) {
               ++entry.ticks;
            }
         }

         var2 = DataScenes.ScenesToRun.iterator();

         while(var2.hasNext()) {
            DataScenes.SceneContainer var4 = (DataScenes.SceneContainer)var2.next();
            var4.update();
         }

         DataScenes.ScenesToRun = new ArrayList();
      }

   }

   @SubscribeEvent
   public void playerLogin(PlayerLoggedInEvent event) {
      if(this.serverName == null) {
         String e = "local";
         MinecraftServer server = MinecraftServer.getServer();
         if(server.isDedicatedServer()) {
            try {
               e = InetAddress.getByName(server.getServerHostname()).getCanonicalHostName();
            } catch (UnknownHostException var5) {
               e = MinecraftServer.getServer().getServerHostname();
            }

            if(server.getPort() != 25565) {
               e = e + ":" + server.getPort();
            }
         }

         if(e == null || e.startsWith("192.168") || e.contains("127.0.0.1") || e.startsWith("localhost")) {
            e = "local";
         }

         this.serverName = e;
      }

      AnalyticsTracking.sendData(event.player, "join", this.serverName);
   }
}
