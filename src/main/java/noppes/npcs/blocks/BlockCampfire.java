package noppes.npcs.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.BlockLightable;
import noppes.npcs.blocks.tiles.TileCampfire;

public class BlockCampfire extends BlockLightable {

   public BlockCampfire(boolean lit) {
      super(Blocks.cobblestone, lit);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
      if(lit) {
         this.setLightLevel(0.9375F);
      }

   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileCampfire();
   }

   public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      ItemStack item = player.inventory.getCurrentItem();
      if(item == null) {
         return true;
      } else if((item.getItem() == Items.flint || item.getItem() == Items.flint_and_steel) && this.unlitBlock() == this) {
         if(world.rand.nextInt(3) == 0 && !world.isRemote) {
            super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
         }

         CustomNpcs.proxy.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), 0.0D, 0.0D, 0.0D, 2.0F);
         if(item.getItem() == Items.flint) {
            NoppesUtilServer.consumeItemStack(1, player);
         } else {
            item.damageItem(1, player);
         }

         return true;
      } else {
         if(item.getItem() == Item.getItemFromBlock(Blocks.sand) && this.litBlock() == this) {
            super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
         }

         return true;
      }
   }

   public int maxRotation() {
      return 8;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random random) {
      if(state.getBlock() == CustomItems.campfire) {
         if(random.nextInt(36) == 0) {
            world.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), "fire.fire", 1.0F + random.nextFloat(), 0.3F + random.nextFloat() * 0.7F, false);
         }

         TileCampfire tile = (TileCampfire)world.getTileEntity(pos);
         float xOffset = 0.5F;
         float yOffset = 0.7F;
         float zOffset = 0.5F;
         double d0 = (double)((float)pos.getX() + xOffset);
         double d1 = (double)((float)pos.getY() + yOffset);
         double d2 = (double)((float)pos.getZ() + zOffset);
         GlStateManager.pushMatrix();
         CustomNpcs.proxy.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, 2.0F);
         CustomNpcs.proxy.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, 4.0F);
         GlStateManager.popMatrix();
      }
   }

   public Block unlitBlock() {
      return CustomItems.campfire_unlit;
   }

   public Block litBlock() {
      return CustomItems.campfire;
   }
}
