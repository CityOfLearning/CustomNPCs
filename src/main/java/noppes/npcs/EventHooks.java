
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
	public static void onGlobalFactionsLoaded(IFactionHandler handler) {
		HandlerEvent.FactionsLoadedEvent event = new HandlerEvent.FactionsLoadedEvent(handler);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onGlobalRecipesLoaded(IRecipeHandler handler) {
		HandlerEvent.RecipesLoadedEvent event = new HandlerEvent.RecipesLoadedEvent(handler);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCAttacksMelee(EntityNPCInterface npc, NpcEvent.MeleeAttackEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.ATTACK_MELEE, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCCollide(EntityNPCInterface npc, Entity entity) {
		if (npc.script.isClient()) {
			return;
		}
		NpcEvent.CollideEvent event = new NpcEvent.CollideEvent(npc.wrappedNPC, entity);
		npc.script.runScript(EnumScriptType.COLLIDE, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDamaged(EntityNPCInterface npc, NpcEvent.DamagedEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		event.setCanceled(npc.isKilled());
		npc.script.runScript(EnumScriptType.DAMAGED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDialog(EntityNPCInterface npc, EntityPlayer player, Dialog dialog) {
		if (npc.script.isClient()) {
			return false;
		}
		DialogEvent.OpenEvent event = new DialogEvent.OpenEvent(npc.wrappedNPC, player, dialog);
		npc.script.runScript(EnumScriptType.DIALOG, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDialogOption(EntityNPCInterface npc, EntityPlayerMP player, Dialog dialog,
			DialogOption option) {
		if (npc.script.isClient()) {
			return false;
		}
		DialogEvent.OptionEvent event = new DialogEvent.OptionEvent(npc.wrappedNPC, player, dialog, option);
		npc.script.runScript(EnumScriptType.DIALOG_OPTION, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCDied(EntityNPCInterface npc, DamageSource damagesource, Entity entity) {
		if (npc.script.isClient()) {
			return false;
		}
		NpcEvent.DiedEvent event = new NpcEvent.DiedEvent(npc.wrappedNPC, damagesource, entity);
		npc.script.runScript(EnumScriptType.DIED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCInit(EntityNPCInterface npc) {
		if (npc.script.isClient()) {
			return;
		}
		NpcEvent.InitEvent event = new NpcEvent.InitEvent(npc.wrappedNPC);
		npc.script.runScript(EnumScriptType.INIT, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCInteract(EntityNPCInterface npc, EntityPlayer player) {
		if (npc.script.isClient()) {
			return false;
		}
		NpcEvent.InteractEvent event = new NpcEvent.InteractEvent(npc.wrappedNPC, player);
		event.setCanceled(npc.isAttacking() || npc.isKilled() || npc.faction.isAggressiveToPlayer(player));
		npc.script.runScript(EnumScriptType.INTERACT, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCKills(EntityNPCInterface npc, EntityLivingBase entityLiving) {
		if (npc.script.isClient()) {
			return;
		}
		NpcEvent.KilledEntityEvent event = new NpcEvent.KilledEntityEvent(npc.wrappedNPC, entityLiving);
		npc.script.runScript(EnumScriptType.KILL, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCRangedLaunched(EntityNPCInterface npc, NpcEvent.RangedLaunchedEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.RANGED_LAUNCHED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCRole(EntityNPCInterface npc, RoleEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.ROLE, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCTarget(EntityNPCInterface npc, NpcEvent.TargetEvent event) {
		if (npc.script.isClient()) {
			return false;
		}
		npc.script.runScript(EnumScriptType.TARGET, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onNPCTargetLost(EntityNPCInterface npc, EntityLivingBase prevtarget) {
		if (npc.script.isClient()) {
			return false;
		}
		NpcEvent.TargetLostEvent event = new NpcEvent.TargetLostEvent(npc.wrappedNPC, prevtarget);
		npc.script.runScript(EnumScriptType.TARGET_LOST, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCTick(EntityNPCInterface npc) {
		if (npc.script.isClient()) {
			return;
		}
		NpcEvent.UpdateEvent event = new NpcEvent.UpdateEvent(npc.wrappedNPC);
		npc.script.runScript(EnumScriptType.TICK, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onNPCTimer(EntityNPCInterface npc, int id) {
		NpcEvent.TimerEvent event = new NpcEvent.TimerEvent(npc.wrappedNPC, id);
		npc.script.runScript(EnumScriptType.TIMER, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onQuestFinished(EntityPlayer player, Quest quest) {
		if (player.worldObj.isRemote) {
			return;
		}
		WrapperNpcAPI.EVENT_BUS.post(new QuestEvent.QuestCompletedEvent(player, quest));
	}

	public static boolean onQuestStarted(EntityPlayer player, Quest quest) {
		return !player.worldObj.isRemote && WrapperNpcAPI.EVENT_BUS.post(new QuestEvent.QuestStartEvent(player, quest));
	}

	public static void onQuestTurnedIn(EntityPlayerMP player, Quest quest) {
		if (player.worldObj.isRemote) {
			return;
		}
		WrapperNpcAPI.EVENT_BUS.post(new QuestEvent.QuestTurnedInEvent(player, quest));
	}

	public static void onScriptBlockBreak(IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(handler.getBlock());
		handler.runScript(EnumScriptType.BROKEN, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockClicked(IScriptBlockHandler handler, EntityPlayer player) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.ClickedEvent event = new BlockEvent.ClickedEvent(handler.getBlock(), player);
		handler.runScript(EnumScriptType.CLICKED, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockCollide(IScriptBlockHandler handler, Entity entityIn) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.CollidedEvent event = new BlockEvent.CollidedEvent(handler.getBlock(), entityIn);
		handler.runScript(EnumScriptType.COLLIDE, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onScriptBlockDoorToggle(IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return false;
		}
		BlockEvent.DoorToggleEvent event = new BlockEvent.DoorToggleEvent(handler.getBlock());
		handler.runScript(EnumScriptType.DOOR_TOGGLE, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onScriptBlockExploded(IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return false;
		}
		BlockEvent.ExplodedEvent event = new BlockEvent.ExplodedEvent(handler.getBlock());
		handler.runScript(EnumScriptType.EXPLODED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static float onScriptBlockFallenUpon(IScriptBlockHandler handler, Entity entity, float distance) {
		if (handler.isClient()) {
			return distance;
		}
		BlockEvent.EntityFallenUponEvent event = new BlockEvent.EntityFallenUponEvent(handler.getBlock(), entity,
				distance);
		handler.runScript(EnumScriptType.FALLEN_UPON, event);
		if (WrapperNpcAPI.EVENT_BUS.post(event)) {
			return 0.0f;
		}
		return event.distanceFallen;
	}

	public static boolean onScriptBlockHarvest(IScriptBlockHandler handler, EntityPlayer player) {
		if (handler.isClient()) {
			return false;
		}
		BlockEvent.HarvestedEvent event = new BlockEvent.HarvestedEvent(handler.getBlock(), player);
		handler.runScript(EnumScriptType.HARVESTED, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockInit(IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.InitEvent event = new BlockEvent.InitEvent(handler.getBlock());
		handler.runScript(EnumScriptType.INIT, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static boolean onScriptBlockInteract(IScriptBlockHandler handler, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ) {
		if (handler.isClient()) {
			return false;
		}
		BlockEvent.InteractEvent event = new BlockEvent.InteractEvent(handler.getBlock(), player, side, hitX, hitY,
				hitZ);
		handler.runScript(EnumScriptType.INTERACT, event);
		return WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockNeighborChanged(IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.NeighborChangedEvent event = new BlockEvent.NeighborChangedEvent(handler.getBlock());
		handler.runScript(EnumScriptType.NEIGHBOR_CHANGED, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockRainFill(IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.RainFillEvent event = new BlockEvent.RainFillEvent(handler.getBlock());
		handler.runScript(EnumScriptType.RAIN_FILLED, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockRedstonePower(IScriptBlockHandler handler, int prevPower, int power) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.RedstoneEvent event = new BlockEvent.RedstoneEvent(handler.getBlock(), prevPower, power);
		handler.runScript(EnumScriptType.REDSTONE, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockTimer(IScriptBlockHandler handler, int id) {
		BlockEvent.TimerEvent event = new BlockEvent.TimerEvent(handler.getBlock(), id);
		handler.runScript(EnumScriptType.TIMER, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}

	public static void onScriptBlockUpdate(IScriptBlockHandler handler) {
		if (handler.isClient()) {
			return;
		}
		BlockEvent.UpdateEvent event = new BlockEvent.UpdateEvent(handler.getBlock());
		handler.runScript(EnumScriptType.TICK, event);
		WrapperNpcAPI.EVENT_BUS.post(event);
	}
}
