package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockLightable;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileLamp;

public class BlockLamp extends BlockLightable {

   public BlockLamp(boolean lit) {
      super(Blocks.planks, lit);
      this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
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

            this.setBlockBounds(0.3F + xOffset, 0.2F, 0.3F + yOffset, 0.7F + xOffset, 0.7F, 0.7F + yOffset);
         } else {
            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
         }

      }
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileLamp();
   }

   public Block unlitBlock() {
      return CustomItems.lamp_unlit;
   }

   public Block litBlock() {
      return CustomItems.lamp;
   }
}
