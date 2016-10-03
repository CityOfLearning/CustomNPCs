package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.TextBlock;
import noppes.npcs.client.ClientProxy;

public class TextBlockClient extends TextBlock {

   private ChatStyle style;
   public int color;
   private String name;
   private ICommandSender sender;


   public TextBlockClient(String name, String text, int lineWidth, int color, Object ... obs) {
      this(text, lineWidth, false, obs);
      this.color = color;
      this.name = name;
   }

   public TextBlockClient(ICommandSender sender, String text, int lineWidth, int color, Object ... obs) {
      this(text, lineWidth, false, obs);
      this.color = color;
      this.sender = sender;
   }

   public String getName() {
      return this.sender != null?this.sender.getName():this.name;
   }

   public TextBlockClient(String text, int lineWidth, boolean mcFont, Object ... obs) {
      this.color = 14737632;
      this.style = new ChatStyle();
      text = NoppesStringUtils.formatText(text, obs);
      String line = "";
      text = text.replace("\n", " \n ");
      text = text.replace("\r", " \r ");
      String[] words = text.split(" ");
      FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
      String[] var8 = words;
      int var9 = words.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String word = var8[var10];
         if(!word.isEmpty()) {
            if(word.length() == 1) {
               char newLine = word.charAt(0);
               if(newLine == 13 || newLine == 10) {
                  this.addLine(line);
                  line = "";
                  continue;
               }
            }

            String var13;
            if(line.isEmpty()) {
               var13 = word;
            } else {
               var13 = line + " " + word;
            }

            if((mcFont?font.getStringWidth(var13):ClientProxy.Font.width(var13)) > lineWidth) {
               this.addLine(line);
               line = word.trim();
            } else {
               line = var13;
            }
         }
      }

      if(!line.isEmpty()) {
         this.addLine(line);
      }

   }

   private void addLine(String text) {
      ChatComponentText line = new ChatComponentText(text);
      line.setChatStyle(this.style);
      this.lines.add(line);
   }
}
