
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBarrel extends ModelBase {
	ModelRenderer Plank1;
	ModelRenderer Plank2;
	ModelRenderer Plank3;
	ModelRenderer Plank4;
	ModelRenderer Plank5;
	ModelRenderer Plank6;
	ModelRenderer Plank7;
	ModelRenderer Plank8;
	ModelRenderer Plank9;
	ModelRenderer Plank10;
	ModelRenderer Plank11;
	ModelRenderer Plank12;

	public ModelBarrel() {
		(Plank1 = new ModelRenderer(this, 10, 0)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank1.setRotationPoint(0.0f, 7.01f, 0.0f);
		setRotation(Plank1, 0.0f, 0.0f, 1.570796f);
		(Plank2 = new ModelRenderer(this, 10, 8)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank2.setRotationPoint(0.0f, 7.0f, 0.0f);
		setRotation(Plank2, 0.0f, 0.5235988f, 1.570796f);
		(Plank3 = new ModelRenderer(this, 10, 0)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank3.setRotationPoint(0.0f, 7.01f, 0.0f);
		setRotation(Plank3, 0.0f, 1.047198f, 1.570796f);
		(Plank4 = new ModelRenderer(this, 10, 8)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank4.setRotationPoint(0.0f, 7.0f, 0.0f);
		setRotation(Plank4, 0.0f, 1.570796f, 1.570796f);
		(Plank5 = new ModelRenderer(this, 10, 0)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank5.setRotationPoint(0.0f, 7.01f, 0.0f);
		setRotation(Plank5, 0.0f, 2.094395f, 1.570796f);
		(Plank6 = new ModelRenderer(this, 10, 8)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank6.setRotationPoint(0.0f, 7.0f, 0.0f);
		setRotation(Plank6, 0.0f, 2.617994f, 1.570796f);
		(Plank7 = new ModelRenderer(this, 10, 0)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank7.setRotationPoint(0.0f, 7.01f, 0.0f);
		setRotation(Plank7, 0.0f, 3.150901f, 1.570796f);
		(Plank8 = new ModelRenderer(this, 10, 8)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank8.setRotationPoint(0.0f, 7.0f, 0.0f);
		setRotation(Plank8, 0.0f, -2.617994f, 1.570796f);
		(Plank9 = new ModelRenderer(this, 10, 0)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank9.setRotationPoint(0.0f, 7.01f, 0.0f);
		setRotation(Plank9, 0.0f, -2.094395f, 1.570796f);
		(Plank10 = new ModelRenderer(this, 10, 8)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank10.setRotationPoint(0.0f, 7.0f, 0.0f);
		setRotation(Plank10, 0.0f, -1.570796f, 1.570796f);
		(Plank11 = new ModelRenderer(this, 10, 0)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank11.setRotationPoint(0.0f, 7.01f, 0.0f);
		setRotation(Plank11, 0.0f, -1.047198f, 1.570796f);
		(Plank12 = new ModelRenderer(this, 10, 0)).addBox(0.0f, 6.5f, -2.0f, 17, 1, 4);
		Plank12.setRotationPoint(0.0f, 7.0f, 0.0f);
		setRotation(Plank12, 0.0f, -0.5235988f, 1.570796f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Plank1.render(f5);
		Plank2.render(f5);
		Plank3.render(f5);
		Plank4.render(f5);
		Plank5.render(f5);
		Plank6.render(f5);
		Plank7.render(f5);
		Plank8.render(f5);
		Plank9.render(f5);
		Plank10.render(f5);
		Plank11.render(f5);
		Plank12.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = y;
		model.rotateAngleY = x;
		model.rotateAngleZ = z;
	}
}
