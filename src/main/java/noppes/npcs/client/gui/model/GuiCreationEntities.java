package noppes.npcs.client.gui.model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import noppes.npcs.client.gui.model.GuiCreationScreenInterface;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiCreationEntities extends GuiCreationScreenInterface implements ICustomScrollListener {

   public HashMap data = new HashMap();
   private List list;
   private GuiCustomScroll scroll;
   private boolean resetToSelected = true;


   public GuiCreationEntities(EntityNPCInterface npc) {
      super(npc);
      Iterator var2 = EntityList.stringToClassMapping.keySet().iterator();

      while(var2.hasNext()) {
         Object name = var2.next();
         Class c = (Class)EntityList.stringToClassMapping.get(name);

         try {
            if(EntityLiving.class.isAssignableFrom(c) && c.getConstructor(new Class[]{World.class}) != null && !Modifier.isAbstract(c.getModifiers()) && Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(c) instanceof RendererLivingEntity) {
               String e = name.toString();
               if(!e.toLowerCase().contains("customnpc")) {
                  this.data.put(name.toString(), c.asSubclass(EntityLivingBase.class));
               }
            }
         } catch (SecurityException var6) {
            var6.printStackTrace();
         } catch (NoSuchMethodException var7) {
            ;
         }
      }

      this.data.put("NPC 64x32", EntityNPC64x32.class);
      this.list = new ArrayList(this.data.keySet());
      this.list.add("NPC");
      Collections.sort(this.list, String.CASE_INSENSITIVE_ORDER);
      this.active = 1;
      this.xOffset = 60;
   }

   public void initGui() {
      super.initGui();
      this.addButton(new GuiNpcButton(10, this.guiLeft, this.guiTop + 46, 120, 20, "Reset To NPC"));
      if(this.scroll == null) {
         this.scroll = new GuiCustomScroll(this, 0);
         this.scroll.setUnsortedList(this.list);
      }

      this.scroll.guiLeft = this.guiLeft;
      this.scroll.guiTop = this.guiTop + 68;
      this.scroll.setSize(100, this.ySize - 96);
      String selected = "NPC";
      if(this.entity != null) {
         Iterator var2 = this.data.entrySet().iterator();

         while(var2.hasNext()) {
            Entry en = (Entry)var2.next();
            if(((Class)en.getValue()).toString().equals(this.entity.getClass().toString())) {
               selected = (String)en.getKey();
            }
         }
      }

      this.scroll.setSelected(selected);
      if(this.resetToSelected) {
         this.scroll.scrollTo(this.scroll.getSelected());
         this.resetToSelected = false;
      }

      this.addScroll(this.scroll);
   }

   protected void actionPerformed(GuiButton btn) {
      super.actionPerformed(btn);
      if(btn.id == 10) {
         this.playerdata.setEntityClass((Class)null);
         this.resetToSelected = true;
         this.initGui();
      }

   }

   public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
      this.playerdata.setEntityClass((Class)this.data.get(scroll.getSelected()));
      EntityLivingBase entity = this.playerdata.getEntity(this.npc);
      if(entity != null) {
         RendererLivingEntity render = (RendererLivingEntity)this.mc.getRenderManager().getEntityClassRenderObject(entity.getClass());
         this.npc.display.setSkinTexture(NPCRendererHelper.getTexture(render, entity));
      } else {
         this.npc.display.setSkinTexture("customnpcs:textures/entity/humanmale/Steve.png");
      }

      this.initGui();
   }
}
