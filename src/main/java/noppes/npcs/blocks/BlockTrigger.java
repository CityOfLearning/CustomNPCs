package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.BlockRotated;
import noppes.npcs.blocks.tiles.TileColorable;

public abstract class BlockTrigger extends BlockRotated {

   protected BlockTrigger(Block block) {
      super(block);
   }

   public boolean canProvidePower() {
      return true;
   }

   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return this.getWeakPower(worldIn, pos, state, side);
   }

   public int getWeakPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) {
      TileColorable tile = (TileColorable)world.getTileEntity(pos);
      return tile != null?tile.powerProvided():0;
   }

   public void updateSurrounding(World par1World, BlockPos pos) {
      par1World.notifyNeighborsOfStateChange(pos, this);
      par1World.notifyNeighborsOfStateChange(pos.down(), this);
      par1World.notifyNeighborsOfStateChange(pos.up(), this);
      par1World.notifyNeighborsOfStateChange(pos.west(), this);
      par1World.notifyNeighborsOfStateChange(pos.east(), this);
      par1World.notifyNeighborsOfStateChange(pos.south(), this);
      par1World.notifyNeighborsOfStateChange(pos.north(), this);
   }
}
