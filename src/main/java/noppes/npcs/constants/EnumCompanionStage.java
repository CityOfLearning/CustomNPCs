package noppes.npcs.constants;


public enum EnumCompanionStage {

   BABY("BABY", 0, 0, 7, "companion.baby"),
   CHILD("CHILD", 1, 72000, 0, "companion.child"),
   TEEN("TEEN", 2, 180000, 0, "companion.teenager"),
   ADULT("ADULT", 3, 324000, 0, "companion.adult"),
   FULLGROWN("FULLGROWN", 4, 450000, 0, "companion.fullgrown");
   public int matureAge;
   public int animation;
   public String name;
   // $FF: synthetic field
   private static final EnumCompanionStage[] $VALUES = new EnumCompanionStage[]{BABY, CHILD, TEEN, ADULT, FULLGROWN};


   private EnumCompanionStage(String var1, int var2, int age, int animation, String name) {
      this.matureAge = age;
      this.animation = animation;
      this.name = name;
   }

}
