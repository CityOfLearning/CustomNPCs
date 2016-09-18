
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWeaponRack extends ModelBase {
	ModelRenderer Support_1;
	ModelRenderer Support_2;
	ModelRenderer Support_3;
	ModelRenderer Support_4;
	ModelRenderer Support_5;
	ModelRenderer Support_6;
	ModelRenderer Rung_1_A;
	ModelRenderer Rung_1_B;
	ModelRenderer Rung_1_C;
	ModelRenderer Rung_2_A;
	ModelRenderer Rung_2_B;
	ModelRenderer Rung_2_C;
	ModelRenderer Rung_3_A;
	ModelRenderer Rung_3_B;
	ModelRenderer Rung_3_C;
	ModelRenderer Cross_Top_1;
	ModelRenderer Cross_Top_2;
	ModelRenderer Bottom_Support_1;
	ModelRenderer Bottom_Support_2;
	ModelRenderer Middle_Support_1;

	public ModelWeaponRack() {
		(Support_1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 30, 2);
		Support_1.setRotationPoint(-5.0f, -6.9f, 5.0f);
		(Support_2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 30, 2);
		Support_2.setRotationPoint(-8.01f, -6.9f, 5.0f);
		(Support_3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 30, 2);
		Support_3.setRotationPoint(-2.0f, -6.9f, 5.0f);
		(Support_4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 30, 2);
		Support_4.setRotationPoint(1.0f, -6.9f, 5.0f);
		(Support_5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 30, 2);
		Support_5.setRotationPoint(4.0f, -6.9f, 5.0f);
		(Support_6 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 30, 2);
		Support_6.setRotationPoint(7.01f, -6.9f, 5.0f);
		(Rung_1_A = new ModelRenderer(this, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 0);
		Rung_1_A.setRotationPoint(-8.0f, 11.0f, 3.99f);
		(Rung_1_B = new ModelRenderer(this, 0, 24)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Rung_1_B.setRotationPoint(-8.0f, 11.0f, 4.0f);
		(Rung_1_C = new ModelRenderer(this, 0, 24)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Rung_1_C.setRotationPoint(-5.0f, 11.0f, 4.0f);
		(Rung_2_A = new ModelRenderer(this, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 0);
		Rung_2_A.setRotationPoint(-2.0f, 11.0f, 3.99f);
		(Rung_2_B = new ModelRenderer(this, 0, 24)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Rung_2_B.setRotationPoint(-2.0f, 11.0f, 4.0f);
		(Rung_2_C = new ModelRenderer(this, 0, 24)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Rung_2_C.setRotationPoint(1.0f, 11.0f, 4.0f);
		(Rung_3_A = new ModelRenderer(this, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 0);
		Rung_3_A.setRotationPoint(4.0f, 11.0f, 3.99f);
		(Rung_3_B = new ModelRenderer(this, 0, 24)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Rung_3_B.setRotationPoint(4.0f, 11.0f, 4.0f);
		(Rung_3_C = new ModelRenderer(this, 0, 24)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		Rung_3_C.setRotationPoint(7.0f, 11.0f, 4.0f);
		(Cross_Top_1 = new ModelRenderer(this, 6, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 2, 1);
		Cross_Top_1.setRotationPoint(-8.0f, -8.6f, 6.0f);
		setRotation(Cross_Top_1, -0.5235988f, 0.0f, 0.0f);
		(Cross_Top_2 = new ModelRenderer(this, 6, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 2, 1);
		Cross_Top_2.setRotationPoint(-8.0f, -8.6f, 6.01f);
		(Bottom_Support_1 = new ModelRenderer(this, 6, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 2, 1);
		Bottom_Support_1.setRotationPoint(-8.0f, 23.0f, 6.0f);
		setRotation(Bottom_Support_1, -1.570796f, 0.0f, 0.0f);
		(Bottom_Support_2 = new ModelRenderer(this, 6, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 2, 1);
		Bottom_Support_2.setRotationPoint(-8.0f, 23.0f, 8.0f);
		setRotation(Bottom_Support_2, -1.570796f, 0.0f, 0.0f);
		(Middle_Support_1 = new ModelRenderer(this, 6, 3)).addBox(0.0f, 0.0f, 0.0f, 16, 1, 3);
		Middle_Support_1.setRotationPoint(-8.0f, 10.0f, 7.01f);
		setRotation(Middle_Support_1, -1.570796f, 0.0f, 0.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Support_1.render(f5);
		Support_2.render(f5);
		Support_3.render(f5);
		Support_4.render(f5);
		Support_5.render(f5);
		Support_6.render(f5);
		Rung_1_A.render(f5);
		Rung_1_B.render(f5);
		Rung_1_C.render(f5);
		Rung_2_A.render(f5);
		Rung_2_B.render(f5);
		Rung_2_C.render(f5);
		Rung_3_A.render(f5);
		Rung_3_B.render(f5);
		Rung_3_C.render(f5);
		Cross_Top_1.render(f5);
		Cross_Top_2.render(f5);
		Bottom_Support_1.render(f5);
		Bottom_Support_2.render(f5);
		Middle_Support_1.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
