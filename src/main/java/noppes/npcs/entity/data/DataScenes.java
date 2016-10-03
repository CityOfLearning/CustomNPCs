package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.controllers.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityProjectile;

public class DataScenes {

   private EntityNPCInterface npc;
   public List scenes = new ArrayList();
   public static Map StartedScenes = new HashMap();
   public static List ScenesToRun = new ArrayList();
   private EntityLivingBase owner = null;
   private String ownerScene = null;


   public DataScenes(EntityNPCInterface npc) {
      this.npc = npc;
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      NBTTagList list = new NBTTagList();
      Iterator var3 = this.scenes.iterator();

      while(var3.hasNext()) {
         DataScenes.SceneContainer scene = (DataScenes.SceneContainer)var3.next();
         list.appendTag(scene.writeToNBT(new NBTTagCompound()));
      }

      compound.setTag("Scenes", list);
      return compound;
   }

   public void readFromNBT(NBTTagCompound compound) {
      NBTTagList list = compound.getTagList("Scenes", 10);
      ArrayList scenes = new ArrayList();

      for(int i = 0; i < list.tagCount(); ++i) {
         DataScenes.SceneContainer scene = new DataScenes.SceneContainer();
         scene.readFromNBT(list.getCompoundTagAt(i));
         scenes.add(scene);
      }

      this.scenes = scenes;
   }

   public EntityLivingBase getOwner() {
      return this.owner;
   }

   public static void Toggle(ICommandSender sender, String id) {
      DataScenes.SceneState state = (DataScenes.SceneState)StartedScenes.get(id.toLowerCase());
      if(state != null && !state.paused) {
         state.paused = true;
         NoppesUtilServer.NotifyOPs("Paused scene %s at %s", new Object[]{id, Integer.valueOf(state.ticks)});
      } else {
         Start(sender, id);
      }

   }

   public static void Start(ICommandSender sender, String id) {
      DataScenes.SceneState state = (DataScenes.SceneState)StartedScenes.get(id.toLowerCase());
      if(state == null) {
         NoppesUtilServer.NotifyOPs("Started scene %s", new Object[]{id});
         StartedScenes.put(id.toLowerCase(), new DataScenes.SceneState());
      } else if(state.paused) {
         state.paused = false;
         NoppesUtilServer.NotifyOPs("Started scene %s from %s", new Object[]{id, Integer.valueOf(state.ticks)});
      }

   }

   public static void Pause(ICommandSender sender, String id) {
      if(id == null) {
         DataScenes.SceneState state1;
         for(Iterator state = StartedScenes.values().iterator(); state.hasNext(); state1.paused = true) {
            state1 = (DataScenes.SceneState)state.next();
         }

         NoppesUtilServer.NotifyOPs("Paused all scenes", new Object[0]);
      } else {
         DataScenes.SceneState state2 = (DataScenes.SceneState)StartedScenes.get(id.toLowerCase());
         state2.paused = true;
         NoppesUtilServer.NotifyOPs("Paused scene %s at %s", new Object[]{id, Integer.valueOf(state2.ticks)});
      }

   }

   public static void Reset(ICommandSender sender, String id) {
      if(id == null) {
         StartedScenes = new HashMap();
         NoppesUtilServer.NotifyOPs("Reset all scene", new Object[0]);
      } else if(StartedScenes.remove(id.toLowerCase()) == null) {
         sender.addChatMessage(new ChatComponentTranslation("Unknown scene %s ", new Object[]{id}));
      } else {
         NoppesUtilServer.NotifyOPs("Reset scene %s", new Object[]{id});
      }

   }

   public void update() {
      Iterator var1 = this.scenes.iterator();

      while(var1.hasNext()) {
         DataScenes.SceneContainer scene = (DataScenes.SceneContainer)var1.next();
         if(scene.validState()) {
            ScenesToRun.add(scene);
         }
      }

      if(this.owner != null && !StartedScenes.containsKey(this.ownerScene.toLowerCase())) {
         this.owner = null;
         this.ownerScene = null;
      }

   }

