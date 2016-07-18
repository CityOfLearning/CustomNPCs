//

//

package noppes.npcs.client.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityRainbowFX extends EntityFX {
	public static float[][] colorTable;
	static {
		EntityRainbowFX.colorTable = new float[][] { { 1.0f, 0.0f, 0.0f }, { 1.0f, 0.5f, 0.0f }, { 1.0f, 1.0f, 0.0f },
				{ 0.0f, 1.0f, 0.0f }, { 0.0f, 0.0f, 1.0f }, { 0.0f, 4375.0f, 0.0f, 1.0f }, { 0.5625f, 0.0f, 1.0f } };
	}

	float reddustParticleScale;

	public EntityRainbowFX(final World world, final double d, final double d1, final double d2, final double f,
			final double f1, final double f2) {
		this(world, d, d1, d2, 1.0f, f, f1, f2);
	}

	public EntityRainbowFX(final World world, final double d, final double d1, final double d2, final float f,
			double f1, final double f2, final double f3) {
		super(world, d, d1, d2, 0.0, 0.0, 0.0);
		motionX *= 0.10000000149011612;
		motionY *= 0.10000000149011612;
		motionZ *= 0.10000000149011612;
		if (f1 == 0.0) {
			f1 = 1.0;
		}
		final int i = world.rand.nextInt(EntityRainbowFX.colorTable.length);
		particleRed = EntityRainbowFX.colorTable[i][0];
		particleGreen = EntityRainbowFX.colorTable[i][1];
		particleBlue = EntityRainbowFX.colorTable[i][2];
		particleScale *= 0.75f;
		particleScale *= f;
		reddustParticleScale = particleScale;
		particleMaxAge = (int) (16.0 / ((Math.random() * 0.8) + 0.2));
		particleMaxAge *= (int) f;
		noClip = false;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setDead();
		}
		setParticleTextureIndex(7 - ((particleAge * 8) / particleMaxAge));
		moveEntity(motionX, motionY, motionZ);
		if (posY == prevPosY) {
			motionX *= 1.1;
			motionZ *= 1.1;
		}
		motionX *= 0.9599999785423279;
		motionY *= 0.9599999785423279;
		motionZ *= 0.9599999785423279;
		if (onGround) {
			motionX *= 0.699999988079071;
			motionZ *= 0.699999988079071;
		}
	}

	@Override
	public void renderParticle(final WorldRenderer tessellator, final Entity entity, final float f, final float f1,
			final float f2, final float f3, final float f4, final float f5) {
		float f6 = ((particleAge + f) / particleMaxAge) * 32.0f;
		if (f6 < 0.0f) {
			f6 = 0.0f;
		} else if (f6 > 1.0f) {
			f6 = 1.0f;
		}
		particleScale = reddustParticleScale * f6;
		super.renderParticle(tessellator, entity, f, f1, f2, f3, f4, f5);
	}
}
