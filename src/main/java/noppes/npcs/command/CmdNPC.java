
package noppes.npcs.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class CmdNPC extends CommandNoppesBase {
	public EntityNPCInterface selectedNpc;

	@Override
	public List addTabCompletionOptions(ICommandSender par1, String[] args, BlockPos pos) {
		if (args.length == 2) {
			return CommandBase.getListOfStringsMatchingLastWord(args,
					new String[] { "create", "home", "visible", "delete", "owner", "name" });
		}
		if ((args.length == 3) && args[1].equalsIgnoreCase("owner")) {
			return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
		}
		return null;
	}

	@SubCommand(desc = "Creates an NPC", usage = "[name]")
	public void create(ICommandSender sender, String[] args) {
		World pw = sender.getEntityWorld();
		EntityCustomNpc npc = new EntityCustomNpc(pw);
		if (args.length > 0) {
			npc.display.setName(args[0]);
		}
		BlockPos pos = sender.getPosition();
		npc.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 0.0f, 0.0f);
		selectedNpc.ai.setStartPos(pos);
		pw.spawnEntityInWorld(npc);
		npc.setHealth(npc.getMaxHealth());
	}

	@SubCommand(desc = "Delete an NPC")
	public void delete(ICommandSender sender, String[] args) {
		selectedNpc.delete();
	}

	@Override
	public String getCommandName() {
		return "npc";
	}

	@Override
	public String getDescription() {
		return "NPC operation";
	}

	public <T extends Entity> List<T> getEntities(Class<? extends T> cls, World world, BlockPos pos, int range) {
		return world.getEntitiesWithinAABB(cls, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(range, range, range));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public String getUsage() {
		return "<name> <command>";
	}

	@SubCommand(desc = "Set Home (respawn place)", usage = "[x] [y] [z]")
	public void home(ICommandSender sender, String[] args) {
		BlockPos pos = sender.getPosition();
		if (args.length == 3) {
			try {
				pos = CommandBase.parseBlockPos(sender, args, 0, false);
			} catch (NumberInvalidException ex) {
			}
		}
		selectedNpc.ai.setStartPos(pos);
	}

	@SubCommand(desc = "Set NPC name", usage = "[name]")
	public void name(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			return;
		}
		String name = args[0];
		for (int i = 1; i < args.length; ++i) {
			name = name + " " + args[i];
		}
		if (!selectedNpc.display.getName().equals(name)) {
			selectedNpc.display.setName(name);
			selectedNpc.updateClient = true;
		}
	}

	@SubCommand(desc = "Sets the owner of an follower/companion", usage = "[player]")
	public void owner(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			EntityPlayer player = null;
			if (selectedNpc.roleInterface instanceof RoleFollower) {
				player = ((RoleFollower) selectedNpc.roleInterface).owner;
			}
			if (selectedNpc.roleInterface instanceof RoleCompanion) {
				player = ((RoleCompanion) selectedNpc.roleInterface).owner;
			}
			if (player == null) {
				sendMessage(sender, "No owner", new Object[0]);
			} else {
				sendMessage(sender, "Owner is: " + player.getName(), new Object[0]);
			}
		} else {
			EntityPlayerMP player2 = null;
			try {
				player2 = CommandBase.getPlayer(sender, args[0]);
			} catch (PlayerNotFoundException ex) {
			}
			if (selectedNpc.roleInterface instanceof RoleFollower) {
				((RoleFollower) selectedNpc.roleInterface).setOwner(player2);
			}
			if (selectedNpc.roleInterface instanceof RoleCompanion) {
				((RoleCompanion) selectedNpc.roleInterface).setOwner(player2);
			}
		}
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		String npcname = args[0].replace("%", " ");
		String command = args[1];
		args = Arrays.copyOfRange(args, 2, args.length);
		if (command.equalsIgnoreCase("create")) {
			processSubCommand(sender, command, new String[] { args[0], npcname });
			return;
		}
		List<EntityNPCInterface> list = this.getEntities(EntityNPCInterface.class, sender.getEntityWorld(),
				sender.getPosition(), 80);
		for (EntityNPCInterface npc : list) {
			String name = npc.display.getName().replace(" ", "_");
			if (name.equalsIgnoreCase(npcname) && ((selectedNpc == null)
					|| (selectedNpc.getDistanceSq(sender.getPosition()) > npc.getDistanceSq(sender.getPosition())))) {
				selectedNpc = npc;
			}
		}
		if (selectedNpc == null) {
			throw new CommandException("Npc '%s' was not found", new Object[] { npcname });
		}
		processSubCommand(sender, command, args);
		selectedNpc = null;
	}

	@Override
	public boolean runSubCommands() {
		return false;
	}

	@SubCommand(desc = "Set NPC visibility", usage = "[true/false/semi]")
	public void visible(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			return;
		}
		boolean bo = args[0].equalsIgnoreCase("true");
		boolean semi = args[0].equalsIgnoreCase("semi");
		selectedNpc.display.getVisible();
		if (semi) {
			selectedNpc.display.setVisible(2);
		} else if (bo) {
			selectedNpc.display.setVisible(0);
		} else {
			selectedNpc.display.setVisible(1);
		}
	}
}
