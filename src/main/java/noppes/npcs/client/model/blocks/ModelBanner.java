//

//

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
		(Base = new ModelRenderer(this, 3, 1)).addBox(-7.0f, 0.0f, -7.0f, 14, 1, 14);
		Base.setRotationPoint(0.0f, 23.0f, 0.0f);
		(MiddleStick = new ModelRenderer(this, 12, 2)).addBox(-1.0f, 0.0f, -1.0f, 2, 32, 2);
		MiddleStick.setRotationPoint(0.0f, -9.0f, 0.0f);
		(StickDecoration = new ModelRenderer(this, 11, 12)).addBox(0.0f, 0.0f, 0.0f, 16, 3, 3);
		StickDecoration.setRotationPoint(-8.0f, -7.5f, -1.5f);
		(TopDecoration = new ModelRenderer(this, 45, 19)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		TopDecoration.setRotationPoint(-0.5f, -10.0f, -0.5f);
		(FlagPole1 = new ModelRenderer(this, 45, 19)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		FlagPole1.setRotationPoint(-7.0f, -6.5f, -2.5f);
		(FlagPole2 = new ModelRenderer(this, 45, 19)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		FlagPole2.setRotationPoint(6.0f, -6.5f, -2.5f);
		(BaseDeco1 = new ModelRenderer(this, 1, 14)).addBox(0.0f, 0.0f, 0.0f, 12, 1, 1);
		BaseDeco1.setRotationPoint(-6.0f, 23.0f, -8.0f);
		(BaseDeco2 = new ModelRenderer(this, 1, 14)).addBox(0.0f, 0.0f, 0.0f, 12, 1, 1);
		BaseDeco2.setRotationPoint(-6.0f, 23.0f, 7.0f);
		(BaseDeco3 = new ModelRenderer(this, 2, 2)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 12);
		BaseDeco3.setRotationPoint(-8.0f, 23.0f, -6.0f);
		(BaseDeco4 = new ModelRenderer(this, 2, 2)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 12);
		BaseDeco4.setRotationPoint(7.0f, 23.0f, -6.0f);
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
