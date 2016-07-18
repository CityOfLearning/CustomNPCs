//

//

package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.PlayerMail;
import noppes.npcs.controllers.PlayerMailData;

public class GuiMailbox extends GuiNPCInterface implements IGuiData, ICustomScrollListener, GuiYesNoCallback {
	private GuiCustomScroll scroll;
	private PlayerMailData data;
	private PlayerMail selected;

	public GuiMailbox() {
		xSize = 256;
		setBackground("menubg.png");
		NoppesUtilPlayer.sendData(EnumPlayerPacket.MailGet, new Object[0]);
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		final int id = guibutton.id;
		if (scroll.selected < 0) {
			return;
		}
		if (id == 0) {
			GuiMailmanWrite.parent = this;
			GuiMailmanWrite.mail = selected;
			NoppesUtilPlayer.sendData(EnumPlayerPacket.MailboxOpenMail, selected.time, selected.sender);
			selected = null;
			scroll.selected = -1;
		}
		if (id == 1) {
			final GuiYesNo guiyesno = new GuiYesNo(this, "Confirm", StatCollector.translateToLocal("gui.delete"), 0);
			displayGuiScreen(guiyesno);
		}
	}

	@Override
	public void confirmClicked(final boolean flag, final int i) {
		if (flag && (selected != null)) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.MailDelete, selected.time, selected.sender);
			selected = null;
		}
		NoppesUtil.openGUI(player, this);
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll guiCustomScroll) {
		selected = data.playermail.get(guiCustomScroll.selected);
		initGui();
		if ((selected != null) && !selected.beenRead) {
			selected.beenRead = true;
			NoppesUtilPlayer.sendData(EnumPlayerPacket.MailRead, selected.time, selected.sender);
		}
	}

	private String getTimePast() {
		if (selected.timePast > 86400000L) {
			final int days = (int) (selected.timePast / 86400000L);
			if (days == 1) {
				return days + " " + StatCollector.translateToLocal("mailbox.day");
			}
			return days + " " + StatCollector.translateToLocal("mailbox.days");
		} else if (selected.timePast > 3600000L) {
			final int hours = (int) (selected.timePast / 3600000L);
			if (hours == 1) {
				return hours + " " + StatCollector.translateToLocal("mailbox.hour");
			}
			return hours + " " + StatCollector.translateToLocal("mailbox.hours");
		} else {
			final int minutes = (int) (selected.timePast / 60000L);
			if (minutes == 1) {
				return minutes + " " + StatCollector.translateToLocal("mailbox.minutes");
			}
			return minutes + " " + StatCollector.translateToLocal("mailbox.minutes");
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setSize(165, 186);
		}
		scroll.guiLeft = guiLeft + 4;
		scroll.guiTop = guiTop + 4;
		addScroll(scroll);
		final String title = StatCollector.translateToLocal("mailbox.name");
		final int x = (xSize - fontRendererObj.getStringWidth(title)) / 2;
		addLabel(new GuiNpcLabel(0, title, guiLeft + x, guiTop - 8));
		if (selected != null) {
			addLabel(new GuiNpcLabel(3, StatCollector.translateToLocal("mailbox.sender") + ":", guiLeft + 170,
					guiTop + 6));
			addLabel(new GuiNpcLabel(1, selected.sender, guiLeft + 174, guiTop + 18));
			addLabel(new GuiNpcLabel(2,
					StatCollector.translateToLocalFormatted("mailbox.timesend", new Object[] { getTimePast() }),
					guiLeft + 174, guiTop + 30));
		}
		addButton(new GuiNpcButton(0, guiLeft + 4, guiTop + 192, 82, 20, "mailbox.read"));
		addButton(new GuiNpcButton(1, guiLeft + 88, guiTop + 192, 82, 20, "selectWorld.deleteButton"));
		getButton(1).setEnabled(selected != null);
	}

	@Override
	public void keyTyped(final char c, final int i) {
		if ((i == 1) || isInventoryKey(i)) {
			close();
		}
	}

	@Override
	public void mouseClicked(final int i, final int j, final int k) {
		super.mouseClicked(i, j, k);
		scroll.mouseClicked(i, j, k);
	}

	@Override
	public void save() {
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		final PlayerMailData data = new PlayerMailData();
		data.loadNBTData(compound);
		final List<String> list = new ArrayList<String>();
		Collections.sort(data.playermail, new Comparator<PlayerMail>() {
			@Override
			public int compare(final PlayerMail o1, final PlayerMail o2) {
				if (o1.time == o2.time) {
					return 0;
				}
				return (o1.time > o2.time) ? -1 : 1;
			}
		});
		for (final PlayerMail mail : data.playermail) {
			list.add(mail.subject);
		}
		this.data = data;
		scroll.clear();
		selected = null;
		scroll.setUnsortedList(list);
	}
}
