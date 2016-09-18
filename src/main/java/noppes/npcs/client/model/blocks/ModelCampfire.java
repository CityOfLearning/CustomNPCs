
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCampfire extends ModelBase {
	ModelRenderer Rock1;
	ModelRenderer Rock2;
	ModelRenderer Rock3;
	ModelRenderer Rock4;
	ModelRenderer Rock5;
	ModelRenderer Rock6;
	ModelRenderer Rock7;
	ModelRenderer Rock8;
	ModelRenderer Log3;
	ModelRenderer Log1;
	ModelRenderer Log4;
	ModelRenderer Log2;

	public ModelCampfire() {
		(Rock1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 3);
		Rock1.setRotationPoint(5.0f, 22.0f, 3.0f);
		setRotation(Rock1, 0.0f, -0.7435722f, 0.0f);
		(Rock2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 3, 6);
		Rock2.setRotationPoint(5.0f, 21.0f, -3.0f);
		(Rock3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 5, 3, 3);
		Rock3.setRotationPoint(2.5f, 21.0f, -8.0f);
		setRotation(Rock3, 0.0f, -0.5576792f, 0.0f);
		(Rock4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 2);
		Rock4.setRotationPoint(-2.0f, 22.0f, -7.5f);
		(Rock5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, -2.0f, 7, 2, 2);
		Rock5.setRotationPoint(-3.5f, 22.0f, 7.8f);
		(Rock6 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 3, 3);
		Rock6.setRotationPoint(-5.0f, 21.0f, 3.0f);
		setRotation(Rock6, 0.0f, -1.003822f, 0.0f);
		(Rock7 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 3, 3);
		Rock7.setRotationPoint(-7.0f, 21.0f, -4.5f);
		setRotation(Rock7, 0.0f, 0.8551081f, 0.0f);
		(Rock8 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 6);
		Rock8.setRotationPoint(-8.0f, 22.0f, -3.0f);
		(Log3 = new ModelRenderer(this, 0, 16)).addBox(0.0f, 0.0f, 0.0f, 2, 9, 2);
		Log3.setRotationPoint(0.0f, 16.0f, -1.0f);
		setRotation(Log3, 0.3717861f, -1.487144f, -0.1487144f);
		(Log1 = new ModelRenderer(this, 8, 21)).addBox(0.0f, 0.0f, 0.0f, 2, 9, 2);
		Log1.setRotationPoint(0.0f, 16.0f, -1.0f);
		setRotation(Log1, -0.1487144f, 0.0f, -0.3717861f);
		(Log4 = new ModelRenderer(this, 0, 16)).addBox(0.0f, 0.0f, -2.0f, 2, 9, 2);
		Log4.setRotationPoint(1.0f, 16.0f, 1.0f);
		setRotation(Log4, -0.3346075f, 3.141593f, 0.0f);
		(Log2 = new ModelRenderer(this, 0, 20)).addBox(0.0f, 0.0f, 0.0f, 2, 9, 2);
		Log2.setRotationPoint(1.0f, 16.0f, -1.0f);
		setRotation(Log2, 0.2974289f, 3.141593f, 0.0f);
	}

	public void renderLog(float f5) {
		Log3.render(f5);
		Log1.render(f5);
		Log4.render(f5);
		Log2.render(f5);
	}

	public void renderRock(float f5) {
		Rock1.render(f5);
		Rock2.render(f5);
		Rock3.render(f5);
		Rock4.render(f5);
		Rock5.render(f5);
		Rock6.render(f5);
		Rock7.render(f5);
		Rock8.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
