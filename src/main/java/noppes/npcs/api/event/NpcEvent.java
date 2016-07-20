//

//

package noppes.npcs.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.IPlayer;

public class NpcEvent extends Event {
	public static class CollideEvent extends NpcEvent {
		public IEntity entity;

		public CollideEvent(ICustomNpc npc, Entity entity) {
			super(npc);
			this.entity = NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class DamagedEvent extends NpcEvent {
		public IEntityLivingBase source;
		public DamageSource mcDamageSource;
		public float damage;
		public boolean clearTarget;

		public DamagedEvent(ICustomNpc npc, EntityLivingBase source, float damage, DamageSource mcDamageSource) {
			super(npc);
			clearTarget = false;
			this.source = (IEntityLivingBase) NpcAPI.Instance().getIEntity(source);
			this.damage = damage;
			this.mcDamageSource = mcDamageSource;
		}
	}

	public static class DiedEvent extends NpcEvent {
		public DamageSource mcDamageSource;
		public String type;
		public IEntity source;

		public DiedEvent(ICustomNpc npc, DamageSource damagesource, Entity entity) {
			super(npc);
			mcDamageSource = damagesource;
			type = damagesource.damageType;
			source = NpcAPI.Instance().getIEntity(entity);
		}
	}

	public static class InitEvent extends NpcEvent {
		public InitEvent(ICustomNpc npc) {
			super(npc);
		}
	}

	@Cancelable
	public static class InteractEvent extends NpcEvent {
		public IPlayer player;

		public InteractEvent(ICustomNpc npc, EntityPlayer player) {
			super(npc);
			this.player = (IPlayer) NpcAPI.Instance().getIEntity(player);
		}
	}

	public static class KilledEntityEvent extends NpcEvent {
		public IEntityLivingBase entity;

		public KilledEntityEvent(ICustomNpc npc, EntityLivingBase entity) {
			super(npc);
			this.entity = (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class MeleeAttackEvent extends NpcEvent {
		public IEntityLivingBase target;
		public float damage;

		public MeleeAttackEvent(ICustomNpc npc, EntityLivingBase target, float damage) {
			super(npc);
			this.target = (IEntityLivingBase) NpcAPI.Instance().getIEntity(target);
			this.damage = damage;
		}
	}

	@Cancelable
	public static class RangedLaunchedEvent extends NpcEvent {
		public IEntityLivingBase target;
		public float damage;

		public RangedLaunchedEvent(ICustomNpc npc, EntityLivingBase target, float damage) {
			super(npc);
			this.target = (IEntityLivingBase) NpcAPI.Instance().getIEntity(target);
			this.damage = damage;
		}
	}

	@Cancelable
	public static class TargetEvent extends NpcEvent {
		public IEntityLivingBase entity;

		public TargetEvent(ICustomNpc npc, EntityLivingBase entity) {
			super(npc);
			this.entity = (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class TargetLostEvent extends NpcEvent {
		public IEntityLivingBase entity;

		public TargetLostEvent(ICustomNpc npc, EntityLivingBase entity) {
			super(npc);
			this.entity = (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity);
		}
	}

	public static class TimerEvent extends NpcEvent {
		public int id;

		public TimerEvent(ICustomNpc npc, int id) {
			super(npc);
			this.id = id;
		}
	}

	public static class UpdateEvent extends NpcEvent {
		public UpdateEvent(ICustomNpc npc) {
			super(npc);
		}
	}

	public ICustomNpc npc;

	public NpcEvent(ICustomNpc npc) {
		this.npc = npc;
	}
}
