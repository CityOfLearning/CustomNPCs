//

//

package noppes.npcs.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileCandle;
import noppes.npcs.blocks.tiles.TileColorable;

public class BlockCandle extends BlockLightable {
	public BlockCandle(final boolean lit) {
		super(Blocks.planks, lit);
		setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 0.5f, 0.7f);
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileCandle();
	}

	@Override
	public Block litBlock() {
		return CustomItems.candle;
	}

	@Override
	public int maxRotation() {
		return 8;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World world, final BlockPos pos, final IBlockState state, final Random random) {
		if (this == unlitBlock()) {
			return;
		}
		final TileCandle tile = (TileCandle) world.getTileEntity(pos);
		final int x = pos.getX();
		final int y = pos.getY();
		final int z = pos.getZ();
		if (tile.color == 1) {
			if ((tile.rotation % 2) == 0) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.5f, y + 0.66f, z + 0.13f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.5f, y + 0.65f, z + 0.13f, 0.0, 0.0, 0.0, new int[0]);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.5f, y + 0.66f, z + 0.87f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.5f, y + 0.65f, z + 0.87f, 0.0, 0.0, 0.0, new int[0]);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.13f, y + 0.66f, z + 0.5f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.13f, y + 0.65f, z + 0.5f, 0.0, 0.0, 0.0, new int[0]);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.87f, y + 0.66f, z + 0.5f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.87f, y + 0.65f, z + 0.5f, 0.0, 0.0, 0.0, new int[0]);
			} else {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.24f, y + 0.66f, z + 0.24f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.24f, y + 0.65f, z + 0.24f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.76f, y + 0.66f, z + 0.76f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.76f, y + 0.65f, z + 0.76f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.24f, y + 0.66f, z + 0.76f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.24f, y + 0.65f, z + 0.76f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.76f, y + 0.66f, z + 0.24f, 0.0, 0.0, 0.0,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.76f, y + 0.65f, z + 0.24f, 0.0, 0.0, 0.0,
						new int[0]);
			}
		} else {
			float xOffset = 0.5f;
			float yOffset = 0.45f;
			float zOffset = 0.5f;
			if (tile.color == 2) {
				yOffset = 1.05f;
				if (tile.rotation == 0) {
					zOffset += 0.12f;
				}
				if (tile.rotation == 4) {
					zOffset -= 0.12f;
				}
				if (tile.rotation == 6) {
					xOffset += 0.12f;
				}
				if (tile.rotation == 2) {
					xOffset -= 0.12f;
				}
			}
			final double d0 = x + xOffset;
			final double d2 = y + yOffset;
			final double d3 = z + zOffset;
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
			world.spawnParticle(EnumParticleTypes.FLAME, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {
		final TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		final TileColorable tile = (TileColorable) tileentity;
		if (tile.color == 2) {
			float xOffset = 0.0f;
			float yOffset = 0.0f;
			if (tile.rotation == 0) {
				yOffset = 0.2f;
			} else if (tile.rotation == 4) {
				yOffset = -0.2f;
			} else if (tile.rotation == 6) {
				xOffset = 0.2f;
			} else if (tile.rotation == 2) {
				xOffset = -0.2f;
			}
			setBlockBounds(0.2f + xOffset, 0.4f, 0.2f + yOffset, 0.8f + xOffset, 0.9f, 0.8f + yOffset);
		} else if (tile.color == 1) {
			setBlockBounds(0.1f, 0.1f, 0.1f, 0.9f, 0.8f, 0.9f);
		} else {
			setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 0.5f, 0.7f);
		}
	}

	@Override
	public Block unlitBlock() {
		return CustomItems.candle_unlit;
	}
}
