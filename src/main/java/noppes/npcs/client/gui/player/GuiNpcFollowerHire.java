
package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.ai.roles.RoleFollower;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NoppesUtilPlayer;

public class GuiNpcFollowerHire extends GuiContainerNPCInterface {
	private ResourceLocation resource;
	private RoleFollower role;

	public GuiNpcFollowerHire(EntityNPCInterface npc, ContainerNPCFollowerHire container) {
		super(npc, container);
		resource = new ResourceLocation("customnpcs", "textures/gui/followerhire.png");
		role = (RoleFollower) npc.roleInterface;
		closeOnEsc = true;
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 5) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerHire, new Object[0]);
			close();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		int l = (width - xSize) / 2;
		int i2 = (height - ySize) / 2;
		this.drawTexturedModalRect(l, i2, 0, 0, xSize, ySize);
		int index = 0;
		for (int id : role.inventory.items.keySet()) {
			ItemStack itemstack = role.inventory.items.get(id);
			if (itemstack == null) {
				continue;
			}
			int days = 1;
			if (role.rates.containsKey(id)) {
				days = role.rates.get(id);
			}
			int yOffset = index * 26;
			int x = guiLeft + 78;
			int y = guiTop + yOffset + 10;
			GlStateManager.enableRescaleNormal();
			RenderHelper.enableGUIStandardItemLighting();
			itemRender.renderItemAndEffectIntoGUI(itemstack, x + 11, y);
			itemRender.renderItemOverlays(fontRendererObj, itemstack, x + 11, y);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			String daysS = days + " " + ((days == 1) ? StatCollector.translateToLocal("follower.day")
					: StatCollector.translateToLocal("follower.days"));
			fontRendererObj.drawString(" = " + daysS, x + 27, y + 4, CustomNpcResourceListener.DefaultTextColor);
			if (isPointInRegion((x - guiLeft) + 11, y - guiTop, 16, 16, mouseX, mouseY)) {
				renderToolTip(itemstack, mouseX, mouseY);
			}
			++index;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(5, guiLeft + 26, guiTop + 60, 50, 20,
				StatCollector.translateToLocal("follower.hire")));
	}

	@Override
	public void save() {
	}
}
