package noppes.npcs.blocks.tiles;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.blocks.tiles.TileNpcEntity;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.util.ValueUtil;

public class TileScripted extends TileNpcEntity implements ITickable, IScriptBlockHandler {

   public List scripts = new ArrayList();
   public String scriptLanguage = "ECMAScript";
   public boolean enabled = false;
   private IBlock blockDummy = null;
   public DataTimers timers = new DataTimers(this);
   public boolean hasInited = false;
   private short ticksExisted = 0;
   public ItemStack itemModel;
   public boolean needsClientUpdate;
   public int powering;
   public int activePowering;
   public int newPower;
   public int prevPower;
   public boolean isLadder;
   public int lightValue;
   public int rotationX;
   public int rotationY;
   public int rotationZ;
   public float scaleX;
   public float scaleY;
   public float scaleZ;
   public TileEntity renderTile;
   public boolean renderTileErrored;
   public ITickable renderTileUpdate;


   public TileScripted() {
      this.itemModel = new ItemStack(CustomItems.scripted);
      this.needsClientUpdate = false;
      this.powering = 0;
      this.activePowering = 0;
      this.newPower = 0;
      this.prevPower = 0;
      this.isLadder = false;
      this.lightValue = 0;
      this.rotationX = 0;
      this.rotationY = 0;
      this.rotationZ = 0;
      this.scaleX = 1.0F;
      this.scaleY = 1.0F;
      this.scaleZ = 1.0F;
      this.renderTileErrored = true;
      this.renderTileUpdate = null;
   }

   public IBlock getBlock() {
      if(this.blockDummy == null) {
         this.blockDummy = new BlockScriptedWrapper(this.getWorld(), this.getBlockType(), this.getPos());
      }

      return this.blockDummy;
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.setNBT(compound);
      this.setDisplayNBT(compound);
      this.timers.readFromNBT(compound);
   }

   public void setNBT(NBTTagCompound compound) {
      this.scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
      this.scriptLanguage = compound.getString("ScriptLanguage");
      this.enabled = compound.getBoolean("ScriptEnabled");
      this.activePowering = this.powering = compound.getInteger("BlockPowering");
      this.prevPower = compound.getInteger("BlockPrevPower");
   }

   public void setDisplayNBT(NBTTagCompound compound) {
      this.itemModel = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("ScriptBlockModel"));
      if(this.itemModel == null || this.itemModel.getItem() == null) {
         this.itemModel = new ItemStack(CustomItems.scripted);
      }

      this.renderTileUpdate = null;
      this.renderTile = null;
      this.renderTileErrored = false;
      this.lightValue = compound.getInteger("LightValue");
      this.isLadder = compound.getBoolean("IsLadder");
      this.rotationX = compound.getInteger("RotationX");
      this.rotationY = compound.getInteger("RotationY");
      this.rotationZ = compound.getInteger("RotationZ");
      this.scaleX = compound.getFloat("ScaleX");
      this.scaleY = compound.getFloat("ScaleY");
      this.scaleZ = compound.getFloat("ScaleZ");
      if(this.scaleX <= 0.0F) {
         this.scaleX = 1.0F;
      }

      if(this.scaleY <= 0.0F) {
         this.scaleY = 1.0F;
      }

      if(this.scaleZ <= 0.0F) {
         this.scaleZ = 1.0F;
      }

   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      this.getNBT(compound);
      this.getDisplayNBT(compound);
      this.timers.writeToNBT(compound);
   }

   public NBTTagCompound getNBT(NBTTagCompound compound) {
      compound.setTag("Scripts", NBTTags.NBTScript(this.scripts));
      compound.setString("ScriptLanguage", this.scriptLanguage);
      compound.setBoolean("ScriptEnabled", this.enabled);
      compound.setInteger("BlockPowering", this.powering);
      compound.setInteger("BlockPrevPower", this.prevPower);
      return compound;
   }

   public NBTTagCompound getDisplayNBT(NBTTagCompound compound) {
      NBTTagCompound itemcompound = new NBTTagCompound();
      this.itemModel.writeToNBT(itemcompound);
      compound.setTag("ScriptBlockModel", itemcompound);
      compound.setInteger("LightValue", this.lightValue);
      compound.setBoolean("IsLadder", this.isLadder);
      compound.setInteger("RotationX", this.rotationX);
      compound.setInteger("RotationY", this.rotationY);
      compound.setInteger("RotationZ", this.rotationZ);
      compound.setFloat("ScaleX", this.scaleX);
      compound.setFloat("ScaleY", this.scaleY);
      compound.setFloat("ScaleZ", this.scaleZ);
      return compound;
   }

   private boolean isEnabled() {
      return this.enabled && ScriptController.HasStart && !this.worldObj.isRemote;
   }

   public void update() {
      if(this.renderTileUpdate != null) {
         try {
            this.renderTileUpdate.update();
         } catch (Exception var2) {
            this.renderTileUpdate = null;
         }
      }

      ++this.ticksExisted;
      if(this.prevPower != this.newPower && this.powering <= 0) {
         EventHooks.onScriptBlockRedstonePower(this, this.prevPower, this.newPower);
         this.prevPower = this.newPower;
      }

      this.timers.update();
      if(this.ticksExisted >= 10) {
         EventHooks.onScriptBlockUpdate(this);
         this.ticksExisted = 0;
         if(this.needsClientUpdate) {
            this.worldObj.markBlockForUpdate(this.pos);
            this.needsClientUpdate = false;
         }
      }

   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      int light = this.lightValue;
      this.setDisplayNBT(pkt.getNbtCompound());
      if(light != this.lightValue) {
         this.worldObj.checkLight(this.pos);
      }

   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      this.getDisplayNBT(compound);
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }

   public void setItemModel(ItemStack item) {
      if(item == null || item.getItem() == null) {
         item = new ItemStack(CustomItems.scripted);
      }

      if(!NoppesUtilPlayer.compareItems(item, this.itemModel, false, false)) {
         this.itemModel = item;
         this.needsClientUpdate = true;
      }
   }

   public void setLightValue(int value) {
      if(value != this.lightValue) {
         this.lightValue = ValueUtil.CorrectInt(value, 0, 15);
         this.needsClientUpdate = true;
      }
   }

   public void setRedstonePower(int strength) {
      if(this.powering != strength) {
         this.prevPower = this.activePowering = ValueUtil.CorrectInt(strength, 0, 15);
         this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
         this.powering = this.activePowering;
      }
   }

   public void setScale(float x, float y, float z) {
      if(this.scaleX != x || this.scaleY != y || this.scaleZ != z) {
         this.scaleX = ValueUtil.correctFloat(x, 0.0F, 10.0F);
         this.scaleY = ValueUtil.correctFloat(y, 0.0F, 10.0F);
         this.scaleZ = ValueUtil.correctFloat(z, 0.0F, 10.0F);
         this.needsClientUpdate = true;
      }
   }

   public void setRotation(int x, int y, int z) {
      if(this.rotationX != x || this.rotationY != y || this.rotationZ != z) {
         this.rotationX = ValueUtil.CorrectInt(x, 0, 359);
         this.rotationY = ValueUtil.CorrectInt(y, 0, 359);
         this.rotationZ = ValueUtil.CorrectInt(z, 0, 359);
         this.needsClientUpdate = true;
      }
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

   public boolean isClient() {
      return this.getWorld().isRemote;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bo) {
      this.enabled = bo;
   }

   public String noticeString() {
      BlockPos pos = this.getPos();
      return Objects.toStringHelper(this).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
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
}
