package tconstruct.client.tabs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tconstruct.client.tabs.AbstractTab;
import tconstruct.client.tabs.InventoryTabVanilla;

public class TabRegistry {

   private static ArrayList tabList = new ArrayList();
   private static Minecraft mc = FMLClientHandler.instance().getClient();


   public static void registerTab(AbstractTab tab) {
      tabList.add(tab);
   }

   public static ArrayList getTabList() {
      return tabList;
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void guiPostInit(Post event) {
      if(event.gui instanceof GuiInventory) {
         short xSize = 176;
         short ySize = 166;
         int guiLeft = (event.gui.width - xSize) / 2;
         int guiTop = (event.gui.height - ySize) / 2;
         guiLeft += getPotionOffset();
         updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
         addTabsToList(event.buttonList);
      }

   }

   public static void openInventoryGui() {
      mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
      GuiInventory inventory = new GuiInventory(mc.thePlayer);
      mc.displayGuiScreen(inventory);
   }

   public static void updateTabValues(int cornerX, int cornerY, Class selectedButton) {
      int count = 2;

      for(int i = 0; i < tabList.size(); ++i) {
         AbstractTab t = (AbstractTab)tabList.get(i);
         if(t.shouldAddToList()) {
            t.id = count;
            t.xPosition = cornerX + (count - 2) * 28;
            t.yPosition = cornerY - 28;
            t.enabled = !t.getClass().equals(selectedButton);
            ++count;
         }
      }

   }

   public static void addTabsToList(List buttonList) {
      Iterator var1 = tabList.iterator();

      while(var1.hasNext()) {
         AbstractTab tab = (AbstractTab)var1.next();
         if(tab.shouldAddToList()) {
            buttonList.add(tab);
         }
      }

   }

   public static int getPotionOffset() {
      if(!mc.thePlayer.getActivePotionEffects().isEmpty()) {
         if(!Loader.isModLoaded("NotEnoughItems")) {
            return 60;
         }

         try {
            Class e = Class.forName("codechicken.nei.NEIClientConfig");
            Object hidden = e.getMethod("isHidden", new Class[0]).invoke((Object)null, new Object[0]);
            Object enabled = e.getMethod("isEnabled", new Class[0]).invoke((Object)null, new Object[0]);
            if(hidden != null && hidden instanceof Boolean && enabled != null && enabled instanceof Boolean && (((Boolean)hidden).booleanValue() || !((Boolean)enabled).booleanValue())) {
               return 60;
            }
         } catch (Exception var3) {
            ;
         }
      }

      return 0;
   }

}
