package noppes.npcs.blocks.tiles;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.wrapper.BlockScriptedDoorWrapper;
import noppes.npcs.blocks.tiles.TileDoor;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.data.DataTimers;

public class TileScriptedDoor extends TileDoor implements ITickable, IScriptBlockHandler {

   public List scripts = new ArrayList();
   public boolean shouldRefreshData = false;
   public String scriptLanguage = "ECMAScript";
   public boolean enabled = false;
   private IBlock blockDummy = null;
   public DataTimers timers = new DataTimers(this);
   public boolean hasInited = false;
   private short ticksExisted = 0;
   public int newPower = 0;
   public int prevPower = 0;


   public IBlock getBlock() {
      if(this.blockDummy == null) {
         this.blockDummy = new BlockScriptedDoorWrapper(this.getWorld(), this.getBlockType(), this.getPos());
      }

      return this.blockDummy;
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.setNBT(compound);
      this.timers.readFromNBT(compound);
   }

   public void setNBT(NBTTagCompound compound) {
      this.scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
      this.scriptLanguage = compound.getString("ScriptLanguage");
      this.enabled = compound.getBoolean("ScriptEnabled");
      this.prevPower = compound.getInteger("BlockPrevPower");
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      this.getNBT(compound);
      this.timers.writeToNBT(compound);
   }

   public NBTTagCompound getNBT(NBTTagCompound compound) {
      compound.setTag("Scripts", NBTTags.NBTScript(this.scripts));
      compound.setString("ScriptLanguage", this.scriptLanguage);
      compound.setBoolean("ScriptEnabled", this.enabled);
      compound.setInteger("BlockPrevPower", this.prevPower);
      return compound;
   }

   public void runScript(EnumScriptType type, Event event) {
      if(this.isEnabled()) {
         if(!this.hasInited) {
            this.hasInited = true;
            EventHooks.onScriptBlockInit(this);
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

   private boolean isEnabled() {
      return this.enabled && ScriptController.HasStart && !this.worldObj.isRemote;
   }

   public void update() {
      super.update();
      ++this.ticksExisted;
      if(this.prevPower != this.newPower) {
         EventHooks.onScriptBlockRedstonePower(this, this.prevPower, this.newPower);
         this.prevPower = this.newPower;
      }

      this.timers.update();
      if(this.ticksExisted >= 10) {
         EventHooks.onScriptBlockUpdate(this);
         this.ticksExisted = 0;
      }

   }

   public boolean isClient() {
      return this.getWorld().isRemote;
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
      BlockPos pos = this.getPos();
      return Objects.toStringHelper(this).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
   }
}
