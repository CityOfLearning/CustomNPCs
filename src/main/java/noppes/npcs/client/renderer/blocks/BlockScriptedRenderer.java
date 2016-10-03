package noppes.npcs.client.renderer.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockScriptedRenderer extends BlockRendererInterface {

   private static Random random = new Random();


   public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float var8, int blockDamage) {
      TileScripted tile = (TileScripted)var1;
      GlStateManager.pushMatrix();
      GlStateManager.disableBlend();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.translate(x + 0.5D, y, z + 0.5D);
      if(this.overrideModel()) {
         GlStateManager.translate(0.0D, 0.5D, 0.0D);
         GlStateManager.scale(2.0F, 2.0F, 2.0F);
         this.renderItem(new ItemStack(CustomItems.scripted));
      } else {
         GlStateManager.rotate((float)tile.rotationY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate((float)tile.rotationX, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate((float)tile.rotationZ, 0.0F, 0.0F, 1.0F);
         GlStateManager.scale(tile.scaleX, tile.scaleY, tile.scaleZ);
         Block b = Block.getBlockFromItem(tile.itemModel.getItem());
         if(b == null) {
            GlStateManager.translate(0.0D, 0.5D, 0.0D);
            this.renderItem(tile.itemModel);
         } else if(b == CustomItems.scripted) {
            GlStateManager.translate(0.0D, 0.5D, 0.0D);
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            this.renderItem(tile.itemModel);
         } else {
            IBlockState state = b.getStateFromMeta(tile.itemModel.getItemDamage());
            this.renderBlock(tile, b, state);
            if(b.hasTileEntity(state) && !tile.renderTileErrored) {
               try {
                  if(tile.renderTile == null) {
                     TileEntity e = b.createTileEntity(this.getWorld(), state);
                     e.setPos(tile.getPos());
                     e.setWorldObj(this.getWorld());
                     ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, e, Integer.valueOf(tile.itemModel.getItemDamage()), 6);
                     ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, e, b, 7);
                     tile.renderTile = e;
                     if(e instanceof ITickable) {
                        tile.renderTileUpdate = (ITickable)e;
                     }
                  }

                  TileEntitySpecialRenderer e1 = TileEntityRendererDispatcher.instance.getSpecialRenderer(tile.renderTile);
                  if(e1 != null) {
                     e1.renderTileEntityAt(tile.renderTile, -0.5D, 0.0D, -0.5D, var8, blockDamage);
                  } else {
                     tile.renderTileErrored = true;
                  }
               } catch (Exception var14) {
                  tile.renderTileErrored = true;
               }
            }
         }
      }

      GlStateManager.popMatrix();
   }

   private void renderItem(ItemStack item) {
      Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.NONE);
   }

   private void renderBlock(TileScripted tile, Block b, IBlockState state) {
      GlStateManager.pushMatrix();
      this.bindTexture(TextureMap.locationBlocksTexture);
      GlStateManager.translate(-0.5F, 0.0F, 0.5F);
      Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, 1.0F);
      if(b.getTickRandomly() && random.nextInt(12) == 1) {
         b.randomDisplayTick(tile.getWorld(), tile.getPos(), state, random);
      }

      GlStateManager.popMatrix();
   }

   private boolean overrideModel() {
      ItemStack held = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
      return held == null?false:held.getItem() == CustomItems.wand || held.getItem() == CustomItems.scripter;
   }

}
