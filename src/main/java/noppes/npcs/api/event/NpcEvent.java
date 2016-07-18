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
		public final IEntity entity;

		public CollideEvent(final ICustomNpc npc, final Entity entity) {
			super(npc);
			this.entity = NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class DamagedEvent extends NpcEvent {
		public final IEntityLivingBase source;
		public final DamageSource mcDamageSource;
		public float damage;
		public boolean clearTarget;

		public DamagedEvent(final ICustomNpc npc, final EntityLivingBase source, final float damage,
				final DamageSource mcDamageSource) {
			super(npc);
			clearTarget = false;
			this.source = (IEntityLivingBase) NpcAPI.Instance().getIEntity(source);
			this.damage = damage;
			this.mcDamageSource = mcDamageSource;
		}
	}

	public static class DiedEvent extends NpcEvent {
		public final DamageSource mcDamageSource;
		public final String type;
		public final IEntity source;

		public DiedEvent(final ICustomNpc npc, final DamageSource damagesource, final Entity entity) {
			super(npc);
			mcDamageSource = damagesource;
			type = damagesource.damageType;
			source = NpcAPI.Instance().getIEntity(entity);
		}
	}

	public static class InitEvent extends NpcEvent {
		public InitEvent(final ICustomNpc npc) {
			super(npc);
		}
	}

	@Cancelable
	public static class InteractEvent extends NpcEvent {
		public final IPlayer player;

		public InteractEvent(final ICustomNpc npc, final EntityPlayer player) {
			super(npc);
			this.player = (IPlayer) NpcAPI.Instance().getIEntity(player);
		}
	}

	public static class KilledEntityEvent extends NpcEvent {
		public final IEntityLivingBase entity;

		public KilledEntityEvent(final ICustomNpc npc, final EntityLivingBase entity) {
			super(npc);
			this.entity = (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class MeleeAttackEvent extends NpcEvent {
		public final IEntityLivingBase target;
		public float damage;

		public MeleeAttackEvent(final ICustomNpc npc, final EntityLivingBase target, final float damage) {
			super(npc);
			this.target = (IEntityLivingBase) NpcAPI.Instance().getIEntity(target);
			this.damage = damage;
		}
	}

	@Cancelable
	public static class RangedLaunchedEvent extends NpcEvent {
		public final IEntityLivingBase target;
		public float damage;

		public RangedLaunchedEvent(final ICustomNpc npc, final EntityLivingBase target, final float damage) {
			super(npc);
			this.target = (IEntityLivingBase) NpcAPI.Instance().getIEntity(target);
			this.damage = damage;
		}
	}

	@Cancelable
	public static class TargetEvent extends NpcEvent {
		public IEntityLivingBase entity;

		public TargetEvent(final ICustomNpc npc, final EntityLivingBase entity) {
			super(npc);
			this.entity = (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class TargetLostEvent extends NpcEvent {
		public final IEntityLivingBase entity;

		public TargetLostEvent(final ICustomNpc npc, final EntityLivingBase entity) {
			super(npc);
			this.entity = (IEntityLivingBase) NpcAPI.Instance().getIEntity(entity);
		}
	}

	public static class TimerEvent extends NpcEvent {
		public final int id;

		public TimerEvent(final ICustomNpc npc, final int id) {
			super(npc);
			this.id = id;
		}
	}

	public static class UpdateEvent extends NpcEvent {
		public UpdateEvent(final ICustomNpc npc) {
			super(npc);
		}
	}

	public final ICustomNpc npc;

	public NpcEvent(final ICustomNpc npc) {
		this.npc = npc;
	}
}
