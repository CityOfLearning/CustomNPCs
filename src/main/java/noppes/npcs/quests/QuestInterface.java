
package noppes.npcs.quests;

import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class QuestInterface {
	public int questId;

	public abstract Vector<String> getQuestLogStatus(EntityPlayer p0);

	public abstract void handleComplete(EntityPlayer p0);

	public abstract boolean isCompleted(EntityPlayer p0);

	public abstract void readEntityFromNBT(NBTTagCompound p0);

	public abstract void writeEntityToNBT(NBTTagCompound p0);
}
