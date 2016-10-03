package noppes.npcs.api.block;

import noppes.npcs.api.block.IBlock;

public interface IBlockFluidContainer extends IBlock {

   float getFluidPercentage();

   float getFuildDensity();

   float getFuildTemperature();

   float getFluidValue();

   String getFluidName();
}
