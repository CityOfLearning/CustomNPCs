package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelChair;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockChairRenderer extends BlockRendererInterface {

   private final ModelChair model = new ModelChair();


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileColorable tile = (TileColorable)var1;
      GlStateManager.enableLighting();
      GlStateManager.pushMatrix();
      GlStateManager.disableBlend();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.68F, (float)var6 + 0.5F);
      GlStateManager.scale(1.2F, 1.1F, 1.2F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      this.setWoodTexture(var1.getBlockMetadata());
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
   }
}
