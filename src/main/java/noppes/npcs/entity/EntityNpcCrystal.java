package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcCrystal extends EntityNPCInterface {

   public EntityNpcCrystal(World world) {
      super(world);
      this.scaleX = 0.7F;
      this.scaleY = 0.7F;
      this.scaleZ = 0.7F;
      this.display.setSkinTexture("customnpcs:textures/entity/crystal/EnderCrystal.png");
   }

   public void onUpdate() {
      this.isDead = true;
      if(!this.worldObj.isRemote) {
         NBTTagCompound compound = new NBTTagCompound();
         this.writeToNBT(compound);
         EntityCustomNpc npc = new EntityCustomNpc(this.worldObj);
         npc.readFromNBT(compound);
         ModelData data = npc.modelData;
         data.setEntityClass(EntityNpcCrystal.class);
         this.worldObj.spawnEntityInWorld(npc);
      }

      super.onUpdate();
   }
}
