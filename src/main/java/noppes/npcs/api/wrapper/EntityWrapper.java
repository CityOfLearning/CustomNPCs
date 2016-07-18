//

//

package noppes.npcs.api.wrapper;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.data.IData;

public class EntityWrapper<T extends Entity> implements IEntity {
	protected T entity;
	private Map<String, Object> tempData;
	private WorldWrapper worldWrapper;
	private final IData tempdata;
	private final IData storeddata;

	public EntityWrapper(final T entity) {
		this.tempData = new HashMap<String, Object>();
		this.tempdata = new IData() {
			@Override
			public void clear() {
				EntityWrapper.this.tempData.clear();
			}

			@Override
			public Object get(final String key) {
				return EntityWrapper.this.tempData.get(key);
			}

			@Override
			public boolean has(final String key) {
				return EntityWrapper.this.tempData.containsKey(key);
			}

			@Override
			public void put(final String key, final Object value) {
				EntityWrapper.this.tempData.put(key, value);
			}

			@Override
			public void remove(final String key) {
				EntityWrapper.this.tempData.remove(key);
			}
		};
		this.storeddata = new IData() {
			@Override
			public void clear() {
				EntityWrapper.this.entity.getEntityData().removeTag("CNPCStoredData");
			}

			@Override
			public Object get(final String key) {
				final NBTTagCompound compound = this.getStoredCompound();
				if (!compound.hasKey(key)) {
					return null;
				}
				final NBTBase base = compound.getTag(key);
				if (base instanceof NBTBase.NBTPrimitive) {
					return ((NBTBase.NBTPrimitive) base).getDouble();
				}
				return ((NBTTagString) base).getString();
			}

			private NBTTagCompound getStoredCompound() {
				NBTTagCompound compound = EntityWrapper.this.entity.getEntityData().getCompoundTag("CNPCStoredData");
				if (compound == null) {
					EntityWrapper.this.entity.getEntityData().setTag("CNPCStoredData", compound = new NBTTagCompound());
				}
				return compound;
			}

			@Override
			public boolean has(final String key) {
				return this.getStoredCompound().hasKey(key);
			}

			@Override
			public void put(final String key, final Object value) {
				final NBTTagCompound compound = this.getStoredCompound();
				if (value instanceof Number) {
					compound.setDouble(key, ((Number) value).doubleValue());
				} else if (value instanceof String) {
					compound.setString(key, (String) value);
				}
				this.saveStoredCompound(compound);
			}

			@Override
			public void remove(final String key) {
				final NBTTagCompound compound = this.getStoredCompound();
				compound.removeTag(key);
				this.saveStoredCompound(compound);
			}

			private void saveStoredCompound(final NBTTagCompound compound) {
				EntityWrapper.this.entity.getEntityData().setTag("CNPCStoredData", compound);
			}
		};
		this.entity = entity;
		this.worldWrapper = new WorldWrapper(entity.worldObj);
	}

	@Override
	public void despawn() {
		this.entity.isDead = true;
	}

	@Override
	public void dropItem(final IItemStack item) {
		this.entity.entityDropItem(item.getMCItemStack(), 0.0f);
	}

	@Override
	public void extinguish() {
		this.entity.extinguish();
	}

	@Override
	public long getAge() {
		return this.entity.ticksExisted;
	}

	@Override
	public int getBlockX() {
		return MathHelper.floor_double(this.entity.posZ);
	}

	@Override
	public int getBlockY() {
		return MathHelper.floor_double(this.entity.posY);
	}

	@Override
	public int getBlockZ() {
		return MathHelper.floor_double(this.entity.posZ);
	}

	@Override
	public T getMCEntity() {
		return this.entity;
	}

	@Override
	public IEntity getMount() {
		return NpcAPI.Instance().getIEntity(this.entity.ridingEntity);
	}

	@Override
	public IEntity getRider() {
		return NpcAPI.Instance().getIEntity(this.entity.riddenByEntity);
	}

	@Override
	public float getRotation() {
		return this.entity.rotationYaw;
	}

	@Override
	public IData getStoreddata() {
		return this.storeddata;
	}

	@Override
	public IData getTempdata() {
		return this.tempdata;
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public String getTypeName() {
		return EntityList.getEntityString(this.entity);
	}

	@Override
	public String getUUID() {
		return this.entity.getUniqueID().toString();
	}

	@Override
	public IWorld getWorld() {
		if (this.entity.worldObj != this.worldWrapper.world) {
			this.worldWrapper = new WorldWrapper(this.entity.worldObj);
		}
		return this.worldWrapper;
	}

	@Override
	public double getX() {
		return this.entity.posX;
	}

	@Override
	public double getY() {
		return this.entity.posY;
	}

	@Override
	public double getZ() {
		return this.entity.posZ;
	}

	@Override
	public boolean inFire() {
		return this.entity.isInsideOfMaterial(Material.fire);
	}

	@Override
	public boolean inLava() {
		return this.entity.isInsideOfMaterial(Material.lava);
	}

	@Override
	public boolean inWater() {
		return this.entity.isInsideOfMaterial(Material.water);
	}

	@Override
	public boolean isAlive() {
		return this.entity.isEntityAlive();
	}

	@Override
	public boolean isBurning() {
		return this.entity.isBurning();
	}

	@Override
	public boolean isSneaking() {
		return this.entity.isSneaking();
	}

	@Override
	public boolean isSprinting() {
		return this.entity.isSprinting();
	}

	@Override
	public void knockback(final int power, final float direction) {
		final float v = (direction * 3.1415927f) / 180.0f;
		this.entity.addVelocity(-MathHelper.sin(v) * power, 0.1 + (power * 0.04f), MathHelper.cos(v) * power);
		final Entity entity = this.entity;
		entity.motionX *= 0.6;
		final Entity entity2 = this.entity;
		entity2.motionZ *= 0.6;
		this.entity.attackEntityFrom(DamageSource.outOfWorld, 1.0E-4f);
	}

	@Override
	public void setBurning(final int ticks) {
		this.entity.setFire(ticks);
	}

	@Override
	public void setMount(final IEntity entity) {
		if (entity == null) {
			this.entity.mountEntity((Entity) null);
		} else {
			this.entity.mountEntity(entity.getMCEntity());
		}
	}

	@Override
	public void setPosition(final double x, final double y, final double z) {
		this.entity.setPosition(x, y, z);
	}

	@Override
	public void setRider(final IEntity entity) {
		if (entity != null) {
			entity.getMCEntity().mountEntity(this.entity);
		} else if (this.entity.riddenByEntity != null) {
			this.entity.riddenByEntity.mountEntity((Entity) null);
		}
	}

	@Override
	public void setRotation(final float rotation) {
		this.entity.rotationYaw = rotation;
	}

	@Override
	public void setX(final double x) {
		this.entity.posX = x;
	}

	@Override
	public void setY(final double y) {
		this.entity.posY = y;
	}

	@Override
	public void setZ(final double z) {
		this.entity.posZ = z;
	}

	@Override
	public boolean typeOf(final int type) {
		return type == 0;
	}
}
