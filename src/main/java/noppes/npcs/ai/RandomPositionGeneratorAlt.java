
package noppes.npcs.ai;

import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RandomPositionGeneratorAlt {
	private static Vec3 staticVector;

	static {
		RandomPositionGeneratorAlt.staticVector = new Vec3(0.0, 0.0, 0.0);
	}

	public static Vec3 findRandomTarget(EntityCreature par0EntityCreature, int par1, int par2) {
		return findRandomTargetBlock(par0EntityCreature, par1, par2, null);
	}

	private static Vec3 findRandomTargetBlock(EntityCreature par0EntityCreature, int par1, int par2, Vec3 par3Vec3) {
		if (par1 <= 0) {
			par1 = 1;
		}
		if (par2 <= 0) {
			par2 = 1;
		}
		Random random = par0EntityCreature.getRNG();
		boolean flag = false;
		int k = 0;
		int l = 0;
		int i1 = 0;
		float f = -99999.0f;
		boolean flag2;
		if (par0EntityCreature.hasHome()) {
			double d0 = par0EntityCreature.getHomePosition().distanceSq(
					MathHelper.floor_double(par0EntityCreature.posX), MathHelper.floor_double(par0EntityCreature.posY),
					MathHelper.floor_double(par0EntityCreature.posZ)) + 4.0;
			double d2 = par0EntityCreature.getMaximumHomeDistance() + par1;
			flag2 = (d0 < (d2 * d2));
		} else {
			flag2 = false;
		}
		for (int l2 = 0; l2 < 10; ++l2) {
			int j1 = random.nextInt(2 * par1) - par1;
			int i2 = random.nextInt(2 * par2) - par2;
			int k2 = random.nextInt(2 * par1) - par1;
			if ((par3Vec3 == null) || (((j1 * par3Vec3.xCoord) + (k2 * par3Vec3.zCoord)) >= 0.0)) {
				if (random.nextBoolean()) {
					j1 += MathHelper.floor_double(par0EntityCreature.posX);
					i2 += MathHelper.floor_double(par0EntityCreature.posY);
					k2 += MathHelper.floor_double(par0EntityCreature.posZ);
				} else {
					j1 += MathHelper.ceiling_double_int(par0EntityCreature.posX);
					i2 += MathHelper.ceiling_double_int(par0EntityCreature.posY);
					k2 += MathHelper.ceiling_double_int(par0EntityCreature.posZ);
				}
				BlockPos pos = new BlockPos(j1, i2, k2);
				if (!flag2 || par0EntityCreature.isWithinHomeDistanceFromPosition(pos)) {
					float f2 = par0EntityCreature.getBlockPathWeight(pos);
					if (f2 > f) {
						f = f2;
						k = j1;
						l = i2;
						i1 = k2;
						flag = true;
					}
				}
			}
		}
		if (flag) {
			return new Vec3(k, l, i1);
		}
		return null;
	}

	public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature entity, int par1, int par2, Vec3 par3Vec3) {
		RandomPositionGeneratorAlt.staticVector = new Vec3(entity.posX, entity.posY, entity.posZ).subtract(par3Vec3);
		return findRandomTargetBlock(entity, par1, par2, RandomPositionGeneratorAlt.staticVector);
	}

	public static Vec3 findRandomTargetBlockTowards(EntityCreature par0EntityCreature, int par1, int par2,
			Vec3 par3Vec3) {
		RandomPositionGeneratorAlt.staticVector = par3Vec3.subtract(par0EntityCreature.posX, par0EntityCreature.posY,
				par0EntityCreature.posZ);
		return findRandomTargetBlock(par0EntityCreature, par1, par2, RandomPositionGeneratorAlt.staticVector);
	}
}
