//

//

package noppes.npcs.entity.data;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.data.INPCMelee;
import noppes.npcs.entity.EntityNPCInterface;

public class DataMelee implements INPCMelee {
	private EntityNPCInterface npc;
	private int attackStrength;
	private int attackSpeed;
	private int attackRange;
	private int knockback;
	private int potionType;
	private int potionDuration;
	private int potionAmp;

	public DataMelee(final EntityNPCInterface npc) {
		attackStrength = 5;
		attackSpeed = 20;
		attackRange = 2;
		knockback = 0;
		potionType = 0;
		potionDuration = 5;
		potionAmp = 0;
		this.npc = npc;
	}

	@Override
	public int getDelay() {
		return attackSpeed;
	}

	@Override
	public int getEffectStrength() {
		return potionAmp;
	}

	@Override
	public int getEffectTime() {
		return potionDuration;
	}

	@Override
	public int getEffectType() {
		return potionType;
	}

	@Override
	public int getKnockback() {
		return knockback;
	}

	@Override
	public int getRange() {
		return attackRange;
	}

	@Override
	public int getStrength() {
		return attackStrength;
	}

	public void readFromNBT(final NBTTagCompound compound) {
		attackSpeed = compound.getInteger("AttackSpeed");
		setStrength(compound.getInteger("AttackStrenght"));
		attackRange = compound.getInteger("AttackRange");
		knockback = compound.getInteger("KnockBack");
		potionType = compound.getInteger("PotionEffect");
		potionDuration = compound.getInteger("PotionDuration");
		potionAmp = compound.getInteger("PotionAmp");
	}

	@Override
	public void setDelay(final int speed) {
		attackSpeed = speed;
	}

	@Override
	public void setEffect(final int type, final int strength, final int time) {
		potionType = type;
		potionDuration = time;
		potionAmp = strength;
	}

	@Override
	public void setKnockback(final int knockback) {
		this.knockback = knockback;
	}

	@Override
	public void setRange(final int range) {
		attackRange = range;
	}

	@Override
	public void setStrength(final int strength) {
		attackStrength = strength;
		npc.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(attackStrength);
	}

	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setInteger("AttackStrenght", attackStrength);
		compound.setInteger("AttackSpeed", attackSpeed);
		compound.setInteger("AttackRange", attackRange);
		compound.setInteger("KnockBack", knockback);
		compound.setInteger("PotionEffect", potionType);
		compound.setInteger("PotionDuration", potionDuration);
		compound.setInteger("PotionAmp", potionAmp);
		return compound;
	}
}
