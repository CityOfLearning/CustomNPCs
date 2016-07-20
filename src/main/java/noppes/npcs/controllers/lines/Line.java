//

//

package noppes.npcs.controllers.lines;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Line {
	public String text;
	public String sound;
	public boolean hideText;

	public Line() {
		text = "";
		sound = "";
		hideText = false;
	}

	public Line(final String text) {
		this.text = "";
		sound = "";
		hideText = false;
		this.text = text;
	}

	public Line copy() {
		final Line line = new Line(text);
		line.sound = sound;
		line.hideText = hideText;
		return line;
	}

	public Line formatTarget(final EntityLivingBase entity) {
		if (entity == null) {
			return this;
		}
		final Line line = copy();
		if (entity instanceof EntityPlayer) {
			line.text = line.text.replace("@target", ((EntityPlayer) entity).getDisplayNameString());
		} else {
			line.text = line.text.replace("@target", entity.getName());
		}
		return line;
	}
}
