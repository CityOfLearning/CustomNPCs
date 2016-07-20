//

//

package noppes.npcs.entity;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.entity.data.DataRanged;
import noppes.npcs.util.IProjectileCallback;

public class EntityProjectile extends EntityThrowable {
	private BlockPos tilePos;
	private Block inTile;
	protected boolean inGround;
	private int inData;
	public int throwableShake;
	public int arrowShake;
	public boolean canBePickedUp;
	public boolean destroyedOnEntityHit;
	private EntityLivingBase thrower;
	private EntityNPCInterface npc;
	private String throwerName;
	private int ticksInGround;
	public int ticksInAir;
	private double accelerationX;
	private double accelerationY;
	private double accelerationZ;
	public float damage;
	public int punch;
	public boolean accelerate;
	public boolean explosiveDamage;
	public int explosiveRadius;
	public int effect;
	public int duration;
	public int amplify;
	public IProjectileCallback callback;

	public EntityProjectile(World par1World) {
		super(par1World);
		tilePos = BlockPos.ORIGIN;
		inGround = false;
		inData = 0;
		throwableShake = 0;
		arrowShake = 0;
		canBePickedUp = false;
		destroyedOnEntityHit = true;
		throwerName = null;
		ticksInAir = 0;
		damage = 5.0f;
		punch = 0;
		accelerate = false;
		explosiveDamage = true;
		explosiveRadius = 0;
		effect = 0;
		duration = 5;
		amplify = 0;
		setSize(0.25f, 0.25f);
	}

