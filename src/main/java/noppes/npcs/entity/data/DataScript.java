package noppes.npcs.entity.data;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.EntityNPCInterface;

public class DataScript implements IScriptHandler {

   private List scripts = new ArrayList();
   private String scriptLanguage = "ECMAScript";
   private EntityNPCInterface npc;
   private boolean enabled = false;
   public boolean hasInited = false;


   public DataScript(EntityNPCInterface npc) {
      this.npc = npc;
   }

   public void readFromNBT(NBTTagCompound compound) {
      this.scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
      this.scriptLanguage = compound.getString("ScriptLanguage");
      this.enabled = compound.getBoolean("ScriptEnabled");
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      compound.setTag("Scripts", NBTTags.NBTScript(this.scripts));
      compound.setString("ScriptLanguage", this.scriptLanguage);
      compound.setBoolean("ScriptEnabled", this.enabled);
      return compound;
   }

   public void runScript(EnumScriptType type, Event event) {
      if(this.isEnabled()) {
         if(!this.hasInited) {
            this.hasInited = true;
            EventHooks.onNPCInit(this.npc);
         }

         Iterator var3 = this.scripts.iterator();

         while(var3.hasNext()) {
            ScriptContainer script = (ScriptContainer)var3.next();
            if(!script.errored && script.hasCode()) {
               script.setEngine(this.scriptLanguage);
               if(script.engine != null) {
                  script.run(type, event);
               }
            }
         }

      }
   }

   public boolean isEnabled() {
      return this.enabled && ScriptController.HasStart && !this.npc.worldObj.isRemote;
   }

   public boolean isClient() {
      return this.npc.isRemote();
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bo) {
      this.enabled = bo;
   }

   public String getLanguage() {
      return this.scriptLanguage;
   }

   public void setLanguage(String lang) {
      this.scriptLanguage = lang;
   }

   public List getScripts() {
      return this.scripts;
   }

   public String noticeString() {
      BlockPos pos = this.npc.getPosition();
      return Objects.toStringHelper(this.npc).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
   }
}
