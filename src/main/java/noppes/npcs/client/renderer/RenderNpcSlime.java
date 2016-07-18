//

//

package noppes.npcs.client.renderer;

import net.minecraft.client.model.ModelBase;
import noppes.npcs.client.layer.LayerSlimeNpc;

public class RenderNpcSlime extends RenderNPCInterface {
	public RenderNpcSlime(final ModelBase par1ModelBase, final ModelBase par2ModelBase, final float par3) {
		super(par1ModelBase, par3);
		addLayer(new LayerSlimeNpc(this));
	}
}
