package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelTradingTable;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockTradingRenderer extends BlockRendererInterface {

   private final ModelTradingTable model = new ModelTradingTable();
   private static final ResourceLocation resource1 = new ResourceLocation("customnpcs", "textures/models/TradingTable.png");
   private static final ResourceLocation resource2 = new ResourceLocation("customnpcs", "textures/models/TradingArrows.png");


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileColorable tile = (TileColorable)var1;
      GlStateManager.enableLighting();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.42F, (float)var6 + 0.5F);
      GlStateManager.scale(1.0F, 0.94F, 1.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(resource1);
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      GlStateManager.translate(var2 + 0.05D, var4, var6 + 0.05D);
      GlStateManager.scale(0.9F, 0.9F, 0.9F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(resource2);
      WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);
      byte i = 0;
      byte j = 1;
      byte k = 1;
      if(tile.rotation == 0) {
         i = -1;
         j = 0;
         k = -1;
      } else if(tile.rotation == 3) {
         j = -1;
         k = -1;
      } else if(tile.rotation == 2) {
         i = 1;
         j = 0;
         k = 1;
      }

      tessellator.pos(0.0D, 0.01D, 0.0D).tex(0.0D, 0.0D).endVertex();
      tessellator.pos(1.0D, 0.01D, 0.0D).tex((double)i, (double)j).endVertex();
      tessellator.pos(1.0D, 0.01D, 1.0D).tex((double)k, (double)k).endVertex();
      tessellator.pos(0.0D, 0.01D, 1.0D).tex((double)j, (double)i).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.enableCull();
      GlStateManager.popMatrix();
   }

}
