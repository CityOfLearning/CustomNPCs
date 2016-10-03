package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileWeaponRack;
import noppes.npcs.client.model.blocks.ModelWeaponRack;
import noppes.npcs.client.renderer.blocks.BlockRendererInterface;

public class BlockWeaponRackRenderer extends BlockRendererInterface {

   private final ModelWeaponRack model = new ModelWeaponRack();


   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int blockDamage) {
      TileWeaponRack tile = (TileWeaponRack)var1;
      GlStateManager.enableLighting();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 1.34F, (float)var6 + 0.5F);
      GlStateManager.scale(0.9F, 0.9F, 0.9F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate((float)(90 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      this.setWoodTexture(var1.getBlockMetadata());
      this.model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      if(!this.playerTooFar(tile)) {
         for(int i = 0; i < 3; ++i) {
            this.doRender(tile.getStackInSlot(i), i);
         }
      }

      GlStateManager.popMatrix();
   }

   private void doRender(ItemStack itemstack, int pos) {
      if(itemstack != null && itemstack.getItem() != null && !(itemstack.getItem() instanceof ItemBlock)) {
         GlStateManager.pushMatrix();
         GlStateManager.translate(-0.37F + (float)pos * 0.37F, 0.6F, 0.33F);
         Minecraft minecraft = Minecraft.getMinecraft();
         IBakedModel model = minecraft.getRenderItem().getItemModelMesher().getItemModel(itemstack);
         ItemTransformVec3f p_175034_1_ = model.getItemCameraTransforms().thirdPerson;
         Item item = itemstack.getItem();
         GlStateManager.rotate(p_175034_1_.rotation.x, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.scale(p_175034_1_.scale.x + ItemCameraTransforms.field_181696_h, p_175034_1_.scale.y + ItemCameraTransforms.field_181697_i, p_175034_1_.scale.z + ItemCameraTransforms.field_181698_j);
         GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
         minecraft.getRenderItem().renderItem(itemstack, TransformType.NONE);
         GlStateManager.popMatrix();
      }
   }

   public int specialRenderDistance() {
      return 26;
   }
}
