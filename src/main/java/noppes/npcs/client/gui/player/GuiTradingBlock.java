package noppes.npcs.client.gui.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerTradingBlock;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiTradingBlock extends GuiContainerNPCInterface implements IGuiData {

   private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/tradingblock.png");
   public Map items = new HashMap();
   private ContainerTradingBlock container;


   public GuiTradingBlock(ContainerTradingBlock container) {
      super((EntityNPCInterface)null, container);
      this.container = container;
      this.ySize = 230;
      this.closeOnEsc = true;
      container.tile.trader1 = this.player;
      container.tile.trader2 = null;
      this.title = "";
   }

   public void initGui() {
      super.initGui();
      GuiNpcLabel label = null;
      if(this.container.tile.trader2 == null) {
         this.addLabel(label = new GuiNpcLabel(0, "trader.waiting", this.guiLeft + 53, this.guiTop + 107));
      } else if(this.container.state != 0 && this.container.state != 1) {
         if(this.container.state == 3) {
            this.addLabel(label = new GuiNpcLabel(0, "trader.complete", this.guiLeft + 53, this.guiTop + 107));
         } else if(this.container.state == 4) {
            this.addLabel(label = new GuiNpcLabel(0, "trader.cancelled", this.guiLeft + 53, this.guiTop + 107));
         }
      } else {
         this.addButton(new GuiNpcButton(0, this.guiLeft + 53, this.guiTop + 102, 70, 20, "trader.trade"));
      }

      if(label != null) {
         label.center(70);
      }

   }

   public void actionPerformed(GuiButton guibutton) {
      if(guibutton.id == 0) {
         NoppesUtilPlayer.sendData(EnumPlayerPacket.TradeAccept, new Object[0]);
      }

   }

   public void save() {}

   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      this.drawWorldBackground(0);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc = Minecraft.getMinecraft();
      this.mc.renderEngine.bindTexture(this.resource);
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
      GuiInventory.drawEntityOnScreen(this.guiLeft + 34, this.guiTop + 94, 30, (float)(this.guiLeft + 33 - i), (float)(this.guiTop + 52 - j), this.mc.thePlayer);
      if(this.container.state != 3 && this.container.state != 4) {
         if(this.container.tile.trader2 != null) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 142, this.guiTop + 94, 30, (float)(this.guiLeft + 141 - i), (float)(this.guiTop + 52 - j), this.container.tile.trader2);
         }

         ItemStack hover = null;

         for(int k = 0; k < 9; ++k) {
            ItemStack itemstack = (ItemStack)this.items.get(Integer.valueOf(k));
            if(itemstack != null) {
               int x = this.guiLeft + 8 + k % 5 * 18;
               int y = this.guiTop + 8 + k / 5 * 18;
               GlStateManager.enableRescaleNormal();
               RenderHelper.enableGUIStandardItemLighting();
               this.itemRender.renderItemAndEffectIntoGUI(itemstack, x, y);
               this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, x, y);
               RenderHelper.disableStandardItemLighting();
               GlStateManager.disableRescaleNormal();
               if(this.isPointInRegion(x - this.guiLeft, y - this.guiTop, 16, 16, i, j)) {
                  hover = itemstack;
               }
            }
         }

         if(hover != null) {
            this.renderToolTip(hover, i, j);
         }

         if(this.container.state == 1) {
            this.drawSquare(this.guiLeft + 6, this.guiTop + 6, 164, 20, 2);
         }

         if(this.container.state == 2) {
            this.drawSquare(this.guiLeft + 6, this.guiTop + 123, 164, 20, 2);
         }

         super.drawGuiContainerBackgroundLayer(f, i, j);
      } else {
         super.drawGuiContainerBackgroundLayer(f, i, j);
      }
   }

   private void drawSquare(int x, int y, int width, int height, int thick) {
      drawRect(x, y, x + width, y + thick, -16711936);
      drawRect(x, y, x + thick, y + height, -16711936);
      drawRect(x, y + height, x + width, y + height - thick, -16711936);
      drawRect(x + width, y, x + width - thick, y + height, -16711936);
   }

   public void setGuiData(NBTTagCompound compound) {
      if(compound.hasKey("Player")) {
         this.items = new HashMap();
         String id = compound.getString("Player");
         if(id.isEmpty()) {
            this.container.tile.trader2 = null;
         } else {
            this.container.tile.trader2 = this.player.worldObj.getPlayerEntityByUUID(UUID.fromString(id));
         }
      } else if(compound.hasKey("State")) {
         this.container.state = compound.getInteger("State");
      } else {
         this.items = ContainerTradingBlock.CompToItem(compound);
      }

      this.initGui();
   }
}
