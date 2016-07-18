//

//

package noppes.npcs.client.renderer;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.client.layer.LayerArms;
import noppes.npcs.client.layer.LayerBody;
import noppes.npcs.client.layer.LayerHead;
import noppes.npcs.client.layer.LayerHeadwear;
import noppes.npcs.client.layer.LayerLeftHeldItem;
import noppes.npcs.client.layer.LayerLegs;
import noppes.npcs.client.layer.LayerNpcCloak;
import noppes.npcs.client.layer.LayerPreRender;
import noppes.npcs.client.model.ModelBipedAlt;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class RenderCustomNpc<T extends EntityCustomNpc> extends RenderNPCInterface<T> {
	private float partialTicks;
	private EntityLivingBase entity;
	private RendererLivingEntity renderEntity;
	public ModelBiped npcmodel;

	public RenderCustomNpc(final ModelBiped model) {
		super(model, 0.5f);
		this.npcmodel = (ModelBiped) mainModel;
		this.addLayer((LayerRenderer) new LayerHeadwear(this));
		this.addLayer((LayerRenderer) new LayerHead(this));
		this.addLayer((LayerRenderer) new LayerArms(this));
		this.addLayer((LayerRenderer) new LayerLegs(this));
		this.addLayer((LayerRenderer) new LayerBody(this));
		this.addLayer((LayerRenderer) new LayerHeldItem(this));
		this.addLayer((LayerRenderer) new LayerLeftHeldItem(this));
		this.addLayer((LayerRenderer) new LayerNpcCloak(this));
		this.addLayer((LayerRenderer) new LayerCustomHead(this.npcmodel.bipedHead));
		final LayerBipedArmor armor = new LayerBipedArmor(this);
		this.addLayer((LayerRenderer) armor);
		ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(0.5f), 1);
		ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(1.0f), 2);
	}

	@Override
	public void doRender(final T npc, final double d, final double d1, final double d2, final float f,
			final float partialTicks) {
		this.partialTicks = partialTicks;
		this.entity = npc.modelData.getEntity(npc);
		if (this.entity != null) {
			final Render render = renderManager.getEntityRenderObject((Entity) this.entity);
			if (render instanceof RendererLivingEntity) {
				this.renderEntity = (RendererLivingEntity) render;
			} else {
				this.renderEntity = null;
				this.entity = null;
			}
		} else {
			this.renderEntity = null;
			final List<LayerRenderer<T>> list = layerRenderers;
			for (final LayerRenderer layer : list) {
				if (layer instanceof LayerPreRender) {
					((LayerPreRender) layer).preRender(npc);
				}
			}
		}
		this.npcmodel.heldItemRight = ((npc.getHeldItem() != null) ? 1 : 0);
		this.npcmodel.heldItemLeft = ((npc.getOffHand() != null) ? 1 : 0);
		super.doRender(npc, d, d1, d2, f, partialTicks);
	}

	@Override
	protected float handleRotationFloat(final T par1EntityLivingBase, final float par2) {
		if (this.renderEntity != null) {
			return NPCRendererHelper.handleRotationFloat(this.entity, par2, this.renderEntity);
		}
		return super.handleRotationFloat(par1EntityLivingBase, par2);
	}

	@Override
	protected void preRenderCallback(final T npc, final float f) {
		if (this.renderEntity != null) {
			renderColor(npc);
			final int size = npc.display.getSize();
			if (this.entity instanceof EntityNPCInterface) {
				((EntityNPCInterface) this.entity).display.setSize(5);
			}
			NPCRendererHelper.preRenderCallback(this.entity, f, this.renderEntity);
			npc.display.setSize(size);
			GlStateManager.scale(0.2f * npc.display.getSize(), 0.2f * npc.display.getSize(),
					0.2f * npc.display.getSize());
		} else {
			super.preRenderCallback(npc, f);
		}
	}

	@Override
	protected void renderLayers(final T livingEntity, final float p_177093_2_, final float p_177093_3_,
			final float p_177093_4_, final float p_177093_5_, final float p_177093_6_, final float p_177093_7_,
			final float p_177093_8_) {
		if ((this.entity != null) && (this.renderEntity != null)) {
			NPCRendererHelper.DrawLayers(this.entity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_,
					p_177093_7_, p_177093_8_, this.renderEntity);
		} else {
			super.renderLayers(livingEntity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_,
					p_177093_7_, p_177093_8_);
		}
	}

	@Override
	protected void renderModel(final T npc, final float par2, final float par3, final float par4, final float par5,
			final float par6, final float par7) {
		if (this.renderEntity != null) {
			final boolean flag = !npc.isInvisible();
			final boolean flag2 = !flag && !npc.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
			if (!flag && !flag2) {
				return;
			}
			if (flag2) {
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(770, 771);
				GlStateManager.alphaFunc(516, 0.003921569f);
			}
			ModelBase model = this.renderEntity.getMainModel();
			if (PixelmonHelper.isPixelmon(this.entity)) {
				model = (ModelBase) PixelmonHelper.getModel(this.entity);
			}
			model.setLivingAnimations(this.entity, par2, par3, this.partialTicks);
			model.setRotationAngles(par2, par3, par4, par5, par6, par7, this.entity);
			model.setModelAttributes(mainModel);
			model.isChild = this.entity.isChild();
			NPCRendererHelper.RenderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model,
					getEntityTexture(npc));
			if (!npc.display.getOverlayTexture().isEmpty()) {
				GlStateManager.depthFunc(515);
				if (npc.textureGlowLocation == null) {
					npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
				}
				final float f1 = 1.0f;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(1, 1);
				GlStateManager.disableLighting();
				if (npc.isInvisible()) {
					GlStateManager.depthMask(false);
				} else {
					GlStateManager.depthMask(true);
				}
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GlStateManager.pushMatrix();
				GlStateManager.scale(1.001f, 1.001f, 1.001f);
				NPCRendererHelper.RenderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model,
						npc.textureGlowLocation);
				GlStateManager.popMatrix();
				GlStateManager.enableLighting();
				GlStateManager.color(1.0f, 1.0f, 1.0f, f1);
				GlStateManager.depthFunc(515);
				GlStateManager.disableBlend();
			}
			if (flag2) {
				GlStateManager.disableBlend();
				GlStateManager.alphaFunc(516, 0.1f);
				GlStateManager.popMatrix();
				GlStateManager.depthMask(true);
			}
		} else {
			super.renderModel(npc, par2, par3, par4, par5, par6, par7);
		}
	}
}
