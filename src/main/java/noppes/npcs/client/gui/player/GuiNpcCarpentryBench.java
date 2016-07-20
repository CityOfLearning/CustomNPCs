//

//

package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.containers.ContainerCarpentryBench;
import noppes.npcs.controllers.recipies.RecipeController;

public class GuiNpcCarpentryBench extends GuiContainerNPCInterface {
	private ResourceLocation resource;
	private ContainerCarpentryBench container;
	private GuiNpcButton button;

	public GuiNpcCarpentryBench(ContainerCarpentryBench container) {
		super(null, container);
		resource = new ResourceLocation("customnpcs", "textures/gui/carpentry.png");
		this.container = container;
		title = "";
		allowUserInput = false;
		closeOnEsc = true;
		ySize = 180;
	}

	@Override
	public void buttonEvent(GuiButton guibutton) {
		displayGuiScreen(new GuiRecipes());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		button.enabled = ((RecipeController.instance != null) && !RecipeController.instance.anvilRecipes.isEmpty());
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		int l = (width - xSize) / 2;
		int i2 = (height - ySize) / 2;
		String title = StatCollector.translateToLocal("tile.npcCarpentyBench.name");
		if (container.getType() == 1) {
			title = StatCollector.translateToLocal("tile.anvil.name");
		}
		this.drawTexturedModalRect(l, i2, 0, 0, xSize, ySize);
		super.drawGuiContainerBackgroundLayer(f, i, j);
		fontRendererObj.drawString(title, guiLeft + 4, guiTop + 4, CustomNpcResourceListener.DefaultTextColor);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), guiLeft + 4, guiTop + 87,
				CustomNpcResourceListener.DefaultTextColor);
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(button = new GuiNpcButton(0, guiLeft + 158, guiTop + 4, 12, 20, "..."));
	}

	@Override
	public void save() {
	}
}
