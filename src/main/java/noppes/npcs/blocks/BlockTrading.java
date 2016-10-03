package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileTrading;
import noppes.npcs.client.renderer.ITileRenderer;
import noppes.npcs.constants.EnumGuiType;

public class BlockTrading extends BlockContainer implements ITileRenderer {

   private TileTrading renderTile;


   public BlockTrading() {
      super(Material.wood);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.4F, 1.0F);
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      int l = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      l %= 4;
      TileColorable tile = (TileColorable)world.getTileEntity(pos);
      tile.rotation = l;
   }

   public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         TileTrading tile = (TileTrading)world.getTileEntity(pos);
         if(tile.isFull()) {
            player.addChatComponentMessage(new ChatComponentTranslation("trader.busy", new Object[0]));
            return false;
         } else {
            player.openGui(CustomNpcs.instance, EnumGuiType.TradingBlock.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            tile.addTrader(player);
            return true;
         }
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
      return null;
   }

   public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
      return false;
   }

   public int getRenderType() {
      return -1;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileTrading();
   }

   public TileEntity getTile() {
      if(this.renderTile == null) {
         this.renderTile = (TileTrading)this.createNewTileEntity((World)null, 0);
      }

      return this.renderTile;
   }
}
