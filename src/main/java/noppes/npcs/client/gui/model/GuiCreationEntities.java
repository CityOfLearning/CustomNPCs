
package noppes.npcs.client.gui.model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiCreationEntities extends GuiCreationScreenInterface implements ICustomScrollListener {
	public HashMap<String, Class<? extends EntityLivingBase>> data;
	private List<String> list;
	private GuiCustomScroll scroll;
	private boolean resetToSelected;

	public GuiCreationEntities(EntityNPCInterface npc) {
		super(npc);
		data = new HashMap<String, Class<? extends EntityLivingBase>>();
		resetToSelected = true;
		for (Object name : EntityList.stringToClassMapping.keySet()) {
			Class<? extends Entity> c = EntityList.stringToClassMapping.get(name);
			try {
				if (!EntityLiving.class.isAssignableFrom(c) || (c.getConstructor(World.class) == null)
						|| Modifier.isAbstract(c.getModifiers()) || !(Minecraft.getMinecraft().getRenderManager()
								.getEntityClassRenderObject((Class) c) instanceof RendererLivingEntity)) {
					continue;
				}
				String s = name.toString();
				if (s.toLowerCase().contains("customnpc")) {
					continue;
				}
				data.put(name.toString(), c.asSubclass(EntityLivingBase.class));
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException ex) {
			}
		}
		data.put("NPC 64x32", EntityNPC64x32.class);
		(list = new ArrayList<String>(data.keySet())).add("NPC");
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		active = 1;
		xOffset = 60;
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		super.actionPerformed(btn);
		if (btn.id == 10) {
			playerdata.setEntityClass((Class<? extends EntityLivingBase>) null);
			resetToSelected = true;
			initGui();
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		playerdata.setEntityClass(data.get(scroll.getSelected()));
		Entity entity = playerdata.getEntity(npc);
		if (entity != null) {
			RendererLivingEntity render = (RendererLivingEntity) mc.getRenderManager()
					.getEntityClassRenderObject((Class) entity.getClass());
			npc.display.setSkinTexture(NPCRendererHelper.getTexture(render, entity));
		} else {
			npc.display.setSkinTexture("customnpcs:textures/entity/humanmale/Steve.png");
		}
		initGui();
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiNpcButton(10, guiLeft, guiTop + 46, 120, 20, "Reset To NPC"));
		if (scroll == null) {
			(scroll = new GuiCustomScroll(this, 0)).setUnsortedList(list);
		}
		scroll.guiLeft = guiLeft;
		scroll.guiTop = guiTop + 68;
		scroll.setSize(100, ySize - 96);
		String selected = "NPC";
		if (entity != null) {
			for (Map.Entry<String, Class<? extends EntityLivingBase>> en : data.entrySet()) {
				if (en.getValue().toString().equals(entity.getClass().toString())) {
					selected = en.getKey();
				}
			}
		}
		scroll.setSelected(selected);
		if (resetToSelected) {
			scroll.scrollTo(scroll.getSelected());
			resetToSelected = false;
		}
		addScroll(scroll);
	}
}
