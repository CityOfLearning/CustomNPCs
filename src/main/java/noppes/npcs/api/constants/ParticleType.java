//

//

package noppes.npcs.api.constants;

import net.minecraft.util.EnumParticleTypes;

public class ParticleType {
	public static final int NONE = 0;
	public static final int SMOKE = 1;
	public static final int PORTAL = 2;
	public static final int REDSTONE = 3;
	public static final int LIGHTNING = 4;
	public static final int LARGE_SMOKE = 5;
	public static final int MAGIC = 6;
	public static final int ENCHANT = 7;
	public static final int CRIT = 8;

	public static EnumParticleTypes getMCType(final int type) {
		if (type == 1) {
			return EnumParticleTypes.SMOKE_NORMAL;
		}
		if (type == 2) {
			return EnumParticleTypes.PORTAL;
		}
		if (type == 3) {
			return EnumParticleTypes.REDSTONE;
		}
		if (type == 4) {
			return EnumParticleTypes.CRIT_MAGIC;
		}
		if (type == 5) {
			return EnumParticleTypes.SMOKE_LARGE;
		}
		if (type == 6) {
			return EnumParticleTypes.SPELL_WITCH;
		}
		if (type == 7) {
			return EnumParticleTypes.ENCHANTMENT_TABLE;
		}
		if (type == 8) {
			return EnumParticleTypes.CRIT;
		}
		return null;
	}
}
