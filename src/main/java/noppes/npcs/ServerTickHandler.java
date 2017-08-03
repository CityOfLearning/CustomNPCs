
package noppes.npcs;

import java.util.ArrayList;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.entity.data.DataScenes;

public class ServerTickHandler {
	public int ticks;

	public ServerTickHandler() {
		ticks = 0;
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if ((event.phase == TickEvent.Phase.START) && (ticks++ >= 20)) {
			MassBlockController.Update();
			ticks = 0;
			for (DataScenes.SceneState state : DataScenes.StartedScenes.values()) {
				if (!state.paused) {
					DataScenes.SceneState sceneState = state;
					++sceneState.ticks;
				}
			}
			for (DataScenes.SceneContainer entry : DataScenes.ScenesToRun) {
				entry.update();
			}
			DataScenes.ScenesToRun = new ArrayList<>();
		}
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			NPCSpawning.findChunksForSpawning((WorldServer) event.world);
		}
	}
}
