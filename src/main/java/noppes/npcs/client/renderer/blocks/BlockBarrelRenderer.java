package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelBarrel;
import noppes.npcs.client.model.blocks.ModelBarrelLit;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockBarrelRenderer extends BlockRendererInterface {

   private final ModelBarrel model = new ModelBarrel();
   private final ModelBarrelLit modelLit = new ModelBarrelLit();
   private static final ResourceLocation resource1 = new ResourceLocation("customnpcs", "textures/models/Barrel.png");


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileColorable tile = (TileColorable)var1;
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.42F, (float)var6 + 0.5F);
      GlStateManager.scale(1.0F, 0.94F, 1.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(45 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      GlStateManager.enableCull();
      this.setWoodTexture(var1.getBlockMetadata());
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(resource1);
      this.modelLit.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
   }

}
