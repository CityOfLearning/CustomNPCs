package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class ItemScriptedDoor extends ItemDoor implements IPermission {

   public ItemScriptedDoor(Block block) {
      super(block);
      this.maxStackSize = 1;
      this.setCreativeTab(CustomItems.tab);
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
      boolean res = super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
      if(res && !worldIn.isRemote) {
         BlockPos newPos = pos.up();
         NoppesUtilServer.sendOpenGui(playerIn, EnumGuiType.ScriptDoor, (EntityNPCInterface)null, newPos.getX(), newPos.getY(), newPos.getZ());
         return true;
      } else {
         return res;
      }
   }

   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
      return stack;
   }

   public Item setUnlocalizedName(String name) {
      GameRegistry.registerItem(this, name);
      CustomNpcs.proxy.registerItem(this, name, 0);
      return super.setUnlocalizedName(name);
   }

   public boolean isAllowed(EnumPacketServer e) {
      return e == EnumPacketServer.ScriptDoorDataSave;
   }
}
