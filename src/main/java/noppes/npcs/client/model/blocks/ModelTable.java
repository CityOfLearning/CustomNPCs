//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTable extends ModelBase {
	public ModelRenderer Shape1;
	public ModelRenderer Table;
	public ModelRenderer Shape3;
	public ModelRenderer Shape4;
	public ModelRenderer Shape5;

	public ModelTable() {
		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.mirror = true;
		Shape1.addBox(-1.0f, 0.0f, -1.0f, 2, 14, 2);
		Shape1.setRotationPoint(-6.0f, 10.0f, 6.0f);
		(Table = new ModelRenderer(this, 0, 0)).addBox(0.0f, -2.0f, 0.0f, 16, 2, 16);
		Table.setRotationPoint(-8.0f, 10.0f, -8.0f);
		(Shape3 = new ModelRenderer(this, 0, 0)).addBox(-1.0f, 0.0f, -1.0f, 2, 14, 2);
		Shape3.setRotationPoint(6.0f, 10.0f, -6.0f);
		(Shape4 = new ModelRenderer(this, 0, 0)).addBox(-1.0f, 0.0f, -1.0f, 2, 14, 2);
		Shape4.setRotationPoint(6.0f, 10.0f, 6.0f);
		Shape5 = new ModelRenderer(this, 0, 0);
		Shape5.mirror = true;
		Shape5.addBox(-1.0f, 0.0f, -1.0f, 2, 14, 2);
		Shape5.setRotationPoint(-6.0f, 10.0f, -6.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Shape1.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
	}
}
