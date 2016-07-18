//

//

package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemPlaceholder extends ItemBlock {
	public ItemPlaceholder(final Block p_i45328_1_) {
		super(p_i45328_1_);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(final int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(final ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + "_" + par1ItemStack.getItemDamage();
	}
}
