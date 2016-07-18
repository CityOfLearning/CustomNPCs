//

//

package noppes.npcs.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;

public class VersionChecker extends Thread {
	private int revision;

	public VersionChecker() {
		revision = 12;
	}

	private boolean hasUpdate() { 
		try {
			final URL url = new URL("https://dl.dropboxusercontent.com/u/3096920/update/minecraft/1.8/CustomNPCs.txt");
			final URLConnection yc = url.openConnection();
			final BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			final String inputLine = in.readLine();
			if (inputLine == null) {
				return false;
			}
			final int newVersion = Integer.parseInt(inputLine);
			return revision < newVersion;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void run() {
		final String name = "§2CustomNpcs§f";
		final String link = "§9§nClick here";
		String text = name + " installed. For more info " + link;
		if (hasUpdate()) {
			text = name + '§' + "4 update available " + link;
		}
		try {
			Minecraft.getMinecraft();
		} catch (NoSuchMethodError e2) {
			return;
		}
		EntityPlayer player;
		while ((player = Minecraft.getMinecraft().thePlayer) == null) {
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		final ChatComponentTranslation message = new ChatComponentTranslation(text, new Object[0]);
		message.getChatStyle().setChatClickEvent(
				new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.kodevelopment.nl/minecraft/customnpcs/"));
		player.addChatMessage(message);
	}
}
