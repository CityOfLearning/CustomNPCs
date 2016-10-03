package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.BlockNpcDoorInterface;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockScriptedDoor extends BlockNpcDoorInterface implements IPermission {

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileScriptedDoor();
   }

   public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         BlockPos blockpos1 = state.getValue(HALF) == EnumDoorHalf.LOWER?pos:pos.down();
         IBlockState iblockstate1 = pos.equals(blockpos1)?state:world.getBlockState(blockpos1);
         if(iblockstate1.getBlock() != this) {
            return false;
         } else {
            ItemStack currentItem = player.inventory.getCurrentItem();
            if(currentItem != null && (currentItem.getItem() == CustomItems.wand || currentItem.getItem() == CustomItems.scripter || currentItem.getItem() == CustomItems.scriptedDoorTool)) {
               NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptDoor, (EntityNPCInterface)null, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
               return true;
            } else {
               TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(blockpos1);
               if(EventHooks.onScriptBlockInteract(tile, player, side.getIndex(), hitX, hitY, hitZ)) {
                  return false;
               } else {
                  this.toggleDoor(world, blockpos1, ((Boolean)iblockstate1.getValue(BlockDoor.OPEN)).equals(Boolean.valueOf(false)));
                  return true;
               }
            }
         }
      }
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if(state.getValue(HALF) == EnumDoorHalf.UPPER) {
         BlockPos flag1 = pos.down();
         IBlockState blockpos2 = worldIn.getBlockState(flag1);
         if(blockpos2.getBlock() != this) {
            worldIn.setBlockToAir(pos);
         } else if(neighborBlock != this) {
            this.onNeighborBlockChange(worldIn, flag1, blockpos2, neighborBlock);
         }
      } else {
         boolean var16 = false;
         BlockPos var17 = pos.up();
         IBlockState iblockstate2 = worldIn.getBlockState(var17);
         if(iblockstate2.getBlock() != this) {
            worldIn.setBlockToAir(pos);
            var16 = true;
         }

         if(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
            worldIn.setBlockToAir(pos);
            var16 = true;
            if(iblockstate2.getBlock() == this) {
               worldIn.setBlockToAir(var17);
            }
         }

         if(var16) {
            if(!worldIn.isRemote) {
               this.dropBlockAsItem(worldIn, pos, state, 0);
            }
         } else {
            TileScriptedDoor tile = (TileScriptedDoor)worldIn.getTileEntity(pos);
            if(!worldIn.isRemote) {
               EventHooks.onScriptBlockNeighborChanged(tile);
            }

            boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(var17);
            if((flag || neighborBlock.canProvidePower()) && neighborBlock != this && flag != ((Boolean)iblockstate2.getValue(POWERED)).booleanValue()) {
               worldIn.setBlockState(var17, iblockstate2.withProperty(POWERED, Boolean.valueOf(flag)), 2);
               if(flag != ((Boolean)state.getValue(OPEN)).booleanValue()) {
                  this.toggleDoor(worldIn, pos, flag);
               }
            }

            int power = 0;
            EnumFacing[] var11 = EnumFacing.values();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               EnumFacing enumfacing = var11[var13];
               int p = worldIn.getRedstonePower(pos.offset(enumfacing), enumfacing);
               if(p > power) {
                  power = p;
               }
            }

            tile.newPower = power;
         }
      }

   }

   public void toggleDoor(World worldIn, BlockPos pos, boolean open) {
      TileScriptedDoor tile = (TileScriptedDoor)worldIn.getTileEntity(pos);
      if(!EventHooks.onScriptBlockDoorToggle(tile)) {
         super.toggleDoor(worldIn, pos, open);
      }
   }

   public void onBlockClicked(World world, BlockPos pos, EntityPlayer playerIn) {
      if(!world.isRemote) {
         IBlockState state = world.getBlockState(pos);
         BlockPos blockpos1 = state.getValue(HALF) == EnumDoorHalf.LOWER?pos:pos.down();
         IBlockState iblockstate1 = pos.equals(blockpos1)?state:world.getBlockState(blockpos1);
         if(iblockstate1.getBlock() == this) {
            TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(blockpos1);
            EventHooks.onScriptBlockClicked(tile, playerIn);
         }
      }
   }

   public void breakBlock(World world, BlockPos pos, IBlockState state) {
      BlockPos blockpos1 = state.getValue(HALF) == EnumDoorHalf.LOWER?pos:pos.down();
      IBlockState iblockstate1 = pos.equals(blockpos1)?state:world.getBlockState(blockpos1);
      if(iblockstate1.getBlock() == this && !world.isRemote) {
         TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(pos);
         EventHooks.onScriptBlockBreak(tile);
      }
   }

   public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entityIn) {
      if(!world.isRemote) {
         TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(pos);
         EventHooks.onScriptBlockCollide(tile, entityIn);
      }
   }

   public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      BlockPos blockpos1 = state.getValue(HALF) == EnumDoorHalf.LOWER?pos:pos.down();
      IBlockState iblockstate1 = pos.equals(blockpos1)?state:world.getBlockState(blockpos1);
      if(player.capabilities.isCreativeMode && iblockstate1.getValue(HALF) == EnumDoorHalf.LOWER && iblockstate1.getBlock() == this) {
         TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(blockpos1);
         if(!world.isRemote) {
            EventHooks.onScriptBlockHarvest(tile, player);
         }

         world.setBlockToAir(blockpos1);
      }

   }

   public boolean isAllowed(EnumPacketServer e) {
      return e == EnumPacketServer.ScriptDoorDataSave;
   }
}
