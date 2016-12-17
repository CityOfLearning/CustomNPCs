
package noppes.npcs.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.ModelPlayerAlt;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.model.ModelPartConfig;

public class LayerNpcCloak extends LayerInterface {
	public LayerNpcCloak(RenderLiving render) {
		super(render);
	}

	@Override
	public void render(float par2, float par3, float par4, float par5, float par6, float par7) {
		if (npc.textureCloakLocation == null) {
			if ((npc.display.getCapeTexture() == null) || npc.display.getCapeTexture().isEmpty()
					|| !(model instanceof ModelPlayerAlt)) {
				return;
			}
			npc.textureCloakLocation = new ResourceLocation(npc.display.getCapeTexture());
		}
		render.bindTexture(npc.textureCloakLocation);
		GlStateManager.pushMatrix();
		ModelPartConfig config = playerdata.getPartConfig(EnumParts.BODY);
		if (npc.isSneaking()) {
			GlStateManager.translate(0.0f, 0.2f, 0.0f);
		}
		GlStateManager.translate(config.transX, config.transY, config.transZ);
		GlStateManager.translate(0.0f, 0.0f, 0.125f);
		double d = (npc.field_20066_r + ((npc.field_20063_u - npc.field_20066_r) * par7))
				- (npc.prevPosX + ((npc.posX - npc.prevPosX) * par7));
		double d3 = (npc.field_20064_t + ((npc.field_20061_w - npc.field_20064_t) * par7))
				- (npc.prevPosZ + ((npc.posZ - npc.prevPosZ) * par7));
		float f11 = npc.prevRenderYawOffset + ((npc.renderYawOffset - npc.prevRenderYawOffset) * par7);
		double d4 = MathHelper.sin((f11 * 3.141593f) / 180.0f);
		double d5 = -MathHelper.cos((f11 * 3.141593f) / 180.0f);
		float f12 = (float) ((d * d4) + (d3 * d5)) * 100.0f;
		float f13 = (float) ((d * d5) - (d3 * d4)) * 100.0f;
		if (f12 < 0.0f) {
			f12 = 0.0f;
		}
		float f15 = 5.0f;
		if (npc.isSneaking()) {
			f15 += 25.0f;
		}
		GlStateManager.rotate(6.0f + (f12 / 2.0f) + f15, 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(f13 / 2.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(-f13 / 2.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
		((ModelPlayerAlt) model).renderCape(0.0625f);
		GlStateManager.popMatrix();
	}

	@Override
	public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
	}
}
