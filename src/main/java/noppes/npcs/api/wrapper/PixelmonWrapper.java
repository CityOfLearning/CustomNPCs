//

//

package noppes.npcs.api.wrapper;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.IPixelmon;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.util.ValueUtil;

public class PixelmonWrapper<T extends EntityTameable> extends AnimalWrapper<T> implements IPixelmon {
	private NBTTagCompound compound;

	public PixelmonWrapper(final T pixelmon) {
		super(pixelmon);
		this.compound = null;
		pixelmon.writeEntityToNBT(this.compound = new NBTTagCompound());
	}

	public PixelmonWrapper(final T pixelmon, final NBTTagCompound compound) {
		this(pixelmon);
		this.compound = compound;
	}

	@Override
	public int getEV(final int type) {
		if (type == 0) {
			return this.compound.getInteger("EVHP");
		}
		if (type == 1) {
			return this.compound.getInteger("EVAttack");
		}
		if (type == 2) {
			return this.compound.getInteger("EVDefence");
		}
		if (type == 3) {
			return this.compound.getInteger("EVSpecialAttack");
		}
		if (type == 4) {
			return this.compound.getInteger("EVSpecialDefence");
		}
		if (type == 5) {
			return this.compound.getInteger("EVSpeed");
		}
		return -1;
	}

	@Override
	public int getHapiness() {
		return this.compound.getInteger("Friendship");
	}

	@Override
	public boolean getIsShiny() {
		return this.compound.getBoolean("IsShiny");
	}

	@Override
	public int getIV(final int type) {
		if (type == 0) {
			return this.compound.getInteger("IVHP");
		}
		if (type == 1) {
			return this.compound.getInteger("IVAttack");
		}
		if (type == 2) {
			return this.compound.getInteger("IVDefence");
		}
		if (type == 3) {
			return this.compound.getInteger("IVSpAtt");
		}
		if (type == 4) {
			return this.compound.getInteger("IVSpDef");
		}
		if (type == 5) {
			return this.compound.getInteger("IVSpeed");
		}
		return -1;
	}

	@Override
	public int getLevel() {
		return this.compound.getInteger("Level");
	}

	@Override
	public String getMove(final int slot) {
		if (!this.compound.hasKey("PixelmonMoveID" + slot)) {
			return null;
		}
		return PixelmonHelper.getAttackName(this.compound.getInteger("PixelmonMoveID" + slot));
	}

	@Override
	public int getNature() {
		return this.compound.getShort("Nature");
	}

	@Override
	public String getNickname() {
		return this.compound.getString("Nickname");
	}

	@Override
	public int getPokeball() {
		if (this.compound.hasKey("CaughtBall")) {
			return -1;
		}
		return this.compound.getInteger("CaughtBall");
	}

	@Override
	public int getSize() {
		return this.compound.getShort("Growth");
	}

	@Override
	public int getStat(final int type) {
		if (type == 0) {
			return this.compound.getInteger("StatsHP");
		}
		if (type == 1) {
			return this.compound.getInteger("StatsAttack");
		}
		if (type == 2) {
			return this.compound.getInteger("StatsDefence");
		}
		if (type == 3) {
			return this.compound.getInteger("StatsSpecialAttack");
		}
		if (type == 4) {
			return this.compound.getInteger("StatsSpecialDefence");
		}
		if (type == 5) {
			return this.compound.getInteger("StatsSpeed");
		}
		return -1;
	}

	@Override
	public boolean hasNickname() {
		return !this.getNickname().isEmpty();
	}

	@Override
	public void setEV(final int type, final int value) {
		if (type == 0) {
			this.compound.setInteger("EVHP", value);
		} else if (type == 1) {
			this.compound.setInteger("EVAttack", value);
		} else if (type == 2) {
			this.compound.setInteger("EVDefence", value);
		} else if (type == 3) {
			this.compound.setInteger("EVSpecialAttack", value);
		} else if (type == 4) {
			this.compound.setInteger("EVSpecialDefence", value);
		} else if (type == 5) {
			this.compound.setInteger("EVSpeed", value);
		}
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setHapiness(int value) {
		value = ValueUtil.CorrectInt(value, 0, 255);
		this.compound.setInteger("Friendship", value);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setIsShiny(final boolean bo) {
		this.compound.setBoolean("IsShiny", bo);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setIV(final int type, final int value) {
		if (type == 0) {
			this.compound.setInteger("IVHP", value);
		} else if (type == 1) {
			this.compound.setInteger("IVAttack", value);
		} else if (type == 2) {
			this.compound.setInteger("IVDefence", value);
		} else if (type == 3) {
			this.compound.setInteger("IVSpAtt", value);
		} else if (type == 4) {
			this.compound.setInteger("IVSpDef", value);
		} else if (type == 5) {
			this.compound.setInteger("IVSpeed", value);
		}
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setLevel(final int level) {
		this.compound.setInteger("Level", level);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setMove(int slot, final String move) {
		slot = ValueUtil.CorrectInt(slot, 0, 3);
		final int id = PixelmonHelper.getAttackID(move);
		this.compound.removeTag("PixelmonMovePP" + slot);
		this.compound.removeTag("PixelmonMovePPBase" + slot);
		if (id < 0) {
			this.compound.removeTag("PixelmonMoveID" + slot);
		} else {
			this.compound.setInteger("PixelmonMoveID" + slot, id);
		}
		int size = 0;
		for (int i = 0; i < 4; ++i) {
			if (this.compound.hasKey("PixelmonMoveID" + i)) {
				++size;
			}
		}
		this.compound.setInteger("PixelmonNumberMoves", size);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setNature(final int type) {
		this.compound.setShort("Nature", (short) type);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setNickname(final String name) {
		this.compound.setString("Nickname", name);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setPokeball(final int type) {
		this.compound.setInteger("CaughtBall", type);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setSize(final int type) {
		this.compound.setShort("Growth", (short) type);
		entity.readEntityFromNBT(this.compound);
	}

	@Override
	public void setStat(final int type, final int value) {
		if (type == 0) {
			this.compound.setInteger("StatsHP", value);
		} else if (type == 1) {
			this.compound.setInteger("StatsAttack", value);
		} else if (type == 2) {
			this.compound.setInteger("StatsDefence", value);
		} else if (type == 3) {
			this.compound.setInteger("StatsSpecialAttack", value);
		} else if (type == 4) {
			this.compound.setInteger("StatsSpecialDefence", value);
		} else if (type == 5) {
			this.compound.setInteger("StatsSpeed", value);
		}
		entity.readEntityFromNBT(this.compound);
	}
}
