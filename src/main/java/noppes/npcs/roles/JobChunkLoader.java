package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobInterface;

public class JobChunkLoader extends JobInterface {

   private List chunks = new ArrayList();
   private int ticks = 20;
   private long playerLastSeen = 0L;


   public JobChunkLoader(EntityNPCInterface npc) {
      super(npc);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      compound.setLong("ChunkPlayerLastSeen", this.playerLastSeen);
      return compound;
   }

   public void readFromNBT(NBTTagCompound compound) {
      this.playerLastSeen = compound.getLong("ChunkPlayerLastSeen");
   }

   public boolean aiShouldExecute() {
      --this.ticks;
      if(this.ticks > 0) {
         return false;
      } else {
         this.ticks = 20;
         List players = this.npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.npc.getEntityBoundingBox().expand(48.0D, 48.0D, 48.0D));
         if(!players.isEmpty()) {
            this.playerLastSeen = System.currentTimeMillis();
         }

         if(System.currentTimeMillis() > this.playerLastSeen + 600000L) {
            ChunkController.instance.deleteNPC(this.npc);
            this.chunks.clear();
            return false;
         } else {
            Ticket ticket = ChunkController.instance.getTicket(this.npc);
            if(ticket == null) {
               return false;
            } else {
               double x = this.npc.posX / 16.0D;
               double z = this.npc.posZ / 16.0D;
               ArrayList list = new ArrayList();
               list.add(new ChunkCoordIntPair(MathHelper.floor_double(x), MathHelper.floor_double(z)));
               list.add(new ChunkCoordIntPair(MathHelper.ceiling_double_int(x), MathHelper.ceiling_double_int(z)));
               list.add(new ChunkCoordIntPair(MathHelper.floor_double(x), MathHelper.ceiling_double_int(z)));
               list.add(new ChunkCoordIntPair(MathHelper.ceiling_double_int(x), MathHelper.floor_double(z)));
               Iterator var8 = list.iterator();

               ChunkCoordIntPair chunk;
               while(var8.hasNext()) {
                  chunk = (ChunkCoordIntPair)var8.next();
                  if(!this.chunks.contains(chunk)) {
                     ForgeChunkManager.forceChunk(ticket, chunk);
                  } else {
                     this.chunks.remove(chunk);
                  }
               }

               var8 = this.chunks.iterator();

               while(var8.hasNext()) {
                  chunk = (ChunkCoordIntPair)var8.next();
                  ForgeChunkManager.unforceChunk(ticket, chunk);
               }

               this.chunks = list;
               return false;
            }
         }
      }
   }

   public boolean aiContinueExecute() {
      return false;
   }

   public void reset() {
      ChunkController.instance.deleteNPC(this.npc);
      this.chunks.clear();
      this.playerLastSeen = 0L;
   }

   public void delete() {}
}
