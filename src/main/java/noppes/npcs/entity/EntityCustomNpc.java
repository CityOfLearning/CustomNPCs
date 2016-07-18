//

//

package noppes.npcs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.constants.EnumParts;

public class EntityCustomNpc extends EntityNPCFlying {
	public ModelData modelData;

	public EntityCustomNpc(final World world) {
		super(world);
		modelData = new ModelData();
	}

	@Override
	public void mountEntity(final Entity par1Entity) {
		super.mountEntity(par1Entity);
		updateHitbox();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isRemote()) {
			final ModelPartData particles = modelData.getPartData(EnumParts.PARTICLES);
			if ((particles != null) && !isKilled()) {
				CustomNpcs.proxy.spawnParticle(this, "ModelData", modelData, particles);
			}
			final EntityLivingBase entity = modelData.getEntity(this);
			if (entity != null) {
				try {
					entity.onUpdate();
				} catch (Exception ex) {
				}
				EntityUtil.Copy(this, entity);
			}
		}
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound compound) {
		if (compound.hasKey("NpcModelData")) {
			modelData.readFromNBT(compound.getCompoundTag("NpcModelData"));
		}
		super.readEntityFromNBT(compound);
	}

	@Override
	public void updateHitbox() {
		final Entity entity = modelData.getEntity(this);
		if ((modelData == null) || (entity == null)) {
			baseHeight = (1.9f - modelData.getBodyY())
					+ ((modelData.getPartConfig(EnumParts.HEAD).scaleY - 1.0f) / 2.0f);
			super.updateHitbox();
		} else {
			if (entity instanceof EntityNPCInterface) {
				((EntityNPCInterface) entity).updateHitbox();
			}
			width = (entity.width / 5.0f) * display.getSize();
			height = (entity.height / 5.0f) * display.getSize();
			if (width < 0.1f) {
				width = 0.1f;
			}
			if (height < 0.1f) {
				height = 0.1f;
			}
			setPosition(posX, posY, posZ);
		}
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setTag("NpcModelData", modelData.writeToNBT());
	}
}
