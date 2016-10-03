package noppes.npcs.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class ItemNpcScripter extends Item implements IPermission {

   public ItemNpcScripter() {
      this.maxStackSize = 1;
      this.setCreativeTab(CustomItems.tab);
   }

   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      return 9127187;
   }

   public Item setUnlocalizedName(String name) {
      GameRegistry.registerItem(this, name);
      CustomNpcs.proxy.registerItem(this, name, 0);
      return super.setUnlocalizedName(name);
   }

   public boolean isAllowed(EnumPacketServer e) {
      return e == EnumPacketServer.ScriptDataGet || e == EnumPacketServer.ScriptDataSave || e == EnumPacketServer.ScriptBlockDataSave || e == EnumPacketServer.ScriptDoorDataSave;
   }
}
