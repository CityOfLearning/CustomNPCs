
package noppes.npcs.client.gui.model;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ModelData;
import noppes.npcs.client.Client;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.mainmenu.GuiNpcDisplay;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiCreationScreenInterface extends GuiNPCInterface implements ISubGuiListener, ISliderListener {
	public static String Message;
	private static float rotation;
	static {
		GuiCreationScreenInterface.Message = "";
		GuiCreationScreenInterface.rotation = 0.5f;
	}
	public EntityLivingBase entity;
	private boolean saving;
	protected boolean hasSaving;
	public int active;
	private EntityPlayer player;
	public int xOffset;
	public ModelData playerdata;

	protected NBTTagCompound original;

	public GuiCreationScreenInterface(EntityNPCInterface npc) {
		super(npc);
		saving = false;
		hasSaving = true;
		active = 0;
		xOffset = 0;
		original = new NBTTagCompound();
		playerdata = ((EntityCustomNpc) npc).modelData;
		original = playerdata.writeToNBT();
		xSize = 400;
		ySize = 240;
		xOffset = 140;
		player = Minecraft.getMinecraft().thePlayer;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		super.actionPerformed(btn);
		if (btn.id == 1) {
			openGui(new GuiCreationEntities(npc));
		}
		if (btn.id == 2) {
			if (entity == null) {
				openGui(new GuiCreationParts(npc));
			} else {
				openGui(new GuiCreationExtra(npc));
			}
		}
		if (btn.id == 3) {
			openGui(new GuiCreationScale(npc));
		}
		if (btn.id == 4) {
			setSubGui(new GuiPresetSave(this, playerdata));
		}
		if (btn.id == 5) {
			openGui(new GuiCreationLoad(npc));
		}
		if (btn.id == 66) {
			save();
			NoppesUtil.openGUI(player, new GuiNpcDisplay(npc));
		}
	}

	@Override
	public void drawScreen(int x, int y, float f) {
		super.drawScreen(x, y, f);
		entity = playerdata.getEntity(npc);
		EntityLivingBase entity = this.entity;
		if (entity == null) {
			entity = npc;
		} else {
			EntityUtil.Copy(entity, npc);
		}
		this.drawNpc(entity, xOffset + 200, 200, 2.0f, (int) ((GuiCreationScreenInterface.rotation * 360.0f) - 180.0f));
	}

	@Override
	public void initGui() {
		super.initGui();
		entity = playerdata.getEntity(npc);
		Keyboard.enableRepeatEvents(true);
		addButton(new GuiNpcButton(1, guiLeft + 62, guiTop, 60, 20, "gui.entity"));
		if (entity == null) {
			addButton(new GuiNpcButton(2, guiLeft, guiTop + 23, 60, 20, "gui.parts"));
		} else if (!(entity instanceof EntityNPCInterface)) {
			GuiCreationExtra gui = new GuiCreationExtra(npc);
			gui.playerdata = playerdata;
			if (!gui.getData(entity).isEmpty()) {
				addButton(new GuiNpcButton(2, guiLeft, guiTop + 23, 60, 20, "gui.extra"));
			} else if (active == 2) {
				mc.displayGuiScreen(new GuiCreationEntities(npc));
				return;
			}
		}
		if (entity == null) {
			addButton(new GuiNpcButton(3, guiLeft + 62, guiTop + 23, 60, 20, "gui.scale"));
		}
		if (hasSaving) {
			addButton(new GuiNpcButton(4, guiLeft, (guiTop + ySize) - 24, 60, 20, "gui.save"));
			addButton(new GuiNpcButton(5, guiLeft + 62, (guiTop + ySize) - 24, 60, 20, "gui.load"));
		}
		if (getButton(active) == null) {
			openGui(new GuiCreationEntities(npc));
			return;
		}
		getButton(active).enabled = false;
		addButton(new GuiNpcButton(66, (guiLeft + xSize) - 20, guiTop, 20, 20, "X"));
		addLabel(
				new GuiNpcLabel(0, GuiCreationScreenInterface.Message, guiLeft + 120, (guiTop + ySize) - 10, 16711680));
		getLabel(0).center(xSize - 120);
		addSlider(new GuiNpcSlider(this, 500, guiLeft + xOffset + 142, guiTop + 210, 120, 20,
				GuiCreationScreenInterface.rotation));
	}

	@Override
	public void mouseClicked(int i, int j, int k) {
		if (!saving) {
			super.mouseClicked(i, j, k);
		}
	}

	@Override
	public void mouseDragged(GuiNpcSlider slider) {
		if (slider.id == 500) {
			GuiCreationScreenInterface.rotation = slider.sliderValue;
			slider.setString("" + (int) (GuiCreationScreenInterface.rotation * 360.0f));
		}
	}

	@Override
	public void mousePressed(GuiNpcSlider slider) {
	}

	@Override
	public void mouseReleased(GuiNpcSlider slider) {
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void openGui(GuiScreen gui) {
		mc.displayGuiScreen(gui);
	}

	@Override
	public void save() {
		NBTTagCompound newCompound = playerdata.writeToNBT();
		Client.sendData(EnumPacketServer.MainmenuDisplaySave, npc.display.writeToNBT(new NBTTagCompound()));
		Client.sendData(EnumPacketServer.ModelDataSave, newCompound);
	}

	@Override
	public void subGuiClosed(SubGuiInterface subgui) {
		initGui();
	}
}
