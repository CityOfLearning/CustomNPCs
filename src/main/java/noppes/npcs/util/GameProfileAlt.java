package noppes.npcs.util;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import noppes.npcs.entity.EntityNPCInterface;

public class GameProfileAlt extends GameProfile {

   private static final UUID id = UUID.randomUUID();
   public EntityNPCInterface npc;


   public GameProfileAlt() {
      super((UUID)null, "customnpc");
   }

   public String getName() {
      return this.npc == null?super.getName():this.npc.getName();
   }

   public UUID getId() {
      return this.npc == null?id:this.npc.getPersistentID();
   }

}
