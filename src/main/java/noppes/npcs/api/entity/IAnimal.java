package noppes.npcs.api.entity;

import net.minecraft.entity.passive.EntityAnimal;
import noppes.npcs.api.entity.IEntityLiving;

public interface IAnimal extends IEntityLiving {

   EntityAnimal getMCEntity();
}
