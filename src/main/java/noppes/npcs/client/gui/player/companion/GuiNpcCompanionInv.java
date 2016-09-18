
package noppes.npcs.client.gui.player.companion;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerNPCCompanion;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;

public class GuiNpcCompanionInv extends GuiContainerNPCInterface {
	private ResourceLocation resource;
	private ResourceLocation slot;
	private EntityNPCInterface npc;
	private RoleCompanion role;

	public GuiNpcCompanionInv(EntityNPCInterface npc, ContainerNPCCompanion container) {
		super(npc, container);
		resource = new ResourceLocation("customnpcs", "textures/gui/companioninv.png");
		slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");
		this.npc = npc;
		role = (RoleCompanion) npc.roleInterface;
		closeOnEsc = true;
		xSize = 171;
		ySize = 166;
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		int id = guibutton.id;
		if (id == 1) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.Companion);
		}
		if (id == 2) {
			CustomNpcs.proxy.openGui(npc, EnumGuiType.CompanionTalent);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int xMouse, int yMouse) {
		drawWorldBackground(0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(resource);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		mc.renderEngine.bindTexture(slot);
		if (role.getTalentLevel(EnumCompanionTalent.ARMOR) > 0) {
			for (int i = 0; i < 4; ++i) {
				this.drawTexturedModalRect(guiLeft + 5, guiTop + 7 + (i * 18), 0, 0, 18, 18);
			}
		}
		if (role.getTalentLevel(EnumCompanionTalent.SWORD) > 0) {
			this.drawTexturedModalRect(guiLeft + 78, guiTop + 16, 0, (npc.inventory.weapons.get(0) == null) ? 18 : 0,
					18, 18);
		}
		if (role.getTalentLevel(EnumCompanionTalent.RANGED) > 0) {
		}
		if (role.talents.containsKey(EnumCompanionTalent.INVENTORY)) {
			for (int size = (role.getTalentLevel(EnumCompanionTalent.INVENTORY) + 1) * 2, j = 0; j < size; ++j) {
				this.drawTexturedModalRect(guiLeft + 113 + ((j % 3) * 18), guiTop + 7 + ((j / 3) * 18), 0, 0, 18, 18);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		super.drawNpc(52, 70);
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiNpcCompanionStats.addTopMenu(role, this, 3);
	}

	@Override
	public void save() {
	}
}
