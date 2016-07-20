//

//

package noppes.npcs.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.IChatMessages;
import noppes.npcs.LogWriter;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcDamageSource;
import noppes.npcs.Server;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.ai.EntityAIAmbushTarget;
import noppes.npcs.ai.EntityAIAnimation;
import noppes.npcs.ai.EntityAIAttackTarget;
import noppes.npcs.ai.EntityAIAvoidTarget;
import noppes.npcs.ai.EntityAIBustDoor;
import noppes.npcs.ai.EntityAIDodgeShoot;
import noppes.npcs.ai.EntityAIFindShade;
import noppes.npcs.ai.EntityAIFollow;
import noppes.npcs.ai.EntityAIJob;
import noppes.npcs.ai.EntityAILook;
import noppes.npcs.ai.EntityAIMoveIndoors;
import noppes.npcs.ai.EntityAIMovingPath;
import noppes.npcs.ai.EntityAIOrbitTarget;
import noppes.npcs.ai.EntityAIPanic;
import noppes.npcs.ai.EntityAIPounceTarget;
import noppes.npcs.ai.EntityAIRangedAttack;
import noppes.npcs.ai.EntityAIReturn;
import noppes.npcs.ai.EntityAIRole;
import noppes.npcs.ai.EntityAISprintToTarget;
import noppes.npcs.ai.EntityAIStalkTarget;
import noppes.npcs.ai.EntityAITransform;
import noppes.npcs.ai.EntityAIWander;
import noppes.npcs.ai.EntityAIWatchClosest;
import noppes.npcs.ai.EntityAIWaterNav;
import noppes.npcs.ai.EntityAIWorldLines;
import noppes.npcs.ai.EntityAIZigZagTarget;
import noppes.npcs.ai.FlyingMoveHelper;
import noppes.npcs.ai.PathNavigateFlying;
import noppes.npcs.ai.selector.NPCAttackSelector;
import noppes.npcs.ai.target.EntityAIClearTarget;
import noppes.npcs.ai.target.EntityAIClosestTarget;
import noppes.npcs.ai.target.EntityAIOwnerHurtByTarget;
import noppes.npcs.ai.target.EntityAIOwnerHurtTarget;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.DataTransform;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.entity.data.DataAI;
import noppes.npcs.entity.data.DataAdvanced;
import noppes.npcs.entity.data.DataDisplay;
import noppes.npcs.entity.data.DataInventory;
import noppes.npcs.entity.data.DataScript;
import noppes.npcs.entity.data.DataStats;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.roles.JobBard;
import noppes.npcs.roles.JobFollower;
import noppes.npcs.roles.JobInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.roles.RoleInterface;
import noppes.npcs.util.GameProfileAlt;
import noppes.npcs.util.IProjectileCallback;

