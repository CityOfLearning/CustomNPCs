package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.blocks.ModelMailboxUS;
import noppes.npcs.client.model.blocks.ModelMailboxWow;

public class BlockMailboxRenderer extends TileEntitySpecialRenderer {

   private final ModelMailboxUS model = new ModelMailboxUS();
   private final ModelMailboxWow model2 = new ModelMailboxWow();
   private static final ResourceLocation text1 = new ResourceLocation("customnpcs", "textures/models/mailbox1.png");
   private static final ResourceLocation text2 = new ResourceLocation("customnpcs", "textures/models/mailbox2.png");


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      int meta = 0;
      int type = var1.getBlockMetadata();
      if(var1.getPos() != BlockPos.ORIGIN) {
         meta = var1.getBlockMetadata() | 4;
         type = var1.getBlockMetadata() >> 2;
      }

      GlStateManager.pushMatrix();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * meta), 0.0F, 1.0F, 0.0F);
      if(type == 0) {
         this.bindTexture(text1);
         this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      }

      if(type == 1) {
         this.bindTexture(text2);
         this.model2.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      }

      GlStateManager.popMatrix();
   }

}
