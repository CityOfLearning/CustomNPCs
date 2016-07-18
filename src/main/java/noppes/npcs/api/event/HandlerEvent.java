//

//

package noppes.npcs.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IRecipeHandler;

public class HandlerEvent {
	public static class FactionsLoadedEvent extends Event {
		public final IFactionHandler handler;

		public FactionsLoadedEvent(final IFactionHandler handler) {
			this.handler = handler;
		}
	}

	public static class RecipesLoadedEvent extends Event {
		public final IRecipeHandler handler;

		public RecipesLoadedEvent(final IRecipeHandler handler) {
			this.handler = handler;
		}
	}
}
