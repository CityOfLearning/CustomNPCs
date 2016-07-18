//

//

package noppes.npcs.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.model.part.head.ModelHeadwear;
import noppes.npcs.entity.EntityCustomNpc;

public class LayerHeadwear extends LayerInterface implements LayerPreRender {
	private ModelHeadwear headwear;

	public LayerHeadwear(final RenderLiving render) {
		super(render);
		createParts();
	}

	private void createParts() {
		headwear = new ModelHeadwear(model);
	}

	@Override
	public void preRender(final EntityCustomNpc player) {
		model.bipedHeadwear.isHidden = (CustomNpcs.HeadWearType == 1);
		headwear.config = null;
	}

	@Override
	public void render(final float par2, final float par3, final float par4, final float par5, final float par6,
			final float par7) {
		if (CustomNpcs.HeadWearType != 1) {
			return;
		}
		if ((npc.hurtTime <= 0) && (npc.deathTime <= 0)) {
			final int color = npc.display.getTint();
			final float red = ((color >> 16) & 0xFF) / 255.0f;
			final float green = ((color >> 8) & 0xFF) / 255.0f;
			final float blue = (color & 0xFF) / 255.0f;
			GlStateManager.color(red, green, blue, 1.0f);
		}
		ClientProxy.bindTexture(npc.textureLocation);
		model.bipedHead.postRender(par7);
		headwear.render(par7);
	}

	@Override
	public void rotate(final float par2, final float par3, final float par4, final float par5, final float par6,
			final float par7) {
	}
}
