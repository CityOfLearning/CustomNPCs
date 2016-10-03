package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.items.ItemNpcInterface;

public class ItemCrossbow extends ItemNpcInterface {

   public ItemCrossbow(int par1) {
      super(par1);
      this.setMaxDamage(129);
      this.setCreativeTab(CustomItems.tabWeapon);
   }

   public void onPlayerStoppedUsing(ItemStack stack, World par2World, EntityPlayer player, int count) {
      if(!player.worldObj.isRemote && (stack.hasTagCompound() || player.capabilities.isCreativeMode)) {
         NBTTagCompound compound = stack.getTagCompound();
         if(compound == null) {
            stack.setTagCompound(compound = new NBTTagCompound());
         }

         if(compound.getInteger("IsLoaded") == 1 || player.capabilities.isCreativeMode) {
            if(compound.getInteger("Reloading") == 1 && !player.capabilities.isCreativeMode) {
               compound.setInteger("Reloading", 0);
               return;
            }

            stack.damageItem(1, player);
            EntityProjectile projectile = new EntityProjectile(player.worldObj, player, new ItemStack(Items.arrow, 1, 0), false);
            projectile.damage = 10.0F;
            projectile.setSpeed(20);
            projectile.setHasGravity(true);
            projectile.shoot(2.0F);
            if(!player.capabilities.isCreativeMode) {
               this.consumeItem(player, CustomItems.crossbowBolt);
            }

            player.worldObj.playSoundAtEntity(player, "random.bow", 0.9F, itemRand.nextFloat() * 0.3F + 0.8F);
            player.worldObj.spawnEntityInWorld(projectile);
            compound.setInteger("IsLoaded", 0);
         }

      }
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
      if(!player.worldObj.isRemote) {
         int ticks = this.getMaxItemUseDuration(stack) - count;
         if(!player.capabilities.isCreativeMode && stack.hasTagCompound()) {
            if(stack.getTagCompound().getInteger("Reloading") == 1 && this.hasItem(player, CustomItems.crossbowBolt) && ticks == 20) {
               player.worldObj.playSoundAtEntity(player, "random.click", 1.0F, 1.0F);
               stack.getTagCompound().setInteger("IsLoaded", 1);
            }

         }
      }
   }

   public void renderSpecial() {
      GlStateManager.rotate(96.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(-10.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.scale(0.8F, 0.8F, 0.8F);
      GlStateManager.translate(0.5F, -0.7F, -0.4F);
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      NBTTagCompound compound = stack.getTagCompound();
      if(compound == null) {
         compound = new NBTTagCompound();
      }

      if(!player.capabilities.isCreativeMode && this.hasItem(player, CustomItems.crossbowBolt) && compound.getInteger("IsLoaded") == 0) {
         compound.setInteger("Reloading", 1);
         stack.setTagCompound(compound);
      }

      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return stack.getTagCompound() != null && stack.getTagCompound().getInteger("Reloading") != 0?EnumAction.BLOCK:EnumAction.BOW;
   }
}
