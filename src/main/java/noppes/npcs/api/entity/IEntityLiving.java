
package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLiving;

public interface IEntityLiving<T extends EntityLiving> extends IEntityLivingBase<T> {
	void clearNavigation();

	@Override
	T getMCEntity();

	boolean isNavigating();

	void navigateTo(double p0, double p1, double p2, double p3);
}
