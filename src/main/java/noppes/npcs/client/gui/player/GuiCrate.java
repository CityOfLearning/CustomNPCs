package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.containers.ContainerCrate;

@SideOnly(Side.CLIENT)
public class GuiCrate extends GuiContainer {

   private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
   private IInventory upperChestInventory;
   private IInventory lowerChestInventory;
   private int inventoryRows;


   public GuiCrate(ContainerCrate container) {
      super(container);
      this.upperChestInventory = container.upperChestInventory;
      this.lowerChestInventory = container.lowerChestInventory;
      this.allowUserInput = false;
      short short1 = 222;
      int i = short1 - 108;
      this.inventoryRows = this.lowerChestInventory.getSizeInventory() / 9;
      this.ySize = i + this.inventoryRows * 18;
   }

   protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
      this.fontRendererObj.drawString(this.lowerChestInventory.hasCustomName()?this.lowerChestInventory.getName():I18n.format(this.lowerChestInventory.getName(), new Object[0]), 8, 6, CustomNpcResourceListener.DefaultTextColor);
      this.fontRendererObj.drawString(this.upperChestInventory.hasCustomName()?this.upperChestInventory.getName():I18n.format(this.upperChestInventory.getName(), new Object[0]), 8, this.ySize - 96 + 2, CustomNpcResourceListener.DefaultTextColor);
   }

   protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
      int k = (this.width - this.xSize) / 2;
      int l = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
      this.drawTexturedModalRect(k, l + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
   }

}
