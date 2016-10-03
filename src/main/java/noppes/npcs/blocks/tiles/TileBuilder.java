package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.NBTTags;
import noppes.npcs.Schematic;
import noppes.npcs.controllers.Availability;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBuilder;

public class TileBuilder extends TileEntity implements ITickable {

   private Schematic schematic = null;
   public int rotation = 0;
   public int yOffest = 0;
   public boolean enabled = false;
   public boolean started = false;
   public boolean finished = false;
   public Availability availability = new Availability();
   private Stack positions = new Stack();
   private Stack positionsSecond = new Stack();
   public static BlockPos DrawPos = null;
   public static boolean Compiled = false;
   private int ticks = 20;


   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      if(compound.hasKey("SchematicName")) {
         this.schematic = SchematicController.Instance.load(compound.getString("SchematicName"));
      }

      Stack positions = new Stack();
      positions.addAll(NBTTags.getIntegerList(compound.getTagList("Positions", 10)));
      this.positions = positions;
      positions = new Stack();
      positions.addAll(NBTTags.getIntegerList(compound.getTagList("PositionsSecond", 10)));
      this.positionsSecond = positions;
      this.readPartNBT(compound);
   }

   public void readPartNBT(NBTTagCompound compound) {
      this.rotation = compound.getInteger("Rotation");
      this.yOffest = compound.getInteger("YOffset");
      this.enabled = compound.getBoolean("Enabled");
      this.started = compound.getBoolean("Started");
      this.finished = compound.getBoolean("Finished");
      this.availability.readFromNBT(compound.getCompoundTag("Availability"));
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      if(this.schematic != null) {
         compound.setString("SchematicName", this.schematic.name);
      }

      compound.setTag("Positions", NBTTags.nbtIntegerCollection(new ArrayList(this.positions)));
      compound.setTag("PositionsSecond", NBTTags.nbtIntegerCollection(new ArrayList(this.positionsSecond)));
      this.writePartNBT(compound);
   }

   public NBTTagCompound writePartNBT(NBTTagCompound compound) {
      compound.setInteger("Rotation", this.rotation);
      compound.setInteger("YOffset", this.yOffest);
      compound.setBoolean("Enabled", this.enabled);
      compound.setBoolean("Started", this.started);
      compound.setBoolean("Finished", this.finished);
      compound.setTag("Availability", this.availability.writeToNBT(new NBTTagCompound()));
      return compound;
   }

   @SideOnly(Side.CLIENT)
   public void setDrawSchematic(Schematic schematics) {
      this.schematic = schematics;
   }

   public void setSchematic(Schematic schematics) {
      this.schematic = schematics;
      if(schematics == null) {
         this.positions.clear();
         this.positionsSecond.clear();
      } else {
         Stack positions = new Stack();

         for(int y = 0; y < schematics.height; ++y) {
            int z;
            int x;
            for(z = 0; z < schematics.length / 2; ++z) {
               for(x = 0; x < schematics.width / 2; ++x) {
                  positions.add(0, Integer.valueOf(schematics.xyzToIndex(x, y, z)));
               }
            }

            for(z = 0; z < schematics.length / 2; ++z) {
               for(x = schematics.width / 2; x < schematics.width; ++x) {
                  positions.add(0, Integer.valueOf(schematics.xyzToIndex(x, y, z)));
               }
            }

            for(z = schematics.length / 2; z < schematics.length; ++z) {
               for(x = 0; x < schematics.width / 2; ++x) {
                  positions.add(0, Integer.valueOf(schematics.xyzToIndex(x, y, z)));
               }
            }

            for(z = schematics.length / 2; z < schematics.length; ++z) {
               for(x = schematics.width / 2; x < schematics.width; ++x) {
                  positions.add(0, Integer.valueOf(schematics.xyzToIndex(x, y, z)));
               }
            }
         }

         this.positions = positions;
         this.positionsSecond.clear();
      }
   }

   public Schematic getSchematic() {
      return this.schematic;
   }

   public boolean hasSchematic() {
      return this.schematic != null;
   }

   public void update() {
      if(!this.worldObj.isRemote && this.hasSchematic() && !this.finished) {
         --this.ticks;
         if(this.ticks <= 0) {
            this.ticks = 200;
            if(this.positions.isEmpty() && this.positionsSecond.isEmpty()) {
               this.finished = true;
            } else {
               if(!this.started) {
                  Iterator list = this.getPlayerList().iterator();

                  while(list.hasNext()) {
                     EntityPlayer player = (EntityPlayer)list.next();
                     if(this.availability.isAvailable(player)) {
                        this.started = true;
                        break;
                     }
                  }

                  if(!this.started) {
                     return;
                  }
               }

               List var5 = this.worldObj.getEntitiesWithinAABB(EntityNPCInterface.class, (new AxisAlignedBB(this.getPos(), this.getPos())).expand(32.0D, 32.0D, 32.0D));
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  EntityNPCInterface npc = (EntityNPCInterface)var6.next();
                  if(npc.advanced.job == 10) {
                     JobBuilder job = (JobBuilder)npc.jobInterface;
                     if(job.build == null) {
                        job.build = this;
                     }
                  }
               }

            }
         }
      }
   }

   private List getPlayerList() {
      return this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + 1), (double)(this.pos.getZ() + 1))).expand(10.0D, 10.0D, 10.0D));
   }

   public Stack getBlock() {
      if(this.enabled && !this.finished && this.hasSchematic()) {
         boolean bo = this.positions.isEmpty();
         Stack list = new Stack();
         int size = this.schematic.width * this.schematic.length / 4;
         if(size > 30) {
            size = 30;
         }

         for(int i = 0; i < size; ++i) {
            if(this.positions.isEmpty() && !bo || this.positionsSecond.isEmpty() && bo) {
               return list;
            }

            int pos = (bo?(Integer)this.positionsSecond.pop():(Integer)this.positions.pop()).intValue();
            if(pos < this.schematic.blockArray.length) {
               Block b = Block.getBlockById(this.schematic.blockArray[pos]);
               if(b == null) {
                  b = Blocks.air;
               }

               if(!b.isFullBlock() && !bo && b != Blocks.air) {
                  this.positionsSecond.add(0, Integer.valueOf(pos));
               } else {
                  byte meta = this.schematic.blockDataArray[pos];
                  int x = pos % this.schematic.width;
                  int z = (pos - x) / this.schematic.width % this.schematic.length;
                  int y = ((pos - x) / this.schematic.width - z) / this.schematic.length;
                  BlockPos blockPos = this.getPos().add(1, this.yOffest, 1).add(this.schematic.rotatePos(x, y, z, this.rotation));
                  IBlockState original = this.worldObj.getBlockState(blockPos);
                  if(original.getBlock() != b || b != Blocks.air && b.getMetaFromState(original) != meta) {
                     IBlockState state = b.getStateFromMeta(meta);
                     state = this.schematic.rotationState(state, this.rotation);
                     NBTTagCompound tile = null;
                     if(b instanceof ITileEntityProvider) {
                        tile = this.schematic.getTileEntity(x, y, z, blockPos);
                     }

                     list.add(0, new BlockData(blockPos, state, tile));
                  }
               }
            }
         }

         return list;
      } else {
         return null;
      }
   }

   public static void SetDrawPos(BlockPos pos) {
      DrawPos = pos;
      Compiled = false;
   }

}
