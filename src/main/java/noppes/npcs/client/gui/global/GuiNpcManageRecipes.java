//

//

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.controllers.recipies.RecipeCarpentry;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcManageRecipes extends GuiContainerNPCInterface2
		implements IScrollData, IGuiData, ICustomScrollListener, ITextfieldListener {
	private GuiCustomScroll scroll;
	private HashMap<String, Integer> data;
	private ContainerManageRecipes container;
	private String selected;
	private ResourceLocation slot;

	public GuiNpcManageRecipes(EntityNPCInterface npc, ContainerManageRecipes container) {
		super(npc, container);
		data = new HashMap<String, Integer>();
		selected = null;
		this.container = container;
		drawDefaultBackground = false;
		Client.sendData(EnumPacketServer.RecipesGet, container.width, 100);
		setBackground("inventorymenu.png");
		slot = getResource("slot.png");
		ySize = 200;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			save();
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes, 3, 0, 0);
		}
		if (button.id == 1) {
			save();
			NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes, 4, 0, 0);
		}
		if (button.id == 3) {
			save();
			scroll.clear();
			String name;
			for (name = "New"; data.containsKey(name); name += "_") {
			}
			RecipeCarpentry recipe = new RecipeCarpentry(name);
			recipe.isGlobal = (container.width == 3);
			Client.sendData(EnumPacketServer.RecipeSave, recipe.writeNBT());
		}
		if ((button.id == 4) && data.containsKey(scroll.getSelected())) {
			Client.sendData(EnumPacketServer.RecipeRemove, data.get(scroll.getSelected()));
			scroll.clear();
		}
		if (button.id == 5) {
			container.recipe.ignoreDamage = (button.getValue() == 1);
		}
		if (button.id == 6) {
			container.recipe.ignoreNBT = (button.getValue() == 1);
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		save();
		selected = scroll.getSelected();
		Client.sendData(EnumPacketServer.RecipeGet, data.get(selected));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(slot);
		for (int i = 0; i < container.width; ++i) {
			for (int j = 0; j < container.width; ++j) {
				this.drawTexturedModalRect(guiLeft + (i * 18) + 7, guiTop + (j * 18) + 34, 0, 0, 18, 18);
			}
		}
		this.drawTexturedModalRect(guiLeft + 86, guiTop + 60, 0, 0, 18, 18);
	}

	@Override
	public void initGui() {
		super.initGui();
		if (scroll == null) {
			scroll = new GuiCustomScroll(this, 0);
		}
		scroll.setSize(130, 180);
		scroll.guiLeft = guiLeft + 172;
		scroll.guiTop = guiTop + 8;
		addScroll(scroll);
		addButton(new GuiNpcButton(0, guiLeft + 306, guiTop + 10, 84, 20, "menu.global"));
		addButton(new GuiNpcButton(1, guiLeft + 306, guiTop + 32, 84, 20, "tile.npcCarpentyBench.name"));
		getButton(0).setEnabled(container.width == 4);
		getButton(1).setEnabled(container.width == 3);
		addButton(new GuiNpcButton(3, guiLeft + 306, guiTop + 60, 84, 20, "gui.add"));
		addButton(new GuiNpcButton(4, guiLeft + 306, guiTop + 82, 84, 20, "gui.remove"));
		addLabel(new GuiNpcLabel(0, "gui.ignoreDamage", guiLeft + 86, guiTop + 32));
		addButton(new GuiNpcButtonYesNo(5, guiLeft + 114, guiTop + 40, 50, 20, container.recipe.ignoreDamage));
		addLabel(new GuiNpcLabel(1, "gui.ignoreNBT", guiLeft + 86, guiTop + 82));
		addButton(new GuiNpcButtonYesNo(6, guiLeft + 114, guiTop + 90, 50, 20, container.recipe.ignoreNBT));
		addTextField(
				new GuiNpcTextField(0, this, fontRendererObj, guiLeft + 8, guiTop + 8, 160, 20, container.recipe.name));
		getTextField(0).enabled = false;
		getButton(5).setEnabled(false);
		getButton(6).setEnabled(false);
	}

	@Override
	public void save() {
		GuiNpcTextField.unfocus();
		if ((selected != null) && data.containsKey(selected)) {
			container.saveRecipe();
			Client.sendData(EnumPacketServer.RecipeSave, container.recipe.writeNBT());
		}
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		String name = scroll.getSelected();
		this.data = data;
		scroll.setList(list);
		getTextField(0).enabled = (name != null);
		getButton(5).setEnabled(name != null);
		if (name != null) {
			scroll.setSelected(name);
		}
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		RecipeCarpentry recipe = RecipeCarpentry.read(compound);
		getTextField(0).setText(recipe.name);
		container.setRecipe(recipe);
		getTextField(0).enabled = true;
		getButton(5).setEnabled(true);
		getButton(5).setDisplay(recipe.ignoreDamage ? 1 : 0);
		getButton(6).setEnabled(true);
		getButton(6).setDisplay(recipe.ignoreNBT ? 1 : 0);
		setSelected(recipe.name);
	}

	@Override
	public void setSelected(String selected) {
		this.selected = selected;
		scroll.setSelected(selected);
	}

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		String name = guiNpcTextField.getText();
		if (!name.isEmpty() && !data.containsKey(name)) {
			String old = container.recipe.name;
			data.remove(container.recipe.name);
			container.recipe.name = name;
			data.put(container.recipe.name, container.recipe.id);
			selected = name;
			scroll.replace(old, container.recipe.name);
		}
	}
}
