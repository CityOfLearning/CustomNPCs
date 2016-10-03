package noppes.npcs.api.block;

import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.block.IBlock;

public interface IBlockScripted extends IBlock {

   void setModel(IItemStack var1);

   void setModel(String var1);

   IItemStack getModel();

   ITimers getTimers();

   void setRedstonePower(int var1);

   int getRedstonePower();

   void setIsLadder(boolean var1);

   boolean getIsLadder();

   void setLight(int var1);

   int getLight();

   void setScale(float var1, float var2, float var3);

   float getScaleX();

   float getScaleY();

   float getScaleZ();

   void setRotation(int var1, int var2, int var3);

   int getRotationX();

   int getRotationY();

   int getRotationZ();

   void executeCommand(String var1);
}
