
package noppes.npcs.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class Resistances {
	public float knockback;
	public float arrow;
	public float melee;
	public float explosion;

	public Resistances() {
		knockback = 1.0f;
		arrow = 1.0f;
		melee = 1.0f;
		explosion = 1.0f;
	}

	public float applyResistance(DamageSource source, float damage) {
		if (source.damageType.equals("arrow") || source.damageType.equals("thrown") || source.isProjectile()) {
			damage *= 2.0f - arrow;
		} else if (source.damageType.equals("player") || source.damageType.equals("mob")) {
			damage *= 2.0f - melee;
		} else if (source.damageType.equals("explosion") || source.damageType.equals("explosion.player")) {
			damage *= 2.0f - explosion;
		}
		return damage;
	}

	public void readToNBT(NBTTagCompound compound) {
		knockback = compound.getFloat("Knockback");
		arrow = compound.getFloat("Arrow");
		melee = compound.getFloat("Melee");
		explosion = compound.getFloat("Explosion");
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setFloat("Knockback", knockback);
		compound.setFloat("Arrow", arrow);
		compound.setFloat("Melee", melee);
		compound.setFloat("Explosion", explosion);
		return compound;
	}
}
