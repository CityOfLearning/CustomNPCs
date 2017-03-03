
package noppes.npcs.client.gui.player;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.faction.Faction;
import noppes.npcs.controllers.faction.PlayerFactionData;
import noppes.npcs.util.NoppesUtilPlayer;
import tconstruct.client.tabs.InventoryTabFactions;
import tconstruct.client.tabs.TabRegistry;

public class GuiFaction extends GuiNPCInterface implements IGuiData {
	private int xSize;
	private int ySize;
	private int guiLeft;
	private int guiTop;
	private ArrayList<Faction> playerFactions;
	private int page;
	private int pages;
	private GuiButtonNextPage buttonNextPage;
	private GuiButtonNextPage buttonPreviousPage;
	private ResourceLocation indicator;

	public GuiFaction() {
		playerFactions = new ArrayList<>();
		page = 0;
		pages = 1;
		xSize = 200;
		ySize = 195;
		drawDefaultBackground = false;
		title = "";
		NoppesUtilPlayer.sendData(EnumPlayerPacket.FactionsGet, new Object[0]);
		indicator = getResource("standardbg.png");
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!(guibutton instanceof GuiButtonNextPage)) {
			return;
		}
		int id = guibutton.id;
		if (id == 1) {
			++page;
		}
		if (id == 2) {
			--page;
		}
		updateButtons();
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(indicator);
		this.drawTexturedModalRect(guiLeft, guiTop + 8, 0, 0, xSize, ySize);
		this.drawTexturedModalRect(guiLeft + 4, guiTop + 8, 56, 0, 200, ySize);
		if (playerFactions.isEmpty()) {
			String noFaction = StatCollector.translateToLocal("faction.nostanding");
			fontRendererObj.drawString(noFaction, guiLeft + ((xSize - fontRendererObj.getStringWidth(noFaction)) / 2),
					guiTop + 80, CustomNpcResourceListener.DefaultTextColor);
		} else {
			renderScreen();
		}
		super.drawScreen(i, j, f);
	}

	@Override
	public void initGui() {
		super.initGui();
		guiLeft = (width - xSize) / 2;
		guiTop = ((height - ySize) / 2) + 12;
		TabRegistry.updateTabValues(guiLeft, guiTop + 8, InventoryTabFactions.class);
		TabRegistry.addTabsToList(buttonList);
		buttonList.add(buttonNextPage = new GuiButtonNextPage(1, (guiLeft + xSize) - 43, guiTop + 180, true));
		buttonList.add(buttonPreviousPage = new GuiButtonNextPage(2, guiLeft + 20, guiTop + 180, false));
		updateButtons();
	}

	@Override
	public void keyTyped(char c, int i) {
		if ((i == 1) || isInventoryKey(i)) {
			close();
		}
	}

	private void renderScreen() {
		int size = 5;
		if (pages == 1) {
			size = playerFactions.size();
		}
		if (page == pages) {
			size = playerFactions.size() % 5;
		}
		for (int id = 0; id < size; ++id) {
			drawHorizontalLine(guiLeft + 2, guiLeft + xSize, guiTop + 14 + (id * 30),
					-16777216 + CustomNpcResourceListener.DefaultTextColor);
			Faction faction = playerFactions.get(((page - 1) * 5) + id);
			String name = faction.name;
			String points = " : " + faction.defaultPoints;
			String standing = StatCollector.translateToLocal("faction.friendly");
			int color = 65280;
			if (faction.defaultPoints < faction.neutralPoints) {
				standing = StatCollector.translateToLocal("faction.unfriendly");
				color = 16711680;
				points = points + "/" + faction.neutralPoints;
			} else if (faction.defaultPoints < faction.friendlyPoints) {
				standing = StatCollector.translateToLocal("faction.neutral");
				color = 15924992;
				points = points + "/" + faction.friendlyPoints;
			} else {
				points += "/-";
			}
			fontRendererObj.drawString(name, guiLeft + ((xSize - fontRendererObj.getStringWidth(name)) / 2),
					guiTop + 19 + (id * 30), faction.color);
			fontRendererObj.drawString(standing, (width / 2) - fontRendererObj.getStringWidth(standing) - 1,
					guiTop + 33 + (id * 30), color);
			fontRendererObj.drawString(points, width / 2, guiTop + 33 + (id * 30),
					CustomNpcResourceListener.DefaultTextColor);
		}
		drawHorizontalLine(guiLeft + 2, guiLeft + xSize, guiTop + 14 + (size * 30),
				-16777216 + CustomNpcResourceListener.DefaultTextColor);
		if (pages > 1) {
			String s = page + "/" + pages;
			fontRendererObj.drawString(s, guiLeft + ((xSize - fontRendererObj.getStringWidth(s)) / 2), guiTop + 203,
					CustomNpcResourceListener.DefaultTextColor);
		}
	}

	@Override
	public void save() {
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		playerFactions = new ArrayList<>();
		NBTTagList list = compound.getTagList("FactionList", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			Faction faction = new Faction();
			faction.readNBT(list.getCompoundTagAt(i));
			playerFactions.add(faction);
		}
		PlayerFactionData data = new PlayerFactionData();
		data.loadNBTData(compound);
		for (int id : data.factionData.keySet()) {
			int points = data.factionData.get(id);
			for (Faction faction2 : playerFactions) {
				if (faction2.id == id) {
					faction2.defaultPoints = points;
				}
			}
		}
		pages = (playerFactions.size() - 1) / 5;
		++pages;
		page = 1;
		updateButtons();
	}

	private void updateButtons() {
		buttonNextPage.setVisible(page < pages);
		buttonPreviousPage.setVisible(page > 1);
	}
}
