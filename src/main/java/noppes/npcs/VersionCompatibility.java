package noppes.npcs;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.ICompatibilty;
import noppes.npcs.NBTTags;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.Lines;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityNPCInterface;

public class VersionCompatibility {

   public static int ModRev = 17;


   public static void CheckNpcCompatibility(EntityNPCInterface npc, NBTTagCompound compound) {
      if(npc.npcVersion != ModRev) {
         if(npc.npcVersion < 12) {
            CompatabilityFix(compound, npc.advanced.writeToNBT(new NBTTagCompound()));
            CompatabilityFix(compound, npc.ai.writeToNBT(new NBTTagCompound()));
            CompatabilityFix(compound, npc.stats.writeToNBT(new NBTTagCompound()));
            CompatabilityFix(compound, npc.display.writeToNBT(new NBTTagCompound()));
            CompatabilityFix(compound, npc.inventory.writeEntityToNBT(new NBTTagCompound()));
         }

         if(npc.npcVersion < 5) {
            String list = compound.getString("Texture");
            list = list.replace("/mob/customnpcs/", "customnpcs:textures/entity/");
            list = list.replace("/mob/", "customnpcs:textures/entity/");
            compound.setString("Texture", list);
         }

         int i;
         int var16;
         if(npc.npcVersion < 6 && compound.getTag("NpcInteractLines") instanceof NBTTagList) {
            List var9 = NBTTags.getStringList(compound.getTagList("NpcInteractLines", 10));
            Lines script = new Lines();

            for(i = 0; i < var9.size(); ++i) {
               Line scriptOld = new Line();
               scriptOld.text = (String)var9.toArray()[i];
               script.lines.put(Integer.valueOf(i), scriptOld);
            }

            compound.setTag("NpcInteractLines", script.writeToNBT());
            List var13 = NBTTags.getStringList(compound.getTagList("NpcLines", 10));
            script = new Lines();

            for(var16 = 0; var16 < var13.size(); ++var16) {
               Line type = new Line();
               type.text = (String)var13.toArray()[var16];
               script.lines.put(Integer.valueOf(var16), type);
            }

            compound.setTag("NpcLines", script.writeToNBT());
            List var17 = NBTTags.getStringList(compound.getTagList("NpcAttackLines", 10));
            script = new Lines();

            for(int var18 = 0; var18 < var17.size(); ++var18) {
               Line i1 = new Line();
               i1.text = (String)var17.toArray()[var18];
               script.lines.put(Integer.valueOf(var18), i1);
            }

            compound.setTag("NpcAttackLines", script.writeToNBT());
            List var20 = NBTTags.getStringList(compound.getTagList("NpcKilledLines", 10));
            script = new Lines();

            for(int var21 = 0; var21 < var20.size(); ++var21) {
               Line s = new Line();
               s.text = (String)var20.toArray()[var21];
               script.lines.put(Integer.valueOf(var21), s);
            }

            compound.setTag("NpcKilledLines", script.writeToNBT());
         }

         NBTTagList var10;
         if(npc.npcVersion == 12) {
            var10 = compound.getTagList("StartPos", 3);
            if(var10.tagCount() == 3) {
               int var12 = ((NBTTagInt)var10.removeTag(2)).getInt();
               i = ((NBTTagInt)var10.removeTag(1)).getInt();
               var16 = ((NBTTagInt)var10.removeTag(0)).getInt();
               compound.setIntArray("StartPosNew", new int[]{var16, i, var12});
            }
         }

         if(npc.npcVersion == 13) {
            boolean var11 = compound.getBoolean("HealthRegen");
            compound.setInteger("HealthRegen", var11?1:0);
            NBTTagCompound var14 = compound.getCompoundTag("TransformStats");
            var11 = var14.getBoolean("HealthRegen");
            var14.setInteger("HealthRegen", var11?1:0);
            compound.setTag("TransformStats", var14);
         }

         if(npc.npcVersion == 15) {
            var10 = compound.getTagList("ScriptsContainers", 10);
            if(var10.tagCount() > 0) {
               ScriptContainer var15 = new ScriptContainer(npc.script);

               for(i = 0; i < var10.tagCount(); ++i) {
                  NBTTagCompound var19 = var10.getCompoundTagAt(i);
                  EnumScriptType var22 = EnumScriptType.values()[var19.getInteger("Type")];
                  var15.script = var15.script + "\nfunction " + var22.function + "(event) {\n" + var19.getString("Script") + "\n}";
                  Iterator var23 = NBTTags.getStringList(compound.getTagList("ScriptList", 10)).iterator();

                  while(var23.hasNext()) {
                     String var24 = (String)var23.next();
                     if(!var15.scripts.contains(var24)) {
                        var15.scripts.add(var24);
                     }
                  }
               }
            }

            if(compound.getBoolean("CanDespawn")) {
               compound.setInteger("SpawnCycle", 4);
            }

            if(compound.getInteger("RangeAndMelee") <= 0) {
               compound.setInteger("DistanceToMelee", 0);
            }
         }

         if(npc.npcVersion == 16) {
            compound.setString("HitSound", "random.bowhit");
            compound.setString("GroundSound", "random.break");
         }

         npc.npcVersion = ModRev;
      }
   }

   public static void CheckAvailabilityCompatibility(ICompatibilty compatibilty, NBTTagCompound compound) {
      if(compatibilty.getVersion() != ModRev) {
         CompatabilityFix(compound, compatibilty.writeToNBT(new NBTTagCompound()));
         compatibilty.setVersion(ModRev);
      }
   }

   private static void CompatabilityFix(NBTTagCompound compound, NBTTagCompound check) {
      Set tags = check.getKeySet();
      Iterator var3 = tags.iterator();

      while(var3.hasNext()) {
         String name = (String)var3.next();
         NBTBase nbt = check.getTag(name);
         if(!compound.hasKey(name)) {
            compound.setTag(name, nbt);
         } else if(nbt instanceof NBTTagCompound && compound.getTag(name) instanceof NBTTagCompound) {
            CompatabilityFix(compound.getCompoundTag(name), (NBTTagCompound)nbt);
         }
      }

   }

}
