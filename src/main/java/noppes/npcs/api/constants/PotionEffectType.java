
package noppes.npcs.api.constants;

import net.minecraft.potion.Potion;

public class PotionEffectType {
	public static int NONE = 0;
	public static int FIRE = 1;
	public static int POISON = 2;
	public static int HUNGER = 3;
	public static int WEAKNESS = 4;
	public static int SLOWNESS = 5;
	public static int NAUSEA = 6;
	public static int BLINDNESS = 7;
	public static int WITHER = 8;

	public static Potion getMCType(int effect) {
		switch (effect) {
		case 2: {
			return Potion.poison;
		}
		case 3: {
			return Potion.hunger;
		}
		case 4: {
			return Potion.weakness;
		}
		case 5: {
			return Potion.moveSlowdown;
		}
		case 6: {
			return Potion.confusion;
		}
		case 7: {
			return Potion.blindness;
		}
		case 8: {
			return Potion.wither;
		}
		default: {
			return null;
		}
		}
	}
}
