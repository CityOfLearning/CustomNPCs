
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

import com.dyn.schematics.Schematic;
import com.google.common.collect.Lists;

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
import net.minecraftforge.fml.common.registry.GameData;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.util.NoppesUtilServer;

public class SchematicController {
	public static SchematicController instance;
	static {
		SchematicController.instance = new SchematicController();
	}
	private Schematic building;
	private ICommandSender buildStarter;
	public List<String> included = Lists.newArrayList();

	public SchematicController() {
		building = null;
		buildStarter = null;
		included.addAll(Arrays.asList("Archery_Range", "Bakery", "Barn", "Building_Site", "Chapel", "Church", "Gate",
				"Glassworks", "Guard_Tower", "Guild_House", "House", "House_Small", "Inn", "Library", "Lighthouse",
				"Mill", "Observatory", "Rollercoaster", "Ship", "Shop", "Stall", "Stall2", "Stall3", "Tier_House1",
				"Tier_House2", "Tier_House3", "Tower", "Wall", "Wall_Corner"));
	}

	public File getDir() {
		File dir = new File(CustomNpcs.getWorldSaveDirectory(), "schematics");
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	public void info(ICommandSender sender) {
		if (building == null) {
			sendMessage(sender, "Nothing is being built");
		} else if (buildStarter != null) {
			sendMessage(sender, "Build started by: " + buildStarter.getName());
		}
	}

	public List<String> list() {
		List<String> list = new ArrayList<>();
		list.addAll(included);
		for (File file : getDir().listFiles()) {
			String name = file.getName();
			if (name.toLowerCase().endsWith(".schematic") && !name.contains(" ")) {
				list.add(name.substring(0, name.length() - 10));
			}
		}
		Collections.sort(list);
		return list;
	}

	public Schematic load(String name) {
		InputStream stream = null;
		if (included.contains(name)) {
			stream = MinecraftServer.class.getResourceAsStream("/assets/customnpcs/schematics/" + name + ".schematic");
		}
		if (stream == null) {
			File file = new File(getDir(), name + ".schematic");
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
			Schematic schema = new Schematic(name);
			schema.readFromNBT(CompressedStreamTools.readCompressed(stream));
			stream.close();
			return schema;
		} catch (IOException e) {
			CustomNpcs.logger.catching(e);
			return null;
		}
	}

	public void save(ICommandSender sender, String name, BlockPos pos, short height, short width, short length) {
		name = name.replace(" ", "_");
		if (included.contains(name)) {
			return;
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort("Width", width);
		nbt.setShort("Height", height);
		nbt.setShort("Length", length);

		nbt.setString("Materials", "Alpha");

		NoppesUtilServer.NotifyOPs("Creating schematic at: " + pos + " might lag slightly", new Object[0]);
		World world = sender.getEntityWorld();

		NBTTagList tileEntities = new NBTTagList();

		byte[] blocks = new byte[width * height * length];
		byte[] blocksMeta = new byte[width * height * length];
		byte[] addBlocks = null;

		for (int i = 0; i < width * height * length; ++i) {
			int x = i % width;
			int z = ((i - x) / width) % length;
			int y = (((i - x) / width) - z) / length;
			IBlockState state = world.getBlockState(pos.add(x, y, z));
			if (state.getBlock() != Blocks.air) {
				if (state.getBlock() != CustomItems.copy) {
					Block block = state.getBlock();
					int id = GameData.getBlockRegistry().getId(block);
					int meta = block.getMetaFromState(state);
					int index = y * width * length + z * width + x;
					if (id > 255) {
						if (addBlocks == null) {
							addBlocks = new byte[(blocks.length >> 1) + 1];
						}

						if ((index & 0x1) == 0x0) {
							addBlocks[index >> 1] = (byte) ((addBlocks[index >> 1] & 0xF0) | ((id >> 8) & 0xF));
						} else {
							addBlocks[index >> 1] = (byte) ((addBlocks[index >> 1] & 0xF) | (((id >> 8) & 0xF) << 4));
						}
					}
					blocks[index] = (byte) id;
					blocksMeta[index] = (byte) meta;

					if (state.getBlock() instanceof ITileEntityProvider) {
						TileEntity tile = world.getTileEntity(pos.add(x, y, z));
						if (tile != null) {
							NBTTagCompound tag = new NBTTagCompound();
							tile.writeToNBT(tag);
							tag.setInteger("x", x);
							tag.setInteger("y", y);
							tag.setInteger("z", z);
							tileEntities.appendTag(tag);
						}
					}
				}
			}
		}
		
		nbt.setByteArray("Blocks", blocks);
		nbt.setByteArray("Data", blocksMeta);
		if (addBlocks.length > 0) {
			nbt.setByteArray("AddBlocks", addBlocks);
		}
		nbt.setTag("TileEntities", tileEntities);
		
		File file = new File(getDir(), name + ".schematic");
		NoppesUtilServer.NotifyOPs("Schematic " + name + " succesfully created", new Object[0]);
		try {
			CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(ICommandSender sender, String message) {
		if (sender == null) {
			return;
		}
		sender.addChatMessage(new ChatComponentText(message));
	}

	public void stop(ICommandSender sender) {
		if ((building == null)) {
			sendMessage(sender, "Not building");
		} else {
			sendMessage(sender, "Stopped building: " + building.getName());
			building = null;
		}
	}

	public void build(Schematic schem, ICommandSender sender, World world, BlockPos pos, int rotation) {
		if ((building != null)) {
			info(sender);
			return;
		}
		building = schem;
		buildStarter = sender;
		building.build(world, pos, rotation, sender);
	}
}
