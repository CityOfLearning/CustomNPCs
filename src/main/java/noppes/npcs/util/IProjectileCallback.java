//

//

package noppes.npcs.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import noppes.npcs.entity.EntityProjectile;

public interface IProjectileCallback {
	boolean onImpact(final EntityProjectile p0, final BlockPos p1, final Entity p2);
}
