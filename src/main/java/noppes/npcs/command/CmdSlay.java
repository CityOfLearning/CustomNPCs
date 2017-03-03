
package noppes.npcs.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdSlay extends CommandNoppesBase {
	public Map<String, Class<?>> SlayMap;

	public CmdSlay() {
		(SlayMap = new LinkedHashMap<>()).clear();
		SlayMap.put("all", EntityLivingBase.class);
		SlayMap.put("mobs", EntityMob.class);
		SlayMap.put("animals", EntityAnimal.class);
		SlayMap.put("items", EntityItem.class);
		SlayMap.put("xporbs", EntityXPOrb.class);
		HashMap<String, Class<?>> list = new HashMap<>(EntityList.stringToClassMapping);
		for (String name : list.keySet()) {
			Class<?> cls = list.get(name);
			if (EntityNPCInterface.class.isAssignableFrom(cls)) {
				continue;
			}
			if (!EntityLivingBase.class.isAssignableFrom(cls)) {
				continue;
			}
			SlayMap.put(name.toLowerCase(), list.get(name));
		}
		SlayMap.remove("monster");
		SlayMap.remove("mob");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return CommandBase.getListOfStringsMatchingLastWord(args, SlayMap.keySet().toArray(new String[SlayMap.size()]));
	}

	private boolean delete(Entity entity, ArrayList<Class<?>> toDelete) {
		for (Class<?> delete : toDelete) {
			if ((delete == EntityAnimal.class) && (entity instanceof EntityHorse)) {
				continue;
			}
			if (delete.isAssignableFrom(entity.getClass())) {
				return entity.isDead = true;
			}
		}
		return false;
	}

	@Override
	public String getCommandName() {
		return "slay";
	}

	@Override
	public String getDescription() {
		return "Kills given entity within range. Also has all, mobs, animal options. Can have multiple types";
	}

	@Override
	public String getUsage() {
		return "<type>.. [range]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		ArrayList<Class<?>> toDelete = new ArrayList<>();
		boolean deleteNPCs = false;
		for (String delete : args) {
			delete = delete.toLowerCase();
			Class<?> cls = SlayMap.get(delete);
			if (cls != null) {
				toDelete.add(cls);
			}
			if (delete.equals("mobs")) {
				toDelete.add(EntityGhast.class);
				toDelete.add(EntityDragon.class);
			}
			if (delete.equals("npcs")) {
				deleteNPCs = true;
			}
		}
		int count = 0;
		int range = 120;
		try {
			range = Integer.parseInt(args[args.length - 1]);
		} catch (NumberFormatException ex) {
		}
		AxisAlignedBB box = new AxisAlignedBB(sender.getPosition(), sender.getPosition().add(1, 1, 1)).expand(range,
				range, range);
		List<? extends Entity> list = sender.getEntityWorld().getEntitiesWithinAABB((Class) EntityLivingBase.class,
				box);
		for (Entity entity : list) {
			if (entity instanceof EntityPlayer) {
				continue;
			}
			if ((entity instanceof EntityTameable) && ((EntityTameable) entity).isTamed()) {
				continue;
			}
			if ((entity instanceof EntityNPCInterface) && !deleteNPCs) {
				continue;
			}
			if (!delete(entity, toDelete)) {
				continue;
			}
			++count;
		}
		if (toDelete.contains(EntityXPOrb.class)) {
			list = sender.getEntityWorld().getEntitiesWithinAABB((Class) EntityXPOrb.class, box);
			for (Entity entity : list) {
				entity.isDead = true;
				++count;
			}
		}
		if (toDelete.contains(EntityItem.class)) {
			list = sender.getEntityWorld().getEntitiesWithinAABB((Class) EntityItem.class, box);
			for (Entity entity : list) {
				entity.isDead = true;
				++count;
			}
		}
		sender.addChatMessage(new ChatComponentTranslation(count + " entities deleted", new Object[0]));
	}
}
