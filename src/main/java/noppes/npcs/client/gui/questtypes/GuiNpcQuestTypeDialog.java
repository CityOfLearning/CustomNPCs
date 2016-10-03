package noppes.npcs.client.gui.questtypes;

import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNPCDialogSelection;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestDialog;

public class GuiNpcQuestTypeDialog extends SubGuiInterface implements GuiSelectionListener, IGuiData {

   private GuiScreen parent;
   private QuestDialog quest;
   private HashMap data = new HashMap();
   private int selectedSlot;


   public GuiNpcQuestTypeDialog(EntityNPCInterface npc, Quest q, GuiScreen parent) {
      this.npc = npc;
      this.parent = parent;
      this.title = "Quest Dialog Setup";
      this.quest = (QuestDialog)q.questInterface;
      this.setBackground("menubg.png");
      this.xSize = 256;
      this.ySize = 216;
      this.closeOnEsc = true;
      Client.sendData(EnumPacketServer.QuestDialogGetTitle, new Object[]{Integer.valueOf(this.quest.dialogs.containsKey(Integer.valueOf(0))?((Integer)this.quest.dialogs.get(Integer.valueOf(0))).intValue():-1), Integer.valueOf(this.quest.dialogs.containsKey(Integer.valueOf(1))?((Integer)this.quest.dialogs.get(Integer.valueOf(1))).intValue():-1), Integer.valueOf(this.quest.dialogs.containsKey(Integer.valueOf(2))?((Integer)this.quest.dialogs.get(Integer.valueOf(2))).intValue():-1)});
   }

   public void initGui() {
      super.initGui();

      for(int i = 0; i < 3; ++i) {
         String title = "dialog.selectoption";
         if(this.data.containsKey(Integer.valueOf(i))) {
            title = (String)this.data.get(Integer.valueOf(i));
         }

         this.addButton(new GuiNpcButton(i + 9, this.guiLeft + 10, 55 + i * 22, 20, 20, "X"));
         this.addButton(new GuiNpcButton(i + 3, this.guiLeft + 34, 55 + i * 22, 210, 20, title));
      }

      this.addButton(new GuiNpcButton(0, this.guiLeft + 150, this.guiTop + 190, 98, 20, "gui.back"));
   }

   protected void actionPerformed(GuiButton guibutton) {
      GuiNpcButton button = (GuiNpcButton)guibutton;
      if(button.id == 0) {
         this.close();
      }

      int slot;
      if(button.id >= 3 && button.id < 9) {
         this.selectedSlot = button.id - 3;
         slot = -1;
         if(this.quest.dialogs.containsKey(Integer.valueOf(this.selectedSlot))) {
            slot = ((Integer)this.quest.dialogs.get(Integer.valueOf(this.selectedSlot))).intValue();
         }

         GuiNPCDialogSelection gui = new GuiNPCDialogSelection(this.npc, this.parent, slot);
         gui.listener = this;
         NoppesUtil.openGUI(this.player, gui);
      }

      if(button.id >= 9 && button.id < 15) {
         slot = button.id - 9;
         this.quest.dialogs.remove(Integer.valueOf(slot));
         this.data.remove(Integer.valueOf(slot));
         this.save();
         this.initGui();
      }

   }

   public void save() {}

   public void selected(int id, String name) {
      this.quest.dialogs.put(Integer.valueOf(this.selectedSlot), Integer.valueOf(id));
      this.data.put(Integer.valueOf(this.selectedSlot), name);
   }

   public void setGuiData(NBTTagCompound compound) {
      this.data.clear();
      if(compound.hasKey("1")) {
         this.data.put(Integer.valueOf(0), compound.getString("1"));
      }

      if(compound.hasKey("2")) {
         this.data.put(Integer.valueOf(1), compound.getString("2"));
      }

      if(compound.hasKey("3")) {
         this.data.put(Integer.valueOf(2), compound.getString("3"));
      }

      this.initGui();
   }
}
