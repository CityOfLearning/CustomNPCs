//

//

package noppes.npcs.api.entity;

import net.minecraft.entity.passive.EntityTameable;

public interface IPixelmon<T extends EntityTameable> extends IAnimal<T> {
	int getEV(final int p0);

	int getHapiness();

	boolean getIsShiny();

	int getIV(final int p0);

	int getLevel();

	@Override
	T getMCEntity();

	String getMove(final int p0);

	int getNature();

	String getNickname();

	int getPokeball();

	int getSize();

	int getStat(final int p0);

	boolean hasNickname();

	void setEV(final int p0, final int p1);

	void setHapiness(final int p0);

	void setIsShiny(final boolean p0);

	void setIV(final int p0, final int p1);

	void setLevel(final int p0);

	void setMove(final int p0, final String p1);

	void setNature(final int p0);

	void setNickname(final String p0);

	void setPokeball(final int p0);

	void setSize(final int p0);

	void setStat(final int p0, final int p1);
}
