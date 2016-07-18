//

//

package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.util.NBTJsonUtil;

public class PlayerDataController {
	public static PlayerDataController instance;

	public PlayerDataController() {
		PlayerDataController.instance = this;
	}

	public void addPlayerMessage(final String username, final PlayerMail mail) {
		mail.time = System.currentTimeMillis();
		MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
		final PlayerData data = getDataFromUsername(username);
		data.mailData.playermail.add(mail.copy());
		savePlayerData(data);
	}

	public PlayerBankData getBankData(final EntityPlayer player, final int bankId) {
		final Bank bank = BankController.getInstance().getBank(bankId);
		final PlayerBankData data = getPlayerData(player).bankData;
		if (!data.hasBank(bank.id)) {
			data.loadNew(bank.id);
		}
		return data;
	}

	public PlayerData getDataFromUsername(final String username) {
		final EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
		PlayerData data = null;
		if (player == null) {
			final Map<String, NBTTagCompound> map = getUsernameData();
			for (final String name : map.keySet()) {
				if (name.equalsIgnoreCase(username)) {
					data = new PlayerData();
					data.setNBT(map.get(name));
					break;
				}
			}
		} else {
			data = getPlayerData(player);
		}
		return data;
	}

	public PlayerData getPlayerData(final EntityPlayer player) {
		PlayerData data = (PlayerData) player.getExtendedProperties("CustomNpcsData");
		if (data == null) {
			player.registerExtendedProperties("CustomNpcsData", data = new PlayerData());
			data.player = player;
			data.loadNBTData(null);
		}
		data.player = player;
		return data;
	}

	public List<PlayerData> getPlayersData(final ICommandSender sender, final String username) {
		final ArrayList<PlayerData> list = new ArrayList<PlayerData>();
		final List<EntityPlayerMP> players = PlayerSelector.matchEntities(sender, username,
				(Class) EntityPlayerMP.class);
		if (players.isEmpty()) {
			final PlayerData data = PlayerDataController.instance.getDataFromUsername(username);
			if (data != null) {
				list.add(data);
			}
		} else {
			for (final EntityPlayer player : players) {
				list.add(PlayerDataController.instance.getPlayerData(player));
			}
		}
		return list;
	}

	public File getSaveDir() {
		try {
			final File file = new File(CustomNpcs.getWorldSaveDirectory(), "playerdata");
			if (!file.exists()) {
				file.mkdir();
			}
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, NBTTagCompound> getUsernameData() {
		final Map<String, NBTTagCompound> map = new HashMap<String, NBTTagCompound>();
		for (final File file : getSaveDir().listFiles()) {
			if (!file.isDirectory()) {
				if (file.getName().endsWith(".json")) {
					try {
						final NBTTagCompound compound = NBTJsonUtil.LoadFile(file);
						if (compound.hasKey("PlayerName")) {
							map.put(compound.getString("PlayerName"), compound);
						}
					} catch (Exception e) {
						LogWriter.error("Error loading: " + file.getAbsolutePath(), e);
					}
				}
			}
		}
		return map;
	}

	public boolean hasMail(final EntityPlayer player) {
		return getPlayerData(player).mailData.hasMail();
	}

	public String hasPlayer(final String username) {
		for (final String name : getUsernameData().keySet()) {
			if (name.equalsIgnoreCase(username)) {
				return name;
			}
		}
		return "";
	}

	public NBTTagCompound loadPlayerData(final String player) {
		final File saveDir = getSaveDir();
		String filename = player;
		if (filename.isEmpty()) {
			filename = "noplayername";
		}
		filename += ".json";
		File file = null;
		try {
			file = new File(saveDir, filename);
			if (file.exists()) {
				return NBTJsonUtil.LoadFile(file);
			}
		} catch (Exception e) {
			LogWriter.error("Error loading: " + file.getAbsolutePath(), e);
		}
		return new NBTTagCompound();
	}

	public NBTTagCompound loadPlayerDataOld(final String player) {
		final File saveDir = getSaveDir();
		String filename = player;
		if (filename.isEmpty()) {
			filename = "noplayername";
		}
		filename += ".dat";
		try {
			File file = new File(saveDir, filename);
			if (file.exists()) {
				final NBTTagCompound comp = CompressedStreamTools.readCompressed(new FileInputStream(file));
				file.delete();
				file = new File(saveDir, filename + "_old");
				if (file.exists()) {
					file.delete();
				}
				return comp;
			}
		} catch (Exception e) {
			LogWriter.except(e);
		}
		try {
			final File file = new File(saveDir, filename + "_old");
			if (file.exists()) {
				return CompressedStreamTools.readCompressed(new FileInputStream(file));
			}
		} catch (Exception e) {
			LogWriter.except(e);
		}
		return new NBTTagCompound();
	}

	public void savePlayerData(final PlayerData data) {
		final NBTTagCompound compound = data.getNBT();
		final String filename = data.uuid + ".json";
		try {
			final File saveDir = getSaveDir();
			final File file = new File(saveDir, filename + "_new");
			final File file2 = new File(saveDir, filename);
			NBTJsonUtil.SaveFile(file, compound);
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
		} catch (Exception e) {
			LogWriter.except(e);
		}
	}
}
