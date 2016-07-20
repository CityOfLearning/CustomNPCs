//

//

package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTombstone1 extends ModelBase {
	ModelRenderer Mid;
	ModelRenderer OuterEdge1;
	ModelRenderer OuterEdge2;
	ModelRenderer OuterEdgeTop;

	public ModelTombstone1() {
		(Mid = new ModelRenderer(this, 36, 0)).addBox(0.0f, 0.0f, 0.0f, 10, 14, 3);
		Mid.setRotationPoint(-5.0f, 10.0f, -1.5f);
		(OuterEdge1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 2, 16, 4);
		OuterEdge1.setRotationPoint(-7.0f, 8.0f, -2.0f);
		OuterEdge2 = new ModelRenderer(this, 0, 0);
		OuterEdge2.mirror = true;
		OuterEdge2.addBox(1.0f, 0.0f, 0.0f, 2, 16, 4);
		OuterEdge2.setRotationPoint(4.0f, 8.0f, -2.0f);
		(OuterEdgeTop = new ModelRenderer(this, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 10, 2, 4);
		OuterEdgeTop.setRotationPoint(-5.0f, 8.0f, -2.0f);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		Mid.render(f5);
		OuterEdge1.render(f5);
		OuterEdge2.render(f5);
		OuterEdgeTop.render(f5);
	}
}
