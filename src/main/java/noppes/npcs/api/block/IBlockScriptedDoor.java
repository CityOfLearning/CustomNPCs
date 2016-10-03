package noppes.npcs.api.block;

import noppes.npcs.api.ITimers;
import noppes.npcs.api.block.IBlock;

public interface IBlockScriptedDoor extends IBlock {

   ITimers getTimers();

   boolean getOpen();

   void setOpen(boolean var1);

   void setBlockModel(String var1);

   String getBlockModel();
}
