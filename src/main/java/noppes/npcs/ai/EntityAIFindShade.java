//

//

package noppes.npcs.ai;

import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;

public class EntityAIFindShade extends EntityAIBase {
	private EntityCreature theCreature;
	private double shelterX;
	private double shelterY;
	private double shelterZ;
	private World theWorld;

	public EntityAIFindShade(EntityCreature par1EntityCreature) {
		theCreature = par1EntityCreature;
		theWorld = par1EntityCreature.worldObj;
		setMutexBits(AiMutex.PASSIVE);
	}

	@Override
	public boolean continueExecuting() {
		return !theCreature.getNavigator().noPath();
	}

	private Vec3 findPossibleShelter() {
		Random random = theCreature.getRNG();
		BlockPos blockpos = new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ);
		for (int i = 0; i < 10; ++i) {
			BlockPos blockpos2 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
			if (!theWorld.canSeeSky(blockpos2) && (theCreature.getBlockPathWeight(blockpos2) < 0.0f)) {
				return new Vec3(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ());
			}
		}
		return null;
	}

	@Override
	public boolean shouldExecute() {
		if (!theWorld.isDaytime()) {
			return false;
		}
		if (!theWorld
				.canSeeSky(new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ))) {
			return false;
		}
		Vec3 var1 = findPossibleShelter();
		if (var1 == null) {
			return false;
		}
		shelterX = var1.xCoord;
		shelterY = var1.yCoord;
		shelterZ = var1.zCoord;
		return true;
	}

	@Override
	public void startExecuting() {
		theCreature.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, 1.0);
	}
}