	public EntityProjectile(World par1World, EntityLivingBase par2EntityLiving, ItemStack item, boolean isNPC) {
		super(par1World);
		tilePos = BlockPos.ORIGIN;
		inGround = false;
		inData = 0;
		throwableShake = 0;
		arrowShake = 0;
		canBePickedUp = false;
		destroyedOnEntityHit = true;
		throwerName = null;
		ticksInAir = 0;
		damage = 5.0f;
		punch = 0;
		accelerate = false;
		explosiveDamage = true;
		explosiveRadius = 0;
		effect = 0;
		duration = 5;
		amplify = 0;
		thrower = par2EntityLiving;
		if (thrower != null) {
			throwerName = thrower.getUniqueID().toString();
		}
		setThrownItem(item);
		dataWatcher.updateObject(27, (byte) ((getItem() == Items.arrow) ? 1 : 0));
		setSize(dataWatcher.getWatchableObjectInt(23) / 10, dataWatcher.getWatchableObjectInt(23) / 10);
		setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + par2EntityLiving.getEyeHeight(),
				par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180.0f) * 3.1415927f) * 0.1f;
		posY -= 0.10000000149011612;
		posZ -= MathHelper.sin((rotationYaw / 180.0f) * 3.1415927f) * 0.1f;
		setPosition(posX, posY, posZ);
		if (isNPC) {
			npc = (EntityNPCInterface) thrower;
			getStatProperties(npc.stats.ranged);
		}
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObjectByDataType(21, 5);
		dataWatcher.addObject(22, (-1));
		dataWatcher.addObject(23, 10);
		dataWatcher.addObject(24, (byte) 0);
		dataWatcher.addObject(25, 10);
		dataWatcher.addObject(26, (byte) 0);
		dataWatcher.addObject(27, (byte) 0);
		dataWatcher.addObject(28, (byte) 0);
		dataWatcher.addObject(29, (byte) 0);
		dataWatcher.addObject(30, (byte) 0);
		dataWatcher.addObject(31, (byte) 0);
	}

	public float getAngleForXYZ(double varX, double varY, double varZ, double horiDist, boolean arc) {
		float g = getGravityVelocity();
		float var1 = getSpeed() * getSpeed();
		double var2 = g * horiDist;
		double var3 = (g * horiDist * horiDist) + (2.0 * varY * var1);
		double var4 = (var1 * var1) - (g * var3);
		if (var4 < 0.0) {
			return 30.0f;
		}
		float var5 = arc ? (var1 + MathHelper.sqrt_double(var4)) : (var1 - MathHelper.sqrt_double(var4));
		float var6 = (float) ((Math.atan2(var5, var2) * 180.0) / 3.141592653589793);
		return var6;
	}

	@Override
	public float getBrightness(float par1) {
		return (dataWatcher.getWatchableObjectByte(24) == 1) ? 1.0f : super.getBrightness(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float par1) {
		return (dataWatcher.getWatchableObjectByte(24) == 1) ? 15728880 : super.getBrightnessForRender(par1);
	}

	@Override
	public IChatComponent getDisplayName() {
		if (getItemDisplay() != null) {
			return new ChatComponentTranslation(getItemDisplay().getDisplayName(), new Object[0]);
		}
		return super.getDisplayName();
	}

	private Item getItem() {
		ItemStack item = getItemDisplay();
		if (item == null) {
			return null;
		}
		return item.getItem();
	}

	public ItemStack getItemDisplay() {
		return dataWatcher.getWatchableObjectItemStack(21);
	}

	protected float getMotionFactor() {
		return accelerate ? 0.95f : 1.0f;
	}

	private int getPotionColor(int p) {
		switch (p) {
		case 2: {
			return 32660;
		}
		case 3: {
			return 32660;
		}
		case 4: {
			return 32696;
		}
		case 5: {
			return 32698;
		}
		case 6: {
			return 32732;
		}
		case 7: {
			return 15;
		}
		case 8: {
			return 32732;
		}
		default: {
			return 0;
		}
		}
	}

	public float getSpeed() {
		return dataWatcher.getWatchableObjectInt(25) / 10.0f;
	}

	public void getStatProperties(DataRanged stats) {
		damage = stats.getStrength();
		punch = stats.getKnockback();
		accelerate = stats.getAccelerate();
		explosiveRadius = stats.getExplodeSize();
		effect = stats.getEffectType();
		duration = stats.getEffectTime();
		amplify = stats.getEffectStrength();
		setParticleEffect(stats.getParticle());
		dataWatcher.updateObject(23, stats.getSize());
		dataWatcher.updateObject(24, (byte) (stats.getGlows() ? 1 : 0));
		setSpeed(stats.getSpeed());
		setHasGravity(stats.getHasGravity());
		setIs3D(stats.getRender3D());
		setRotating(stats.getSpins());
		setStickInWall(stats.getSticks());
	}

	@Override
	public EntityLivingBase getThrower() {
		if ((throwerName == null) || throwerName.isEmpty()) {
			return null;
		}
		try {
			UUID uuid = UUID.fromString(throwerName);
			if ((thrower == null) && (uuid != null)) {
				thrower = worldObj.getPlayerEntityByUUID(uuid);
			}
		} catch (IllegalArgumentException ex) {
		}
		return thrower;
	}

	public boolean glows() {
		return dataWatcher.getWatchableObjectByte(24) == 1;
	}

	public boolean hasGravity() {
		return dataWatcher.getWatchableObjectByte(26) == 1;
	}

	public boolean is3D() {
		return (dataWatcher.getWatchableObjectByte(28) == 1) || isBlock();
	}

	public boolean isArrow() {
		return dataWatcher.getWatchableObjectByte(27) == 1;
	}

	public boolean isBlock() {
		ItemStack item = getItemDisplay();
		return (item != null) && (item.getItem() instanceof ItemBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double par1) {
		double d1 = getEntityBoundingBox().getAverageEdgeLength() * 4.0;
		d1 *= 64.0;
		return par1 < (d1 * d1);
	}

	public boolean isRotating() {
		return dataWatcher.getWatchableObjectByte(29) == 1;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
		if (worldObj.isRemote || !canBePickedUp || !inGround || (arrowShake > 0)) {
			return;
		}
		if (par1EntityPlayer.inventory.addItemStackToInventory(getItemDisplay())) {
			inGround = false;
			playSound("random.pop", 0.2f, (((rand.nextFloat() - rand.nextFloat()) * 0.7f) + 1.0f) * 2.0f);
			par1EntityPlayer.onItemPickup(this, 1);
			setDead();
		}
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		if (callback != null) {
			BlockPos pos = null;
			if (movingobjectposition.entityHit != null) {
				pos = movingobjectposition.entityHit.getPosition();
			} else {
				pos = movingobjectposition.getBlockPos();
			}
			if (pos == BlockPos.ORIGIN) {
				pos = new BlockPos(movingobjectposition.hitVec);
			}
			if (callback.onImpact(this, pos, movingobjectposition.entityHit)) {
				return;
			}
		}
		if (movingobjectposition.entityHit != null) {
			float damage = this.damage;
			if (damage == 0.0f) {
				damage = 0.001f;
			}
			if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()),
					damage)) {
				if ((movingobjectposition.entityHit instanceof EntityLivingBase) && (isArrow() || sticksToWalls())) {
					EntityLivingBase entityliving = (EntityLivingBase) movingobjectposition.entityHit;
					if (!worldObj.isRemote) {
						entityliving.setArrowCountInEntity(entityliving.getArrowCountInEntity() + 1);
					}
					if (destroyedOnEntityHit && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
						setDead();
					}
				}
				if (isBlock()) {
					worldObj.playAuxSFX(2001, movingobjectposition.entityHit.getPosition(),
							Item.getIdFromItem(getItem()));
				} else if (!isArrow() && !sticksToWalls()) {
					int[] intArr = { Item.getIdFromItem(getItem()) };
					if (getItem().getHasSubtypes()) {
						intArr = new int[] { Item.getIdFromItem(getItem()), getItemDisplay().getMetadata() };
					}
					for (int i = 0; i < 8; ++i) {
						worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ,
								rand.nextGaussian() * 0.15, rand.nextGaussian() * 0.2, rand.nextGaussian() * 0.15,
								intArr);
					}
				}
				if (punch > 0) {
					float f3 = MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ));
					if (f3 > 0.0f) {
						movingobjectposition.entityHit.addVelocity((motionX * punch * 0.6000000238418579) / f3, 0.1,
								(motionZ * punch * 0.6000000238418579) / f3);
					}
				}
				if ((effect != 0) && (movingobjectposition.entityHit instanceof EntityLivingBase)) {
					if (effect != 1) {
						Potion p = PotionEffectType.getMCType(effect);
						((EntityLivingBase) movingobjectposition.entityHit)
								.addPotionEffect(new PotionEffect(p.id, duration * 20, amplify));
					} else {
						movingobjectposition.entityHit.setFire(duration);
					}
				}
			} else if (hasGravity() && (isArrow() || sticksToWalls())) {
				motionX *= -0.10000000149011612;
				motionY *= -0.10000000149011612;
				motionZ *= -0.10000000149011612;
				rotationYaw += 180.0f;
				prevRotationYaw += 180.0f;
				ticksInAir = 0;
			}
		} else if (isArrow() || sticksToWalls()) {
			tilePos = movingobjectposition.getBlockPos();
			IBlockState state = worldObj.getBlockState(tilePos);
			inTile = state.getBlock();
			inData = inTile.getMetaFromState(state);
			motionX = (float) (movingobjectposition.hitVec.xCoord - posX);
			motionY = (float) (movingobjectposition.hitVec.yCoord - posY);
			motionZ = (float) (movingobjectposition.hitVec.zCoord - posZ);
			float f4 = MathHelper.sqrt_double((motionX * motionX) + (motionY * motionY) + (motionZ * motionZ));
			posX -= (motionX / f4) * 0.05000000074505806;
			posY -= (motionY / f4) * 0.05000000074505806;
			posZ -= (motionZ / f4) * 0.05000000074505806;
			inGround = true;
			arrowShake = 7;
			if (!hasGravity()) {
				dataWatcher.updateObject(26, (byte) 1);
			}
			if (inTile != null) {
				inTile.onEntityCollidedWithBlock(worldObj, tilePos, this);
			}
		} else if (isBlock()) {
			worldObj.playAuxSFX(2001, getPosition(), Item.getIdFromItem(getItem()));
		} else {
			int[] intArr2 = { Item.getIdFromItem(getItem()) };
			if (getItem().getHasSubtypes()) {
				intArr2 = new int[] { Item.getIdFromItem(getItem()), getItemDisplay().getMetadata() };
			}
			for (int j = 0; j < 8; ++j) {
				worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ, rand.nextGaussian() * 0.15,
						rand.nextGaussian() * 0.2, rand.nextGaussian() * 0.15, intArr2);
			}
		}
		if (explosiveRadius > 0) {
			boolean terraindamage = worldObj.getGameRules().getBoolean("mobGriefing") && explosiveDamage;
			worldObj.newExplosion((getThrower() == null) ? this : getThrower(), posX, posY, posZ, explosiveRadius,
					effect == 1, terraindamage);
			if (effect != 0) {
				AxisAlignedBB axisalignedbb = getEntityBoundingBox().expand(explosiveRadius * 2, explosiveRadius * 2,
						explosiveRadius * 2);
				List<EntityLivingBase> list1 = worldObj.getEntitiesWithinAABB((Class) EntityLivingBase.class,
						axisalignedbb);
				for (EntityLivingBase entity : list1) {
					if (effect != 1) {
						Potion p2 = PotionEffectType.getMCType(effect);
						if (p2 == null) {
							continue;
						}
						entity.addPotionEffect(new PotionEffect(p2.id, duration * 20, amplify));
					} else {
						entity.setFire(duration);
					}
				}
			}
			worldObj.playAuxSFX(2002, getPosition(), getPotionColor(effect));
			setDead();
		}
		if (!worldObj.isRemote && !isArrow() && !sticksToWalls()) {
			setDead();
		}
	}

	@Override
	public void onUpdate() {
		super.onEntityUpdate();
		if ((effect == 1) && !inGround) {
			setFire(1);
		}
		IBlockState state = worldObj.getBlockState(tilePos);
		Block block = state.getBlock();
		if ((isArrow() || sticksToWalls()) && (tilePos != BlockPos.ORIGIN)) {
			block.setBlockBoundsBasedOnState(worldObj, tilePos);
			AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(worldObj, tilePos, state);
			if ((axisalignedbb != null) && axisalignedbb.isVecInside(new Vec3(posX, posY, posZ))) {
				inGround = true;
			}
		}
		if (arrowShake > 0) {
			--arrowShake;
		}
		if (inGround) {
			int j = block.getMetaFromState(state);
			if ((block == inTile) && (j == inData)) {
				++ticksInGround;
				if (ticksInGround == 1200) {
					setDead();
				}
			} else {
				inGround = false;
				motionX *= rand.nextFloat() * 0.2f;
				motionY *= rand.nextFloat() * 0.2f;
				motionZ *= rand.nextFloat() * 0.2f;
				ticksInGround = 0;
				ticksInAir = 0;
			}
		} else {
			++ticksInAir;
			if (ticksInAir == 1200) {
				setDead();
			}
			Vec3 vec3 = new Vec3(posX, posY, posZ);
			Vec3 vec4 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
			MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3, vec4, false, true, false);
			vec3 = new Vec3(posX, posY, posZ);
			vec4 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
			if (movingobjectposition != null) {
				vec4 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}
			if (!worldObj.isRemote) {
				Entity entity = null;
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
						getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0));
				double d0 = 0.0;
				getThrower();
				for (int k = 0; k < list.size(); ++k) {
					Entity entity2 = (Entity) list.get(k);
					if (entity2.canBeCollidedWith() && (!entity2.isEntityEqual(thrower) || (ticksInAir >= 25))) {
						float f = 0.3f;
						AxisAlignedBB axisalignedbb2 = entity2.getEntityBoundingBox().expand(f, f, f);
						MovingObjectPosition movingobjectposition2 = axisalignedbb2.calculateIntercept(vec3, vec4);
						if (movingobjectposition2 != null) {
							double d2 = vec3.distanceTo(movingobjectposition2.hitVec);
							if ((d2 < d0) || (d0 == 0.0)) {
								entity = entity2;
								d0 = d2;
							}
						}
					}
				}
				if (entity != null) {
					movingobjectposition = new MovingObjectPosition(entity);
				}
				if ((movingobjectposition != null) && (movingobjectposition.entityHit != null)) {
					if ((npc != null) && (movingobjectposition.entityHit instanceof EntityLivingBase)
							&& npc.isOnSameTeam((EntityLivingBase) movingobjectposition.entityHit)) {
						movingobjectposition = null;
					} else if (movingobjectposition.entityHit instanceof EntityPlayer) {
						EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
						if (entityplayer.capabilities.disableDamage || ((thrower instanceof EntityPlayer)
								&& !((EntityPlayer) thrower).canAttackPlayer(entityplayer))) {
							movingobjectposition = null;
						}
					}
				}
			}
			if (movingobjectposition != null) {
				if ((movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
						&& (worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal)) {
					setPortal(movingobjectposition.getBlockPos());
				} else {
					dataWatcher.updateObject(29, (byte) 0);
					onImpact(movingobjectposition);
				}
			}
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			float f2 = MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ));
			rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180.0) / 3.141592653589793);
			rotationPitch = (float) ((Math.atan2(motionY, f2) * 180.0) / 3.141592653589793);
			while ((rotationPitch - prevRotationPitch) < -180.0f) {
				prevRotationPitch -= 360.0f;
			}
			while ((rotationPitch - prevRotationPitch) >= 180.0f) {
				prevRotationPitch += 360.0f;
			}
			while ((rotationYaw - prevRotationYaw) < -180.0f) {
				prevRotationYaw -= 360.0f;
			}
			while ((rotationYaw - prevRotationYaw) >= 180.0f) {
				prevRotationYaw += 360.0f;
			}
			rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch);
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw);
			if (isRotating()) {
				int spin = isBlock() ? 10 : 20;
				rotationPitch -= (ticksInAir % 15) * spin * getSpeed();
			}
			float f3 = getMotionFactor();
			float f4 = getGravityVelocity();
			if (isInWater()) {
				if (worldObj.isRemote) {
					for (int i = 0; i < 4; ++i) {
						float f5 = 0.25f;
						worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - (motionX * f5),
								posY - (motionY * f5), posZ - (motionZ * f5), motionX, motionY, motionZ, new int[0]);
					}
				}
				f3 = 0.8f;
			}
			motionX *= f3;
			motionY *= f3;
			motionZ *= f3;
			if (hasGravity()) {
				motionY -= f4;
			}
			if (accelerate) {
				motionX += accelerationX;
				motionY += accelerationY;
				motionZ += accelerationZ;
			}
			if (worldObj.isRemote && (dataWatcher.getWatchableObjectInt(22) > 0)) {
				worldObj.spawnParticle(ParticleType.getMCType(dataWatcher.getWatchableObjectInt(22)), posX, posY, posZ,
						0.0, 0.0, 0.0, new int[0]);
			}
			setPosition(posX, posY, posZ);
			doBlockCollisions();
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		tilePos = new BlockPos(compound.getShort("xTile"), compound.getShort("yTile"), compound.getShort("zTile"));
		inTile = Block.getBlockById(compound.getByte("inTile") & 0xFF);
		inData = (compound.getByte("inData") & 0xFF);
		throwableShake = (compound.getByte("shake") & 0xFF);
		inGround = (compound.getByte("inGround") == 1);
		dataWatcher.updateObject(27, compound.getByte("isArrow"));
		throwerName = compound.getString("ownerName");
		canBePickedUp = compound.getBoolean("canBePickedUp");
		damage = compound.getFloat("damagev2");
		punch = compound.getInteger("punch");
		explosiveRadius = compound.getInteger("explosiveRadius");
		duration = compound.getInteger("effectDuration");
		accelerate = compound.getBoolean("accelerate");
		effect = compound.getInteger("PotionEffect");
		dataWatcher.updateObject(22, compound.getInteger("trailenum"));
		dataWatcher.updateObject(23, compound.getInteger("size"));
		dataWatcher.updateObject(24, (byte) (compound.getBoolean("glows") ? 1 : 0));
		dataWatcher.updateObject(25, compound.getInteger("velocity"));
		dataWatcher.updateObject(26, (byte) (compound.getBoolean("gravity") ? 1 : 0));
		dataWatcher.updateObject(28, (byte) (compound.getBoolean("Render3D") ? 1 : 0));
		dataWatcher.updateObject(29, (byte) (compound.getBoolean("Spins") ? 1 : 0));
		dataWatcher.updateObject(30, (byte) (compound.getBoolean("Sticks") ? 1 : 0));
		if ((throwerName != null) && (throwerName.length() == 0)) {
			throwerName = null;
		}
		if (compound.hasKey("direction")) {
			NBTTagList nbttaglist = compound.getTagList("direction", 6);
			motionX = nbttaglist.getDoubleAt(0);
			motionY = nbttaglist.getDoubleAt(1);
			motionZ = nbttaglist.getDoubleAt(2);
		}
		NBTTagCompound var2 = compound.getCompoundTag("Item");
		ItemStack item = ItemStack.loadItemStackFromNBT(var2);
		if (item == null) {
			setDead();
		} else {
			dataWatcher.updateObject(21, item);
		}
	}

	public void setHasGravity(boolean bo) {
		dataWatcher.updateObject(26, (byte) (bo ? 1 : 0));
	}

	public void setIs3D(boolean bo) {
		dataWatcher.updateObject(28, (byte) (bo ? 1 : 0));
	}

	public void setParticleEffect(int type) {
		dataWatcher.updateObject(22, type);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9,
			boolean bo) {
		if (worldObj.isRemote && inGround) {
			return;
		}
		setPosition(par1, par3, par5);
		setRotation(par7, par8);
	}

	public void setRotating(boolean bo) {
		dataWatcher.updateObject(29, (byte) (bo ? 1 : 0));
	}

	public void setSpeed(int speed) {
		dataWatcher.updateObject(25, speed);
	}

	public void setStickInWall(boolean bo) {
		dataWatcher.updateObject(30, (byte) (bo ? 1 : 0));
	}

	@Override
	public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
		float f2 = MathHelper.sqrt_double((par1 * par1) + (par3 * par3) + (par5 * par5));
		float f3 = MathHelper.sqrt_double((par1 * par1) + (par5 * par5));
		float yaw = (float) ((Math.atan2(par1, par5) * 180.0) / 3.141592653589793);
		float pitch = hasGravity() ? par7 : ((float) ((Math.atan2(par3, f3) * 180.0) / 3.141592653589793));
		float n = yaw;
		rotationYaw = n;
		prevRotationYaw = n;
		float n2 = pitch;
		rotationPitch = n2;
		prevRotationPitch = n2;
		motionX = MathHelper.sin((yaw / 180.0f) * 3.1415927f) * MathHelper.cos((pitch / 180.0f) * 3.1415927f);
		motionZ = MathHelper.cos((yaw / 180.0f) * 3.1415927f) * MathHelper.cos((pitch / 180.0f) * 3.1415927f);
		motionY = MathHelper.sin(((pitch + 1.0f) / 180.0f) * 3.1415927f);
		motionX += rand.nextGaussian() * 0.007499999832361937 * par8;
		motionZ += rand.nextGaussian() * 0.007499999832361937 * par8;
		motionY += rand.nextGaussian() * 0.007499999832361937 * par8;
		motionX *= getSpeed();
		motionZ *= getSpeed();
		motionY *= getSpeed();
		accelerationX = (par1 / f2) * 0.1;
		accelerationY = (par3 / f2) * 0.1;
		accelerationZ = (par5 / f2) * 0.1;
		ticksInGround = 0;
	}

	public void setThrownItem(ItemStack item) {
		dataWatcher.updateObject(21, item);
	}

	public void shoot(float speed) {
		double varX = -MathHelper.sin((rotationYaw / 180.0f) * 3.1415927f)
				* MathHelper.cos((rotationPitch / 180.0f) * 3.1415927f);
		double varZ = MathHelper.cos((rotationYaw / 180.0f) * 3.1415927f)
				* MathHelper.cos((rotationPitch / 180.0f) * 3.1415927f);
		double varY = -MathHelper.sin((rotationPitch / 180.0f) * 3.1415927f);
		setThrowableHeading(varX, varY, varZ, -rotationPitch, speed);
	}

	public boolean sticksToWalls() {
		return is3D() && (dataWatcher.getWatchableObjectByte(30) == 1);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setShort("xTile", (short) tilePos.getX());
		par1NBTTagCompound.setShort("yTile", (short) tilePos.getY());
		par1NBTTagCompound.setShort("zTile", (short) tilePos.getZ());
		par1NBTTagCompound.setByte("inTile", (byte) Block.getIdFromBlock(inTile));
		par1NBTTagCompound.setByte("inData", (byte) inData);
		par1NBTTagCompound.setByte("shake", (byte) throwableShake);
		par1NBTTagCompound.setByte("inGround", (byte) (inGround ? 1 : 0));
		par1NBTTagCompound.setByte("isArrow", (byte) (isArrow() ? 1 : 0));
		par1NBTTagCompound.setTag("direction", newDoubleNBTList(new double[] { motionX, motionY, motionZ }));
		par1NBTTagCompound.setBoolean("canBePickedUp", canBePickedUp);
		if (((throwerName == null) || (throwerName.length() == 0)) && (thrower != null)
				&& (thrower instanceof EntityPlayer)) {
			throwerName = thrower.getUniqueID().toString();
		}
		par1NBTTagCompound.setString("ownerName", (throwerName == null) ? "" : throwerName);
		if (getItemDisplay() != null) {
			par1NBTTagCompound.setTag("Item", getItemDisplay().writeToNBT(new NBTTagCompound()));
		}
		par1NBTTagCompound.setFloat("damagev2", damage);
		par1NBTTagCompound.setInteger("punch", punch);
		par1NBTTagCompound.setInteger("size", dataWatcher.getWatchableObjectInt(23));
		par1NBTTagCompound.setInteger("velocity", dataWatcher.getWatchableObjectInt(25));
		par1NBTTagCompound.setInteger("explosiveRadius", explosiveRadius);
		par1NBTTagCompound.setInteger("effectDuration", duration);
		par1NBTTagCompound.setBoolean("gravity", hasGravity());
		par1NBTTagCompound.setBoolean("accelerate", accelerate);
		par1NBTTagCompound.setByte("glows", dataWatcher.getWatchableObjectByte(24));
		par1NBTTagCompound.setInteger("PotionEffect", effect);
		par1NBTTagCompound.setInteger("trailenum", dataWatcher.getWatchableObjectInt(22));
		par1NBTTagCompound.setByte("Render3D", dataWatcher.getWatchableObjectByte(28));
		par1NBTTagCompound.setByte("Spins", dataWatcher.getWatchableObjectByte(29));
		par1NBTTagCompound.setByte("Sticks", dataWatcher.getWatchableObjectByte(30));
	}
}
