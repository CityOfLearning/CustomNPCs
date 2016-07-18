//

//

package noppes.npcs.blocks.tiles;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileBook extends TileColorable {
	public ItemStack book;

	public TileBook() {
		book = new ItemStack(Items.writable_book);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		book = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Items"));
		if (book == null) {
			book = new ItemStack(Items.writable_book);
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Items", book.writeToNBT(new NBTTagCompound()));
	}
}
