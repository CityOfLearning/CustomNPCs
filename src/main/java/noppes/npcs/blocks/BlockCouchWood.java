package noppes.npcs.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.BlockChair;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.client.renderer.ITileRenderer;

public class BlockCouchWood extends BlockContainer implements ITileRenderer {

   public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 5);
   private TileColorable renderTile;


   public BlockCouchWood() {
      super(Material.wood);
   }

   public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      ItemStack item = player.inventory.getCurrentItem();
      if(item != null && item.getItem() == Items.dye) {
         TileColorable tile = (TileColorable)par1World.getTileEntity(pos);
         int color = EnumDyeColor.byDyeDamage(item.getItemDamage()).getMetadata();
         if(tile.color != color) {
            NoppesUtilServer.consumeItemStack(1, player);
            tile.color = color;
            par1World.markBlockForUpdate(pos);
         }

         return true;
      } else {
         return BlockChair.MountBlock(par1World, pos, player);
      }
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 4));
      par3List.add(new ItemStack(par1, 1, 5));
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
      return ((Integer)state.getValue(DAMAGE)).intValue() % 7;
   }

   public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
      return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)pos.getY() + 0.5D, (double)(pos.getZ() + 1));
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      l %= 4;
      world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
      TileCouchWood tile = (TileCouchWood)world.getTileEntity(pos);
      tile.rotation = l;
      tile.color = 15 - stack.getItemDamage();
      this.updateModel(world, pos, tile);
      this.onNeighborBlockChange(world, pos.east(), state, this);
      this.onNeighborBlockChange(world, pos.west(), state, this);
      this.onNeighborBlockChange(world, pos.north(), state, this);
      this.onNeighborBlockChange(world, pos.south(), state, this);
      this.updateModel(world, pos, tile);
      world.markBlockForUpdate(pos);
   }

   public void onNeighborBlockChange(World worldObj, BlockPos pos, IBlockState state, Block block) {
      if(!worldObj.isRemote && block == this) {
         TileEntity tile = worldObj.getTileEntity(pos);
         if(tile != null && tile instanceof TileCouchWood) {
            this.updateModel(worldObj, pos, (TileCouchWood)tile);
            worldObj.markBlockForUpdate(pos);
         }
      }
   }

   private void updateModel(World world, BlockPos pos, TileCouchWood tile) {
      if(!world.isRemote) {
         int meta = tile.getBlockMetadata();
         if(tile.rotation == 0) {
            tile.hasLeft = this.compareTiles(tile, pos.west(), world, meta);
            tile.hasRight = this.compareTiles(tile, pos.east(), world, meta);
         } else if(tile.rotation == 2) {
            tile.hasLeft = this.compareTiles(tile, pos.east(), world, meta);
            tile.hasRight = this.compareTiles(tile, pos.west(), world, meta);
         } else if(tile.rotation == 1) {
            tile.hasLeft = this.compareTiles(tile, pos.north(), world, meta);
            tile.hasRight = this.compareTiles(tile, pos.south(), world, meta);
         } else if(tile.rotation == 3) {
            tile.hasLeft = this.compareTiles(tile, pos.south(), world, meta);
            tile.hasRight = this.compareTiles(tile, pos.north(), world, meta);
         }

      }
   }

   private boolean compareTiles(TileCouchWood tile, BlockPos pos, World world, int meta) {
      IBlockState state = world.getBlockState(pos);
      if(state.getBlock() != this) {
         return false;
      } else {
         int meta2 = ((Integer)state.getValue(DAMAGE)).intValue();
         if(meta2 != meta) {
            return false;
         } else {
            TileEntity tile2 = world.getTileEntity(pos);
            if(tile2 != null && tile2 instanceof TileCouchWood) {
               TileCouchWood couch = (TileCouchWood)tile2;
               return tile.rotation == couch.rotation;
            } else {
               return false;
            }
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
      return new TileCouchWood();
   }

   public TileColorable getTile() {
      if(this.renderTile == null) {
         this.renderTile = (TileColorable)this.createNewTileEntity((World)null, 0);
      }

      return this.renderTile;
   }

}
