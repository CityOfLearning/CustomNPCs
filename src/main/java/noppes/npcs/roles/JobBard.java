package noppes.npcs.roles;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.entity.data.role.IJobBard;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.constants.EnumBardInstrument;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobInterface;

public class JobBard extends JobInterface implements IJobBard {

   public int minRange = 2;
   public int maxRange = 64;
   public boolean isStreamer = true;
   public boolean hasOffRange = true;
   public String song = "";
   private EnumBardInstrument instrument;
   private long ticks;


   public JobBard(EntityNPCInterface npc) {
      super(npc);
      this.instrument = EnumBardInstrument.Banjo;
      this.ticks = 0L;
      if(CustomItems.banjo != null) {
         this.mainhand = new ItemStackWrapper(new ItemStack(CustomItems.banjo));
         this.overrideMainHand = this.overrideOffHand = true;
      }

   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setString("BardSong", this.song);
      nbttagcompound.setInteger("BardMinRange", this.minRange);
      nbttagcompound.setInteger("BardMaxRange", this.maxRange);
      nbttagcompound.setInteger("BardInstrument", this.instrument.ordinal());
      nbttagcompound.setBoolean("BardStreamer", this.isStreamer);
      nbttagcompound.setBoolean("BardHasOff", this.hasOffRange);
      return nbttagcompound;
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      this.song = nbttagcompound.getString("BardSong");
      this.minRange = nbttagcompound.getInteger("BardMinRange");
      this.maxRange = nbttagcompound.getInteger("BardMaxRange");
      this.setInstrument(nbttagcompound.getInteger("BardInstrument"));
      this.isStreamer = nbttagcompound.getBoolean("BardStreamer");
      this.hasOffRange = nbttagcompound.getBoolean("BardHasOff");
   }

   public void setInstrument(int i) {
      if(CustomItems.banjo != null) {
         this.instrument = EnumBardInstrument.values()[i];
         this.overrideMainHand = this.overrideOffHand = this.instrument != EnumBardInstrument.None;
         switch(null.$SwitchMap$noppes$npcs$constants$EnumBardInstrument[this.instrument.ordinal()]) {
         case 1:
            this.mainhand = null;
            this.offhand = null;
            break;
         case 2:
            this.mainhand = new ItemStackWrapper(new ItemStack(CustomItems.banjo));
            this.offhand = null;
            break;
         case 3:
            this.mainhand = new ItemStackWrapper(new ItemStack(CustomItems.violin));
            this.offhand = new ItemStackWrapper(new ItemStack(CustomItems.violinbow));
            break;
         case 4:
            this.mainhand = new ItemStackWrapper(new ItemStack(CustomItems.guitar));
            this.offhand = null;
            break;
         case 5:
            this.mainhand = new ItemStackWrapper(new ItemStack(CustomItems.harp));
            this.offhand = null;
            break;
         case 6:
            this.mainhand = new ItemStackWrapper(new ItemStack(CustomItems.frenchHorn));
            this.offhand = null;
         }

      }
   }

   public EnumBardInstrument getInstrument() {
      return this.instrument;
   }

   public void onLivingUpdate() {
      if(this.npc.isRemote()) {
         ++this.ticks;
         if(this.ticks % 10L == 0L) {
            if(!this.song.isEmpty()) {
               List list;
               if(!MusicController.Instance.isPlaying(this.song)) {
                  list = this.npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.npc.getEntityBoundingBox().expand((double)this.minRange, (double)(this.minRange / 2), (double)this.minRange));
                  if(!list.contains(CustomNpcs.proxy.getPlayer())) {
                     return;
                  }

                  if(this.isStreamer) {
                     MusicController.Instance.playStreaming(this.song, this.npc);
                  } else {
                     MusicController.Instance.playMusic(this.song, this.npc);
                  }
               } else if(MusicController.Instance.playingEntity != this.npc) {
                  EntityPlayer list1 = CustomNpcs.proxy.getPlayer();
                  if(this.npc.getDistanceSqToEntity(list1) < MusicController.Instance.playingEntity.getDistanceSqToEntity(list1)) {
                     MusicController.Instance.playingEntity = this.npc;
                  }
               } else if(this.hasOffRange) {
                  list = this.npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.npc.getEntityBoundingBox().expand((double)this.maxRange, (double)(this.maxRange / 2), (double)this.maxRange));
                  if(!list.contains(CustomNpcs.proxy.getPlayer())) {
                     MusicController.Instance.stopMusic();
                  }
               }

            }
         }
      }
   }

   public void killed() {
      this.delete();
   }

   public void delete() {
      if(this.npc.worldObj.isRemote && this.hasOffRange && MusicController.Instance.isPlaying(this.song)) {
         MusicController.Instance.stopMusic();
      }

   }

   public String getSong() {
      return this.song;
   }

   public void setSong(String song) {
      this.song = song;
      this.npc.updateClient = true;
   }
}
