//

//

package noppes.npcs.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;

public class ItemWand extends ItemNpcInterface {
	public ItemWand(final int par1) {
		super(par1);
		setCreativeTab(CustomItems.tabBlocks);
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public void renderSpecial() {
		GlStateManager.scale(0.54f, 0.54f, 0.54f);
		GlStateManager.translate(0.1f, 0.5f, 0.1f);
	}
}
