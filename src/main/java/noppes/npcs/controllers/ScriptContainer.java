package noppes.npcs.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.constants.AnimationType;
import noppes.npcs.api.constants.EntityType;
import noppes.npcs.api.constants.JobType;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.constants.RoleType;
import noppes.npcs.api.constants.TacticalType;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptController;

public class ScriptContainer {

   private static final AnimationType animations = new AnimationType();
   private static final EntityType entities = new EntityType();
   private static final JobType jobs = new JobType();
   private static final RoleType roles = new RoleType();
   private static final TacticalType tacticalVariantTypes = new TacticalType();
   private static final PotionEffectType potionEffectTypes = new PotionEffectType();
   private static final ParticleType particleTypes = new ParticleType();
   public String fullscript = "";
   public String script = "";
   public Map console = new HashMap();
   public boolean errored = false;
   public List scripts = new ArrayList();
   private List unknownFunctions = new ArrayList();
   private long lastCreated = 0L;
   private String currentScriptLanguage = null;
   public ScriptEngine engine = null;
   private IScriptHandler handler = null;
   private boolean init = false;


   public ScriptContainer(IScriptHandler handler) {
      this.handler = handler;
   }

   public void readFromNBT(NBTTagCompound compound) {
      this.script = compound.getString("Script");
      this.console = NBTTags.GetLongStringMap(compound.getTagList("Console", 10));
      this.scripts = NBTTags.getStringList(compound.getTagList("ScriptList", 10));
      this.lastCreated = 0L;
   }

   public void writeToNBT(NBTTagCompound compound) {
      compound.setString("Script", this.script);
      compound.setTag("Console", NBTTags.NBTLongStringMap(this.console));
      compound.setTag("ScriptList", NBTTags.nbtStringList(this.scripts));
   }

   public String getCode() {
      if(ScriptController.Instance.lastLoaded > this.lastCreated) {
         this.lastCreated = ScriptController.Instance.lastLoaded;
         this.fullscript = this.script;
         if(!this.fullscript.isEmpty()) {
            this.fullscript = this.fullscript + "\n";
         }

         Iterator var1 = this.scripts.iterator();

         while(var1.hasNext()) {
            String loc = (String)var1.next();
            String code = (String)ScriptController.Instance.scripts.get(loc);
            if(code != null && !code.isEmpty()) {
               this.fullscript = this.fullscript + code + "\n";
            }
         }

         this.unknownFunctions = new ArrayList();
         this.init = false;
      }

      return this.fullscript;
   }

   public void run(EnumScriptType type, Event event) {
      if(this.hasCode() && !this.unknownFunctions.contains(Integer.valueOf(type.ordinal()))) {
         StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw);
         this.engine.getContext().setWriter(pw);
         this.engine.getContext().setErrorWriter(pw);

         try {
            if(!this.init) {
               this.engine.eval(this.getCode());
               this.init = true;
            }

            ((Invocable)this.engine).invokeFunction(type.function, new Object[]{event});
         } catch (NoSuchMethodException var6) {
            this.unknownFunctions.add(Integer.valueOf(type.ordinal()));
         } catch (Exception var7) {
            this.errored = true;
            var7.printStackTrace(pw);
            NoppesUtilServer.NotifyOPs(this.handler.noticeString() + " script errored", new Object[0]);
         }

         this.appandConsole(sw.getBuffer().toString().trim());
         pw.close();
      }
   }

   public void appandConsole(String message) {
      if(message != null && !message.isEmpty()) {
         this.console.put(Long.valueOf(System.currentTimeMillis()), message);
      }
   }

   public boolean hasCode() {
      return !this.getCode().isEmpty();
   }

   public void setEngine(String scriptLanguage) {
      if(this.currentScriptLanguage == null || !this.currentScriptLanguage.equals(scriptLanguage)) {
         this.engine = ScriptController.Instance.getEngineByName(scriptLanguage);
         if(this.engine == null) {
            this.errored = true;
         } else {
            this.engine.put("AnimationType", animations);
            this.engine.put("EntityType", entities);
            this.engine.put("RoleType", roles);
            this.engine.put("JobType", jobs);
            this.engine.put("TacticalVariantType", tacticalVariantTypes);
            this.engine.put("PotionEffectType", potionEffectTypes);
            this.engine.put("ParticleType", particleTypes);
            this.currentScriptLanguage = scriptLanguage;
            this.init = false;
         }
      }
   }

}
