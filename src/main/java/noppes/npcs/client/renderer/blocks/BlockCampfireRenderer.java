package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelCampfire;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockCampfireRenderer extends BlockRendererInterface {

   private final ModelCampfire model = new ModelCampfire();


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileColorable tile = (TileColorable)var1;
      GlStateManager.enableLighting();
      GlStateManager.pushMatrix();
      GlStateManager.disableBlend();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(45 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(PlanksOak);
      this.model.renderLog(0.0625F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(Stone);
      this.model.renderRock(0.0625F);
      GlStateManager.popMatrix();
   }
}
