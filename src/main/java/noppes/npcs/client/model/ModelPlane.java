//

//

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.WorldRenderer;
import noppes.npcs.client.renderer.EnumPlanePosition;

public class ModelPlane extends ModelBox {
	private PositionTextureVertex[] vertexPositions;
	private TexturedQuad quad;

	public ModelPlane(final ModelRenderer par1ModelRenderer, final int textureOffsetX, final int textureOffsetY,
			float par4, float par5, float par6, final int par7, final int par8, final int par9, final float par10,
			final EnumPlanePosition position) {
		super(par1ModelRenderer, textureOffsetX, textureOffsetY, par4, par5, par6, par7, par8, par9, par10);
		vertexPositions = new PositionTextureVertex[8];
		float var11 = par4 + par7;
		float var12 = par5 + par8;
		float var13 = par6 + par9;
		par4 -= par10;
		par5 -= par10;
		par6 -= par10;
		var11 += par10;
		var12 += par10;
		var13 += par10;
		if (par1ModelRenderer.mirror) {
			final float var14 = var11;
			var11 = par4;
			par4 = var14;
		}
		final PositionTextureVertex var15 = new PositionTextureVertex(par4, par5, par6, 0.0f, 0.0f);
		final PositionTextureVertex var16 = new PositionTextureVertex(var11, par5, par6, 0.0f, 8.0f);
		final PositionTextureVertex var17 = new PositionTextureVertex(var11, var12, par6, 8.0f, 8.0f);
		final PositionTextureVertex var18 = new PositionTextureVertex(par4, var12, par6, 8.0f, 0.0f);
		final PositionTextureVertex var19 = new PositionTextureVertex(par4, par5, var13, 0.0f, 0.0f);
		final PositionTextureVertex var20 = new PositionTextureVertex(var11, par5, var13, 0.0f, 8.0f);
		final PositionTextureVertex var21 = new PositionTextureVertex(var11, var12, var13, 8.0f, 8.0f);
		final PositionTextureVertex var22 = new PositionTextureVertex(par4, var12, var13, 8.0f, 0.0f);
		vertexPositions[0] = var15;
		vertexPositions[1] = var16;
		vertexPositions[2] = var17;
		vertexPositions[3] = var18;
		vertexPositions[4] = var19;
		vertexPositions[5] = var20;
		vertexPositions[6] = var21;
		vertexPositions[7] = var22;
		if (position == EnumPlanePosition.LEFT) {
			quad = new TexturedQuad(new PositionTextureVertex[] { var20, var16, var17, var21 }, textureOffsetX,
					textureOffsetY, textureOffsetX + par9, textureOffsetY + par8, par1ModelRenderer.textureWidth,
					par1ModelRenderer.textureHeight);
		}
		if (position == EnumPlanePosition.TOP) {
			quad = new TexturedQuad(new PositionTextureVertex[] { var20, var19, var15, var16 }, textureOffsetX,
					textureOffsetY, textureOffsetX + par7, textureOffsetY + par9, par1ModelRenderer.textureWidth,
					par1ModelRenderer.textureHeight);
		}
		if (position == EnumPlanePosition.BACK) {
			quad = new TexturedQuad(new PositionTextureVertex[] { var16, var15, var18, var17 }, textureOffsetX,
					textureOffsetY, textureOffsetX + par7, textureOffsetY + par8, par1ModelRenderer.textureWidth,
					par1ModelRenderer.textureHeight);
		}
		if (par1ModelRenderer.mirror) {
			quad.flipFace();
		}
	}

	@Override
	public void render(final WorldRenderer par1Tessellator, final float par2) {
		quad.draw(par1Tessellator, par2);
	}
}
