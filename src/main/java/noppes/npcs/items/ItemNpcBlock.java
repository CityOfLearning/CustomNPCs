//

//

package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemNpcBlock extends ItemBlock {
	public String[] names;

	public ItemNpcBlock(final Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedName(final ItemStack par1ItemStack) {
		if ((names != null) && (par1ItemStack.getItemDamage() < names.length)) {
			return names[par1ItemStack.getItemDamage()];
		}
		return block.getUnlocalizedName();
	}
}
