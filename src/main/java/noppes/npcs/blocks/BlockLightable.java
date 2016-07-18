//

//

package noppes.npcs.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.blocks.tiles.TileColorable;

public abstract class BlockLightable extends BlockRotated {
	public static final PropertyBool LIT;

	static {
		LIT = PropertyBool.create("lit");
	}

	protected BlockLightable(final Block block, final boolean lit) {
		super(block);
		setDefaultState(blockState.getBaseState().withProperty(BlockLightable.LIT, lit));
		if (lit) {
			setLightLevel(1.0f);
		}
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockRotated.DAMAGE, BlockLightable.LIT });
	}

	@Override
	protected ItemStack createStackedBlock(final IBlockState state) {
		return new ItemStack(litBlock());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(final World world, final BlockPos pos) {
		return Item.getItemFromBlock(litBlock());
	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		return Item.getItemFromBlock(litBlock());
	}

	public abstract Block litBlock();

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final TileEntity tile = world.getTileEntity(pos);
		if (litBlock() == this) {
			world.setBlockState(pos, unlitBlock().getDefaultState().withProperty(BlockRotated.DAMAGE,
					state.getValue(BlockRotated.DAMAGE)), 2);
		} else {
			world.setBlockState(pos,
					litBlock().getDefaultState().withProperty(BlockRotated.DAMAGE, state.getValue(BlockRotated.DAMAGE)),
					2);
		}
		tile.validate();
		world.setTileEntity(pos, tile);
		return true;
	}

	public void onPostBlockPlaced(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final ItemStack stack, final EnumFacing facing) {
		final TileColorable tile = (TileColorable) world.getTileEntity(pos);
		if (facing == EnumFacing.UP) {
			tile.color = 0;
		} else if (facing == EnumFacing.DOWN) {
			tile.color = 1;
		} else {
			tile.color = 2;
			if (facing == EnumFacing.NORTH) {
				tile.rotation = 0;
			} else if (facing == EnumFacing.EAST) {
				tile.rotation = 2;
			} else if (facing == EnumFacing.SOUTH) {
				tile.rotation = 4;
			} else if (facing == EnumFacing.WEST) {
				tile.rotation = 6;
			}
		}
	}

	public abstract Block unlitBlock();
}
