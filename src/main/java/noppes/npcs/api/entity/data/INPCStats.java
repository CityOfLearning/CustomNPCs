//

//

package noppes.npcs.api.entity.data;

public interface INPCStats {
	int getAggroRange();

	int getCombatRegen();

	int getCreatureType();

	int getHealthRegen();

	boolean getHideDeadBody();

	boolean getImmune(final int p0);

	int getMaxHealth();

	INPCMelee getMelee();

	INPCRanged getRanged();

	float getResistance(final int p0);

	int getRespawnTime();

	int getRespawnType();

	void setAggroRange(final int p0);

	void setCombatRegen(final int p0);

	void setCreatureType(final int p0);

	void setHealthRegen(final int p0);

	void setHideDeadBody(final boolean p0);

	void setImmune(final int p0, final boolean p1);

	void setMaxHealth(final int p0);

	void setResistance(final int p0, final float p1);

	void setRespawnTime(final int p0);

	void setRespawnType(final int p0);
}
