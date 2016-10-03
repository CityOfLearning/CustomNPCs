package noppes.npcs.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockScripted extends BlockContainer implements IPermission {

   public BlockScripted() {
      super(Material.rock);
      this.setBlockBounds(0.001F, 0.001F, 0.001F, 0.998F, 0.998F, 0.998F);
   }

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileScripted();
   }

   public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         ItemStack currentItem = player.inventory.getCurrentItem();
         if(currentItem != null && (currentItem.getItem() == CustomItems.wand || currentItem.getItem() == CustomItems.scripter)) {
            NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
            return true;
         } else {
            TileScripted tile = (TileScripted)world.getTileEntity(pos);
            return !EventHooks.onScriptBlockInteract(tile, player, side.getIndex(), hitX, hitY, hitZ);
         }
      }
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      if(entity instanceof EntityPlayer && !world.isRemote) {
         NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.ScriptBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
      }

   }

   public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entityIn) {
      if(!world.isRemote) {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         EventHooks.onScriptBlockCollide(tile, entityIn);
      }
   }

   public void fillWithRain(World world, BlockPos pos) {
      if(!world.isRemote) {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         EventHooks.onScriptBlockRainFill(tile);
      }
   }

   public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
      if(!world.isRemote) {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         fallDistance = EventHooks.onScriptBlockFallenUpon(tile, entity, fallDistance);
         super.onFallenUpon(world, pos, entity, fallDistance);
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
      if(!world.isRemote) {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         EventHooks.onScriptBlockClicked(tile, player);
      }
   }

   public void breakBlock(World world, BlockPos pos, IBlockState state) {
      if(!world.isRemote) {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         EventHooks.onScriptBlockBreak(tile);
      }
   }

   public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      if(world.isRemote) {
         return super.removedByPlayer(world, pos, player, willHarvest);
      } else {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         return EventHooks.onScriptBlockHarvest(tile, player)?false:super.removedByPlayer(world, pos, player, willHarvest);
      }
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
      if(!world.isRemote) {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         if(EventHooks.onScriptBlockExploded(tile)) {
            return;
         }
      }

      super.onBlockExploded(world, pos, explosion);
   }

   public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
      if(!world.isRemote) {
         TileScripted tile = (TileScripted)world.getTileEntity(pos);
         EventHooks.onScriptBlockNeighborChanged(tile);
         int power = 0;
         EnumFacing[] var7 = EnumFacing.values();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            EnumFacing enumfacing = var7[var9];
            int p = world.getRedstonePower(pos.offset(enumfacing), enumfacing);
            if(p > power) {
               power = p;
            }
         }

         if(tile.prevPower != power && tile.powering <= 0) {
            tile.newPower = power;
         }

      }
   }

   public boolean canProvidePower() {
      return true;
   }

   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return this.getStrongPower(worldIn, pos, state, side);
   }

   public int getStrongPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) {
      return ((TileScripted)world.getTileEntity(pos)).activePowering;
   }

   public boolean isLadder(IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
      return ((TileScripted)world.getTileEntity(pos)).isLadder;
   }

   public boolean canCreatureSpawn(IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
      return true;
   }

   public int getLightValue(IBlockAccess world, BlockPos pos) {
      TileScripted tile = (TileScripted)world.getTileEntity(pos);
      return tile == null?0:tile.lightValue;
   }

   public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity) {
      return super.canEntityDestroy(world, pos, entity);
   }

   public float getEnchantPowerBonus(World world, BlockPos pos) {
      return super.getEnchantPowerBonus(world, pos);
   }

   public float getBlockHardness(World worldIn, BlockPos pos) {
      return this.blockHardness;
   }

   public boolean isAllowed(EnumPacketServer e) {
      return e == EnumPacketServer.SaveTileEntity || e == EnumPacketServer.ScriptBlockDataSave;
   }
}
