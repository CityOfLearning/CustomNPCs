package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.INPCRole;

public interface IRoleFollower extends INPCRole {

   int getDays();

   void addDays(int var1);

   boolean getInfinite();

   void setInfinite(boolean var1);

   boolean getGuiDisabled();

   void setGuiDisabled(boolean var1);

   boolean isFollowing();

   IPlayer getFollowing();

   void setFollowing(IPlayer var1);
}
