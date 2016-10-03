package noppes.npcs.entity.old;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.old.EntityNpcEnderchibi;

public class EntityNPCEnderman extends EntityNpcEnderchibi {

   public EntityNPCEnderman(World world) {
      super(world);
      this.display.setSkinTexture("customnpcs:textures/entity/enderman/enderman.png");
      this.display.setOverlayTexture("customnpcs:textures/overlays/ender_eyes.png");
      this.width = 0.6F;
      this.height = 2.9F;
   }

   public void updateHitbox() {
      if(this.currentAnimation == 2) {
         this.width = this.height = 0.2F;
      } else if(this.currentAnimation == 1) {
         this.width = 0.6F;
         this.height = 2.3F;
      } else {
         this.width = 0.6F;
         this.height = 2.9F;
      }

      this.width = this.width / 5.0F * (float)this.display.getSize();
      this.height = this.height / 5.0F * (float)this.display.getSize();
   }

   public void onUpdate() {
      this.isDead = true;
      if(!this.worldObj.isRemote) {
         NBTTagCompound compound = new NBTTagCompound();
         this.writeToNBT(compound);
         EntityCustomNpc npc = new EntityCustomNpc(this.worldObj);
         npc.readFromNBT(compound);
         ModelData data = npc.modelData;
         data.setEntityClass(EntityEnderman.class);
         this.worldObj.spawnEntityInWorld(npc);
      }

      super.onUpdate();
   }
}
