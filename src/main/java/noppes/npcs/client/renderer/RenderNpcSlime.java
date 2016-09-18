
package noppes.npcs.client.renderer;

import net.minecraft.client.model.ModelBase;
import noppes.npcs.client.layer.LayerSlimeNpc;

public class RenderNpcSlime extends RenderNPCInterface {
	public RenderNpcSlime(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		addLayer(new LayerSlimeNpc(this));
	}
}
