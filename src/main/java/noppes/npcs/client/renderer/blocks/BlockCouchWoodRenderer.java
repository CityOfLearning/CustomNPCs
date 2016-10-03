package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.client.model.blocks.ModelCouchWoodLeft;
import noppes.npcs.client.model.blocks.ModelCouchWoodMiddle;
import noppes.npcs.client.model.blocks.ModelCouchWoodRight;
import noppes.npcs.client.model.blocks.ModelCouchWoodSingle;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockCouchWoodRenderer extends BlockRendererInterface {

   private final ModelBase model = new ModelCouchWoodMiddle();
   private final ModelBase modelLeft = new ModelCouchWoodLeft();
   private final ModelBase modelRight = new ModelCouchWoodRight();
   private final ModelBase modelCorner = new ModelCouchWoodSingle();


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileCouchWood tile = (TileCouchWood)var1;
      GlStateManager.enableLighting();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      this.setWoodTexture(var1.getBlockMetadata());
      if(tile.hasLeft && tile.hasRight) {
         this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      } else if(tile.hasLeft) {
         this.modelLeft.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      } else if(tile.hasRight) {
         this.modelRight.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      } else {
         this.modelCorner.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      }

      GlStateManager.popMatrix();
   }
}
