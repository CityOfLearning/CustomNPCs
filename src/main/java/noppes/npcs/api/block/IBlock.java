
package noppes.npcs.api.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IBlock {
	boolean canStoreData();

	int getContainerSize();

	IItemStack getContainerSlot(int p0);

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

	IBlock setBlock(IBlock p0);

	IBlock setBlock(String p0);

	void setContainerSlot(int p0, IItemStack p1);
}
