//

//

package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDigitigradeLegs extends ModelRenderer {
	private ModelRenderer rightleg;
	private ModelRenderer rightleg2;
	private ModelRenderer rightleglow;
	private ModelRenderer rightfoot;
	private ModelRenderer leftleg;
	private ModelRenderer leftleg2;
	private ModelRenderer leftleglow;
	private ModelRenderer leftfoot;
	private ModelBiped base;

	public ModelDigitigradeLegs(ModelBiped base) {
		super(base);
		this.base = base;
		(rightleg = new ModelRenderer(base, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4);
		rightleg.setRotationPoint(-2.1f, 11.0f, 0.0f);
		setRotation(rightleg, -0.3f, 0.0f, 0.0f);
		addChild(rightleg);
		(rightleg2 = new ModelRenderer(base, 0, 20)).addBox(-1.5f, -1.0f, -2.0f, 3, 7, 3);
		rightleg2.setRotationPoint(0.0f, 4.1f, 0.0f);
		setRotation(rightleg2, 1.1f, 0.0f, 0.0f);
		rightleg.addChild(rightleg2);
		(rightleglow = new ModelRenderer(base, 0, 24)).addBox(-1.5f, 0.0f, -1.0f, 3, 5, 2);
		rightleglow.setRotationPoint(0.0f, 5.0f, 0.0f);
		setRotation(rightleglow, -1.35f, 0.0f, 0.0f);
		rightleg2.addChild(rightleglow);
		(rightfoot = new ModelRenderer(base, 1, 26)).addBox(-1.5f, 0.0f, -5.0f, 3, 2, 4);
		rightfoot.setRotationPoint(0.0f, 3.7f, 1.2f);
		setRotation(rightfoot, 0.55f, 0.0f, 0.0f);
		rightleglow.addChild(rightfoot);
		leftleg = new ModelRenderer(base, 0, 16);
		leftleg.mirror = true;
		leftleg.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4);
		leftleg.setRotationPoint(2.1f, 11.0f, 0.0f);
		setRotation(leftleg, -0.3f, 0.0f, 0.0f);
		addChild(leftleg);
		leftleg2 = new ModelRenderer(base, 0, 20);
		leftleg2.mirror = true;
		leftleg2.addBox(-1.5f, -1.0f, -2.0f, 3, 7, 3);
		leftleg2.setRotationPoint(0.0f, 4.1f, 0.0f);
		setRotation(leftleg2, 1.1f, 0.0f, 0.0f);
		leftleg.addChild(leftleg2);
		leftleglow = new ModelRenderer(base, 0, 24);
		leftleglow.mirror = true;
		leftleglow.addBox(-1.5f, 0.0f, -1.0f, 3, 5, 2);
		leftleglow.setRotationPoint(0.0f, 5.0f, 0.0f);
		setRotation(leftleglow, -1.35f, 0.0f, 0.0f);
		leftleg2.addChild(leftleglow);
		leftfoot = new ModelRenderer(base, 1, 26);
		leftfoot.mirror = true;
		leftfoot.addBox(-1.5f, 0.0f, -5.0f, 3, 2, 4);
		leftfoot.setRotationPoint(0.0f, 3.7f, 1.2f);
		setRotation(leftfoot, 0.55f, 0.0f, 0.0f);
		leftleglow.addChild(leftfoot);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6,
			Entity entity) {
		rightleg.rotateAngleX = base.bipedRightLeg.rotateAngleX - 0.3f;
		leftleg.rotateAngleX = base.bipedLeftLeg.rotateAngleX - 0.3f;
		rightleg.rotationPointY = base.bipedRightLeg.rotationPointY;
		leftleg.rotationPointY = base.bipedLeftLeg.rotationPointY;
		rightleg.rotationPointZ = base.bipedRightLeg.rotationPointZ;
		leftleg.rotationPointZ = base.bipedLeftLeg.rotationPointZ;
		if (!base.isSneak) {
			ModelRenderer leftleg = this.leftleg;
			--leftleg.rotationPointY;
			ModelRenderer rightleg = this.rightleg;
			--rightleg.rotationPointY;
		}
	}
}
