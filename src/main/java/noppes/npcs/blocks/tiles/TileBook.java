package noppes.npcs.blocks.tiles;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileBook extends TileColorable {
	private ItemStack book = new ItemStack(Items.writable_book);

	public ItemStack getBook() {
		return book;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		book = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Items"));
		if (book == null) {
			book = new ItemStack(Items.writable_book);
		}
	}

	public void setBook(ItemStack book) {
		this.book = book;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Items", book.writeToNBT(new NBTTagCompound()));
	}
}
