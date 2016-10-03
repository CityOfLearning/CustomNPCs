package noppes.npcs.controllers;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import noppes.npcs.LogWriter;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobSpawner;
import org.apache.logging.log4j.LogManager;

public class PixelmonHelper {

   public static boolean Enabled = false;
   private static Object PokeballManager = null;
   private static Method getPlayerStorage = null;
   private static Object ComputerManager = null;
   private static Method getPlayerComputerStorage = null;
   private static Constructor attackByID = null;
   private static Constructor attackByName = null;
   private static Field baseAttack = null;
   private static Field getAttackID = null;
   private static Field getAttackName = null;
   private static Method getPixelmonModel = null;


   public static void load() {
      Enabled = Loader.isModLoaded("pixelmon");
      if(Enabled) {
         try {
            Class e = Class.forName("com.pixelmonmod.pixelmon.storage.PixelmonStorage");
            PokeballManager = e.getField("PokeballManager").get((Object)null);
            ComputerManager = e.getField("ComputerManager").get((Object)null);
            getPlayerStorage = PokeballManager.getClass().getMethod("getPlayerStorage", new Class[]{EntityPlayerMP.class});
            getPlayerComputerStorage = ComputerManager.getClass().getMethod("getPlayerStorage", new Class[]{EntityPlayerMP.class});
            e = Class.forName("com.pixelmonmod.pixelmon.battles.attacks.Attack");
            attackByID = e.getConstructor(new Class[]{Integer.TYPE});
            attackByName = e.getConstructor(new Class[]{String.class});
            baseAttack = e.getField("baseAttack");
            e = Class.forName("com.pixelmonmod.pixelmon.battles.attacks.AttackBase");
            getAttackID = e.getField("attackIndex");
            getAttackName = e.getDeclaredField("attackName");
            getAttackName.setAccessible(true);
            e = Class.forName("com.pixelmonmod.pixelmon.entities.pixelmon.Entity2HasModel");
            getPixelmonModel = e.getMethod("getModel", new Class[0]);
         } catch (Exception var1) {
            LogWriter.except(var1);
         }

      }
   }

