//

//

package noppes.npcs.api.entity.data;

import noppes.npcs.api.entity.IPixelmon;

public interface IPixelmonPlayerData {
	int countPCPixelmon();

	IPixelmon getPartySlot(final int p0);
}
