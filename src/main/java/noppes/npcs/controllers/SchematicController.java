//

//

package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Schematic;

public class SchematicController {
	public static SchematicController Instance;
	static {
		SchematicController.Instance = new SchematicController();
	}
	private Schematic building;
	private ICommandSender buildStarter;
	private int buildingPercentage;

	public List<String> included;

	public SchematicController() {
		building = null;
		buildStarter = null;
		buildingPercentage = 0;
		included = Arrays.asList("Archery_Range", "Bakery", "Barn", "Building_Site", "Chapel", "Church", "Gate",
				"Glassworks", "Guard_Tower", "Guild_House", "House", "House_Small", "Inn", "Library", "Lighthouse",
				"Mill", "Observatory", "Ship", "Shop", "Stall", "Stall2", "Stall3", "Tier_House1", "Tier_House2",
				"Tier_House3", "Tower", "Wall", "Wall_Corner");
	}

	public void build(final Schematic schem, final ICommandSender sender) {
		if ((building != null) && building.isBuilding) {
			info(sender);
			return;
		}
		buildingPercentage = 0;
		building = schem;
		building.isBuilding = true;
		buildStarter = sender;
	}

	public File getDir() {
		final File dir = new File(CustomNpcs.getWorldSaveDirectory(), "schematics");
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	public void info(final ICommandSender sender) {
		if (building == null) {
			sendMessage(sender, "Nothing is being build");
		} else {
			sendMessage(sender, "Already building: " + building.name + " - " + building.getPercentage() + "%");
			if (buildStarter != null) {
				sendMessage(sender, "Build started by: " + buildStarter.getName());
			}
		}
	}

	public List<String> list() {
		final List<String> list = new ArrayList<String>();
		list.addAll(included);
		for (final File file : getDir().listFiles()) {
			final String name = file.getName();
			if (name.toLowerCase().endsWith(".schematic") && !name.contains(" ")) {
				list.add(name.substring(0, name.length() - 10));
			}
		}
		Collections.sort(list);
		return list;
	}

	public Schematic load(final String name) {
		InputStream stream = null;
		if (included.contains(name)) {
			stream = MinecraftServer.class.getResourceAsStream("/assets/customnpcs/schematics/" + name + ".schematic");
		}
		if (stream == null) {
			final File file = new File(getDir(), name + ".schematic");
			if (!file.exists()) {
				return null;
			}
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e2) {
				return null;
			}
		}
		try {
			final Schematic schema = new Schematic(name);
			schema.load(CompressedStreamTools.readCompressed(stream));
			stream.close();
			return schema;
		} catch (IOException e) {
			LogWriter.except(e);
			return null;
		}
	}

	public void save(final ICommandSender sender, String name, final BlockPos pos, final short height,
			final short width, final short length) {
		name = name.replace(" ", "_");
		if (included.contains(name)) {
			return;
		}
		final Schematic schema = new Schematic(name);
		schema.height = height;
		schema.width = width;
		schema.length = length;
		schema.size = height * width * length;
		schema.blockArray = new short[schema.size];
		schema.blockDataArray = new byte[schema.size];
		NoppesUtilServer.NotifyOPs("Creating schematic at: " + pos + " might lag slightly", new Object[0]);
		final World world = sender.getEntityWorld();
		schema.tileList = new NBTTagList();
		for (int i = 0; i < schema.size; ++i) {
			final int x = i % width;
			final int z = ((i - x) / width) % length;
			final int y = (((i - x) / width) - z) / length;
			final IBlockState state = world.getBlockState(pos.add(x, y, z));
			if (state.getBlock() != Blocks.air) {
				if (state.getBlock() != CustomItems.copy) {
					schema.blockArray[i] = (short) Block.blockRegistry.getIDForObject(state.getBlock());
					schema.blockDataArray[i] = (byte) state.getBlock().getMetaFromState(state);
					if (state.getBlock() instanceof ITileEntityProvider) {
						final TileEntity tile = world.getTileEntity(pos.add(x, y, z));
						final NBTTagCompound compound = new NBTTagCompound();
						tile.writeToNBT(compound);
						compound.setInteger("x", x);
						compound.setInteger("y", y);
						compound.setInteger("z", z);
						schema.tileList.appendTag(compound);
					}
				}
			}
		}
		final File file = new File(getDir(), name + ".schematic");
		NoppesUtilServer.NotifyOPs("Schematic " + name + " succesfully created", new Object[0]);
		try {
			CompressedStreamTools.writeCompressed(schema.save(), new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(final ICommandSender sender, final String message) {
		if (sender == null) {
			return;
		}
		sender.addChatMessage(new ChatComponentText(message));
	}

	public void stop(final ICommandSender sender) {
		if ((building == null) || !building.isBuilding) {
			sendMessage(sender, "Not building");
		} else {
			sendMessage(sender, "Stopped building: " + building.name);
			building = null;
		}
	}

	public void updateBuilding() {
		if (building == null) {
			return;
		}
		building.build();
		if ((buildStarter != null) && ((building.getPercentage() - buildingPercentage) >= 10)) {
			sendMessage(buildStarter, "Building at " + building.getPercentage() + "%");
			buildingPercentage = building.getPercentage();
		}
		if (!building.isBuilding) {
			if (buildStarter != null) {
				sendMessage(buildStarter, "Building finished");
			}
			building = null;
		}
	}
}
