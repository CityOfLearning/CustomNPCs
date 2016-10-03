package noppes.npcs;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class Schematic {

   public static final int buildSize = 10000;
   public String name;
   public short width;
   public short height;
   public short length;
   private BlockPos offset;
   private BlockPos start;
   private Map[] tileEntities;
   private NBTTagList entityList;
   public NBTTagList tileList;
   public short[] blockArray;
   public byte[] blockDataArray;
   private World world;
   public boolean isBuilding;
   public boolean firstLayer;
   public int buildPos;
   public int size;
   private int rotation;


   public Schematic(String name) {
      this.offset = BlockPos.ORIGIN;
      this.start = BlockPos.ORIGIN;
      this.isBuilding = false;
      this.firstLayer = true;
      this.rotation = 0;
      this.name = name;
   }

   public void load(NBTTagCompound compound) {
      this.width = compound.getShort("Width");
      this.height = compound.getShort("Height");
      this.length = compound.getShort("Length");
      this.size = this.width * this.height * this.length;
      byte[] addId = compound.hasKey("AddBlocks")?compound.getByteArray("AddBlocks"):new byte[0];
      this.setBlockBytes(compound.getByteArray("Blocks"), addId);
      this.blockDataArray = compound.getByteArray("Data");
      this.entityList = compound.getTagList("Entities", 10);
      this.tileEntities = new Map[this.height];
      this.tileList = compound.getTagList("TileEntities", 10);

      for(int i = 0; i < this.tileList.tagCount(); ++i) {
         NBTTagCompound teTag = this.tileList.getCompoundTagAt(i);
         int x = teTag.getInteger("x");
         int y = teTag.getInteger("y");
         int z = teTag.getInteger("z");
         Object map = this.tileEntities[y];
         if(map == null) {
            this.tileEntities[y] = map = new HashMap();
         }

         ((Map)map).put(new ChunkCoordIntPair(x, z), teTag);
      }

   }

   public NBTTagCompound save() {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setShort("Width", this.width);
      compound.setShort("Height", this.height);
      compound.setShort("Length", this.length);
      byte[][] arr = this.getBlockBytes();
      compound.setByteArray("Blocks", arr[0]);
      if(arr.length > 1) {
         compound.setByteArray("AddBlocks", arr[1]);
      }

      compound.setByteArray("Data", this.blockDataArray);
      compound.setTag("TileEntities", this.tileList);
      return compound;
   }

   public void setBlockBytes(byte[] blockId, byte[] addId) {
      this.blockArray = new short[blockId.length];

      for(int index = 0; index < blockId.length; ++index) {
         short id = (short)(blockId[index] & 255);
         if(index >> 1 < addId.length) {
            if((index & 1) == 0) {
               id += (short)((addId[index >> 1] & 15) << 8);
            } else {
               id += (short)((addId[index >> 1] & 240) << 4);
            }
         }

         this.blockArray[index] = id;
      }

   }

   public byte[][] getBlockBytes() {
      byte[] blocks = new byte[this.blockArray.length];
      byte[] addBlocks = null;

      for(int i = 0; i < blocks.length; ++i) {
         short id = this.blockArray[i];
         if(id > 255) {
            if(addBlocks == null) {
               addBlocks = new byte[(blocks.length >> 1) + 1];
            }

            if((i & 1) == 0) {
               addBlocks[i >> 1] = (byte)(addBlocks[i >> 1] & 240 | id >> 8 & 15);
            } else {
               addBlocks[i >> 1] = (byte)(addBlocks[i >> 1] & 15 | (id >> 8 & 15) << 4);
            }
         }

         blocks[i] = (byte)id;
      }

      if(addBlocks == null) {
         return new byte[][]{blocks};
      } else {
         return new byte[][]{blocks, addBlocks};
      }
   }

   public NBTTagCompound getNBTSmall() {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setShort("Width", this.width);
      compound.setShort("Height", this.height);
      compound.setShort("Length", this.length);
      compound.setString("SchematicName", this.name);
      if(this.size < 125000) {
         byte[][] arr = this.getBlockBytes();
         compound.setByteArray("Blocks", arr[0]);
         if(arr.length > 1) {
            compound.setByteArray("AddBlocks", arr[1]);
         }

         compound.setByteArray("Data", this.blockDataArray);
      }

      return compound;
   }

   public void offset(int x, int y, int z) {
      this.offset = new BlockPos(x, y, z);
   }

   public void init(BlockPos pos, World world, int rotation) {
      this.start = pos;
      this.world = world;
      this.rotation = rotation;
   }

   public void build() {
      if(this.world != null && this.isBuilding) {
         long endPos = (long)(this.buildPos + 10000);
         if(endPos > (long)this.size) {
            endPos = (long)this.size;
         }

         for(; (long)this.buildPos < endPos; ++this.buildPos) {
            int x = this.buildPos % this.width;
            int z = (this.buildPos - x) / this.width % this.length;
            int y = ((this.buildPos - x) / this.width - z) / this.length;
            if(this.firstLayer) {
               this.place(x, y, z, 1);
            } else {
               this.place(x, y, z, 2);
            }
         }

         if(this.buildPos >= this.size) {
            if(this.firstLayer) {
               this.firstLayer = false;
               this.buildPos = 0;
            } else {
               this.isBuilding = false;
            }
         }

      }
   }

   public void place(int x, int y, int z, int flag) {
      int i = this.xyzToIndex(x, y, z);
      Block b = Block.getBlockById(this.blockArray[i]);
      if(b != null && (flag != 1 || b.isFullBlock() || b == Blocks.air) && (flag != 2 || !b.isFullBlock() && b != Blocks.air)) {
         int rotation = this.rotation / 90;
         BlockPos pos = this.start.add(this.rotatePos(x, y, z, rotation));
         IBlockState state = b.getStateFromMeta(this.blockDataArray[i]);
         state = this.rotationState(state, rotation);
         this.world.setBlockState(pos, state, 2);
         if(state.getBlock() instanceof ITileEntityProvider) {
            TileEntity tile = this.world.getTileEntity(pos);
            if(tile != null) {
               NBTTagCompound comp = this.getTileEntity(x, y, z, pos);
               if(comp != null) {
                  tile.readFromNBT(comp);
               }
            }
         }

      }
   }

   public IBlockState rotationState(IBlockState state, int rotation) {
      if(rotation == 0) {
         return state;
      } else {
         ImmutableSet set = state.getProperties().keySet();
         Iterator var4 = set.iterator();

         while(var4.hasNext()) {
            IProperty prop = (IProperty)var4.next();
            if(prop instanceof PropertyDirection) {
               EnumFacing direction = (EnumFacing)state.getValue(prop);
               if(direction != EnumFacing.UP && direction != EnumFacing.DOWN) {
                  for(int i = 0; i < rotation; ++i) {
                     direction = direction.rotateY();
                  }

                  return state.withProperty(prop, direction);
               }
            }
         }

         return state;
      }
   }

   public BlockPos rotatePos(int x, int y, int z, int rotation) {
      return rotation == 1?new BlockPos(this.length - z - 1, y, x):(rotation == 2?new BlockPos(this.width - x - 1, y, this.length - z - 1):(rotation == 3?new BlockPos(z, y, this.width - x - 1):new BlockPos(x, y, z)));
   }

   public NBTTagCompound getTileEntity(int x, int y, int z, BlockPos pos) {
      if(y < this.tileEntities.length && this.tileEntities[y] != null) {
         NBTTagCompound compound = (NBTTagCompound)this.tileEntities[y].get(new ChunkCoordIntPair(x, z));
         if(compound == null) {
            return null;
         } else {
            compound = (NBTTagCompound)compound.copy();
            compound.setInteger("x", pos.getX());
            compound.setInteger("y", pos.getY());
            compound.setInteger("z", pos.getZ());
            return compound;
         }
      } else {
         return null;
      }
   }

   public int getPercentage() {
      double l = (double)(this.buildPos + (this.firstLayer?0:this.size));
      return (int)(l / (double)this.size * 50.0D);
   }

   public int xyzToIndex(int x, int y, int z) {
      return (y * this.length + z) * this.width + x;
   }
}
