package noppes.npcs.blocks;

import java.util.List;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.renderer.ITileRenderer;

public class BlockBanner extends BlockContainer implements ITileRenderer {

   public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 5);
   public static final PropertyBool TOP = PropertyBool.create("top");
   private TileColorable renderTile;


   public BlockBanner() {
      super(Material.rock);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
   }

   public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      ItemStack item = player.inventory.getCurrentItem();
      if(item == null) {
         return false;
      } else {
         if(((Boolean)state.getValue(TOP)).booleanValue()) {
            pos = pos.down();
         }

         TileBanner tile = (TileBanner)par1World.getTileEntity(pos);
         if(tile.canEdit()) {
            return true;
         } else if(item.getItem() != Items.dye) {
            return false;
         } else {
            int color = EnumDyeColor.byMetadata(item.getItemDamage()).getMapColor().colorValue;
            if(tile.color != color) {
               NoppesUtilServer.consumeItemStack(1, player);
               tile.color = color;
               par1World.markBlockForUpdate(pos);
            }

            return true;
         }
      }
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 4));
   }

   public int damageDropped(IBlockState state) {
      return ((Integer)state.getValue(DAMAGE)).intValue();
   }

   public int getMetaFromState(IBlockState state) {
      return ((Integer)state.getValue(DAMAGE)).intValue() + (((Boolean)state.getValue(TOP)).booleanValue()?7:0);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(DAMAGE, Integer.valueOf(meta % 7)).withProperty(TOP, Boolean.valueOf(meta >= 7));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{DAMAGE, TOP});
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
      try {
         if(((Boolean)world.getBlockState(pos).getValue(TOP)).booleanValue()) {
            this.setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
         }
      } catch (IllegalArgumentException var4) {
         ;
      }

   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      if(!world.isAirBlock(pos.up())) {
         world.setBlockToAir(pos);
      } else {
         world.setBlockState(pos, this.getDefaultState().withProperty(TOP, Boolean.valueOf(false)).withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
         world.setBlockState(pos.up(), this.getDefaultState().withProperty(TOP, Boolean.valueOf(true)).withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
         int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
         l %= 4;
         TileBanner tile = (TileBanner)world.getTileEntity(pos);
         tile.rotation = l;
         tile.color = 15 - stack.getItemDamage();
         tile.time = System.currentTimeMillis();
         if(entity instanceof EntityPlayer && world.isRemote) {
            ((EntityPlayer)entity).addChatComponentMessage(new ChatComponentTranslation("availability.editIcon", new Object[0]));
         }
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return var2 < 7?new TileBanner():null;
   }

   public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      if(((Boolean)state.getValue(TOP)).booleanValue() && world.getBlockState(pos.down()).getBlock() == this) {
         world.setBlockToAir(pos.down());
      } else if(!((Boolean)state.getValue(TOP)).booleanValue() && world.getBlockState(pos.up()).getBlock() == this) {
         world.setBlockToAir(pos.up());
      }

   }

   public TileColorable getTile() {
      if(this.renderTile == null) {
         this.renderTile = (TileColorable)this.createNewTileEntity((World)null, 0);
      }

      return this.renderTile;
   }

}
