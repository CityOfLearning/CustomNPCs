//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLiving;

public interface IEntityLiving<T extends EntityLiving> extends IEntityLivingBase<T> {
	void clearNavigation();

	@Override
	T getMCEntity();

	boolean isNavigating();

	void navigateTo(final double p0, final double p1, final double p2, final double p3);
}
