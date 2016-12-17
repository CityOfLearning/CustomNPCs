
package noppes.npcs.client.gui.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.model.ModelPartData;

public class GuiModelColor extends SubGuiInterface implements ITextfieldListener {
	private static ResourceLocation color;
	private static ResourceLocation colorgui;
	static {
		color = new ResourceLocation("moreplayermodels:textures/gui/color.png");
		colorgui = new ResourceLocation("moreplayermodels:textures/gui/color_gui.png");
	}
	private int colorX;
	private int colorY;
	private GuiNpcTextField textfield;

	private ModelPartData data;

	public GuiModelColor(GuiScreen parent, ModelPartData data) {
		this.data = data;
		ySize = 230;
		closeOnEsc = false;
		background = GuiModelColor.colorgui;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 66) {
			close();
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(GuiModelColor.color);
		this.drawTexturedModalRect(colorX, colorY, 0, 0, 120, 120);
	}

	@Override
	public void initGui() {
		super.initGui();
		colorX = guiLeft + 4;
		colorY = guiTop + 50;
		addTextField(textfield = new GuiNpcTextField(0, this, guiLeft + 35, guiTop + 25, 60, 20, data.getColor()));
		addButton(new GuiNpcButton(66, guiLeft + 107, guiTop + 8, 20, 20, "X"));
		textfield.setTextColor(data.color);
	}

	@Override
	public void keyTyped(char c, int i) {
		String prev = textfield.getText();
		super.keyTyped(c, i);
		String newText = textfield.getText();
		if (newText.equals(prev)) {
			return;
		}
		try {
			int color = Integer.parseInt(textfield.getText(), 16);
			data.color = color;
			textfield.setTextColor(color);
		} catch (NumberFormatException e) {
			textfield.setText(prev);
		}
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		if ((i < colorX) || (i > (colorX + 120)) || (j < colorY) || (j > (colorY + 120))) {
			return;
		}
		InputStream stream = null;
		try {
			IResource resource = mc.getResourceManager().getResource(GuiModelColor.color);
			BufferedImage bufferedimage = ImageIO.read(stream = resource.getInputStream());
			int color = bufferedimage.getRGB((i - guiLeft - 4) * 4, (j - guiTop - 50) * 4) & 0xFFFFFF;
			if (color != 0) {
				data.color = color;
				textfield.setTextColor(color);
				textfield.setText(data.getColor());
			}
		} catch (IOException e) {
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		int color = 0;
		try {
			color = Integer.parseInt(textfield.getText(), 16);
		} catch (NumberFormatException e) {
			color = 0;
		}
		textfield.setTextColor(data.color = color);
	}
}
