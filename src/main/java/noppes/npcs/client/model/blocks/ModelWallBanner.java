//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWallBanner extends ModelBase {
	ModelRenderer MiddleStick;
	ModelRenderer StickDecoration;
	ModelRenderer TopDecoration;
	ModelRenderer FlagPole1;
	ModelRenderer FlagPole2;

	public ModelWallBanner() {
		(MiddleStick = new ModelRenderer(this, 56, 0)).addBox(-1.0f, 0.0f, -1.0f, 2, 3, 2);
		MiddleStick.setRotationPoint(0.0f, -9.0f, 6.5f);
		(StickDecoration = new ModelRenderer(this, 11, 12)).addBox(0.0f, 0.0f, 0.0f, 16, 3, 3);
		StickDecoration.setRotationPoint(-8.0f, -7.5f, 5.0f);
		(TopDecoration = new ModelRenderer(this, 45, 19)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		TopDecoration.setRotationPoint(-0.5f, -10.0f, 6.0f);
		(FlagPole1 = new ModelRenderer(this, 45, 19)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		FlagPole1.setRotationPoint(-7.0f, -6.5f, 4.0f);
		(FlagPole2 = new ModelRenderer(this, 45, 19)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
		FlagPole2.setRotationPoint(6.0f, -6.5f, 4.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		MiddleStick.render(f5);
		StickDecoration.render(f5);
		TopDecoration.render(f5);
		FlagPole1.render(f5);
		FlagPole2.render(f5);
	}
}
