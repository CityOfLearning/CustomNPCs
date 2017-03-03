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
import noppes.npcs.controllers.bank.Bank;
import noppes.npcs.controllers.bank.BankController;
import noppes.npcs.controllers.bank.PlayerBankData;
import noppes.npcs.controllers.mail.PlayerMail;
import noppes.npcs.util.NBTJsonUtil;

public class PlayerDataController {
	public static PlayerDataController instance;

	public PlayerDataController() {
		PlayerDataController.instance = this;
	}

	public void addPlayerMessage(String username, PlayerMail mail) {
		mail.time = System.currentTimeMillis();
		MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
		PlayerData data = getDataFromUsername(username);
		data.mailData.playermail.add(mail.copy());
		savePlayerData(data);
	}

	public PlayerBankData getBankData(EntityPlayer player, int bankId) {
		Bank bank = BankController.getInstance().getBank(bankId);
		PlayerBankData data = getPlayerData(player).bankData;
		if (!data.hasBank(bank.id)) {
			data.loadNew(bank.id);
		}
		return data;
	}

	public PlayerData getDataFromUsername(String username) {
		EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
		PlayerData data = null;
		if (player == null) {
			Map<String, NBTTagCompound> map = getUsernameData();
			for (String name : map.keySet()) {
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

	public PlayerData getPlayerData(EntityPlayer player) {
		PlayerData data = (PlayerData) player.getExtendedProperties("CustomNpcsData");
		if (data == null) {
			player.registerExtendedProperties("CustomNpcsData", data = new PlayerData());
			data.player = player;
			data.loadNBTData(null);
		}
		data.player = player;
		return data;
	}

	public List<PlayerData> getPlayersData(ICommandSender sender, String username) {
		ArrayList<PlayerData> list = new ArrayList<>();
		List<EntityPlayerMP> players = PlayerSelector.matchEntities(sender, username, EntityPlayerMP.class);
		if (players.isEmpty()) {
			PlayerData data = PlayerDataController.instance.getDataFromUsername(username);
			if (data != null) {
				list.add(data);
			}
		} else {
			for (EntityPlayer player : players) {
				list.add(PlayerDataController.instance.getPlayerData(player));
			}
		}
		return list;
	}

	public File getSaveDir() {
		try {
			File file = new File(CustomNpcs.getWorldSaveDirectory(), "playerdata");
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
		Map<String, NBTTagCompound> map = new HashMap<>();
		for (File file : getSaveDir().listFiles()) {
			if (!file.isDirectory()) {
				if (file.getName().endsWith(".json")) {
					try {
						NBTTagCompound compound = NBTJsonUtil.LoadFile(file);
						if (compound.hasKey("PlayerName")) {
							map.put(compound.getString("PlayerName"), compound);
						}
					} catch (Exception e) {
						CustomNpcs.logger.error("Error loading: " + file.getAbsolutePath(), e);
					}
				}
			}
		}
		return map;
	}

	public boolean hasMail(EntityPlayer player) {
		return getPlayerData(player).mailData.hasMail();
	}

	public String hasPlayer(String username) {
		for (String name : getUsernameData().keySet()) {
			if (name.equalsIgnoreCase(username)) {
				return name;
			}
		}
		return "";
	}

	public NBTTagCompound loadPlayerData(String player) {
		File saveDir = getSaveDir();
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
			CustomNpcs.logger.error("Error loading: " + file.getAbsolutePath(), e);
		}
		return new NBTTagCompound();
	}

	public NBTTagCompound loadPlayerDataOld(String player) {
		File saveDir = getSaveDir();
		String filename = player;
		if (filename.isEmpty()) {
			filename = "noplayername";
		}
		filename += ".dat";
		try {
			File file = new File(saveDir, filename);
			if (file.exists()) {
				NBTTagCompound comp = CompressedStreamTools.readCompressed(new FileInputStream(file));
				file.delete();
				file = new File(saveDir, filename + "_old");
				if (file.exists()) {
					file.delete();
				}
				return comp;
			}
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
		}
		try {
			File file = new File(saveDir, filename + "_old");
			if (file.exists()) {
				return CompressedStreamTools.readCompressed(new FileInputStream(file));
			}
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
		}
		return new NBTTagCompound();
	}

	public void savePlayerData(PlayerData data) {
		NBTTagCompound compound = data.getNBT();
		String filename = data.uuid + ".json";
		try {
			File saveDir = getSaveDir();
			File file = new File(saveDir, filename + "_new");
			File file2 = new File(saveDir, filename);
			NBTJsonUtil.SaveFile(file, compound);
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
		} catch (Exception e) {
			CustomNpcs.logger.catching(e);
		}
	}
}
