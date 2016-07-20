//

//

package noppes.npcs.api.entity.data;

public interface INPCStats {
	int getAggroRange();

	int getCombatRegen();

	int getCreatureType();

	int getHealthRegen();

	boolean getHideDeadBody();

	boolean getImmune(int p0);

	int getMaxHealth();

	INPCMelee getMelee();

	INPCRanged getRanged();

	float getResistance(int p0);

	int getRespawnTime();

	int getRespawnType();

	void setAggroRange(int p0);

	void setCombatRegen(int p0);

	void setCreatureType(int p0);

	void setHealthRegen(int p0);

	void setHideDeadBody(boolean p0);

	void setImmune(int p0, boolean p1);

	void setMaxHealth(int p0);

	void setResistance(int p0, float p1);

	void setRespawnTime(int p0);

	void setRespawnType(int p0);
}