   public void addScene(String name) {
      if(!name.isEmpty()) {
         DataScenes.SceneContainer scene = new DataScenes.SceneContainer();
         scene.name = name;
         this.scenes.add(scene);
      }
   }


   public static class SceneState {

      public boolean paused = false;
      public int ticks = -1;


   }

   public static class SceneEvent implements Comparable {

      public int ticks = 0;
      public DataScenes.SceneType type;
      public String param = "";


      public String toString() {
         return this.ticks + " " + this.type.name() + " " + this.param;
      }

      public static DataScenes.SceneEvent parse(String str) {
         DataScenes.SceneEvent event = new DataScenes.SceneEvent();
         int i = str.indexOf(" ");
         if(i <= 0) {
            return null;
         } else {
            try {
               event.ticks = Integer.parseInt(str.substring(0, i));
               str = str.substring(i + 1);
            } catch (NumberFormatException var8) {
               return null;
            }

            i = str.indexOf(" ");
            if(i <= 0) {
               return null;
            } else {
               String name = str.substring(0, i);
               DataScenes.SceneType[] var4 = DataScenes.SceneType.values();
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  DataScenes.SceneType type = var4[var6];
                  if(name.equalsIgnoreCase(type.name())) {
                     event.type = type;
                  }
               }

               if(event.type == null) {
                  return null;
               } else {
                  event.param = str.substring(i + 1);
                  return event;
               }
            }
         }
      }

