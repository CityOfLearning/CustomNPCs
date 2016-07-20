//

//

package noppes.npcs.client.gui.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.gui.util.GuiButtonBiDirectional;
import noppes.npcs.client.gui.util.GuiColorButton;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiCreationParts extends GuiCreationScreenInterface implements ITextfieldListener, ICustomScrollListener {
	class GuiPart {
		EnumParts part;
		protected String[] types;
		protected ModelPartData data;
		protected boolean hasPlayerOption;
		protected boolean noPlayerTypes;
		protected boolean canBeDeleted;

		public GuiPart(EnumParts part) {
			types = new String[] { "gui.none" };
			hasPlayerOption = true;
			noPlayerTypes = false;
			canBeDeleted = true;
			this.part = part;
			data = GuiCreationParts.this.playerdata.getPartData(part);
		}

		protected void actionPerformed(GuiButton btn) {
			if (btn.id == 20) {
				int i = ((GuiNpcButton) btn).getValue();
				if ((i == 0) && canBeDeleted) {
					GuiCreationParts.this.playerdata.removePart(part);
				} else {
					data = GuiCreationParts.this.playerdata.getOrCreatePart(part);
					data.pattern = 0;
					data.setType(i - 1);
				}
				GuiCreationParts.this.initGui();
			}
			if (btn.id == 22) {
				data.pattern = (byte) ((GuiNpcButton) btn).getValue();
			}
			if (btn.id == 21) {
				data.playerTexture = ((GuiNpcButtonYesNo) btn).getBoolean();
				GuiCreationParts.this.initGui();
			}
			if (btn.id == 23) {
				setSubGui(new GuiModelColor(GuiCreationParts.this, data));
			}
		}

		public int initGui() {
			data = GuiCreationParts.this.playerdata.getPartData(part);
			int y = GuiCreationParts.this.guiTop + 50;
			if ((data == null) || !data.playerTexture || !noPlayerTypes) {
				addLabel(new GuiNpcLabel(20, "gui.type", GuiCreationParts.this.guiLeft + 102, y + 5, 16777215));
				addButton(new GuiButtonBiDirectional(20, GuiCreationParts.this.guiLeft + 145, y, 100, 20, types,
						(data == null) ? 0 : (data.type + 1)));
				y += 25;
			}
			if ((data != null) && hasPlayerOption) {
				addLabel(new GuiNpcLabel(21, "gui.playerskin", GuiCreationParts.this.guiLeft + 102, y + 5, 16777215));
				addButton(new GuiNpcButtonYesNo(21, GuiCreationParts.this.guiLeft + 170, y, data.playerTexture));
				y += 25;
			}
			if ((data != null) && !data.playerTexture) {
				addLabel(new GuiNpcLabel(23, "gui.color", GuiCreationParts.this.guiLeft + 102, y + 5, 16777215));
				addButton(new GuiColorButton(23, GuiCreationParts.this.guiLeft + 170, y, data.color));
				y += 25;
			}
			return y;
		}

		public GuiPart noPlayerOptions() {
			hasPlayerOption = false;
			return this;
		}

		public GuiPart noPlayerTypes() {
			noPlayerTypes = true;
			return this;
		}

		public GuiPart setTypes(String[] types) {
			this.types = types;
			return this;
		}
	}

	class GuiPartBeard extends GuiPart {
		public GuiPartBeard() {
			super(EnumParts.BEARD);
			types = new String[] { "gui.none", "1", "2", "3", "4" };
			noPlayerTypes();
		}
	}

	class GuiPartClaws extends GuiPart {
		public GuiPartClaws() {
			super(EnumParts.CLAWS);
			types = new String[] { "gui.none", "gui.show" };
		}

		@Override
		public int initGui() {
			int y = super.initGui();
			if (data == null) {
				return y;
			}
			addLabel(new GuiNpcLabel(22, "gui.pattern", GuiCreationParts.this.guiLeft + 102, y + 5, 16777215));
			addButton(new GuiButtonBiDirectional(22, GuiCreationParts.this.guiLeft + 145, y, 100, 20,
					new String[] { "gui.both", "gui.left", "gui.right" }, data.pattern));
			return y;
		}
	}

	class GuiPartHair extends GuiPart {
		public GuiPartHair() {
			super(EnumParts.HAIR);
			types = new String[] { "gui.none", "1", "2", "3", "4" };
			noPlayerTypes();
		}
	}

	class GuiPartHorns extends GuiPart {
		public GuiPartHorns() {
			super(EnumParts.HORNS);
			types = new String[] { "gui.none", "horns.bull", "horns.antlers", "horns.antenna" };
		}

		@Override
		public int initGui() {
			int y = super.initGui();
			if ((data != null) && (data.type == 2)) {
				addLabel(new GuiNpcLabel(22, "gui.pattern", GuiCreationParts.this.guiLeft + 102, y + 5, 16777215));
				addButton(new GuiButtonBiDirectional(22, GuiCreationParts.this.guiLeft + 145, y, 100, 20,
						new String[] { "1", "2" }, data.pattern));
			}
			return y;
		}
	}

	class GuiPartLegs extends GuiPart {
		public GuiPartLegs() {
			super(EnumParts.LEGS);
			types = new String[] { "gui.none", "gui.normal", "legs.naga", "legs.spider", "legs.horse", "legs.mermaid",
					"legs.digitigrade" };
			canBeDeleted = false;
		}

		@Override
		protected void actionPerformed(GuiButton btn) {
			if (btn.id == 20) {
				int i = ((GuiNpcButton) btn).getValue();
				if (i <= 1) {
					data.playerTexture = true;
				} else {
					data.playerTexture = false;
				}
			}
			super.actionPerformed(btn);
		}

		@Override
		public int initGui() {
			hasPlayerOption = ((data.type == 1) || (data.type == 5));
			return super.initGui();
		}
	}

	class GuiPartSnout extends GuiPart {
		public GuiPartSnout() {
			super(EnumParts.SNOUT);
			types = new String[] { "gui.none", "snout.small", "snout.medium", "snout.large", "snout.bunny",
					"snout.beak" };
		}
	}

	class GuiPartTail extends GuiPart {
		public GuiPartTail() {
			super(EnumParts.TAIL);
			types = new String[] { "gui.none", "part.tail", "tail.dragon", "tail.horse", "tail.squirrel", "tail.fin",
					"tail.rodent" };
		}

		@Override
		public int initGui() {
			data = GuiCreationParts.this.playerdata.getPartData(part);
			hasPlayerOption = ((data != null) && ((data.type == 0) || (data.type == 1)));
			return super.initGui();
		}
	}

	private static int selected;

	static {
		GuiCreationParts.selected = 0;
	}

	private GuiCustomScroll scroll;

	private GuiPart[] parts;

	public GuiCreationParts(EntityNPCInterface npc) {
		super(npc);
		parts = new GuiPart[] {
				new GuiPart(EnumParts.EARS).setTypes(new String[] { "gui.none", "gui.normal", "ears.bunny" }),
				new GuiPartHorns(), new GuiPartHair(),
				new GuiPart(EnumParts.MOHAWK).setTypes(new String[] { "gui.none", "1", "2" }).noPlayerOptions(),
				new GuiPartSnout(), new GuiPartBeard(),
				new GuiPart(EnumParts.FIN).setTypes(new String[] { "gui.none", "fin.shark", "fin.reptile" }),
				new GuiPart(EnumParts.BREASTS).setTypes(new String[] { "gui.none", "1", "2", "3" }).noPlayerOptions(),
				new GuiPart(EnumParts.WINGS).setTypes(new String[] { "gui.none", "1", "2", "3" }), new GuiPartClaws(),
				new GuiPart(EnumParts.SKIRT).setTypes(new String[] { "gui.none", "gui.normal" }).noPlayerOptions(),
				new GuiPartLegs(), new GuiPartTail() };
		active = 2;
		closeOnEsc = false;
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		super.actionPerformed(btn);
		if (parts[GuiCreationParts.selected] != null) {
			parts[GuiCreationParts.selected].actionPerformed(btn);
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if (scroll.selected >= 0) {
			GuiCreationParts.selected = scroll.selected;
			initGui();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		if (entity != null) {
			openGui(new GuiCreationExtra(npc));
			return;
		}
		if (scroll == null) {
			List<String> list = new ArrayList<String>();
			for (GuiPart part : parts) {
				list.add(StatCollector.translateToLocal("part." + part.part.name));
			}
			(scroll = new GuiCustomScroll(this, 0)).setUnsortedList(list);
		}
		scroll.guiLeft = guiLeft;
		scroll.guiTop = guiTop + 46;
		scroll.setSize(100, ySize - 74);
		addScroll(scroll);
		if (parts[GuiCreationParts.selected] != null) {
			scroll.setSelected(StatCollector.translateToLocal("part." + parts[GuiCreationParts.selected].part.name));
			parts[GuiCreationParts.selected].initGui();
		}
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id == 23) {
		}
	}
}
