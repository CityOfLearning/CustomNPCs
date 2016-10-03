package noppes.npcs.client.gui.global;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiMailmanSendSetup;
import noppes.npcs.client.gui.SubGuiNpcCommand;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.PlayerMail;

public class SubGuiNpcDialogExtra extends SubGuiInterface implements ISubGuiListener {

   private Dialog dialog;
   private int slot = 0;
   public GuiScreen parent2;


   public SubGuiNpcDialogExtra(Dialog dialog, GuiScreen parent) {
      this.parent2 = parent;
      this.dialog = dialog;
      this.setBackground("menubg.png");
      this.xSize = 256;
      this.ySize = 216;
      this.closeOnEsc = true;
   }

   public void initGui() {
      super.initGui();
      int y = this.guiTop + 4;
      this.addButton(new GuiNpcButton(13, this.guiLeft + 4, y, 164, 20, "mailbox.setup"));
      this.addButton(new GuiNpcButton(14, this.guiLeft + 170, y, 20, 20, "X"));
      if(!this.dialog.mail.subject.isEmpty()) {
         this.getButton(13).setDisplayText(this.dialog.mail.subject);
      }

      GuiNpcButton var10001 = new GuiNpcButton;
      int var10004 = this.guiLeft + 120;
      y += 22;
      var10001.<init>(10, var10004, y, 50, 20, "selectServer.edit");
      this.addButton(var10001);
      this.addLabel(new GuiNpcLabel(10, "advMode.command", this.guiLeft + 4, y + 5));
      GuiNpcButtonYesNo var2 = new GuiNpcButtonYesNo;
      var10004 = this.guiLeft + 120;
      y += 22;
      var2.<init>(11, var10004, y, this.dialog.hideNPC);
      this.addButton(var2);
      this.addLabel(new GuiNpcLabel(11, "dialog.hideNPC", this.guiLeft + 4, y + 5));
      var2 = new GuiNpcButtonYesNo;
      var10004 = this.guiLeft + 120;
      y += 22;
      var2.<init>(12, var10004, y, this.dialog.showWheel);
      this.addButton(var2);
      this.addLabel(new GuiNpcLabel(12, "dialog.showWheel", this.guiLeft + 4, y + 5));
      var2 = new GuiNpcButtonYesNo;
      var10004 = this.guiLeft + 120;
      y += 22;
      var2.<init>(15, var10004, y, this.dialog.disableEsc);
      this.addButton(var2);
      this.addLabel(new GuiNpcLabel(15, "dialog.disableEsc", this.guiLeft + 4, y + 5));
      this.addButton(new GuiNpcButton(66, this.guiLeft + 82, this.guiTop + 192, 98, 20, "gui.done"));
   }

   protected void actionPerformed(GuiButton guibutton) {
      GuiNpcButton button = (GuiNpcButton)guibutton;
      if(button.id == 10) {
         this.setSubGui(new SubGuiNpcCommand(this.dialog.command));
      }

      if(button.id == 11) {
         this.dialog.hideNPC = button.getValue() == 1;
      }

      if(button.id == 12) {
         this.dialog.showWheel = button.getValue() == 1;
      }

      if(button.id == 15) {
         this.dialog.disableEsc = button.getValue() == 1;
      }

      if(button.id == 13) {
         this.setSubGui(new SubGuiMailmanSendSetup(this.dialog.mail, this.getParent()));
      }

      if(button.id == 14) {
         this.dialog.mail = new PlayerMail();
         this.initGui();
      }

      if(button.id == 66) {
         this.close();
         if(this.parent2 != null) {
            NoppesUtil.openGUI(this.player, this.parent2);
         }
      }

   }

   public void subGuiClosed(SubGuiInterface subgui) {
      if(subgui instanceof SubGuiNpcCommand) {
         this.dialog.command = ((SubGuiNpcCommand)subgui).command;
      }

   }
}