      public int compareTo(DataScenes.SceneEvent o) {
         return this.ticks - o.ticks;
      }
   }

   public static enum SceneType {

      ANIMATE("ANIMATE", 0),
      MOVE("MOVE", 1),
      FACTION("FACTION", 2),
      COMMAND("COMMAND", 3),
      EQUIP("EQUIP", 4),
      THROW("THROW", 5),
      ATTACK("ATTACK", 6),
      FOLLOW("FOLLOW", 7),
      SAY("SAY", 8),
      ROTATE("ROTATE", 9);
      // $FF: synthetic field
      private static final DataScenes.SceneType[] $VALUES = new DataScenes.SceneType[]{ANIMATE, MOVE, FACTION, COMMAND, EQUIP, THROW, ATTACK, FOLLOW, SAY, ROTATE};


      private SceneType(String var1, int var2) {}

   }

   public class SceneContainer {

      public int btn = 0;
      public String name = "";
      public String lines = "";
      public boolean enabled = false;
      public int ticks = -1;
      private DataScenes.SceneState state = null;
      private List events = new ArrayList();


      public NBTTagCompound writeToNBT(NBTTagCompound compound) {
         compound.setBoolean("Enabled", this.enabled);
         compound.setString("Name", this.name);
         compound.setString("Lines", this.lines);
         compound.setInteger("Button", this.btn);
         compound.setInteger("Ticks", this.ticks);
         return compound;
      }

      public boolean validState() {
         if(!this.enabled) {
            return false;
         } else {
            if(this.state != null) {
               if(DataScenes.StartedScenes.containsValue(this.state)) {
                  return !this.state.paused;
               }

               this.state = null;
            }

            this.state = (DataScenes.SceneState)DataScenes.StartedScenes.get(this.name.toLowerCase());
            if(this.state == null) {
               this.state = (DataScenes.SceneState)DataScenes.StartedScenes.get(this.btn + "btn");
            }

            return this.state != null?!this.state.paused:false;
         }
      }

      public void readFromNBT(NBTTagCompound compound) {
         this.enabled = compound.getBoolean("Enabled");
         this.name = compound.getString("Name");
         this.lines = compound.getString("Lines");
         this.btn = compound.getInteger("Button");
         this.ticks = compound.getInteger("Ticks");
         this.events = new ArrayList();
         String[] var2 = this.lines.split("\r\n|\r|\n");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            DataScenes.SceneEvent event = DataScenes.SceneEvent.parse(line);
            if(event != null) {
               this.events.add(event);
            }
         }

         Collections.sort(this.events);
      }

      public void update() {
         if(this.enabled && !this.events.isEmpty() && this.state != null) {
            Iterator var1 = this.events.iterator();

            while(var1.hasNext()) {
               DataScenes.SceneEvent event = (DataScenes.SceneEvent)var1.next();
               if(event.ticks > this.state.ticks) {
                  break;
               }

               if(event.ticks == this.state.ticks) {
                  try {
                     this.handle(event);
                  } catch (Exception var4) {
                     ;
                  }
               }
            }

            this.ticks = this.state.ticks;
         }
      }

      private void handle(DataScenes.SceneEvent event) throws Exception {
         String[] entity;
         if(event.type == DataScenes.SceneType.MOVE) {
            entity = event.param.split(" ");

            while(entity.length > 1) {
               boolean entity1 = false;
               if(entity[0].startsWith("to")) {
                  entity1 = true;
               } else if(!entity[0].startsWith("tp")) {
                  break;
               }

               BlockPos damage = null;
               if(entity[0].startsWith("@")) {
                  EntityLivingBase stack = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc, entity[0], EntityLivingBase.class);
                  if(stack != null) {
                     damage = stack.getPosition();
                  }

                  entity = (String[])Arrays.copyOfRange(entity, 2, entity.length);
               } else {
                  if(entity.length < 4) {
                     return;
                  }

                  damage = CommandBase.parseBlockPos(DataScenes.this.npc, entity, 1, false);
                  entity = (String[])Arrays.copyOfRange(entity, 4, entity.length);
               }

               if(damage != null) {
                  DataScenes.this.npc.ai.setStartPos(damage);
                  DataScenes.this.npc.getNavigator().clearPathEntity();
                  if(entity1) {
                     PathEntity stack1 = DataScenes.this.npc.getNavigator().getPathToPos(damage);
                     DataScenes.this.npc.getNavigator().setPath(stack1, 1.0D);
                  } else if(!DataScenes.this.npc.isInRange((double)damage.getX() + 0.5D, (double)damage.getY(), (double)damage.getZ() + 0.5D, 2.0D)) {
                     DataScenes.this.npc.setPosition((double)damage.getX() + 0.5D, (double)damage.getY(), (double)damage.getZ() + 0.5D);
                  }
               }
            }
         } else if(event.type == DataScenes.SceneType.SAY) {
            DataScenes.this.npc.saySurrounding(new Line(event.param));
         } else {
            EntityLivingBase entity2;
            if(event.type == DataScenes.SceneType.ROTATE) {
               DataScenes.this.npc.lookAi.resetTask();
               if(event.param.startsWith("@")) {
                  entity2 = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc, event.param, EntityLivingBase.class);
                  DataScenes.this.npc.lookAi.rotate(DataScenes.this.npc.worldObj.getClosestPlayerToEntity(entity2, 30.0D));
               } else {
                  DataScenes.this.npc.lookAi.rotate(Integer.parseInt(event.param));
               }
            } else if(event.type == DataScenes.SceneType.EQUIP) {
               entity = event.param.split(" ");
               if(entity.length < 2) {
                  return;
               }

               ItemStackWrapper entity3 = null;
               if(!entity[1].equalsIgnoreCase("none")) {
                  Item damage1 = CommandBase.getItemByText(DataScenes.this.npc, entity[1]);
                  int stack2 = entity.length >= 3?CommandBase.parseInt(entity[2], 1, 64):1;
                  int projectile = entity.length >= 4?CommandBase.parseInt(entity[3]):0;
                  entity3 = new ItemStackWrapper(new ItemStack(damage1, stack2, projectile));
               }

               if(entity[0].equalsIgnoreCase("main")) {
                  DataScenes.this.npc.inventory.weapons.put(Integer.valueOf(0), entity3);
               } else if(entity[0].equalsIgnoreCase("off")) {
                  DataScenes.this.npc.inventory.weapons.put(Integer.valueOf(2), entity3);
               } else if(entity[0].equalsIgnoreCase("proj")) {
                  DataScenes.this.npc.inventory.weapons.put(Integer.valueOf(1), entity3);
               } else if(entity[0].equalsIgnoreCase("head")) {
                  DataScenes.this.npc.inventory.armor.put(Integer.valueOf(0), entity3);
               } else if(entity[0].equalsIgnoreCase("body")) {
                  DataScenes.this.npc.inventory.armor.put(Integer.valueOf(1), entity3);
               } else if(entity[0].equalsIgnoreCase("legs")) {
                  DataScenes.this.npc.inventory.armor.put(Integer.valueOf(2), entity3);
               } else if(entity[0].equalsIgnoreCase("boots")) {
                  DataScenes.this.npc.inventory.armor.put(Integer.valueOf(3), entity3);
               }
            } else if(event.type == DataScenes.SceneType.ATTACK) {
               if(event.param.equals("none")) {
                  DataScenes.this.npc.setAttackTarget((EntityLivingBase)null);
               } else {
                  entity2 = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc, event.param, EntityLivingBase.class);
                  if(entity2 != null) {
                     DataScenes.this.npc.setAttackTarget(entity2);
                  }
               }
            } else if(event.type == DataScenes.SceneType.THROW) {
               entity = event.param.split(" ");
               EntityLivingBase entity4 = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc, entity[0], EntityLivingBase.class);
               if(entity4 == null) {
                  return;
               }

               float damage2 = Float.parseFloat(entity[1]);
               if(damage2 <= 0.0F) {
                  damage2 = 0.01F;
               }

               ItemStack stack3 = ItemStackWrapper.MCItem(DataScenes.this.npc.inventory.getProjectile());
               if(entity.length > 2) {
                  Item projectile1 = CommandBase.getItemByText(DataScenes.this.npc, entity[2]);
                  stack3 = new ItemStack(projectile1, 1, 0);
               }

               EntityProjectile projectile2 = DataScenes.this.npc.shoot(entity4, 100, stack3, false);
               projectile2.damage = damage2;
            } else if(event.type == DataScenes.SceneType.ANIMATE) {
               DataScenes.this.npc.animateAi.temp = 0;
               if(event.param.equalsIgnoreCase("sleep")) {
                  DataScenes.this.npc.animateAi.temp = 2;
               } else if(event.param.equalsIgnoreCase("sneak")) {
                  DataScenes.this.npc.ai.animationType = 4;
               } else if(event.param.equalsIgnoreCase("normal")) {
                  DataScenes.this.npc.ai.animationType = 0;
               } else if(event.param.equalsIgnoreCase("sit")) {
                  DataScenes.this.npc.animateAi.temp = 1;
               } else if(event.param.equalsIgnoreCase("crawl")) {
                  DataScenes.this.npc.ai.animationType = 7;
               } else if(event.param.equalsIgnoreCase("bow")) {
                  DataScenes.this.npc.animateAi.temp = 11;
               } else if(event.param.equalsIgnoreCase("yes")) {
                  DataScenes.this.npc.animateAi.temp = 13;
               } else if(event.param.equalsIgnoreCase("no")) {
                  DataScenes.this.npc.animateAi.temp = 12;
               }
            } else if(event.type == DataScenes.SceneType.COMMAND) {
               NoppesUtilServer.runCommand(DataScenes.this.npc, DataScenes.this.npc.getName(), event.param, (EntityPlayer)null);
            } else if(event.type == DataScenes.SceneType.FACTION) {
               DataScenes.this.npc.setFaction(Integer.parseInt(event.param));
            } else if(event.type == DataScenes.SceneType.FOLLOW) {
               if(event.param.equalsIgnoreCase("none")) {
                  DataScenes.this.owner = null;
                  DataScenes.this.ownerScene = null;
               } else {
                  entity2 = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc, event.param, EntityLivingBase.class);
                  if(entity2 == null) {
                     return;
                  }

                  DataScenes.this.owner = entity2;
                  DataScenes.this.ownerScene = this.name;
               }
            }
         }

      }
   }
}
