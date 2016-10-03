package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.items.ItemNpcInterface;

public class ItemMusic extends ItemNpcInterface {

   private boolean shouldRotate = false;


   public ItemMusic() {
      this.setCreativeTab(CustomItems.tabMisc);
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
      if(par2World.isRemote) {
         return par1ItemStack;
      } else {
         int note = par2World.rand.nextInt(24);
         float var7 = (float)Math.pow(2.0D, (double)(note - 12) / 12.0D);
         String var8 = "harp";
         par2World.playSoundEffect(player.posX, player.posY, player.posZ, "note." + var8, 3.0F, var7);
         par2World.spawnParticle(EnumParticleTypes.NOTE, player.posY, player.posY + 1.2D, player.posY, (double)note / 24.0D, 0.0D, 0.0D, new int[0]);
         return par1ItemStack;
      }
   }

   public Item setRotated() {
      this.shouldRotate = true;
      return this;
   }

   public boolean shouldRotateAroundWhenRendering() {
      return this.shouldRotate;
   }
}
