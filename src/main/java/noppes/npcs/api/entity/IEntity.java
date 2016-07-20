//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.Entity;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IEntity<T extends Entity> {
	void despawn();

	void dropItem(IItemStack p0);

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

	void knockback(int p0, float p1);

	void setBurning(int p0);

	void setMount(IEntity p0);

	void setPosition(double p0, double p1, double p2);

	void setRider(IEntity p0);

	void setRotation(float p0);

	void setX(double p0);

	void setY(double p0);

	void setZ(double p0);

	boolean typeOf(int p0);
}
