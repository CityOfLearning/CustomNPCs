package noppes.npcs.api.entity;

import net.minecraft.entity.monster.EntityMob;
import noppes.npcs.api.entity.IEntityLiving;

public interface IMonster extends IEntityLiving {

   EntityMob getMCEntity();
}
