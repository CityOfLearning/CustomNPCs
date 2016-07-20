//

//

package noppes.npcs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import noppes.npcs.api.event.BlockEvent;
import noppes.npcs.api.event.DialogEvent;
import noppes.npcs.api.event.HandlerEvent;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.event.QuestEvent;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogOption;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.controllers.script.IScriptBlockHandler;
import noppes.npcs.entity.EntityNPCInterface;

public class EventHooks {
	public static void onGlobalFactionsLoaded(final IFactionHandler handler) {
		final HandlerEvent.FactionsLoadedEvent event = new HandlerEvent.FactionsLoadedEvent(handler);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onGlobalRecipesLoaded(final IRecipeHandler handler) {
		final HandlerEvent.RecipesLoadedEvent event = new HandlerEvent.RecipesLoadedEvent(handler);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCAttacksMelee(final EntityNPCInterface npc, final NpcEvent.MeleeAttackEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.ATTACK_MELEE, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCCollide(final EntityNPCInterface npc, final Entity entity) {
		if (npc.script.isClient()) {
			return;
		}
		final NpcEvent.CollideEvent event = new NpcEvent.CollideEvent(npc.wrappedNPC, entity);
		npc.script.runScript(EnumScriptType.COLLIDE, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDamaged(final EntityNPCInterface npc, final NpcEvent.DamagedEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		event.setCanceled(npc.isKilled());
		npc.script.runScript(EnumScriptType.DAMAGED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDialog(final EntityNPCInterface npc, final EntityPlayer player, final Dialog dialog) {
		if (npc.script.isClient()) {
			return false;
		}
		final DialogEvent.OpenEvent event = new DialogEvent.OpenEvent(npc.wrappedNPC, player, dialog);
		npc.script.runScript(EnumScriptType.DIALOG, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDialogOption(final EntityNPCInterface npc, final EntityPlayerMP player,
			final Dialog dialog, final DialogOption option) {
		if (npc.script.isClient()) {
			return false;
		}
		final DialogEvent.OptionEvent event = new DialogEvent.OptionEvent(npc.wrappedNPC, player, dialog, option);
		npc.script.runScript(EnumScriptType.DIALOG_OPTION, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDied(final EntityNPCInterface npc, final Entity entity,
			final DamageSource damagesource) {
		if (npc.script.isClient()) {
			return false;
		}
		final NpcEvent.DiedEvent event = new NpcEvent.DiedEvent(npc.wrappedNPC, damagesource, entity);
		npc.script.runScript(EnumScriptType.DIED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCInit(final EntityNPCInterface npc) {
		if (npc.script.isClient()) {
			return;
		}
		final NpcEvent.InitEvent event = new NpcEvent.InitEvent(npc.wrappedNPC);
		npc.script.runScript(EnumScriptType.INIT, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCInteract(final EntityNPCInterface npc, final EntityPlayer player) {
		if (npc.script.isClient()) {
			return false;
		}
		final NpcEvent.InteractEvent event = new NpcEvent.InteractEvent(npc.wrappedNPC, player);
		event.setCanceled(npc.isAttacking() || npc.isKilled() || npc.faction.isAggressiveToPlayer(player));
		npc.script.runScript(EnumScriptType.INTERACT, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCKills(final EntityNPCInterface npc, final EntityLivingBase entityLiving) {
		if (npc.script.isClient()) {
			return;
		}
		final NpcEvent.KilledEntityEvent event = new NpcEvent.KilledEntityEvent(npc.wrappedNPC, entityLiving);
		npc.script.runScript(EnumScriptType.KILL, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCRangedLaunched(final EntityNPCInterface npc, final NpcEvent.RangedLaunchedEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.RANGED_LAUNCHED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCRole(final EntityNPCInterface npc, final RoleEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.ROLE, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCTarget(final EntityNPCInterface npc, final NpcEvent.TargetEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.TARGET, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCTargetLost(final EntityNPCInterface npc, final EntityLivingBase prevtarget) {
		if (npc.script.isClient()) {
			return false;
		}
		final NpcEvent.TargetLostEvent event = new NpcEvent.TargetLostEvent(npc.wrappedNPC, prevtarget);
		npc.script.runScript(EnumScriptType.TARGET_LOST, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCTick(final EntityNPCInterface npc) {
		if (npc.script.isClient()) {
			return;
		}
		final NpcEvent.UpdateEvent event = new NpcEvent.UpdateEvent(npc.wrappedNPC);
		npc.script.runScript(EnumScriptType.TICK, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCTimer(final EntityNPCInterface npc, final int id) {
		final NpcEvent.TimerEvent event = new NpcEvent.TimerEvent(npc.wrappedNPC, id);
		npc.script.runScript(EnumScriptType.TIMER, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onQuestFinished(final EntityPlayer player, final Quest quest) {
		if (player.worldObj.isRemote) {
			return;
		}
		WrapperNpcAPI.EVENT_BUS.post(new QuestEvent.QuestCompletedEvent(player, quest));
	}

	public static boolean onQuestStarted(final EntityPlayer player, final Quest quest) {
		return !player.worldObj.isRemote && WrapperNpcAPI.EVENT_BUS.post(new QuestEvent.QuestStartEvent(player, quest));
	}

	public static void onQuestTurnedIn(final EntityPlayerMP player, final Quest quest) {
		if (player.worldObj.isRemote) {
			return;
		}
		WrapperNpcAPI.EVENT_BUS.post(new QuestEvent.QuestTurnedInEvent(player, quest));
	}

	public static void onScriptBlockBreak(final IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(handler.getBlock());
		handler.runScript(EnumScriptType.BROKEN, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockClicked(final IScriptBlockHandler handler, final EntityPlayer player) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.ClickedEvent event = new BlockEvent.ClickedEvent(handler.getBlock(), player);
		handler.runScript(EnumScriptType.CLICKED, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockCollide(final IScriptBlockHandler handler, final Entity entityIn) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.CollidedEvent event = new BlockEvent.CollidedEvent(handler.getBlock(), entityIn);
		handler.runScript(EnumScriptType.COLLIDE, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onScriptBlockDoorToggle(final IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return false;
		}
		final BlockEvent.DoorToggleEvent event = new BlockEvent.DoorToggleEvent(handler.getBlock());
		handler.runScript(EnumScriptType.DOOR_TOGGLE, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onScriptBlockExploded(final IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return false;
		}
		final BlockEvent.ExplodedEvent event = new BlockEvent.ExplodedEvent(handler.getBlock());
		handler.runScript(EnumScriptType.EXPLODED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static float onScriptBlockFallenUpon(final IScriptBlockHandler handler, final Entity entity,
			final float distance) {
		if (handler.isClient()) {
			return distance;
		}
		final BlockEvent.EntityFallenUponEvent event = new BlockEvent.EntityFallenUponEvent(handler.getBlock(), entity,
				distance);
		handler.runScript(EnumScriptType.FALLEN_UPON, event);
		if (WrapperNpcAPI.EVENT_BUS.post(event)) {
			return 0.0f;
		}
		return event.distanceFallen;
	}

	public static boolean onScriptBlockHarvest(final IScriptBlockHandler handler, final EntityPlayer player) {
		if (handler.isClient()) {
			return false;
		}
		final BlockEvent.HarvestedEvent event = new BlockEvent.HarvestedEvent(handler.getBlock(), player);
		handler.runScript(EnumScriptType.HARVESTED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockInit(final IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.InitEvent event = new BlockEvent.InitEvent(handler.getBlock());
		handler.runScript(EnumScriptType.INIT, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onScriptBlockInteract(final IScriptBlockHandler handler, final EntityPlayer player,
			final int side, final float hitX, final float hitY, final float hitZ) {
		if (handler.isClient()) {
			return false;
		}
		final BlockEvent.InteractEvent event = new BlockEvent.InteractEvent(handler.getBlock(), player, side, hitX,
				hitY, hitZ);
		handler.runScript(EnumScriptType.INTERACT, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockNeighborChanged(final IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.NeighborChangedEvent event = new BlockEvent.NeighborChangedEvent(handler.getBlock());
		handler.runScript(EnumScriptType.NEIGHBOR_CHANGED, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockRainFill(final IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.RainFillEvent event = new BlockEvent.RainFillEvent(handler.getBlock());
		handler.runScript(EnumScriptType.RAIN_FILLED, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockRedstonePower(final IScriptBlockHandler handler, final int prevPower,
			final int power) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.RedstoneEvent event = new BlockEvent.RedstoneEvent(handler.getBlock(), prevPower, power);
		handler.runScript(EnumScriptType.REDSTONE, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockTimer(final IScriptBlockHandler handler, final int id) {
		final BlockEvent.TimerEvent event = new BlockEvent.TimerEvent(handler.getBlock(), id);
		handler.runScript(EnumScriptType.TIMER, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockUpdate(final IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		final BlockEvent.UpdateEvent event = new BlockEvent.UpdateEvent(handler.getBlock());
		handler.runScript(EnumScriptType.TICK, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}
}
