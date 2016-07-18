//

//

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
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;

public class JobGuard extends JobInterface {
	public List<String> targets;

	public JobGuard(final EntityNPCInterface npc) {
		super(npc);
		targets = new ArrayList<String>();
	}

	public boolean isEntityApplicable(final Entity entity) {
		return !(entity instanceof EntityPlayer) && !(entity instanceof EntityNPCInterface)
				&& targets.contains("entity." + EntityList.getEntityString(entity) + ".name");
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		targets = NBTTags.getStringList(nbttagcompound.getTagList("GuardTargets", 10));
		if (nbttagcompound.getBoolean("GuardAttackAnimals")) {
			for (final Object entity : EntityList.stringToClassMapping.keySet()) {
				final String name = "entity." + entity + ".name";
				final Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityAnimal.class.isAssignableFrom(cl) && !targets.contains(name)) {
					targets.add(name);
				}
			}
		}
		if (nbttagcompound.getBoolean("GuardAttackMobs")) {
			for (final Object entity : EntityList.stringToClassMapping.keySet()) {
				final String name = "entity." + entity + ".name";
				final Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityMob.class.isAssignableFrom(cl) && !EntityCreeper.class.isAssignableFrom(cl)
						&& !targets.contains(name)) {
					targets.add(name);
				}
			}
		}
		if (nbttagcompound.getBoolean("GuardAttackCreepers")) {
			for (final Object entity : EntityList.stringToClassMapping.keySet()) {
				final String name = "entity." + entity + ".name";
				final Class cl = EntityList.stringToClassMapping.get(entity);
				if (EntityCreeper.class.isAssignableFrom(cl) && !targets.contains(name)) {
					targets.add(name);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("GuardTargets", NBTTags.nbtStringList(targets));
		return nbttagcompound;
	}
}
