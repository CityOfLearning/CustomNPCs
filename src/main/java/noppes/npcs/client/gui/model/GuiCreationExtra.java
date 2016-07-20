//

//

package noppes.npcs.client.gui.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.gui.util.GuiButtonBiDirectional;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.entity.EntityFakeLiving;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiCreationExtra extends GuiCreationScreenInterface implements ICustomScrollListener {
	abstract class GuiType {
		public String name;

		public GuiType(String name) {
			this.name = name;
		}

		public void actionPerformed(GuiButton button) {
		}

		public void initGui() {
		}

		public void scrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		}
	}

	class GuiTypeBoolean extends GuiType {
		private boolean bo;

		public GuiTypeBoolean(String name, boolean bo) {
			super(name);
			this.bo = bo;
		}

		@Override
		public void actionPerformed(GuiButton button) {
			if (button.id != 11) {
				return;
			}
			bo = ((GuiNpcButtonYesNo) button).getBoolean();
			if (name.equals("Child")) {
				GuiCreationExtra.this.playerdata.extra.setInteger("Age", bo ? -24000 : 0);
				GuiCreationExtra.this.playerdata.clearEntity();
			} else {
				GuiCreationExtra.this.playerdata.extra.setBoolean(name, bo);
				GuiCreationExtra.this.playerdata.clearEntity();
			}
		}

		@Override
		public void initGui() {
			addButton(new GuiNpcButtonYesNo(11, GuiCreationExtra.this.guiLeft + 120, GuiCreationExtra.this.guiTop + 50,
					60, 20, bo));
		}
	}

	class GuiTypeByte extends GuiType {
		public GuiTypeByte(String name, byte b) {
			super(name);
		}
	}

	class GuiTypeDoggyStyle extends GuiType {
		public GuiTypeDoggyStyle(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(GuiButton button) {
			if (button.id != 11) {
				return;
			}
			((GuiNpcButton) button).getValue();
			EntityLivingBase entity = GuiCreationExtra.this.playerdata.getEntity(GuiCreationExtra.this.npc);
			GuiCreationExtra.this.playerdata.setExtra(entity, "breed", ((GuiNpcButton) button).getValue() + "");
			updateTexture();
		}

		@Override
		public void initGui() {
			Enum breed = null;
			try {
				Method method = GuiCreationExtra.this.entity.getClass().getMethod("getBreedID", new Class[0]);
				breed = (Enum) method.invoke(GuiCreationExtra.this.entity, new Object[0]);
			} catch (Exception ex) {
			}
			addButton(new GuiButtonBiDirectional(11, GuiCreationExtra.this.guiLeft + 120,
					GuiCreationExtra.this.guiTop + 45,
					50, 20, new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
							"14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26" },
					breed.ordinal()));
		}
	}

	private String[] ignoredTags;

	private GuiCustomScroll scroll;

	private Map<String, GuiType> data;

	private GuiType selected;

	public GuiCreationExtra(EntityNPCInterface npc) {
		super(npc);
		ignoredTags = new String[] { "CanBreakDoors", "Bred", "PlayerCreated", "HasReproduced" };
		data = new HashMap<String, GuiType>();
		active = 2;
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		super.actionPerformed(btn);
		if (selected != null) {
			selected.actionPerformed(btn);
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if (scroll.id == 0) {
			initGui();
		} else if (selected != null) {
			selected.scrollClicked(i, j, k, scroll);
		}
	}

	public Map<String, GuiType> getData(EntityLivingBase entity) {
		Map<String, GuiType> data = new HashMap<String, GuiType>();
		NBTTagCompound compound = getExtras(entity);
		Set<String> keys = compound.getKeySet();
		for (String name : keys) {
			if (isIgnored(name)) {
				continue;
			}
			NBTBase base = compound.getTag(name);
			if (name.equals("Age")) {
				data.put("Child", new GuiTypeBoolean("Child", entity.isChild()));
			} else {
				if (base.getId() != 1) {
					continue;
				}
				byte b = ((NBTTagByte) base).getByte();
				if ((b != 0) && (b != 1)) {
					continue;
				}
				if (playerdata.extra.hasKey(name)) {
					b = playerdata.extra.getByte(name);
				}
				data.put(name, new GuiTypeBoolean(name, b == 1));
			}
		}
		if (EntityList.getEntityString(entity).equals("tgvstyle.Dog")) {
			data.put("Breed", new GuiTypeDoggyStyle("Breed"));
		}
		return data;
	}

	private NBTTagCompound getExtras(EntityLivingBase entity) {
		NBTTagCompound fake = new NBTTagCompound();
		new EntityFakeLiving(entity.worldObj).writeEntityToNBT(fake);
		NBTTagCompound compound = new NBTTagCompound();
		entity.writeEntityToNBT(compound);
		Set<String> keys = fake.getKeySet();
		for (String name : keys) {
			compound.removeTag(name);
		}
		return compound;
	}

	@Override
	public void initGui() {
		super.initGui();
		if (entity == null) {
			openGui(new GuiCreationParts(npc));
			return;
		}
		if (scroll == null) {
			data = getData(entity);
			scroll = new GuiCustomScroll(this, 0);
			List<String> list = new ArrayList<String>(data.keySet());
			scroll.setList(list);
			if (list.isEmpty()) {
				return;
			}
			scroll.setSelected(list.get(0));
		}
		selected = data.get(scroll.getSelected());
		if (selected == null) {
			return;
		}
		scroll.guiLeft = guiLeft;
		scroll.guiTop = guiTop + 46;
		scroll.setSize(100, ySize - 74);
		addScroll(scroll);
		selected.initGui();
	}

	private boolean isIgnored(String tag) {
		for (String s : ignoredTags) {
			if (s.equals(tag)) {
				return true;
			}
		}
		return false;
	}

	private void updateTexture() {
		EntityLivingBase entity = playerdata.getEntity(npc);
		RendererLivingEntity render = (RendererLivingEntity) mc.getRenderManager()
				.getEntityRenderObject((Entity) entity);
		npc.display.setSkinTexture(NPCRendererHelper.getTexture(render, entity));
	}
}
