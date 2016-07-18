//

//

package noppes.npcs.api.entity.data;

public interface INPCAi {
	int getAnimation();

	boolean getAttackInvisible();

	boolean getAttackLOS();

	boolean getAvoidsWater();

	boolean getCanSwim();

	int getCurrentAnimation();

	int getDoorInteract();

	boolean getInteractWithNPCs();

	boolean getLeapAtTarget();

	boolean getMovingPathPauses();

	int getMovingPathType();

	int getMovingType();

	int getRetaliateType();

	boolean getReturnsHome();

	int getSheltersFrom();

	int getStandingType();

	boolean getStopOnInteract();

	int getTacticalRange();

	int getTacticalType();

	int getWalkingSpeed();

	int getWanderingRange();

	void setAnimation(final int p0);

	void setAttackInvisible(final boolean p0);

	void setAttackLOS(final boolean p0);

	void setAvoidsWater(final boolean p0);

	void setCanSwim(final boolean p0);

	void setDoorInteract(final int p0);

	void setInteractWithNPCs(final boolean p0);

	void setLeapAtTarget(final boolean p0);

	void setMovingPathType(final int p0, final boolean p1);

	void setMovingType(final int p0);

	void setRetaliateType(final int p0);

	void setReturnsHome(final boolean p0);

	void setSheltersFrom(final int p0);

	void setStandingType(final int p0);

	void setStopOnInteract(final boolean p0);

	void setTacticalRange(final int p0);

	void setTacticalType(final int p0);

	void setWalkingSpeed(final int p0);

	void setWanderingRange(final int p0);
}
