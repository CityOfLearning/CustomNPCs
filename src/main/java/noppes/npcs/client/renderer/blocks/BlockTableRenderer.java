package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.client.model.blocks.ModelTable;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockTableRenderer extends BlockRendererInterface {

   private final ModelTable model = new ModelTable();
   private static final ResourceLocation resource1 = new ResourceLocation("customnpcs", "textures/cache/planks_oak.png");
   private static final ResourceLocation resource2 = new ResourceLocation("customnpcs", "textures/cache/planks_big_oak.png");
   private static final ResourceLocation resource3 = new ResourceLocation("customnpcs", "textures/cache/planks_spruce.png");
   private static final ResourceLocation resource4 = new ResourceLocation("customnpcs", "textures/cache/planks_birch.png");
   private static final ResourceLocation resource5 = new ResourceLocation("customnpcs", "textures/cache/planks_acacia.png");
   private static final ResourceLocation resource6 = new ResourceLocation("customnpcs", "textures/cache/planks_jungle.png");


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileColorable tile = (TileColorable)var1;
      GlStateManager.pushMatrix();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      BlockPos pos = var1.getPos();
      if(pos != BlockPos.ORIGIN) {
         boolean south = var1.getWorld().getBlockState(pos.east()).getBlock() == CustomItems.table;
         boolean north = var1.getWorld().getBlockState(pos.west()).getBlock() == CustomItems.table;
         boolean east = var1.getWorld().getBlockState(pos.south()).getBlock() == CustomItems.table;
         boolean west = var1.getWorld().getBlockState(pos.north()).getBlock() == CustomItems.table;
         this.model.Shape1.showModel = !south && !east;
         this.model.Shape3.showModel = !north && !west;
         this.model.Shape4.showModel = !north && !east;
         this.model.Shape5.showModel = !south && !west;
      } else {
         this.model.Shape1.showModel = this.model.Shape3.showModel = this.model.Shape4.showModel = this.model.Shape5.showModel = true;
      }

      this.setWoodTexture(var1.getBlockMetadata());
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      this.model.Table.render(0.0625F);
      GlStateManager.popMatrix();
   }

}
