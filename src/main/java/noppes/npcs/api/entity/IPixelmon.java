package noppes.npcs.api.entity;

import net.minecraft.entity.passive.EntityTameable;
import noppes.npcs.api.entity.IAnimal;

public interface IPixelmon extends IAnimal {

   boolean getIsShiny();

   void setIsShiny(boolean var1);

   int getLevel();

   void setLevel(int var1);

   int getIV(int var1);

   void setIV(int var1, int var2);

   int getEV(int var1);

   void setEV(int var1, int var2);

   int getStat(int var1);

   void setStat(int var1, int var2);

   int getSize();

   void setSize(int var1);

   int getHapiness();

   void setHapiness(int var1);

   int getNature();

   void setNature(int var1);

   int getPokeball();

   void setPokeball(int var1);

   String getNickname();

   boolean hasNickname();

   void setNickname(String var1);

   String getMove(int var1);

   void setMove(int var1, String var2);

   EntityTameable getMCEntity();
}
