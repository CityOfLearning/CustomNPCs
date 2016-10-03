package noppes.npcs.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.world.World;
import noppes.npcs.ai.FlyNodeProcessor;

public class PathNavigateFlying extends PathNavigateSwimmer {

   public PathNavigateFlying(EntityLiving p_i45873_1_, World worldIn) {
      super(p_i45873_1_, worldIn);
   }

   protected PathFinder getPathFinder() {
      return new PathFinder(new FlyNodeProcessor());
   }

   protected boolean canNavigate() {
      return true;
   }
}
