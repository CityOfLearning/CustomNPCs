//

//

package noppes.npcs.client.renderer;

import noppes.npcs.client.model.ModelNpcCrystal;

public class RenderNpcCrystal extends RenderNPCInterface {
	ModelNpcCrystal mainmodel;

	public RenderNpcCrystal(final ModelNpcCrystal model) {
		super(model, 0.0f);
		mainmodel = model;
	}
}
