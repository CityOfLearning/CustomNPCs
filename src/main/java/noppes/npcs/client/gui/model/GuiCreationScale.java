
package noppes.npcs.client.gui.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.model.ModelPartConfig;

public class GuiCreationScale extends GuiCreationScreenInterface implements ISliderListener, ICustomScrollListener {
	private static EnumParts selected;
	static {
		GuiCreationScale.selected = EnumParts.HEAD;
	}
	private GuiCustomScroll scroll;

	private List<EnumParts> data;

	public GuiCreationScale(EntityNPCInterface npc) {
		super(npc);
		data = new ArrayList<>();
		active = 3;
		xOffset = 140;
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		super.actionPerformed(btn);
		if (btn.id == 13) {
			boolean bo = ((GuiNpcButton) btn).getValue() == 0;
			playerdata.getPartConfig(GuiCreationScale.selected).notShared = bo;
			initGui();
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if (scroll.selected >= 0) {
			GuiCreationScale.selected = data.get(scroll.selected);
			initGui();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		if (scroll == null) {
			scroll = new GuiCustomScroll(this, 0);
		}
		ArrayList list = new ArrayList();
		EnumParts[] parts = { EnumParts.HEAD, EnumParts.BODY, EnumParts.ARM_LEFT, EnumParts.ARM_RIGHT,
				EnumParts.LEG_LEFT, EnumParts.LEG_RIGHT };
		data.clear();
		for (EnumParts part : parts) {
			Label_0210: {
				if (part == EnumParts.ARM_RIGHT) {
					ModelPartConfig config = playerdata.getPartConfig(EnumParts.ARM_LEFT);
					if (!config.notShared) {
						break Label_0210;
					}
				}
				if (part == EnumParts.LEG_RIGHT) {
					ModelPartConfig config = playerdata.getPartConfig(EnumParts.LEG_LEFT);
					if (!config.notShared) {
						break Label_0210;
					}
				}
				data.add(part);
				list.add(StatCollector.translateToLocal("part." + part.name));
			}
		}
		scroll.setUnsortedList(list);
		scroll.setSelected(StatCollector.translateToLocal("part." + GuiCreationScale.selected.name));
		scroll.guiLeft = guiLeft;
		scroll.guiTop = guiTop + 46;
		scroll.setSize(100, ySize - 74);
		addScroll(scroll);
		ModelPartConfig config2 = playerdata.getPartConfig(GuiCreationScale.selected);
		int y = guiTop + 65;
		addLabel(new GuiNpcLabel(10, "scale.width", guiLeft + 102, y + 5, 16777215));
		addSlider(new GuiNpcSlider(this, 10, guiLeft + 150, y, 100, 20, config2.scaleX - 0.5f));
		y += 22;
		addLabel(new GuiNpcLabel(11, "scale.height", guiLeft + 102, y + 5, 16777215));
		addSlider(new GuiNpcSlider(this, 11, guiLeft + 150, y, 100, 20, config2.scaleY - 0.5f));
		y += 22;
		addLabel(new GuiNpcLabel(12, "scale.depth", guiLeft + 102, y + 5, 16777215));
		addSlider(new GuiNpcSlider(this, 12, guiLeft + 150, y, 100, 20, config2.scaleZ - 0.5f));
		if ((GuiCreationScale.selected == EnumParts.ARM_LEFT) || (GuiCreationScale.selected == EnumParts.LEG_LEFT)) {
			y += 22;
			addLabel(new GuiNpcLabel(13, "scale.shared", guiLeft + 102, y + 5, 16777215));
			addButton(new GuiNpcButton(13, guiLeft + 150, y, 50, 20, new String[] { "gui.no", "gui.yes" },
					config2.notShared ? 0 : 1));
		}
	}

	@Override
	public void mouseDragged(GuiNpcSlider slider) {
		super.mouseDragged(slider);
		if ((slider.id >= 10) && (slider.id <= 12)) {
			int percent = (int) (50.0f + (slider.sliderValue * 100.0f));
			slider.setString(percent + "%");
			ModelPartConfig config = playerdata.getPartConfig(GuiCreationScale.selected);
			if (slider.id == 10) {
				config.scaleX = slider.sliderValue + 0.5f;
			}
			if (slider.id == 11) {
				config.scaleY = slider.sliderValue + 0.5f;
			}
			if (slider.id == 12) {
				config.scaleZ = slider.sliderValue + 0.5f;
			}
			updateTransate();
		}
	}

	private void updateTransate() {
		for (EnumParts part : EnumParts.values()) {
			ModelPartConfig config = playerdata.getPartConfig(part);
			if (config != null) {
				if (part == EnumParts.HEAD) {
					config.setTranslate(0.0f, playerdata.getBodyY(), 0.0f);
				} else if (part == EnumParts.ARM_LEFT) {
					ModelPartConfig body = playerdata.getPartConfig(EnumParts.BODY);
					float x = ((1.0f - body.scaleX) * 0.25f) + ((1.0f - config.scaleX) * 0.075f);
					float y = playerdata.getBodyY() + ((1.0f - config.scaleY) * -0.1f);
					config.setTranslate(-x, y, 0.0f);
					if (!config.notShared) {
						ModelPartConfig arm = playerdata.getPartConfig(EnumParts.ARM_RIGHT);
						arm.copyValues(config);
					}
				} else if (part == EnumParts.ARM_RIGHT) {
					ModelPartConfig body = playerdata.getPartConfig(EnumParts.BODY);
					float x = ((1.0f - body.scaleX) * 0.25f) + ((1.0f - config.scaleX) * 0.075f);
					float y = playerdata.getBodyY() + ((1.0f - config.scaleY) * -0.1f);
					config.setTranslate(x, y, 0.0f);
				} else if (part == EnumParts.LEG_LEFT) {
					config.setTranslate((config.scaleX * 0.125f) - 0.113f, playerdata.getLegsY(), 0.0f);
					if (!config.notShared) {
						ModelPartConfig leg = playerdata.getPartConfig(EnumParts.LEG_RIGHT);
						leg.copyValues(config);
					}
				} else if (part == EnumParts.LEG_RIGHT) {
					config.setTranslate((1.0f - config.scaleX) * 0.125f, playerdata.getLegsY(), 0.0f);
				} else if (part == EnumParts.BODY) {
					config.setTranslate(0.0f, playerdata.getBodyY(), 0.0f);
				}
			}
		}
	}
}
