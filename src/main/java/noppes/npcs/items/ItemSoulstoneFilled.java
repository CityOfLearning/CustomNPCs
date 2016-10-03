package noppes.npcs.items;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class ItemSoulstoneFilled extends Item {

   public ItemSoulstoneFilled() {
      this.setMaxStackSize(1);
   }

   public Item setUnlocalizedName(String name) {
      super.setUnlocalizedName(name);
      GameRegistry.registerItem(this, name);
      CustomNpcs.proxy.registerItem(this, name, 0);
      return this;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bo) {
      NBTTagCompound compound = stack.getTagCompound();
      if(compound != null && compound.hasKey("Entity", 10)) {
         String name = StatCollector.translateToLocal(compound.getString("Name"));
         if(compound.hasKey("DisplayName")) {
            name = compound.getString("DisplayName") + " (" + name + ")";
         }

         list.add(EnumChatFormatting.BLUE + name);
         if(stack.getTagCompound().hasKey("ExtraText")) {
            String text = "";
            String[] split = compound.getString("ExtraText").split(",");
            String[] var9 = split;
            int var10 = split.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               String s = var9[var11];
               text = text + StatCollector.translateToLocal(s);
            }

            list.add(text);
         }

      } else {
         list.add(EnumChatFormatting.RED + "Error");
      }
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
      if(world.isRemote) {
         return true;
      } else if(Spawn(player, stack, world, pos) == null) {
         return false;
      } else {
         if(!player.capabilities.isCreativeMode) {
            stack.splitStack(1);
         }

         return true;
      }
   }

   public static Entity Spawn(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
      if(world.isRemote) {
         return null;
      } else if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("Entity", 10)) {
         NBTTagCompound compound = stack.getTagCompound().getCompoundTag("Entity");
         Entity entity = EntityList.createEntityFromNBT(compound, world);
         if(entity == null) {
            return null;
         } else {
            entity.setPosition((double)pos.getX() + 0.5D, (double)((float)(pos.getY() + 1) + 0.2F), (double)pos.getZ() + 0.5D);
            if(entity instanceof EntityNPCInterface) {
               EntityNPCInterface npc = (EntityNPCInterface)entity;
               npc.ai.setStartPos(pos);
               npc.setHealth(npc.getMaxHealth());
               npc.setPosition((double)((float)pos.getX() + 0.5F), npc.getStartYPos(), (double)((float)pos.getZ() + 0.5F));
               if(npc.advanced.role == 6 && player != null) {
                  PlayerData data = PlayerDataController.instance.getPlayerData(player);
                  if(data.hasCompanion()) {
                     return null;
                  }

                  ((RoleCompanion)npc.roleInterface).setOwner(player);
                  data.setCompanion(npc);
               }

               if(npc.advanced.role == 2 && player != null) {
                  ((RoleFollower)npc.roleInterface).setOwner(player);
               }
            }

            world.spawnEntityInWorld(entity);
            return entity;
         }
      } else {
         return null;
      }
   }
}
