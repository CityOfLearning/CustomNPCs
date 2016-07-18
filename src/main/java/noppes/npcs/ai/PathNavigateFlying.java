//

//

package noppes.npcs.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.world.World;

public class PathNavigateFlying extends PathNavigateSwimmer {
	public PathNavigateFlying(final EntityLiving p_i45873_1_, final World worldIn) {
		super(p_i45873_1_, worldIn);
	}

	@Override
	protected boolean canNavigate() {
		return true;
	}

	@Override
	protected PathFinder getPathFinder() {
		return new PathFinder(new FlyNodeProcessor());
	}
}
