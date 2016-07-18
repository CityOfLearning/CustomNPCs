//

//

package noppes.npcs.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.model.Model2DRenderer;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.constants.EnumParts;

public class LayerArms extends LayerInterface {
	private Model2DRenderer lClaw;
	private Model2DRenderer rClaw;

	public LayerArms(final RenderNPCInterface render) {
		super(render);
		createParts();
	}

	private void createParts() {
		(lClaw = new Model2DRenderer(model, 0.0f, 16.0f, 4, 4)).setRotationPoint(3.0f, 14.0f, -2.0f);
		lClaw.rotateAngleY = -1.5707964f;
		lClaw.setScale(0.25f);
		(rClaw = new Model2DRenderer(model, 0.0f, 16.0f, 4, 4)).setRotationPoint(-2.0f, 14.0f, -2.0f);
		rClaw.rotateAngleY = -1.5707964f;
		rClaw.setScale(0.25f);
	}

	@Override
	public void render(final float par2, final float par3, final float par4, final float par5, final float par6,
			final float par7) {
		final ModelPartData data = playerdata.getPartData(EnumParts.CLAWS);
		if (data == null) {
			return;
		}
		preRender(data);
		if ((data.pattern == 0) || (data.pattern == 1)) {
			GlStateManager.pushMatrix();
			model.bipedLeftArm.postRender(0.0625f);
			lClaw.render(par7);
			GlStateManager.popMatrix();
		}
		if ((data.pattern == 0) || (data.pattern == 2)) {
			GlStateManager.pushMatrix();
			model.bipedRightArm.postRender(0.0625f);
			rClaw.render(par7);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void rotate(final float par2, final float par3, final float par4, final float par5, final float par6,
			final float par7) {
	}
}
