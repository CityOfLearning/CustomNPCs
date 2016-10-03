package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileTallLamp;
import noppes.npcs.client.model.blocks.ModelTallLamp;
import noppes.npcs.client.model.blocks.ModelTallLampTop;
import noppes.npcs.client.renderer.blocks.BlockBannerRenderer;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockTallLampRenderer extends BlockRendererInterface {

   private final ModelTallLamp model = new ModelTallLamp();
   private final ModelTallLampTop top = new ModelTallLampTop();
   public static final ResourceLocation resourceTop = new ResourceLocation("customnpcs", "textures/cache/wool_colored_white.png");


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileTallLamp tile = (TileTallLamp)var1;
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      setMaterialTexture(var1.getBlockMetadata());
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      this.bindTexture(resourceTop);
      float[] color = BlockBannerRenderer.colorTable[tile.color];
      GlStateManager.color(color[0], color[1], color[2]);
      this.top.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
   }

}
