package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileBlood;

public class BlockBloodRenderer extends TileEntitySpecialRenderer {

   private static final ResourceLocation resource = new ResourceLocation("customnpcs:textures/blocks/npcBloodBlock_0.png");
   private static final ResourceLocation resource1 = new ResourceLocation("customnpcs:textures/blocks/npcBloodBlock_1.png");
   private static final ResourceLocation resource2 = new ResourceLocation("customnpcs:textures/blocks/npcBloodBlock_2.png");


   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float p_180535_8_, int p_180535_9_) {
      TileBlood blood = (TileBlood)tile;
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
      WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);
      int meta = tile.getBlockMetadata();
      if(meta == 1) {
         this.bindTexture(resource1);
      } else if(meta == 2) {
         this.bindTexture(resource2);
      } else {
         this.bindTexture(resource);
      }

      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      byte i = 0;
      byte j = 1;
      byte k = 1;
      if(blood.rotation == 1) {
         i = -1;
         j = 0;
         k = -1;
      } else if(blood.rotation == 2) {
         j = -1;
         k = -1;
      } else if(blood.rotation == 3) {
         i = 1;
         j = 0;
         k = 1;
      }

      if(!blood.hideSouth) {
         tessellator.pos(0.0D, 0.0D, 0.99D).tex(0.0D, 0.0D).endVertex();
         tessellator.pos(0.0D, 1.0D, 0.99D).tex((double)i, (double)j).endVertex();
         tessellator.pos(1.0D, 1.0D, 0.99D).tex((double)k, (double)k).endVertex();
         tessellator.pos(1.0D, 0.0D, 0.99D).tex((double)j, (double)i).endVertex();
      }

      if(!blood.hideNorth) {
         tessellator.pos(0.0D, 0.0D, 0.01D).tex(0.0D, 0.0D).endVertex();
         tessellator.pos(0.0D, 1.0D, 0.01D).tex((double)i, (double)j).endVertex();
         tessellator.pos(1.0D, 1.0D, 0.01D).tex((double)k, (double)k).endVertex();
         tessellator.pos(1.0D, 0.0D, 0.01D).tex((double)j, (double)i).endVertex();
      }

      if(!blood.hideEast) {
         tessellator.pos(0.99D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
         tessellator.pos(0.99D, 1.0D, 0.0D).tex((double)i, (double)j).endVertex();
         tessellator.pos(0.99D, 1.0D, 1.0D).tex((double)k, (double)k).endVertex();
         tessellator.pos(0.99D, 0.0D, 1.0D).tex((double)j, (double)i).endVertex();
      }

      if(!blood.hideWest) {
         tessellator.pos(0.01D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
         tessellator.pos(0.01D, 1.0D, 0.0D).tex((double)i, (double)j).endVertex();
         tessellator.pos(0.01D, 1.0D, 1.0D).tex((double)k, (double)k).endVertex();
         tessellator.pos(0.01D, 0.0D, 1.0D).tex((double)j, (double)i).endVertex();
      }

      if(!blood.hideBottom) {
         tessellator.pos(0.0D, 0.01D, 0.0D).tex(0.0D, 0.0D).endVertex();
         tessellator.pos(1.0D, 0.01D, 0.0D).tex((double)i, (double)j).endVertex();
         tessellator.pos(1.0D, 0.01D, 1.0D).tex((double)k, (double)k).endVertex();
         tessellator.pos(0.0D, 0.01D, 1.0D).tex((double)j, (double)i).endVertex();
      }

      if(!blood.hideTop) {
         tessellator.pos(0.0D, 0.99D, 0.0D).tex(0.0D, 0.0D).endVertex();
         tessellator.pos(1.0D, 0.99D, 0.0D).tex((double)i, (double)j).endVertex();
         tessellator.pos(1.0D, 0.99D, 1.0D).tex((double)k, (double)k).endVertex();
         tessellator.pos(0.0D, 0.99D, 1.0D).tex((double)j, (double)i).endVertex();
      }

      Tessellator.getInstance().draw();
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      GlStateManager.depthMask(true);
      GlStateManager.popMatrix();
   }

}
