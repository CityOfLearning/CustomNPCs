package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockNpcRedstone extends BlockContainer implements IPermission {

   public static final PropertyBool ACTIVE = PropertyBool.create("active");


   public BlockNpcRedstone() {
      super(Material.rock);
   }

   public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(par1World.isRemote) {
         return false;
      } else {
         ItemStack currentItem = player.inventory.getCurrentItem();
         if(currentItem != null && currentItem.getItem() == CustomItems.wand && CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS)) {
            NoppesUtilServer.sendOpenGui(player, EnumGuiType.RedstoneBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
            return true;
         } else {
            return false;
         }
      }
   }

   public void onBlockAdded(World par1World, BlockPos pos, IBlockState state) {
      par1World.notifyNeighborsOfStateChange(pos, this);
      par1World.notifyNeighborsOfStateChange(pos.down(), this);
      par1World.notifyNeighborsOfStateChange(pos.up(), this);
      par1World.notifyNeighborsOfStateChange(pos.west(), this);
      par1World.notifyNeighborsOfStateChange(pos.east(), this);
      par1World.notifyNeighborsOfStateChange(pos.south(), this);
      par1World.notifyNeighborsOfStateChange(pos.north(), this);
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityliving, ItemStack item) {
      if(entityliving instanceof EntityPlayer && !world.isRemote) {
         NoppesUtilServer.sendOpenGui((EntityPlayer)entityliving, EnumGuiType.RedstoneBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
      }

   }

   public void onBlockDestroyedByPlayer(World par1World, BlockPos pos, IBlockState state) {
      this.onBlockAdded(par1World, pos, state);
   }

   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return this.isActivated(state);
   }

   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return this.isActivated(state);
   }

   public boolean canProvidePower() {
      return true;
   }

   public int getMetaFromState(IBlockState state) {
      return ((Boolean)state.getValue(ACTIVE)).booleanValue()?1:0;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(ACTIVE, Boolean.valueOf(false));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{ACTIVE});
   }

   public int isActivated(IBlockState state) {
      return ((Boolean)state.getValue(ACTIVE)).booleanValue()?15:0;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileRedstoneBlock();
   }

   public int getRenderType() {
      return 3;
   }

   public boolean isAllowed(EnumPacketServer e) {
      return e == EnumPacketServer.SaveTileEntity;
   }

}
