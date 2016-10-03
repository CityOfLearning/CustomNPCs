package noppes.npcs.blocks;

import java.util.ArrayList;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;

public class BlockMailbox extends BlockContainer implements ITileRenderer {

   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 8);
   public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);
   private TileEntity renderTile;


   public BlockMailbox() {
      super(Material.iron);
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
   }

   public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(!par1World.isRemote) {
         Server.sendData((EntityPlayerMP)player, EnumPacketClient.GUI, new Object[]{EnumGuiType.PlayerMailbox, Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ())});
      }

      return true;
   }

   public ArrayList getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ArrayList ret = new ArrayList();
      int damage = ((Integer)state.getValue(TYPE)).intValue();
      ret.add(new ItemStack(this, 1, damage));
      return ret;
   }

   public int damageDropped(IBlockState state) {
      return ((Integer)state.getValue(TYPE)).intValue();
   }

   public int getMetaFromState(IBlockState state) {
      return ((Integer)state.getValue(ROTATION)).intValue() | ((Integer)state.getValue(TYPE)).intValue() << 2;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(TYPE, Integer.valueOf(Integer.valueOf(meta).intValue() >> 2)).withProperty(ROTATION, Integer.valueOf(meta | 4));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{TYPE, ROTATION});
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      l %= 4;
      world.setBlockState(pos, state.withProperty(TYPE, Integer.valueOf(stack.getItemDamage())).withProperty(ROTATION, Integer.valueOf(l)), 2);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileMailbox();
   }

   public TileEntity getTile() {
      if(this.renderTile == null) {
         this.renderTile = this.createNewTileEntity((World)null, 0);
      }

      return this.renderTile;
   }

}
