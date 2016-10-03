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
	public static final PropertyBool LIT = PropertyBool.create("lit");

	protected BlockLightable(Block block, boolean lit) {
		super(block);
		setDefaultState(blockState.getBaseState().withProperty(LIT, Boolean.valueOf(lit)));
		if (lit) {
			setLightLevel(1.0F);
		}
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { DAMAGE, LIT });
	}

	@Override
	protected ItemStack createStackedBlock(IBlockState state) {
		return new ItemStack(litBlock());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos) {
		return Item.getItemFromBlock(litBlock());
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(litBlock());
	}

	public abstract Block litBlock();

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if (litBlock() == this) {
			world.setBlockState(pos, unlitBlock().getDefaultState().withProperty(DAMAGE, state.getValue(DAMAGE)), 2);
		} else {
			world.setBlockState(pos, litBlock().getDefaultState().withProperty(DAMAGE, state.getValue(DAMAGE)), 2);
		}
		tile.validate();
		world.setTileEntity(pos, tile);
		return true;
	}

	public void onPostBlockPlaced(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack stack,
			EnumFacing facing) {
		TileColorable tile = (TileColorable) world.getTileEntity(pos);
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
