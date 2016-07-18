//

//

package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.ModelWrapper;

public class NPCRendererHelper {
	private static final ModelWrapper wrapper;

	static {
		wrapper = new ModelWrapper();
	}

	public static void DrawLayers(final EntityLivingBase entity, final float p_177093_2_, final float p_177093_3_,
			final float p_177093_4_, final float p_177093_5_, final float p_177093_6_, final float p_177093_7_,
			final float p_177093_8_, final RendererLivingEntity renderEntity) {
		renderEntity.renderLayers(entity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_,
				p_177093_8_);
	}

	public static ModelBase getMainModel(final RendererLivingEntity render) {
		return render.mainModel;
	}

	public static String getTexture(final RendererLivingEntity render, final Entity entity) {
		final ResourceLocation location = render.getEntityTexture(entity);
		return location.toString();
	}

	public static float handleRotationFloat(final EntityLivingBase entity, final float par2,
			final RendererLivingEntity renderEntity) {
		return renderEntity.handleRotationFloat(entity, par2);
	}

	public static void preRenderCallback(final EntityLivingBase entity, final float f,
			final RendererLivingEntity render) {
		render.preRenderCallback(entity, f);
	}

	public static void RenderModel(final EntityLivingBase entity, final float f, final float f2, final float f3,
			final float f4, final float f5, final float f6, final RendererLivingEntity render, final ModelBase main,
			final ResourceLocation resource) {
		if (!(main instanceof ModelWrapper)) {
			NPCRendererHelper.wrapper.wrapped = main;
			NPCRendererHelper.wrapper.texture = resource;
			render.mainModel = NPCRendererHelper.wrapper;
		}
		try {
			render.renderModel(entity, f, f2, f3, f4, f5, f6);
		} catch (Exception ex) {
		}
		render.mainModel = main;
	}
}
