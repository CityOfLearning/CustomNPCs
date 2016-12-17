
package noppes.npcs.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.util.Schematic;

public class ClientEventHandler {
	private int displayList;

	public ClientEventHandler() {
		displayList = -1;
	}

	public void drawSelectionBox(BlockPos pos) {
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		AxisAlignedBB bb = new AxisAlignedBB(BlockPos.ORIGIN, pos);
		RenderGlobal.drawOutlinedBoundingBox(bb, 255, 0, 0, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
	}

	@SubscribeEvent
	public void onRenderTick(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if ((TileBuilder.DrawPos == null) || (TileBuilder.DrawPos.distanceSq(player.getPosition()) > 1000000.0)) {
			return;
		}
		TileEntity te = player.worldObj.getTileEntity(TileBuilder.DrawPos);
		if ((te == null) || !(te instanceof TileBuilder)) {
			return;
		}
		TileBuilder tile = (TileBuilder) te;
		Schematic schem = tile.getSchematic();
		if (schem == null) {
			return;
		}
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(TileBuilder.DrawPos.getX() - TileEntityRendererDispatcher.staticPlayerX,
				(TileBuilder.DrawPos.getY() - TileEntityRendererDispatcher.staticPlayerY) + 0.01,
				TileBuilder.DrawPos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);
		GlStateManager.translate(1.0f, tile.getyOffest(), 1.0f);
		if ((tile.getRotation() % 2) == 0) {
			drawSelectionBox(new BlockPos(schem.width, schem.height, schem.length));
		} else {
			drawSelectionBox(new BlockPos(schem.length, schem.height, schem.width));
		}
		if (schem.size < 125000) {
			if (TileBuilder.Compiled) {
				GlStateManager.callList(displayList);
			} else {
				BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
				if (displayList >= 0) {
					GLAllocation.deleteDisplayLists(displayList);
				}
				GL11.glNewList(displayList = GLAllocation.generateDisplayLists(1), 4864);
				try {
					for (int i = 0; i < schem.size; ++i) {
						Block b = Block.getBlockById(schem.blockArray[i]);
						if (b != null) {
							if (b.getRenderType() == 3) {
								IBlockState state = b.getStateFromMeta(schem.blockDataArray[i]);
								int posX = i % schem.width;
								int posZ = ((i - posX) / schem.width) % schem.length;
								int posY = (((i - posX) / schem.width) - posZ) / schem.length;
								BlockPos pos = schem.rotatePos(posX, posY, posZ, tile.getRotation());
								GlStateManager.pushMatrix();
								GlStateManager.pushAttrib();
								GlStateManager.enableRescaleNormal();
								GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
								Minecraft.getMinecraft().getTextureManager()
										.bindTexture(TextureMap.locationBlocksTexture);
								GlStateManager.color(1.0f, 1.0f, 1.0f);
								GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
								state = schem.rotationState(state, tile.getRotation());
								try {
									dispatcher.renderBlockBrightness(state, 1.0f);
									if (GL11.glGetError() != 0) {
										break;
									}
								} catch (Exception e2) {
								} finally {
									GlStateManager.popAttrib();
									GlStateManager.disableRescaleNormal();
									GlStateManager.popMatrix();
								}
							}
						}
					}
				} catch (Exception e) {
					CustomNpcs.logger.error("Error preview builder block", e);
				} finally {
					GL11.glEndList();
					if (GL11.glGetError() == 0) {
						TileBuilder.Compiled = true;
					}
				}
			}
		}
		RenderHelper.disableStandardItemLighting();
		GlStateManager.translate(-1.0f, 0.0f, -1.0f);
		GlStateManager.popMatrix();
	}
}
