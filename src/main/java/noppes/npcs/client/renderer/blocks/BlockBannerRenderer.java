package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.client.model.blocks.ModelBanner;
import noppes.npcs.client.model.blocks.ModelBannerFlag;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockBannerRenderer extends BlockRendererInterface {

   private final ModelBanner model = new ModelBanner();
   private final ModelBannerFlag flag = new ModelBannerFlag();
   public static final ResourceLocation resourceFlag = new ResourceLocation("customnpcs", "textures/models/BannerFlag.png");


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int p_180535_9_) {
      TileBanner tile = (TileBanner)var1;
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      setMaterialTexture(var1.getBlockMetadata());
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      this.bindTexture(resourceFlag);
      float[] color = colorTable[tile.color];
      GlStateManager.color(color[0], color[1], color[2]);
      this.flag.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      if(tile.icon != null && !this.playerTooFar(tile)) {
         this.doRender(var2, var4, var6, tile.rotation, tile.icon);
      }

   }

   public void doRender(double par2, double par4, double par6, int meta, ItemStack iicon) {
      if(!(iicon.getItem() instanceof ItemBlock)) {
         GlStateManager.pushMatrix();
         this.bindTexture(TextureMap.locationBlocksTexture);
         GlStateManager.translate((float)par2 + 0.5F, (float)par4 + 1.3F, (float)par6 + 0.5F);
         GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate((float)(90 * meta), 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0D, 0.0D, 0.62D);
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
