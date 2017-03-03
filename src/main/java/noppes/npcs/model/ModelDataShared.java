package noppes.npcs.model;

import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.constants.EnumParts;

public class ModelDataShared {
	protected ModelPartConfig arm1;
	protected ModelPartConfig arm2;
	protected ModelPartConfig body;
	protected ModelPartConfig leg1;
	protected ModelPartConfig leg2;
	protected ModelPartConfig head;
	protected ModelPartData legParts;
	public Class<? extends EntityLivingBase> entityClass;
	protected EntityLivingBase entity;
	public NBTTagCompound extra;
	protected HashMap<EnumParts, ModelPartData> parts;

	public ModelDataShared() {
		arm1 = new ModelPartConfig();
		arm2 = new ModelPartConfig();
		body = new ModelPartConfig();
		leg1 = new ModelPartConfig();
		leg2 = new ModelPartConfig();
		head = new ModelPartConfig();
		legParts = new ModelPartData("legs");
		extra = new NBTTagCompound();
		parts = new HashMap<>();
	}

	public void clearEntity() {
		entity = null;
	}

	public float getBodyY() {
		return ((1.0f - body.scaleY) * 0.75f) + getLegsY();
	}

	public Class<? extends EntityLivingBase> getEntityClass() {
		return entityClass;
	}

	public float getLegsY() {
		ModelPartConfig legs = leg1;
		if (leg2.notShared && (leg2.scaleY > leg1.scaleY)) {
			legs = leg2;
		}
		return (1.0f - legs.scaleY) * 0.75f;
	}

	public ModelPartData getOrCreatePart(EnumParts type) {
		if (type == null) {
			return null;
		}
		ModelPartData part = getPartData(type);
		if (part == null) {
			parts.put(type, part = new ModelPartData(type.name));
		}
		return part;
	}

	public ModelPartConfig getPartConfig(EnumParts type) {
		if (type == EnumParts.BODY) {
			return body;
		}
		if (type == EnumParts.ARM_LEFT) {
			return arm1;
		}
		if (type == EnumParts.ARM_RIGHT) {
			return arm2;
		}
		if (type == EnumParts.LEG_LEFT) {
			return leg1;
		}
		if (type == EnumParts.LEG_RIGHT) {
			return leg2;
		}
		return head;
	}

	public ModelPartData getPartData(EnumParts type) {
		if (type == EnumParts.LEGS) {
			return legParts;
		}
		return parts.get(type);
	}

	public float offsetY() {
		if (entity == null) {
			return -getBodyY();
		}
		return entity.height - 1.8f;
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.setEntityClass(compound.getString("EntityClass"));
		arm1.readFromNBT(compound.getCompoundTag("ArmsConfig"));
		body.readFromNBT(compound.getCompoundTag("BodyConfig"));
		leg1.readFromNBT(compound.getCompoundTag("LegsConfig"));
		head.readFromNBT(compound.getCompoundTag("HeadConfig"));
		legParts.readFromNBT(compound.getCompoundTag("LegParts"));
		extra = compound.getCompoundTag("ExtraData");
		HashMap<EnumParts, ModelPartData> parts = new HashMap<>();
		NBTTagList list = compound.getTagList("Parts", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound item = list.getCompoundTagAt(i);
			String name = item.getString("PartName");
			ModelPartData part = new ModelPartData(name);
			part.readFromNBT(item);
			EnumParts e = EnumParts.FromName(name);
			if (e != null) {
				parts.put(e, part);
			}
		}
		this.parts = parts;
		updateTransate();
	}

	public void removePart(EnumParts type) {
		parts.remove(type);
	}

	public void setEntityClass(Class<? extends EntityLivingBase> entityClass) {
		this.entityClass = entityClass;
		entity = null;
		extra = new NBTTagCompound();
	}

	private void setEntityClass(String string) {
		entityClass = null;
		entity = null;
		try {
			Class<?> cls = Class.forName(string);
			if (EntityLivingBase.class.isAssignableFrom(cls)) {
				entityClass = cls.asSubclass(EntityLivingBase.class);
			}
		} catch (ClassNotFoundException ex) {
		}
	}

	private void updateTransate() {
		for (EnumParts part : EnumParts.values()) {
			ModelPartConfig config = getPartConfig(part);
			if (config != null) {
				if (part == EnumParts.HEAD) {
					config.setTranslate(0.0f, getBodyY(), 0.0f);
				} else if (part == EnumParts.ARM_LEFT) {
					ModelPartConfig body = getPartConfig(EnumParts.BODY);
					float x = ((1.0f - body.scaleX) * 0.25f) + ((1.0f - config.scaleX) * 0.075f);
					float y = getBodyY() + ((1.0f - config.scaleY) * -0.1f);
					config.setTranslate(-x, y, 0.0f);
					if (!config.notShared) {
						ModelPartConfig arm = getPartConfig(EnumParts.ARM_RIGHT);
						arm.copyValues(config);
					}
				} else if (part == EnumParts.ARM_RIGHT) {
					ModelPartConfig body = getPartConfig(EnumParts.BODY);
					float x = ((1.0f - body.scaleX) * 0.25f) + ((1.0f - config.scaleX) * 0.075f);
					float y = getBodyY() + ((1.0f - config.scaleY) * -0.1f);
					config.setTranslate(x, y, 0.0f);
				} else if (part == EnumParts.LEG_LEFT) {
					config.setTranslate((config.scaleX * 0.125f) - 0.113f, getLegsY(), 0.0f);
					if (!config.notShared) {
						ModelPartConfig leg = getPartConfig(EnumParts.LEG_RIGHT);
						leg.copyValues(config);
					}
				} else if (part == EnumParts.LEG_RIGHT) {
					config.setTranslate((1.0f - config.scaleX) * 0.125f, getLegsY(), 0.0f);
				} else if (part == EnumParts.BODY) {
					config.setTranslate(0.0f, getBodyY(), 0.0f);
				}
			}
		}
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		if (entityClass != null) {
			compound.setString("EntityClass", entityClass.getCanonicalName());
		}
		compound.setTag("ArmsConfig", arm1.writeToNBT());
		compound.setTag("BodyConfig", body.writeToNBT());
		compound.setTag("LegsConfig", leg1.writeToNBT());
		compound.setTag("HeadConfig", head.writeToNBT());
		compound.setTag("LegParts", legParts.writeToNBT());
		compound.setTag("ExtraData", extra);
		NBTTagList list = new NBTTagList();
		for (EnumParts e : parts.keySet()) {
			NBTTagCompound item = parts.get(e).writeToNBT();
			item.setString("PartName", e.name);
			list.appendTag(item);
		}
		compound.setTag("Parts", list);
		return compound;
	}
}
