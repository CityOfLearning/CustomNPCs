//

//

package noppes.npcs.roles.companion;

import net.minecraft.nbt.NBTTagCompound;

public class CompanionFarmer extends CompanionJobInterface {
	public boolean isStanding;

	public CompanionFarmer() {
		isStanding = false;
	}

	@Override
	public NBTTagCompound getNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("CompanionFarmerStanding", isStanding);
		return compound;
	}

	@Override
	public boolean isSelfSufficient() {
		return isStanding;
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void setNBT(final NBTTagCompound compound) {
		isStanding = compound.getBoolean("CompanionFarmerStanding");
	}
}
