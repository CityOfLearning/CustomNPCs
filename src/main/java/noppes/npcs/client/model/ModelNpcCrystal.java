//

//

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelNpcCrystal extends ModelBase {
	private ModelRenderer field_41057_g;
	private ModelRenderer field_41058_h;
	private ModelRenderer field_41059_i;
	float ticks;

	public ModelNpcCrystal(final float par1) {
		field_41058_h = new ModelRenderer(this, "glass");
		field_41058_h.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
		field_41057_g = new ModelRenderer(this, "cube");
		field_41057_g.setTextureOffset(32, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
		field_41059_i = new ModelRenderer(this, "base");
		field_41059_i.setTextureOffset(0, 16).addBox(-6.0f, 16.0f, -6.0f, 12, 4, 12);
	}

	@Override
	public void render(final Entity par1Entity, final float par2, float par3, float par4, final float par5,
			final float par6, final float par7) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.translate(0.0f, -0.5f, 0.0f);
		field_41059_i.render(par7);
		final float f = par1Entity.ticksExisted + ticks;
		float f2 = (MathHelper.sin(f * 0.2f) / 2.0f) + 0.5f;
		f2 += f2 * f2;
		par3 = f * 3.0f;
		par4 = f2 * 0.2f;
		GlStateManager.rotate(par3, 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.0f, 0.1f + par4, 0.0f);
		GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
		field_41058_h.render(par7);
		final float sca = 0.875f;
		GlStateManager.scale(sca, sca, sca);
		GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
		GlStateManager.rotate(par3, 0.0f, 1.0f, 0.0f);
		field_41058_h.render(par7);
		GlStateManager.scale(sca, sca, sca);
		GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
		GlStateManager.rotate(par3, 0.0f, 1.0f, 0.0f);
		field_41057_g.render(par7);
		GlStateManager.popMatrix();
	}

	@Override
	public void setLivingAnimations(final EntityLivingBase par1EntityLiving, final float f6, final float f5,
			final float par9) {
		ticks = par9;
	}
}
