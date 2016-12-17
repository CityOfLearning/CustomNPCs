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
	public BlockCandle(boolean lit) {
		super(Blocks.planks, lit);
		setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.5F, 0.7F);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
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
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random random) {
		if (this == unlitBlock()) {
			return;
		}
		TileCandle tile = (TileCandle) world.getTileEntity(pos);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		if (tile.getColor() == 1) {
			if ((tile.getRotation() % 2) == 0) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.5F, y + 0.66F, z + 0.13F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.5F, y + 0.65F, z + 0.13F, 0.0D, 0.0D, 0.0D,
						new int[0]);

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.5F, y + 0.66F, z + 0.87F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.5F, y + 0.65F, z + 0.87F, 0.0D, 0.0D, 0.0D,
						new int[0]);

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.13F, y + 0.66F, z + 0.5F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.13F, y + 0.65F, z + 0.5F, 0.0D, 0.0D, 0.0D,
						new int[0]);

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.87F, y + 0.66F, z + 0.5F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.87F, y + 0.65F, z + 0.5F, 0.0D, 0.0D, 0.0D,
						new int[0]);
			} else {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.24F, y + 0.66F, z + 0.24F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.24F, y + 0.65F, z + 0.24F, 0.0D, 0.0D, 0.0D,
						new int[0]);

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.76F, y + 0.66F, z + 0.76F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.76F, y + 0.65F, z + 0.76F, 0.0D, 0.0D, 0.0D,
						new int[0]);

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.24F, y + 0.66F, z + 0.76F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.24F, y + 0.65F, z + 0.76F, 0.0D, 0.0D, 0.0D,
						new int[0]);

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.76F, y + 0.66F, z + 0.24F, 0.0D, 0.0D, 0.0D,
						new int[0]);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.76F, y + 0.65F, z + 0.24F, 0.0D, 0.0D, 0.0D,
						new int[0]);
			}
		} else {
			float xOffset = 0.5F;
			float yOffset = 0.45F;
			float zOffset = 0.5F;
			if (tile.getColor() == 2) {
				yOffset = 1.05F;
				if (tile.getRotation() == 0) {
					zOffset += 0.12F;
				}
				if (tile.getRotation() == 4) {
					zOffset -= 0.12F;
				}
				if (tile.getRotation() == 6) {
					xOffset += 0.12F;
				}
				if (tile.getRotation() == 2) {
					xOffset -= 0.12F;
				}
			}
			double d0 = x + xOffset;
			double d1 = y + yOffset;
			double d2 = z + zOffset;

			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileColorable)) {
			super.setBlockBoundsBasedOnState(world, pos);
			return;
		}
		TileColorable tile = (TileColorable) tileentity;
		if (tile.getColor() == 2) {
			float xOffset = 0.0F;
			float yOffset = 0.0F;
			if (tile.getRotation() == 0) {
				yOffset = 0.2F;
			} else if (tile.getRotation() == 4) {
				yOffset = -0.2F;
			} else if (tile.getRotation() == 6) {
				xOffset = 0.2F;
			} else if (tile.getRotation() == 2) {
				xOffset = -0.2F;
			}
			setBlockBounds(0.2F + xOffset, 0.4F, 0.2F + yOffset, 0.8F + xOffset, 0.9F, 0.8F + yOffset);
		} else if (tile.getColor() == 1) {
			setBlockBounds(0.1F, 0.1F, 0.1F, 0.9F, 0.8F, 0.9F);
		} else {
			setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.5F, 0.7F);
		}
	}

	@Override
	public Block unlitBlock() {
		return CustomItems.candle_unlit;
	}
}
