
package tconstruct.client.tabs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TabRegistry {
	private static ArrayList<AbstractTab> tabList;
	private static Minecraft mc;

	static {
		TabRegistry.tabList = new ArrayList<>();
		TabRegistry.mc = FMLClientHandler.instance().getClient();
	}

	public static void addTabsToList(List buttonList) {
		for (AbstractTab tab : TabRegistry.tabList) {
			if (tab.shouldAddToList()) {
				buttonList.add(tab);
			}
		}
	}

	public static int getPotionOffset() {
		if (!TabRegistry.mc.thePlayer.getActivePotionEffects().isEmpty()) {
			if (!Loader.isModLoaded("NotEnoughItems")) {
				return 60;
			}
			try {
				Class<?> c = Class.forName("codechicken.nei.NEIClientConfig");
				Object hidden = c.getMethod("isHidden", new Class[0]).invoke(null, new Object[0]);
				Object enabled = c.getMethod("isEnabled", new Class[0]).invoke(null, new Object[0]);
				if ((hidden != null) && (hidden instanceof Boolean) && (enabled != null) && (enabled instanceof Boolean)
						&& ((Boolean) hidden || !(Boolean) enabled)) {
					return 60;
				}
			} catch (Exception e) {
			}
		}
		return 0;
	}

	public static ArrayList<AbstractTab> getTabList() {
		return TabRegistry.tabList;
	}

	public static void openInventoryGui() {
		TabRegistry.mc.thePlayer.sendQueue
				.addToSendQueue(new C0DPacketCloseWindow(TabRegistry.mc.thePlayer.openContainer.windowId));
		GuiInventory inventory = new GuiInventory(TabRegistry.mc.thePlayer);
		TabRegistry.mc.displayGuiScreen(inventory);
	}

	public static void registerTab(AbstractTab tab) {
		TabRegistry.tabList.add(tab);
	}

	public static void updateTabValues(int cornerX, int cornerY, Class<?> selectedButton) {
		int count = 2;
		for (int i = 0; i < TabRegistry.tabList.size(); ++i) {
			AbstractTab t = TabRegistry.tabList.get(i);
			if (t.shouldAddToList()) {
				t.id = count;
				t.xPosition = cornerX + ((count - 2) * 28);
				t.yPosition = cornerY - 28;
				t.enabled = !t.getClass().equals(selectedButton);
				++count;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.gui instanceof GuiInventory) {
			int xSize = 176;
			int ySize = 166;
			int guiLeft = (event.gui.width - xSize) / 2;
			int guiTop = (event.gui.height - ySize) / 2;
			guiLeft += getPotionOffset();
			updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
			addTabsToList(event.buttonList);
		}
	}
}