   public static List getPixelmonList() {
      ArrayList list = new ArrayList();
      if(!Enabled) {
         return list;
      } else {
         try {
            Class e = Class.forName("com.pixelmonmod.pixelmon.enums.EnumPokemon");
            Object[] array = e.getEnumConstants();
            Object[] var3 = array;
            int var4 = array.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Object ob = var3[var5];
               list.add(ob.toString());
            }
         } catch (Exception var7) {
            LogManager.getLogger().error("getPixelmonList", var7);
         }

         return list;
      }
   }

   public static boolean isPixelmon(Entity entity) {
      if(!Enabled) {
         return false;
      } else {
         String s = EntityList.getEntityString(entity);
         return s == null?false:s.equals("pixelmon.Pixelmon");
      }
   }

   public static void setName(EntityLivingBase entity, String name) {
      if(Enabled && isPixelmon(entity)) {
         try {
            Method e = entity.getClass().getMethod("init", new Class[]{String.class});
            e.invoke(entity, new Object[]{name});
            Class c = Class.forName("com.pixelmonmod.pixelmon.entities.pixelmon.Entity2HasModel");
            e = c.getDeclaredMethod("loadModel", new Class[0]);
            e.setAccessible(true);
            e.invoke(entity, new Object[0]);
         } catch (Exception var4) {
            LogManager.getLogger().error("setName", var4);
         }

      }
   }

   public static String getName(EntityLivingBase entity) {
      if(Enabled && isPixelmon(entity)) {
         try {
            Method e = entity.getClass().getMethod("getName", new Class[0]);
            return e.invoke(entity, new Object[0]).toString();
         } catch (Exception var2) {
            LogManager.getLogger().error("getName", var2);
            return "";
         }
      } else {
         return "";
      }
   }

   public static Object getModel(EntityLivingBase entity) {
      try {
         return getPixelmonModel.invoke(entity, new Object[0]);
      } catch (Exception var2) {
         LogManager.getLogger().error("getModel", var2);
         return null;
      }
   }

   public static void debug(EntityLivingBase entity) {
      if(Enabled && isPixelmon(entity)) {
         try {
            Method e = entity.getClass().getMethod("getModel", new Class[0]);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((String)e.invoke(entity, new Object[0])));
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }
   }

   public static boolean isTrainer(EntityLivingBase entity) {
      if(!Enabled) {
         return false;
      } else {
         String s = EntityList.getEntityString(entity);
         return s == null?false:s.equals("pixelmon.Trainer");
      }
   }

   public static boolean isBattling(EntityPlayerMP player) {
      if(!Enabled) {
         return false;
      } else {
         try {
            Class e = Class.forName("com.pixelmonmod.pixelmon.battles.BattleRegistry");
            Method m = e.getMethod("getBattle", new Class[]{EntityPlayer.class});
            return m.invoke((Object)null, new Object[]{player}) == null;
         } catch (Exception var3) {
            LogManager.getLogger().error("canBattle", var3);
            return false;
         }
      }
   }

   public static boolean isBattling(EntityLivingBase trainer) {
      if(Enabled && isTrainer(trainer)) {
         try {
            Field e = trainer.getClass().getField("battleController");
            return e.get(trainer) != null;
         } catch (Exception var2) {
            LogManager.getLogger().error("canBattle", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean canBattle(EntityPlayerMP player, EntityNPCInterface npc) {
      if(Enabled && npc.advanced.job == 6 && !isBattling(player)) {
         try {
            JobSpawner e = (JobSpawner)npc.jobInterface;
            if(e.isOnCooldown(player.getName())) {
               return false;
            } else {
               Object ob = getPlayerStorage.invoke(PokeballManager, new Object[]{player});
               Method m = ob.getClass().getMethod("countAblePokemon", new Class[0]);
               return ((Integer)m.invoke(ob, new Object[0])).intValue() != 0;
            }
         } catch (Exception var5) {
            LogManager.getLogger().error("canBattle", var5);
            return false;
         }
      } else {
         return false;
      }
   }

   public static EntityTameable pixelmonFromNBT(NBTTagCompound compound, EntityPlayer player) {
      if(!Enabled) {
         return null;
      } else {
         try {
            Object e = getPlayerStorage.invoke(PokeballManager, new Object[]{player});
            return (EntityTameable)e.getClass().getMethod("sendOut", new Class[]{NBTTagCompound.class, World.class}).invoke(e, new Object[]{compound, player});
         } catch (Exception var3) {
            var3.printStackTrace();
            return null;
         }
      }
   }

   public static NBTTagCompound getPartySlot(int i, EntityPlayer player) {
      if(!Enabled) {
         return null;
      } else {
         try {
            Object e = getPlayerStorage.invoke(PokeballManager, new Object[]{player});
            NBTTagCompound[] party = (NBTTagCompound[])((NBTTagCompound[])e.getClass().getFields()[0].get(e));
            return party[i];
         } catch (Exception var4) {
            var4.printStackTrace();
            return null;
         }
      }
   }

   public static boolean startBattle(EntityPlayerMP player, EntityLivingBase trainer) {
      if(!Enabled) {
         return false;
      } else {
         try {
            Object e = getPlayerStorage.invoke(PokeballManager, new Object[]{player});
            Class c = e.getClass();
            Method m = c.getMethod("getFirstAblePokemon", new Class[]{World.class});
            Entity pixelmon = (Entity)m.invoke(e, new Object[]{player.worldObj});
            Class cEntity = Class.forName("com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon");
            m = c.getMethod("EntityAlreadyExists", new Class[]{cEntity});
            if(!((Boolean)m.invoke(e, new Object[]{pixelmon})).booleanValue()) {
               m = cEntity.getMethod("releaseFromPokeball", new Class[0]);
               pixelmon.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, 0.0F);
            }

            c = Class.forName("com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant");
            Object parTrainer = c.getConstructor(new Class[]{trainer.getClass(), EntityPlayer.class, Integer.TYPE}).newInstance(new Object[]{trainer, player, Integer.valueOf(1)});
            Object[] pixelmonArray = (Object[])((Object[])Array.newInstance(cEntity, 1));
            pixelmonArray[0] = pixelmon;
            c = Class.forName("com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant");
            Object parPlayer = c.getConstructor(new Class[]{EntityPlayerMP.class, pixelmonArray.getClass()}).newInstance(new Object[]{player, pixelmonArray});
            cEntity = Class.forName("com.pixelmonmod.pixelmon.entities.pixelmon.Entity6CanBattle");
            c = Class.forName("com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant");
            m = cEntity.getMethod("StartBattle", new Class[]{c, c});
            m.invoke(pixelmon, new Object[]{parTrainer, parPlayer});
            return true;
         } catch (Exception var10) {
            LogManager.getLogger().error("startBattle", var10);
            return false;
         }
      }
   }

   public static int countPCPixelmon(EntityPlayerMP player) {
      try {
         Object e = getPlayerComputerStorage.invoke(player, new Object[0]);
         return ((Integer)e.getClass().getMethod("count", new Class[0]).invoke(e, new Object[0])).intValue();
      } catch (Exception var2) {
         var2.printStackTrace();
         return 0;
      }
   }

   public static String getAttackName(int id) {
      try {
         Object e = attackByID.newInstance(new Object[]{Integer.valueOf(id)});
         return e == null?null:getAttackName.get(baseAttack.get(e)) + "";
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static int getAttackID(String name) {
      try {
         Object e = attackByName.newInstance(new Object[]{name});
         return e == null?-1:getAttackName.getInt(baseAttack.get(e));
      } catch (Exception var2) {
         var2.printStackTrace();
         return -1;
      }
   }

}
