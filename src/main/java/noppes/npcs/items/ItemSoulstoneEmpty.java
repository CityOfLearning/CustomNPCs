package noppes.npcs.items;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class ItemSoulstoneEmpty extends Item {

   public ItemSoulstoneEmpty() {
      this.setMaxStackSize(64);
   }

   public Item setUnlocalizedName(String name) {
      super.setUnlocalizedName(name);
      GameRegistry.registerItem(this, name);
      CustomNpcs.proxy.registerItem(this, name, 0);
      return this;
   }

   public boolean store(EntityLivingBase entity, ItemStack stack, EntityPlayer player) {
      if(this.hasPermission(entity, player) && !(entity instanceof EntityPlayer)) {
         ItemStack stone = new ItemStack(CustomItems.soulstoneFull);
         NBTTagCompound compound = new NBTTagCompound();
         if(!entity.writeToNBTOptional(compound)) {
            return false;
         } else {
            ServerCloneController.Instance.cleanTags(compound);
            stone.setTagInfo("Entity", compound);
            String name = EntityList.getEntityString(entity);
            if(name == null) {
               name = "generic";
            }

            stone.setTagInfo("Name", new NBTTagString("entity." + name + ".name"));
            if(entity instanceof EntityNPCInterface) {
               EntityNPCInterface npc = (EntityNPCInterface)entity;
               stone.setTagInfo("DisplayName", new NBTTagString(entity.getName()));
               if(npc.advanced.role == 6) {
                  RoleCompanion role = (RoleCompanion)npc.roleInterface;
                  stone.setTagInfo("ExtraText", new NBTTagString("companion.stage,: ," + role.stage.name));
               }
            } else if(entity instanceof EntityLiving && ((EntityLiving)entity).hasCustomName()) {
               stone.setTagInfo("DisplayName", new NBTTagString(((EntityLiving)entity).getCustomNameTag()));
            }

            NoppesUtilServer.GivePlayerItem(player, player, stone);
            if(!player.capabilities.isCreativeMode) {
               stack.splitStack(1);
               if(stack.stackSize <= 0) {
                  player.destroyCurrentEquippedItem();
               }
            }

            entity.isDead = true;
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean hasPermission(EntityLivingBase entity, EntityPlayer player) {
      if(NoppesUtilServer.isOp(player)) {
         return true;
      } else if(CustomNpcsPermissions.enabled() && CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.SOULSTONE_ALL)) {
         return true;
      } else if(entity instanceof EntityNPCInterface) {
         EntityNPCInterface npc = (EntityNPCInterface)entity;
         if(npc.advanced.role == 6) {
            RoleCompanion role = (RoleCompanion)npc.roleInterface;
            if(role.getOwner() == player) {
               return true;
            }
         }

         if(npc.advanced.role == 2) {
            RoleFollower role1 = (RoleFollower)npc.roleInterface;
            if(role1.getOwner() == player) {
               return !role1.refuseSoulStone;
            }
         }

         return false;
      } else {
         return entity instanceof EntityAnimal?CustomNpcs.SoulStoneAnimals:false;
      }
   }
}
