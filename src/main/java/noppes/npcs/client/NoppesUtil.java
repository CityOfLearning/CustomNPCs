
package noppes.npcs.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.lwjgl.Sys;

import com.rabbit.gui.RabbitGui;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.Server;
import noppes.npcs.client.gui.player.GuiDialogInteract;
import noppes.npcs.client.gui.player.GuiQuestCompletion;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.dialog.Dialog;
import noppes.npcs.controllers.dialog.DialogController;
import noppes.npcs.controllers.quest.Quest;
import noppes.npcs.entity.EntityNPCInterface;

public class NoppesUtil {
	private static EntityNPCInterface lastNpc;
	private static HashMap<String, Integer> data;

	static {
		NoppesUtil.data = new HashMap<String, Integer>();
	}

	public static void addScrollData(ByteBuf buffer) {
		try {
			for (int size = buffer.readInt(), i = 0; i < size; ++i) {
				int id = buffer.readInt();
				String name = Server.readString(buffer);
				NoppesUtil.data.put(name, id);
			}
		} catch (Exception ex) {
		}
	}

	public static void clickSound() {
		Minecraft.getMinecraft().getSoundHandler()
				.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
	}

	public static EntityNPCInterface getLastNpc() {
		return NoppesUtil.lastNpc;
	}

	public static void guiQuestCompletion(EntityPlayer player, NBTTagCompound read) {
		Quest quest = new Quest();
		quest.readNBT(read);
		if (!quest.completeText.equals("")) {
			openGUI(player, new GuiQuestCompletion(quest));
		} else {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, quest.id);
		}
	}

	public static void openDialog(NBTTagCompound compound, EntityNPCInterface npc, EntityPlayer player) {
		if (DialogController.instance == null) {
			DialogController.instance = new DialogController();
		}
		Dialog dialog = new Dialog();
		dialog.readNBT(compound);
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if ((gui == null) || (RabbitGui.proxy.getCurrentStage() == null)
				|| !(RabbitGui.proxy.getCurrentStage().getShow() instanceof GuiDialogInteract)) {
			RabbitGui.proxy.display(new GuiDialogInteract(npc, dialog));
		} else {
			GuiDialogInteract dia = (GuiDialogInteract) RabbitGui.proxy.getCurrentStage().getShow();
			dia.appendDialog(dialog);
		}
	}

	public static void openFolder(File dir) {
		String s = dir.getAbsolutePath();
		if (Util.getOSType() == Util.EnumOS.OSX)
	    {
	      try
	      {
	        Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
	        return;
	      }
	      catch (IOException ioexception1) {}
	    }
	    else if (Util.getOSType() == Util.EnumOS.WINDOWS)
	    {
	      String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });
	      try
	      {
	        Runtime.getRuntime().exec(s1);
	        return;
	      }
	      catch (IOException ioexception) {}
	    }
	    boolean flag = false;
	    try
	    {
	      Class oclass = Class.forName("java.awt.Desktop");
	      Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
	      oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { dir.toURI() });
	    }
	    catch (Throwable throwable)
	    {
	      flag = true;
	    }
	    if (flag) {
	      Sys.openURL("file://" + s);
	    }
	}

	public static void openGUI(EntityPlayer player, Object guiscreen) {
		CustomNpcs.proxy.openGui(player, guiscreen);
	}

	public static void requestOpenGUI(EnumGuiType gui) {
		requestOpenGUI(gui, 0, 0, 0);
	}

	public static void requestOpenGUI(EnumGuiType gui, int i, int j, int k) {
		Client.sendData(EnumPacketServer.Gui, gui.ordinal(), i, j, k);
	}

	public static void setLastNpc(EntityNPCInterface npc) {
		NoppesUtil.lastNpc = npc;
	}

	public static void setScrollData(ByteBuf buffer) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui == null) {
			return;
		}
		try {
			for (int size = buffer.readInt(), i = 0; i < size; ++i) {
				int id = buffer.readInt();
				String name = Server.readString(buffer);
				NoppesUtil.data.put(name, id);
			}
		} catch (Exception ex) {
		}
		if ((gui instanceof GuiNPCInterface) && ((GuiNPCInterface) gui).hasSubGui()) {
			gui = ((GuiNPCInterface) gui).getSubGui();
		}
		if ((gui instanceof GuiContainerNPCInterface) && ((GuiContainerNPCInterface) gui).hasSubGui()) {
			gui = ((GuiContainerNPCInterface) gui).getSubGui();
		}
		if (gui instanceof IScrollData) {
			((IScrollData) gui).setData(new Vector<String>(NoppesUtil.data.keySet()), NoppesUtil.data);
		}
		NoppesUtil.data = new HashMap<String, Integer>();
	}

	public static void setScrollList(ByteBuf buffer) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if ((gui instanceof GuiNPCInterface) && ((GuiNPCInterface) gui).hasSubGui()) {
			gui = ((GuiNPCInterface) gui).getSubGui();
		}
		if ((gui == null) || !(gui instanceof IScrollData)) {
			return;
		}
		Vector<String> data = new Vector<String>();
		try {
			for (int size = buffer.readInt(), i = 0; i < size; ++i) {
				data.add(Server.readString(buffer));
			}
		} catch (Exception ex) {
		}
		((IScrollData) gui).setData(data, null);
	}

	public static void spawnParticle(ByteBuf buffer) throws IOException {
		double posX = buffer.readDouble();
		double posY = buffer.readDouble();
		double posZ = buffer.readDouble();
		float height = buffer.readFloat();
		float width = buffer.readFloat();
		String particle = Server.readString(buffer);
		World worldObj = Minecraft.getMinecraft().theWorld;
		Random rand = worldObj.rand;
		if (particle.equals("heal")) {
			for (int k = 0; k < 6; ++k) {
				worldObj.spawnParticle(EnumParticleTypes.SPELL_INSTANT, posX + ((rand.nextDouble() - 0.5) * width),
						posY + (rand.nextDouble() * height), posZ + ((rand.nextDouble() - 0.5) * width), 0.0, 0.0, 0.0,
						new int[0]);
				worldObj.spawnParticle(EnumParticleTypes.SPELL, posX + ((rand.nextDouble() - 0.5) * width),
						posY + (rand.nextDouble() * height), posZ + ((rand.nextDouble() - 0.5) * width), 0.0, 0.0, 0.0,
						new int[0]);
			}
		}
	}
}
