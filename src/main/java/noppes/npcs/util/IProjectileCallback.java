//

//

package noppes.npcs.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import noppes.npcs.entity.EntityProjectile;

public interface IProjectileCallback {
	boolean onImpact(EntityProjectile p0, BlockPos p1, Entity p2);
}
