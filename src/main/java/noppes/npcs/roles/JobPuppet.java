
package noppes.npcs.roles;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class JobPuppet extends JobInterface {
	public static class PartConfig {
		public float rotationX;
		public float rotationY;
		public float rotationZ;
		public boolean disabled;

		public PartConfig() {
			rotationX = 0.0f;
			rotationY = 0.0f;
			rotationZ = 0.0f;
			disabled = false;
		}

		public void readNBT(NBTTagCompound compound) {
			rotationX = ValueUtil.correctFloat(compound.getFloat("RotationX"), -0.5f, 0.5f);
			rotationY = ValueUtil.correctFloat(compound.getFloat("RotationY"), -0.5f, 0.5f);
			rotationZ = ValueUtil.correctFloat(compound.getFloat("RotationZ"), -0.5f, 0.5f);
			disabled = compound.getBoolean("Disabled");
		}

		public NBTTagCompound writeNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setFloat("RotationX", rotationX);
			compound.setFloat("RotationY", rotationY);
			compound.setFloat("RotationZ", rotationZ);
			compound.setBoolean("Disabled", disabled);
			return compound;
		}
	}

	public PartConfig head;
	public PartConfig larm;
	public PartConfig rarm;
	public PartConfig body;
	public PartConfig lleg;
	public PartConfig rleg;
	public boolean whileStanding;
	public boolean whileAttacking;

	public boolean whileMoving;

	public JobPuppet(EntityNPCInterface npc) {
		super(npc);
		head = new PartConfig();
		larm = new PartConfig();
		rarm = new PartConfig();
		body = new PartConfig();
		lleg = new PartConfig();
		rleg = new PartConfig();
		whileStanding = true;
		whileAttacking = false;
		whileMoving = false;
	}

	@Override
	public boolean aiShouldExecute() {
		return false;
	}

	@Override
	public void delete() {
	}

	public boolean isActive() {
		return npc.isEntityAlive() && ((whileAttacking && npc.isAttacking()) || (whileMoving && npc.isWalking())
				|| (whileStanding && !npc.isWalking()));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		head.readNBT(compound.getCompoundTag("PuppetHead"));
		larm.readNBT(compound.getCompoundTag("PuppetLArm"));
		rarm.readNBT(compound.getCompoundTag("PuppetRArm"));
		body.readNBT(compound.getCompoundTag("PuppetBody"));
		lleg.readNBT(compound.getCompoundTag("PuppetLLeg"));
		rleg.readNBT(compound.getCompoundTag("PuppetRLeg"));
		whileStanding = compound.getBoolean("PuppetStanding");
		whileAttacking = compound.getBoolean("PuppetAttacking");
		whileMoving = compound.getBoolean("PuppetMoving");
	}

	@Override
	public void reset() {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("PuppetHead", head.writeNBT());
		compound.setTag("PuppetLArm", larm.writeNBT());
		compound.setTag("PuppetRArm", rarm.writeNBT());
		compound.setTag("PuppetBody", body.writeNBT());
		compound.setTag("PuppetLLeg", lleg.writeNBT());
		compound.setTag("PuppetRLeg", rleg.writeNBT());
		compound.setBoolean("PuppetStanding", whileStanding);
		compound.setBoolean("PuppetAttacking", whileAttacking);
		compound.setBoolean("PuppetMoving", whileMoving);
		return compound;
	}
}
