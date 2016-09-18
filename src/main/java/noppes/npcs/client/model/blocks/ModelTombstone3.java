
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTombstone3 extends ModelBase {
	ModelRenderer Bottom;
	ModelRenderer Piece5;
	ModelRenderer Piece2;
	ModelRenderer Piece1;
	ModelRenderer Piece4;
	ModelRenderer Piece3;
	ModelRenderer Piece7;

	public ModelTombstone3() {
		(Bottom = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 12, 5, 4);
		Bottom.setRotationPoint(-6.0f, 19.0f, -2.0f);
		(Piece5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 1, 4);
		Piece5.setRotationPoint(-4.0f, 16.0f, -2.0f);
		(Piece2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 2, 4);
		Piece2.setRotationPoint(2.0f, 17.0f, -2.0f);
		(Piece1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 6, 2, 4);
		Piece1.setRotationPoint(-5.0f, 17.0f, -2.0f);
		(Piece4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 4);
		Piece4.setRotationPoint(-5.0f, 14.0f, -2.0f);
		(Piece3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 3, 1, 4);
		Piece3.setRotationPoint(3.0f, 16.0f, -2.0f);
		(Piece7 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 4);
		Piece7.setRotationPoint(-4.0f, 15.0f, -2.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Bottom.render(f5);
		Piece5.render(f5);
		Piece2.render(f5);
		Piece1.render(f5);
		Piece4.render(f5);
		Piece3.render(f5);
		Piece7.render(f5);
	}
}
