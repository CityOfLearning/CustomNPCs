package noppes.npcs.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockLightable;
import noppes.npcs.blocks.tiles.TileCandle;
import noppes.npcs.blocks.tiles.TileColorable;

public class BlockCandle extends BlockLightable {

   public BlockCandle(boolean lit) {
      super(Blocks.planks, lit);
      this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.5F, 0.7F);
   }

   public int maxRotation() {
      return 8;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
      TileEntity tileentity = world.getTileEntity(pos);
      if(!(tileentity instanceof TileColorable)) {
         super.setBlockBoundsBasedOnState(world, pos);
      } else {
         TileColorable tile = (TileColorable)tileentity;
         if(tile.color == 2) {
            float xOffset = 0.0F;
            float yOffset = 0.0F;
            if(tile.rotation == 0) {
               yOffset = 0.2F;
            } else if(tile.rotation == 4) {
               yOffset = -0.2F;
            } else if(tile.rotation == 6) {
               xOffset = 0.2F;
            } else if(tile.rotation == 2) {
               xOffset = -0.2F;
            }

            this.setBlockBounds(0.2F + xOffset, 0.4F, 0.2F + yOffset, 0.8F + xOffset, 0.9F, 0.8F + yOffset);
         } else if(tile.color == 1) {
            this.setBlockBounds(0.1F, 0.1F, 0.1F, 0.9F, 0.8F, 0.9F);
         } else {
            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.5F, 0.7F);
         }

      }
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileCandle();
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random random) {
      if(this != this.unlitBlock()) {
         TileCandle tile = (TileCandle)world.getTileEntity(pos);
         int x = pos.getX();
         int y = pos.getY();
         int z = pos.getZ();
         if(tile.color == 1) {
            if(tile.rotation % 2 == 0) {
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.5F), (double)((float)y + 0.66F), (double)((float)z + 0.13F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.5F), (double)((float)y + 0.65F), (double)((float)z + 0.13F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.5F), (double)((float)y + 0.66F), (double)((float)z + 0.87F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.5F), (double)((float)y + 0.65F), (double)((float)z + 0.87F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.13F), (double)((float)y + 0.66F), (double)((float)z + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.13F), (double)((float)y + 0.65F), (double)((float)z + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.87F), (double)((float)y + 0.66F), (double)((float)z + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.87F), (double)((float)y + 0.65F), (double)((float)z + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
            } else {
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.24F), (double)((float)y + 0.66F), (double)((float)z + 0.24F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.24F), (double)((float)y + 0.65F), (double)((float)z + 0.24F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.76F), (double)((float)y + 0.66F), (double)((float)z + 0.76F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.76F), (double)((float)y + 0.65F), (double)((float)z + 0.76F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.24F), (double)((float)y + 0.66F), (double)((float)z + 0.76F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.24F), (double)((float)y + 0.65F), (double)((float)z + 0.76F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)x + 0.76F), (double)((float)y + 0.66F), (double)((float)z + 0.24F), 0.0D, 0.0D, 0.0D, new int[0]);
               world.spawnParticle(EnumParticleTypes.FLAME, (double)((float)x + 0.76F), (double)((float)y + 0.65F), (double)((float)z + 0.24F), 0.0D, 0.0D, 0.0D, new int[0]);
            }
         } else {
            float xOffset = 0.5F;
            float yOffset = 0.45F;
            float zOffset = 0.5F;
            if(tile.color == 2) {
               yOffset = 1.05F;
               if(tile.rotation == 0) {
                  zOffset += 0.12F;
               }

               if(tile.rotation == 4) {
                  zOffset -= 0.12F;
               }

               if(tile.rotation == 6) {
                  xOffset += 0.12F;
               }

               if(tile.rotation == 2) {
                  xOffset -= 0.12F;
               }
            }

            double d0 = (double)((float)x + xOffset);
            double d1 = (double)((float)y + yOffset);
            double d2 = (double)((float)z + zOffset);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
            world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
         }

      }
   }

   public Block unlitBlock() {
      return CustomItems.candle_unlit;
   }

   public Block litBlock() {
      return CustomItems.candle;
   }
}
