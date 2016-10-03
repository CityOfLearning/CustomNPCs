package noppes.npcs.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.LogWriter;
import noppes.npcs.Schematic;
import noppes.npcs.blocks.tiles.TileBuilder;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {

   private int displayList = -1;


   @SubscribeEvent
   public void onRenderTick(RenderWorldLastEvent event) {
      EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
      if(TileBuilder.DrawPos != null && TileBuilder.DrawPos.distanceSq(player.getPosition()) <= 1000000.0D) {
         TileEntity te = player.worldObj.getTileEntity(TileBuilder.DrawPos);
         if(te != null && te instanceof TileBuilder) {
            TileBuilder tile = (TileBuilder)te;
            Schematic schem = tile.getSchematic();
            if(schem != null) {
               GlStateManager.pushMatrix();
               RenderHelper.enableStandardItemLighting();
               GlStateManager.translate((double)TileBuilder.DrawPos.getX() - TileEntityRendererDispatcher.staticPlayerX, (double)TileBuilder.DrawPos.getY() - TileEntityRendererDispatcher.staticPlayerY + 0.01D, (double)TileBuilder.DrawPos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);
               GlStateManager.translate(1.0F, (float)tile.yOffest, 1.0F);
               if(tile.rotation % 2 == 0) {
                  this.drawSelectionBox(new BlockPos(schem.width, schem.height, schem.length));
               } else {
                  this.drawSelectionBox(new BlockPos(schem.length, schem.height, schem.width));
               }

               if(schem.size < 125000) {
                  if(TileBuilder.Compiled) {
                     GlStateManager.callList(this.displayList);
                  } else {
                     BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                     if(this.displayList >= 0) {
                        GLAllocation.deleteDisplayLists(this.displayList);
                     }

                     this.displayList = GLAllocation.generateDisplayLists(1);
                     GL11.glNewList(this.displayList, 4864);

                     try {
                        for(int e = 0; e < schem.size; ++e) {
                           Block b = Block.getBlockById(schem.blockArray[e]);
                           if(b != null && b.getRenderType() == 3) {
                              IBlockState state = b.getStateFromMeta(schem.blockDataArray[e]);
                              int posX = e % schem.width;
                              int posZ = (e - posX) / schem.width % schem.length;
                              int posY = ((e - posX) / schem.width - posZ) / schem.length;
                              BlockPos pos = schem.rotatePos(posX, posY, posZ, tile.rotation);
                              GlStateManager.pushMatrix();
                              GlStateManager.pushAttrib();
                              GlStateManager.enableRescaleNormal();
                              GlStateManager.translate((float)pos.getX(), (float)pos.getY(), (float)pos.getZ());
                              Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                              GlStateManager.color(1.0F, 1.0F, 1.0F);
                              GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                              state = schem.rotationState(state, tile.rotation);

                              try {
                                 dispatcher.renderBlockBrightness(state, 1.0F);
                                 if(GL11.glGetError() != 0) {
                                    break;
                                 }
                              } catch (Exception var25) {
                                 ;
                              } finally {
                                 GlStateManager.popAttrib();
                                 GlStateManager.disableRescaleNormal();
                                 GlStateManager.popMatrix();
                              }
                           }
                        }
                     } catch (Exception var27) {
                        LogWriter.error("Error preview builder block", var27);
                     } finally {
                        GL11.glEndList();
                        if(GL11.glGetError() == 0) {
                           TileBuilder.Compiled = true;
                        }

                     }
                  }
               }

               RenderHelper.disableStandardItemLighting();
               GlStateManager.translate(-1.0F, 0.0F, -1.0F);
               GlStateManager.popMatrix();
            }
         }
      }
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
}
