//

//

package noppes.npcs.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IRecipeHandler;

public class HandlerEvent {
	public static class FactionsLoadedEvent extends Event {
		public IFactionHandler handler;

		public FactionsLoadedEvent(IFactionHandler handler) {
			this.handler = handler;
		}
	}

	public static class RecipesLoadedEvent extends Event {
		public IRecipeHandler handler;

		public RecipesLoadedEvent(IRecipeHandler handler) {
			this.handler = handler;
		}
	}
}
