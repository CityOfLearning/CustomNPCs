//

//

package noppes.npcs.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileCampfire;

public class BlockCampfire extends BlockLightable {
	public BlockCampfire(final boolean lit) {
		super(Blocks.cobblestone, lit);
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
		if (lit) {
			setLightLevel(0.9375f);
		}
	}

	@Override
	public TileEntity createNewTileEntity(final World var1, final int var2) {
		return new TileCampfire();
	}

	@Override
	public Block litBlock() {
		return CustomItems.campfire;
	}

	@Override
	public int maxRotation() {
		return 8;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack item = player.inventory.getCurrentItem();
		if (item == null) {
			return true;
		}
		if (((item.getItem() == Items.flint) || (item.getItem() == Items.flint_and_steel)) && (unlitBlock() == this)) {
			if ((world.rand.nextInt(3) == 0) && !world.isRemote) {
				super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
			}
			CustomNpcs.proxy.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.5f, pos.getY() + 0.5f,
					pos.getZ() + 0.5f, 0.0, 0.0, 0.0, 2.0f);
			if (item.getItem() == Items.flint) {
				NoppesUtilServer.consumeItemStack(1, player);
			} else {
				item.damageItem(1, player);
			}
			return true;
		}
		if ((item.getItem() == Item.getItemFromBlock(Blocks.sand)) && (litBlock() == this)) {
			super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World world, final BlockPos pos, final IBlockState state, final Random random) {
		if (state.getBlock() != CustomItems.campfire) {
			return;
		}
		if (random.nextInt(36) == 0) {
			world.playSound(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "fire.fire",
					1.0f + random.nextFloat(), 0.3f + (random.nextFloat() * 0.7f), false);
		}
		world.getTileEntity(pos);
		final float xOffset = 0.5f;
		final float yOffset = 0.7f;
		final float zOffset = 0.5f;
		final double d0 = pos.getX() + xOffset;
		final double d2 = pos.getY() + yOffset;
		final double d3 = pos.getZ() + zOffset;
		GlStateManager.pushMatrix();
		CustomNpcs.proxy.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d2, d3, 0.0, 0.0, 0.0, 2.0f);
		CustomNpcs.proxy.spawnParticle(EnumParticleTypes.FLAME, d0, d2, d3, 0.0, 0.0, 0.0, 4.0f);
		GlStateManager.popMatrix();
	}

	@Override
	public Block unlitBlock() {
		return CustomItems.campfire_unlit;
	}
}
