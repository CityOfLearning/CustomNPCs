
package noppes.npcs.controllers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import noppes.npcs.controllers.bank.PlayerBankData;
import noppes.npcs.controllers.dialog.PlayerDialogData;
import noppes.npcs.controllers.faction.PlayerFactionData;
import noppes.npcs.controllers.mail.PlayerMailData;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.transport.PlayerTransportData;
import noppes.npcs.entity.EntityNPCInterface;

public class PlayerData implements IExtendedEntityProperties {
	public PlayerDialogData dialogData;
	public PlayerBankData bankData;
	public PlayerQuestData questData;
	public PlayerTransportData transportData;
	public PlayerFactionData factionData;
	public PlayerItemGiverData itemgiverData;
	public PlayerMailData mailData;
	public EntityNPCInterface editingNpc;
	public NBTTagCompound cloned;
	public EntityPlayer player;
	public String playername;
	public String uuid;

	public PlayerData() {
		dialogData = new PlayerDialogData();
		bankData = new PlayerBankData();
		questData = new PlayerQuestData();
		transportData = new PlayerTransportData();
		factionData = new PlayerFactionData();
		itemgiverData = new PlayerItemGiverData();
		mailData = new PlayerMailData();
		playername = "";
		uuid = "";
	}

	public NBTTagCompound getNBT() {
		if (player != null) {
			playername = player.getName();
			uuid = player.getPersistentID().toString();
		}
		NBTTagCompound compound = new NBTTagCompound();
		dialogData.saveNBTData(compound);
		bankData.saveNBTData(compound);
		questData.saveNBTData(compound);
		transportData.saveNBTData(compound);
		factionData.saveNBTData(compound);
		itemgiverData.saveNBTData(compound);
		mailData.saveNBTData(compound);
		compound.setString("PlayerName", playername);
		compound.setString("UUID", uuid);
		return compound;
	}

	@Override
	public void init(Entity entity, World world) {
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound data = PlayerDataController.instance.loadPlayerData(player.getPersistentID().toString());
		if (data.hasNoTags()) {
			data = PlayerDataController.instance.loadPlayerDataOld(player.getName());
		}
		setNBT(data);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		PlayerDataController.instance.savePlayerData(this);
	}

	public void setNBT(NBTTagCompound data) {
		dialogData.loadNBTData(data);
		bankData.loadNBTData(data);
		questData.loadNBTData(data);
		transportData.loadNBTData(data);
		factionData.loadNBTData(data);
		itemgiverData.loadNBTData(data);
		mailData.loadNBTData(data);
		if (player != null) {
			playername = player.getName();
			uuid = player.getPersistentID().toString();
		} else {
			playername = data.getString("PlayerName");
			uuid = data.getString("UUID");
		}
	}
}
