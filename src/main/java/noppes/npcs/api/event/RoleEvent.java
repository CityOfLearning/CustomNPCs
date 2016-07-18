//

//

package noppes.npcs.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IPlayerMail;

public class RoleEvent extends Event {
	public static class BankUnlockedEvent extends RoleEvent {
		public final int slot;

		public BankUnlockedEvent(final EntityPlayer player, final ICustomNpc npc, final int slot) {
			super(player, npc);
			this.slot = slot;
		}
	}

	public static class BankUpgradedEvent extends RoleEvent {
		public final int slot;

		public BankUpgradedEvent(final EntityPlayer player, final ICustomNpc npc, final int slot) {
			super(player, npc);
			this.slot = slot;
		}
	}

	public static class FollowerFinishedEvent extends RoleEvent {
		public FollowerFinishedEvent(final EntityPlayer player, final ICustomNpc npc) {
			super(player, npc);
		}
	}

	@Cancelable
	public static class FollowerHireEvent extends RoleEvent {
		public int days;

		public FollowerHireEvent(final EntityPlayer player, final ICustomNpc npc, final int days) {
			super(player, npc);
			this.days = days;
		}
	}

	@Cancelable
	public static class MailmanEvent extends RoleEvent {
		public final IPlayerMail mail;

		public MailmanEvent(final EntityPlayer player, final ICustomNpc npc, final IPlayerMail mail) {
			super(player, npc);
			this.mail = mail;
		}
	}

	@Cancelable
	public static class TraderEvent extends RoleEvent {
		public IItemStack sold;
		public IItemStack currency1;
		public IItemStack currency2;

		public TraderEvent(final EntityPlayer player, final ICustomNpc npc, final ItemStack sold,
				final ItemStack currency1, final ItemStack currency2) {
			super(player, npc);
			this.currency1 = ((currency1 == null) ? null : NpcAPI.Instance().getIItemStack(currency1.copy()));
			this.currency2 = ((currency2 == null) ? null : NpcAPI.Instance().getIItemStack(currency2.copy()));
			this.sold = NpcAPI.Instance().getIItemStack(sold.copy());
		}
	}

	@Cancelable
	public static class TransporterUnlockedEvent extends RoleEvent {
		public TransporterUnlockedEvent(final EntityPlayer player, final ICustomNpc npc) {
			super(player, npc);
		}
	}

	@Cancelable
	public static class TransporterUseEvent extends RoleEvent {
		public TransporterUseEvent(final EntityPlayer player, final ICustomNpc npc) {
			super(player, npc);
		}
	}

	public final ICustomNpc npc;

	public final IPlayer player;

	public RoleEvent(final EntityPlayer player, final ICustomNpc npc) {
		this.npc = npc;
		this.player = (IPlayer) NpcAPI.Instance().getIEntity(player);
	}
}
