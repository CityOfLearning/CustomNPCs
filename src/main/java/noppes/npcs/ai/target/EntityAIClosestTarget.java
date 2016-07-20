//

//

package noppes.npcs.ai.target;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.MathHelper;

public class EntityAIClosestTarget extends EntityAITarget {
	private Class targetClass;
	private int targetChance;
	private EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	private Predicate targetEntitySelector;
	private EntityLivingBase targetEntity;

	public EntityAIClosestTarget(EntityCreature par1EntityCreature, Class par2Class, int par3, boolean par4) {
		this(par1EntityCreature, par2Class, par3, par4, false);
	}

	public EntityAIClosestTarget(EntityCreature par1EntityCreature, Class par2Class, int par3, boolean par4,
			boolean par5) {
		this(par1EntityCreature, par2Class, par3, par4, par5, null);
	}

	public EntityAIClosestTarget(EntityCreature par1EntityCreature, Class par2Class, int par3, boolean par4,
			boolean par5, Predicate par6IEntitySelector) {
		super(par1EntityCreature, par4, par5);
		targetClass = par2Class;
		targetChance = par3;
		theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(par1EntityCreature);
		setMutexBits(1);
		targetEntitySelector = par6IEntitySelector;
	}

	@Override
	public boolean shouldExecute() {
		if ((targetChance > 0) && (taskOwner.getRNG().nextInt(targetChance) != 0)) {
			return false;
		}
		double d0 = getTargetDistance();
		List list = taskOwner.worldObj.getEntitiesWithinAABB(targetClass,
				taskOwner.getEntityBoundingBox().expand(d0, MathHelper.ceiling_double_int(d0 / 2.0), d0),
				targetEntitySelector);
		Collections.sort(list, theNearestAttackableTargetSorter);
		if (list.isEmpty()) {
			return false;
		}
		targetEntity = (EntityLivingBase) list.get(0);
		return true;
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(targetEntity);
		if ((targetEntity instanceof EntityMob) && (((EntityMob) targetEntity).getAttackTarget() == null)) {
			((EntityMob) targetEntity).setAttackTarget(taskOwner);
		}
		super.startExecuting();
	}
}
