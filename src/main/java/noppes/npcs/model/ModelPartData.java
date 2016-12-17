
package noppes.npcs.model;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ModelPartData {
	private static Map<String, ResourceLocation> resources;
	static {
		ModelPartData.resources = new HashMap<String, ResourceLocation>();
	}
	public int color;
	public int colorPattern;
	public byte type;
	public byte pattern;
	public boolean playerTexture;
	public String name;

	private ResourceLocation location;

	public ModelPartData(String name) {
		color = 16777215;
		colorPattern = 16777215;
		type = 0;
		pattern = 0;
		playerTexture = false;
		this.name = name;
	}

	public String getColor() {
		String str;
		for (str = Integer.toHexString(color); str.length() < 6; str = "0" + str) {
		}
		return str;
	}

	public ResourceLocation getResource() {
		if (location != null) {
			return location;
		}
		String texture = name + "/" + type;
		if ((location = ModelPartData.resources.get(texture)) != null) {
			return location;
		}
		location = new ResourceLocation("moreplayermodels:textures/" + texture + ".png");
		ModelPartData.resources.put(texture, location);
		return location;
	}

	public void readFromNBT(NBTTagCompound compound) {
		type = compound.getByte("Type");
		color = compound.getInteger("Color");
		playerTexture = compound.getBoolean("PlayerTexture");
		pattern = compound.getByte("Pattern");
		location = null;
	}

	public void setType(int type) {
		this.type = (byte) type;
		location = null;
	}

	@Override
	public String toString() {
		return "Color: " + color + " Type: " + type;
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setByte("Type", type);
		compound.setInteger("Color", color);
		compound.setBoolean("PlayerTexture", playerTexture);
		compound.setByte("Pattern", pattern);
		return compound;
	}
}
