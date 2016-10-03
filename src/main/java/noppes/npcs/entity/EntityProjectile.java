package noppes.npcs.entity;

import java.util.Iterator;
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
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.entity.EntityNPCInterface;
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
      this.tilePos = BlockPos.ORIGIN;
      this.inGround = false;
      this.inData = 0;
      this.throwableShake = 0;
      this.arrowShake = 0;
      this.canBePickedUp = false;
      this.destroyedOnEntityHit = true;
      this.throwerName = null;
      this.ticksInAir = 0;
      this.damage = 5.0F;
      this.punch = 0;
      this.accelerate = false;
      this.explosiveDamage = true;
      this.explosiveRadius = 0;
      this.effect = 0;
      this.duration = 5;
      this.amplify = 0;
      this.setSize(0.25F, 0.25F);
   }

   protected void entityInit() {
      this.dataWatcher.addObjectByDataType(21, 5);
      this.dataWatcher.addObject(22, Integer.valueOf(-1));
      this.dataWatcher.addObject(23, Integer.valueOf(10));
      this.dataWatcher.addObject(24, Byte.valueOf((byte)0));
      this.dataWatcher.addObject(25, Integer.valueOf(10));
      this.dataWatcher.addObject(26, Byte.valueOf((byte)0));
      this.dataWatcher.addObject(27, Byte.valueOf((byte)0));
      this.dataWatcher.addObject(28, Byte.valueOf((byte)0));
      this.dataWatcher.addObject(29, Byte.valueOf((byte)0));
      this.dataWatcher.addObject(30, Byte.valueOf((byte)0));
      this.dataWatcher.addObject(31, Byte.valueOf((byte)0));
   }

   @SideOnly(Side.CLIENT)
   public boolean isInRangeToRenderDist(double par1) {
      double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
      d1 *= 64.0D;
      return par1 < d1 * d1;
   }

   public EntityProjectile(World par1World, EntityLivingBase par2EntityLiving, ItemStack item, boolean isNPC) {
      super(par1World);
      this.tilePos = BlockPos.ORIGIN;
      this.inGround = false;
      this.inData = 0;
      this.throwableShake = 0;
      this.arrowShake = 0;
      this.canBePickedUp = false;
      this.destroyedOnEntityHit = true;
      this.throwerName = null;
      this.ticksInAir = 0;
      this.damage = 5.0F;
      this.punch = 0;
      this.accelerate = false;
      this.explosiveDamage = true;
      this.explosiveRadius = 0;
      this.effect = 0;
      this.duration = 5;
      this.amplify = 0;
      this.thrower = par2EntityLiving;
      if(this.thrower != null) {
         this.throwerName = this.thrower.getUniqueID().toString();
      }

      this.setThrownItem(item);
      this.dataWatcher.updateObject(27, Byte.valueOf((byte)(this.getItem() == Items.arrow?1:0)));
      this.setSize((float)(this.dataWatcher.getWatchableObjectInt(23) / 10), (float)(this.dataWatcher.getWatchableObjectInt(23) / 10));
      this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
      this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.1F);
      this.posY -= 0.10000000149011612D;
      this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.1F);
      this.setPosition(this.posX, this.posY, this.posZ);
      if(isNPC) {
         this.npc = (EntityNPCInterface)this.thrower;
         this.getStatProperties(this.npc.stats.ranged);
      }

   }

   public void setThrownItem(ItemStack item) {
      this.dataWatcher.updateObject(21, item);
   }

   public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
      float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
      float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
      float yaw = (float)(Math.atan2(par1, par5) * 180.0D / 3.141592653589793D);
      float pitch = this.hasGravity()?par7:(float)(Math.atan2(par3, (double)f3) * 180.0D / 3.141592653589793D);
      this.prevRotationYaw = this.rotationYaw = yaw;
      this.prevRotationPitch = this.rotationPitch = pitch;
      this.motionX = (double)(MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F));
      this.motionZ = (double)(MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F));
      this.motionY = (double)MathHelper.sin((pitch + 1.0F) / 180.0F * 3.1415927F);
      this.motionX += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
      this.motionZ += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
      this.motionY += this.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
      this.motionX *= (double)this.getSpeed();
      this.motionZ *= (double)this.getSpeed();
      this.motionY *= (double)this.getSpeed();
      this.accelerationX = par1 / (double)f2 * 0.1D;
      this.accelerationY = par3 / (double)f2 * 0.1D;
      this.accelerationZ = par5 / (double)f2 * 0.1D;
      this.ticksInGround = 0;
   }

   public float getAngleForXYZ(double varX, double varY, double varZ, double horiDist, boolean arc) {
      float g = this.getGravityVelocity();
      float var1 = this.getSpeed() * this.getSpeed();
      double var2 = (double)g * horiDist;
      double var3 = (double)g * horiDist * horiDist + 2.0D * varY * (double)var1;
      double var4 = (double)(var1 * var1) - (double)g * var3;
      if(var4 < 0.0D) {
         return 30.0F;
      } else {
         float var6 = arc?var1 + MathHelper.sqrt_double(var4):var1 - MathHelper.sqrt_double(var4);
         float var7 = (float)(Math.atan2((double)var6, var2) * 180.0D / 3.141592653589793D);
         return var7;
      }
   }

   public void shoot(float speed) {
      double varX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
      double varZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
      double varY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F));
      this.setThrowableHeading(varX, varY, varZ, -this.rotationPitch, speed);
   }

   @SideOnly(Side.CLIENT)
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9, boolean bo) {
      if(!this.worldObj.isRemote || !this.inGround) {
         this.setPosition(par1, par3, par5);
         this.setRotation(par7, par8);
      }
   }

   public void onUpdate() {
      super.onEntityUpdate();
      if(this.effect == 1 && !this.inGround) {
         this.setFire(1);
      }

      IBlockState state = this.worldObj.getBlockState(this.tilePos);
      Block block = state.getBlock();
      if((this.isArrow() || this.sticksToWalls()) && this.tilePos != BlockPos.ORIGIN) {
         block.setBlockBoundsBasedOnState(this.worldObj, this.tilePos);
         AxisAlignedBB vec3 = block.getCollisionBoundingBox(this.worldObj, this.tilePos, state);
         if(vec3 != null && vec3.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
            this.inGround = true;
         }
      }

      if(this.arrowShake > 0) {
         --this.arrowShake;
      }

      if(this.inGround) {
         int var18 = block.getMetaFromState(state);
         if(block == this.inTile && var18 == this.inData) {
            ++this.ticksInGround;
            if(this.ticksInGround == 1200) {
               this.setDead();
            }
         } else {
            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
         }
      } else {
         ++this.ticksInAir;
         if(this.ticksInAir == 1200) {
            this.setDead();
         }

         Vec3 var19 = new Vec3(this.posX, this.posY, this.posZ);
         Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(var19, vec31, false, true, false);
         var19 = new Vec3(this.posX, this.posY, this.posZ);
         vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         if(movingobjectposition != null) {
            vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
         }

         if(!this.worldObj.isRemote) {
            Entity f1 = null;
            List f2 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double f3 = 0.0D;
            EntityLivingBase f4 = this.getThrower();

            for(int entityplayer = 0; entityplayer < f2.size(); ++entityplayer) {
               Entity entity1 = (Entity)f2.get(entityplayer);
               if(entity1.canBeCollidedWith() && (!entity1.isEntityEqual(this.thrower) || this.ticksInAir >= 25)) {
                  float f = 0.3F;
                  AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f, (double)f, (double)f);
                  MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(var19, vec31);
                  if(movingobjectposition1 != null) {
                     double d1 = var19.distanceTo(movingobjectposition1.hitVec);
                     if(d1 < f3 || f3 == 0.0D) {
                        f1 = entity1;
                        f3 = d1;
                     }
                  }
               }
            }

            if(f1 != null) {
               movingobjectposition = new MovingObjectPosition(f1);
            }

            if(movingobjectposition != null && movingobjectposition.entityHit != null) {
               if(this.npc != null && movingobjectposition.entityHit instanceof EntityLivingBase && this.npc.isOnSameTeam((EntityLivingBase)movingobjectposition.entityHit)) {
                  movingobjectposition = null;
               } else if(movingobjectposition.entityHit instanceof EntityPlayer) {
                  EntityPlayer var25 = (EntityPlayer)movingobjectposition.entityHit;
                  if(var25.capabilities.disableDamage || this.thrower instanceof EntityPlayer && !((EntityPlayer)this.thrower).canAttackPlayer(var25)) {
                     movingobjectposition = null;
                  }
               }
            }
         }

         if(movingobjectposition != null) {
            if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK && this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal) {
               this.setPortal(movingobjectposition.getBlockPos());
            } else {
               this.dataWatcher.updateObject(29, Byte.valueOf((byte)0));
               this.onImpact(movingobjectposition);
            }
         }

         this.posX += this.motionX;
         this.posY += this.motionY;
         this.posZ += this.motionZ;
         float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D);

         for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var20) * 180.0D / 3.141592653589793D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
         }

         while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
         }

         while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
         }

         while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
         }

         this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch);
         this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw);
         if(this.isRotating()) {
            int var21 = this.isBlock()?10:20;
            this.rotationPitch -= (float)(this.ticksInAir % 15 * var21) * this.getSpeed();
         }

         float var22 = this.getMotionFactor();
         float var23 = this.getGravityVelocity();
         if(this.isInWater()) {
            if(this.worldObj.isRemote) {
               for(int k = 0; k < 4; ++k) {
                  float var24 = 0.25F;
                  this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)var24, this.posY - this.motionY * (double)var24, this.posZ - this.motionZ * (double)var24, this.motionX, this.motionY, this.motionZ, new int[0]);
               }
            }

            var22 = 0.8F;
         }

         this.motionX *= (double)var22;
         this.motionY *= (double)var22;
         this.motionZ *= (double)var22;
         if(this.hasGravity()) {
            this.motionY -= (double)var23;
         }

         if(this.accelerate) {
            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
         }

         if(this.worldObj.isRemote && this.dataWatcher.getWatchableObjectInt(22) > 0) {
            this.worldObj.spawnParticle(ParticleType.getMCType(this.dataWatcher.getWatchableObjectInt(22)), this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
         }

         this.setPosition(this.posX, this.posY, this.posZ);
         this.doBlockCollisions();
      }

   }

   public boolean isBlock() {
      ItemStack item = this.getItemDisplay();
      return item == null?false:item.getItem() instanceof ItemBlock;
   }

   private Item getItem() {
      ItemStack item = this.getItemDisplay();
      return item == null?null:item.getItem();
   }

   protected float getMotionFactor() {
      return this.accelerate?0.95F:1.0F;
   }

   protected void onImpact(MovingObjectPosition movingobjectposition) {
      if(this.callback != null) {
         BlockPos terraindamage = null;
         if(movingobjectposition.entityHit != null) {
            terraindamage = movingobjectposition.entityHit.getPosition();
         } else {
            terraindamage = movingobjectposition.getBlockPos();
         }

         if(terraindamage == BlockPos.ORIGIN) {
            terraindamage = new BlockPos(movingobjectposition.hitVec);
         }

         if(this.callback.onImpact(this, terraindamage, movingobjectposition.entityHit)) {
            return;
         }
      }

      float var10;
      if(movingobjectposition.entityHit != null) {
         float var9 = this.damage;
         if(var9 == 0.0F) {
            var9 = 0.001F;
         }

         if(movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), var9)) {
            if(movingobjectposition.entityHit instanceof EntityLivingBase && (this.isArrow() || this.sticksToWalls())) {
               EntityLivingBase axisalignedbb = (EntityLivingBase)movingobjectposition.entityHit;
               if(!this.worldObj.isRemote) {
                  axisalignedbb.setArrowCountInEntity(axisalignedbb.getArrowCountInEntity() + 1);
               }

               if(this.destroyedOnEntityHit && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
                  this.setDead();
               }
            }

            if(this.isBlock()) {
               this.worldObj.playAuxSFX(2001, movingobjectposition.entityHit.getPosition(), Item.getIdFromItem(this.getItem()));
            } else if(!this.isArrow() && !this.sticksToWalls()) {
               int[] var8 = new int[]{Item.getIdFromItem(this.getItem())};
               if(this.getItem().getHasSubtypes()) {
                  var8 = new int[]{Item.getIdFromItem(this.getItem()), this.getItemDisplay().getMetadata()};
               }

               for(int list1 = 0; list1 < 8; ++list1) {
                  this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 0.15D, this.rand.nextGaussian() * 0.2D, this.rand.nextGaussian() * 0.15D, var8);
               }
            }

            if(this.punch > 0) {
               var10 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
               if(var10 > 0.0F) {
                  movingobjectposition.entityHit.addVelocity(this.motionX * (double)this.punch * 0.6000000238418579D / (double)var10, 0.1D, this.motionZ * (double)this.punch * 0.6000000238418579D / (double)var10);
               }
            }

            if(this.effect != 0 && movingobjectposition.entityHit instanceof EntityLivingBase) {
               if(this.effect != 1) {
                  Potion var12 = PotionEffectType.getMCType(this.effect);
                  ((EntityLivingBase)movingobjectposition.entityHit).addPotionEffect(new PotionEffect(var12.id, this.duration * 20, this.amplify));
               } else {
                  movingobjectposition.entityHit.setFire(this.duration);
               }
            }
         } else if(this.hasGravity() && (this.isArrow() || this.sticksToWalls())) {
            this.motionX *= -0.10000000149011612D;
            this.motionY *= -0.10000000149011612D;
            this.motionZ *= -0.10000000149011612D;
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            this.ticksInAir = 0;
         }
      } else if(!this.isArrow() && !this.sticksToWalls()) {
         if(this.isBlock()) {
            this.worldObj.playAuxSFX(2001, this.getPosition(), Item.getIdFromItem(this.getItem()));
         } else {
            int[] var14 = new int[]{Item.getIdFromItem(this.getItem())};
            if(this.getItem().getHasSubtypes()) {
               var14 = new int[]{Item.getIdFromItem(this.getItem()), this.getItemDisplay().getMetadata()};
            }

            for(int var15 = 0; var15 < 8; ++var15) {
               this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 0.15D, this.rand.nextGaussian() * 0.2D, this.rand.nextGaussian() * 0.15D, var14);
            }
         }
      } else {
         this.tilePos = movingobjectposition.getBlockPos();
         IBlockState var13 = this.worldObj.getBlockState(this.tilePos);
         this.inTile = var13.getBlock();
         this.inData = this.inTile.getMetaFromState(var13);
         this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
         this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
         this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
         var10 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
         this.posX -= this.motionX / (double)var10 * 0.05000000074505806D;
         this.posY -= this.motionY / (double)var10 * 0.05000000074505806D;
         this.posZ -= this.motionZ / (double)var10 * 0.05000000074505806D;
         this.inGround = true;
         this.arrowShake = 7;
         if(!this.hasGravity()) {
            this.dataWatcher.updateObject(26, Byte.valueOf((byte)1));
         }

         if(this.inTile != null) {
            this.inTile.onEntityCollidedWithBlock(this.worldObj, this.tilePos, this);
         }
      }

      if(this.explosiveRadius > 0) {
         boolean var17 = this.worldObj.getGameRules().getBoolean("mobGriefing") && this.explosiveDamage;
         this.worldObj.newExplosion((Entity)(this.getThrower() == null?this:this.getThrower()), this.posX, this.posY, this.posZ, (float)this.explosiveRadius, this.effect == 1, var17);
         if(this.effect != 0) {
            AxisAlignedBB var16 = this.getEntityBoundingBox().expand((double)(this.explosiveRadius * 2), (double)(this.explosiveRadius * 2), (double)(this.explosiveRadius * 2));
            List var11 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, var16);
            Iterator var5 = var11.iterator();

            while(var5.hasNext()) {
               EntityLivingBase entity = (EntityLivingBase)var5.next();
               if(this.effect != 1) {
                  Potion p = PotionEffectType.getMCType(this.effect);
                  if(p != null) {
                     entity.addPotionEffect(new PotionEffect(p.id, this.duration * 20, this.amplify));
                  }
               } else {
                  entity.setFire(this.duration);
               }
            }
         }

         this.worldObj.playAuxSFX(2002, this.getPosition(), this.getPotionColor(this.effect));
         this.setDead();
      }

      if(!this.worldObj.isRemote && !this.isArrow() && !this.sticksToWalls()) {
         this.setDead();
      }

   }

   private void blockParticles() {}

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setShort("xTile", (short)this.tilePos.getX());
      par1NBTTagCompound.setShort("yTile", (short)this.tilePos.getY());
      par1NBTTagCompound.setShort("zTile", (short)this.tilePos.getZ());
      par1NBTTagCompound.setByte("inTile", (byte)Block.getIdFromBlock(this.inTile));
      par1NBTTagCompound.setByte("inData", (byte)this.inData);
      par1NBTTagCompound.setByte("shake", (byte)this.throwableShake);
      par1NBTTagCompound.setByte("inGround", (byte)(this.inGround?1:0));
      par1NBTTagCompound.setByte("isArrow", (byte)(this.isArrow()?1:0));
      par1NBTTagCompound.setTag("direction", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
      par1NBTTagCompound.setBoolean("canBePickedUp", this.canBePickedUp);
      if((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
         this.throwerName = this.thrower.getUniqueID().toString();
      }

      par1NBTTagCompound.setString("ownerName", this.throwerName == null?"":this.throwerName);
      if(this.getItemDisplay() != null) {
         par1NBTTagCompound.setTag("Item", this.getItemDisplay().writeToNBT(new NBTTagCompound()));
      }

      par1NBTTagCompound.setFloat("damagev2", this.damage);
      par1NBTTagCompound.setInteger("punch", this.punch);
      par1NBTTagCompound.setInteger("size", this.dataWatcher.getWatchableObjectInt(23));
      par1NBTTagCompound.setInteger("velocity", this.dataWatcher.getWatchableObjectInt(25));
      par1NBTTagCompound.setInteger("explosiveRadius", this.explosiveRadius);
      par1NBTTagCompound.setInteger("effectDuration", this.duration);
      par1NBTTagCompound.setBoolean("gravity", this.hasGravity());
      par1NBTTagCompound.setBoolean("accelerate", this.accelerate);
      par1NBTTagCompound.setByte("glows", this.dataWatcher.getWatchableObjectByte(24));
      par1NBTTagCompound.setInteger("PotionEffect", this.effect);
      par1NBTTagCompound.setInteger("trailenum", this.dataWatcher.getWatchableObjectInt(22));
      par1NBTTagCompound.setByte("Render3D", this.dataWatcher.getWatchableObjectByte(28));
      par1NBTTagCompound.setByte("Spins", this.dataWatcher.getWatchableObjectByte(29));
      par1NBTTagCompound.setByte("Sticks", this.dataWatcher.getWatchableObjectByte(30));
   }

   public void readEntityFromNBT(NBTTagCompound compound) {
      this.tilePos = new BlockPos(compound.getShort("xTile"), compound.getShort("yTile"), compound.getShort("zTile"));
      this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
      this.inData = compound.getByte("inData") & 255;
      this.throwableShake = compound.getByte("shake") & 255;
      this.inGround = compound.getByte("inGround") == 1;
      this.dataWatcher.updateObject(27, Byte.valueOf(compound.getByte("isArrow")));
      this.throwerName = compound.getString("ownerName");
      this.canBePickedUp = compound.getBoolean("canBePickedUp");
      this.damage = compound.getFloat("damagev2");
      this.punch = compound.getInteger("punch");
      this.explosiveRadius = compound.getInteger("explosiveRadius");
      this.duration = compound.getInteger("effectDuration");
      this.accelerate = compound.getBoolean("accelerate");
      this.effect = compound.getInteger("PotionEffect");
      this.dataWatcher.updateObject(22, Integer.valueOf(compound.getInteger("trailenum")));
      this.dataWatcher.updateObject(23, Integer.valueOf(compound.getInteger("size")));
      this.dataWatcher.updateObject(24, Byte.valueOf((byte)(compound.getBoolean("glows")?1:0)));
      this.dataWatcher.updateObject(25, Integer.valueOf(compound.getInteger("velocity")));
      this.dataWatcher.updateObject(26, Byte.valueOf((byte)(compound.getBoolean("gravity")?1:0)));
      this.dataWatcher.updateObject(28, Byte.valueOf((byte)(compound.getBoolean("Render3D")?1:0)));
      this.dataWatcher.updateObject(29, Byte.valueOf((byte)(compound.getBoolean("Spins")?1:0)));
      this.dataWatcher.updateObject(30, Byte.valueOf((byte)(compound.getBoolean("Sticks")?1:0)));
      if(this.throwerName != null && this.throwerName.length() == 0) {
         this.throwerName = null;
      }

      if(compound.hasKey("direction")) {
         NBTTagList var2 = compound.getTagList("direction", 6);
         this.motionX = var2.getDoubleAt(0);
         this.motionY = var2.getDoubleAt(1);
         this.motionZ = var2.getDoubleAt(2);
      }

      NBTTagCompound var21 = compound.getCompoundTag("Item");
      ItemStack item = ItemStack.loadItemStackFromNBT(var21);
      if(item == null) {
         this.setDead();
      } else {
         this.dataWatcher.updateObject(21, item);
      }

   }

   public EntityLivingBase getThrower() {
      if(this.throwerName != null && !this.throwerName.isEmpty()) {
         try {
            UUID ex = UUID.fromString(this.throwerName);
            if(this.thrower == null && ex != null) {
               this.thrower = this.worldObj.getPlayerEntityByUUID(ex);
            }
         } catch (IllegalArgumentException var2) {
            ;
         }

         return this.thrower;
      } else {
         return null;
      }
   }

   private int getPotionColor(int p) {
      switch(p) {
      case 2:
         return 32660;
      case 3:
         return 32660;
      case 4:
         return 32696;
      case 5:
         return 32698;
      case 6:
         return 32732;
      case 7:
         return 15;
      case 8:
         return 32732;
      default:
         return 0;
      }
   }

   public void getStatProperties(DataRanged stats) {
      this.damage = (float)stats.getStrength();
      this.punch = stats.getKnockback();
      this.accelerate = stats.getAccelerate();
      this.explosiveRadius = stats.getExplodeSize();
      this.effect = stats.getEffectType();
      this.duration = stats.getEffectTime();
      this.amplify = stats.getEffectStrength();
      this.setParticleEffect(stats.getParticle());
      this.dataWatcher.updateObject(23, Integer.valueOf(stats.getSize()));
      this.dataWatcher.updateObject(24, Byte.valueOf((byte)(stats.getGlows()?1:0)));
      this.setSpeed(stats.getSpeed());
      this.setHasGravity(stats.getHasGravity());
      this.setIs3D(stats.getRender3D());
      this.setRotating(stats.getSpins());
      this.setStickInWall(stats.getSticks());
   }

   public void setParticleEffect(int type) {
      this.dataWatcher.updateObject(22, Integer.valueOf(type));
   }

   public void setHasGravity(boolean bo) {
      this.dataWatcher.updateObject(26, Byte.valueOf((byte)(bo?1:0)));
   }

   public void setIs3D(boolean bo) {
      this.dataWatcher.updateObject(28, Byte.valueOf((byte)(bo?1:0)));
   }

   public void setStickInWall(boolean bo) {
      this.dataWatcher.updateObject(30, Byte.valueOf((byte)(bo?1:0)));
   }

   public ItemStack getItemDisplay() {
      return this.dataWatcher.getWatchableObjectItemStack(21);
   }

   public float getBrightness(float par1) {
      return this.dataWatcher.getWatchableObjectByte(24) == 1?1.0F:super.getBrightness(par1);
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return this.dataWatcher.getWatchableObjectByte(24) == 1?15728880:super.getBrightnessForRender(par1);
   }

   public boolean hasGravity() {
      return this.dataWatcher.getWatchableObjectByte(26) == 1;
   }

   public void setSpeed(int speed) {
      this.dataWatcher.updateObject(25, Integer.valueOf(speed));
   }

   public float getSpeed() {
      return (float)this.dataWatcher.getWatchableObjectInt(25) / 10.0F;
   }

   public boolean isArrow() {
      return this.dataWatcher.getWatchableObjectByte(27) == 1;
   }

   public void setRotating(boolean bo) {
      this.dataWatcher.updateObject(29, Byte.valueOf((byte)(bo?1:0)));
   }

   public boolean isRotating() {
      return this.dataWatcher.getWatchableObjectByte(29) == 1;
   }

   public boolean glows() {
      return this.dataWatcher.getWatchableObjectByte(24) == 1;
   }

   public boolean is3D() {
      return this.dataWatcher.getWatchableObjectByte(28) == 1 || this.isBlock();
   }

   public boolean sticksToWalls() {
      return this.is3D() && this.dataWatcher.getWatchableObjectByte(30) == 1;
   }

   public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
      if(!this.worldObj.isRemote && this.canBePickedUp && this.inGround && this.arrowShake <= 0) {
         if(par1EntityPlayer.inventory.addItemStackToInventory(this.getItemDisplay())) {
            this.inGround = false;
            this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            par1EntityPlayer.onItemPickup(this, 1);
            this.setDead();
         }

      }
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.getItemDisplay() != null?new ChatComponentTranslation(this.getItemDisplay().getDisplayName(), new Object[0]):super.getDisplayName());
   }
}
