
package noppes.npcs.client.renderer;

import java.io.File;
import java.security.MessageDigest;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ImageDownloadAlt;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class RenderNPCInterface<T extends EntityNPCInterface> extends RenderLiving<T> {
	public static int LastTextureTick;

	public RenderNPCInterface(ModelBase model, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), model, f);
	}

	@Override
	public void doRender(T npc, double d, double d1, double d2, float f, float f1) {
		if (npc.isKilled() && npc.stats.hideKilledBody && (npc.deathTime > 20)) {
			return;
		}
		if (((npc.display.getBossbar() == 1) || ((npc.display.getBossbar() == 2) && npc.isAttacking()))
				&& !npc.isKilled() && (npc.deathTime <= 20) && npc.canSee(Minecraft.getMinecraft().thePlayer)) {
			BossStatus.setBossStatus(npc, true);
		}
		if ((npc.ai.getStandingType() == 3) && !npc.isWalking() && !npc.isInteracting()) {
			float n = npc.ai.orientation;
			npc.renderYawOffset = n;
			npc.prevRenderYawOffset = n;
		}
		super.doRender(npc, d, d1, d2, f, f1);
	}

	@Override
	public void doRenderShadowAndFire(Entity par1Entity, double par2, double par4, double par6, float par8,
			float par9) {
		EntityNPCInterface npc = (EntityNPCInterface) par1Entity;
		shadowSize = npc.width;
		if (!npc.isKilled()) {
			super.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
		}
	}

	@Override
	public ResourceLocation getEntityTexture(T npc) {
		if (npc.textureLocation == null) {
			if (npc.display.skinType == 0) {
				npc.textureLocation = new ResourceLocation(npc.display.getSkinTexture());
			} else {
				if (RenderNPCInterface.LastTextureTick < 5) {
					return DefaultPlayerSkin.getDefaultSkinLegacy();
				}
				if ((npc.display.skinType == 1) && (npc.display.playerProfile != null)) {
					Minecraft minecraft = Minecraft.getMinecraft();
					Map map = minecraft.getSkinManager().loadSkinFromCache(npc.display.playerProfile);
					if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
						npc.textureLocation = minecraft.getSkinManager().loadSkin(
								(MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN),
								MinecraftProfileTexture.Type.SKIN);
					}
				} else if (npc.display.skinType == 2) {
					try {
						MessageDigest digest = MessageDigest.getInstance("MD5");
						byte[] hash = digest.digest(npc.display.getSkinUrl().getBytes("UTF-8"));
						StringBuilder sb = new StringBuilder(2 * hash.length);
						for (byte b : hash) {
							sb.append(String.format("%02x", b & 0xFF));
						}
						this.loadSkin(null, npc.textureLocation = new ResourceLocation("skins/" + sb.toString()),
								npc.display.getSkinUrl());
					} catch (Exception ex) {
					}
				}
			}
		}
		if (npc.textureLocation == null) {
			return DefaultPlayerSkin.getDefaultSkinLegacy();
		}
		return npc.textureLocation;
	}

	@Override
	protected float handleRotationFloat(T npc, float par2) {
		if (npc.isKilled() || !npc.display.getHasLivingAnimation()) {
			return 0.0f;
		}
		return super.handleRotationFloat(npc, par2);
	}

	private void loadSkin(File file, ResourceLocation resource, String par1Str) {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject object = new ImageDownloadAlt(file, par1Str, DefaultPlayerSkin.getDefaultSkinLegacy(),
				new ImageBufferDownloadAlt());
		texturemanager.loadTexture(resource, object);
	}

	@Override
	protected void preRenderCallback(T npc, float f) {
		this.renderColor(npc);
		int size = npc.display.getSize();
		GlStateManager.scale((npc.scaleX / 5.0f) * size, (npc.scaleY / 5.0f) * size, (npc.scaleZ / 5.0f) * size);
	}

	protected void renderColor(EntityNPCInterface npc) {
		if ((npc.hurtTime <= 0) && (npc.deathTime <= 0)) {
			float red = ((npc.display.getTint() >> 16) & 0xFF) / 255.0f;
			float green = ((npc.display.getTint() >> 8) & 0xFF) / 255.0f;
			float blue = (npc.display.getTint() & 0xFF) / 255.0f;
			GlStateManager.color(red, green, blue, 1.0f);
		}
	}

	private void renderLiving(T npc, double d, double d1, double d2, float xoffset, float yoffset, float zoffset) {
		xoffset = (xoffset / 5.0f) * npc.display.getSize();
		yoffset = (yoffset / 5.0f) * npc.display.getSize();
		zoffset = (zoffset / 5.0f) * npc.display.getSize();
		super.renderLivingAt(npc, d + xoffset, d1 + yoffset, d2 + zoffset);
	}

	@Override
	protected void renderLivingAt(T npc, double d, double d1, double d2) {
		shadowSize = npc.display.getSize() / 10.0f;
		float xOffset = 0.0f;
		float yOffset = (npc.currentAnimation == 0) ? ((npc.ai.bodyOffsetY / 10.0f) - 0.5f) : 0.0f;
		float zOffset = 0.0f;
		if (npc.isEntityAlive()) {
			if (npc.isPlayerSleeping()) {
				xOffset = (float) (-Math.cos(Math.toRadians(180 - npc.ai.orientation)));
				zOffset = (float) (-Math.sin(Math.toRadians(npc.ai.orientation)));
				yOffset += 0.14f;
			} else if (npc.isRiding()) {
				yOffset -= 0.5f - (((EntityCustomNpc) npc).modelData.getLegsY() * 0.8f);
			}
		}
		this.renderLiving(npc, d, d1, d2, xOffset, yOffset, zOffset);
	}

	protected void renderLivingLabel(EntityNPCInterface npc, double d, double d1, double d2, int i, Object... obs) {
		FontRenderer fontrenderer = getFontRendererFromRenderManager();
		i = npc.getBrightnessForRender(0.0f);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
		float f1 = (npc.baseHeight / 5.0f) * npc.display.getSize();
		float f2 = 0.01666667f * f1;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) d + 0.0f, (float) d1, (float) d2);
		GL11.glNormal3f(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		float height = f1 / 6.5f;
		for (j = 0; j < obs.length; j += 2) {
			float scale = (Float) obs[j + 1];
			height += (f1 / 6.5f) * scale;
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			String s = obs[j].toString();
			GlStateManager.translate(0.0f, height, 0.0f);
			GlStateManager.scale(-f2 * scale, -f2 * scale, f2 * scale);
			tessellator.begin(7, DefaultVertexFormats.POSITION_COLOR);
			int size = fontrenderer.getStringWidth(s) / 2;
			tessellator.pos(-size - 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
			tessellator.pos(-size - 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
			tessellator.pos(size + 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
			tessellator.pos(size + 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.enableTexture2D();
			int color = npc.faction.color;
			if (npc.isInRange(renderManager.livingPlayer, 4.0)) {
				GlStateManager.disableDepth();
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, color + 1426063360);
			}
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, color);
			GlStateManager.popMatrix();
		}
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	@Override
	protected void renderModel(T npc, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.renderModel(npc, par2, par3, par4, par5, par6, par7);
		if (!npc.display.getOverlayTexture().isEmpty()) {
			GlStateManager.depthFunc(515);
			if (npc.textureGlowLocation == null) {
				npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
			}
			bindTexture(npc.textureGlowLocation);
			float f1 = 1.0f;
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
			mainModel.render(npc, par2, par3, par4, par5, par6, par7);
			GlStateManager.popMatrix();
			GlStateManager.enableLighting();
			GlStateManager.color(1.0f, 1.0f, 1.0f, f1);
			GlStateManager.depthFunc(515);
			GlStateManager.disableBlend();
		}
	}

	@Override
	public void renderName(T npc, double d, double d1, double d2) {
		if ((npc == null) || !canRenderName(npc)) {
			return;
		}
		if (!npc.isInRange(renderManager.livingPlayer, npc.isSneaking() ? 32.0 : 64.0)) {
			return;
		}
		if (npc.messages != null) {
			float height = (npc.baseHeight / 5.0f) * npc.display.getSize();
			float offset = npc.height
					* (1.2f + (npc.display.showName() ? (npc.display.getTitle().isEmpty() ? 0.15f : 0.25f) : 0.0f));
			npc.messages.renderMessages(d, d1 + offset, d2, 0.666667f * height,
					npc.isInRange(renderManager.livingPlayer, 4.0));
		}
		float scale = (npc.baseHeight / 5.0f) * npc.display.getSize();
		if (npc.display.showName()) {
			String s = npc.getName();
			if (!npc.display.getTitle().isEmpty()) {
				this.renderLivingLabel(npc, d, (d1 + npc.height) - (0.06f * scale), d2, 64,
						"<" + npc.display.getTitle() + ">", 0.6f, s, 1.0f);
			} else {
				this.renderLivingLabel(npc, d, (d1 + npc.height) - (0.06f * scale), d2, 64, s, 1.0f);
			}
		}
	}

	@Override
	protected void rotateCorpse(T npc, float f, float f1, float f2) {
		if (npc.isEntityAlive() && npc.isPlayerSleeping()) {
			GlStateManager.rotate(npc.ai.orientation, 0.0f, 1.0f, 0.0f);
			GlStateManager.rotate(getDeathMaxRotation(npc), 0.0f, 0.0f, 1.0f);
			GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
		} else if (npc.isEntityAlive() && (npc.currentAnimation == 7)) {
			GlStateManager.rotate(270.0f - f1, 0.0f, 1.0f, 0.0f);
			float scale = ((EntityCustomNpc) npc).display.getSize() / 5.0f;
			GlStateManager.translate(-scale + (((EntityCustomNpc) npc).modelData.getLegsY() * scale), 0.14f, 0.0f);
			GlStateManager.rotate(270.0f, 0.0f, 0.0f, 1.0f);
			GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
		} else {
			super.rotateCorpse(npc, f, f1, f2);
		}
	}
}
