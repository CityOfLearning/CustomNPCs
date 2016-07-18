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

	public TextBlockClient(final ICommandSender sender, final String text, final int lineWidth, final int color,
			final Object... obs) {
		this(text, lineWidth, false, obs);
		this.color = color;
		this.sender = sender;
	}

	public TextBlockClient(String text, final int lineWidth, final boolean mcFont, final Object... obs) {
		color = 14737632;
		style = new ChatStyle();
		text = NoppesStringUtils.formatText(text, obs);
		String line = "";
		text = text.replace("\n", " \n ");
		text = text.replace("\r", " \r ");
		final String[] words = text.split(" ");
		final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		for (final String word : words) {
			Label_0235: {
				if (!word.isEmpty()) {
					if (word.length() == 1) {
						final char c = word.charAt(0);
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
					if ((mcFont ? font.getStringWidth(newLine) : ClientProxy.Font.width(newLine)) > lineWidth) {
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

	public TextBlockClient(final String name, final String text, final int lineWidth, final int color,
			final Object... obs) {
		this(text, lineWidth, false, obs);
		this.color = color;
		this.name = name;
	}

	private void addLine(final String text) {
		final ChatComponentText line = new ChatComponentText(text);
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
