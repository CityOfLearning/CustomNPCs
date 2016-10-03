package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.items.ItemNpcInterface;

public class ItemSlingshot extends ItemNpcInterface {

   public ItemSlingshot(int par1) {
      super(par1);
      this.maxStackSize = 1;
      this.setMaxDamage(384);
      this.setCreativeTab(CustomItems.tabWeapon);
   }

   public void onPlayerStoppedUsing(ItemStack par1ItemStack, World worldObj, EntityPlayer player, int par4) {
      if(!worldObj.isRemote) {
         int ticks = this.getMaxItemUseDuration(par1ItemStack) - par4;
         if(ticks >= 6) {
            if(player.capabilities.isCreativeMode || this.consumeItem(player, Item.getItemFromBlock(Blocks.cobblestone))) {
               par1ItemStack.damageItem(1, player);
               EntityProjectile projectile = new EntityProjectile(worldObj, player, new ItemStack(Blocks.cobblestone), false);
               projectile.damage = 4.0F;
               projectile.punch = 1;
               projectile.setRotating(true);
               if(ticks > 24) {
                  projectile.setParticleEffect(8);
                  projectile.punch = 2;
               }

               projectile.setHasGravity(true);
               projectile.setSpeed(14);
               projectile.shoot(1.0F);
               worldObj.playSoundAtEntity(player, "random.bow", 1.0F, itemRand.nextFloat() * 0.3F + 0.8F);
               worldObj.spawnEntityInWorld(projectile);
            }
         }
      }
   }

   public void renderSpecial() {
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      GlStateManager.translate(0.0F, 0.5F, 0.0F);
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
      return par1ItemStack;
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.BOW;
   }
}
