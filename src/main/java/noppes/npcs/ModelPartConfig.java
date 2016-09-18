
package noppes.npcs;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.util.ValueUtil;

public class ModelPartConfig {
	public float scaleX;
	public float scaleY;
	public float scaleZ;
	public float transX;
	public float transY;
	public float transZ;
	public boolean notShared;

	public ModelPartConfig() {
		scaleX = 1.0f;
		scaleY = 1.0f;
		scaleZ = 1.0f;
		transX = 0.0f;
		transY = 0.0f;
		transZ = 0.0f;
		notShared = false;
	}

	public float checkValue(float given, float min, float max) {
		if (given < min) {
			return min;
		}
		if (given > max) {
			return max;
		}
		return given;
	}

	public void copyValues(ModelPartConfig config) {
		scaleX = config.scaleX;
		scaleY = config.scaleY;
		scaleZ = config.scaleZ;
		transX = config.transX;
		transY = config.transY;
		transZ = config.transZ;
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public float getScaleZ() {
		return scaleZ;
	}

	public void readFromNBT(NBTTagCompound compound) {
		scaleX = checkValue(compound.getFloat("ScaleX"), 0.5f, 1.5f);
		scaleY = checkValue(compound.getFloat("ScaleY"), 0.5f, 1.5f);
		scaleZ = checkValue(compound.getFloat("ScaleZ"), 0.5f, 1.5f);
		transX = checkValue(compound.getFloat("TransX"), -1.0f, 1.0f);
		transY = checkValue(compound.getFloat("TransY"), -1.0f, 1.0f);
		transZ = checkValue(compound.getFloat("TransZ"), -1.0f, 1.0f);
		notShared = compound.getBoolean("NotShared");
	}

	public void setScale(float x, float y) {
		scaleX = x;
		scaleZ = x;
		scaleY = y;
	}

	public void setScale(float x, float y, float z) {
		scaleX = ValueUtil.correctFloat(x, 0.5f, 1.5f);
		scaleY = ValueUtil.correctFloat(y, 0.5f, 1.5f);
		scaleZ = ValueUtil.correctFloat(z, 0.5f, 1.5f);
	}

	public void setTranslate(float transX, float transY, float transZ) {
		this.transX = transX;
		this.transY = transY;
		this.transZ = transZ;
	}

	@Override
	public String toString() {
		return "ScaleX: " + scaleX + " - ScaleY: " + scaleY + " - ScaleZ: " + scaleZ;
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setFloat("ScaleX", scaleX);
		compound.setFloat("ScaleY", scaleY);
		compound.setFloat("ScaleZ", scaleZ);
		compound.setFloat("TransX", transX);
		compound.setFloat("TransY", transY);
		compound.setFloat("TransZ", transZ);
		compound.setBoolean("NotShared", notShared);
		return compound;
	}
}
