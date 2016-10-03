package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNpcSoundSelection;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

public class SubGuiNpcConversationLine extends SubGuiInterface implements ITextfieldListener {

   public String line;
   public String sound;
   private GuiNpcSoundSelection gui;


   public SubGuiNpcConversationLine(String line, String sound) {
      this.line = line;
      this.sound = sound;
      this.setBackground("menubg.png");
      this.xSize = 256;
      this.ySize = 216;
      this.closeOnEsc = true;
   }

   public void initGui() {
      super.initGui();
      this.addLabel(new GuiNpcLabel(0, "Line", this.guiLeft + 4, this.guiTop + 10));
      this.addTextField(new GuiNpcTextField(0, this, this.fontRendererObj, this.guiLeft + 4, this.guiTop + 22, 200, 20, this.line));
      this.addButton(new GuiNpcButton(1, this.guiLeft + 4, this.guiTop + 55, 90, 20, "Select Sound"));
      this.addButton(new GuiNpcButton(2, this.guiLeft + 96, this.guiTop + 55, 20, 20, "X"));
      this.addLabel(new GuiNpcLabel(1, this.sound, this.guiLeft + 4, this.guiTop + 81));
      this.addButton(new GuiNpcButton(66, this.guiLeft + 162, this.guiTop + 192, 90, 20, "gui.done"));
   }

   public void unFocused(GuiNpcTextField textfield) {
      this.line = textfield.getText();
   }

   public void elementClicked() {
      this.sound = this.gui.getSelected();
   }

   protected void actionPerformed(GuiButton guibutton) {
      int id = guibutton.id;
      if(id == 1) {
         NoppesUtil.openGUI(this.player, this.gui = new GuiNpcSoundSelection(this.parent, this.sound));
      }

      if(id == 2) {
         this.sound = "";
         this.initGui();
      }

      if(id == 66) {
         this.close();
      }

   }
}
