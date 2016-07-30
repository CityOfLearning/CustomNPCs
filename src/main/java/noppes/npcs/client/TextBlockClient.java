//

//

package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.TextBlock;

public class TextBlockClient extends TextBlock {
	private ChatStyle style;
	public int color;
	private String name;
	private ICommandSender sender;

	public TextBlockClient(ICommandSender sender, String text, int lineWidth, int color, Object... obs) {
		this(text, lineWidth, false, obs);
		this.color = color;
		this.sender = sender;
	}

	public TextBlockClient(String text, int lineWidth, boolean mcFont, Object... obs) {
		color = 14737632;
		style = new ChatStyle();
		text = NoppesStringUtils.formatText(text, obs);
		String line = "";
		text = text.replace("\n", " \n ");
		text = text.replace("\r", " \r ");
		String[] words = text.split(" ");
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		for (String word : words) {
			Label_0235: {
				if (!word.isEmpty()) {
					if (word.length() == 1) {
						char c = word.charAt(0);
						if ((c == '\r') || (c == '\n')) {
							addLine(line);
							line = "";
							break Label_0235;
						}
					}
					String newLine;
					if (line.isEmpty()) {
						newLine = word;
					} else {
						newLine = line + " " + word;
					}
					if ((mcFont ? font.getStringWidth(newLine)
							: Minecraft.getMinecraft().fontRendererObj.getStringWidth(newLine)) > lineWidth) {
						addLine(line);
						line = word.trim();
					} else {
						line = newLine;
					}
				}
			}
		}
		if (!line.isEmpty()) {
			addLine(line);
		}
	}

	public TextBlockClient(String name, String text, int lineWidth, int color, Object... obs) {
		this(text, lineWidth, false, obs);
		this.color = color;
		this.name = name;
	}

	private void addLine(String text) {
		ChatComponentText line = new ChatComponentText(text);
		line.setChatStyle(style);
		lines.add(line);
	}

	public String getName() {
		if (sender != null) {
			return sender.getName();
		}
		return name;
	}
}
