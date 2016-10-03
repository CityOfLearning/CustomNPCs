package noppes.npcs.api.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IBlock {

   int getX();

   int getY();

   int getZ();

   String getName();

   void remove();

   boolean isRemoved();

   IBlock setBlock(String var1);

   IBlock setBlock(IBlock var1);

   boolean isContainer();

   int getContainerSize();

   IItemStack getContainerSlot(int var1);

   void setContainerSlot(int var1, IItemStack var2);

   boolean canStoreData();

   IData getTempdata();

   IData getStoreddata();

   IWorld getWorld();

   Block getMCBlock();

   TileEntity getMCTileEntity();
}
