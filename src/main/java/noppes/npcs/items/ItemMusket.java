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

public class ItemMusket extends ItemNpcInterface {

   public ItemMusket(int par1) {
      super(par1);
      this.setMaxDamage(129);
      this.setCreativeTab(CustomItems.tabWeapon);
   }

   public void onPlayerStoppedUsing(ItemStack stack, World par2World, EntityPlayer player, int count) {
      if(!player.worldObj.isRemote && stack.hasTagCompound()) {
         NBTTagCompound compound = stack.getTagCompound();
         if((compound.getBoolean("IsLoaded2") || player.capabilities.isCreativeMode) && CustomNpcs.GunsEnabled) {
            if(compound.getBoolean("Reloading2") && !player.capabilities.isCreativeMode) {
               compound.setBoolean("Reloading2", false);
            } else {
               stack.damageItem(1, player);
               EntityProjectile projectile = new EntityProjectile(player.worldObj, player, new ItemStack(CustomItems.bulletBlack, 1, 0), false);
               projectile.damage = 16.0F;
               projectile.setSpeed(50);
               projectile.setParticleEffect(1);
               projectile.shoot(2.0F);
               if(!player.capabilities.isCreativeMode) {
                  this.consumeItem(player, CustomItems.bulletBlack);
               }

               player.worldObj.playSoundAtEntity(player, "random.explode", 0.9F, itemRand.nextFloat() * 0.3F + 1.8F);
               player.worldObj.playSoundAtEntity(player, "ambient.weather.thunder", 2.0F, itemRand.nextFloat() * 0.3F + 1.8F);
               player.worldObj.spawnEntityInWorld(projectile);
               compound.setBoolean("IsLoaded2", false);
            }
         } else {
            player.worldObj.playSoundAtEntity(player, "customnpcs:gun.empty", 1.0F, 1.0F);
         }
      }
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
      if(!player.worldObj.isRemote) {
         int ticks = this.getMaxItemUseDuration(stack) - count;
         if(!player.capabilities.isCreativeMode && stack.hasTagCompound() && stack.getTagCompound().getBoolean("Reloading2") && this.hasItem(player, CustomItems.bulletBlack) && ticks == 60) {
            player.worldObj.playSoundAtEntity(player, "customnpcs:gun.ak47.load", 1.0F, 1.0F);
            stack.setTagInfo("IsLoaded2", new NBTTagByte((byte)1));
         }

      }
   }

   public void renderSpecial() {
      GlStateManager.rotate(-6.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.scale(0.7F, 0.7F, 0.7F);
      GlStateManager.translate(0.4F, 0.0F, 0.2F);
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      NBTTagCompound compound = stack.getTagCompound();
      if(compound == null) {
         stack.setTagCompound(compound = new NBTTagCompound());
      }

      if(!player.capabilities.isCreativeMode && this.hasItem(player, CustomItems.bulletBlack) && !compound.getBoolean("IsLoaded2")) {
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
