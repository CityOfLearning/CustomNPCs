package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLongCrate extends ModelBase {
	ModelRenderer Vertical1;
	ModelRenderer Horizontal1;
	ModelRenderer Cratebody;
	ModelRenderer Horizontal2;
	ModelRenderer Vertical3;
	ModelRenderer Vertical4;
	ModelRenderer Vertical2;

	public ModelLongCrate() {
		Vertical1 = new ModelRenderer(this, 80, 0);
		Vertical1.addBox(0.0F, 0.0F, 0.0F, 4, 13, 1);
		Vertical1.setRotationPoint(-12.0F, 11.0F, 8.0F);

		Horizontal1 = new ModelRenderer(this, 0, 0);
		Horizontal1.mirror = true;
		Horizontal1.addBox(0.0F, 0.0F, 0.0F, 4, 1, 18);
		Horizontal1.setRotationPoint(8.0F, 10.0F, -9.0F);

		Cratebody = new ModelRenderer(this, 8, 0);
		Cratebody.addBox(-16.0F, 0.0F, -8.0F, 32, 13, 16);
		Cratebody.setRotationPoint(0.0F, 11.0F, 0.0F);

		Horizontal2 = new ModelRenderer(this, 0, 0);
		Horizontal2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 18);
		Horizontal2.setRotationPoint(-12.0F, 10.0F, -9.0F);

		Vertical3 = new ModelRenderer(this, 80, 0);
		Vertical3.addBox(0.0F, 0.0F, 0.0F, 4, 13, 1);
		Vertical3.setRotationPoint(-12.0F, 11.0F, -9.0F);

		Vertical4 = new ModelRenderer(this, 80, 0);
		Vertical4.mirror = true;
		Vertical4.addBox(0.0F, 0.0F, 0.0F, 4, 13, 1);
		Vertical4.setRotationPoint(8.0F, 11.0F, -9.0F);

		Vertical2 = new ModelRenderer(this, 80, 0);
		Vertical2.mirror = true;
		Vertical2.addBox(0.0F, 0.0F, 0.0F, 4, 13, 1);
		Vertical2.setRotationPoint(8.0F, 11.0F, 8.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Vertical1.render(f5);
		Horizontal1.render(f5);
		Cratebody.render(f5);
		Horizontal2.render(f5);
		Vertical3.render(f5);
		Vertical4.render(f5);
		Vertical2.render(f5);
	}
}
