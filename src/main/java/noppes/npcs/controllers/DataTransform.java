//

//

package noppes.npcs.controllers;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class DataTransform {
	public NBTTagCompound display;
	public NBTTagCompound ai;
	public NBTTagCompound advanced;
	public NBTTagCompound inv;
	public NBTTagCompound stats;
	public NBTTagCompound role;
	public NBTTagCompound job;
	public boolean hasDisplay;
	public boolean hasAi;
	public boolean hasAdvanced;
	public boolean hasInv;
	public boolean hasStats;
	public boolean hasRole;
	public boolean hasJob;
	public boolean isActive;
	private EntityNPCInterface npc;
	public boolean editingModus;

	public DataTransform(final EntityNPCInterface npc) {
		editingModus = false;
		this.npc = npc;
	}

	public NBTTagCompound getAdvanced() {
		final int jopType = npc.advanced.job;
		final int roleType = npc.advanced.role;
		npc.advanced.job = 0;
		npc.advanced.role = 0;
		final NBTTagCompound compound = npc.advanced.writeToNBT(new NBTTagCompound());
		compound.removeTag("Role");
		compound.removeTag("NpcJob");
		npc.advanced.job = jopType;
		npc.advanced.role = roleType;
		return compound;
	}

	public NBTTagCompound getDisplay() {
		final NBTTagCompound compound = npc.display.writeToNBT(new NBTTagCompound());
		if (npc instanceof EntityCustomNpc) {
			compound.setTag("ModelData", ((EntityCustomNpc) npc).modelData.writeToNBT());
		}
		return compound;
	}

	public NBTTagCompound getJob() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("NpcJob", npc.advanced.job);
		if ((npc.advanced.job != 0) && (npc.jobInterface != null)) {
			npc.jobInterface.writeToNBT(compound);
		}
		return compound;
	}

	public NBTTagCompound getRole() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Role", npc.advanced.role);
		if ((npc.advanced.role != 0) && (npc.roleInterface != null)) {
			npc.roleInterface.writeToNBT(compound);
		}
		return compound;
	}

	public boolean isValid() {
		return hasAdvanced || hasAi || hasDisplay || hasInv || hasStats || hasJob || hasRole;
	}

	public NBTTagCompound processAdvanced(NBTTagCompound compoundAdv, NBTTagCompound compoundRole,
			NBTTagCompound compoundJob) {
		if (hasAdvanced) {
			compoundAdv = advanced;
		}
		if (hasRole) {
			compoundRole = role;
		}
		if (hasJob) {
			compoundJob = job;
		}
		Set<String> names = compoundRole.getKeySet();
		for (final String name : names) {
			compoundAdv.setTag(name, compoundRole.getTag(name));
		}
		names = compoundJob.getKeySet();
		for (final String name : names) {
			compoundAdv.setTag(name, compoundJob.getTag(name));
		}
		return compoundAdv;
	}

	public void readOptions(final NBTTagCompound compound) {
		final boolean hadDisplay = hasDisplay;
		final boolean hadAI = hasAi;
		final boolean hadAdvanced = hasAdvanced;
		final boolean hadInv = hasInv;
		final boolean hadStats = hasStats;
		final boolean hadRole = hasRole;
		final boolean hadJob = hasJob;
		hasDisplay = compound.getBoolean("TransformHasDisplay");
		hasAi = compound.getBoolean("TransformHasAI");
		hasAdvanced = compound.getBoolean("TransformHasAdvanced");
		hasInv = compound.getBoolean("TransformHasInv");
		hasStats = compound.getBoolean("TransformHasStats");
		hasRole = compound.getBoolean("TransformHasRole");
		hasJob = compound.getBoolean("TransformHasJob");
		editingModus = compound.getBoolean("TransformEditingModus");
		if (hasDisplay && !hadDisplay) {
			display = getDisplay();
		}
		if (hasAi && !hadAI) {
			ai = npc.ai.writeToNBT(new NBTTagCompound());
		}
		if (hasStats && !hadStats) {
			stats = npc.stats.writeToNBT(new NBTTagCompound());
		}
		if (hasInv && !hadInv) {
			inv = npc.inventory.writeEntityToNBT(new NBTTagCompound());
		}
		if (hasAdvanced && !hadAdvanced) {
			advanced = getAdvanced();
		}
		if (hasJob && !hadJob) {
			job = getJob();
		}
		if (hasRole && !hadRole) {
			role = getRole();
		}
	}

	public void readToNBT(final NBTTagCompound compound) {
		isActive = compound.getBoolean("TransformIsActive");
		readOptions(compound);
		display = (hasDisplay ? compound.getCompoundTag("TransformDisplay") : getDisplay());
		ai = (hasAi ? compound.getCompoundTag("TransformAI") : npc.ai.writeToNBT(new NBTTagCompound()));
		advanced = (hasAdvanced ? compound.getCompoundTag("TransformAdvanced") : getAdvanced());
		inv = (hasInv ? compound.getCompoundTag("TransformInv") : npc.inventory.writeEntityToNBT(new NBTTagCompound()));
		stats = (hasStats ? compound.getCompoundTag("TransformStats") : npc.stats.writeToNBT(new NBTTagCompound()));
		job = (hasJob ? compound.getCompoundTag("TransformJob") : getJob());
		role = (hasRole ? compound.getCompoundTag("TransformRole") : getRole());
	}

	public void transform(final boolean isActive) {
		if (this.isActive == isActive) {
			return;
		}
		if (hasDisplay) {
			final NBTTagCompound compound = getDisplay();
			npc.display.readToNBT(NBTTags.NBTMerge(compound, display));
			if (npc instanceof EntityCustomNpc) {
				((EntityCustomNpc) npc).modelData.readFromNBT(
						NBTTags.NBTMerge(compound.getCompoundTag("ModelData"), display.getCompoundTag("ModelData")));
			}
			display = compound;
		}
		if (hasStats) {
			final NBTTagCompound compound = npc.stats.writeToNBT(new NBTTagCompound());
			npc.stats.readToNBT(NBTTags.NBTMerge(compound, stats));
			stats = compound;
		}
		if (hasAdvanced || hasJob || hasRole) {
			final NBTTagCompound compoundAdv = getAdvanced();
			final NBTTagCompound compoundRole = getRole();
			final NBTTagCompound compoundJob = getJob();
			final NBTTagCompound compound2 = processAdvanced(compoundAdv, compoundRole, compoundJob);
			npc.advanced.readToNBT(compound2);
			if ((npc.advanced.role != 0) && (npc.roleInterface != null)) {
				npc.roleInterface.readFromNBT(NBTTags.NBTMerge(compoundRole, compound2));
			}
			if ((npc.advanced.job != 0) && (npc.jobInterface != null)) {
				npc.jobInterface.readFromNBT(NBTTags.NBTMerge(compoundJob, compound2));
			}
			if (hasAdvanced) {
				advanced = compoundAdv;
			}
			if (hasRole) {
				role = compoundRole;
			}
			if (hasJob) {
				job = compoundJob;
			}
		}
		if (hasAi) {
			final NBTTagCompound compound = npc.ai.writeToNBT(new NBTTagCompound());
			npc.ai.readToNBT(NBTTags.NBTMerge(compound, ai));
			ai = compound;
			npc.setCurrentAnimation(0);
		}
		if (hasInv) {
			final NBTTagCompound compound = npc.inventory.writeEntityToNBT(new NBTTagCompound());
			npc.inventory.readEntityFromNBT(NBTTags.NBTMerge(compound, inv));
			inv = compound;
		}
		npc.updateAI = true;
		this.isActive = isActive;
		npc.updateClient = true;
	}

	public Object writeOptions(final NBTTagCompound compound) {
		compound.setBoolean("TransformHasDisplay", hasDisplay);
		compound.setBoolean("TransformHasAI", hasAi);
		compound.setBoolean("TransformHasAdvanced", hasAdvanced);
		compound.setBoolean("TransformHasInv", hasInv);
		compound.setBoolean("TransformHasStats", hasStats);
		compound.setBoolean("TransformHasRole", hasRole);
		compound.setBoolean("TransformHasJob", hasJob);
		compound.setBoolean("TransformEditingModus", editingModus);
		return compound;
	}

	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setBoolean("TransformIsActive", isActive);
		writeOptions(compound);
		if (hasDisplay) {
			compound.setTag("TransformDisplay", display);
		}
		if (hasAi) {
			compound.setTag("TransformAI", ai);
		}
		if (hasAdvanced) {
			compound.setTag("TransformAdvanced", advanced);
		}
		if (hasInv) {
			compound.setTag("TransformInv", inv);
		}
		if (hasStats) {
			compound.setTag("TransformStats", stats);
		}
		if (hasRole) {
			compound.setTag("TransformRole", role);
		}
		if (hasJob) {
			compound.setTag("TransformJob", job);
		}
		return compound;
	}
}