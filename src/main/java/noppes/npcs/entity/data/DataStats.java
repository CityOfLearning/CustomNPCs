//

//

package noppes.npcs.entity.data;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.INPCMelee;
import noppes.npcs.api.entity.data.INPCRanged;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.Resistances;
import noppes.npcs.util.ValueUtil;

public class DataStats implements INPCStats {
	public int aggroRange;
	public int maxHealth;
	public int respawnTime;
	public int spawnCycle;
	public boolean hideKilledBody;
	public Resistances resistances;
	public boolean immuneToFire;
	public boolean potionImmune;
	public boolean canDrown;
	public boolean burnInSun;
	public boolean noFallDamage;
	public boolean ignoreCobweb;
	public int healthRegen;
	public int combatRegen;
	public EnumCreatureAttribute creatureType;
	public DataMelee melee;
	public DataRanged ranged;
	private EntityNPCInterface npc;

	public DataStats(EntityNPCInterface npc) {
		aggroRange = 16;
		maxHealth = 20;
		respawnTime = 20;
		spawnCycle = 0;
		hideKilledBody = false;
		resistances = new Resistances();
		immuneToFire = false;
		potionImmune = false;
		canDrown = true;
		burnInSun = false;
		noFallDamage = false;
		ignoreCobweb = false;
		healthRegen = 1;
		combatRegen = 0;
		creatureType = EnumCreatureAttribute.UNDEFINED;
		this.npc = npc;
		melee = new DataMelee(npc);
		ranged = new DataRanged(npc);
	}

	@Override
	public int getAggroRange() {
		return aggroRange;
	}

	@Override
	public int getCombatRegen() {
		return combatRegen;
	}

	@Override
	public int getCreatureType() {
		return creatureType.ordinal();
	}

	@Override
	public int getHealthRegen() {
		return healthRegen;
	}

	@Override
	public boolean getHideDeadBody() {
		return hideKilledBody;
	}

	@Override
	public boolean getImmune(int type) {
		if (type == 0) {
			return potionImmune;
		}
		if (type == 1) {
			return !noFallDamage;
		}
		if (type == 2) {
			return burnInSun;
		}
		if (type == 3) {
			return immuneToFire;
		}
		if (type == 4) {
			return !canDrown;
		}
		if (type == 5) {
			return ignoreCobweb;
		}
		throw new CustomNPCsException("Unknown immune type: " + type, new Object[0]);
	}

	@Override
	public int getMaxHealth() {
		return maxHealth;
	}

	@Override
	public INPCMelee getMelee() {
		return melee;
	}

	@Override
	public INPCRanged getRanged() {
		return ranged;
	}

	@Override
	public float getResistance(int type) {
		if (type == 0) {
			return resistances.melee;
		}
		if (type == 1) {
			return resistances.arrow;
		}
		if (type == 2) {
			return resistances.explosion;
		}
		if (type == 3) {
			return resistances.knockback;
		}
		return 1.0f;
	}

	@Override
	public int getRespawnTime() {
		return respawnTime;
	}

	@Override
	public int getRespawnType() {
		return spawnCycle;
	}

	public void readToNBT(NBTTagCompound compound) {
		resistances.readToNBT(compound.getCompoundTag("Resistances"));
		setMaxHealth(compound.getInteger("MaxHealth"));
		hideKilledBody = compound.getBoolean("HideBodyWhenKilled");
		aggroRange = compound.getInteger("AggroRange");
		respawnTime = compound.getInteger("RespawnTime");
		spawnCycle = compound.getInteger("SpawnCycle");
		creatureType = EnumCreatureAttribute.values()[compound.getInteger("CreatureType")];
		healthRegen = compound.getInteger("HealthRegen");
		combatRegen = compound.getInteger("CombatRegen");
		immuneToFire = compound.getBoolean("ImmuneToFire");
		potionImmune = compound.getBoolean("PotionImmune");
		canDrown = compound.getBoolean("CanDrown");
		burnInSun = compound.getBoolean("BurnInSun");
		noFallDamage = compound.getBoolean("NoFallDamage");
		npc.setImmuneToFire(immuneToFire);
		ignoreCobweb = compound.getBoolean("IgnoreCobweb");
		melee.readFromNBT(compound);
		ranged.readFromNBT(compound);
	}

	@Override
	public void setAggroRange(int range) {
		aggroRange = range;
	}

	@Override
	public void setCombatRegen(int regen) {
		combatRegen = regen;
	}

	@Override
	public void setCreatureType(int type) {
		creatureType = EnumCreatureAttribute.values()[type];
	}

	@Override
	public void setHealthRegen(int regen) {
		healthRegen = regen;
	}

	@Override
	public void setHideDeadBody(boolean hide) {
		hideKilledBody = hide;
		npc.updateClient = true;
	}

	@Override
	public void setImmune(int type, boolean bo) {
		if (type == 0) {
			potionImmune = bo;
		} else if (type == 1) {
			noFallDamage = !bo;
		} else if (type == 2) {
			burnInSun = bo;
		} else if (type == 3) {
			npc.setImmuneToFire(bo);
		} else if (type == 4) {
			canDrown = !bo;
		} else {
			if (type != 5) {
				throw new CustomNPCsException("Unknown immune type: " + type, new Object[0]);
			}
			ignoreCobweb = bo;
		}
	}

	@Override
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		npc.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
		npc.updateClient = true;
	}

	@Override
	public void setResistance(int type, float value) {
		value = ValueUtil.correctFloat(value, 0.0f, 2.0f);
		if (type == 0) {
			resistances.melee = value;
		} else if (type == 1) {
			resistances.arrow = value;
		} else if (type == 2) {
			resistances.explosion = value;
		} else if (type == 3) {
			resistances.knockback = value;
		}
	}

	@Override
	public void setRespawnTime(int seconds) {
		respawnTime = seconds;
	}

	@Override
	public void setRespawnType(int type) {
		spawnCycle = type;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Resistances", resistances.writeToNBT());
		compound.setInteger("MaxHealth", maxHealth);
		compound.setInteger("AggroRange", aggroRange);
		compound.setBoolean("HideBodyWhenKilled", hideKilledBody);
		compound.setInteger("RespawnTime", respawnTime);
		compound.setInteger("SpawnCycle", spawnCycle);
		compound.setInteger("CreatureType", creatureType.ordinal());
		compound.setInteger("HealthRegen", healthRegen);
		compound.setInteger("CombatRegen", combatRegen);
		compound.setBoolean("ImmuneToFire", immuneToFire);
		compound.setBoolean("PotionImmune", potionImmune);
		compound.setBoolean("CanDrown", canDrown);
		compound.setBoolean("BurnInSun", burnInSun);
		compound.setBoolean("NoFallDamage", noFallDamage);
		compound.setBoolean("IgnoreCobweb", ignoreCobweb);
		melee.writeToNBT(compound);
		ranged.writeToNBT(compound);
		return compound;
	}
}
