//

//

package noppes.npcs.api.handler.data;

import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;

public interface IFaction {
	int getColor();

	int getDefaultPoints();

	int getId();

	String getName();

	boolean hostileToNpc(final ICustomNpc p0);

	int playerStatus(final IPlayer p0);
}
