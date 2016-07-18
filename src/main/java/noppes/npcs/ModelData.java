//

//

package noppes.npcs;

import java.lang.reflect.Method;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelData extends ModelDataShared {
	public ModelData copy() {
		final ModelData data = new ModelData();
		data.readFromNBT(writeToNBT());
		return data;
	}

	public EntityLivingBase getEntity(final EntityNPCInterface npc) {
		if (entityClass == null) {
			return null;
		}
		if (entity == null) {
			try {
				(entity = entityClass.getConstructor(World.class).newInstance(npc.worldObj)).readEntityFromNBT(extra);
				if (entity instanceof EntityLiving) {
					final EntityLiving living = (EntityLiving) entity;
					for (int i = 0; i < 5; ++i) {
						living.setCurrentItemOrArmor(0, npc.getEquipmentInSlot(i));
					}
				}
				if (PixelmonHelper.isPixelmon(entity) && npc.worldObj.isRemote) {
					if (extra.hasKey("Name")) {
						PixelmonHelper.setName(entity, extra.getString("Name"));
					} else {
						PixelmonHelper.setName(entity, "Abra");
					}
				}
			} catch (Exception ex) {
			}
		}
		return entity;
	}

	public void setExtra(final EntityLivingBase entity, String key, final String value) {
		key = key.toLowerCase();
		if (key.equals("breed") && EntityList.getEntityString(entity).equals("tgvstyle.Dog")) {
			try {
				Method method = entity.getClass().getMethod("getBreedID", new Class[0]);
				final Enum breed = (Enum) method.invoke(entity, new Object[0]);
				method = entity.getClass().getMethod("setBreedID", breed.getClass());
				method.invoke(entity, breed.getClass().getEnumConstants()[Integer.parseInt(value)]);
				final NBTTagCompound comp = new NBTTagCompound();
				entity.writeEntityToNBT(comp);
				extra.setString("EntityData21", comp.getString("EntityData21"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (key.equalsIgnoreCase("name") && PixelmonHelper.isPixelmon(entity)) {
			extra.setString("Name", value);
		}
		clearEntity();
	}
}
