//

//

package noppes.npcs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import noppes.npcs.client.AnalyticsTracking;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.entity.data.DataScenes;

public class ServerTickHandler {
	public int ticks;
	private String serverName;

	public ServerTickHandler() {
		ticks = 0;
		serverName = null;
	}

	@SubscribeEvent
	public void onServerTick(final TickEvent.ServerTickEvent event) {
		if ((event.phase == TickEvent.Phase.START) && (ticks++ >= 20)) {
			SchematicController.Instance.updateBuilding();
			MassBlockController.Update();
			ticks = 0;
			for (final DataScenes.SceneState state : DataScenes.StartedScenes.values()) {
				if (!state.paused) {
					final DataScenes.SceneState sceneState = state;
					++sceneState.ticks;
				}
			}
			for (final DataScenes.SceneContainer entry : DataScenes.ScenesToRun) {
				entry.update();
			}
			DataScenes.ScenesToRun = new ArrayList<DataScenes.SceneContainer>();
		}
	}

	@SubscribeEvent
	public void onServerTick(final TickEvent.WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			NPCSpawning.findChunksForSpawning((WorldServer) event.world);
		}
	}

	@SubscribeEvent
	public void playerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
		if (serverName == null) {
			String e = "local";
			final MinecraftServer server = MinecraftServer.getServer();
			if (server.isDedicatedServer()) {
				try {
					e = InetAddress.getByName(server.getServerHostname()).getCanonicalHostName();
				} catch (UnknownHostException e2) {
					e = MinecraftServer.getServer().getServerHostname();
				}
				if (server.getPort() != 25565) {
					e = e + ":" + server.getPort();
				}
			}
			if ((e == null) || e.startsWith("192.168") || e.contains("127.0.0.1") || e.startsWith("localhost")) {
				e = "local";
			}
			serverName = e;
		}
		AnalyticsTracking.sendData(event.player, "join", serverName);
	}
}
