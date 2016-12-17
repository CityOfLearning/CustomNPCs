package noppes.npcs.packets;

import java.util.Iterator;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.Server;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileBook;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.containers.ContainerTradingBlock;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.bank.BankData;
import noppes.npcs.controllers.faction.PlayerFactionData;
import noppes.npcs.controllers.mail.PlayerMail;
import noppes.npcs.controllers.mail.PlayerMailData;
import noppes.npcs.controllers.quest.PlayerQuestController;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.util.NoppesUtilPlayer;
import noppes.npcs.util.NoppesUtilServer;

public class PacketHandlerPlayer {
	@SubscribeEvent
	public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
		EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
		ByteBuf buffer = event.packet.payload();
		MinecraftServer.getServer().addScheduledTask(() -> {
			EnumPlayerPacket type = null;
			try {
				type = EnumPlayerPacket.values()[buffer.readInt()];
				PacketHandlerPlayer.this.player(buffer, player, type);
			} catch (Exception e) {
				CustomNpcs.logger.error("Error with EnumPlayerPacket." + type, e);
			}
		});
	}

	private void player(ByteBuf buffer, EntityPlayerMP player, EnumPlayerPacket type) throws Exception {
		if (type == EnumPlayerPacket.CompanionTalentExp) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 6) || (player != npc.getOwner())) {
				return;
			}
			int id = buffer.readInt();
			int exp = buffer.readInt();
			RoleCompanion role = (RoleCompanion) npc.roleInterface;
			if ((exp <= 0) || !role.canAddExp(-exp) || (id < 0) || (id >= EnumCompanionTalent.values().length)) {
				return;
			}
			EnumCompanionTalent talent = EnumCompanionTalent.values()[id];
			role.addExp(-exp);
			role.addTalentExp(talent, exp);
		} else if (type == EnumPlayerPacket.CompanionOpenInv) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 6) || (player != npc.getOwner())) {
				return;
			}
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.CompanionInv, npc);
		} else if (type == EnumPlayerPacket.TradeAccept) {
			if (!(player.openContainer instanceof ContainerTradingBlock)) {
				return;
			}
			ContainerTradingBlock con = (ContainerTradingBlock) player.openContainer;
			if (!con.tile.isFull()) {
				return;
			}
			ContainerTradingBlock con2 = (ContainerTradingBlock) con.tile.other(player).openContainer;
			if (con.state == 0) {
				con.setState(2);
				con2.setState(1);
			} else if ((con.state == 1) || (con.state == 2)) {
				con.setState(3);
				con2.setState(3);
				for (int i = 0; i < 9; ++i) {
					ItemStack item = con.craftMatrix.getStackInSlot(i);
					con.craftMatrix.setInventorySlotContents(i, con2.craftMatrix.getStackInSlot(i));
					con2.craftMatrix.setInventorySlotContents(i, item);
				}
			}
		} else if (type == EnumPlayerPacket.FollowerHire) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 2)) {
				return;
			}
			NoppesUtilPlayer.hireFollower(player, npc);
		} else if (type == EnumPlayerPacket.FollowerExtend) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 2)) {
				return;
			}
			NoppesUtilPlayer.extendFollower(player, npc);
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPlayerPacket.FollowerState) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 2)) {
				return;
			}
			NoppesUtilPlayer.changeFollowerState(player, npc);
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPlayerPacket.RoleGet) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role == 0)) {
				return;
			}
			Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(new NBTTagCompound()));
		} else if (type == EnumPlayerPacket.Transport) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 4)) {
				return;
			}
			NoppesUtilPlayer.transport(player, npc, Server.readString(buffer));
		} else if (type == EnumPlayerPacket.BankUpgrade) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 3)) {
				return;
			}
			NoppesUtilPlayer.bankUpgrade(player, npc);
		} else if (type == EnumPlayerPacket.BankUnlock) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 3)) {
				return;
			}
			NoppesUtilPlayer.bankUnlock(player, npc);
		} else if (type == EnumPlayerPacket.BankSlotOpen) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if ((npc == null) || (npc.advanced.role != 3)) {
				return;
			}
			int slot = buffer.readInt();
			int bankId = buffer.readInt();
			BankData data = PlayerDataController.instance.getBankData(player, bankId).getBankOrDefault(bankId);
			data.openBankGui(player, npc, bankId, slot);
		} else if (type == EnumPlayerPacket.Dialog) {
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if (npc == null) {
				return;
			}
			NoppesUtilPlayer.dialogSelected(buffer.readInt(), buffer.readInt(), player, npc);
		} else if (type == EnumPlayerPacket.CheckQuestCompletion) {
			PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
			playerdata.checkQuestCompletion(player, null);
		} else if (type == EnumPlayerPacket.QuestLog) {
			NoppesUtilPlayer.sendQuestLogData(player);
		} else if (type == EnumPlayerPacket.QuestCompletion) {
			NoppesUtilPlayer.questCompletion(player, buffer.readInt());
		} else if (type == EnumPlayerPacket.FactionsGet) {
			PlayerFactionData data2 = PlayerDataController.instance.getPlayerData(player).factionData;
			Server.sendData(player, EnumPacketClient.GUI_DATA, data2.getPlayerGuiData());
		} else if (type == EnumPlayerPacket.MailGet) {
			PlayerMailData data3 = PlayerDataController.instance.getPlayerData(player).mailData;
			Server.sendData(player, EnumPacketClient.GUI_DATA, data3.saveNBTData(new NBTTagCompound()));
		} else if (type == EnumPlayerPacket.MailDelete) {
			long time = buffer.readLong();
			String username = Server.readString(buffer);
			PlayerMailData data4 = PlayerDataController.instance.getPlayerData(player).mailData;
			Iterator<PlayerMail> it = data4.playermail.iterator();
			while (it.hasNext()) {
				PlayerMail mail = it.next();
				if ((mail.time == time) && mail.sender.equals(username)) {
					it.remove();
				}
			}
			Server.sendData(player, EnumPacketClient.GUI_DATA, data4.saveNBTData(new NBTTagCompound()));
		} else if (type == EnumPlayerPacket.MailSend) {
			String username2 = PlayerDataController.instance.hasPlayer(Server.readString(buffer));
			if (username2.isEmpty()) {
				NoppesUtilServer.sendGuiError(player, 0);
				return;
			}
			PlayerMail mail2 = new PlayerMail();
			String s = player.getDisplayNameString();
			if (!s.equals(player.getName())) {
				s = s + "(" + player.getName() + ")";
			}
			mail2.readNBT(Server.readNBT(buffer));
			mail2.sender = s;
			mail2.items = ((ContainerMail) player.openContainer).mail.items;
			if (mail2.subject.isEmpty()) {
				NoppesUtilServer.sendGuiError(player, 1);
				return;
			}
			NBTTagCompound comp = new NBTTagCompound();
			comp.setString("username", username2);
			NoppesUtilServer.sendGuiClose(player, 1, comp);
			EntityNPCInterface npc2 = NoppesUtilServer.getEditingNpc(player);
			if ((npc2 != null)
					&& EventHooks.onNPCRole(npc2, new RoleEvent.MailmanEvent(player, npc2.wrappedNPC, mail2))) {
				return;
			}
			PlayerDataController.instance.addPlayerMessage(username2, mail2);
		} else if (type == EnumPlayerPacket.MailboxOpenMail) {
			long time = buffer.readLong();
			String username = Server.readString(buffer);
			player.closeContainer();
			PlayerMailData data4 = PlayerDataController.instance.getPlayerData(player).mailData;
			for (PlayerMail mail : data4.playermail) {
				if ((mail.time == time) && mail.sender.equals(username)) {
					ContainerMail.staticmail = mail;
					player.openGui(CustomNpcs.instance, EnumGuiType.PlayerMailman.ordinal(), player.worldObj, 0, 0, 0);
					break;
				}
			}
		} else if (type == EnumPlayerPacket.MailRead) {
			long time = buffer.readLong();
			String username = Server.readString(buffer);
			PlayerMailData data4 = PlayerDataController.instance.getPlayerData(player).mailData;
			for (PlayerMail mail : data4.playermail) {
				if ((mail.time == time) && mail.sender.equals(username)) {
					mail.beenRead = true;
					if (!mail.hasQuest()) {
						continue;
					}
					PlayerQuestController.addActiveQuest(mail.getQuest(), player);
				}
			}
		} else if (type == EnumPlayerPacket.SignSave) {
			BlockPos pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			TileEntity tile = player.worldObj.getTileEntity(pos);
			if ((tile == null) || (!(tile instanceof TileBigSign))) {
				return;
			}
			TileBigSign sign = (TileBigSign) tile;
			if (sign.isCanEdit()) {
				sign.setText(Server.readString(buffer));
				sign.setCanEdit(false);
				player.worldObj.markBlockForUpdate(pos);
			}
		} else if (type == EnumPlayerPacket.SaveBook) {
			BlockPos pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			TileEntity tileentity = player.worldObj.getTileEntity(pos);
			if (!(tileentity instanceof TileBook)) {
				return;
			}
			TileBook tile = (TileBook) tileentity;
			if (tile.getBook().getItem() == Items.written_book) {
				return;
			}
			boolean sign = buffer.readBoolean();
			ItemStack book = ItemStack.loadItemStackFromNBT(Server.readNBT(buffer));
			if (book == null) {
				return;
			}
			if ((book.getItem() == Items.writable_book) && (!sign)
					&& (ItemWritableBook.isNBTValid(book.getTagCompound()))) {
				tile.getBook().setTagInfo("pages", book.getTagCompound().getTagList("pages", 8));
			}
			if ((book.getItem() == Items.written_book) && (sign)
					&& (ItemEditableBook.validBookTagContents(book.getTagCompound()))) {
				tile.getBook().setTagInfo("author", new NBTTagString(player.getName()));
				tile.getBook().setTagInfo("title", new NBTTagString(book.getTagCompound().getString("title")));
				tile.getBook().setTagInfo("pages", book.getTagCompound().getTagList("pages", 8));
				tile.getBook().setItem(Items.written_book);
			}
		}
	}
}
