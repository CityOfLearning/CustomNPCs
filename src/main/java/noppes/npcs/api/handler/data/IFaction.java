package noppes.npcs.api.handler.data;

import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;

public interface IFaction {

   int getId();

   String getName();

   int getDefaultPoints();

   int getColor();

   int playerStatus(IPlayer var1);

   boolean hostileToNpc(ICustomNpc var1);
}
