//

//

package noppes.npcs.client.renderer.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileScripted;

public class BlockScriptedRenderer extends BlockRendererInterface {
	private static Random random;

	static {
		BlockScriptedRenderer.random = new Random();
	}

	private boolean overrideModel() {
		ItemStack held = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
		return (held != null) && ((held.getItem() == CustomItems.wand) || (held.getItem() == CustomItems.scripter));
	}

	private void renderBlock(TileScripted tile, Block b, IBlockState state) {
		GlStateManager.pushMatrix();
		bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.translate(-0.5f, 0.0f, 0.5f);
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, 1.0f);
		if (b.getTickRandomly() && (BlockScriptedRenderer.random.nextInt(12) == 1)) {
			b.randomDisplayTick(tile.getWorld(), tile.getPos(), state, BlockScriptedRenderer.random);
		}
		GlStateManager.popMatrix();
	}

	private void renderItem(ItemStack item) {
		Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float var8, int blockDamage) {
		TileScripted tile = (TileScripted) var1;
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(x + 0.5, y, z + 0.5);
		if (overrideModel()) {
			GlStateManager.translate(0.0, 0.5, 0.0);
			GlStateManager.scale(2.0f, 2.0f, 2.0f);
			renderItem(new ItemStack(CustomItems.scripted));
		} else {
			GlStateManager.rotate(tile.rotationY, 0.0f, 1.0f, 0.0f);
			GlStateManager.rotate(tile.rotationX, 1.0f, 0.0f, 0.0f);
			GlStateManager.rotate(tile.rotationZ, 0.0f, 0.0f, 1.0f);
			GlStateManager.scale(tile.scaleX, tile.scaleY, tile.scaleZ);
			Block b = Block.getBlockFromItem(tile.itemModel.getItem());
			if (b == null) {
				GlStateManager.translate(0.0, 0.5, 0.0);
				renderItem(tile.itemModel);
			} else if (b == CustomItems.scripted) {
				GlStateManager.translate(0.0, 0.5, 0.0);
				GlStateManager.scale(2.0f, 2.0f, 2.0f);
				renderItem(tile.itemModel);
			} else {
				IBlockState state = b.getStateFromMeta(tile.itemModel.getItemDamage());
				renderBlock(tile, b, state);
				if (b.hasTileEntity(state) && !tile.renderTileErrored) {
					try {
						if (tile.renderTile == null) {
							TileEntity entity = b.createTileEntity(getWorld(), state);
							entity.setPos(tile.getPos());
							entity.setWorldObj(getWorld());
							ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity,
									tile.itemModel.getItemDamage(), 6);
							ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity, b, 7);
							tile.renderTile = entity;
							if (entity instanceof ITickable) {
								tile.renderTileUpdate = (ITickable) entity;
							}
						}
						TileEntitySpecialRenderer renderer = TileEntityRendererDispatcher.instance
								.getSpecialRenderer(tile.renderTile);
						if (renderer != null) {
							renderer.renderTileEntityAt(tile.renderTile, -0.5, 0.0, -0.5, var8, blockDamage);
						} else {
							tile.renderTileErrored = true;
						}
					} catch (Exception e) {
						tile.renderTileErrored = true;
					}
				}
			}
		}
		GlStateManager.popMatrix();
	}
}
