//

//

package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class BlockRendererInterface extends TileEntitySpecialRenderer {
	protected static final ResourceLocation Stone;
	protected static final ResourceLocation Iron;
	protected static final ResourceLocation Gold;
	protected static final ResourceLocation Diamond;
	protected static final ResourceLocation PlanksOak;
	protected static final ResourceLocation PlanksBigOak;
	protected static final ResourceLocation PlanksSpruce;
	protected static final ResourceLocation PlanksBirch;
	protected static final ResourceLocation PlanksAcacia;
	protected static final ResourceLocation PlanksJungle;
	protected static final ResourceLocation Steel;
	public static float[][] colorTable;

	static {
		Stone = new ResourceLocation("customnpcs", "textures/cache/stone.png");
		Iron = new ResourceLocation("customnpcs", "textures/cache/iron_block.png");
		Gold = new ResourceLocation("customnpcs", "textures/cache/gold_block.png");
		Diamond = new ResourceLocation("customnpcs", "textures/cache/diamond_block.png");
		PlanksOak = new ResourceLocation("customnpcs", "textures/cache/planks_oak.png");
		PlanksBigOak = new ResourceLocation("customnpcs", "textures/cache/planks_big_oak.png");
		PlanksSpruce = new ResourceLocation("customnpcs", "textures/cache/planks_spruce.png");
		PlanksBirch = new ResourceLocation("customnpcs", "textures/cache/planks_birch.png");
		PlanksAcacia = new ResourceLocation("customnpcs", "textures/cache/planks_acacia.png");
		PlanksJungle = new ResourceLocation("customnpcs", "textures/cache/planks_jungle.png");
		Steel = new ResourceLocation("customnpcs", "textures/models/Steel.png");
		BlockRendererInterface.colorTable = new float[][] { { 1.0f, 1.0f, 1.0f }, { 0.95f, 0.7f, 0.2f },
				{ 0.9f, 0.5f, 0.85f }, { 0.6f, 0.7f, 0.95f }, { 0.9f, 0.9f, 0.2f }, { 0.5f, 0.8f, 0.1f },
				{ 0.95f, 0.7f, 0.8f }, { 0.3f, 0.3f, 0.3f }, { 0.6f, 0.6f, 0.6f }, { 0.3f, 0.6f, 0.7f },
				{ 0.7f, 0.4f, 0.9f }, { 0.2f, 0.4f, 0.8f }, { 0.5f, 0.4f, 0.3f }, { 0.4f, 0.5f, 0.2f },
				{ 0.8f, 0.3f, 0.3f }, { 0.1f, 0.1f, 0.1f } };
	}

	public static void setMaterialTexture(final int meta) {
		final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		if (meta == 1) {
			manager.bindTexture(BlockRendererInterface.Stone);
		} else if (meta == 2) {
			manager.bindTexture(BlockRendererInterface.Iron);
		} else if (meta == 3) {
			manager.bindTexture(BlockRendererInterface.Gold);
		} else if (meta == 4) {
			manager.bindTexture(BlockRendererInterface.Diamond);
		} else {
			manager.bindTexture(BlockRendererInterface.PlanksOak);
		}
	}

	public boolean playerTooFar(final TileEntity tile) {
		final Minecraft mc = Minecraft.getMinecraft();
		final double d6 = mc.getRenderViewEntity().posX - tile.getPos().getX();
		final double d7 = mc.getRenderViewEntity().posY - tile.getPos().getY();
		final double d8 = mc.getRenderViewEntity().posZ - tile.getPos().getZ();
		return ((d6 * d6) + (d7 * d7) + (d8 * d8)) > (specialRenderDistance() * specialRenderDistance());
	}

	public void setWoodTexture(final int meta) {
		final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		if (meta == 1) {
			manager.bindTexture(BlockRendererInterface.PlanksSpruce);
		} else if (meta == 2) {
			manager.bindTexture(BlockRendererInterface.PlanksBirch);
		} else if (meta == 3) {
			manager.bindTexture(BlockRendererInterface.PlanksJungle);
		} else if (meta == 4) {
			manager.bindTexture(BlockRendererInterface.PlanksAcacia);
		} else if (meta == 5) {
			manager.bindTexture(BlockRendererInterface.PlanksBigOak);
		} else {
			manager.bindTexture(BlockRendererInterface.PlanksOak);
		}
	}

	public int specialRenderDistance() {
		return 20;
	}
}
