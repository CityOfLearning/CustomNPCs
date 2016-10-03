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


   public static EnumParticleTypes getMCType(int type) {
      return type == 1?EnumParticleTypes.SMOKE_NORMAL:(type == 2?EnumParticleTypes.PORTAL:(type == 3?EnumParticleTypes.REDSTONE:(type == 4?EnumParticleTypes.CRIT_MAGIC:(type == 5?EnumParticleTypes.SMOKE_LARGE:(type == 6?EnumParticleTypes.SPELL_WITCH:(type == 7?EnumParticleTypes.ENCHANTMENT_TABLE:(type == 8?EnumParticleTypes.CRIT:null)))))));
   }
}
