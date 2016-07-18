//

//

package noppes.npcs.api.handler;

import java.util.List;

import noppes.npcs.api.handler.data.IFaction;

public interface IFactionHandler {
	IFaction create(final String p0, final int p1);

	IFaction delete(final int p0);

	List<IFaction> list();
}