public abstract class EntityNPCInterface extends EntityCreature
		implements IEntityAdditionalSpawnData, ICommandSender, IRangedAttackMob, IBossDisplayData {
	public static final int DWRole = 16;
	public static final int DWJob = 17;
	public static final int DWBool = 25;
	private static final GameProfileAlt chateventProfile;
	private static FakePlayer chateventPlayer;
	static {
		chateventProfile = new GameProfileAlt();
	}
	public ICustomNpc wrappedNPC;
	public DataDisplay display;
	public DataStats stats;
	public DataAI ai;
	public DataAdvanced advanced;
	public DataInventory inventory;
	public DataScript script;
	public DataTransform transform;
	public DataTimers timers;
	public String linkedName;
	public long linkedLast;
	public LinkedNpcController.LinkedData linkedData;
	public float baseHeight;
	public float scaleX;
	public float scaleY;
	public float scaleZ;
	private boolean wasKilled;
	public RoleInterface roleInterface;
	public JobInterface jobInterface;
	public HashMap<Integer, DialogOption> dialogs;
	public boolean hasDied;
	public long killedtime;
	public long totalTicksAlive;
	private int taskCount;
	public int lastInteract;
	public Faction faction;
	private EntityAIRangedAttack aiRange;
	private EntityAIBase aiAttackTarget;
	public EntityAILook lookAi;
	public EntityAIAnimation animateAi;
	public List<EntityLivingBase> interactingEntities;
	public ResourceLocation textureLocation;
	public ResourceLocation textureGlowLocation;
	public ResourceLocation textureCloakLocation;
	public int currentAnimation;
	public int animationStart;
	public int npcVersion;
	public IChatMessages messages;
	public boolean updateClient;
	public boolean updateAI;
	public double field_20066_r;
	public double field_20065_s;
	public double field_20064_t;
	public double field_20063_u;
	public double field_20062_v;
	public double field_20061_w;

	private double startYPos;

	public EntityNPCInterface(final World world) {
		super(world);
		linkedName = "";
		linkedLast = 0L;
		baseHeight = 1.8f;
		wasKilled = false;
		hasDied = false;
		killedtime = 0L;
		totalTicksAlive = 0L;
		taskCount = 1;
		lastInteract = 0;
		interactingEntities = new ArrayList<EntityLivingBase>();
		textureLocation = null;
		textureGlowLocation = null;
		textureCloakLocation = null;
		currentAnimation = 0;
		animationStart = 0;
		npcVersion = VersionCompatibility.ModRev;
		updateClient = false;
		updateAI = false;
		startYPos = -1.0;
		if (!isRemote()) {
			wrappedNPC = new NPCWrapper(this);
		}
		dialogs = new HashMap<Integer, DialogOption>();
		if (!CustomNpcs.DefaultInteractLine.isEmpty()) {
			advanced.interactLines.lines.put(0, new Line(CustomNpcs.DefaultInteractLine));
		}
		experienceValue = 0;
		final float scaleX = 0.9375f;
		scaleZ = scaleX;
		scaleY = scaleX;
		this.scaleX = scaleX;
		faction = getFaction();
		setFaction(faction.id);
		setSize(1.0f, 1.0f);
		updateAI = true;
	}

	@Override
	public void addChatMessage(final IChatComponent var1) {
	}

	public void addInteract(final EntityLivingBase entity) {
		if (!ai.stopAndInteract || isAttacking() || !entity.isEntityAlive() || isAIDisabled()) {
			return;
		}
		if ((ticksExisted - lastInteract) < 180) {
			interactingEntities.clear();
		}
		getNavigator().clearPathEntity();
		lastInteract = ticksExisted;
		if (!interactingEntities.contains(entity)) {
			interactingEntities.add(entity);
		}
	}

	public void addRegularEntries() {
		tasks.addTask(taskCount++, new EntityAIReturn(this));
		tasks.addTask(taskCount++, new EntityAIFollow(this));
		if ((ai.getStandingType() != 1) && (ai.getStandingType() != 3)) {
			tasks.addTask(taskCount++, new EntityAIWatchClosest(this, EntityLivingBase.class, 5.0f));
		}
		tasks.addTask(taskCount++, lookAi = new EntityAILook(this));
		tasks.addTask(taskCount++, new EntityAIWorldLines(this));
		tasks.addTask(taskCount++, new EntityAIJob(this));
		tasks.addTask(taskCount++, new EntityAIRole(this));
		tasks.addTask(taskCount++, animateAi = new EntityAIAnimation(this));
		if (transform.isValid()) {
			tasks.addTask(taskCount++, new EntityAITransform(this));
		}
	}

	@Override
	public void addVelocity(final double d, final double d1, final double d2) {
		if (isWalking() && !isKilled()) {
			super.addVelocity(d, d1, d2);
		}
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	@Override
	protected float applyArmorCalculations(final DamageSource source, float damage) {
		if (advanced.role == 6) {
			damage = ((RoleCompanion) roleInterface).applyArmorCalculations(source, damage);
		}
		return damage;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		display = new DataDisplay(this);
		stats = new DataStats(this);
		ai = new DataAI(this);
		advanced = new DataAdvanced(this);
		inventory = new DataInventory(this);
		transform = new DataTransform(this);
		script = new DataScript(this);
		timers = new DataTimers(this);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(stats.maxHealth);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(CustomNpcs.NpcNavRange);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(getSpeed());
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(stats.melee.getStrength());
	}

	@Override
	public boolean attackEntityAsMob(final Entity par1Entity) {
		float f = stats.melee.getStrength();
		if (stats.melee.getDelay() < 10) {
			par1Entity.hurtResistantTime = 0;
		}
		if (par1Entity instanceof EntityLivingBase) {
			final NpcEvent.MeleeAttackEvent event = new NpcEvent.MeleeAttackEvent(wrappedNPC,
					(EntityLivingBase) par1Entity, f);
			if (EventHooks.onNPCAttacksMelee(this, event)) {
				return false;
			}
			f = event.damage;
		}
		final boolean var4 = par1Entity.attackEntityFrom(new NpcDamageSource("mob", this), f);
		if (var4) {
			if (getOwner() instanceof EntityPlayer) {
				EntityUtil.setRecentlyHit((EntityLivingBase) par1Entity);
			}
			if (stats.melee.getKnockback() > 0) {
				par1Entity.addVelocity(
						-MathHelper.sin((rotationYaw * 3.1415927f) / 180.0f) * stats.melee.getKnockback() * 0.5f, 0.1,
						MathHelper.cos((rotationYaw * 3.1415927f) / 180.0f) * stats.melee.getKnockback() * 0.5f);
				motionX *= 0.6;
				motionZ *= 0.6;
			}
			if (advanced.role == 6) {
				((RoleCompanion) roleInterface).attackedEntity(par1Entity);
			}
		}
		if (stats.melee.getEffectType() != 0) {
			if (stats.melee.getEffectType() != 1) {
				((EntityLivingBase) par1Entity)
						.addPotionEffect(new PotionEffect(PotionEffectType.getMCType(stats.melee.getEffectType()).id,
								stats.melee.getEffectTime() * 20, stats.melee.getEffectStrength()));
			} else {
				par1Entity.setFire(stats.melee.getEffectTime());
			}
		}
		return var4;
	}

	@Override
	public boolean attackEntityFrom(final DamageSource damagesource, float i) {
		if (worldObj.isRemote || CustomNpcs.FreezeNPCs || damagesource.damageType.equals("inWall")) {
			return false;
		}
		if (damagesource.damageType.equals("outOfWorld") && isKilled()) {
			reset();
		}
		i = stats.resistances.applyResistance(damagesource, i);
		if ((hurtResistantTime > (maxHurtResistantTime / 2.0f)) && (i <= lastDamage)) {
			return false;
		}
		final Entity entity = damagesource.getSourceOfDamage();
		EntityLivingBase attackingEntity = null;
		if (entity instanceof EntityLivingBase) {
			attackingEntity = (EntityLivingBase) entity;
		}
		if ((entity instanceof EntityArrow) && (((EntityArrow) entity).shootingEntity instanceof EntityLivingBase)) {
			attackingEntity = (EntityLivingBase) ((EntityArrow) entity).shootingEntity;
		} else if (entity instanceof EntityThrowable) {
			attackingEntity = ((EntityThrowable) entity).getThrower();
		}
		if ((attackingEntity != null) && (attackingEntity == getOwner())) {
			return false;
		}
		if (attackingEntity instanceof EntityNPCInterface) {
			final EntityNPCInterface npc = (EntityNPCInterface) attackingEntity;
			if (npc.faction.id == faction.id) {
				return false;
			}
			if (npc.getOwner() instanceof EntityPlayer) {
				recentlyHit = 100;
			}
		} else if ((attackingEntity instanceof EntityPlayer)
				&& faction.isFriendlyToPlayer((EntityPlayer) attackingEntity)) {
			return false;
		}
		final NpcEvent.DamagedEvent event = new NpcEvent.DamagedEvent(wrappedNPC, attackingEntity, i, damagesource);
		if (EventHooks.onNPCDamaged(this, event)) {
			return false;
		}
		i = event.damage;
		if (isKilled()) {
			return false;
		}
		if (attackingEntity == null) {
			return super.attackEntityFrom(damagesource, i);
		}
		try {
			if (isAttacking()) {
				if ((getAttackTarget() != null) && (attackingEntity != null)
						&& (getDistanceSqToEntity(getAttackTarget()) > getDistanceSqToEntity(attackingEntity))) {
					setAttackTarget(attackingEntity);
				}
				return super.attackEntityFrom(damagesource, i);
			}
			if (i > 0.0f) {
				final List<EntityNPCInterface> inRange = worldObj.getEntitiesWithinAABB(
						(Class) EntityNPCInterface.class, getEntityBoundingBox().expand(32.0, 16.0, 32.0));
				for (final EntityNPCInterface npc2 : inRange) {
					if (!npc2.isKilled() && npc2.advanced.defendFaction) {
						if (npc2.faction.id != faction.id) {
							continue;
						}
						if (!npc2.canSee(this) && !npc2.ai.directLOS && !npc2.canSee(attackingEntity)) {
							continue;
						}
						npc2.onAttack(attackingEntity);
					}
				}
				setAttackTarget(attackingEntity);
			}
			return super.attackEntityFrom(damagesource, i);
		} finally {
			if (event.clearTarget) {
				setAttackTarget(null);
				setRevengeTarget((EntityLivingBase) null);
			}
		}
	}

	@Override
	public void attackEntityWithRangedAttack(final EntityLivingBase entity, final float f) {
		final ItemStack proj = ItemStackWrapper.MCItem(inventory.getProjectile());
		if (proj == null) {
			updateAI = true;
			return;
		}
		final NpcEvent.RangedLaunchedEvent event = new NpcEvent.RangedLaunchedEvent(wrappedNPC, entity,
				stats.ranged.getStrength());
		if (EventHooks.onNPCRangedLaunched(this, event)) {
			return;
		}
		for (int i = 0; i < stats.ranged.getShotCount(); ++i) {
			final EntityProjectile projectile = this.shoot(entity, stats.ranged.getAccuracy(), proj, f == 1.0f);
			projectile.damage = event.damage;
			projectile.callback = new IProjectileCallback() {
				@Override
				public boolean onImpact(final EntityProjectile projectile, final BlockPos pos, final Entity entity) {
					projectile.playSound(stats.ranged.getSound((entity != null) ? 1 : 2), 1.0f,
							1.2f / ((EntityNPCInterface.this.getRNG().nextFloat() * 0.2f) + 0.9f));
					return false;
				}
			};
		}
		playSound(stats.ranged.getSound(0), 2.0f, 1.0f);
	}

	private double calculateStartYPos(BlockPos pos) {
		while (pos.getY() > 0) {
			final IBlockState state = worldObj.getBlockState(pos);
			final AxisAlignedBB bb = state.getBlock().getCollisionBoundingBox(worldObj, pos, state);
			if (bb != null) {
				return bb.maxY;
			}
			pos = pos.down();
		}
		return 0.0;
	}

	private BlockPos calculateTopPos(final BlockPos pos) {
		for (BlockPos check = pos; check.getY() > 0; check = check.down()) {
			final IBlockState state = worldObj.getBlockState(pos);
			final AxisAlignedBB bb = state.getBlock().getCollisionBoundingBox(worldObj, pos, state);
			if (bb != null) {
				return check;
			}
		}
		return pos;
	}

	@Override
	public boolean canAttackClass(final Class par1Class) {
		return EntityBat.class != par1Class;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isKilled();
	}

	@Override
	public boolean canCommandSenderUseCommand(final int var1, final String var2) {
		return CustomNpcs.NpcUseOpCommands || (var1 <= 2);
	}

	@Override
	protected boolean canDespawn() {
		return stats.spawnCycle == 4;
	}

	public boolean canFly() {
		return false;
	}

	public boolean canSee(final Entity entity) {
		return getEntitySenses().canSee(entity);
	}

	private void clearTasks(final EntityAITasks tasks) {
		tasks.taskEntries.iterator();
		final List<EntityAITasks.EntityAITaskEntry> list = new ArrayList<EntityAITasks.EntityAITaskEntry>(
				tasks.taskEntries);
		for (final EntityAITasks.EntityAITaskEntry entityaitaskentry : list) {
			this.tasks.removeTask(entityaitaskentry.action);
		}
		tasks.taskEntries = new ArrayList();
	}

	public void cloakUpdate() {
		field_20066_r = field_20063_u;
		field_20065_s = field_20062_v;
		field_20064_t = field_20061_w;
		final double d = posX - field_20063_u;
		final double d2 = posY - field_20062_v;
		final double d3 = posZ - field_20061_w;
		final double d4 = 10.0;
		if (d > d4) {
			final double posX = this.posX;
			field_20063_u = posX;
			field_20066_r = posX;
		}
		if (d3 > d4) {
			final double posZ = this.posZ;
			field_20061_w = posZ;
			field_20064_t = posZ;
		}
		if (d2 > d4) {
			final double posY = this.posY;
			field_20062_v = posY;
			field_20065_s = posY;
		}
		if (d < -d4) {
			final double posX2 = posX;
			field_20063_u = posX2;
			field_20066_r = posX2;
		}
		if (d3 < -d4) {
			final double posZ2 = posZ;
			field_20061_w = posZ2;
			field_20064_t = posZ2;
		}
		if (d2 < -d4) {
			final double posY2 = posY;
			field_20062_v = posY2;
			field_20065_s = posY2;
		}
		field_20063_u += d * 0.25;
		field_20061_w += d3 * 0.25;
		field_20062_v += d2 * 0.25;
	}

	@Override
	protected int decreaseAirSupply(final int par1) {
		if (!stats.canDrown) {
			return par1;
		}
		return super.decreaseAirSupply(par1);
	}

	public void delete() {
		if ((advanced.role != 0) && (roleInterface != null)) {
			roleInterface.delete();
		}
		if ((advanced.job != 0) && (jobInterface != null)) {
			jobInterface.delete();
		}
		super.setDead();
	}

	public void doorInteractType() {
		if (canFly()) {
			return;
		}
		EntityAIBase aiDoor = null;
		if (ai.doorInteract == 1) {
			tasks.addTask(taskCount++, aiDoor = new EntityAIOpenDoor(this, true));
		} else if (ai.doorInteract == 0) {
			tasks.addTask(taskCount++, aiDoor = new EntityAIBustDoor(this));
		}
		if (getNavigator() instanceof PathNavigateGround) {
			((PathNavigateGround) getNavigator()).setBreakDoors(aiDoor != null);
		}
	}

	@Override
	protected void dropEquipment(final boolean p_82160_1_, final int p_82160_2_) {
	}

	@Override
	protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(13, String.valueOf(""));
		dataWatcher.addObject(14, 0);
		dataWatcher.addObject(25, 0);
		dataWatcher.addObject(16, String.valueOf(""));
		dataWatcher.addObject(17, String.valueOf(""));
	}

	@Override
	public void fall(final float distance, final float modifier) {
		if (!stats.noFallDamage) {
			super.fall(distance, modifier);
		}
	}

	public int followRange() {
		if (advanced.scenes.getOwner() != null) {
			return 4;
		}
		if ((advanced.role == 2) && ((RoleFollower) roleInterface).isFollowing()) {
			return 6;
		}
		if ((advanced.role == 6) && ((RoleCompanion) roleInterface).isFollowing()) {
			return 4;
		}
		if ((advanced.job == 5) && ((JobFollower) jobInterface).isFollowing()) {
			return 4;
		}
		return 15;
	}

	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return true;
	}

	@Override
	public float getBlockPathWeight(final BlockPos pos) {
		float weight = worldObj.getLightBrightness(pos) - 0.5f;
		final Block block = worldObj.getBlockState(pos).getBlock();
		if (block.isOpaqueCube()) {
			weight += 10.0f;
		}
		return weight;
	}

	public boolean getBoolFlag(final int id) {
		return (dataWatcher.getWatchableObjectInt(25) & id) != 0x0;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return stats.creatureType;
	}

	@Override
	public ItemStack getCurrentArmor(final int slot) {
		return ItemStackWrapper.MCItem(inventory.getArmor(3 - slot));
	}

	@Override
	public String getDeathSound() {
		return advanced.getSound(3);
	}

	private Dialog getDialog(final EntityPlayer player) {
		for (final DialogOption option : dialogs.values()) {
			if (option == null) {
				continue;
			}
			if (!option.hasDialog()) {
				continue;
			}
			final Dialog dialog = option.getDialog();
			if (dialog.availability.isAvailable(player)) {
				return dialog;
			}
		}
		return null;
	}

	@Override
	public World getEntityWorld() {
		return worldObj;
	}

	@Override
	public ItemStack getEquipmentInSlot(final int slot) {
		if (slot == 0) {
			return ItemStackWrapper.MCItem(inventory.weapons.get(0));
		}
		return ItemStackWrapper.MCItem(inventory.getArmor(4 - slot));
	}

	public Faction getFaction() {
		final String[] split = dataWatcher.getWatchableObjectString(13).split(":");
		int faction = 0;
		if ((worldObj == null) || ((split.length <= 1) && worldObj.isRemote)) {
			return new Faction();
		}
		if (split.length > 1) {
			faction = Integer.parseInt(split[0]);
		}
		if (worldObj.isRemote) {
			final Faction fac = new Faction();
			fac.id = faction;
			fac.color = Integer.parseInt(split[1]);
			fac.name = split[2];
			return fac;
		}
		Faction fac = FactionController.getInstance().getFaction(faction);
		if (fac == null) {
			faction = FactionController.getInstance().getFirstFactionId();
			fac = FactionController.getInstance().getFaction(faction);
		}
		return fac;
	}

	public EntityPlayerMP getFakePlayer() {
		if (worldObj.isRemote) {
			return null;
		}
		if (EntityNPCInterface.chateventPlayer == null) {
			EntityNPCInterface.chateventPlayer = new FakePlayer((WorldServer) worldObj,
					(GameProfile) EntityNPCInterface.chateventProfile);
		}
		EntityUtil.Copy(this, EntityNPCInterface.chateventPlayer);
		EntityNPCInterface.chateventProfile.npc = this;
		EntityNPCInterface.chateventPlayer.refreshDisplayName();
		return EntityNPCInterface.chateventPlayer;
	}

	@Override
	public ItemStack getHeldItem() {
		if (inventory.renderOffhand != null) {
			return inventory.renderOffhand;
		}
		IItemStack item = null;
		if (isAttacking()) {
			item = inventory.getRightHand();
		} else if (advanced.role == 6) {
			item = ((RoleCompanion) roleInterface).getHeldItem();
		} else if ((jobInterface != null) && jobInterface.overrideMainHand) {
			item = jobInterface.getMainhand();
		} else {
			item = inventory.getRightHand();
		}
		return ItemStackWrapper.MCItem(item);
	}

	@Override
	public String getHurtSound() {
		return advanced.getSound(2);
	}

	@Override
	public ItemStack[] getInventory() {
		final ItemStack[] inv = new ItemStack[5];
		for (int i = 0; i < 5; ++i) {
			inv[i] = getEquipmentInSlot(i);
		}
		return inv;
	}

	@Override
	public boolean getLeashed() {
		return false;
	}

	@Override
	protected String getLivingSound() {
		if (!isEntityAlive()) {
			return null;
		}
		return advanced.getSound((getAttackTarget() != null) ? 1 : 0);
	}

	@Override
	public String getName() {
		return display.getName();
	}

	public ItemStack getOffHand() {
		IItemStack item = null;
		if (isAttacking()) {
			item = inventory.getLeftHand();
		} else if ((jobInterface != null) && jobInterface.overrideOffHand) {
			item = jobInterface.getOffhand();
		} else {
			item = inventory.getLeftHand();
		}
		return ItemStackWrapper.MCItem(item);
	}

	public EntityLivingBase getOwner() {
		if (advanced.scenes.getOwner() != null) {
			return advanced.scenes.getOwner();
		}
		if ((advanced.role == 2) && (roleInterface instanceof RoleFollower)) {
			return ((RoleFollower) roleInterface).owner;
		}
		if ((advanced.role == 6) && (roleInterface instanceof RoleCompanion)) {
			return ((RoleCompanion) roleInterface).owner;
		}
		if ((advanced.job == 5) && (jobInterface instanceof JobFollower)) {
			return ((JobFollower) jobInterface).following;
		}
		return null;
	}

	@Override
	public BlockPos getPosition() {
		return new BlockPos(posX, posY, posZ);
	}

	@Override
	public Vec3 getPositionVector() {
		return new Vec3(posX, posY, posZ);
	}

	public EntityAIRangedAttack getRangedTask() {
		return aiRange;
	}

	public String getRoleDataWatcher() {
		return dataWatcher.getWatchableObjectString(16);
	}

	@Override
	protected float getSoundPitch() {
		if (advanced.disablePitch) {
			return 1.0f;
		}
		return super.getSoundPitch();
	}

	public float getSpeed() {
		return ai.getWalkingSpeed() / 20.0f;
	}

	public float getStartXPos() {
		return ai.startPos().getX() + (ai.bodyOffsetX / 10.0f);
	}

	public double getStartYPos() {
		if (startYPos < 0.0) {
			return calculateStartYPos(ai.startPos());
		}
		return startYPos;
	}

	public float getStartZPos() {
		return ai.startPos().getZ() + (ai.bodyOffsetZ / 10.0f);
	}

	@Override
	public int getTalkInterval() {
		return 160;
	}

	public void givePlayerItem(final EntityPlayer player, ItemStack item) {
		if (worldObj.isRemote) {
			return;
		}
		item = item.copy();
		final float f = 0.7f;
		final double d = (worldObj.rand.nextFloat() * f) + (1.0f - f);
		final double d2 = (worldObj.rand.nextFloat() * f) + (1.0f - f);
		final double d3 = (worldObj.rand.nextFloat() * f) + (1.0f - f);
		final EntityItem entityitem = new EntityItem(worldObj, posX + d, posY + d2, posZ + d3, item);
		entityitem.setPickupDelay(2);
		worldObj.spawnEntityInWorld(entityitem);
		final int i = item.stackSize;
		if (player.inventory.addItemStackToInventory(item)) {
			worldObj.playSoundAtEntity(entityitem, "random.pop", 0.2f,
					(((rand.nextFloat() - rand.nextFloat()) * 0.7f) + 1.0f) * 2.0f);
			player.onItemPickup(entityitem, i);
			if (item.stackSize <= 0) {
				entityitem.setDead();
			}
		}
	}

	public boolean hasOwner() {
		return (advanced.scenes.getOwner() != null)
				|| ((advanced.role == 2) && ((RoleFollower) roleInterface).hasOwner())
				|| ((advanced.role == 6) && ((RoleCompanion) roleInterface).hasOwner())
				|| ((advanced.job == 5) && ((JobFollower) jobInterface).hasOwner());
	}

	@Override
	public boolean interact(final EntityPlayer player) {
		if (worldObj.isRemote) {
			return false;
		}
		final ItemStack currentItem = player.inventory.getCurrentItem();
		if (currentItem != null) {
			final Item item = currentItem.getItem();
			if ((item == CustomItems.cloner) || (item == CustomItems.wand) || (item == CustomItems.mount)
					|| (item == CustomItems.scripter)) {
				setAttackTarget(null);
				setRevengeTarget((EntityLivingBase) null);
				return true;
			}
			if (item == CustomItems.moving) {
				setAttackTarget(null);
				currentItem.setTagInfo("NPCID", new NBTTagInt(getEntityId()));
				player.addChatMessage(
						new ChatComponentTranslation("Registered " + getName() + " to your NPC Pather", new Object[0]));
				return true;
			}
		}
		if (EventHooks.onNPCInteract(this, player)) {
			return false;
		}
		addInteract(player);
		final Dialog dialog = getDialog(player);
		final PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		final QuestData data = playerdata.getQuestCompletion(player, this);
		if (data != null) {
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.QUEST_COMPLETION,
					data.quest.writeToNBT(new NBTTagCompound()));
		} else if (dialog != null) {
			NoppesUtilServer.openDialog(player, this, dialog);
		} else if (roleInterface != null) {
			roleInterface.interact(player);
		} else {
			say(player, advanced.getInteractLine());
		}
		return true;
	}

	public boolean isAttacking() {
		return getBoolFlag(4);
	}

	@Override
	public boolean isEntityAlive() {
		return super.isEntityAlive() && !isKilled();
	}

	public boolean isFollower() {
		return (advanced.scenes.getOwner() != null)
				|| ((advanced.role == 2) && ((RoleFollower) roleInterface).isFollowing())
				|| ((advanced.role == 6) && ((RoleCompanion) roleInterface).isFollowing())
				|| ((advanced.job == 5) && ((JobFollower) jobInterface).isFollowing());
	}

	public boolean isInRange(final double posX, final double posY, final double posZ, final double range) {
		double x = this.posX - posX;
		double y = this.posY - posY;
		double z = this.posZ - posZ;
		if (x < 0.0) {
			x = -x;
		}
		if (z < 0.0) {
			z = -z;
		}
		if (y < 0.0) {
			y = -y;
		}
		return ((posY < 0.0) || (y <= range)) && (x <= range) && (z <= range);
	}

	public boolean isInRange(final Entity entity, final double range) {
		return this.isInRange(entity.posX, entity.posY, entity.posZ, range);
	}

	public boolean isInteracting() {
		return ((ticksExisted - lastInteract) < 40) || (isRemote() && getBoolFlag(2))
				|| (ai.stopAndInteract && !interactingEntities.isEmpty() && ((ticksExisted - lastInteract) < 180));
	}

	@Override
	public boolean isInvisible() {
		return display.getVisible() != 0;
	}

	@Override
	public boolean isInvisibleToPlayer(final EntityPlayer player) {
		return (display.getVisible() == 1)
				&& ((player.getHeldItem() == null) || (player.getHeldItem().getItem() != CustomItems.wand));
	}

	public boolean isKilled() {
		return getBoolFlag(8) || isDead;
	}

	@Override
	public boolean isOnSameTeam(final EntityLivingBase entity) {
		if (!isRemote()) {
			if ((entity instanceof EntityPlayer) && getFaction().isFriendlyToPlayer((EntityPlayer) entity)) {
				return true;
			}
			if (entity == getOwner()) {
				return true;
			}
			if ((entity instanceof EntityNPCInterface) && (((EntityNPCInterface) entity).faction.id == faction.id)) {
				return true;
			}
		}
		return super.isOnSameTeam(entity);
	}

	@Override
	public boolean isPlayerSleeping() {
		return (currentAnimation == 2) && !isAttacking();
	}

	@Override
	public boolean isPotionApplicable(final PotionEffect effect) {
		return !stats.potionImmune && ((getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD)
				|| (effect.getPotionID() != Potion.poison.id)) && super.isPotionApplicable(effect);
	}

	public boolean isRemote() {
		return (worldObj == null) || worldObj.isRemote;
	}

	@Override
	public boolean isRiding() {
		return ((currentAnimation == 1) && !isAttacking()) || (ridingEntity != null);
	}

	@Override
	public boolean isSneaking() {
		return currentAnimation == 4;
	}

	public boolean isVeryNearAssignedPlace() {
		final double xx = posX - getStartXPos();
		final double zz = posZ - getStartZPos();
		return (xx >= -0.2) && (xx <= 0.2) && (zz >= -0.2) && (zz <= 0.2);
	}

	public boolean isWalking() {
		return (ai.getMovingType() != 0) || isAttacking() || isFollower() || getBoolFlag(1);
	}

	@Override
	public void knockBack(final Entity par1Entity, final float par2, final double par3, final double par5) {
		if (stats.resistances.knockback >= 2.0f) {
			return;
		}
		isAirBorne = true;
		final float f1 = MathHelper.sqrt_double((par3 * par3) + (par5 * par5));
		final float f2 = 0.5f * (2.0f - stats.resistances.knockback);
		motionX /= 2.0;
		motionY /= 2.0;
		motionZ /= 2.0;
		motionX -= (par3 / f1) * f2;
		motionY += 0.2 + (f2 / 2.0f);
		motionZ -= (par5 / f1) * f2;
		if (motionY > 0.4000000059604645) {
			motionY = 0.4000000059604645;
		}
	}

	@Override
	public void moveEntityWithHeading(final float p_70612_1_, final float p_70612_2_) {
		final double d0 = posX;
		final double d2 = posY;
		final double d3 = posZ;
		super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		if ((advanced.role == 6) && !isRemote()) {
			((RoleCompanion) roleInterface).addMovementStat(posX - d0, posY - d2, posZ - d3);
		}
	}

	public boolean nearPosition(final BlockPos pos) {
		final BlockPos npcpos = getPosition();
		final float x = npcpos.getX() - pos.getX();
		final float z = npcpos.getZ() - pos.getZ();
		final float y = npcpos.getY() - pos.getY();
		final float height = MathHelper.ceiling_double_int(this.height + 1.0f)
				* MathHelper.ceiling_double_int(this.height + 1.0f);
		return (((x * x) + (z * z)) < 2.5) && ((y * y) < (height + 2.5));
	}

	public void onAttack(final EntityLivingBase entity) {
		if ((entity == null) || (entity == this) || isAttacking() || (ai.onAttack == 3) || (entity == getOwner())) {
			return;
		}
		super.setAttackTarget(entity);
	}

	public void onCollide() {
		if (!isEntityAlive() || ((ticksExisted % 4) != 0) || worldObj.isRemote) {
			return;
		}
		AxisAlignedBB axisalignedbb = null;
		if ((ridingEntity != null) && ridingEntity.isEntityAlive()) {
			axisalignedbb = getEntityBoundingBox().union(ridingEntity.getEntityBoundingBox()).expand(1.0, 0.0, 1.0);
		} else {
			axisalignedbb = getEntityBoundingBox().expand(1.0, 0.5, 1.0);
		}
		final List list = worldObj.getEntitiesWithinAABB((Class) EntityLivingBase.class, axisalignedbb);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.size(); ++i) {
			final Entity entity = (Entity) list.get(i);
			if ((entity != this) && entity.isEntityAlive()) {
				EventHooks.onNPCCollide(this, entity);
			}
		}
	}

	@Override
	public void onDeath(final DamageSource damagesource) {
		setSprinting(false);
		getNavigator().clearPathEntity();
		extinguish();
		clearActivePotions();
		if (!isRemote()) {
			Entity attackingEntity = damagesource.getSourceOfDamage();
			if ((attackingEntity instanceof EntityArrow)
					&& (((EntityArrow) attackingEntity).shootingEntity instanceof EntityLivingBase)) {
				attackingEntity = ((EntityArrow) attackingEntity).shootingEntity;
			} else if (attackingEntity instanceof EntityThrowable) {
				attackingEntity = ((EntityThrowable) attackingEntity).getThrower();
			}
			if (EventHooks.onNPCDied(this, attackingEntity, damagesource)) {
				return;
			}
			inventory.dropStuff(attackingEntity, damagesource);
			final Line line = advanced.getKilledLine();
			if (line != null) {
				saySurrounding(line.formatTarget(
						(EntityLivingBase) ((attackingEntity instanceof EntityLivingBase) ? attackingEntity : null)));
			}
		}
		super.onDeath(damagesource);
	}

	@Override
	public void onDeathUpdate() {
		if (stats.spawnCycle == 3) {
			super.onDeathUpdate();
			return;
		}
		++deathTime;
		if (worldObj.isRemote) {
			return;
		}
		if (!hasDied) {
			setDead();
		}
		if ((killedtime < System.currentTimeMillis())
				&& ((stats.spawnCycle == 0) || (worldObj.isDaytime() && (stats.spawnCycle == 1))
						|| (!worldObj.isDaytime() && (stats.spawnCycle == 2)))) {
			reset();
		}
	}

	@Override
	public void onLivingUpdate() {
		if (CustomNpcs.FreezeNPCs) {
			return;
		}
		++totalTicksAlive;
		updateArmSwingProgress();
		if ((ticksExisted % 20) == 0) {
			faction = getFaction();
		}
		if (!worldObj.isRemote) {
			if (!isKilled() && ((ticksExisted % 20) == 0)) {
				advanced.scenes.update();
				if (getHealth() < getMaxHealth()) {
					if ((stats.healthRegen > 0) && !isAttacking()) {
						heal(stats.healthRegen);
					}
					if ((stats.combatRegen > 0) && isAttacking()) {
						heal(stats.combatRegen);
					}
				}
				if (faction.getsAttacked && !isAttacking()) {
					final List<EntityMob> list = worldObj.getEntitiesWithinAABB((Class) EntityMob.class,
							getEntityBoundingBox().expand(16.0, 16.0, 16.0));
					for (final EntityMob mob : list) {
						if ((mob.getAttackTarget() == null) && canSee(mob)) {
							if ((mob instanceof EntityZombie) && !mob.getEntityData().hasKey("AttackNpcs")) {
								mob.tasks.addTask(2,
										new EntityAIAttackOnCollide(mob, EntityLivingBase.class, 1.0, false));
								mob.getEntityData().setBoolean("AttackNpcs", true);
							}
							mob.setAttackTarget(this);
						}
					}
				}
				if ((linkedData != null) && (linkedData.time > linkedLast)) {
					LinkedNpcController.Instance.loadNpcData(this);
				}
				if (updateClient) {
					final NBTTagCompound compound = this.writeSpawnData();
					compound.setInteger("EntityId", getEntityId());
					Server.sendAssociatedData((Entity) this, EnumPacketClient.UPDATE_NPC, compound);
					updateClient = false;
				}
				if (updateAI) {
					updateTasks();
					updateAI = false;
				}
			}
			if (getHealth() <= 0.0f) {
				clearActivePotions();
				setBoolFlag(true, 8);
			}
			setBoolFlag(getAttackTarget() != null, 4);
			setBoolFlag(!getNavigator().noPath(), 1);
			setBoolFlag(isInteracting(), 2);
			onCollide();
		}
		if ((wasKilled != isKilled()) && wasKilled) {
			reset();
		}
		wasKilled = isKilled();
		if (worldObj.isDaytime() && !worldObj.isRemote && stats.burnInSun) {
			final float f = getBrightness(1.0f);
			if ((f > 0.5f) && ((rand.nextFloat() * 30.0f) < ((f - 0.4f) * 2.0f))
					&& worldObj.canBlockSeeSky(new BlockPos(this))) {
				setFire(8);
			}
		}
		super.onLivingUpdate();
		if (worldObj.isRemote) {
			if (roleInterface != null) {
				roleInterface.clientUpdate();
			}
			if (textureCloakLocation != null) {
				cloakUpdate();
			}
			if (currentAnimation != dataWatcher.getWatchableObjectInt(14)) {
				currentAnimation = dataWatcher.getWatchableObjectInt(14);
				animationStart = ticksExisted;
				updateHitbox();
			}
			if (advanced.job == 1) {
				((JobBard) jobInterface).onLivingUpdate();
			}
		}
	}

	@Override
	public void onUpdate() {
		startYPos = calculateStartYPos(ai.startPos()) + 1.0;
		if ((startYPos < 0.0) && !isRemote()) {
			setDead();
		}
		super.onUpdate();
		if ((ticksExisted % 10) == 0) {
			EventHooks.onNPCTick(this);
		}
		timers.update();
	}

	@Override
	protected void playStepSound(final BlockPos pos, final Block block) {
		final String sound = advanced.getSound(4);
		if (sound != null) {
			playSound(sound, 0.15f, 1.0f);
		} else {
			super.playStepSound(pos, block);
		}
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		npcVersion = compound.getInteger("ModRev");
		VersionCompatibility.CheckNpcCompatibility(this, compound);
		display.readToNBT(compound);
		stats.readToNBT(compound);
		ai.readToNBT(compound);
		script.readFromNBT(compound);
		timers.readFromNBT(compound);
		advanced.readToNBT(compound);
		if ((advanced.role != 0) && (roleInterface != null)) {
			roleInterface.readFromNBT(compound);
		}
		if ((advanced.job != 0) && (jobInterface != null)) {
			jobInterface.readFromNBT(compound);
		}
		inventory.readEntityFromNBT(compound);
		transform.readToNBT(compound);
		killedtime = compound.getLong("KilledTime");
		totalTicksAlive = compound.getLong("TotalTicksAlive");
		linkedName = compound.getString("LinkedNpcName");
		if (!isRemote()) {
			LinkedNpcController.Instance.loadNpcData(this);
		}
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(CustomNpcs.NpcNavRange);
		updateAI = true;
	}

	@Override
	public void readSpawnData(final ByteBuf buf) {
		try {
			this.readSpawnData(Server.readNBT(buf));
		} catch (IOException ex) {
		}
	}

	public void readSpawnData(final NBTTagCompound compound) {
		stats.maxHealth = compound.getInteger("MaxHealth");
		ai.setWalkingSpeed(compound.getInteger("Speed"));
		stats.hideKilledBody = compound.getBoolean("DeadBody");
		ai.setStandingType(compound.getInteger("StandingState"));
		ai.setMovingType(compound.getInteger("MovingState"));
		ai.orientation = compound.getInteger("Orientation");
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(stats.maxHealth);
		inventory.armor = NBTTags.getIItemStackMap(compound.getTagList("Armor", 10));
		inventory.weapons = NBTTags.getIItemStackMap(compound.getTagList("Weapons", 10));
		advanced.setRole(compound.getInteger("Role"));
		advanced.setJob(compound.getInteger("Job"));
		if (advanced.job == 1) {
			final NBTTagCompound bard = compound.getCompoundTag("Bard");
			jobInterface.readFromNBT(bard);
		}
		if (advanced.job == 9) {
			final NBTTagCompound puppet = compound.getCompoundTag("Puppet");
			jobInterface.readFromNBT(puppet);
		}
		if (advanced.role == 6) {
			final NBTTagCompound puppet = compound.getCompoundTag("Companion");
			roleInterface.readFromNBT(puppet);
		}
		if (this instanceof EntityCustomNpc) {
			((EntityCustomNpc) this).modelData.readFromNBT(compound.getCompoundTag("ModelData"));
		}
		display.readToNBT(compound);
	}

	public void reset() {
		hasDied = false;
		isDead = false;
		setSprinting(wasKilled = false);
		setHealth(getMaxHealth());
		dataWatcher.updateObject(14, 0);
		dataWatcher.updateObject(25, 0);
		setAttackTarget(null);
		setRevengeTarget((EntityLivingBase) null);
		deathTime = 0;
		if (ai.returnToStart && !hasOwner() && !isRemote()) {
			setLocationAndAngles(getStartXPos(), getStartYPos(), getStartZPos(), rotationYaw, rotationPitch);
		}
		killedtime = 0L;
		extinguish();
		clearActivePotions();
		moveEntityWithHeading(0.0f, 0.0f);
		distanceWalkedModified = 0.0f;
		getNavigator().clearPathEntity();
		currentAnimation = 0;
		updateHitbox();
		updateAI = true;
		ai.movingPos = 0;
		if (getOwner() != null) {
			getOwner().setLastAttacker((Entity) null);
		}
		if (jobInterface != null) {
			jobInterface.reset();
		}
		EventHooks.onNPCInit(this);
	}

	public void say(final EntityPlayer player, final Line line) {
		if ((line == null) || !canSee(player) || (line.text == null)) {
			return;
		}
		if (!line.sound.isEmpty()) {
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.PLAY_SOUND, line.sound, (float) posX,
					(float) posY, (float) posZ);
		}
		LogWriter.info("Text: " + line.text);
		Server.sendData((EntityPlayerMP) player, EnumPacketClient.CHATBUBBLE, getEntityId(), line.text, !line.hideText);
	}

	public void saySurrounding(final Line line) {
		if ((line == null) || (line.text == null)) {
			return;
		}
		LogWriter.info("Text Surround Before: " + line.text);
		final ServerChatEvent event = new ServerChatEvent(getFakePlayer(), line.text,
				new ChatComponentTranslation(line.text.replace("%", "%%"), new Object[0]));
		if (MinecraftForge.EVENT_BUS.post(event) || (event.component == null)) {
			return;
		}
		//why does this line exist? it just kept prepending the npc name...
		//line.text = event.component.getUnformattedText().replace("%%", "%");
		final List<EntityPlayer> inRange = worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				getEntityBoundingBox().expand(20.0, 20.0, 20.0));
		LogWriter.info("Text Surround After: " + line.text);
		for (final EntityPlayer player : inRange) {
			say(player, line);
		}
	}

	public void seekShelter() {
		if (ai.findShelter == 0) {
			tasks.addTask(taskCount++, new EntityAIMoveIndoors(this));
		} else if (ai.findShelter == 1) {
			if (!canFly()) {
				tasks.addTask(taskCount++, new EntityAIRestrictSun(this));
			}
			tasks.addTask(taskCount++, new EntityAIFindShade(this));
		}
	}

	@Override
	public void setAttackTarget(EntityLivingBase entity) {
		if (((entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.disableDamage)
				|| ((entity != null) && (entity == getOwner())) || (getAttackTarget() == entity)) {
			return;
		}
		if (entity != null) {
			final NpcEvent.TargetEvent event = new NpcEvent.TargetEvent(wrappedNPC, entity);
			if (EventHooks.onNPCTarget(this, event)) {
				return;
			}
			if (event.entity == null) {
				entity = null;
			} else {
				entity = event.entity.getMCEntity();
			}
		} else if (EventHooks.onNPCTargetLost(this, getAttackTarget())) {
			return;
		}
		if ((entity != null) && (entity != this) && (ai.onAttack != 3) && !isAttacking() && !isRemote()) {
			final Line line = advanced.getAttackLine();
			if (line != null) {
				saySurrounding(line.formatTarget(entity));
			}
		}
		super.setAttackTarget(entity);
	}

	public void setBoolFlag(final boolean bo, final int id) {
		final int i = dataWatcher.getWatchableObjectInt(25);
		if (bo && ((i & id) == 0x0)) {
			dataWatcher.updateObject(25, (i | id));
		}
		if (!bo && ((i & id) != 0x0)) {
			dataWatcher.updateObject(25, (i - id));
		}
	}

	public void setCurrentAnimation(final int animation) {
		currentAnimation = animation;
		dataWatcher.updateObject(14, animation);
	}

	@Override
	public void setCurrentItemOrArmor(final int slot, final ItemStack item) {
		if (slot == 0) {
			inventory.weapons.put(0, (item == null) ? null : new ItemStackWrapper(item));
		} else {
			inventory.armor.put(4 - slot, (item == null) ? null : new ItemStackWrapper(item));
		}
	}

	public void setDataWatcher(final DataWatcher dataWatcher) {
		this.dataWatcher = dataWatcher;
	}

	@Override
	public void setDead() {
		hasDied = true;
		if (worldObj.isRemote || (stats.spawnCycle == 3)) {
			spawnExplosionParticle();
			delete();
		} else {
			if (riddenByEntity != null) {
				riddenByEntity.mountEntity((Entity) null);
			}
			if (ridingEntity != null) {
				mountEntity((Entity) null);
			}
			setHealth(-1.0f);
			setSprinting(false);
			getNavigator().clearPathEntity();
			setCurrentAnimation(2);
			updateHitbox();
			if (killedtime <= 0L) {
				killedtime = (stats.respawnTime * 1000) + System.currentTimeMillis();
			}
			if ((advanced.role != 0) && (roleInterface != null)) {
				roleInterface.killed();
			}
			if ((advanced.job != 0) && (jobInterface != null)) {
				jobInterface.killed();
			}
		}
	}

	public void setFaction(final int integer) {
		if ((integer < 0) || isRemote()) {
			return;
		}
		final Faction faction = FactionController.getInstance().getFaction(integer);
		if (faction == null) {
			return;
		}
		String str = faction.id + ":" + faction.color + ":" + faction.name;
		if (str.length() > 64) {
			str = str.substring(0, 64);
		}
		dataWatcher.updateObject(13, str);
	}

	@Override
	public void setHomePosAndDistance(final BlockPos pos, final int range) {
		super.setHomePosAndDistance(pos, range);
		ai.setStartPos(pos);
	}

	public void setImmuneToFire(final boolean immuneToFire) {
		isImmuneToFire = immuneToFire;
		stats.immuneToFire = immuneToFire;
	}

	@Override
	public void setInWeb() {
		if (!stats.ignoreCobweb) {
			super.setInWeb();
		}
	}

	public void setMoveType() {
		if (ai.getMovingType() == 1) {
			tasks.addTask(taskCount++, new EntityAIWander(this));
		}
		if (ai.getMovingType() == 2) {
			tasks.addTask(taskCount++, new EntityAIMovingPath(this));
		}
	}

	@Override
	public void setPortal(final BlockPos pos) {
	}

	private void setResponse() {
		final EntityAIRangedAttack entityAIRangedAttack = null;
		aiRange = entityAIRangedAttack;
		aiAttackTarget = entityAIRangedAttack;
		if (ai.canSprint) {
			tasks.addTask(taskCount++, new EntityAISprintToTarget(this));
		}
		if (ai.onAttack == 1) {
			tasks.addTask(taskCount++, new EntityAIPanic(this, 1.2f));
		} else if (ai.onAttack == 2) {
			tasks.addTask(taskCount++, new EntityAIAvoidTarget(this));
		} else if (ai.onAttack == 0) {
			if (ai.canLeap) {
				tasks.addTask(taskCount++, new EntityAIPounceTarget(this));
			}
			if (inventory.getProjectile() == null) {
				switch (ai.tacticalVariant) {
				case 1: {
					tasks.addTask(taskCount++, new EntityAIZigZagTarget(this, 1.3));
					break;
				}
				case 2: {
					tasks.addTask(taskCount++, new EntityAIOrbitTarget(this, 1.3, true));
					break;
				}
				case 3: {
					tasks.addTask(taskCount++, new EntityAIAvoidTarget(this));
					break;
				}
				case 4: {
					tasks.addTask(taskCount++, new EntityAIAmbushTarget(this, 1.2));
					break;
				}
				case 5: {
					tasks.addTask(taskCount++, new EntityAIStalkTarget(this));
					break;
				}
				}
			} else {
				switch (ai.tacticalVariant) {
				case 1: {
					tasks.addTask(taskCount++, new EntityAIDodgeShoot(this));
					break;
				}
				case 2: {
					tasks.addTask(taskCount++, new EntityAIOrbitTarget(this, 1.3, false));
					break;
				}
				case 3: {
					tasks.addTask(taskCount++, new EntityAIAvoidTarget(this));
					break;
				}
				case 4: {
					tasks.addTask(taskCount++, new EntityAIAmbushTarget(this, 1.3));
					break;
				}
				case 5: {
					tasks.addTask(taskCount++, new EntityAIStalkTarget(this));
					break;
				}
				}
			}
			tasks.addTask(taskCount, aiAttackTarget = new EntityAIAttackTarget(this));
			((EntityAIAttackTarget) aiAttackTarget).navOverride(ai.tacticalVariant == 6);
			if (inventory.getProjectile() != null) {
				tasks.addTask(taskCount++, aiRange = new EntityAIRangedAttack(this));
				aiRange.navOverride(ai.tacticalVariant == 6);
			}
		} else if (ai.onAttack == 3) {
		}
	}

	public void setRoleDataWatcher(final String s) {
		dataWatcher.updateObject(16, s);
	}

	public EntityProjectile shoot(final double x, final double y, final double z, final int accuracy,
			final ItemStack proj, final boolean indirect) {
		final EntityProjectile projectile = new EntityProjectile(worldObj, this, proj.copy(), true);
		final double varX = x - posX;
		final double varY = y - (posY + getEyeHeight());
		final double varZ = z - posZ;
		final float varF = projectile.hasGravity() ? MathHelper.sqrt_double((varX * varX) + (varZ * varZ)) : 0.0f;
		final float angle = projectile.getAngleForXYZ(varX, varY, varZ, varF, indirect);
		final float acc = 20.0f - MathHelper.floor_float(accuracy / 5.0f);
		projectile.setThrowableHeading(varX, varY, varZ, angle, acc);
		worldObj.spawnEntityInWorld(projectile);
		return projectile;
	}

	public EntityProjectile shoot(final EntityLivingBase entity, final int accuracy, final ItemStack proj,
			final boolean indirect) {
		return this.shoot(entity.posX, entity.getEntityBoundingBox().minY + (entity.height / 2.0f), entity.posZ,
				accuracy, proj, indirect);
	}

	public void tpTo(final EntityLivingBase owner) {
		if (owner == null) {
			return;
		}
		final EnumFacing facing = owner.getHorizontalFacing().getOpposite();
		BlockPos pos = new BlockPos(owner.posX, owner.getEntityBoundingBox().minY, owner.posZ);
		pos = pos.add(facing.getFrontOffsetX(), 0, facing.getFrontOffsetZ());
		pos = calculateTopPos(pos);
		for (int i = -1; i < 2; ++i) {
			for (int j = 0; j < 3; ++j) {
				BlockPos check;
				if (facing.getFrontOffsetX() == 0) {
					check = pos.add(i, 0, j * facing.getFrontOffsetZ());
				} else {
					check = pos.add(j * facing.getFrontOffsetX(), 0, i);
				}
				check = calculateTopPos(check);
				if (!worldObj.getBlockState(check).getBlock().isFullBlock()
						&& !worldObj.getBlockState(check.up()).getBlock().isFullBlock()) {
					setLocationAndAngles(check.getX() + 0.5f, check.getY(), check.getZ() + 0.5f, rotationYaw,
							rotationPitch);
					getNavigator().clearPathEntity();
					break;
				}
			}
		}
	}

	public void updateHitbox() {
		if ((currentAnimation == 2) || (currentAnimation == 7)) {
			width = 0.8f;
			height = 0.4f;
		} else if (isRiding()) {
			width = 0.6f;
			height = baseHeight * 0.77f;
		} else {
			width = 0.6f;
			height = baseHeight;
		}
		width = (width / 5.0f) * display.getSize();
		height = (height / 5.0f) * display.getSize();
		setPosition(posX, posY, posZ);
	}

	private void updateTasks() {
		if ((worldObj == null) || worldObj.isRemote) {
			return;
		}
		clearTasks(tasks);
		clearTasks(targetTasks);
		final Predicate attackEntitySelector = new NPCAttackSelector(this);
		targetTasks.addTask(0, new EntityAIClearTarget(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		targetTasks.addTask(2,
				new EntityAIClosestTarget(this, EntityLivingBase.class, 4, ai.directLOS, false, attackEntitySelector));
		targetTasks.addTask(3, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(4, new EntityAIOwnerHurtTarget(this));
		if (canFly()) {
			moveHelper = new FlyingMoveHelper(this);
			navigator = new PathNavigateFlying(this, worldObj);
		} else {
			moveHelper = new EntityMoveHelper(this);
			navigator = new PathNavigateGround(this, worldObj);
			tasks.addTask(0, new EntityAIWaterNav(this));
		}
		taskCount = 1;
		addRegularEntries();
		doorInteractType();
		seekShelter();
		setResponse();
		setMoveType();
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		display.writeToNBT(compound);
		stats.writeToNBT(compound);
		ai.writeToNBT(compound);
		script.writeToNBT(compound);
		timers.writeToNBT(compound);
		advanced.writeToNBT(compound);
		if ((advanced.role != 0) && (roleInterface != null)) {
			roleInterface.writeToNBT(compound);
		}
		if ((advanced.job != 0) && (jobInterface != null)) {
			jobInterface.writeToNBT(compound);
		}
		inventory.writeEntityToNBT(compound);
		transform.writeToNBT(compound);
		compound.setLong("KilledTime", killedtime);
		compound.setLong("TotalTicksAlive", totalTicksAlive);
		compound.setInteger("ModRev", npcVersion);
		compound.setString("LinkedNpcName", linkedName);
	}

	public NBTTagCompound writeSpawnData() {
		final NBTTagCompound compound = new NBTTagCompound();
		display.writeToNBT(compound);
		compound.setInteger("MaxHealth", stats.maxHealth);
		compound.setTag("Armor", NBTTags.nbtIItemStackMap(inventory.armor));
		compound.setTag("Weapons", NBTTags.nbtIItemStackMap(inventory.weapons));
		compound.setInteger("Speed", ai.getWalkingSpeed());
		compound.setBoolean("DeadBody", stats.hideKilledBody);
		compound.setInteger("StandingState", ai.getStandingType());
		compound.setInteger("MovingState", ai.getMovingType());
		compound.setInteger("Orientation", ai.orientation);
		compound.setInteger("Role", advanced.role);
		compound.setInteger("Job", advanced.job);
		if (advanced.job == 1) {
			final NBTTagCompound bard = new NBTTagCompound();
			jobInterface.writeToNBT(bard);
			compound.setTag("Bard", bard);
		}
		if (advanced.job == 9) {
			final NBTTagCompound bard = new NBTTagCompound();
			jobInterface.writeToNBT(bard);
			compound.setTag("Puppet", bard);
		}
		if (advanced.role == 6) {
			final NBTTagCompound bard = new NBTTagCompound();
			roleInterface.writeToNBT(bard);
			compound.setTag("Companion", bard);
		}
		if (this instanceof EntityCustomNpc) {
			compound.setTag("ModelData", ((EntityCustomNpc) this).modelData.writeToNBT());
		}
		return compound;
	}

	@Override
	public void writeSpawnData(final ByteBuf buffer) {
		try {
			Server.writeNBT(buffer, this.writeSpawnData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
