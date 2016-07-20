//

//

package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.ModelWrapper;

public class NPCRendererHelper {
	private static ModelWrapper wrapper;

	static {
		wrapper = new ModelWrapper();
	}

	public static void DrawLayers(EntityLivingBase entity, float p_177093_2_, float p_177093_3_, float p_177093_4_,
			float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_,
			RendererLivingEntity renderEntity) {
		renderEntity.renderLayers(entity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_,
				p_177093_8_);
	}

	public static ModelBase getMainModel(RendererLivingEntity render) {
		return render.mainModel;
	}

	public static String getTexture(RendererLivingEntity render, Entity entity) {
		ResourceLocation location = render.getEntityTexture(entity);
		return location.toString();
	}

	public static float handleRotationFloat(EntityLivingBase entity, float par2, RendererLivingEntity renderEntity) {
		return renderEntity.handleRotationFloat(entity, par2);
	}

	public static void preRenderCallback(EntityLivingBase entity, float f, RendererLivingEntity render) {
		render.preRenderCallback(entity, f);
	}

	public static void RenderModel(EntityLivingBase entity, float f, float f2, float f3, float f4, float f5, float f6,
			RendererLivingEntity render, ModelBase main, ResourceLocation resource) {
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
