package noppes.npcs.items;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class ItemNpcMovingPath extends Item implements IPermission {

   public ItemNpcMovingPath() {
      this.maxStackSize = 1;
      this.setCreativeTab(CustomItems.tab);
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if(!par2World.isRemote) {
         CustomNpcsPermissions var10000 = CustomNpcsPermissions.Instance;
         if(CustomNpcsPermissions.hasPermission(par3EntityPlayer, CustomNpcsPermissions.TOOL_MOUNTER)) {
            EntityNPCInterface npc = this.getNpc(par1ItemStack, par2World);
            if(npc != null) {
               NoppesUtilServer.sendOpenGui(par3EntityPlayer, EnumGuiType.MovingPath, npc);
            }

            return par1ItemStack;
         }
      }

      return par1ItemStack;
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos bpos, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(!world.isRemote) {
         CustomNpcsPermissions var10000 = CustomNpcsPermissions.Instance;
         if(CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.TOOL_MOUNTER)) {
            EntityNPCInterface npc = this.getNpc(stack, world);
            if(npc == null) {
               return true;
            }

            List list = npc.ai.getMovingPath();
            int[] pos = (int[])list.get(list.size() - 1);
            int x = bpos.getX();
            int y = bpos.getY();
            int z = bpos.getZ();
            list.add(new int[]{x, y, z});
            double d3 = (double)(x - pos[0]);
            double d4 = (double)(y - pos[1]);
            double d5 = (double)(z - pos[2]);
            double distance = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
            player.addChatMessage(new ChatComponentText("Added point x:" + x + " y:" + y + " z:" + z + " to npc " + npc.getName()));
            if(distance > (double)CustomNpcs.NpcNavRange) {
               player.addChatMessage(new ChatComponentText("Warning: point is too far away from previous point. Max block walk distance = " + CustomNpcs.NpcNavRange));
            }

            return true;
         }
      }

      return false;
   }

   private EntityNPCInterface getNpc(ItemStack item, World world) {
      if(!world.isRemote && item.getTagCompound() != null) {
         Entity entity = world.getEntityByID(item.getTagCompound().getInteger("NPCID"));
         return entity != null && entity instanceof EntityNPCInterface?(EntityNPCInterface)entity:null;
      } else {
         return null;
      }
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
      return e == EnumPacketServer.MovingPathGet || e == EnumPacketServer.MovingPathSave;
   }
}
