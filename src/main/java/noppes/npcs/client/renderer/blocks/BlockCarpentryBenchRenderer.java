package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.blocks.ModelAnvil;
import noppes.npcs.client.model.blocks.ModelCarpentryBench;

public class BlockCarpentryBenchRenderer extends TileEntitySpecialRenderer {

   private final ModelCarpentryBench model = new ModelCarpentryBench();
   private final ModelAnvil anvil = new ModelAnvil();
   private static final ResourceLocation resource3 = new ResourceLocation("customnpcs", "textures/models/Steel.png");
   private static final ResourceLocation field_110631_g = new ResourceLocation("customnpcs", "textures/models/CarpentryBench.png");


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      int rotation = 0;
      boolean anvilModel = var1.getBlockMetadata() == 1;
      if(var1.getPos() != BlockPos.ORIGIN) {
         rotation = var1.getBlockMetadata() % 4;
         anvilModel = var1.getBlockMetadata() >= 4;
      }

      GlStateManager.pushMatrix();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.4F, (float)var6 + 0.5F);
      GlStateManager.scale(0.95F, 0.95F, 0.95F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * rotation), 0.0F, 1.0F, 0.0F);
      if(anvilModel) {
         this.bindTexture(resource3);
         this.anvil.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      } else {
         this.bindTexture(field_110631_g);
         this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      }

      GlStateManager.popMatrix();
   }

}
