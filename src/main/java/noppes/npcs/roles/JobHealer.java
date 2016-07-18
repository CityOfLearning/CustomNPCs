//

//

package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class JobHealer extends JobInterface {
	private int healTicks;
	public int range;
	public byte type;
	public int speed;
	public HashMap<Integer, Integer> effects;
	private List<EntityLivingBase> affected;

	public JobHealer(final EntityNPCInterface npc) {
		super(npc);
		healTicks = 0;
		range = 8;
		type = 2;
		speed = 20;
		effects = new HashMap<Integer, Integer>();
		affected = new ArrayList<EntityLivingBase>();
	}

	@Override
	public boolean aiContinueExecute() {
		return false;
	}

	@Override
	public boolean aiShouldExecute() {
		++healTicks;
		if (healTicks < speed) {
			return false;
		}
		healTicks = 0;
		affected = npc.worldObj.getEntitiesWithinAABB((Class) EntityLivingBase.class,
				npc.getEntityBoundingBox().expand(range, range / 2.0, range));
		return !affected.isEmpty();
	}

	@Override
	public void aiStartExecuting() {
		for (final EntityLivingBase entity : affected) {
			boolean isEnemy = false;
			if (entity instanceof EntityPlayer) {
				isEnemy = npc.faction.isAggressiveToPlayer((EntityPlayer) entity);
			} else if (entity instanceof EntityNPCInterface) {
				isEnemy = npc.faction.isAggressiveToNpc((EntityNPCInterface) entity);
			} else {
				isEnemy = (entity instanceof EntityMob);
			}
			if (entity != npc) {
				if ((type != 2) && isEnemy && (type != 1) && !isEnemy && (type != 0)) {
					continue;
				}
				for (final Integer potionEffect : effects.keySet()) {
					entity.addPotionEffect(new PotionEffect(potionEffect, 100, effects.get(potionEffect)));
				}
			}
		}
		affected.clear();
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		range = nbttagcompound.getInteger("HealerRange");
		type = nbttagcompound.getByte("HealerType");
		effects = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("BeaconEffects", 10));
		speed = ValueUtil.CorrectInt(nbttagcompound.getInteger("HealerSpeed"), 10, Integer.MAX_VALUE);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("HealerRange", range);
		nbttagcompound.setByte("HealerType", type);
		nbttagcompound.setTag("BeaconEffects", NBTTags.nbtIntegerIntegerMap(effects));
		nbttagcompound.setInteger("HealerSpeed", speed);
		return nbttagcompound;
	}
}
