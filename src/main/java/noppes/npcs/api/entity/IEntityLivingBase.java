
package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.api.IItemStack;

public interface IEntityLivingBase<T extends EntityLivingBase> extends IEntity<T> {
	void addPotionEffect(int p0, int p1, int p2, boolean p3);

	boolean canSeeEntity(IEntity p0);

	void clearPotionEffects();

	IItemStack getArmor(int p0);

	IEntityLivingBase getAttackTarget();

	float getHealth();

	IItemStack getHeldItem();

	IEntityLivingBase getLastAttacked();

	float getMaxHealth();

	@Override
	T getMCEntity();

	int getPotionEffect(int p0);

	boolean isAttacking();

	boolean isChild();

	void setArmor(int p0, IItemStack p1);

	void setAttackTarget(IEntityLivingBase p0);

	void setHealth(float p0);

	void setHeldItem(IItemStack p0);

	void setMaxHealth(float p0);

	void swingHand();
}
