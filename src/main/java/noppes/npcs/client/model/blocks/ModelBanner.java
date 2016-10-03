package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBanner extends ModelBase {
	ModelRenderer Base;
	ModelRenderer MiddleStick;
	ModelRenderer StickDecoration;
	ModelRenderer TopDecoration;
	ModelRenderer FlagPole1;
	ModelRenderer FlagPole2;
	ModelRenderer BaseDeco1;
	ModelRenderer BaseDeco2;
	ModelRenderer BaseDeco3;
	ModelRenderer BaseDeco4;

	public ModelBanner() {
		Base = new ModelRenderer(this, 3, 1);
		Base.addBox(-7.0F, 0.0F, -7.0F, 14, 1, 14);
		Base.setRotationPoint(0.0F, 23.0F, 0.0F);

		MiddleStick = new ModelRenderer(this, 12, 2);
		MiddleStick.addBox(-1.0F, 0.0F, -1.0F, 2, 32, 2);
		MiddleStick.setRotationPoint(0.0F, -9.0F, 0.0F);

		StickDecoration = new ModelRenderer(this, 11, 12);
		StickDecoration.addBox(0.0F, 0.0F, 0.0F, 16, 3, 3);
		StickDecoration.setRotationPoint(-8.0F, -7.5F, -1.5F);

		TopDecoration = new ModelRenderer(this, 45, 19);
		TopDecoration.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
		TopDecoration.setRotationPoint(-0.5F, -10.0F, -0.5F);

		FlagPole1 = new ModelRenderer(this, 45, 19);
		FlagPole1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
		FlagPole1.setRotationPoint(-7.0F, -6.5F, -2.5F);

		FlagPole2 = new ModelRenderer(this, 45, 19);
		FlagPole2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
		FlagPole2.setRotationPoint(6.0F, -6.5F, -2.5F);

		BaseDeco1 = new ModelRenderer(this, 1, 14);
		BaseDeco1.addBox(0.0F, 0.0F, 0.0F, 12, 1, 1);
		BaseDeco1.setRotationPoint(-6.0F, 23.0F, -8.0F);

		BaseDeco2 = new ModelRenderer(this, 1, 14);
		BaseDeco2.addBox(0.0F, 0.0F, 0.0F, 12, 1, 1);
		BaseDeco2.setRotationPoint(-6.0F, 23.0F, 7.0F);

		BaseDeco3 = new ModelRenderer(this, 2, 2);
		BaseDeco3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 12);
		BaseDeco3.setRotationPoint(-8.0F, 23.0F, -6.0F);

		BaseDeco4 = new ModelRenderer(this, 2, 2);
		BaseDeco4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 12);
		BaseDeco4.setRotationPoint(7.0F, 23.0F, -6.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Base.render(f5);
		MiddleStick.render(f5);
		StickDecoration.render(f5);
		TopDecoration.render(f5);
		FlagPole1.render(f5);
		FlagPole2.render(f5);
		BaseDeco1.render(f5);
		BaseDeco2.render(f5);
		BaseDeco3.render(f5);
		BaseDeco4.render(f5);
	}
}
