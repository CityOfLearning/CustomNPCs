//

//

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.controllers.Bank;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCManageBanks extends GuiContainerNPCInterface2
		implements IScrollData, ICustomScrollListener, ITextfieldListener, IGuiData {
	private GuiCustomScroll scroll;
	private HashMap<String, Integer> data;
	private ContainerManageBanks container;
	private Bank bank;
	private String selected;

	public GuiNPCManageBanks(final EntityNPCInterface npc, final ContainerManageBanks container) {
		super(npc, container);
		data = new HashMap<String, Integer>();
		bank = new Bank();
		selected = null;
		this.container = container;
		drawDefaultBackground = false;
		setBackground("npcbanksetup.png");
		ySize = 200;
	}

	@Override
	protected void actionPerformed(final GuiButton guibutton) {
		final GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 6) {
			save();
			scroll.clear();
			String name;
			for (name = "New"; data.containsKey(name); name += "_") {
			}
			final Bank bank = new Bank();
			bank.name = name;
			final NBTTagCompound compound = new NBTTagCompound();
			bank.writeEntityToNBT(compound);
			Client.sendData(EnumPacketServer.BankSave, compound);
		} else if (button.id == 7) {
			if (data.containsKey(scroll.getSelected())) {
				Client.sendData(EnumPacketServer.BankRemove, data.get(selected));
			}
		} else if ((button.id >= 0) && (button.id < 6)) {
			bank.slotTypes.put(button.id, button.getValue());
		}
	}

	@Override
	public void customScrollClicked(final int i, final int j, final int k, final GuiCustomScroll guiCustomScroll) {
		if (guiCustomScroll.id == 0) {
			save();
			selected = scroll.getSelected();
			Client.sendData(EnumPacketServer.BankGet, data.get(selected));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		fontRendererObj.drawString("Tab Cost", 23, 28, CustomNpcResourceListener.DefaultTextColor);
		fontRendererObj.drawString("Upg. Cost", 123, 28, CustomNpcResourceListener.DefaultTextColor);
		fontRendererObj.drawString("Start", 6, 70, CustomNpcResourceListener.DefaultTextColor);
		fontRendererObj.drawString("Max", 9, 100, CustomNpcResourceListener.DefaultTextColor);
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(6, guiLeft + 340, guiTop + 10, 45, 20, "gui.add"));
		addButton(new GuiNpcButton(7, guiLeft + 340, guiTop + 32, 45, 20, "gui.remove"));
		if (scroll == null) {
			scroll = new GuiCustomScroll(this, 0);
		}
		scroll.setSize(160, 180);
		scroll.guiLeft = guiLeft + 174;
		scroll.guiTop = guiTop + 8;
		addScroll(scroll);
		for (int i = 0; i < 6; ++i) {
			final int x = guiLeft + 6;
			final int y = guiTop + 36 + (i * 22);
			addButton(new GuiNpcButton(i, x + 50, y, 80, 20,
					new String[] { "Can Upgrade", "Can't Upgrade", "Upgraded" }, 0));
			getButton(i).setEnabled(false);
		}
		addTextField(new GuiNpcTextField(0, this, fontRendererObj, guiLeft + 8, guiTop + 8, 160, 16, ""));
		getTextField(0).setMaxStringLength(20);
		addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 10, guiTop + 80, 16, 16, ""));
		getTextField(1).numbersOnly = true;
		getTextField(1).setMaxStringLength(1);
		addTextField(new GuiNpcTextField(2, this, fontRendererObj, guiLeft + 10, guiTop + 110, 16, 16, ""));
		getTextField(2).numbersOnly = true;
		getTextField(2).setMaxStringLength(1);
	}

	@Override
	public void initPacket() {
		Client.sendData(EnumPacketServer.BanksGet, new Object[0]);
	}

	@Override
	public void save() {
		if ((selected != null) && data.containsKey(selected) && (bank != null)) {
			final NBTTagCompound compound = new NBTTagCompound();
			bank.currencyInventory = container.bank.currencyInventory;
			bank.upgradeInventory = container.bank.upgradeInventory;
			bank.writeEntityToNBT(compound);
			Client.sendData(EnumPacketServer.BankSave, compound);
		}
	}

	@Override
	public void setData(final Vector<String> list, final HashMap<String, Integer> data) {
		final String name = scroll.getSelected();
		this.data = data;
		scroll.setList(list);
		if (name != null) {
			scroll.setSelected(name);
		}
	}

	@Override
	public void setGuiData(final NBTTagCompound compound) {
		final Bank bank = new Bank();
		bank.readEntityFromNBT(compound);
		this.bank = bank;
		if (bank.id == -1) {
			getTextField(0).setText("");
			getTextField(1).setText("");
			getTextField(2).setText("");
			for (int i = 0; i < 6; ++i) {
				getButton(i).setDisplay(0);
				getButton(i).setEnabled(false);
			}
		} else {
			getTextField(0).setText(bank.name);
			getTextField(1).setText(Integer.toString(bank.startSlots));
			getTextField(2).setText(Integer.toString(bank.maxSlots));
			for (int i = 0; i < 6; ++i) {
				int type = 0;
				if (bank.slotTypes.containsKey(i)) {
					type = bank.slotTypes.get(i);
				}
				getButton(i).setDisplay(type);
				getButton(i).setEnabled(true);
			}
		}
		setSelected(bank.name);
	}

	@Override
	public void setSelected(final String selected) {
		this.selected = selected;
		scroll.setSelected(selected);
	}

	@Override
	public void unFocused(final GuiNpcTextField guiNpcTextField) {
		if (bank.id != -1) {
			if (guiNpcTextField.id == 0) {
				final String name = guiNpcTextField.getText();
				if (!name.isEmpty() && !data.containsKey(name)) {
					final String old = bank.name;
					data.remove(bank.name);
					bank.name = name;
					data.put(bank.name, bank.id);
					selected = name;
					scroll.replace(old, bank.name);
				}
			} else if ((guiNpcTextField.id == 1) || (guiNpcTextField.id == 2)) {
				int num = 1;
				if (!guiNpcTextField.isEmpty()) {
					num = guiNpcTextField.getInteger();
				}
				if (num > 6) {
					num = 6;
				}
				if (num < 0) {
					num = 0;
				}
				if (guiNpcTextField.id == 1) {
					bank.startSlots = num;
				} else if (guiNpcTextField.id == 2) {
					bank.maxSlots = num;
				}
				if (bank.startSlots > bank.maxSlots) {
					bank.maxSlots = bank.startSlots;
				}
				guiNpcTextField.setText(Integer.toString(num));
			}
		}
	}
}
