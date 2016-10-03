package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextArea;
import noppes.npcs.client.gui.util.SubGuiInterface;

public class SubGuiNpcTextArea extends SubGuiInterface {

   public String text;
   private GuiNpcTextArea textarea;


   public SubGuiNpcTextArea(String text) {
      this.text = text;
      this.setBackground("bgfilled.png");
      this.xSize = 256;
      this.ySize = 256;
      this.closeOnEsc = true;
   }

   public void initGui() {
      this.xSize = (int)((double)this.width * 0.88D);
      this.ySize = (int)((double)this.xSize * 0.56D);
      this.bgScale = (float)this.xSize / 440.0F;
      super.initGui();
      if(this.textarea != null) {
         this.text = this.textarea.getText();
      }

      int yoffset = (int)((double)this.ySize * 0.02D);
      this.addTextField(this.textarea = new GuiNpcTextArea(2, this, this.guiLeft + yoffset, this.guiTop + yoffset, this.xSize - 100 - yoffset * 2, this.ySize - yoffset * 2, this.text));
      this.buttonList.add(new GuiNpcButton(102, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 20, 56, 20, "gui.clear"));
      this.buttonList.add(new GuiNpcButton(101, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 43, 56, 20, "gui.paste"));
      this.buttonList.add(new GuiNpcButton(100, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 66, 56, 20, "gui.copy"));
      this.buttonList.add(new GuiNpcButton(0, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 160, 56, 20, "gui.close"));
      this.xSize = 420;
      this.ySize = 256;
   }

   public void close() {
      this.text = this.getTextField(2).getText();
      super.close();
   }

   public void buttonEvent(GuiButton guibutton) {
      int id = guibutton.id;
      if(id == 100) {
         NoppesStringUtils.setClipboardContents(this.getTextField(2).getText());
      }

      if(id == 101) {
         this.getTextField(2).setText(NoppesStringUtils.getClipboardContents());
      }

      if(id == 102) {
         this.getTextField(2).setText("");
      }

      if(id == 0) {
         this.close();
      }

   }
}
