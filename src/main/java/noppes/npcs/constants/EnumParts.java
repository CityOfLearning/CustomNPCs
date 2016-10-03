package noppes.npcs.constants;


public enum EnumParts {

   EARS("EARS", 0, "ears"),
   HORNS("HORNS", 1, "horns"),
   HAIR("HAIR", 2, "hair"),
   MOHAWK("MOHAWK", 3, "mohawk"),
   SNOUT("SNOUT", 4, "snout"),
   BEARD("BEARD", 5, "beard"),
   TAIL("TAIL", 6, "tail"),
   CLAWS("CLAWS", 7, "claws"),
   LEGS("LEGS", 8, "legs"),
   FIN("FIN", 9, "fin"),
   SKIRT("SKIRT", 10, "skirt"),
   WINGS("WINGS", 11, "wings"),
   HEAD("HEAD", 12, "head"),
   BODY("BODY", 13, "body"),
   BREASTS("BREASTS", 14, "breasts"),
   PARTICLES("PARTICLES", 15, "particles"),
   ARM_LEFT("ARM_LEFT", 16, "armleft"),
   ARM_RIGHT("ARM_RIGHT", 17, "armright"),
   LEG_LEFT("LEG_LEFT", 18, "legleft"),
   LEG_RIGHT("LEG_RIGHT", 19, "legright");
   public String name;
   public int patterns = 1;
   // $FF: synthetic field
   private static final EnumParts[] $VALUES = new EnumParts[]{EARS, HORNS, HAIR, MOHAWK, SNOUT, BEARD, TAIL, CLAWS, LEGS, FIN, SKIRT, WINGS, HEAD, BODY, BREASTS, PARTICLES, ARM_LEFT, ARM_RIGHT, LEG_LEFT, LEG_RIGHT};


   private EnumParts(String var1, int var2, String name) {
      this.name = name;
   }

   public static EnumParts FromName(String name) {
      EnumParts[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumParts e = var1[var3];
         if(e.name.equals(name)) {
            return e;
         }
      }

      return null;
   }

}
