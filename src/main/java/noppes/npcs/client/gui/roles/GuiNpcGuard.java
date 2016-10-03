package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobGuard;

public class GuiNpcGuard extends GuiNPCInterface2 {

   private JobGuard role;
   private GuiCustomScroll scroll1;
   private GuiCustomScroll scroll2;


   public GuiNpcGuard(EntityNPCInterface npc) {
      super(npc);
      this.role = (JobGuard)npc.jobInterface;
   }

   public void initGui() {
      super.initGui();
      this.addButton(new GuiNpcButton(0, this.guiLeft + 10, this.guiTop + 4, 100, 20, "guard.animals"));
      this.addButton(new GuiNpcButton(1, this.guiLeft + 140, this.guiTop + 4, 100, 20, "guard.mobs"));
      this.addButton(new GuiNpcButton(2, this.guiLeft + 275, this.guiTop + 4, 100, 20, "guard.creepers"));
      if(this.scroll1 == null) {
         this.scroll1 = new GuiCustomScroll(this, 0);
         this.scroll1.setSize(175, 154);
      }

      this.scroll1.guiLeft = this.guiLeft + 4;
      this.scroll1.guiTop = this.guiTop + 58;
      this.addScroll(this.scroll1);
      this.addLabel(new GuiNpcLabel(11, "guard.availableTargets", this.guiLeft + 4, this.guiTop + 48));
      if(this.scroll2 == null) {
         this.scroll2 = new GuiCustomScroll(this, 1);
         this.scroll2.setSize(175, 154);
      }

      this.scroll2.guiLeft = this.guiLeft + 235;
      this.scroll2.guiTop = this.guiTop + 58;
      this.addScroll(this.scroll2);
      this.addLabel(new GuiNpcLabel(12, "guard.currentTargets", this.guiLeft + 235, this.guiTop + 48));
      ArrayList all = new ArrayList();
      Iterator var2 = EntityList.stringToClassMapping.keySet().iterator();

      while(var2.hasNext()) {
         Object entity = var2.next();
         String name = "entity." + entity + ".name";
         Class cl = (Class)EntityList.stringToClassMapping.get(entity);
         if(!this.role.targets.contains(name) && !EntityNPCInterface.class.isAssignableFrom(cl) && EntityLivingBase.class.isAssignableFrom(cl)) {
            all.add(name);
         }
      }

      this.scroll1.setList(all);
      this.scroll2.setList(this.role.targets);
      this.addButton(new GuiNpcButton(11, this.guiLeft + 180, this.guiTop + 80, 55, 20, ">"));
      this.addButton(new GuiNpcButton(12, this.guiLeft + 180, this.guiTop + 102, 55, 20, "<"));
      this.addButton(new GuiNpcButton(13, this.guiLeft + 180, this.guiTop + 130, 55, 20, ">>"));
      this.addButton(new GuiNpcButton(14, this.guiLeft + 180, this.guiTop + 152, 55, 20, "<<"));
   }

   protected void actionPerformed(GuiButton guibutton) {
      GuiNpcButton button = (GuiNpcButton)guibutton;
      Iterator all;
      Object entity;
      String entity1;
      Class name;
      if(button.id == 0) {
         all = EntityList.stringToClassMapping.keySet().iterator();

         while(all.hasNext()) {
            entity = all.next();
            entity1 = "entity." + entity + ".name";
            name = (Class)EntityList.stringToClassMapping.get(entity);
            if(EntityAnimal.class.isAssignableFrom(name) && !this.role.targets.contains(entity1)) {
               this.role.targets.add(entity1);
            }
         }

         this.scroll1.selected = -1;
         this.scroll2.selected = -1;
         this.initGui();
      }

      if(button.id == 1) {
         all = EntityList.stringToClassMapping.keySet().iterator();

         while(all.hasNext()) {
            entity = all.next();
            entity1 = "entity." + entity + ".name";
            name = (Class)EntityList.stringToClassMapping.get(entity);
            if(EntityMob.class.isAssignableFrom(name) && !EntityCreeper.class.isAssignableFrom(name) && !this.role.targets.contains(entity1)) {
               this.role.targets.add(entity1);
            }
         }

         this.scroll1.selected = -1;
         this.scroll2.selected = -1;
         this.initGui();
      }

      if(button.id == 2) {
         all = EntityList.stringToClassMapping.keySet().iterator();

         while(all.hasNext()) {
            entity = all.next();
            entity1 = "entity." + entity + ".name";
            name = (Class)EntityList.stringToClassMapping.get(entity);
            if(EntityCreeper.class.isAssignableFrom(name) && !this.role.targets.contains(entity1)) {
               this.role.targets.add(entity1);
            }
         }

         this.scroll1.selected = -1;
         this.scroll2.selected = -1;
         this.initGui();
      }

      if(button.id == 11 && this.scroll1.hasSelected()) {
         this.role.targets.add(this.scroll1.getSelected());
         this.scroll1.selected = -1;
         this.scroll2.selected = -1;
         this.initGui();
      }

      if(button.id == 12 && this.scroll2.hasSelected()) {
         this.role.targets.remove(this.scroll2.getSelected());
         this.scroll2.selected = -1;
         this.initGui();
      }

      if(button.id == 13) {
         this.role.targets.clear();
         ArrayList all1 = new ArrayList();
         Iterator entity2 = EntityList.stringToClassMapping.keySet().iterator();

         while(entity2.hasNext()) {
            Object entity3 = entity2.next();
            String name1 = "entity." + entity3 + ".name";
            Class cl = (Class)EntityList.stringToClassMapping.get(entity3);
            if(EntityLivingBase.class.isAssignableFrom(cl) && !EntityNPCInterface.class.isAssignableFrom(cl)) {
               all1.add(name1);
            }
         }

         this.role.targets = all1;
         this.scroll1.selected = -1;
         this.scroll2.selected = -1;
         this.initGui();
      }

      if(button.id == 14) {
         this.role.targets.clear();
         this.scroll1.selected = -1;
         this.scroll2.selected = -1;
         this.initGui();
      }

   }

   public void save() {
      Client.sendData(EnumPacketServer.JobSave, new Object[]{this.role.writeToNBT(new NBTTagCompound())});
   }
}
