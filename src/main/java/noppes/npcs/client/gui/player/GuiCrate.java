package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.containers.ContainerCrate;

@SideOnly(Side.CLIENT)
public class GuiCrate extends GuiContainer {
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation(
			"textures/gui/container/generic_54.png");
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private int inventoryRows;

	public GuiCrate(ContainerCrate container) {
		super(container);
		upperChestInventory = container.upperChestInventory;
		lowerChestInventory = container.lowerChestInventory;
		allowUserInput = false;
		short short1 = 222;
		int i = short1 - 108;
		inventoryRows = (lowerChestInventory.getSizeInventory() / 9);
		ySize = (i + (inventoryRows * 18));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, (inventoryRows * 18) + 17);
		drawTexturedModalRect(k, l + (inventoryRows * 18) + 17, 0, 126, xSize, 96);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(
				lowerChestInventory.hasCustomName() ? lowerChestInventory.getName()
						: I18n.format(lowerChestInventory.getName(), new Object[0]),
				8, 6, CustomNpcResourceListener.DefaultTextColor);
		fontRendererObj.drawString(
				upperChestInventory.hasCustomName() ? upperChestInventory.getName()
						: I18n.format(upperChestInventory.getName(), new Object[0]),
				8, (ySize - 96) + 2, CustomNpcResourceListener.DefaultTextColor);
	}
}
