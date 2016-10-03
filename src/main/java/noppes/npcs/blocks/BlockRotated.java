package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.renderer.ITileRenderer;

public abstract class BlockRotated extends BlockContainer implements ITileRenderer {

   private Block block;
   public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 6);
   public int renderId = -1;
   private TileEntity renderTile;


   protected BlockRotated(Block block) {
      super(block.getMaterial());
      this.block = block;
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      int l = MathHelper.floor_double((double)(placer.rotationYaw * (float)this.maxRotation() / 360.0F) + 0.5D) & this.maxRotation() - 1;
      l %= this.maxRotation();
      TileColorable tile = (TileColorable)world.getTileEntity(pos);
      tile.rotation = l;
   }

   public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
      this.setBlockBoundsBasedOnState(world, pos);
      return super.getCollisionBoundingBox(world, pos, state);
   }

   public int maxRotation() {
      return 4;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public int getRenderType() {
      return this.renderId;
   }

   public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
      return true;
   }

   public TileEntity getTile() {
      if(this.renderTile == null) {
         this.renderTile = this.createNewTileEntity((World)null, 0);
      }

      return this.renderTile;
   }

   public int getMetaFromState(IBlockState state) {
      return this.damageDropped(state);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(DAMAGE, Integer.valueOf(meta));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{DAMAGE});
   }

   public int damageDropped(IBlockState state) {
      return ((Integer)state.getValue(DAMAGE)).intValue();
   }

}
