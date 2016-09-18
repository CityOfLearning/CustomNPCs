
package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMusicViolin extends ItemMusic {
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	@Override
	public void renderSpecial() {
		GlStateManager.scale(0.66f, 0.66f, 0.66f);
		GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.2f, -0.9f, -0.52f);
	}
}
