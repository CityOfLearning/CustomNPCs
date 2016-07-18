//

//

package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMusicClarinet extends ItemMusic {
	@Override
	public EnumAction getItemUseAction(final ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World,
			final EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	@Override
	public void renderSpecial() {
		GlStateManager.scale(0.8f, 0.9f, 0.8f);
		GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.translate(0.44f, -0.7f, -0.04f);
	}
}
