package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.global.GuiNPCQuestSelection;
import noppes.npcs.client.gui.roles.SubGuiNpcConversationLine;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobConversation;

public class GuiNpcConversation extends GuiNPCInterface2 implements ITextfieldListener, GuiSelectionListener {

   private JobConversation job;
   private int slot = -1;
   private GuiNPCQuestSelection questSelection;


   public GuiNpcConversation(EntityNPCInterface npc) {
      super(npc);
      this.job = (JobConversation)npc.jobInterface;
   }

   public void initGui() {
      super.initGui();
      this.addLabel(new GuiNpcLabel(40, "gui.name", this.guiLeft + 40, this.guiTop + 4));
      this.addLabel(new GuiNpcLabel(41, "gui.name", this.guiLeft + 240, this.guiTop + 4));
      this.addLabel(new GuiNpcLabel(42, "conversation.delay", this.guiLeft + 164, this.guiTop + 4));
      this.addLabel(new GuiNpcLabel(43, "conversation.delay", this.guiLeft + 364, this.guiTop + 4));

      for(int title = 0; title < 14; ++title) {
         JobConversation.ConversationLine line = this.job.getLine(title);
         int offset = title >= 7?200:0;
         this.addLabel(new GuiNpcLabel(title, "" + (title + 1), this.guiLeft + 5 + offset - (title > 8?6:0), this.guiTop + 18 + title % 7 * 22));
         this.addTextField(new GuiNpcTextField(title, this, this.fontRendererObj, this.guiLeft + 13 + offset, this.guiTop + 13 + title % 7 * 22, 100, 20, line.npc));
         this.addButton(new GuiNpcButton(title, this.guiLeft + 115 + offset, this.guiTop + 13 + title % 7 * 22, 46, 20, "conversation.line"));
         if(title > 0) {
            this.addTextField(new GuiNpcTextField(title + 14, this, this.fontRendererObj, this.guiLeft + 164 + offset, this.guiTop + 13 + title % 7 * 22, 30, 20, line.delay + ""));
            this.getTextField(title + 14).numbersOnly = true;
            this.getTextField(title + 14).setMinMaxDefault(5, 1000, 40);
         }
      }

      this.addLabel(new GuiNpcLabel(50, "conversation.delay", this.guiLeft + 202, this.guiTop + 175));
      this.addTextField(new GuiNpcTextField(50, this, this.fontRendererObj, this.guiLeft + 260, this.guiTop + 170, 40, 20, this.job.generalDelay + ""));
      this.getTextField(50).numbersOnly = true;
      this.getTextField(50).setMinMaxDefault(10, 1000000, 12000);
      this.addLabel(new GuiNpcLabel(54, "gui.range", this.guiLeft + 202, this.guiTop + 196));
      this.addTextField(new GuiNpcTextField(54, this, this.fontRendererObj, this.guiLeft + 260, this.guiTop + 191, 40, 20, this.job.range + ""));
      this.getTextField(54).numbersOnly = true;
      this.getTextField(54).setMinMaxDefault(4, 60, 20);
      this.addLabel(new GuiNpcLabel(51, "quest.quest", this.guiLeft + 13, this.guiTop + 175));
      String var4 = this.job.questTitle;
      if(var4.isEmpty()) {
         var4 = "gui.select";
      }

      this.addButton(new GuiNpcButton(51, this.guiLeft + 70, this.guiTop + 170, 100, 20, var4));
      this.addButton(new GuiNpcButton(52, this.guiLeft + 171, this.guiTop + 170, 20, 20, "X"));
      this.addLabel(new GuiNpcLabel(53, "availability.name", this.guiLeft + 13, this.guiTop + 196));
      this.addButton(new GuiNpcButton(53, this.guiLeft + 110, this.guiTop + 191, 60, 20, "selectServer.edit"));
      this.addButton(new GuiNpcButton(55, this.guiLeft + 310, this.guiTop + 181, 96, 20, new String[]{"gui.always", "gui.playernearby"}, this.job.mode));
   }

   protected void actionPerformed(GuiButton guibutton) {
      GuiNpcButton button = (GuiNpcButton)guibutton;
      if(button.id >= 0 && button.id < 14) {
         this.slot = button.id;
         JobConversation.ConversationLine line = this.job.getLine(this.slot);
         this.setSubGui(new SubGuiNpcConversationLine(line.text, line.sound));
      }

      if(button.id == 51) {
         NoppesUtil.openGUI(this.player, this.questSelection = new GuiNPCQuestSelection(this.npc, this, this.job.quest));
      }

      if(button.id == 52) {
         this.job.quest = -1;
         this.job.questTitle = "";
         this.initGui();
      }

      if(button.id == 53) {
         this.setSubGui(new SubGuiNpcAvailability(this.job.availability));
      }

      if(button.id == 55) {
         this.job.mode = button.getValue();
      }

   }

   public void selected(int ob, String name) {
      this.job.quest = ob;
      this.job.questTitle = this.questSelection.getSelected();
      this.initGui();
   }

   public void closeSubGui(SubGuiInterface gui) {
      super.closeSubGui(gui);
      if(gui instanceof SubGuiNpcConversationLine) {
         SubGuiNpcConversationLine sub = (SubGuiNpcConversationLine)gui;
         JobConversation.ConversationLine line = this.job.getLine(this.slot);
         line.text = sub.line;
         line.sound = sub.sound;
      }

   }

   public void save() {
      Client.sendData(EnumPacketServer.JobSave, new Object[]{this.job.writeToNBT(new NBTTagCompound())});
   }

   public void unFocused(GuiNpcTextField textfield) {
      JobConversation.ConversationLine line;
      if(textfield.id >= 0 && textfield.id < 14) {
         line = this.job.getLine(textfield.id);
         line.npc = textfield.getText();
      }

      if(textfield.id >= 14 && textfield.id < 28) {
         line = this.job.getLine(textfield.id - 14);
         line.delay = textfield.getInteger();
      }

      if(textfield.id == 50) {
         this.job.generalDelay = textfield.getInteger();
      }

      if(textfield.id == 54) {
         this.job.range = textfield.getInteger();
      }

   }
}
