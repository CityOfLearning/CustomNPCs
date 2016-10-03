package noppes.npcs.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import noppes.npcs.entity.EntityProjectile;

public interface IProjectileCallback {

   boolean onImpact(EntityProjectile var1, BlockPos var2, Entity var3);
}
