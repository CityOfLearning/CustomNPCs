package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.items.ItemNpcInterface;

public class ItemMachineGun extends ItemNpcInterface {

   public ItemMachineGun(int par1) {
      super(par1);
      this.setMaxDamage(80);
      this.setCreativeTab(CustomItems.tabWeapon);
   }

   public void onPlayerStoppedUsing(ItemStack stack, World par2World, EntityPlayer player, int count) {
      if(!player.capabilities.isCreativeMode) {
         NBTTagCompound compound = stack.getTagCompound();
         if(compound == null) {
            stack.setTagCompound(compound = new NBTTagCompound());
         }

         int ticks = this.getMaxItemUseDuration(stack) - count;
         int shotsleft = compound.getInteger("ShotsLeft") - ticks / 6;
         if(compound.getBoolean("Reloading2")) {
            shotsleft = ticks / 5;
            if(ticks > 40) {
               shotsleft = 8;
            }

            if(shotsleft > 1) {
               compound.setInteger("ShotsLeft", shotsleft);
               compound.setBoolean("Reloading2", false);
            }
         } else if(shotsleft <= 0) {
            compound.setBoolean("Reloading2", true);
            stack.damageItem(1, player);
         } else {
            compound.setInteger("ShotsLeft", shotsleft);
         }

      }
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
      if(!player.worldObj.isRemote) {
         int ticks = this.getMaxItemUseDuration(stack) - count;
         if(ticks % 6 == 0) {
            NBTTagCompound compound = stack.getTagCompound();
            if(compound == null) {
               stack.setTagCompound(compound = new NBTTagCompound());
            }

            int shotsleft = compound.getInteger("ShotsLeft") - ticks / 6;
            if(player.capabilities.isCreativeMode && CustomNpcs.GunsEnabled) {
               compound.removeTag("Reloading2");
            } else {
               if(compound.getBoolean("Reloading2") && this.hasItem(player, CustomItems.bulletBlack)) {
                  if(ticks > 0 && ticks <= 24) {
                     player.worldObj.playSoundAtEntity(player, "customnpcs:gun.ak47.load", 1.0F, 1.0F);
                  }

                  return;
               }

               if(shotsleft <= 0 || !this.hasItem(player, CustomItems.bulletBlack) || !CustomNpcs.GunsEnabled) {
                  player.worldObj.playSoundAtEntity(player, "customnpcs:gun.empty", 1.0F, 1.0F);
                  return;
               }
            }

            EntityProjectile projectile = new EntityProjectile(player.worldObj, player, new ItemStack(CustomItems.bulletBlack, 1, 0), false);
            projectile.damage = 4.0F;
            projectile.setSpeed(40);
            projectile.shoot(2.0F);
            if(!player.capabilities.isCreativeMode) {
               this.consumeItem(player, CustomItems.bulletBlack);
            }

            player.worldObj.playSoundAtEntity(player, "customnpcs:gun.pistol.shot", 0.9F, itemRand.nextFloat() * 0.3F + 0.8F);
            player.worldObj.spawnEntityInWorld(projectile);
         }
      }
   }

   public void renderSpecial() {
      GlStateManager.rotate(-6.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.scale(0.8F, 0.7F, 0.7F);
      GlStateManager.translate(0.2F, 0.0F, 0.2F);
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(!player.capabilities.isCreativeMode && !this.hasItem(player, CustomItems.bulletBlack)) {
         stack.setTagInfo("Reloading2", new NBTTagByte((byte)1));
      }

      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return stack.hasTagCompound() && stack.getTagCompound().getBoolean("Reloading2")?EnumAction.BLOCK:EnumAction.BOW;
   }
}
