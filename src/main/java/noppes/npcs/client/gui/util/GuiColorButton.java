package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import noppes.npcs.client.gui.util.GuiNpcButton;

public class GuiColorButton extends GuiNpcButton {

   public int color;


   public GuiColorButton(int id, int x, int y, int color) {
      super(id, x, y, 50, 20, "");
      this.color = color;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if(this.visible) {
         drawRect(this.xPosition, this.yPosition, this.xPosition + 50, this.yPosition + 20, -16777216 + this.color);
      }
   }
}
