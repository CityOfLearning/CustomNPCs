package noppes.npcs.ai;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOpenAnyDoor extends EntityAIBase {

   private EntityNPCInterface npc;
   private BlockPos position;
   private Block door;
   private IProperty property;
   private boolean hasStoppedDoorInteraction;
   private float entityPositionX;
   private float entityPositionZ;
   private int closeDoorTemporisation;


   public EntityAIOpenAnyDoor(EntityNPCInterface npc) {
      this.npc = npc;
   }

   public boolean shouldExecute() {
      if(!this.npc.isCollidedHorizontally) {
         return false;
      } else {
         PathEntity pathentity = this.npc.getNavigator().getPath();
         if(pathentity != null && !pathentity.isFinished()) {
            for(int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i) {
               PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
               this.position = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
               if(this.npc.getDistanceSq((double)this.position.getX(), this.npc.posY, (double)this.position.getZ()) <= 2.25D) {
                  this.door = this.getDoor(this.position);
                  if(this.door != null) {
                     return true;
                  }
               }
            }

            this.position = (new BlockPos(this.npc)).up();
            this.door = this.getDoor(this.position);
            return this.door != null;
         } else {
            return false;
         }
      }
   }

   public boolean continueExecuting() {
      return this.closeDoorTemporisation > 0 && !this.hasStoppedDoorInteraction;
   }

   public void startExecuting() {
      this.hasStoppedDoorInteraction = false;
      this.entityPositionX = (float)((double)((float)this.position.getX() + 0.5F) - this.npc.posX);
      this.entityPositionZ = (float)((double)((float)this.position.getZ() + 0.5F) - this.npc.posZ);
      this.closeDoorTemporisation = 20;
      this.setDoorState(this.door, this.position, true);
   }

   public void resetTask() {
      this.setDoorState(this.door, this.position, false);
   }

   public void updateTask() {
      --this.closeDoorTemporisation;
      float f = (float)((double)((float)this.position.getX() + 0.5F) - this.npc.posX);
      float f1 = (float)((double)((float)this.position.getZ() + 0.5F) - this.npc.posZ);
      float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
      if(f2 < 0.0F) {
         this.hasStoppedDoorInteraction = true;
      }

   }

   public Block getDoor(BlockPos pos) {
      IBlockState state = this.npc.worldObj.getBlockState(pos);
      Block block = state.getBlock();
      if(!block.isFullBlock() && block != Blocks.iron_door) {
         if(block instanceof BlockDoor) {
            return block;
         } else {
            ImmutableSet set = state.getProperties().keySet();
            Iterator var5 = set.iterator();

            IProperty prop;
            do {
               if(!var5.hasNext()) {
                  return null;
               }

               prop = (IProperty)var5.next();
            } while(!(prop instanceof PropertyBool) || !prop.getName().equals("open"));

            this.property = prop;
            return block;
         }
      } else {
         return null;
      }
   }

   public void setDoorState(Block block, BlockPos position, boolean open) {
      if(block instanceof BlockDoor) {
         ((BlockDoor)block).toggleDoor(this.npc.worldObj, position, open);
      } else {
         IBlockState state = this.npc.worldObj.getBlockState(position);
         if(state.getBlock() != block) {
            return;
         }

         this.npc.worldObj.setBlockState(position, state.withProperty(this.property, Boolean.valueOf(open)));
         this.npc.worldObj.playAuxSFXAtEntity((EntityPlayer)null, open?1003:1006, position, 0);
      }

   }
}
