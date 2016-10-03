package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.client.model.blocks.ModelWallBanner;
import noppes.npcs.client.model.blocks.ModelWallBannerFlag;
import noppes.npcs.client.renderer.blocks.BlockBannerRenderer;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockWallBannerRenderer extends BlockRendererInterface {

   private final ModelWallBanner model = new ModelWallBanner();
   private final ModelWallBannerFlag flag = new ModelWallBannerFlag();


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileWallBanner tile = (TileWallBanner)var1;
      GlStateManager.enableLighting();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 0.4F, (float)var6 + 0.5F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      setMaterialTexture(var1.getBlockMetadata());
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      this.bindTexture(BlockBannerRenderer.resourceFlag);
      float[] color = BlockBannerRenderer.colorTable[tile.color];
      GlStateManager.color(color[0], color[1], color[2]);
      this.flag.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      if(tile.icon != null && !this.playerTooFar(tile)) {
         this.doRender(var2, var4, var6, tile.rotation, tile.icon);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.disableAlpha();
   }

   public void doRender(double par2, double par4, double par6, int meta, ItemStack iicon) {
      if(!(iicon.getItem() instanceof ItemBlock)) {
         GlStateManager.pushMatrix();
         this.bindTexture(TextureMap.locationBlocksTexture);
         GlStateManager.translate((float)par2 + 0.5F, (float)par4 + 0.2F, (float)par6 + 0.5F);
         GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate((float)(90 * meta), 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0D, 0.0D, 1.02D);
         GlStateManager.depthMask(false);
         float f2 = 0.05F;
         Minecraft mc = Minecraft.getMinecraft();
         GlStateManager.scale((double)f2, (double)f2, -0.005D);
         mc.getRenderItem().renderItemAndEffectIntoGUI(iicon, -8, -8);
         GlStateManager.depthMask(true);
         GlStateManager.popMatrix();
      }
   }

   public int specialRenderDistance() {
      return 26;
   }
}
