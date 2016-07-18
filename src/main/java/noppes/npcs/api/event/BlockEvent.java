//

//

package noppes.npcs.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;

public class BlockEvent extends Event {
	public static class BreakEvent extends BlockEvent {
		public BreakEvent(final IBlock block) {
			super(block);
		}
	}

	public static class ClickedEvent extends BlockEvent {
		public final IPlayer player;

		public ClickedEvent(final IBlock block, final EntityPlayer player) {
			super(block);
			this.player = (IPlayer) NpcAPI.Instance().getIEntity(player);
		}
	}

	public static class CollidedEvent extends BlockEvent {
		public final IEntity entity;

		public CollidedEvent(final IBlock block, final Entity entity) {
			super(block);
			this.entity = NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class DoorToggleEvent extends BlockEvent {
		public DoorToggleEvent(final IBlock block) {
			super(block);
		}
	}

	@Cancelable
	public static class EntityFallenUponEvent extends BlockEvent {
		public final IEntity entity;
		public float distanceFallen;

		public EntityFallenUponEvent(final IBlock block, final Entity entity, final float distance) {
			super(block);
			distanceFallen = distance;
			this.entity = NpcAPI.Instance().getIEntity(entity);
		}
	}

	@Cancelable
	public static class ExplodedEvent extends BlockEvent {
		public ExplodedEvent(final IBlock block) {
			super(block);
		}
	}

	public static class HarvestedEvent extends BlockEvent {
		public final IPlayer player;

		public HarvestedEvent(final IBlock block, final EntityPlayer player) {
			super(block);
			this.player = (IPlayer) NpcAPI.Instance().getIEntity(player);
		}
	}

	public static class InitEvent extends BlockEvent {
		public InitEvent(final IBlock block) {
			super(block);
		}
	}

	@Cancelable
	public static class InteractEvent extends BlockEvent {
		public final IPlayer player;
		public final float hitX;
		public final float hitY;
		public final float hitZ;
		public final int side;

		public InteractEvent(final IBlock block, final EntityPlayer player, final int side, final float hitX,
				final float hitY, final float hitZ) {
			super(block);
			this.player = (IPlayer) NpcAPI.Instance().getIEntity(player);
			this.hitX = hitX;
			this.hitY = hitY;
			this.hitZ = hitZ;
			this.side = side;
		}
	}

	public static class NeighborChangedEvent extends BlockEvent {
		public NeighborChangedEvent(final IBlock block) {
			super(block);
		}
	}

	public static class RainFillEvent extends BlockEvent {
		public RainFillEvent(final IBlock block) {
			super(block);
		}
	}

	public static class RedstoneEvent extends BlockEvent {
		public final int prevPower;
		public final int power;

		public RedstoneEvent(final IBlock block, final int prevPower, final int power) {
			super(block);
			this.power = power;
			this.prevPower = prevPower;
		}
	}

	public static class TimerEvent extends BlockEvent {
		public final int id;

		public TimerEvent(final IBlock block, final int id) {
			super(block);
			this.id = id;
		}
	}

	public static class UpdateEvent extends BlockEvent {
		public UpdateEvent(final IBlock block) {
			super(block);
		}
	}

	public IBlock block;

	public BlockEvent(final IBlock block) {
		this.block = block;
	}
}
