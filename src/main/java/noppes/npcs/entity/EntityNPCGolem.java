package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCGolem extends EntityNPCInterface {

   public EntityNPCGolem(World world) {
      super(world);
      this.display.setSkinTexture("customnpcs:textures/entity/golem/Iron Golem.png");
      this.width = 1.4F;
      this.height = 2.5F;
   }

   public void updateHitbox() {
      this.currentAnimation = this.dataWatcher.getWatchableObjectInt(14);
      if(this.currentAnimation == 2) {
         this.width = this.height = 0.5F;
      } else if(this.currentAnimation == 1) {
         this.width = 1.4F;
         this.height = 2.0F;
      } else {
         this.width = 1.4F;
         this.height = 2.5F;
      }

   }

   public void onUpdate() {
      this.isDead = true;
      if(!this.worldObj.isRemote) {
         NBTTagCompound compound = new NBTTagCompound();
         this.writeToNBT(compound);
         EntityCustomNpc npc = new EntityCustomNpc(this.worldObj);
         npc.readFromNBT(compound);
         ModelData data = npc.modelData;
         data.setEntityClass(EntityNPCGolem.class);
         this.worldObj.spawnEntityInWorld(npc);
      }

      super.onUpdate();
   }
}
