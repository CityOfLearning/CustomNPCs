package noppes.npcs.client.gui.player;

import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class GuiNpcFollowerHire extends GuiContainerNPCInterface {

   private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/followerhire.png");
   private EntityNPCInterface npc;
   private ContainerNPCFollowerHire container;
   private RoleFollower role;


   public GuiNpcFollowerHire(EntityNPCInterface npc, ContainerNPCFollowerHire container) {
      super(npc, container);
      this.container = container;
      this.npc = npc;
      this.role = (RoleFollower)npc.roleInterface;
      this.closeOnEsc = true;
   }

   public void initGui() {
      super.initGui();
      this.addButton(new GuiNpcButton(5, this.guiLeft + 26, this.guiTop + 60, 50, 20, StatCollector.translateToLocal("follower.hire")));
   }

   public void actionPerformed(GuiButton guibutton) {
      super.actionPerformed(guibutton);
      if(guibutton.id == 5) {
         NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerHire, new Object[0]);
         this.close();
      }

   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {}

   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture(this.resource);
      int l = (this.width - this.xSize) / 2;
      int i1 = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);
      int index = 0;
      Iterator var7 = this.role.inventory.items.keySet().iterator();

      while(var7.hasNext()) {
         int id = ((Integer)var7.next()).intValue();
         ItemStack itemstack = (ItemStack)this.role.inventory.items.get(Integer.valueOf(id));
         if(itemstack != null) {
            int days = 1;
            if(this.role.rates.containsKey(Integer.valueOf(id))) {
               days = ((Integer)this.role.rates.get(Integer.valueOf(id))).intValue();
            }

            int yOffset = index * 26;
            int x = this.guiLeft + 78;
            int y = this.guiTop + yOffset + 10;
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            this.itemRender.renderItemAndEffectIntoGUI(itemstack, x + 11, y);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, x + 11, y);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            String daysS = days + " " + (days == 1?StatCollector.translateToLocal("follower.day"):StatCollector.translateToLocal("follower.days"));
            this.fontRendererObj.drawString(" = " + daysS, x + 27, y + 4, CustomNpcResourceListener.DefaultTextColor);
            if(this.isPointInRegion(x - this.guiLeft + 11, y - this.guiTop, 16, 16, this.mouseX, this.mouseY)) {
               this.renderToolTip(itemstack, this.mouseX, this.mouseY);
            }

            ++index;
         }
      }

   }

   public void save() {}
}
