package noppes.npcs.api.wrapper;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import noppes.npcs.api.entity.IEntity;

public class WrapperEntityData implements IExtendedEntityProperties {

   public IEntity base;


   public WrapperEntityData(IEntity base) {
      this.base = base;
   }

   public void saveNBTData(NBTTagCompound compound) {}

   public void loadNBTData(NBTTagCompound compound) {}

   public void init(Entity entity, World world) {}
}
