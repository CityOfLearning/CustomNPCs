//

//

package noppes.npcs.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.constants.EnumParts;

public class ModelScaleRenderer extends ModelRenderer {
	public boolean compiled;
	public int displayList;
	public ModelPartConfig config;
	public EnumParts part;

	public ModelScaleRenderer(final ModelBase par1ModelBase, final EnumParts part) {
		super(par1ModelBase);
		this.part = part;
	}

	public ModelScaleRenderer(final ModelBase par1ModelBase, final int par2, final int par3, final EnumParts part) {
		this(par1ModelBase, part);
		setTextureOffset(par2, par3);
	}

	public void compileDisplayList(final float par1) {
		GL11.glNewList(displayList = GLAllocation.generateDisplayLists(1), 4864);
		final WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
		for (int i = 0; i < cubeList.size(); ++i) {
			cubeList.get(i).render(worldrenderer, par1);
		}
		GL11.glEndList();
		compiled = true;
	}

	public void parentRender(final float par1) {
		super.render(par1);
	}

	@Override
	public void postRender(final float par1) {
		if (config != null) {
			GlStateManager.translate(config.transX, config.transY, config.transZ);
		}
		super.postRender(par1);
		if (config != null) {
			GlStateManager.scale(config.scaleX, config.scaleY, config.scaleZ);
		}
	}

	public void postRenderNoScale(final float par1) {
		GlStateManager.translate(config.transX, config.transY, config.transZ);
		super.postRender(par1);
	}

	@Override
	public void render(final float par1) {
		if (!showModel || isHidden) {
			return;
		}
		if (!compiled) {
			compileDisplayList(par1);
		}
		GlStateManager.pushMatrix();
		postRender(par1);
		GlStateManager.callList(displayList);
		if (childModels != null) {
			for (int i = 0; i < childModels.size(); ++i) {
				childModels.get(i).render(par1);
			}
		}
		GlStateManager.popMatrix();
	}

	public void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
