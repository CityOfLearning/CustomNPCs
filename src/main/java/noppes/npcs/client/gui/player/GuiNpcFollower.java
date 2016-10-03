package noppes.npcs.client.gui.player;

import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class GuiNpcFollower extends GuiContainerNPCInterface implements IGuiData {

   private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/follower.png");
   private EntityNPCInterface npc;
   private RoleFollower role;


   public GuiNpcFollower(EntityNPCInterface npc, ContainerNPCFollower container) {
      super(npc, container);
      this.npc = npc;
      this.role = (RoleFollower)npc.roleInterface;
      this.closeOnEsc = true;
      NoppesUtilPlayer.sendData(EnumPlayerPacket.RoleGet, new Object[0]);
   }

   public void initGui() {
      super.initGui();
      this.buttonList.clear();
      this.addButton(new GuiNpcButton(4, this.guiLeft + 100, this.guiTop + 110, 50, 20, new String[]{StatCollector.translateToLocal("follower.waiting"), StatCollector.translateToLocal("follower.following")}, this.role.isFollowing?1:0));
      if(!this.role.infiniteDays) {
         this.addButton(new GuiNpcButton(5, this.guiLeft + 8, this.guiTop + 30, 50, 20, StatCollector.translateToLocal("follower.hire")));
      }

   }

   public void actionPerformed(GuiButton guibutton) {
      super.actionPerformed(guibutton);
      int id = guibutton.id;
      if(id == 4) {
         NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerState, new Object[0]);
      }

      if(id == 5) {
         NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerExtend, new Object[0]);
      }

   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      this.fontRendererObj.drawString(StatCollector.translateToLocal("follower.health") + ": " + this.npc.getHealth() + "/" + this.npc.getMaxHealth(), 62, 70, CustomNpcResourceListener.DefaultTextColor);
      if(!this.role.infiniteDays) {
         if(this.role.getDays() <= 1) {
            this.fontRendererObj.drawString(StatCollector.translateToLocal("follower.daysleft") + ": " + StatCollector.translateToLocal("follower.lastday"), 62, 94, CustomNpcResourceListener.DefaultTextColor);
         } else {
            this.fontRendererObj.drawString(StatCollector.translateToLocal("follower.daysleft") + ": " + (this.role.getDays() - 1), 62, 94, CustomNpcResourceListener.DefaultTextColor);
         }
      }

   }

   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture(this.resource);
      int l = this.guiLeft;
      int i1 = this.guiTop;
      this.drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);
      int index = 0;
      if(!this.role.infiniteDays) {
         Iterator var7 = this.role.inventory.items.keySet().iterator();

         while(var7.hasNext()) {
            int id = ((Integer)var7.next()).intValue();
            ItemStack itemstack = (ItemStack)this.role.inventory.items.get(Integer.valueOf(id));
            if(itemstack != null) {
               int days = 1;
               if(this.role.rates.containsKey(Integer.valueOf(id))) {
                  days = ((Integer)this.role.rates.get(Integer.valueOf(id))).intValue();
               }

               int yOffset = index * 20;
               int x = this.guiLeft + 68;
               int y = this.guiTop + yOffset + 4;
               GlStateManager.enableRescaleNormal();
               RenderHelper.enableGUIStandardItemLighting();
               this.itemRender.renderItemAndEffectIntoGUI(itemstack, x + 11, y);
               this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, x + 11, y);
               RenderHelper.disableStandardItemLighting();
               GlStateManager.disableRescaleNormal();
               String daysS = days + " " + (days == 1?StatCollector.translateToLocal("follower.day"):StatCollector.translateToLocal("follower.days"));
               this.fontRendererObj.drawString(" = " + daysS, x + 27, y + 4, CustomNpcResourceListener.DefaultTextColor);
               ++index;
            }
         }
      }

      this.drawNpc(33, 131);
   }

   public void save() {}

   public void setGuiData(NBTTagCompound compound) {
      this.npc.roleInterface.readFromNBT(compound);
      this.initGui();
   }
}
