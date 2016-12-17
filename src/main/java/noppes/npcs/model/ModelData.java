
package noppes.npcs.model;

import java.lang.reflect.Method;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelData extends ModelDataShared {
	public ModelData copy() {
		ModelData data = new ModelData();
		data.readFromNBT(writeToNBT());
		return data;
	}

	public EntityLivingBase getEntity(EntityNPCInterface npc) {
		if (entityClass == null) {
			return null;
		}
		if (entity == null) {
			try {
				(entity = entityClass.getConstructor(World.class).newInstance(npc.worldObj)).readEntityFromNBT(extra);
				if (entity instanceof EntityLiving) {
					EntityLiving living = (EntityLiving) entity;
					for (int i = 0; i < 5; ++i) {
						living.setCurrentItemOrArmor(0, npc.getEquipmentInSlot(i));
					}
				}
			} catch (Exception ex) {
			}
		}
		return entity;
	}

	public void setExtra(EntityLivingBase entity, String key, String value) {
		key = key.toLowerCase();
		if (key.equals("breed") && EntityList.getEntityString(entity).equals("tgvstyle.Dog")) {
			try {
				Method method = entity.getClass().getMethod("getBreedID", new Class[0]);
				Enum breed = (Enum) method.invoke(entity, new Object[0]);
				method = entity.getClass().getMethod("setBreedID", breed.getClass());
				method.invoke(entity, breed.getClass().getEnumConstants()[Integer.parseInt(value)]);
				NBTTagCompound comp = new NBTTagCompound();
				entity.writeEntityToNBT(comp);
				extra.setString("EntityData21", comp.getString("EntityData21"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (key.equalsIgnoreCase("name")) {
			extra.setString("Name", value);
		}
		clearEntity();
	}
}
