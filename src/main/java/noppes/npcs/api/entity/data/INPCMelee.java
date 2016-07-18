//

//

package noppes.npcs.api.entity.data;

public interface INPCMelee {
	int getDelay();

	int getEffectStrength();

	int getEffectTime();

	int getEffectType();

	int getKnockback();

	int getRange();

	int getStrength();

	void setDelay(final int p0);

	void setEffect(final int p0, final int p1, final int p2);

	void setKnockback(final int p0);

	void setRange(final int p0);

	void setStrength(final int p0);
}
