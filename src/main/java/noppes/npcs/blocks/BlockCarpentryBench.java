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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;

public class BlockCarpentryBench extends BlockContainer implements ITileRenderer {

   public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 1);
   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);
   private TileEntity renderTile;


   public BlockCarpentryBench() {
      super(Material.wood);
   }

   public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(!par1World.isRemote) {
         player.openGui(CustomNpcs.instance, EnumGuiType.PlayerAnvil.ordinal(), par1World, pos.getX(), pos.getY(), pos.getZ());
      }

      return true;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public int damageDropped(IBlockState state) {
      return ((Integer)state.getValue(TYPE)).intValue();
   }

   public int getMetaFromState(IBlockState state) {
      return ((Integer)state.getValue(ROTATION)).intValue() + ((Integer)state.getValue(TYPE)).intValue() * 4;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(TYPE, Integer.valueOf(Integer.valueOf(meta).intValue() >> 2)).withProperty(ROTATION, Integer.valueOf(meta % 4));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{TYPE, ROTATION});
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      int var6 = MathHelper.floor_double((double)(entity.rotationYaw / 90.0F) + 0.5D) & 3;
      world.setBlockState(pos, state.withProperty(TYPE, Integer.valueOf(stack.getItemDamage())).withProperty(ROTATION, Integer.valueOf(var6)), 2);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileBlockAnvil();
   }

   public TileEntity getTile() {
      if(this.renderTile == null) {
         this.renderTile = this.createNewTileEntity((World)null, 0);
      }

      return this.renderTile;
   }

}
