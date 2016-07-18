//

//

package noppes.npcs.command;

import java.util.Arrays;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockVine;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.ChunkController;

public class CmdConfig extends CommandNoppesBase {
	@SubCommand(desc = "Set how many active chunkloaders you can have", usage = "<number>", permission = 4)
	public void chunkloaders(final ICommandSender sender, final String[] args) throws CommandException {
		if (args.length == 0) {
			sendMessage(sender, "ChunkLoaders: " + ChunkController.instance.size() + "/" + CustomNpcs.ChuckLoaders,
					new Object[0]);
		} else {
			try {
				CustomNpcs.ChuckLoaders = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				throw new CommandException("Didnt get a number", new Object[0]);
			}
			CustomNpcs.Config.updateConfig();
			final int size = ChunkController.instance.size();
			if (size > CustomNpcs.ChuckLoaders) {
				ChunkController.instance.unload(size - CustomNpcs.ChuckLoaders);
				sendMessage(sender, (size - CustomNpcs.ChuckLoaders) + " chunksloaders unloaded", new Object[0]);
			}
			sendMessage(sender, "ChunkLoaders: " + ChunkController.instance.size() + "/" + CustomNpcs.ChuckLoaders,
					new Object[0]);
		}
	}

	@SubCommand(desc = "Get/Set font", usage = "[type] [size]", permission = 4)
	public void font(final ICommandSender sender, String[] args) {
		if (!(sender instanceof EntityPlayerMP)) {
			return;
		}
		int size = 18;
		if (args.length > 1) {
			try {
				size = Integer.parseInt(args[args.length - 1]);
				args = Arrays.copyOfRange(args, 0, args.length - 1);
			} catch (Exception ex) {
			}
		}
		String font = "";
		for (int i = 0; i < args.length; ++i) {
			font = font + " " + args[i];
		}
		Server.sendData((EntityPlayerMP) sender, EnumPacketClient.CONFIG, 0, font.trim(), size);
	}

	@SubCommand(desc = "Freezes/Unfreezes npcs", usage = "[true/false]", permission = 4)
	public void freezenpcs(final ICommandSender sender, final String[] args) {
		if (args.length == 0) {
			sendMessage(sender, "Frozen NPCs: " + CustomNpcs.FreezeNPCs, new Object[0]);
		} else {
			CustomNpcs.FreezeNPCs = Boolean.parseBoolean(args[0]);
			sendMessage(sender, "FrozenNPCs is now " + CustomNpcs.FreezeNPCs, new Object[0]);
		}
	}

	@Override
	public String getCommandName() {
		return "config";
	}

	@Override
	public String getDescription() {
		return "Some config things you can set";
	}

	@SubCommand(desc = "Disable/Enable the ice melting", usage = "[true/false]", permission = 4)
	public void icemelts(final ICommandSender sender, final String[] args) {
		if (args.length == 0) {
			sendMessage(sender, "IceMelts: " + CustomNpcs.IceMeltsEnabled, new Object[0]);
		} else {
			CustomNpcs.IceMeltsEnabled = Boolean.parseBoolean(args[0]);
			CustomNpcs.Config.updateConfig();
			final Set<ResourceLocation> names = Block.blockRegistry.getKeys();
			for (final ResourceLocation name : names) {
				final Block block = Block.blockRegistry.getObject(name);
				if (block instanceof BlockIce) {
					block.setTickRandomly(CustomNpcs.IceMeltsEnabled);
				}
			}
			sendMessage(sender, "IceMelts is now " + CustomNpcs.IceMeltsEnabled, new Object[0]);
		}
	}

	@SubCommand(desc = "Disable/Enable the natural leaves decay", usage = "[true/false]", permission = 4)
	public void leavesdecay(final ICommandSender sender, final String[] args) {
		if (args.length == 0) {
			sendMessage(sender, "LeavesDecay: " + CustomNpcs.LeavesDecayEnabled, new Object[0]);
		} else {
			CustomNpcs.LeavesDecayEnabled = Boolean.parseBoolean(args[0]);
			CustomNpcs.Config.updateConfig();
			final Set<ResourceLocation> names = Block.blockRegistry.getKeys();
			for (final ResourceLocation name : names) {
				final Block block = Block.blockRegistry.getObject(name);
				if (block instanceof BlockLeavesBase) {
					block.setTickRandomly(CustomNpcs.LeavesDecayEnabled);
				}
			}
			sendMessage(sender, "LeavesDecay is now " + CustomNpcs.LeavesDecayEnabled, new Object[0]);
		}
	}

	@SubCommand(desc = "Disable/Enable the vines growing", usage = "[true/false]", permission = 4)
	public void vinegrowth(final ICommandSender sender, final String[] args) {
		if (args.length == 0) {
			sendMessage(sender, "VineGrowth: " + CustomNpcs.VineGrowthEnabled, new Object[0]);
		} else {
			CustomNpcs.VineGrowthEnabled = Boolean.parseBoolean(args[0]);
			CustomNpcs.Config.updateConfig();
			final Set<ResourceLocation> names = Block.blockRegistry.getKeys();
			for (final ResourceLocation name : names) {
				final Block block = Block.blockRegistry.getObject(name);
				if (block instanceof BlockVine) {
					block.setTickRandomly(CustomNpcs.VineGrowthEnabled);
				}
			}
			sendMessage(sender, "VineGrowth is now " + CustomNpcs.VineGrowthEnabled, new Object[0]);
		}
	}
}
