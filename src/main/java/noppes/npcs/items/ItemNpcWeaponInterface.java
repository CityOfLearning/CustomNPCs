package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.items.ItemRenderInterface;

public class ItemNpcWeaponInterface extends ItemSword implements ItemRenderInterface {

   public ItemNpcWeaponInterface(int par1, ToolMaterial material) {
      this(material);
   }

   public ItemNpcWeaponInterface(ToolMaterial material) {
      super(material);
      this.setCreativeTab(CustomItems.tab);
      this.setCreativeTab(CustomItems.tabWeapon);
   }

   public void renderSpecial() {
      GlStateManager.scale(0.66F, 0.66F, 0.66F);
      GlStateManager.translate(0.16F, 0.26F, 0.06F);
   }

   public Item setUnlocalizedName(String name) {
      GameRegistry.registerItem(this, name);
      CustomNpcs.proxy.registerItem(this, name, 0);
      return super.setUnlocalizedName(name);
   }
}
