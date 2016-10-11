package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.List;

import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.display.ChatBox;
import com.rabbit.gui.component.display.entity.DisplayEntity;
import com.rabbit.gui.component.display.entity.EntityComponent;
import com.rabbit.gui.component.list.ScrollableDisplayList;
import com.rabbit.gui.component.list.entries.ButtonEntry;
import com.rabbit.gui.component.list.entries.ListEntry;
import com.rabbit.gui.show.Show;
import com.rabbit.gui.utils.SkinManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiDialogInteract extends Show {
	private Dialog dialog;
	public EntityNPCInterface npc;
	public EntityPlayerSP player;
	private List<String> lines;
	private ScrollableDisplayList optionsList;
	private ChatBox npcDialog;

	private DisplayEntity pEntity;
	private DisplayEntity nEntity;
	private ResourceLocation playerSkin;

	public GuiDialogInteract(EntityNPCInterface npc, Dialog dialog) {
		setBackground(new DefaultBackground());
		lines = new ArrayList<String>();
		this.npc = npc;
		this.dialog = dialog;
		appendDialog(this.dialog);
		player = Minecraft.getMinecraft().thePlayer;
		pEntity = new DisplayEntity(Minecraft.getMinecraft().theWorld);
		String texture = SkinManager.getSkinTexture(Minecraft.getMinecraft().thePlayer);
		playerSkin = Minecraft.getMinecraft().getNetHandler()
				.getPlayerInfo(Minecraft.getMinecraft().thePlayer.getName()).getLocationSkin();
		if (texture != null) {
			pEntity.setTexture(new ResourceLocation(texture));
		} else {
			pEntity.setTexture(playerSkin);
		}

		nEntity = new DisplayEntity(Minecraft.getMinecraft().theWorld);
		nEntity.setTexture(npc.textureLocation);
	}

	public void appendDialog(Dialog dialog) {
		this.dialog = dialog;
		if ((dialog.sound != null) && !dialog.sound.isEmpty()) {
			MusicController.Instance.stopMusic();
			MusicController.Instance.playSound(dialog.sound, (float) npc.posX, (float) npc.posY, (float) npc.posZ);
		}
		lines.add(dialog.text);
		if (npcDialog != null) {
			String text = "";
			for (String line : lines) {
				text += NoppesStringUtils.formatText(line, new Object[] { player, npc }) + "\n";
			}
			npcDialog.setText(text);
		}

		if (optionsList != null) {
			optionsList.clear();
			for (int slot : dialog.options.keySet()) {
				DialogOption option = dialog.options.get(slot);
				if (option != null) {
					if (option.optionType == EnumOptionType.DISABLED) {
						continue;
					}
					optionsList.add(new ButtonEntry(dialog.options.get(slot).title, btn -> {
						handleDialogSelection(slot);
					}));
				}
			}
		}
	}

	private void handleDialogSelection(int optionId) {
		NoppesUtilPlayer.sendData(EnumPlayerPacket.Dialog, dialog.id, optionId);
		if ((dialog == null) || !dialog.hasOtherOptions() || dialog.options.isEmpty()) {
			getStage().close();
			return;
		}
		DialogOption option = dialog.options.get(optionId);
		if ((option == null) || (option.optionType == EnumOptionType.QUIT_OPTION)
				|| (option.optionType == EnumOptionType.DISABLED)) {
			getStage().close();
			return;
		}
		lines.clear();
		NoppesUtil.clickSound();
	}

	@Override
	public void onClose() {
		pEntity.setDead();
		nEntity.setDead();
		NoppesUtilPlayer.sendData(EnumPlayerPacket.CheckQuestCompletion, new Object[0]);
		super.onClose();
	}

	@Override
	public void setup() {
		super.setup();

		String text = "";
		for (String line : lines) {
			text += NoppesStringUtils.formatText(line, new Object[] { player, npc }) + "\n";
		}

		npcDialog = new ChatBox(width / 4 + 10, (int) (height * .05) + 10, (int) ((width * (5.0 / 7.0)) - 20),
				(int) (height * .5) - 20, text);
		registerComponent(npcDialog);

		// components
		ArrayList<ListEntry> dOptions = new ArrayList<ListEntry>();

		for (int slot : dialog.options.keySet()) {
			DialogOption option = dialog.options.get(slot);
			if (option != null) {
				if (option.optionType == EnumOptionType.DISABLED) {
					continue;
				}
				dOptions.add(new ButtonEntry(dialog.options.get(slot).title, btn -> {
					handleDialogSelection(slot);
				}));
			}
		}

		optionsList = (ScrollableDisplayList) new ScrollableDisplayList((int) (width * 0.075),
				(int) (height * .575) + 10, (int) (width * (5.0 / 9.0)), (int) (height * .375), 20, dOptions)
						.setVisibleBackground(false);
		optionsList.setId("dialogOptions");
		registerComponent(optionsList);

		registerComponent(new EntityComponent((int) (width * .15), (int) (height * .5), (int) (width * .2),
				(int) (height * .2), nEntity, 0, 1.5f, false));

		registerComponent(new EntityComponent((int) (width * .8), (int) (height * .95), (int) (width * .2),
				(int) (height * .2), pEntity, 0, 1.5f, false));

	}
}