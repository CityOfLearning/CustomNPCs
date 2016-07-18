//

//

package noppes.npcs.api.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IBlock {
	boolean canStoreData();

	int getContainerSize();

	IItemStack getContainerSlot(final int p0);

	Block getMCBlock();

	TileEntity getMCTileEntity();

	String getName();

	IData getStoreddata();

	IData getTempdata();

	IWorld getWorld();

	int getX();

	int getY();

	int getZ();

	boolean isContainer();

	boolean isRemoved();

	void remove();

	IBlock setBlock(final IBlock p0);

	IBlock setBlock(final String p0);

	void setContainerSlot(final int p0, final IItemStack p1);
}
