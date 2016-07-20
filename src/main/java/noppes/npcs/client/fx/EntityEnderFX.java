//

//

package noppes.npcs.client.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.entity.EntityCustomNpc;

public class EntityEnderFX extends EntityPortalFX {
	private static ResourceLocation resource;
	static {
		resource = new ResourceLocation("textures/particle/particles.png");
	}
	private float portalParticleScale;
	private int particleNumber;
	private EntityCustomNpc npc;
	private ResourceLocation location;
	private boolean move;
	private float startX;
	private float startY;

	private float startZ;

	public EntityEnderFX(EntityCustomNpc npc, double par2, double par4, double par6, double par8, double par10,
			double par12, ModelPartData data) {
		super(npc.worldObj, par2, par4, par6, par8, par10, par12);
		move = true;
		startX = 0.0f;
		startY = 0.0f;
		startZ = 0.0f;
		this.npc = npc;
		particleNumber = npc.getRNG().nextInt(2);
		float n = (rand.nextFloat() * 0.2f) + 0.5f;
		particleScale = n;
		portalParticleScale = n;
		particleRed = ((data.color >> 16) & 0xFF) / 255.0f;
		particleGreen = ((data.color >> 8) & 0xFF) / 255.0f;
		particleBlue = (data.color & 0xFF) / 255.0f;
		if (npc.getRNG().nextInt(3) == 1) {
			move = false;
			startX = (float) npc.posX;
			startY = (float) npc.posY;
			startZ = (float) npc.posZ;
		}
		if (data.playerTexture) {
			location = npc.textureLocation;
		} else {
			location = data.getResource();
		}
	}

	@Override
	public int getFXLayer() {
		return 0;
	}

	@Override
	public void renderParticle(WorldRenderer renderer, Entity entity, float par2, float par3, float par4, float par5,
			float par6, float par7) {
		if (move) {
			startX = (float) (npc.prevPosX + ((npc.posX - npc.prevPosX) * par2));
			startY = (float) (npc.prevPosY + ((npc.posY - npc.prevPosY) * par2));
			startZ = (float) (npc.prevPosZ + ((npc.posZ - npc.prevPosZ) * par2));
		}
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.draw();
		float scale = (particleAge + par2) / particleMaxAge;
		scale = 1.0f - scale;
		scale *= scale;
		scale = 1.0f - scale;
		particleScale = portalParticleScale * scale;
		ClientProxy.bindTexture(location);
		float f = 0.875f;
		float f2 = f + 0.125f;
		float f3 = 0.75f - (particleNumber * 0.25f);
		float f4 = f3 + 0.25f;
		float f5 = 0.1f * particleScale;
		float f6 = (float) (((prevPosX + ((posX - prevPosX) * par2)) - EntityFX.interpPosX) + startX);
		float f7 = (float) (((prevPosY + ((posY - prevPosY) * par2)) - EntityFX.interpPosY) + startY);
		float f8 = (float) (((prevPosZ + ((posZ - prevPosZ) * par2)) - EntityFX.interpPosZ) + startZ);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		renderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		renderer.pos(f6 - (par3 * f5) - (par6 * f5), f7 - (par4 * f5), f8 - (par5 * f5) - (par7 * f5)).tex(f2, f4)
				.color(particleRed, particleGreen, particleBlue, 1.0f).endVertex();
		renderer.pos((f6 - (par3 * f5)) + (par6 * f5), f7 + (par4 * f5), (f8 - (par5 * f5)) + (par7 * f5)).tex(f2, f3)
				.color(particleRed, particleGreen, particleBlue, 1.0f).endVertex();
		renderer.pos(f6 + (par3 * f5) + (par6 * f5), f7 + (par4 * f5), f8 + (par5 * f5) + (par7 * f5)).tex(f, f3)
				.color(particleRed, particleGreen, particleBlue, 1.0f).endVertex();
		renderer.pos((f6 + (par3 * f5)) - (par6 * f5), f7 - (par4 * f5), (f8 + (par5 * f5)) - (par7 * f5)).tex(f, f4)
				.color(particleRed, particleGreen, particleBlue, 1.0f).endVertex();
		tessellator.draw();
		ClientProxy.bindTexture(EntityEnderFX.resource);
		renderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}
}
