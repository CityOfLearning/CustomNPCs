package noppes.npcs.blocks;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.BlockTrigger;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileNpcContainer;
import noppes.npcs.blocks.tiles.TilePedestal;

public class BlockPedestal extends BlockTrigger {

   public BlockPedestal() {
      super(Blocks.stone);
   }

   public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(par1World.isRemote) {
         return true;
      } else {
         TilePedestal tile = (TilePedestal)par1World.getTileEntity(pos);
         ItemStack item = player.getCurrentEquippedItem();
         ItemStack weapon = tile.getStackInSlot(0);
         if(item == null && weapon != null) {
            tile.setInventorySlotContents(0, (ItemStack)null);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, weapon);
            par1World.markBlockForUpdate(pos);
            this.updateSurrounding(par1World, pos);
         } else {
            if(item == null || item.getItem() == null || !(item.getItem() instanceof ItemSword)) {
               return true;
            }

            if(item != null && weapon == null) {
               tile.setInventorySlotContents(0, item);
               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
               par1World.markBlockForUpdate(pos);
               this.updateSurrounding(par1World, pos);
            }
         }

         return true;
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

   public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
      TileEntity tileentity = world.getTileEntity(pos);
      if(!(tileentity instanceof TileColorable)) {
         super.setBlockBoundsBasedOnState(world, pos);
      } else {
         TileColorable tile = (TileColorable)tileentity;
         if(tile.rotation % 2 == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.2F, 1.0F, 0.5F, 0.8F);
         } else {
            this.setBlockBounds(0.2F, 0.0F, 0.0F, 0.8F, 0.5F, 1.0F);
         }

      }
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
      super.onBlockPlacedBy(world, pos, state, entity, stack);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TilePedestal();
   }

   public void breakBlock(World world, BlockPos pos, IBlockState state) {
      TileNpcContainer tile = (TileNpcContainer)world.getTileEntity(pos);
      if(tile != null) {
         tile.dropItems(world, pos);
         world.updateComparatorOutputLevel(pos, state.getBlock());
         super.breakBlock(world, pos, state);
      }
   }
}
