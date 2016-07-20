//

//

package noppes.npcs.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Model2DRenderer extends ModelRenderer {
	public static void renderItemIn2D(WorldRenderer worldrenderer, float p_78439_1_, float p_78439_2_, float p_78439_3_,
			float p_78439_4_, int p_78439_5_, int p_78439_6_, float p_78439_7_) {
		Tessellator tessellator = Tessellator.getInstance();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		worldrenderer.pos(0.0, 0.0, 0.0).tex(p_78439_1_, p_78439_4_).normal(0.0f, 0.0f, 1.0f).endVertex();
		worldrenderer.pos(1.0, 0.0, 0.0).tex(p_78439_3_, p_78439_4_).normal(0.0f, 0.0f, 1.0f).endVertex();
		worldrenderer.pos(1.0, 1.0, 0.0).tex(p_78439_3_, p_78439_2_).normal(0.0f, 0.0f, 1.0f).endVertex();
		worldrenderer.pos(0.0, 1.0, 0.0).tex(p_78439_1_, p_78439_2_).normal(0.0f, 0.0f, 1.0f).endVertex();
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		worldrenderer.pos(0.0, 1.0, 0.0f - p_78439_7_).tex(p_78439_1_, p_78439_2_).normal(0.0f, 0.0f, -1.0f)
				.endVertex();
		worldrenderer.pos(1.0, 1.0, 0.0f - p_78439_7_).tex(p_78439_3_, p_78439_2_).normal(0.0f, 0.0f, -1.0f)
				.endVertex();
		worldrenderer.pos(1.0, 0.0, 0.0f - p_78439_7_).tex(p_78439_3_, p_78439_4_).normal(0.0f, 0.0f, -1.0f)
				.endVertex();
		worldrenderer.pos(0.0, 0.0, 0.0f - p_78439_7_).tex(p_78439_1_, p_78439_4_).normal(0.0f, 0.0f, -1.0f)
				.endVertex();
		tessellator.draw();
		float f5 = (0.5f * (p_78439_1_ - p_78439_3_)) / p_78439_5_;
		float f6 = (0.5f * (p_78439_4_ - p_78439_2_)) / p_78439_6_;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		for (int k = 0; k < p_78439_5_; ++k) {
			float f7 = k / p_78439_5_;
			float f8 = (p_78439_1_ + ((p_78439_3_ - p_78439_1_) * f7)) - f5;
			worldrenderer.pos(f7, 0.0, 0.0f - p_78439_7_).tex(f8, p_78439_4_).normal(-1.0f, 0.0f, 0.0f).endVertex();
			worldrenderer.pos(f7, 0.0, 0.0).tex(f8, p_78439_4_).normal(-1.0f, 0.0f, 0.0f).endVertex();
			worldrenderer.pos(f7, 1.0, 0.0).tex(f8, p_78439_2_).normal(-1.0f, 0.0f, 0.0f).endVertex();
			worldrenderer.pos(f7, 1.0, 0.0f - p_78439_7_).tex(f8, p_78439_2_).normal(-1.0f, 0.0f, 0.0f).endVertex();
		}
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		for (int k = 0; k < p_78439_5_; ++k) {
			float f7 = k / p_78439_5_;
			float f8 = (p_78439_1_ + ((p_78439_3_ - p_78439_1_) * f7)) - f5;
			float f9 = f7 + (1.0f / p_78439_5_);
			worldrenderer.pos(f9, 1.0, 0.0f - p_78439_7_).tex(f8, p_78439_2_).normal(1.0f, 0.0f, 0.0f).endVertex();
			worldrenderer.pos(f9, 1.0, 0.0).tex(f8, p_78439_2_).normal(1.0f, 0.0f, 0.0f).endVertex();
			worldrenderer.pos(f9, 0.0, 0.0).tex(f8, p_78439_4_).normal(1.0f, 0.0f, 0.0f).endVertex();
			worldrenderer.pos(f9, 0.0, 0.0f - p_78439_7_).tex(f8, p_78439_4_).normal(1.0f, 0.0f, 0.0f).endVertex();
		}
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		for (int k = 0; k < p_78439_6_; ++k) {
			float f7 = k / p_78439_6_;
			float f8 = (p_78439_4_ + ((p_78439_2_ - p_78439_4_) * f7)) - f6;
			float f9 = f7 + (1.0f / p_78439_6_);
			worldrenderer.pos(0.0, f9, 0.0).tex(p_78439_1_, f8).normal(0.0f, 1.0f, 0.0f).endVertex();
			worldrenderer.pos(1.0, f9, 0.0).tex(p_78439_3_, f8).normal(0.0f, 1.0f, 0.0f).endVertex();
			worldrenderer.pos(1.0, f9, 0.0f - p_78439_7_).tex(p_78439_3_, f8).normal(0.0f, 1.0f, 0.0f).endVertex();
			worldrenderer.pos(0.0, f9, 0.0f - p_78439_7_).tex(p_78439_1_, f8).normal(0.0f, 1.0f, 0.0f).endVertex();
		}
		tessellator.draw();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		for (int k = 0; k < p_78439_6_; ++k) {
			float f7 = k / p_78439_6_;
			float f8 = (p_78439_4_ + ((p_78439_2_ - p_78439_4_) * f7)) - f6;
			worldrenderer.pos(1.0, f7, 0.0).tex(p_78439_3_, f8).normal(0.0f, -1.0f, 0.0f).endVertex();
			worldrenderer.pos(0.0, f7, 0.0).tex(p_78439_1_, f8).normal(0.0f, -1.0f, 0.0f).endVertex();
			worldrenderer.pos(0.0, f7, 0.0f - p_78439_7_).tex(p_78439_1_, f8).normal(0.0f, -1.0f, 0.0f).endVertex();
			worldrenderer.pos(1.0, f7, 0.0f - p_78439_7_).tex(p_78439_3_, f8).normal(0.0f, -1.0f, 0.0f).endVertex();
		}
		tessellator.draw();
	}

	private boolean compiled;
	private int displayList;
	private float x1;
	private float x2;
	private float y1;
	private float y2;
	private int width;
	private int height;
	private float rotationOffsetX;
	private float rotationOffsetY;
	private float rotationOffsetZ;
	private float scaleX;
	private float scaleY;

	private float thickness;

	public Model2DRenderer(ModelBase par1ModelBase, float x, float y, int width, int height) {
		this(par1ModelBase, x, y, width, height, par1ModelBase.textureWidth, par1ModelBase.textureHeight);
	}

	public Model2DRenderer(ModelBase par1ModelBase, float x, float y, int width, int height, int textureWidth,
			int textureHeight) {
		super(par1ModelBase);
		scaleX = 1.0f;
		scaleY = 1.0f;
		thickness = 1.0f;
		this.width = width;
		this.height = height;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		x1 = x / textureWidth;
		y1 = y / textureHeight;
		x2 = (x + width) / textureWidth;
		y2 = (y + height) / textureHeight;
	}

	@SideOnly(Side.CLIENT)
	private void compileDisplayList(float par1) {
		GL11.glNewList(displayList = GLAllocation.generateDisplayLists(1), 4864);
		GlStateManager.translate(rotationOffsetX * par1, rotationOffsetY * par1, rotationOffsetZ * par1);
		GlStateManager.scale((scaleX * width) / height, scaleY, thickness);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		if (mirror) {
			GlStateManager.translate(0.0f, 0.0f, -1.0f * par1);
			GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
		}
		renderItemIn2D(Tessellator.getInstance().getWorldRenderer(), x1, y1, x2, y2, width, height, par1);
		GL11.glEndList();
		compiled = true;
	}

	@Override
	public void render(float par1) {
		if (!showModel || isHidden) {
			return;
		}
		if (!compiled) {
			compileDisplayList(par1);
		}
		GlStateManager.pushMatrix();
		postRender(par1);
		GlStateManager.callList(displayList);
		GlStateManager.popMatrix();
	}

	public void setRotationOffset(float x, float y, float z) {
		rotationOffsetX = x;
		rotationOffsetY = y;
		rotationOffsetZ = z;
	}

	public void setScale(float scale) {
		scaleX = scale;
		scaleY = scale;
	}

	public void setScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}
}
