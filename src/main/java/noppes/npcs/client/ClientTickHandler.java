//

//

package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.player.GuiQuestLog;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.constants.EnumPlayerPacket;

public class ClientTickHandler {
	private World prevWorld;
	private boolean otherContainer;

	public ClientTickHandler() {
		otherContainer = false;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			return;
		}
		final Minecraft mc = Minecraft.getMinecraft();
		if ((mc.thePlayer != null) && (mc.thePlayer.openContainer instanceof ContainerPlayer)) {
			if (otherContainer) {
				NoppesUtilPlayer.sendData(EnumPlayerPacket.CheckQuestCompletion, new Object[0]);
				otherContainer = false;
			}
		} else {
			otherContainer = true;
		}
		++CustomNpcs.ticks;
		++RenderNPCInterface.LastTextureTick;
		if (prevWorld != mc.theWorld) {
			prevWorld = mc.theWorld;
			MusicController.Instance.stopMusic();
		}
	}

	@SubscribeEvent
	public void onKey(final InputEvent.KeyInputEvent event) {
		if (CustomNpcs.SceneButtonsEnabled) {
			if (ClientProxy.Scene1.isPressed()) {
				Client.sendData(EnumPacketServer.SceneStart, 1);
			}
			if (ClientProxy.Scene2.isPressed()) {
				Client.sendData(EnumPacketServer.SceneStart, 2);
			}
			if (ClientProxy.Scene3.isPressed()) {
				Client.sendData(EnumPacketServer.SceneStart, 3);
			}
			if (ClientProxy.SceneReset.isPressed()) {
				Client.sendData(EnumPacketServer.SceneReset, new Object[0]);
			}
		}
		if (ClientProxy.QuestLog.isPressed()) {
			final Minecraft mc = Minecraft.getMinecraft();
			if (mc.currentScreen == null) {
				NoppesUtil.openGUI(mc.thePlayer, new GuiQuestLog(mc.thePlayer));
			} else if (mc.currentScreen instanceof GuiQuestLog) {
				mc.setIngameFocus();
			}
		}
	}
}
