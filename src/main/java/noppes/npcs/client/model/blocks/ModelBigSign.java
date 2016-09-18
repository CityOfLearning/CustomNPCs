
package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBigSign extends ModelBase {
	public ModelRenderer signBoard;

	public ModelBigSign() {
		(signBoard = new ModelRenderer(this, 0, 0)).addBox(-8.0f, -8.0f, -1.0f, 16, 16, 2, 0.0f);
	}

	public void renderSign() {
		signBoard.render(0.0625f);
	}
}
