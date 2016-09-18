
package noppes.npcs.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.blocks.tiles.TileColorable;

public class TileEntityItemStackRendererAlt extends TileEntityItemStackRenderer {
	public TileEntityItemStackRendererAlt() {
		TileEntityItemStackRenderer.instance = this;
	}

	@Override
	public void renderByItem(ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		if (block instanceof ITileRenderer) {
			GlStateManager.enableRescaleNormal();
			TileEntity entity = ((ITileRenderer) block).getTile();
			if (entity instanceof TileColorable) {
				((TileColorable) entity).color = 15 - stack.getItemDamage();
			}
			setRenderBlockMeta(entity, block, stack.getItemDamage());
			TileEntityRendererDispatcher.instance.renderTileEntityAt(entity, 0.0, 0.0, 0.0, 0.0f);
			GlStateManager.disableRescaleNormal();
		} else {
			super.renderByItem(stack);
		}
	}

	public void setRenderBlockMeta(TileEntity entity, Block block, int meta) {
		ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity, meta, 6);
		ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity, block, 7);
	}
}
