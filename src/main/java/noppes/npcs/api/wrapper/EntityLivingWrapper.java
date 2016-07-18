//

//

package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.IEntityLivingBase;

public class EntityLivingWrapper<T extends EntityLiving> extends EntityLivingBaseWrapper<T> implements IEntityLiving {
	public EntityLivingWrapper(final T entity) {
		super(entity);
	}

	@Override
	public boolean canSeeEntity(final IEntity entity) {
		return this.entity.getEntitySenses().canSee(entity.getMCEntity());
	}

	@Override
	public void clearNavigation() {
		entity.getNavigator().clearPathEntity();
	}

	@Override
	public IEntityLivingBase getAttackTarget() {
		final IEntityLivingBase base = (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity.getAttackTarget());
		return (base != null) ? base : super.getAttackTarget();
	}

	@Override
	public boolean isAttacking() {
		return super.isAttacking() || (entity.getAttackTarget() != null);
	}

	@Override
	public boolean isNavigating() {
		return !entity.getNavigator().noPath();
	}

	@Override
	public void navigateTo(final double x, final double y, final double z, final double speed) {
		entity.getNavigator().clearPathEntity();
		entity.getNavigator().tryMoveToXYZ(x, y, z, speed * 0.7);
	}

	@Override
	public void setAttackTarget(final IEntityLivingBase living) {
		if (living == null) {
			entity.setAttackTarget((EntityLivingBase) null);
		} else {
			entity.setAttackTarget(living.getMCEntity());
		}
		super.setAttackTarget(living);
	}
}
