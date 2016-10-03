package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockCopy extends BlockContainer implements IPermission {

   public BlockCopy() {
      super(Material.rock);
   }

   public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(par1World.isRemote) {
         return true;
      } else {
         ItemStack currentItem = player.inventory.getCurrentItem();
         if(currentItem != null && currentItem.getItem() == CustomItems.wand) {
            NoppesUtilServer.sendOpenGui(player, EnumGuiType.CopyBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
         }

         return true;
      }
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      if(entity instanceof EntityPlayer && !world.isRemote) {
         NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.CopyBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileCopy();
   }

   public boolean isAllowed(EnumPacketServer e) {
      return e == EnumPacketServer.GetTileEntity || e == EnumPacketServer.SchematicsTile || e == EnumPacketServer.SchematicsTileSave || e == EnumPacketServer.SaveTileEntity;
   }
}
