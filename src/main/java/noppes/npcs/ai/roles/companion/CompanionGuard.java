
package noppes.npcs.ai.roles.companion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.entity.EntityNPCInterface;

public class CompanionGuard extends CompanionJobInterface {
	public boolean isStanding;

	public CompanionGuard() {
		isStanding = false;
	}

	@Override
	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("CompanionGuardStanding", isStanding);
		return compound;
	}

	public boolean isEntityApplicable(Entity entity) {
		return !(entity instanceof EntityPlayer) && !(entity instanceof EntityNPCInterface)
				&& !(entity instanceof EntityCreeper) && (entity instanceof IMob);
	}

	@Override
	public boolean isSelfSufficient() {
		return isStanding;
	}

	@Override
	public void setNBT(NBTTagCompound compound) {
		isStanding = compound.getBoolean("CompanionGuardStanding");
	}
}
