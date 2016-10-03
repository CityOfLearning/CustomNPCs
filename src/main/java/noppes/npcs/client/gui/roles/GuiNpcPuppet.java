package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.roles.JobPuppet;

public class GuiNpcPuppet extends GuiNPCInterface implements ISliderListener {

   private GuiScreen parent;
   private int type = 6;
   private JobPuppet job;
   private JobPuppet.PartConfig part;


   public GuiNpcPuppet(GuiScreen parent, EntityCustomNpc npc) {
      super(npc);
      this.parent = parent;
      this.ySize = 230;
      this.xSize = 400;
      this.job = (JobPuppet)npc.jobInterface;
      this.closeOnEsc = true;
   }

   public void initGui() {
      super.initGui();
      int y = this.guiTop;
      this.addLabel(new GuiNpcLabel(26, "gui.settings", this.guiLeft + 55, y + 5, 16777215));
      if(this.type == 6) {
         GuiNpcButton var10001 = new GuiNpcButton;
         int var10004 = this.guiLeft + 120;
         y += 14;
         var10001.<init>(30, var10004, y, 60, 20, new String[]{"gui.yes", "gui.no"}, this.job.whileStanding?0:1);
         this.addButton(var10001);
         this.addLabel(new GuiNpcLabel(30, "puppet.standing", this.guiLeft + 30, y + 5, 16777215));
         var10001 = new GuiNpcButton;
         var10004 = this.guiLeft + 120;
         y += 22;
         var10001.<init>(31, var10004, y, 60, 20, new String[]{"gui.yes", "gui.no"}, this.job.whileMoving?0:1);
         this.addButton(var10001);
         this.addLabel(new GuiNpcLabel(31, "puppet.walking", this.guiLeft + 30, y + 5, 16777215));
         var10001 = new GuiNpcButton;
         var10004 = this.guiLeft + 120;
         y += 22;
         var10001.<init>(32, var10004, y, 60, 20, new String[]{"gui.yes", "gui.no"}, this.job.whileAttacking?0:1);
         this.addButton(var10001);
         this.addLabel(new GuiNpcLabel(32, "puppet.attacking", this.guiLeft + 30, y + 5, 16777215));
         y += 24;
      } else {
         this.addButton(new GuiNpcButton(6, this.guiLeft + 110, y, 60, 20, "selectServer.edit"));
         y += 24;
      }

      this.addLabel(new GuiNpcLabel(20, "model.head", this.guiLeft + 55, y + 5, 16777215));
      if(this.type == 0) {
         this.drawSlider(y, this.job.head);
         y += 90;
      } else {
         this.addButton(new GuiNpcButton(0, this.guiLeft + 110, y, 60, 20, "selectServer.edit"));
         y += 24;
      }

      this.addLabel(new GuiNpcLabel(21, "model.body", this.guiLeft + 55, y + 5, 16777215));
      if(this.type == 1) {
         this.drawSlider(y, this.job.body);
         y += 90;
      } else {
         this.addButton(new GuiNpcButton(1, this.guiLeft + 110, y, 60, 20, "selectServer.edit"));
         y += 24;
      }

      this.addLabel(new GuiNpcLabel(22, "model.larm", this.guiLeft + 55, y + 5, 16777215));
      if(this.type == 2) {
         this.drawSlider(y, this.job.larm);
         y += 90;
      } else {
         this.addButton(new GuiNpcButton(2, this.guiLeft + 110, y, 60, 20, "selectServer.edit"));
         y += 24;
      }

      this.addLabel(new GuiNpcLabel(23, "model.rarm", this.guiLeft + 55, y + 5, 16777215));
      if(this.type == 3) {
         this.drawSlider(y, this.job.rarm);
         y += 90;
      } else {
         this.addButton(new GuiNpcButton(3, this.guiLeft + 110, y, 60, 20, "selectServer.edit"));
         y += 24;
      }

      this.addLabel(new GuiNpcLabel(24, "model.lleg", this.guiLeft + 55, y + 5, 16777215));
      if(this.type == 4) {
         this.drawSlider(y, this.job.lleg);
         y += 90;
      } else {
         this.addButton(new GuiNpcButton(4, this.guiLeft + 110, y, 60, 20, "selectServer.edit"));
         y += 24;
      }

      this.addLabel(new GuiNpcLabel(25, "model.rleg", this.guiLeft + 55, y + 5, 16777215));
      if(this.type == 5) {
         this.drawSlider(y, this.job.rleg);
         y += 90;
      } else {
         this.addButton(new GuiNpcButton(5, this.guiLeft + 110, y, 60, 20, "selectServer.edit"));
         y += 24;
      }

      this.addButton(new GuiNpcButton(66, this.guiLeft + this.xSize - 22, this.guiTop, 20, 20, "X"));
   }

   private void drawSlider(int y, JobPuppet.PartConfig config) {
      this.part = config;
      this.addButton(new GuiNpcButton(29, this.guiLeft + 100, y, 80, 20, new String[]{"gui.enabled", "gui.disabled"}, config.disabled?1:0));
      y += 22;
      this.addLabel(new GuiNpcLabel(10, "X", this.guiLeft, y + 5, 16777215));
      this.addSlider(new GuiNpcSlider(this, 10, this.guiLeft + 50, y, config.rotationX + 0.5F));
      y += 22;
      this.addLabel(new GuiNpcLabel(11, "Y", this.guiLeft, y + 5, 16777215));
      this.addSlider(new GuiNpcSlider(this, 11, this.guiLeft + 50, y, config.rotationY + 0.5F));
      y += 22;
      this.addLabel(new GuiNpcLabel(12, "Z", this.guiLeft, y + 5, 16777215));
      this.addSlider(new GuiNpcSlider(this, 12, this.guiLeft + 50, y, config.rotationZ + 0.5F));
   }

   public void drawScreen(int i, int j, float f) {
      this.drawNpc(320, 200);
      super.drawScreen(i, j, f);
   }

   protected void actionPerformed(GuiButton btn) {
      super.actionPerformed(btn);
      if(btn.id < 7) {
         this.type = btn.id;
         this.initGui();
      }

      if(btn instanceof GuiNpcButton) {
         GuiNpcButton button = (GuiNpcButton)btn;
         if(btn.id == 29) {
            this.part.disabled = button.getValue() == 1;
         }

         if(btn.id == 30) {
            this.job.whileStanding = button.getValue() == 0;
         }

         if(btn.id == 31) {
            this.job.whileMoving = button.getValue() == 0;
         }

         if(btn.id == 32) {
            this.job.whileAttacking = button.getValue() == 0;
         }

         if(btn.id == 66) {
            this.close();
         }

      }
   }

   public void close() {
      this.mc.displayGuiScreen(this.parent);
      Client.sendData(EnumPacketServer.JobSave, new Object[]{this.job.writeToNBT(new NBTTagCompound())});
   }

   public void mouseDragged(GuiNpcSlider slider) {
      int percent = (int)(slider.sliderValue * 360.0F);
      slider.setString(percent + "%");
      if(slider.id == 10) {
         this.part.rotationX = slider.sliderValue - 0.5F;
      }

      if(slider.id == 11) {
         this.part.rotationY = slider.sliderValue - 0.5F;
      }

      if(slider.id == 12) {
         this.part.rotationZ = slider.sliderValue - 0.5F;
      }

      this.npc.updateHitbox();
   }

   public void mousePressed(GuiNpcSlider slider) {}

   public void mouseReleased(GuiNpcSlider slider) {}

   public void save() {}
}
