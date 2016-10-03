package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.NpcBlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobInterface;

public class JobFarmer extends JobInterface implements MassBlockController.IMassBlock {

   public int chestMode = 1;
   private List trackedBlocks = new ArrayList();
   private int ticks = 0;
   private int walkTicks = 0;
   private int blockTicks = 800;
   private boolean waitingForBlocks = false;
   private BlockPos ripe = null;
   private BlockPos chest = null;
   private ItemStack holding = null;


   public JobFarmer(EntityNPCInterface npc) {
      super(npc);
      this.overrideMainHand = true;
   }

   public IItemStack getMainhand() {
      String name = this.npc.getDataWatcher().getWatchableObjectString(17);
      ItemStack item = this.stringToItem(name);
      return (IItemStack)(item == null?(IItemStack)this.npc.inventory.weapons.get(Integer.valueOf(0)):new ItemStackWrapper(item));
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      compound.setInteger("JobChestMode", this.chestMode);
      if(this.holding != null) {
         compound.setTag("JobHolding", this.holding.writeToNBT(new NBTTagCompound()));
      }

      return compound;
   }

   public void readFromNBT(NBTTagCompound compound) {
      this.chestMode = compound.getInteger("JobChestMode");
      if(compound.hasKey("JobHolding")) {
         this.holding = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("JobHolding"));
      }

