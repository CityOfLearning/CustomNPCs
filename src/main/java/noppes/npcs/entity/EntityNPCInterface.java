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
import noppes.npcs.util.IProjectileCallback;
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
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.controllers.faction.Faction;
import noppes.npcs.controllers.faction.FactionController;
import noppes.npcs.controllers.lines.Line;
import noppes.npcs.controllers.quest.PlayerQuestData;
import noppes.npcs.controllers.quest.QuestData;
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

public abstract class EntityNPCInterface extends EntityCreature
		implements IEntityAdditionalSpawnData, ICommandSender, IRangedAttackMob, IBossDisplayData {
	public static int DWRole = 16;
	public static int DWJob = 17;
	public static int DWBool = 25;
	private static GameProfileAlt chateventProfile;
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

	public EntityNPCInterface(World world) {
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
		float scaleX = 0.9375f;
		scaleZ = scaleX;
		scaleY = scaleX;
		this.scaleX = scaleX;
		faction = getFaction();
		setFaction(faction.id);
		setSize(1.0f, 1.0f);
		updateAI = true;
	}

	@Override
	public void addChatMessage(IChatComponent var1) {
	}

	public void addInteract(EntityLivingBase entity) {
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
	public void addVelocity(double d, double d1, double d2) {
		if (isWalking() && !isKilled()) {
			super.addVelocity(d, d1, d2);
		}
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	@Override
	protected float applyArmorCalculations(DamageSource source, float damage) {
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
	public boolean attackEntityAsMob(Entity par1Entity) {
		float f = stats.melee.getStrength();
		if (stats.melee.getDelay() < 10) {
			par1Entity.hurtResistantTime = 0;
		}
		if (par1Entity instanceof EntityLivingBase) {
			NpcEvent.MeleeAttackEvent event = new NpcEvent.MeleeAttackEvent(wrappedNPC, (EntityLivingBase) par1Entity,
					f);
			if (EventHooks.onNPCAttacksMelee(this, event)) {
				return false;
			}
			f = event.damage;
		}
		boolean var4 = par1Entity.attackEntityFrom(new NpcDamageSource("mob", this), f);
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
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
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
		Entity entity = damagesource.getSourceOfDamage();
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
			EntityNPCInterface npc = (EntityNPCInterface) attackingEntity;
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
		NpcEvent.DamagedEvent event = new NpcEvent.DamagedEvent(wrappedNPC, attackingEntity, i, damagesource);
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
				List<EntityNPCInterface> inRange = worldObj.getEntitiesWithinAABB((Class) EntityNPCInterface.class,
						getEntityBoundingBox().expand(32.0, 16.0, 32.0));
				for (EntityNPCInterface npc2 : inRange) {
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

	private double calculateStartYPos(BlockPos pos) {
		while (pos.getY() > 0) {
			IBlockState state = worldObj.getBlockState(pos);
			AxisAlignedBB bb = state.getBlock().getCollisionBoundingBox(worldObj, pos, state);
			if (bb != null) {
				return bb.maxY;
			}
			pos = pos.down();
		}
		return 0.0;
	}

	private BlockPos calculateTopPos(BlockPos pos) {
		for (BlockPos check = pos; check.getY() > 0; check = check.down()) {
			IBlockState state = worldObj.getBlockState(pos);
			AxisAlignedBB bb = state.getBlock().getCollisionBoundingBox(worldObj, pos, state);
			if (bb != null) {
				return check;
			}
		}
		return pos;
	}

	@Override
	public boolean canAttackClass(Class par1Class) {
		return EntityBat.class != par1Class;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isKilled();
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return CustomNpcs.NpcUseOpCommands || (var1 <= 2);
	}

	@Override
	protected boolean canDespawn() {
		return stats.spawnCycle == 4;
	}

	public boolean canFly() {
		return false;
	}

	public boolean canSee(Entity entity) {
		return getEntitySenses().canSee(entity);
	}

	private void clearTasks(EntityAITasks tasks) {
		tasks.taskEntries.iterator();
		List<EntityAITasks.EntityAITaskEntry> list = new ArrayList<EntityAITasks.EntityAITaskEntry>(tasks.taskEntries);
		for (EntityAITasks.EntityAITaskEntry entityaitaskentry : list) {
			this.tasks.removeTask(entityaitaskentry.action);
		}
		tasks.taskEntries = new ArrayList();
	}

	public void cloakUpdate() {
		field_20066_r = field_20063_u;
		field_20065_s = field_20062_v;
		field_20064_t = field_20061_w;
		double d = posX - field_20063_u;
		double d2 = posY - field_20062_v;
		double d3 = posZ - field_20061_w;
		double d4 = 10.0;
		if (d > d4) {
			double posX = this.posX;
			field_20063_u = posX;
			field_20066_r = posX;
		}
		if (d3 > d4) {
			double posZ = this.posZ;
			field_20061_w = posZ;
			field_20064_t = posZ;
		}
		if (d2 > d4) {
			double posY = this.posY;
			field_20062_v = posY;
			field_20065_s = posY;
		}
		if (d < -d4) {
			double posX2 = posX;
			field_20063_u = posX2;
			field_20066_r = posX2;
		}
		if (d3 < -d4) {
			double posZ2 = posZ;
			field_20061_w = posZ2;
			field_20064_t = posZ2;
		}
		if (d2 < -d4) {
			double posY2 = posY;
			field_20062_v = posY2;
			field_20065_s = posY2;
		}
		field_20063_u += d * 0.25;
		field_20061_w += d3 * 0.25;
		field_20062_v += d2 * 0.25;
	}

	@Override
	protected int decreaseAirSupply(int par1) {
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
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
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
	public void fall(float distance, float modifier) {
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
	public float getBlockPathWeight(BlockPos pos) {
		float weight = worldObj.getLightBrightness(pos) - 0.5f;
		Block block = worldObj.getBlockState(pos).getBlock();
		if (block.isOpaqueCube()) {
			weight += 10.0f;
		}
		return weight;
	}

	public boolean getBoolFlag(int id) {
		return (dataWatcher.getWatchableObjectInt(25) & id) != 0x0;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return stats.creatureType;
	}

	@Override
	public ItemStack getCurrentArmor(int slot) {
		return ItemStackWrapper.MCItem(inventory.getArmor(3 - slot));
	}

	@Override
	public String getDeathSound() {
		return advanced.getSound(3);
	}

	private Dialog getDialog(EntityPlayer player) {
		for (DialogOption option : dialogs.values()) {
			if (option == null) {
				continue;
			}
			if (!option.hasDialog()) {
				continue;
			}
			Dialog dialog = option.getDialog();
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
	public ItemStack getEquipmentInSlot(int slot) {
		if (slot == 0) {
			return ItemStackWrapper.MCItem(inventory.weapons.get(0));
		}
		return ItemStackWrapper.MCItem(inventory.getArmor(4 - slot));
	}

	public Faction getFaction() {
		String[] split = dataWatcher.getWatchableObjectString(13).split(":");
		int faction = 0;
		if ((worldObj == null) || ((split.length <= 1) && worldObj.isRemote)) {
			return new Faction();
		}
		if (split.length > 1) {
			faction = Integer.parseInt(split[0]);
		}
		if (worldObj.isRemote) {
			Faction fac = new Faction();
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
		ItemStack[] inv = new ItemStack[5];
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

	public void givePlayerItem(EntityPlayer player, ItemStack item) {
		if (worldObj.isRemote) {
			return;
		}
		item = item.copy();
		float f = 0.7f;
		double d = (worldObj.rand.nextFloat() * f) + (1.0f - f);
		double d2 = (worldObj.rand.nextFloat() * f) + (1.0f - f);
		double d3 = (worldObj.rand.nextFloat() * f) + (1.0f - f);
		EntityItem entityitem = new EntityItem(worldObj, posX + d, posY + d2, posZ + d3, item);
		entityitem.setPickupDelay(2);
		worldObj.spawnEntityInWorld(entityitem);
		int i = item.stackSize;
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
	public boolean interact(EntityPlayer player) {
		if (worldObj.isRemote) {
			return false;
		}
		ItemStack currentItem = player.inventory.getCurrentItem();
		if (currentItem != null) {
			Item item = currentItem.getItem();
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
		Dialog dialog = getDialog(player);
		PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
		QuestData data = playerdata.getQuestCompletion(player, this);
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

	public boolean isInRange(double posX, double posY, double posZ, double range) {
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

	public boolean isInRange(Entity entity, double range) {
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
	public boolean isInvisibleToPlayer(EntityPlayer player) {
		return (display.getVisible() == 1)
				&& ((player.getHeldItem() == null) || (player.getHeldItem().getItem() != CustomItems.wand));
	}

	public boolean isKilled() {
		return getBoolFlag(8) || isDead;
	}

	@Override
	public boolean isOnSameTeam(EntityLivingBase entity) {
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
	public boolean isPotionApplicable(PotionEffect effect) {
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
		double xx = posX - getStartXPos();
		double zz = posZ - getStartZPos();
		return (xx >= -0.2) && (xx <= 0.2) && (zz >= -0.2) && (zz <= 0.2);
	}

	public boolean isWalking() {
		return (ai.getMovingType() != 0) || isAttacking() || isFollower() || getBoolFlag(1);
	}

	@Override
	public void knockBack(Entity par1Entity, float par2, double par3, double par5) {
		if (stats.resistances.knockback >= 2.0f) {
			return;
		}
		isAirBorne = true;
		float f1 = MathHelper.sqrt_double((par3 * par3) + (par5 * par5));
		float f2 = 0.5f * (2.0f - stats.resistances.knockback);
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
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		double d0 = posX;
		double d2 = posY;
		double d3 = posZ;
		super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		if ((advanced.role == 6) && !isRemote()) {
			((RoleCompanion) roleInterface).addMovementStat(posX - d0, posY - d2, posZ - d3);
		}
	}

	public boolean nearPosition(BlockPos pos) {
		BlockPos npcpos = getPosition();
		float x = npcpos.getX() - pos.getX();
		float z = npcpos.getZ() - pos.getZ();
		float y = npcpos.getY() - pos.getY();
		float height = MathHelper.ceiling_double_int(this.height + 1.0f)
				* MathHelper.ceiling_double_int(this.height + 1.0f);
		return (((x * x) + (z * z)) < 2.5) && ((y * y) < (height + 2.5));
	}

	public void onAttack(EntityLivingBase entity) {
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
		List list = worldObj.getEntitiesWithinAABB((Class) EntityLivingBase.class, axisalignedbb);
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.size(); ++i) {
			Entity entity = (Entity) list.get(i);
			if ((entity != this) && entity.isEntityAlive()) {
				EventHooks.onNPCCollide(this, entity);
			}
		}
	}

	
	@Override
	public void onDeath(DamageSource cause){
		setSprinting(false);
		getNavigator().clearPathEntity();
		extinguish();
		clearActivePotions();
		if (!isRemote()) {
			Entity attackingEntity = cause.getSourceOfDamage();
			if ((attackingEntity instanceof EntityArrow)
					&& (((EntityArrow) attackingEntity).shootingEntity instanceof EntityLivingBase)) {
				attackingEntity = ((EntityArrow) attackingEntity).shootingEntity;
			} else if (attackingEntity instanceof EntityThrowable) {
				attackingEntity = ((EntityThrowable) attackingEntity).getThrower();
			}
			//comment out the below sections and it works
	/*		if (EventHooks.onNPCDied(this, attackingEntity, cause)) {
				return;
			} 
			//crashes here too
			inventory.dropStuff(attackingEntity, cause);*/
			Line line = advanced.getKilledLine();
			if (line != null) {
				saySurrounding(line.formatTarget(
						(EntityLivingBase) ((attackingEntity instanceof EntityLivingBase) ? attackingEntity : null)));
			}
		}
		super.onDeath(cause);
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
					List<EntityMob> list = worldObj.getEntitiesWithinAABB((Class) EntityMob.class,
							getEntityBoundingBox().expand(16.0, 16.0, 16.0));
					for (EntityMob mob : list) {
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
					NBTTagCompound compound = this.writeSpawnData();
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
			float f = getBrightness(1.0f);
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
	protected void playStepSound(BlockPos pos, Block block) {
		String sound = advanced.getSound(4);
		if (sound != null) {
			playSound(sound, 0.15f, 1.0f);
		} else {
			super.playStepSound(pos, block);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
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
	public void readSpawnData(ByteBuf buf) {
		try {
			this.readSpawnData(Server.readNBT(buf));
		} catch (IOException ex) {
		}
	}

	public void readSpawnData(NBTTagCompound compound) {
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
			NBTTagCompound bard = compound.getCompoundTag("Bard");
			jobInterface.readFromNBT(bard);
		}
		if (advanced.job == 9) {
			NBTTagCompound puppet = compound.getCompoundTag("Puppet");
			jobInterface.readFromNBT(puppet);
		}
		if (advanced.role == 6) {
			NBTTagCompound puppet = compound.getCompoundTag("Companion");
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

	public void say(EntityPlayer player, Line line) {
		if ((line == null) || !canSee(player) || (line.text == null)) {
			return;
		}
		if (!line.sound.isEmpty()) {
			Server.sendData((EntityPlayerMP) player, EnumPacketClient.PLAY_SOUND, line.sound, (float) posX,
					(float) posY, (float) posZ);
		}
		Server.sendData((EntityPlayerMP) player, EnumPacketClient.CHATBUBBLE, getEntityId(), line.text, !line.hideText);
	}

	public void saySurrounding(Line line) {
		if ((line == null) || (line.text == null)) {
			return;
		}
		ServerChatEvent event = new ServerChatEvent(getFakePlayer(), line.text,
				new ChatComponentTranslation(line.text.replace("%", "%%"), new Object[0]));
		if (MinecraftForge.EVENT_BUS.post(event) || (event.component == null)) {
			return;
		}
		// why does this line exist? it just kept prepending the npc name...
		// line.text = event.component.getUnformattedText().replace("%%", "%");
		List<EntityPlayer> inRange = worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				getEntityBoundingBox().expand(20.0, 20.0, 20.0));
		for (EntityPlayer player : inRange) {
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
			NpcEvent.TargetEvent event = new NpcEvent.TargetEvent(wrappedNPC, entity);
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
			Line line = advanced.getAttackLine();
			if (line != null) {
				saySurrounding(line.formatTarget(entity));
			}
		}
		super.setAttackTarget(entity);
	}

	public void setBoolFlag(boolean bo, int id) {
		int i = dataWatcher.getWatchableObjectInt(25);
		if (bo && ((i & id) == 0x0)) {
			dataWatcher.updateObject(25, (i | id));
		}
		if (!bo && ((i & id) != 0x0)) {
			dataWatcher.updateObject(25, (i - id));
		}
	}

	public void setCurrentAnimation(int animation) {
		currentAnimation = animation;
		dataWatcher.updateObject(14, animation);
	}

	@Override
	public void setCurrentItemOrArmor(int slot, ItemStack item) {
		if (slot == 0) {
			inventory.weapons.put(0, (item == null) ? null : new ItemStackWrapper(item));
		} else {
			inventory.armor.put(4 - slot, (item == null) ? null : new ItemStackWrapper(item));
		}
	}

	public void setDataWatcher(DataWatcher dataWatcher) {
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

	public void setFaction(int integer) {
		if ((integer < 0) || isRemote()) {
			return;
		}
		Faction faction = FactionController.getInstance().getFaction(integer);
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
	public void setHomePosAndDistance(BlockPos pos, int range) {
		super.setHomePosAndDistance(pos, range);
		ai.setStartPos(pos);
	}

	public void setImmuneToFire(boolean immuneToFire) {
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
	public void setPortal(BlockPos pos) {
	}

	private void setResponse() {
		EntityAIRangedAttack entityAIRangedAttack = null;
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

	public void setRoleDataWatcher(String s) {
		dataWatcher.updateObject(16, s);
	}

	public void tpTo(EntityLivingBase owner) {
		if (owner == null) {
			return;
		}
		EnumFacing facing = owner.getHorizontalFacing().getOpposite();
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
		Predicate attackEntitySelector = new NPCAttackSelector(this);
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
	public void writeEntityToNBT(NBTTagCompound compound) {
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
		NBTTagCompound compound = new NBTTagCompound();
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
			NBTTagCompound bard = new NBTTagCompound();
			jobInterface.writeToNBT(bard);
			compound.setTag("Bard", bard);
		}
		if (advanced.job == 9) {
			NBTTagCompound bard = new NBTTagCompound();
			jobInterface.writeToNBT(bard);
			compound.setTag("Puppet", bard);
		}
		if (advanced.role == 6) {
			NBTTagCompound bard = new NBTTagCompound();
			roleInterface.writeToNBT(bard);
			compound.setTag("Companion", bard);
		}
		if (this instanceof EntityCustomNpc) {
			compound.setTag("ModelData", ((EntityCustomNpc) this).modelData.writeToNBT());
		}
		return compound;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entity, float f) {
		ItemStack proj = ItemStackWrapper.MCItem(inventory.getProjectile());
		if (proj == null) {
			updateAI = true;
			return;
		}
		NpcEvent.RangedLaunchedEvent event = new NpcEvent.RangedLaunchedEvent(wrappedNPC, entity,
				stats.ranged.getStrength());
		if (EventHooks.onNPCRangedLaunched(this, event)) {
			return;
		}
		for (int i = 0; i < stats.ranged.getShotCount(); ++i) {
			EntityProjectile projectile = this.shoot(entity, stats.ranged.getAccuracy(), proj, f == 1.0f);
			projectile.damage = event.damage;
			projectile.callback = new IProjectileCallback() {
				@Override
				public boolean onImpact(EntityProjectile projectile, BlockPos pos, Entity entity) {
					projectile.playSound(stats.ranged.getSound((entity != null) ? 1 : 2), 1.0f,
							1.2f / ((EntityNPCInterface.this.getRNG().nextFloat() * 0.2f) + 0.9f));
					return false;
				}
			};
		}
		playSound(stats.ranged.getSound(0), 2.0f, 1.0f);
	}

	public EntityProjectile shoot(double x, double y, double z, int accuracy, ItemStack proj, boolean indirect) {
		EntityProjectile projectile = new EntityProjectile(worldObj, this, proj.copy(), true);
		double varX = x - posX;
		double varY = y - (posY + getEyeHeight());
		double varZ = z - posZ;
		float varF = projectile.hasGravity() ? MathHelper.sqrt_double((varX * varX) + (varZ * varZ)) : 0.0f;
		float angle = projectile.getAngleForXYZ(varX, varY, varZ, varF, indirect);
		float acc = 20.0f - MathHelper.floor_float(accuracy / 5.0f);
		projectile.setThrowableHeading(varX, varY, varZ, angle, acc);
		worldObj.spawnEntityInWorld(projectile);
		return projectile;
	}

	public EntityProjectile shoot(EntityLivingBase entity, int accuracy, ItemStack proj, boolean indirect) {
		return this.shoot(entity.posX, entity.getEntityBoundingBox().minY + (entity.height / 2.0f), entity.posZ,
				accuracy, proj, indirect);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		try {
			Server.writeNBT(buffer, this.writeSpawnData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
