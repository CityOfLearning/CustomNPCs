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
		Rock1 = new ModelRenderer(this, 0, 0);
		Rock1.addBox(0.0F, 0.0F, 0.0F, 3, 2, 3);
		Rock1.setRotationPoint(5.0F, 22.0F, 3.0F);
		setRotation(Rock1, 0.0F, -0.7435722F, 0.0F);

		Rock2 = new ModelRenderer(this, 0, 0);
		Rock2.addBox(0.0F, 0.0F, 0.0F, 3, 3, 6);
		Rock2.setRotationPoint(5.0F, 21.0F, -3.0F);

		Rock3 = new ModelRenderer(this, 0, 0);
		Rock3.addBox(0.0F, 0.0F, 0.0F, 5, 3, 3);
		Rock3.setRotationPoint(2.5F, 21.0F, -8.0F);
		setRotation(Rock3, 0.0F, -0.5576792F, 0.0F);

		Rock4 = new ModelRenderer(this, 0, 0);
		Rock4.addBox(0.0F, 0.0F, 0.0F, 3, 2, 2);
		Rock4.setRotationPoint(-2.0F, 22.0F, -7.5F);

		Rock5 = new ModelRenderer(this, 0, 0);
		Rock5.addBox(0.0F, 0.0F, -2.0F, 7, 2, 2);
		Rock5.setRotationPoint(-3.5F, 22.0F, 7.8F);

		Rock6 = new ModelRenderer(this, 0, 0);
		Rock6.addBox(0.0F, 0.0F, 0.0F, 3, 3, 3);
		Rock6.setRotationPoint(-5.0F, 21.0F, 3.0F);
		setRotation(Rock6, 0.0F, -1.003822F, 0.0F);

		Rock7 = new ModelRenderer(this, 0, 0);
		Rock7.addBox(0.0F, 0.0F, 0.0F, 4, 3, 3);
		Rock7.setRotationPoint(-7.0F, 21.0F, -4.5F);
		setRotation(Rock7, 0.0F, 0.8551081F, 0.0F);

		Rock8 = new ModelRenderer(this, 0, 0);
		Rock8.addBox(0.0F, 0.0F, 0.0F, 3, 2, 6);
		Rock8.setRotationPoint(-8.0F, 22.0F, -3.0F);

		Log3 = new ModelRenderer(this, 0, 16);
		Log3.addBox(0.0F, 0.0F, 0.0F, 2, 9, 2);
		Log3.setRotationPoint(0.0F, 16.0F, -1.0F);
		setRotation(Log3, 0.3717861F, -1.487144F, -0.1487144F);

		Log1 = new ModelRenderer(this, 8, 21);
		Log1.addBox(0.0F, 0.0F, 0.0F, 2, 9, 2);
		Log1.setRotationPoint(0.0F, 16.0F, -1.0F);
		setRotation(Log1, -0.1487144F, 0.0F, -0.3717861F);

		Log4 = new ModelRenderer(this, 0, 16);
		Log4.addBox(0.0F, 0.0F, -2.0F, 2, 9, 2);
		Log4.setRotationPoint(1.0F, 16.0F, 1.0F);
		setRotation(Log4, -0.3346075F, 3.141593F, 0.0F);

		Log2 = new ModelRenderer(this, 0, 20);
		Log2.addBox(0.0F, 0.0F, 0.0F, 2, 9, 2);
		Log2.setRotationPoint(1.0F, 16.0F, -1.0F);
		setRotation(Log2, 0.2974289F, 3.141593F, 0.0F);
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
