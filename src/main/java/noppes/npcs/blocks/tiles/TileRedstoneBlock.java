package noppes.npcs.blocks.tiles;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.blocks.tiles.TileNpcEntity;
import noppes.npcs.controllers.Availability;

public class TileRedstoneBlock extends TileNpcEntity implements ITickable {

   public int onRange = 6;
   public int offRange = 10;
   public int onRangeX = 6;
   public int onRangeY = 6;
   public int onRangeZ = 6;
   public int offRangeX = 10;
   public int offRangeY = 10;
   public int offRangeZ = 10;
   public boolean isDetailed = false;
   public Availability availability = new Availability();
   public boolean isActivated = false;
   private int ticks = 10;


   public void update() {
      if(!this.worldObj.isRemote) {
         --this.ticks;
         if(this.ticks <= 0) {
            this.ticks = 20;
            Block block = this.getBlockType();
            if(block != null && block instanceof BlockNpcRedstone) {
               if(CustomNpcs.FreezeNPCs) {
                  if(this.isActivated) {
                     this.setActive(block, false);
                  }

               } else {
                  int x;
                  int y;
                  int z;
                  List list;
                  Iterator var6;
                  EntityPlayer player;
                  if(!this.isActivated) {
                     x = this.isDetailed?this.onRangeX:this.onRange;
                     y = this.isDetailed?this.onRangeY:this.onRange;
                     z = this.isDetailed?this.onRangeZ:this.onRange;
                     list = this.getPlayerList(x, y, z);
                     if(list.isEmpty()) {
                        return;
                     }

                     var6 = list.iterator();

                     while(var6.hasNext()) {
                        player = (EntityPlayer)var6.next();
                        if(this.availability.isAvailable(player)) {
                           this.setActive(block, true);
                           return;
                        }
                     }
                  } else {
                     x = this.isDetailed?this.offRangeX:this.offRange;
                     y = this.isDetailed?this.offRangeY:this.offRange;
                     z = this.isDetailed?this.offRangeZ:this.offRange;
                     list = this.getPlayerList(x, y, z);
                     var6 = list.iterator();

                     while(var6.hasNext()) {
                        player = (EntityPlayer)var6.next();
                        if(this.availability.isAvailable(player)) {
                           return;
                        }
                     }

                     this.setActive(block, false);
                  }

               }
            }
         }
      }
   }

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
      return oldState.getBlock() != newState.getBlock();
   }

   private void setActive(Block block, boolean bo) {
      this.isActivated = bo;
      IBlockState state = block.getDefaultState().withProperty(BlockNpcRedstone.ACTIVE, Boolean.valueOf(this.isActivated));
      this.worldObj.setBlockState(this.pos, state, 2);
      this.worldObj.markBlockForUpdate(this.pos);
      block.onBlockAdded(this.worldObj, this.pos, state);
   }

   private List getPlayerList(int x, int y, int z) {
      return this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + 1), (double)(this.pos.getZ() + 1))).expand((double)x, (double)y, (double)z));
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.onRange = compound.getInteger("BlockOnRange");
      this.offRange = compound.getInteger("BlockOffRange");
      this.isDetailed = compound.getBoolean("BlockIsDetailed");
      if(compound.hasKey("BlockOnRangeX")) {
         this.isDetailed = true;
         this.onRangeX = compound.getInteger("BlockOnRangeX");
         this.onRangeY = compound.getInteger("BlockOnRangeY");
         this.onRangeZ = compound.getInteger("BlockOnRangeZ");
         this.offRangeX = compound.getInteger("BlockOffRangeX");
         this.offRangeY = compound.getInteger("BlockOffRangeY");
         this.offRangeZ = compound.getInteger("BlockOffRangeZ");
      }

      if(compound.hasKey("BlockActivated")) {
         this.isActivated = compound.getBoolean("BlockActivated");
      }

      this.availability.readFromNBT(compound);
      if(this.worldObj != null) {
         this.setActive(this.getBlockType(), this.isActivated);
      }

   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setInteger("BlockOnRange", this.onRange);
      compound.setInteger("BlockOffRange", this.offRange);
      compound.setBoolean("BlockActivated", this.isActivated);
      compound.setBoolean("BlockIsDetailed", this.isDetailed);
      if(this.isDetailed) {
         compound.setInteger("BlockOnRangeX", this.onRangeX);
         compound.setInteger("BlockOnRangeY", this.onRangeY);
         compound.setInteger("BlockOnRangeZ", this.onRangeZ);
         compound.setInteger("BlockOffRangeX", this.offRangeX);
         compound.setInteger("BlockOffRangeY", this.offRangeY);
         compound.setInteger("BlockOffRangeZ", this.offRangeZ);
      }

      this.availability.writeToNBT(compound);
   }

   public boolean canUpdate() {
      return true;
   }
}
