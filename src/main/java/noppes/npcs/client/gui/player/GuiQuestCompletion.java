package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.Quest;

public class GuiQuestCompletion extends GuiNPCInterface implements ITopButtonListener {

   private Quest quest;
   private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/smallbg.png");


   public GuiQuestCompletion(Quest quest) {
      this.xSize = 176;
      this.ySize = 222;
      this.quest = quest;
      this.drawDefaultBackground = false;
      this.title = "";
   }

   public void initGui() {
      super.initGui();
      String questTitle = this.quest.title;
      int left = (this.xSize - this.fontRendererObj.getStringWidth(questTitle)) / 2;
      this.addLabel(new GuiNpcLabel(0, questTitle, this.guiLeft + left, this.guiTop + 4));
      this.addButton(new GuiNpcButton(0, this.guiLeft + 38, this.guiTop + this.ySize - 24, 100, 20, StatCollector.translateToLocal("quest.complete")));
   }

   public void drawScreen(int i, int j, float f) {
      this.drawDefaultBackground();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture(this.resource);
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
      this.drawHorizontalLine(this.guiLeft + 4, this.guiLeft + 170, this.guiTop + 13, -16777216 + CustomNpcResourceListener.DefaultTextColor);
      this.drawQuestText();
      super.drawScreen(i, j, f);
   }

   private void drawQuestText() {
      int xoffset = this.guiLeft + 4;
      TextBlockClient block = new TextBlockClient(this.quest.completeText, 172, true, new Object[]{this.player});
      int yoffset = this.guiTop + 20;

      for(int i = 0; i < block.lines.size(); ++i) {
         String text = ((IChatComponent)block.lines.get(i)).getFormattedText();
         this.fontRendererObj.drawString(text, this.guiLeft + 4, this.guiTop + 16 + i * this.fontRendererObj.FONT_HEIGHT, CustomNpcResourceListener.DefaultTextColor);
      }

   }

   protected void actionPerformed(GuiButton guibutton) {
      if(guibutton.id == 0) {
         NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, new Object[]{Integer.valueOf(this.quest.id)});
         this.close();
      }

   }

   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {}

   public void keyTyped(char c, int i) {
      if(i == 1 || this.isInventoryKey(i)) {
         this.close();
      }

   }

   public void save() {
      NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, new Object[]{Integer.valueOf(this.quest.id)});
   }
}