      this.blockTicks = 1100;
   }

   public void setHolding(ItemStack item) {
      this.holding = item;
      if(this.holding == null) {
         this.npc.getDataWatcher().updateObject(17, "");
      } else {
         this.npc.getDataWatcher().updateObject(17, this.itemToString(this.holding));
      }

   }

   public boolean aiShouldExecute() {
      if(this.holding != null) {
         if(this.chestMode == 0) {
            this.setHolding((ItemStack)null);
         } else if(this.chestMode == 1) {
            if(this.chest == null) {
               this.dropItem(this.holding);
               this.setHolding((ItemStack)null);
            } else {
               this.chest();
            }
         } else if(this.chestMode == 2) {
            this.dropItem(this.holding);
            this.setHolding((ItemStack)null);
         }

         return false;
      } else if(this.ripe != null) {
         this.pluck();
         return false;
      } else {
         if(!this.waitingForBlocks && this.blockTicks++ > 1200) {
            this.blockTicks = 0;
            this.waitingForBlocks = true;
            MassBlockController.Queue(this);
         }

         if(this.ticks++ < 100) {
            return false;
         } else {
            this.ticks = 0;
            return true;
         }
      }
   }

   private void dropItem(ItemStack item) {
      EntityItem entityitem = new EntityItem(this.npc.worldObj, this.npc.posX, this.npc.posY, this.npc.posZ, item);
      entityitem.setDefaultPickupDelay();
      this.npc.worldObj.spawnEntityInWorld(entityitem);
   }

   private void chest() {
      BlockPos pos = this.chest;
      this.npc.getNavigator().tryMoveToXYZ((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1.0D);
      this.npc.getLookHelper().setLookPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 10.0F, (float)this.npc.getVerticalFaceSpeed());
      if(this.npc.nearPosition(pos) || this.walkTicks++ > 400) {
         if(this.walkTicks < 400) {
            this.npc.swingItem();
         }

         this.npc.getNavigator().clearPathEntity();
         this.ticks = 100;
         this.walkTicks = 0;
         IBlockState state = this.npc.worldObj.getBlockState(pos);
         if(state.getBlock() instanceof BlockChest) {
            TileEntityChest tile = (TileEntityChest)this.npc.worldObj.getTileEntity(pos);

            int i;
            for(i = 0; this.holding != null && i < tile.getSizeInventory(); ++i) {
               this.holding = this.mergeStack(tile, i, this.holding);
            }

            for(i = 0; this.holding != null && i < tile.getSizeInventory(); ++i) {
               ItemStack item = tile.getStackInSlot(i);
               if(item == null) {
                  tile.setInventorySlotContents(i, this.holding);
                  this.holding = null;
               }
            }

            if(this.holding != null) {
               this.dropItem(this.holding);
               this.holding = null;
            }
         } else {
            this.chest = null;
         }

         this.setHolding(this.holding);
      }

   }

   private ItemStack mergeStack(IInventory inventory, int slot, ItemStack item) {
      ItemStack item2 = inventory.getStackInSlot(slot);
      if(!NoppesUtilPlayer.compareItems(item, item2, false, false)) {
         return item;
      } else {
         int size = item2.getMaxStackSize() - item2.stackSize;
         if(size >= item.stackSize) {
            item2.stackSize += item.stackSize;
            return null;
         } else {
            item2.stackSize = item2.getMaxStackSize();
            item.stackSize -= size;
            return item != null && item.stackSize > 0?item:null;
         }
      }
   }

   private void pluck() {
      BlockPos pos = this.ripe;
      this.npc.getNavigator().tryMoveToXYZ((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1.0D);
      this.npc.getLookHelper().setLookPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 10.0F, (float)this.npc.getVerticalFaceSpeed());
      if(this.npc.nearPosition(pos) || this.walkTicks++ > 400) {
         if(this.walkTicks > 400) {
            pos = NoppesUtilServer.GetClosePos(pos, this.npc.worldObj);
            this.npc.setPositionAndUpdate((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
         }

         this.ripe = null;
         this.npc.getNavigator().clearPathEntity();
         this.ticks = 90;
         this.walkTicks = 0;
         this.npc.swingItem();
         IBlockState state = this.npc.worldObj.getBlockState(pos);
         Block b = state.getBlock();
         if(b instanceof BlockCrops) {
            this.npc.worldObj.setBlockState(pos, state.withProperty(BlockCrops.AGE, Integer.valueOf(0)));
            this.holding = new ItemStack(NpcBlockHelper.GetCrop((BlockCrops)b));
         }

         if(b instanceof BlockStem) {
            state = b.getActualState(state, this.npc.worldObj, pos);
            EnumFacing facing = (EnumFacing)state.getValue(BlockStem.FACING);
            if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
               return;
            }

            pos = pos.add(facing.getDirectionVec());
            b = this.npc.worldObj.getBlockState(pos).getBlock();
            this.npc.worldObj.setBlockToAir(pos);
            if(b != Blocks.air) {
               this.holding = new ItemStack(b);
            }
         }

         this.setHolding(this.holding);
      }

   }

   public boolean aiContinueExecute() {
      return false;
   }

   public void aiUpdateTask() {
      Iterator ite = this.trackedBlocks.iterator();

      while(ite.hasNext() && this.ripe == null) {
         BlockPos pos = (BlockPos)ite.next();
         IBlockState state = this.npc.worldObj.getBlockState(pos);
         Block b = state.getBlock();
         if(b instanceof BlockCrops) {
            if(((Integer)state.getValue(BlockCrops.AGE)).intValue() >= 7) {
               this.ripe = pos;
            }
         } else if(b instanceof BlockStem) {
            state = b.getActualState(state, this.npc.worldObj, pos);
            EnumFacing facing = (EnumFacing)state.getValue(BlockStem.FACING);
            if(facing != EnumFacing.UP) {
               this.ripe = pos;
            }
         } else {
            ite.remove();
         }
      }

      this.npc.ai.returnToStart = this.ripe == null;
      if(this.ripe != null) {
         this.npc.getNavigator().clearPathEntity();
         this.npc.getLookHelper().setLookPosition((double)this.ripe.getX(), (double)this.ripe.getY(), (double)this.ripe.getZ(), 10.0F, (float)this.npc.getVerticalFaceSpeed());
      }

   }

   public EntityNPCInterface getNpc() {
      return this.npc;
   }

   public int getRange() {
      return 16;
   }

   public void processed(List list) {
      ArrayList trackedBlocks = new ArrayList();
      BlockPos chest = null;
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         BlockData data = (BlockData)var4.next();
         Block b = data.state.getBlock();
         if(b instanceof BlockChest) {
            if(chest == null || this.npc.getDistanceSq(chest) > this.npc.getDistanceSq(data.pos)) {
               chest = data.pos;
            }
         } else if((b instanceof BlockCrops || b instanceof BlockStem) && !trackedBlocks.contains(data.pos)) {
            trackedBlocks.add(data.pos);
         }
      }

      this.chest = chest;
      this.trackedBlocks = trackedBlocks;
      this.waitingForBlocks = false;
   }

   public int getMutexBits() {
      return this.npc.getNavigator().noPath()?0:AiMutex.LOOK;
   }
}
