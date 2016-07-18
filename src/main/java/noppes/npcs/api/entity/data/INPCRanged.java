//

//

package noppes.npcs.api.entity.data;

public interface INPCRanged {
	boolean getAccelerate();

	int getAccuracy();

	int getBurst();

	int getBurstDelay();

	int getDelayMax();

	int getDelayMin();

	int getDelayRNG();

	int getEffectStrength();

	int getEffectTime();

	int getEffectType();

	int getExplodeSize();

	int getFireType();

	boolean getGlows();

	boolean getHasAimAnimation();

	boolean getHasGravity();

	int getKnockback();

	int getMeleeRange();

	int getParticle();

	int getRange();

	boolean getRender3D();

	int getShotCount();

	int getSize();

	String getSound(final int p0);

	int getSpeed();

	boolean getSpins();

	boolean getSticks();

	int getStrength();

	void setAccelerate(final boolean p0);

	void setAccuracy(final int p0);

	void setBurst(final int p0);

	void setBurstDelay(final int p0);

	void setDelay(final int p0, final int p1);

	void setEffect(final int p0, final int p1, final int p2);

	void setExplodeSize(final int p0);

	void setFireType(final int p0);

	void setGlows(final boolean p0);

	void setHasAimAnimation(final boolean p0);

	void setHasGravity(final boolean p0);

	void setKnockback(final int p0);

	void setMeleeRange(final int p0);

	void setParticle(final int p0);

	void setRange(final int p0);

	void setRender3D(final boolean p0);

	void setShotCount(final int p0);

	void setSize(final int p0);

	void setSound(final int p0, final String p1);

	void setSpeed(final int p0);

	void setSpins(final boolean p0);

	void setSticks(final boolean p0);

	void setStrength(final int p0);
}
