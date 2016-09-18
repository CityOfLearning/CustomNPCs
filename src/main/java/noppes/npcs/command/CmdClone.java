
package noppes.npcs.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdClone extends CommandNoppesBase {
	@SubCommand(desc = "Add NPC(s) to clone storage", usage = "<npc> <tab> [clonedname]", permission = 4)
	public void add(ICommandSender sender, String[] args) {
		int tab = 0;
		try {
			tab = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
		}
		List<EntityNPCInterface> list = this.getEntities(EntityNPCInterface.class, sender.getEntityWorld(),
				sender.getPosition(), 80);
		for (EntityNPCInterface npc : list) {
			if (npc.display.getName().equalsIgnoreCase(args[0])) {
				String name = npc.display.getName();
				if (args.length > 2) {
					name = args[2];
				}
				NBTTagCompound compound = new NBTTagCompound();
				if (!npc.writeToNBTOptional(compound)) {
					return;
				}
				ServerCloneController.Instance.addClone(compound, name, tab);
			}
		}
	}

	@SubCommand(desc = "Remove NPC from clone storage", usage = "<name> <tab>", permission = 4)
	public void del(ICommandSender sender, String[] args) throws CommandException {
		String nametodel = args[0];
		int tab = 0;
		try {
			tab = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
		}
		for (String name : ServerCloneController.Instance.getClones(tab)) {
			if (nametodel.equalsIgnoreCase(name)) {
				ServerCloneController.Instance.removeClone(name, tab);
				break;
			}
		}
		if (!ServerCloneController.Instance.removeClone(nametodel, tab)) {
			throw new CommandException("Npc '%s' wasn't found", new Object[] { nametodel });
		}
	}

	@Override
	public String getCommandName() {
		return "clone";
	}

	@Override
	public String getDescription() {
		return "Clone operation (server side)";
	}

	public <T extends Entity> List<T> getEntities(Class<? extends T> cls, World world, BlockPos pos, int range) {
		return world.getEntitiesWithinAABB((Class) cls,
				new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(range, range, range));
	}

	public World getWorld(String t) {
		WorldServer[] worldServers;
		worldServers = MinecraftServer.getServer().worldServers;
		for (WorldServer w : worldServers) {
			if ((w != null) && (w.provider.getDimensionId() + "").equalsIgnoreCase(t)) {
				return w;
			}
		}
		return null;
	}

	@SubCommand(desc = "List NPC from clone storage", usage = "<tab>", permission = 2)
	public void list(ICommandSender sender, String[] args) {
		sendMessage(sender, "--- Stored NPCs --- (server side)", new Object[0]);
		int tab = 0;
		try {
			tab = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
		}
		for (String name : ServerCloneController.Instance.getClones(tab)) {
			sendMessage(sender, name, new Object[0]);
		}
		sendMessage(sender, "------------------------------------", new Object[0]);
	}

	@SubCommand(desc = "Spawn cloned NPC", usage = "<name> <tab> [[world:]x,y,z]] [newname]", permission = 2)
	public void spawn(ICommandSender sender, String[] args) throws CommandException {
		String name = args[0].replaceAll("%", " ");
		int tab = 0;
		try {
			tab = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
		}
		String newname = null;
		NBTTagCompound compound = ServerCloneController.Instance.getCloneData(sender, name, tab);
		if (compound == null) {
			throw new CommandException("Unknown npc", new Object[0]);
		}
		World world = sender.getEntityWorld();
		BlockPos pos = sender.getPosition();
		if (args.length > 2) {
			String location = args[2];
			if (location.contains(":")) {
				String[] par = location.split(":");
				location = par[1];
				world = getWorld(par[0]);
				if (world == null) {
					throw new CommandException("'%s' is an unknown world", new Object[] { par[0] });
				}
			}
			if (location.contains(",")) {
				String[] par = location.split(",");
				if (par.length != 3) {
					throw new CommandException("Location need be x,y,z", new Object[0]);
				}
				try {
					pos = CommandBase.parseBlockPos(sender, par, 0, false);
				} catch (NumberInvalidException e) {
					throw new CommandException("Location should be in numbers", new Object[0]);
				}
				if (args.length > 3) {
					newname = args[3];
				}
			} else {
				newname = location;
			}
		}
		if ((pos.getX() == 0) && (pos.getY() == 0) && (pos.getZ() == 0)) {
			throw new CommandException("Location needed", new Object[0]);
		}
		Entity entity = EntityList.createEntityFromNBT(compound, world);
		entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
		if (entity instanceof EntityNPCInterface) {
			EntityNPCInterface npc = (EntityNPCInterface) entity;
			npc.ai.setStartPos(pos);
			if ((newname != null) && !newname.isEmpty()) {
				npc.display.setName(newname.replaceAll("%", " "));
			}
		}
		world.spawnEntityInWorld(entity);
	}
}
