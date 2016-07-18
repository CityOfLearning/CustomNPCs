//

//

package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;

@SideOnly(Side.CLIENT)
public class GuiRecipes extends GuiNPCInterface {
	private static final ResourceLocation resource;
	static {
		resource = new ResourceLocation("customnpcs", "textures/gui/slot.png");
	}
	private int page;
	private GuiNpcLabel label;
	private GuiNpcButton left;
	private GuiNpcButton right;

	private List<IRecipe> recipes;

	public GuiRecipes() {
		page = 0;
		recipes = new ArrayList<IRecipe>();
		ySize = 182;
		xSize = 256;
		setBackground("recipes.png");
		closeOnEsc = true;
		recipes.addAll(RecipeController.instance.anvilRecipes.values());
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		if (!button.enabled) {
			return;
		}
		if (button == right) {
			--page;
		}
		if (button == left) {
			++page;
		}
		updateButton();
	}

	private void drawItem(final ItemStack item, final int x, final int y, final int xMouse, final int yMouse) {
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		itemRender.zLevel = 100.0f;
		itemRender.renderItemAndEffectIntoGUI(item, x, y);
		itemRender.renderItemOverlays(fontRendererObj, item, x, y);
		itemRender.zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void drawOverlay(final ItemStack item, final int x, final int y, final int xMouse, final int yMouse) {
		if (isPointInRegion(x - guiLeft, y - guiTop, 16, 16, xMouse, yMouse)) {
			renderToolTip(item, xMouse, yMouse);
		}
	}

	@Override
	public void drawScreen(final int xMouse, final int yMouse, final float f) {
		super.drawScreen(xMouse, yMouse, f);
		mc.renderEngine.bindTexture(GuiRecipes.resource);
		label.label = page + 1 + "/" + MathHelper.ceiling_float_int(recipes.size() / 4.0f);
		label.x = guiLeft + ((256 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(label.label)) / 2);
		for (int i = 0; i < 4; ++i) {
			final int index = i + (page * 4);
			if (index >= recipes.size()) {
				break;
			}
			final IRecipe irecipe = recipes.get(index);
			if (irecipe.getRecipeOutput() != null) {
				int x = guiLeft + 5 + ((i / 2) * 126);
				int y = guiTop + 15 + ((i % 2) * 76);
				drawItem(irecipe.getRecipeOutput(), x + 98, y + 28, xMouse, yMouse);
				if (irecipe instanceof RecipeCarpentry) {
					final RecipeCarpentry recipe = (RecipeCarpentry) irecipe;
					x += (72 - (recipe.recipeWidth * 18)) / 2;
					y += (72 - (recipe.recipeHeight * 18)) / 2;
					for (int j = 0; j < recipe.recipeWidth; ++j) {
						for (int k = 0; k < recipe.recipeHeight; ++k) {
							mc.renderEngine.bindTexture(GuiRecipes.resource);
							GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
							this.drawTexturedModalRect(x + (j * 18), y + (k * 18), 0, 0, 18, 18);
							final ItemStack item = recipe.getCraftingItem(j + (k * recipe.recipeWidth));
							if (item != null) {
								drawItem(item, x + (j * 18) + 1, y + (k * 18) + 1, xMouse, yMouse);
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < 4; ++i) {
			final int index = i + (page * 4);
			if (index >= recipes.size()) {
				break;
			}
			final IRecipe irecipe = recipes.get(index);
			if (irecipe instanceof RecipeCarpentry) {
				final RecipeCarpentry recipe2 = (RecipeCarpentry) irecipe;
				if (recipe2.getRecipeOutput() != null) {
					int x2 = guiLeft + 5 + ((i / 2) * 126);
					int y2 = guiTop + 15 + ((i % 2) * 76);
					drawOverlay(recipe2.getRecipeOutput(), x2 + 98, y2 + 22, xMouse, yMouse);
					x2 += (72 - (recipe2.recipeWidth * 18)) / 2;
					y2 += (72 - (recipe2.recipeHeight * 18)) / 2;
					for (int j = 0; j < recipe2.recipeWidth; ++j) {
						for (int k = 0; k < recipe2.recipeHeight; ++k) {
							final ItemStack item = recipe2.getCraftingItem(j + (k * recipe2.recipeWidth));
							if (item != null) {
								drawOverlay(item, x2 + (j * 18) + 1, y2 + (k * 18) + 1, xMouse, yMouse);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addLabel(new GuiNpcLabel(0, "Recipe List", guiLeft + 5, guiTop + 5));
		addLabel(label = new GuiNpcLabel(1, "", guiLeft + 5, guiTop + 168));
		addButton(left = new GuiButtonNextPage(1, guiLeft + 150, guiTop + 164, true));
		addButton(right = new GuiButtonNextPage(2, guiLeft + 80, guiTop + 164, false));
		updateButton();
	}

	protected boolean isPointInRegion(final int p_146978_1_, final int p_146978_2_, final int p_146978_3_,
			final int p_146978_4_, int p_146978_5_, int p_146978_6_) {
		final int k1 = guiLeft;
		final int l1 = guiTop;
		p_146978_5_ -= k1;
		p_146978_6_ -= l1;
		return (p_146978_5_ >= (p_146978_1_ - 1)) && (p_146978_5_ < (p_146978_1_ + p_146978_3_ + 1))
				&& (p_146978_6_ >= (p_146978_2_ - 1)) && (p_146978_6_ < (p_146978_2_ + p_146978_4_ + 1));
	}

	@Override
	public void save() {
	}

	private void updateButton() {
		final GuiNpcButton right = this.right;
		final GuiNpcButton right2 = this.right;
		final boolean b = page > 0;
		right2.enabled = b;
		right.visible = b;
		final GuiNpcButton left = this.left;
		final GuiNpcButton left2 = this.left;
		final boolean b2 = (page + 1) < MathHelper.ceiling_float_int(recipes.size() / 4.0f);
		left2.enabled = b2;
		left.visible = b2;
	}
}
