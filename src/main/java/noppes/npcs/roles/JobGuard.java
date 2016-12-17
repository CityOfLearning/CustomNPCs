
package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTTags;

public class JobGuard extends JobInterface {
	public List<String> targets;

	public JobGuard(EntityNPCInterface npc) {
		super(npc);
		targets = new ArrayList<String>();
	}

	public boolean isEntityApplicable(Entity entity) {
		return !(entity instanceof EntityPlayer) && !(entity instanceof EntityNPCInterface)
				&& targets.contains("entity." + EntityList.getEntityString(entity) + ".name");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		targets = NBTTags.getStringList(nbttagcompound.getTagList("GuardTargets", 10));
		if (nbttagcompound.getBoolean("GuardAttackAnimals")) {
			for (Object entity : EntityList.stringToClassMapping.keySet()) {
				String name = "entity." + entity + ".name";
				Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityAnimal.class.isAssignableFrom(cl) && !targets.contains(name)) {
					targets.add(name);
				}
			}
		}
		if (nbttagcompound.getBoolean("GuardAttackMobs")) {
			for (Object entity : EntityList.stringToClassMapping.keySet()) {
				String name = "entity." + entity + ".name";
				Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityMob.class.isAssignableFrom(cl) && !EntityCreeper.class.isAssignableFrom(cl)
						&& !targets.contains(name)) {
					targets.add(name);
				}
			}
		}
		if (nbttagcompound.getBoolean("GuardAttackCreepers")) {
			for (Object entity : EntityList.stringToClassMapping.keySet()) {
				String name = "entity." + entity + ".name";
				Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityCreeper.class.isAssignableFrom(cl) && !targets.contains(name)) {
					targets.add(name);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("GuardTargets", NBTTags.nbtStringList(targets));
		return nbttagcompound;
	}
}
