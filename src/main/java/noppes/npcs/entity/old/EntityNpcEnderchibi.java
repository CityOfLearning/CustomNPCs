package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcEnderchibi extends EntityNPCInterface {

   public EntityNpcEnderchibi(World world) {
      super(world);
      this.display.setSkinTexture("customnpcs:textures/entity/enderchibi/MrEnderchibi.png");
   }

   public void onUpdate() {
      this.isDead = true;
      if(!this.worldObj.isRemote) {
         NBTTagCompound compound = new NBTTagCompound();
         this.writeToNBT(compound);
         EntityCustomNpc npc = new EntityCustomNpc(this.worldObj);
         npc.readFromNBT(compound);
         ModelData data = npc.modelData;
         data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.65F, 0.75F);
         data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.5F, 1.45F);
         ModelPartData part = data.getOrCreatePart(EnumParts.PARTICLES);
         part.type = 1;
         part.color = 16711680;
         part.playerTexture = true;
         this.worldObj.spawnEntityInWorld(npc);
      }

      super.onUpdate();
   }
}
