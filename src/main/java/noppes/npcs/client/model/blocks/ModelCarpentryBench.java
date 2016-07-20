//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCarpentryBench extends ModelBase {
	ModelRenderer Leg1;
	ModelRenderer Leg2;
	ModelRenderer Leg3;
	ModelRenderer Leg4;
	ModelRenderer Bottom_plate;
	ModelRenderer Desktop;
	ModelRenderer Backboard;
	ModelRenderer Vice_Jaw1;
	ModelRenderer Vice_Jaw2;
	ModelRenderer Vice_Base1;
	ModelRenderer Vice_Base2;
	ModelRenderer Vice_Crank;
	ModelRenderer Vice_Screw;
	ModelRenderer Blueprint;

	public ModelCarpentryBench() {
		textureWidth = 128;
		textureHeight = 64;
		(Leg1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 14, 2);
		Leg1.setRotationPoint(6.0f, 10.0f, 5.0f);
		(Leg2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 14, 2);
		Leg2.setRotationPoint(6.0f, 10.0f, -5.0f);
		(Leg3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 14, 2);
		Leg3.setRotationPoint(-8.0f, 10.0f, 5.0f);
		(Leg4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 14, 2);
		Leg4.setRotationPoint(-8.0f, 10.0f, -5.0f);
		(Bottom_plate = new ModelRenderer(this, 0, 24)).addBox(0.0f, 0.0f, 0.0f, 14, 1, 10);
		Bottom_plate.setRotationPoint(-7.0f, 21.0f, -4.0f);
		Bottom_plate.setTextureSize(130, 64);
		(Desktop = new ModelRenderer(this, 0, 3)).addBox(0.0f, 0.0f, 0.0f, 18, 2, 13);
		Desktop.setRotationPoint(-9.0f, 9.0f, -6.0f);
		(Backboard = new ModelRenderer(this, 0, 18)).addBox(-1.0f, 0.0f, 0.0f, 18, 5, 1);
		Backboard.setRotationPoint(-8.0f, 7.0f, 7.0f);
		(Vice_Jaw1 = new ModelRenderer(this, 54, 18)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 1);
		Vice_Jaw1.setRotationPoint(3.0f, 6.0f, -8.0f);
		(Vice_Jaw2 = new ModelRenderer(this, 54, 21)).addBox(0.0f, 0.0f, 0.0f, 3, 2, 1);
		Vice_Jaw2.setRotationPoint(3.0f, 6.0f, -6.0f);
		(Vice_Base1 = new ModelRenderer(this, 38, 30)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 3);
		Vice_Base1.setRotationPoint(3.0f, 8.0f, -5.0f);
		(Vice_Base2 = new ModelRenderer(this, 38, 25)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 2);
		Vice_Base2.setRotationPoint(4.0f, 7.0f, -5.0f);
		(Vice_Crank = new ModelRenderer(this, 54, 24)).addBox(0.0f, 0.0f, 0.0f, 1, 5, 1);
		Vice_Crank.setRotationPoint(6.0f, 6.0f, -9.0f);
		(Vice_Screw = new ModelRenderer(this, 44, 25)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 4);
		Vice_Screw.setRotationPoint(4.0f, 8.0f, -8.0f);
		(Blueprint = new ModelRenderer(this, 31, 18)).addBox(0.0f, 0.0f, 0.0f, 8, 0, 7);
		Blueprint.setRotationPoint(0.0f, 9.0f, 1.0f);
		setRotation(Blueprint, 0.3271718f, 0.1487144f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Leg1.render(f5);
		Leg2.render(f5);
		Leg3.render(f5);
		Leg4.render(f5);
		Bottom_plate.render(f5);
		Desktop.render(f5);
		Backboard.render(f5);
		Vice_Jaw1.render(f5);
		Vice_Jaw2.render(f5);
		Vice_Base1.render(f5);
		Vice_Base2.render(f5);
		Vice_Crank.render(f5);
		Vice_Screw.render(f5);
		Blueprint.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
