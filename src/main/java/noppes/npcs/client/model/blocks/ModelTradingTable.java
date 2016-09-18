
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class ModelTradingTable extends ModelBase {
	ModelRenderer Paper_1;
	ModelRenderer Base1;
	ModelRenderer Base2;
	ModelRenderer Pole;
	ModelRenderer Support;
	ModelRenderer Cup_1;
	ModelRenderer Cup_2;

	public ModelTradingTable() {
		textureWidth = 32;
		textureHeight = 32;
		(Paper_1 = new ModelRenderer(this, 14, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 0, 4);
		Paper_1.setRotationPoint(-7.0f, 23.8f, 0.5f);
		Paper_1.setTextureSize(32, 32);
		Paper_1.mirror = true;
		setRotation(Paper_1, 0.0f, 0.1858931f, 0.0f);
		(Base1 = new ModelRenderer(this, 0, 0)).addBox(-1.0f, 0.5f, 0.0f, 1, 1, 4);
		Base1.setRotationPoint(5.0f, 23.0f, -4.0f);
		Base1.setTextureSize(32, 32);
		Base1.mirror = true;
		setRotation(Base1, 0.0f, 0.1919862f, 0.0f);
		(Base2 = new ModelRenderer(this, 0, 0)).addBox(-1.5f, 0.5f, 1.0f, 2, 1, 2);
		Base2.setRotationPoint(5.0f, 23.0f, -4.0f);
		Base2.setTextureSize(32, 32);
		Base2.mirror = true;
		setRotation(Base2, 0.0f, 0.1919862f, 0.0f);
		(Pole = new ModelRenderer(this, 0, 0)).addBox(-1.0f, -4.5f, 1.5f, 1, 5, 1);
		Pole.setRotationPoint(5.0f, 23.0f, -4.0f);
		Pole.setTextureSize(32, 32);
		Pole.mirror = true;
		setRotation(Pole, 0.0f, 0.1919862f, 0.0f);
		(Support = new ModelRenderer(this, 0, 0)).addBox(-1.0f, -5.5f, -1.0f, 1, 1, 6);
		Support.setRotationPoint(5.0f, 23.0f, -4.0f);
		Support.setTextureSize(32, 32);
		Support.mirror = true;
		setRotation(Support, 0.0f, 0.1919862f, 0.0f);
		(Cup_1 = new ModelRenderer(this, 0, 0)).addBox(-1.5f, -3.0f, -1.5f, 2, 1, 2);
		Cup_1.setRotationPoint(5.0f, 23.0f, -4.0f);
		Cup_1.setTextureSize(32, 32);
		Cup_1.mirror = true;
		setRotation(Cup_1, 0.0f, 0.1919862f, 0.0f);
		(Cup_2 = new ModelRenderer(this, 0, 0)).addBox(-1.5f, -3.0f, 3.5f, 2, 1, 2);
		Cup_2.setRotationPoint(5.0f, 23.0f, -4.0f);
		Cup_2.setTextureSize(32, 32);
		Cup_2.mirror = true;
		setRotation(Cup_2, 0.0f, 0.1919862f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Paper_1.render(f5);
		Base1.render(f5);
		Base2.render(f5);
		Pole.render(f5);
		Support.render(f5);
		Cup_1.render(f5);
		Cup_2.render(f5);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		AxisAlignedBB bb = new AxisAlignedBB(0.34, 1.15, 0.035, 0.34, 1.25, 0.035);
		RenderGlobal.drawOutlinedBoundingBox(bb, 0, 0, 0, 255);
		bb = new AxisAlignedBB(0.28, 1.15, -0.28, 0.28, 1.25, -0.28);
		RenderGlobal.drawOutlinedBoundingBox(bb, 0, 0, 0, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
