//

//

package noppes.npcs.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ai.selector.NPCInteractSelector;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.controllers.lines.Line;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWander extends EntityAIBase {
	private EntityNPCInterface entity;
	public final NPCInteractSelector selector;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private EntityNPCInterface nearbyNPC;

	public EntityAIWander(final EntityNPCInterface npc) {
		entity = npc;
		setMutexBits(AiMutex.PASSIVE);
		selector = new NPCInteractSelector(npc);
	}

	@Override
	public boolean continueExecuting() {
		return ((nearbyNPC == null) || (selector.apply(nearbyNPC) && !entity.isInRange(nearbyNPC, entity.width)))
				&& !entity.getNavigator().noPath() && entity.isEntityAlive() && !entity.isInteracting();
	}

	private EntityNPCInterface getNearbyNPC() {
		final List<Entity> list = entity.worldObj.getEntitiesInAABBexcluding(entity,
				entity.getEntityBoundingBox().expand(entity.ai.walkingRange,
						(entity.ai.walkingRange > 7) ? 7.0 : ((double) entity.ai.walkingRange), entity.ai.walkingRange),
				selector);
		final Iterator<Entity> ita = list.iterator();
		while (ita.hasNext()) {
			final EntityNPCInterface npc = (EntityNPCInterface) ita.next();
			if (!npc.ai.stopAndInteract || npc.isAttacking() || !npc.isEntityAlive()
					|| entity.faction.isAggressiveToNpc(npc)) {
				ita.remove();
			}
		}
		if (list.isEmpty()) {
			return null;
		}
		return (EntityNPCInterface) list.get(entity.getRNG().nextInt(list.size()));
	}

	private Vec3 getVec() {
		if (entity.ai.walkingRange <= 0) {
			return RandomPositionGeneratorAlt.findRandomTarget(entity, CustomNpcs.NpcNavRange, 7);
		}
		final double distance = entity.getDistanceSq(entity.getStartXPos(), entity.getStartYPos(),
				entity.getStartZPos());
		int range = (int) MathHelper.sqrt_double((entity.ai.walkingRange * entity.ai.walkingRange) - distance);
		if (range > CustomNpcs.NpcNavRange) {
			range = CustomNpcs.NpcNavRange;
		}
		if (range < 3) {
			range = entity.ai.walkingRange;
			if (range > CustomNpcs.NpcNavRange) {
				range = CustomNpcs.NpcNavRange;
			}
			final Vec3 start = new Vec3(entity.getStartXPos(), entity.getStartYPos(), entity.getStartZPos());
			return RandomPositionGeneratorAlt.findRandomTargetBlockTowards(entity, range / 2,
					((range / 2) > 7) ? 7 : (range / 2), start);
		}
		return RandomPositionGeneratorAlt.findRandomTarget(entity, range, ((range / 2) > 7) ? 7 : (range / 2));
	}

	@Override
	public void resetTask() {
		if ((nearbyNPC != null) && entity.isInRange(nearbyNPC, 3.5)) {
			final Line line = new Line(".........");
			line.hideText = true;
			if (entity.getRNG().nextBoolean()) {
				entity.saySurrounding(line);
			} else {
				nearbyNPC.saySurrounding(line);
			}
			entity.addInteract(nearbyNPC);
			nearbyNPC.addInteract(entity);
		}
		nearbyNPC = null;
	}

	@Override
	public boolean shouldExecute() {
		if ((entity.getAge() >= 100) || !entity.getNavigator().noPath() || entity.isInteracting()
				|| (entity.getRNG().nextInt(80) != 0)) {
			return false;
		}
		if (entity.ai.npcInteracting && (entity.getRNG().nextInt(4) == 1)) {
			nearbyNPC = getNearbyNPC();
		}
		if (nearbyNPC != null) {
			xPosition = MathHelper.floor_double(nearbyNPC.posX);
			yPosition = MathHelper.floor_double(nearbyNPC.posY);
			zPosition = MathHelper.floor_double(nearbyNPC.posZ);
			nearbyNPC.addInteract(entity);
		} else {
			final Vec3 vec = getVec();
			if (vec == null) {
				return false;
			}
			xPosition = vec.xCoord;
			yPosition = vec.yCoord;
			if (entity.canFly()) {
				yPosition = entity.getStartYPos() + (entity.getRNG().nextFloat() * 0.75 * entity.ai.walkingRange);
			}
			zPosition = vec.zCoord;
		}
		return true;
	}

	@Override
	public void startExecuting() {
		entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, 1.0);
	}

	@Override
	public void updateTask() {
		if (nearbyNPC != null) {
			nearbyNPC.getNavigator().clearPathEntity();
		}
	}
}
