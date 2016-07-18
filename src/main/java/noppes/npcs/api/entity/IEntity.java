//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.Entity;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IEntity<T extends Entity> {
	void despawn();

	void dropItem(final IItemStack p0);

	void extinguish();

	long getAge();

	int getBlockX();

	int getBlockY();

	int getBlockZ();

	T getMCEntity();

	IEntity getMount();

	IEntity getRider();

	float getRotation();

	IData getStoreddata();

	IData getTempdata();

	int getType();

	String getTypeName();

	String getUUID();

	IWorld getWorld();

	double getX();

	double getY();

	double getZ();

	boolean inFire();

	boolean inLava();

	boolean inWater();

	boolean isAlive();

	boolean isBurning();

	boolean isSneaking();

	boolean isSprinting();

	void knockback(final int p0, final float p1);

	void setBurning(final int p0);

	void setMount(final IEntity p0);

	void setPosition(final double p0, final double p1, final double p2);

	void setRider(final IEntity p0);

	void setRotation(final float p0);

	void setX(final double p0);

	void setY(final double p0);

	void setZ(final double p0);

	boolean typeOf(final int p0);
}
