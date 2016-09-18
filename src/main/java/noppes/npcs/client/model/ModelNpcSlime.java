
package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelNpcSlime extends ModelBase {
	ModelRenderer outerBody;
	ModelRenderer innerBody;
	ModelRenderer slimeRightEye;
	ModelRenderer slimeLeftEye;
	ModelRenderer slimeMouth;

	public ModelNpcSlime(int par1) {
		textureHeight = 64;
		textureWidth = 64;
		outerBody = new ModelRenderer(this, 0, 0);
		(outerBody = new ModelRenderer(this, 0, 0)).addBox(-8.0f, 32.0f, -8.0f, 16, 16, 16);
		if (par1 > 0) {
			(innerBody = new ModelRenderer(this, 0, 32)).addBox(-3.0f, 17.0f, -3.0f, 6, 6, 6);
			(slimeRightEye = new ModelRenderer(this, 0, 0)).addBox(-3.25f, 18.0f, -3.5f, 2, 2, 2);
			(slimeLeftEye = new ModelRenderer(this, 0, 4)).addBox(1.25f, 18.0f, -3.5f, 2, 2, 2);
			(slimeMouth = new ModelRenderer(this, 0, 8)).addBox(0.0f, 21.0f, -3.5f, 1, 1, 1);
		}
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
		if (innerBody != null) {
			innerBody.render(par7);
		} else {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			outerBody.render(par7);
			GlStateManager.popMatrix();
		}
		if (slimeRightEye != null) {
			slimeRightEye.render(par7);
			slimeLeftEye.render(par7);
			slimeMouth.render(par7);
		}
	}
}
