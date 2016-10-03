package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcSlime extends EntityNPCInterface {

   public EntityNpcSlime(World world) {
      super(world);
      this.scaleX = 2.0F;
      this.scaleY = 2.0F;
      this.scaleZ = 2.0F;
      this.display.setSkinTexture("customnpcs:textures/entity/slime/Slime.png");
      this.width = 0.8F;
      this.height = 0.8F;
   }

   public void updateHitbox() {
      this.width = 0.8F;
      this.height = 0.8F;
   }

   public void onUpdate() {
      this.isDead = true;
      if(!this.worldObj.isRemote) {
         NBTTagCompound compound = new NBTTagCompound();
         this.writeToNBT(compound);
         EntityCustomNpc npc = new EntityCustomNpc(this.worldObj);
         npc.readFromNBT(compound);
         ModelData data = npc.modelData;
         data.setEntityClass(EntityNpcSlime.class);
         this.worldObj.spawnEntityInWorld(npc);
      }

      super.onUpdate();
   }
}
