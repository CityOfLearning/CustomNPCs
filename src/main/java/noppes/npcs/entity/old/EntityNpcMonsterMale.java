package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcMonsterMale extends EntityNPCInterface {

   public EntityNpcMonsterMale(World world) {
      super(world);
      this.display.setSkinTexture("customnpcs:textures/entity/monstermale/ZombieSteve.png");
   }

   public void onUpdate() {
      this.isDead = true;
      if(!this.worldObj.isRemote) {
         NBTTagCompound compound = new NBTTagCompound();
         this.writeToNBT(compound);
         EntityCustomNpc npc = new EntityCustomNpc(this.worldObj);
         npc.readFromNBT(compound);
         npc.ai.animationType = 8;
         this.worldObj.spawnEntityInWorld(npc);
      }

      super.onUpdate();
   }
}
