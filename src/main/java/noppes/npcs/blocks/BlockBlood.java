package noppes.npcs.blocks;

import java.util.List;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileBlood;

public class BlockBlood extends BlockContainer {

   public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);


   public BlockBlood() {
      super(Material.glass);
      this.setBlockUnbreakable();
      this.setCreativeTab(CustomItems.tabMisc);
      this.setBlockBounds(0.01F, 0.01F, 0.01F, 0.99F, 0.99F, 0.99F);
      this.setLightLevel(0.08F);
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
   }

   public int damageDropped(IBlockState state) {
      return ((Integer)state.getValue(TYPE)).intValue();
   }

   public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
      return null;
   }

   public AxisAlignedBB getSelectedBoundingBox(World par1World, BlockPos pos) {
      return new AxisAlignedBB(pos, pos);
   }

   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      int var6 = MathHelper.floor_double((double)(entity.rotationYaw / 90.0F) + 0.5D) & 3;
      world.setBlockState(pos, state.withProperty(TYPE, Integer.valueOf(stack.getItemDamage())), 2);
      TileBlood tile = (TileBlood)world.getTileEntity(pos);
      tile.hideBottom = !world.isSideSolid(pos.down(), EnumFacing.UP);
      tile.hideTop = !world.isSideSolid(pos.up(), EnumFacing.DOWN);
      tile.hideNorth = !world.isSideSolid(pos.north(), EnumFacing.SOUTH);
      tile.hideSouth = !world.isSideSolid(pos.south(), EnumFacing.NORTH);
      tile.hideEast = !world.isSideSolid(pos.east(), EnumFacing.WEST);
      tile.hideWest = !world.isSideSolid(pos.west(), EnumFacing.EAST);
      if(tile.hideBottom && tile.hideTop && tile.hideNorth && tile.hideSouth && tile.hideEast && tile.hideWest) {
         tile.hideBottom = false;
      }

      tile.rotation = var6;
   }

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileBlood();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{TYPE});
   }

   public int getMetaFromState(IBlockState state) {
      return ((Integer)state.getValue(TYPE)).intValue();
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(TYPE, Integer.valueOf(meta));
   }

}
