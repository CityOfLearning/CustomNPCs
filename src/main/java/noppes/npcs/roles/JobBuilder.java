package noppes.npcs.roles;

import java.util.Iterator;
import java.util.Stack;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobInterface;

public class JobBuilder extends JobInterface {

   public TileBuilder build = null;
   private BlockPos possibleBuildPos = null;
   private Stack placingList = null;
   private BlockData placing = null;
   private int tryTicks = 0;
   private int ticks = 0;


   public JobBuilder(EntityNPCInterface npc) {
      super(npc);
      this.overrideMainHand = true;
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      if(this.build != null) {
         compound.setInteger("BuildX", this.build.getPos().getX());
         compound.setInteger("BuildY", this.build.getPos().getY());
         compound.setInteger("BuildZ", this.build.getPos().getZ());
         if(this.placingList != null && !this.placingList.isEmpty()) {
            NBTTagList list = new NBTTagList();
            Iterator var3 = this.placingList.iterator();

            while(var3.hasNext()) {
               BlockData data = (BlockData)var3.next();
               list.appendTag(data.getNBT());
            }

            if(this.placing != null) {
               list.appendTag(this.placing.getNBT());
            }

            compound.setTag("Placing", list);
         }
      }

      return compound;
   }

   public void readFromNBT(NBTTagCompound compound) {
      if(compound.hasKey("BuildX")) {
         this.possibleBuildPos = new BlockPos(compound.getInteger("BuildX"), compound.getInteger("BuildY"), compound.getInteger("BuildZ"));
      }

      if(this.possibleBuildPos != null && compound.hasKey("Placing")) {
         Stack placing = new Stack();
         NBTTagList list = compound.getTagList("Placing", 10);

         for(int i = 0; i < list.tagCount(); ++i) {
            BlockData data = BlockData.getData(list.getCompoundTagAt(i));
            if(data != null) {
               placing.add(data);
            }
         }

         this.placingList = placing;
      }

      this.npc.ai.doorInteract = 1;
   }

   public IItemStack getMainhand() {
      String name = this.npc.getDataWatcher().getWatchableObjectString(17);
      ItemStack item = this.stringToItem(name);
      return (IItemStack)(item == null?(IItemStack)this.npc.inventory.weapons.get(Integer.valueOf(0)):new ItemStackWrapper(item));
   }

   public boolean aiShouldExecute() {
      if(this.possibleBuildPos != null) {
         TileEntity tile = this.npc.worldObj.getTileEntity(this.possibleBuildPos);
         if(tile instanceof TileBuilder) {
            this.build = (TileBuilder)tile;
         } else {
            this.placingList.clear();
         }

         this.possibleBuildPos = null;
      }

      return this.build != null;
   }

   public void aiUpdateTask() {
      if((!this.build.finished || this.placingList != null) && this.build.enabled && !this.build.isInvalid()) {
         if(this.ticks++ >= 10) {
            this.ticks = 0;
            if((this.placingList == null || this.placingList.isEmpty()) && this.placing == null) {
               this.placingList = this.build.getBlock();
               this.npc.getDataWatcher().updateObject(17, "");
            } else {
               if(this.placing == null) {
                  this.placing = (BlockData)this.placingList.pop();
                  this.tryTicks = 0;
                  this.npc.getDataWatcher().updateObject(17, this.blockToString(this.placing));
               }

               this.npc.getNavigator().tryMoveToXYZ((double)this.placing.pos.getX(), (double)(this.placing.pos.getY() + 1), (double)this.placing.pos.getZ(), 1.0D);
               if(this.tryTicks++ > 40 || this.npc.nearPosition(this.placing.pos)) {
                  BlockPos blockPos = this.placing.pos;
                  this.placeBlock();
                  if(this.tryTicks > 40) {
                     blockPos = NoppesUtilServer.GetClosePos(blockPos, this.npc.worldObj);
                     this.npc.setPositionAndUpdate((double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D);
                  }
               }

            }
         }
      } else {
         this.build = null;
         this.npc.getNavigator().tryMoveToXYZ((double)this.npc.getStartXPos(), this.npc.getStartYPos(), (double)this.npc.getStartZPos(), 1.0D);
      }
   }

   private String blockToString(BlockData data) {
      if(data.state.getBlock() == Blocks.air) {
         return "minecraft:iron_pickaxe";
      } else {
         String name = this.itemToString(data.getStack());
         return name == null?"":name;
      }
   }

   public void aiStartExecuting() {
      this.npc.ai.returnToStart = false;
   }

   public void resetTask() {
      this.reset();
   }

   public void reset() {
      this.build = null;
      this.npc.ai.returnToStart = true;
      this.npc.getDataWatcher().updateObject(17, "");
   }

   public void placeBlock() {
      if(this.placing != null) {
         this.npc.getNavigator().clearPathEntity();
         this.npc.swingItem();
         this.npc.worldObj.setBlockState(this.placing.pos, this.placing.state, 2);
         if(this.placing.state.getBlock() instanceof ITileEntityProvider && this.placing.tile != null) {
            TileEntity tile = this.npc.worldObj.getTileEntity(this.placing.pos);
            if(tile != null) {
               tile.readFromNBT(this.placing.tile);
            }
         }

         this.placing = null;
      }
   }
}
