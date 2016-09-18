
package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.entity.EntityNPCInterface;

public class DataAI implements INPCAi {
	private EntityNPCInterface npc;
	public int onAttack;
	public int doorInteract;
	public int findShelter;
	public boolean canSwim;
	public boolean reactsToFire;
	public boolean avoidsWater;
	public boolean avoidsSun;
	public boolean returnToStart;
	public boolean directLOS;
	public boolean canLeap;
	public boolean canSprint;
	public boolean stopAndInteract;
	public boolean attackInvisible;
	public int tacticalVariant;
	private int tacticalRadius;
	public int movementType;
	public int targetType;
	public int specialAggroRange;
	public int animationType;
	private int standingType;
	private int movingType;
	public boolean npcInteracting;
	public int orientation;
	public float bodyOffsetX;
	public float bodyOffsetY;
	public float bodyOffsetZ;
	public int walkingRange;
	private int moveSpeed;
	private List<int[]> movingPath;
	private BlockPos startPos;
	public int movingPos;
	public int movingPattern;
	public boolean movingPause;

	public DataAI(EntityNPCInterface npc) {
		onAttack = 0;
		doorInteract = 2;
		findShelter = 2;
		canSwim = true;
		reactsToFire = false;
		avoidsWater = false;
		avoidsSun = false;
		returnToStart = true;
		directLOS = true;
		canLeap = false;
		canSprint = false;
		stopAndInteract = true;
		attackInvisible = false;
		tacticalVariant = 0;
		tacticalRadius = 8;
		movementType = 0;
		targetType = 0;
		specialAggroRange = 14;
		animationType = 0;
		standingType = 0;
		movingType = 0;
		npcInteracting = true;
		orientation = 0;
		bodyOffsetX = 5.0f;
		bodyOffsetY = 5.0f;
		bodyOffsetZ = 5.0f;
		walkingRange = 10;
		moveSpeed = 5;
		movingPath = new ArrayList<int[]>();
		startPos = null;
		movingPos = 0;
		movingPattern = 0;
		movingPause = true;
		this.npc = npc;
	}

	public void decreaseMovingPath() {
		List<int[]> list = getMovingPath();
		if (list.size() == 1) {
			movingPos = 0;
		} else if (movingPattern == 0) {
			--movingPos;
			if (movingPos < 0) {
				movingPos += list.size();
			}
		} else if (movingPattern == 1) {
			--movingPos;
			if (movingPos < 0) {
				int size = (list.size() * 2) - 2;
				movingPos += size;
			}
		}
	}

	@Override
	public int getAnimation() {
		return animationType;
	}

	@Override
	public boolean getAttackInvisible() {
		return attackInvisible;
	}

	@Override
	public boolean getAttackLOS() {
		return directLOS;
	}

	@Override
	public boolean getAvoidsWater() {
		return avoidsWater;
	}

	@Override
	public boolean getCanSwim() {
		return canSwim;
	}

	@Override
	public int getCurrentAnimation() {
		return npc.currentAnimation;
	}

	public int[] getCurrentMovingPath() {
		List<int[]> list = getMovingPath();
		if (list.size() == 1) {
			movingPos = 0;
		} else if (movingPos >= list.size()) {
			if (movingPattern == 0) {
				movingPos = 0;
			} else {
				int size = (list.size() * 2) - 2;
				if (movingPos >= size) {
					movingPos = 0;
				} else if (movingPos >= list.size()) {
					return list.get(list.size() - (movingPos % list.size()) - 2);
				}
			}
		}
		return list.get(movingPos);
	}

	public double getDistanceSqToPathPoint() {
		int[] pos = getCurrentMovingPath();
		return npc.getDistanceSq(pos[0] + 0.5, pos[1], pos[2] + 0.5);
	}

	@Override
	public int getDoorInteract() {
		return doorInteract;
	}

	@Override
	public boolean getInteractWithNPCs() {
		return npcInteracting;
	}

	@Override
	public boolean getLeapAtTarget() {
		return canLeap;
	}

	public List<int[]> getMovingPath() {
		if (movingPath.isEmpty() && (startPos != null)) {
			movingPath.add(getStartArray());
		}
		return movingPath;
	}

	@Override
	public boolean getMovingPathPauses() {
		return movingPause;
	}

	@Override
	public int getMovingPathType() {
		return movingPattern;
	}

	@Override
	public int getMovingType() {
		return movingType;
	}

	@Override
	public int getRetaliateType() {
		return onAttack;
	}

