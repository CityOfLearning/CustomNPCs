//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAnvil extends ModelBase {
	ModelRenderer Tail;
	ModelRenderer Nose1;
	ModelRenderer Nose2;
	ModelRenderer Nose3;
	ModelRenderer Nose4;
	ModelRenderer Head1;
	ModelRenderer Head2;
	ModelRenderer Neck2;
	ModelRenderer Bottom2;
	ModelRenderer Bottom3;
	ModelRenderer Foot4;

	public ModelAnvil() {
		textureWidth = 64;
		textureHeight = 32;
		(Tail = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 4);
		Tail.setRotationPoint(-7.0f, 12.0f, -2.0f);
		(Nose1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 5, 6);
		Nose1.setRotationPoint(6.0f, 10.0f, -3.0f);
		(Nose2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 4, 5);
		Nose2.setRotationPoint(7.0f, 10.0f, -2.5f);
		(Nose3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 3, 4);
		Nose3.setRotationPoint(8.0f, 10.0f, -2.0f);
		(Nose4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 2, 2);
		Nose4.setRotationPoint(9.0f, 10.0f, -1.0f);
		(Head1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 12, 4, 7);
		Head1.setRotationPoint(-6.0f, 12.0f, -3.5f);
		(Head2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 14, 2, 9);
		Head2.setRotationPoint(-8.0f, 10.0f, -4.5f);
		(Neck2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 10, 1, 6);
		Neck2.setRotationPoint(-5.0f, 16.0f, -3.0f);
		(Bottom2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 10, 2, 7);
		Bottom2.setRotationPoint(-5.0f, 20.0f, -3.5f);
		(Bottom3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 8, 3, 4);
		Bottom3.setRotationPoint(-4.0f, 17.0f, -2.0f);
		(Foot4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 14, 2, 10);
		Foot4.setRotationPoint(-7.0f, 22.0f, -5.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Tail.render(f5);
		Nose1.render(f5);
		Nose2.render(f5);
		Nose3.render(f5);
		Nose4.render(f5);
		Head1.render(f5);
		Head2.render(f5);
		Neck2.render(f5);
		Bottom2.render(f5);
		Bottom3.render(f5);
		Foot4.render(f5);
	}
}
