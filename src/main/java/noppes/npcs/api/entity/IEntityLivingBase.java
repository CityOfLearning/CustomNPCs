//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.api.IItemStack;

public interface IEntityLivingBase<T extends EntityLivingBase> extends IEntity<T> {
	void addPotionEffect(final int p0, final int p1, final int p2, final boolean p3);

	boolean canSeeEntity(final IEntity p0);

	void clearPotionEffects();

	IItemStack getArmor(final int p0);

	IEntityLivingBase getAttackTarget();

	float getHealth();

	IItemStack getHeldItem();

	IEntityLivingBase getLastAttacked();

	float getMaxHealth();

	@Override
	T getMCEntity();

	int getPotionEffect(final int p0);

	boolean isAttacking();

	boolean isChild();

	void setArmor(final int p0, final IItemStack p1);

	void setAttackTarget(final IEntityLivingBase p0);

	void setHealth(final float p0);

	void setHeldItem(final IItemStack p0);

	void setMaxHealth(final float p0);

	void swingHand();
}