	@Override
	public boolean getReturnsHome() {
		return returnToStart;
	}

	@Override
	public int getSheltersFrom() {
		return findShelter;
	}

	@Override
	public int getStandingType() {
		return standingType;
	}

	private int[] getStartArray() {
		BlockPos pos = startPos();
		return new int[] { pos.getX(), pos.getY(), pos.getZ() };
	}

	@Override
	public boolean getStopOnInteract() {
		return stopAndInteract;
	}

	@Override
	public int getTacticalRange() {
		return tacticalRadius;
	}

	@Override
	public int getTacticalType() {
		return tacticalVariant;
	}

	@Override
	public int getWalkingSpeed() {
		return moveSpeed;
	}

	@Override
	public int getWanderingRange() {
		return walkingRange;
	}

	public void incrementMovingPath() {
		List<int[]> list = getMovingPath();
		if (list.size() == 1) {
			movingPos = 0;
		} else if (movingPattern == 0) {
			++movingPos;
			movingPos %= list.size();
		} else if (movingPattern == 1) {
			++movingPos;
			int size = (list.size() * 2) - 2;
			movingPos %= size;
		}
	}

	public void readToNBT(NBTTagCompound compound) {
		canSwim = compound.getBoolean("CanSwim");
		reactsToFire = compound.getBoolean("ReactsToFire");
		setAvoidsWater(compound.getBoolean("AvoidsWater"));
		avoidsSun = compound.getBoolean("AvoidsSun");
		returnToStart = compound.getBoolean("ReturnToStart");
		onAttack = compound.getInteger("OnAttack");
		doorInteract = compound.getInteger("DoorInteract");
		findShelter = compound.getInteger("FindShelter");
		directLOS = compound.getBoolean("DirectLOS");
		canLeap = compound.getBoolean("CanLeap");
		canSprint = compound.getBoolean("CanSprint");
		tacticalRadius = compound.getInteger("TacticalRadius");
		movingPause = compound.getBoolean("MovingPause");
		npcInteracting = compound.getBoolean("npcInteracting");
		stopAndInteract = compound.getBoolean("stopAndInteract");
		targetType = compound.getInteger("TargetType");
		specialAggroRange = compound.getInteger("SpecialAggroRange");
		movementType = compound.getInteger("MovementType");
		animationType = compound.getInteger("MoveState");
		standingType = compound.getInteger("StandingState");
		movingType = compound.getInteger("MovingState");
		tacticalVariant = compound.getInteger("TacticalVariant");
		orientation = compound.getInteger("Orientation");
		bodyOffsetY = compound.getFloat("PositionOffsetY");
		bodyOffsetZ = compound.getFloat("PositionOffsetZ");
		bodyOffsetX = compound.getFloat("PositionOffsetX");
		walkingRange = compound.getInteger("WalkingRange");
		setWalkingSpeed(compound.getInteger("MoveSpeed"));
		setMovingPath(NBTTags.getIntegerArraySet(compound.getTagList("MovingPathNew", 10)));
		movingPos = compound.getInteger("MovingPos");
		movingPattern = compound.getInteger("MovingPatern");
		attackInvisible = compound.getBoolean("AttackInvisible");
		if (compound.hasKey("StartPosNew")) {
			int[] startPos = compound.getIntArray("StartPosNew");
			this.startPos = new BlockPos(startPos[0], startPos[1], startPos[2]);
		}
	}

	@Override
	public void setAnimation(int type) {
		animationType = type;
	}

	@Override
	public void setAttackInvisible(boolean attack) {
		attackInvisible = attack;
	}

	@Override
	public void setAttackLOS(boolean enabled) {
		directLOS = enabled;
		npc.updateAI = true;
	}

	@Override
	public void setAvoidsWater(boolean enabled) {
		if (npc.getNavigator() instanceof PathNavigateGround) {
			((PathNavigateGround) npc.getNavigator()).setAvoidsWater(enabled);
		}
		avoidsWater = enabled;
	}

	@Override
	public void setCanSwim(boolean canSwim) {
		this.canSwim = canSwim;
	}

	@Override
	public void setDoorInteract(int type) {
		doorInteract = type;
		npc.updateAI = true;
	}

	@Override
	public void setInteractWithNPCs(boolean interact) {
		npcInteracting = interact;
	}

	@Override
	public void setLeapAtTarget(boolean leap) {
		canLeap = leap;
		npc.updateAI = true;
	}

