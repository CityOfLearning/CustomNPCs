//

//

package noppes.npcs.client.fx;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.MathHelper;

public class EntityElementalStaffFX extends EntityPortalFX {
	double x;
	double y;
	double z;
	EntityLivingBase player;

	public EntityElementalStaffFX(final EntityLivingBase player, final double d, final double d1, final double d2,
			final double f1, final double f2, final double f3, final int color) {
		super(player.worldObj, player.posX + d, player.posY + d1, player.posZ + d2, f1, f2, f3);
		this.player = player;
		x = d;
		y = d1;
		z = d2;
		float[] colors;
		if (color <= 15) {
			colors = EntitySheep.func_175513_a(EnumDyeColor.byDyeDamage(color));
		} else {
			colors = new float[] { ((color >> 16) & 0xFF) / 255.0f, ((color >> 8) & 0xFF) / 255.0f,
					(color & 0xFF) / 255.0f };
		}
		particleRed = colors[0];
		particleGreen = colors[1];
		particleBlue = colors[2];
		particleMaxAge = (int) (16.0 / ((Math.random() * 0.8) + 0.2));
		noClip = false;
	}

	@Override
	public void onUpdate() {
		if (player.isDead) {
			setDead();
			return;
		}
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		final float var2;
		float var1 = var2 = particleAge / particleMaxAge;
		var1 = -var1 + (var1 * var1 * 2.0f);
		var1 = 1.0f - var1;
		final double dx = -MathHelper.sin((float) ((player.rotationYaw / 180.0f) * 3.141592653589793))
				* MathHelper.cos((float) ((player.rotationPitch / 180.0f) * 3.141592653589793));
		final double dz = MathHelper.cos((float) ((player.rotationYaw / 180.0f) * 3.141592653589793))
				* MathHelper.cos((float) ((player.rotationPitch / 180.0f) * 3.141592653589793));
		posX = player.posX + x + dx + (motionX * var1);
		posY = (player.posY + y + (motionY * var1) + (1.0f - var2)) - (player.rotationPitch / 40.0f);
		posZ = player.posZ + z + dz + (motionZ * var1);
		if (particleAge++ >= particleMaxAge) {
			setDead();
		}
	}

	@Override
	public void setDead() {
		super.setDead();
	}
}
