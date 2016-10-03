package noppes.npcs.api.wrapper;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.IPixelmon;
import noppes.npcs.api.wrapper.AnimalWrapper;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.util.ValueUtil;

public class PixelmonWrapper extends AnimalWrapper implements IPixelmon {

   private NBTTagCompound compound;


   public PixelmonWrapper(EntityTameable pixelmon) {
      super(pixelmon);
      this.compound = null;
      pixelmon.writeEntityToNBT(this.compound = new NBTTagCompound());
   }

   public PixelmonWrapper(EntityTameable pixelmon, NBTTagCompound compound) {
      this(pixelmon);
      this.compound = compound;
   }

   public boolean getIsShiny() {
      return this.compound.getBoolean("IsShiny");
   }

   public void setIsShiny(boolean bo) {
      this.compound.setBoolean("IsShiny", bo);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getLevel() {
      return this.compound.getInteger("Level");
   }

   public void setLevel(int level) {
      this.compound.setInteger("Level", level);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getIV(int type) {
      return type == 0?this.compound.getInteger("IVHP"):(type == 1?this.compound.getInteger("IVAttack"):(type == 2?this.compound.getInteger("IVDefence"):(type == 3?this.compound.getInteger("IVSpAtt"):(type == 4?this.compound.getInteger("IVSpDef"):(type == 5?this.compound.getInteger("IVSpeed"):-1)))));
   }

   public void setIV(int type, int value) {
      if(type == 0) {
         this.compound.setInteger("IVHP", value);
      } else if(type == 1) {
         this.compound.setInteger("IVAttack", value);
      } else if(type == 2) {
         this.compound.setInteger("IVDefence", value);
      } else if(type == 3) {
         this.compound.setInteger("IVSpAtt", value);
      } else if(type == 4) {
         this.compound.setInteger("IVSpDef", value);
      } else if(type == 5) {
         this.compound.setInteger("IVSpeed", value);
      }

      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getEV(int type) {
      return type == 0?this.compound.getInteger("EVHP"):(type == 1?this.compound.getInteger("EVAttack"):(type == 2?this.compound.getInteger("EVDefence"):(type == 3?this.compound.getInteger("EVSpecialAttack"):(type == 4?this.compound.getInteger("EVSpecialDefence"):(type == 5?this.compound.getInteger("EVSpeed"):-1)))));
   }

   public void setEV(int type, int value) {
      if(type == 0) {
         this.compound.setInteger("EVHP", value);
      } else if(type == 1) {
         this.compound.setInteger("EVAttack", value);
      } else if(type == 2) {
         this.compound.setInteger("EVDefence", value);
      } else if(type == 3) {
         this.compound.setInteger("EVSpecialAttack", value);
      } else if(type == 4) {
         this.compound.setInteger("EVSpecialDefence", value);
      } else if(type == 5) {
         this.compound.setInteger("EVSpeed", value);
      }

      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getStat(int type) {
      return type == 0?this.compound.getInteger("StatsHP"):(type == 1?this.compound.getInteger("StatsAttack"):(type == 2?this.compound.getInteger("StatsDefence"):(type == 3?this.compound.getInteger("StatsSpecialAttack"):(type == 4?this.compound.getInteger("StatsSpecialDefence"):(type == 5?this.compound.getInteger("StatsSpeed"):-1)))));
   }

   public void setStat(int type, int value) {
      if(type == 0) {
         this.compound.setInteger("StatsHP", value);
      } else if(type == 1) {
         this.compound.setInteger("StatsAttack", value);
      } else if(type == 2) {
         this.compound.setInteger("StatsDefence", value);
      } else if(type == 3) {
         this.compound.setInteger("StatsSpecialAttack", value);
      } else if(type == 4) {
         this.compound.setInteger("StatsSpecialDefence", value);
      } else if(type == 5) {
         this.compound.setInteger("StatsSpeed", value);
      }

      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getSize() {
      return this.compound.getShort("Growth");
   }

   public void setSize(int type) {
      this.compound.setShort("Growth", (short)type);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getHapiness() {
      return this.compound.getInteger("Friendship");
   }

   public void setHapiness(int value) {
      value = ValueUtil.CorrectInt(value, 0, 255);
      this.compound.setInteger("Friendship", value);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getNature() {
      return this.compound.getShort("Nature");
   }

   public void setNature(int type) {
      this.compound.setShort("Nature", (short)type);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public int getPokeball() {
      return this.compound.hasKey("CaughtBall")?-1:this.compound.getInteger("CaughtBall");
   }

   public void setPokeball(int type) {
      this.compound.setInteger("CaughtBall", type);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public String getNickname() {
      return this.compound.getString("Nickname");
   }

   public boolean hasNickname() {
      return !this.getNickname().isEmpty();
   }

   public void setNickname(String name) {
      this.compound.setString("Nickname", name);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }

   public String getMove(int slot) {
      return !this.compound.hasKey("PixelmonMoveID" + slot)?null:PixelmonHelper.getAttackName(this.compound.getInteger("PixelmonMoveID" + slot));
   }

   public void setMove(int slot, String move) {
      slot = ValueUtil.CorrectInt(slot, 0, 3);
      int id = PixelmonHelper.getAttackID(move);
      this.compound.removeTag("PixelmonMovePP" + slot);
      this.compound.removeTag("PixelmonMovePPBase" + slot);
      if(id < 0) {
         this.compound.removeTag("PixelmonMoveID" + slot);
      } else {
         this.compound.setInteger("PixelmonMoveID" + slot, id);
      }

      int size = 0;

      for(int i = 0; i < 4; ++i) {
         if(this.compound.hasKey("PixelmonMoveID" + i)) {
            ++size;
         }
      }

      this.compound.setInteger("PixelmonNumberMoves", size);
      ((EntityTameable)this.entity).readEntityFromNBT(this.compound);
   }
}
