//

//

package noppes.npcs.client.renderer.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileDoor;

public class BlockDoorRenderer extends BlockRendererInterface {
	private static Random random;

	static {
		BlockDoorRenderer.random = new Random();
	}

	private boolean overrideModel() {
		final ItemStack held = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
		return (held != null) && ((held.getItem() == CustomItems.wand) || (held.getItem() == CustomItems.scripter)
				|| (held.getItem() == CustomItems.scriptedDoorTool));
	}

	private void renderBlock(final TileDoor tile, final Block b, final IBlockState state) {
		bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.translate(-0.5f, 0.0f, 0.5f);
		final BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		final IBakedModel ibakedmodel = dispatcher.getBlockModelShapes().getModelForState(state);
		if (ibakedmodel == null) {
			dispatcher.renderBlockBrightness(state, 1.0f);
		} else {
			dispatcher.getBlockModelRenderer().renderModelBrightness(ibakedmodel, state, 1.0f, true);
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntity var1, final double x, final double y, final double z,
			final float var8, final int blockDamage) {
		final TileDoor tile = (TileDoor) var1;
		final IBlockState original = CustomItems.scriptedDoor.getStateFromMeta(tile.getBlockMetadata());
		BlockPos lowerPos = tile.getPos();
		if (original.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			lowerPos = tile.getPos().down();
		}
		final BlockPos upperPos = lowerPos.up();
		final TileDoor lowerTile = (TileDoor) getWorld().getTileEntity(lowerPos);
		final TileDoor upperTile = (TileDoor) getWorld().getTileEntity(upperPos);
		if ((lowerTile == null) || (upperTile == null)) {
			return;
		}
		final IBlockState lowerState = CustomItems.scriptedDoor.getStateFromMeta(lowerTile.getBlockMetadata());
		final IBlockState upperState = CustomItems.scriptedDoor.getStateFromMeta(upperTile.getBlockMetadata());
		final int meta = BlockDoor.combineMetadata(getWorld(), tile.getPos());
		Block b = lowerTile.blockModel;
		if (overrideModel()) {
			b = CustomItems.scriptedDoor;
		}
		IBlockState state = b.getStateFromMeta(meta);
		state = state.withProperty(BlockDoor.HALF, original.getValue(BlockDoor.HALF));
		state = state.withProperty(BlockDoor.FACING, lowerState.getValue(BlockDoor.FACING));
		state = state.withProperty(BlockDoor.OPEN, lowerState.getValue(BlockDoor.OPEN));
		state = state.withProperty(BlockDoor.HINGE, upperState.getValue(BlockDoor.HINGE));
		state = state.withProperty(BlockDoor.POWERED, upperState.getValue(BlockDoor.POWERED));
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.translate(x + 0.5, y, z + 0.5);
		GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
		renderBlock(tile, b, state);
		GlStateManager.popMatrix();
	}
}