	public void setMovingPath(List<int[]> list) {
		movingPath = list;
		if (!movingPath.isEmpty()) {
			int[] startPos = movingPath.get(0);
			this.startPos = new BlockPos(startPos[0], startPos[1], startPos[2]);
		}
	}

	@Override
	public void setMovingPathType(int type, boolean pauses) {
		if ((type < 0) && (type > 1)) {
			throw new CustomNPCsException("Moving path type: " + type, new Object[0]);
		}
		movingPattern = type;
		movingPause = pauses;
	}

	@Override
	public void setMovingType(int type) {
		if ((type < 0) || (type > 2)) {
			throw new CustomNPCsException("Unknown moving type: " + type, new Object[0]);
		}
		movingType = type;
		npc.updateAI = true;
	}

	@Override
	public void setRetaliateType(int type) {
		if ((type < 0) || (type > 3)) {
			throw new CustomNPCsException("Unknown retaliation type: " + type, new Object[0]);
		}
		onAttack = type;
		npc.updateAI = true;
	}

	@Override
	public void setReturnsHome(boolean bo) {
		returnToStart = bo;
	}

	@Override
	public void setSheltersFrom(int type) {
		findShelter = type;
		npc.updateAI = true;
	}

	@Override
	public void setStandingType(int type) {
		if ((type < 0) || (type > 3)) {
			throw new CustomNPCsException("Unknown standing type: " + type, new Object[0]);
		}
		standingType = type;
		npc.updateAI = true;
	}

	public void setStartPos(BlockPos pos) {
		startPos = pos;
	}

	@Override
	public void setStopOnInteract(boolean stopOnInteract) {
		stopAndInteract = stopOnInteract;
	}

	@Override
	public void setTacticalRange(int range) {
		tacticalRadius = range;
	}

	@Override
	public void setTacticalType(int type) {
		tacticalVariant = type;
		npc.updateAI = true;
	}

	@Override
	public void setWalkingSpeed(int speed) {
		if ((speed < 0) || (speed > 10)) {
			throw new CustomNPCsException("Wrong speed: " + speed, new Object[0]);
		}
		moveSpeed = speed;
		npc.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(npc.getSpeed());
	}

	@Override
	public void setWanderingRange(int range) {
		if ((range < 1) || (range > 50)) {
			throw new CustomNPCsException("Bad wandering range: " + range, new Object[0]);
		}
		walkingRange = range;
	}

	public BlockPos startPos() {
		if (startPos == null) {
			startPos = new BlockPos(npc);
		}
		return startPos;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("CanSwim", canSwim);
		compound.setBoolean("ReactsToFire", reactsToFire);
		compound.setBoolean("AvoidsWater", avoidsWater);
		compound.setBoolean("AvoidsSun", avoidsSun);
		compound.setBoolean("ReturnToStart", returnToStart);
		compound.setInteger("OnAttack", onAttack);
		compound.setInteger("DoorInteract", doorInteract);
		compound.setInteger("FindShelter", findShelter);
		compound.setBoolean("DirectLOS", directLOS);
		compound.setBoolean("CanLeap", canLeap);
		compound.setBoolean("CanSprint", canSprint);
		compound.setInteger("TacticalRadius", tacticalRadius);
		compound.setBoolean("MovingPause", movingPause);
		compound.setBoolean("npcInteracting", npcInteracting);
		compound.setBoolean("stopAndInteract", stopAndInteract);
		compound.setInteger("MoveState", animationType);
		compound.setInteger("StandingState", standingType);
		compound.setInteger("MovingState", movingType);
		compound.setInteger("TacticalVariant", tacticalVariant);
		compound.setInteger("TargetType", targetType);
		compound.setInteger("SpecialAggroRange", specialAggroRange);
		compound.setInteger("MovementType", movementType);
		compound.setInteger("Orientation", orientation);
		compound.setFloat("PositionOffsetX", bodyOffsetX);
		compound.setFloat("PositionOffsetY", bodyOffsetY);
		compound.setFloat("PositionOffsetZ", bodyOffsetZ);
		compound.setInteger("WalkingRange", walkingRange);
		compound.setInteger("MoveSpeed", moveSpeed);
		compound.setTag("MovingPathNew", NBTTags.nbtIntegerArraySet(movingPath));
		compound.setInteger("MovingPos", movingPos);
		compound.setInteger("MovingPatern", movingPattern);
		setAvoidsWater(avoidsWater);
		compound.setIntArray("StartPosNew", getStartArray());
		compound.setBoolean("AttackInvisible", attackInvisible);
		return compound;
	}
}
