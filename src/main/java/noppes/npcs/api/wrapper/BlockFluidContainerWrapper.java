//

//

package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import noppes.npcs.api.block.IBlockFluidContainer;

public class BlockFluidContainerWrapper extends BlockWrapper implements IBlockFluidContainer {
	private BlockFluidBase block;

	public BlockFluidContainerWrapper(World world, Block block, BlockPos pos) {
		super(world, block, pos);
		this.block = (BlockFluidBase) block;
	}

	@Override
	public String getFluidName() {
		return block.getFluid().getName();
	}

	@Override
	public float getFluidPercentage() {
		return block.getFilledPercentage(world.getMCWorld(), pos);
	}

	@Override
	public float getFluidValue() {
		return block.getQuantaValue(world.getMCWorld(), pos);
	}

	@Override
	public float getFuildDensity() {
		return BlockFluidBase.getDensity(world.getMCWorld(), pos);
	}

	@Override
	public float getFuildTemperature() {
		return BlockFluidBase.getTemperature(world.getMCWorld(), pos);
	}
}
