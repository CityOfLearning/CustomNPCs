//

//

package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNpcColored extends ItemColored {
	private Block coloredBlock;

	public ItemNpcColored(Block p_i45332_1_) {
		super(p_i45332_1_, true);
		coloredBlock = p_i45332_1_;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		return coloredBlock.getRenderColor(coloredBlock.getStateFromMeta(stack.getMetadata()));
	}
}
