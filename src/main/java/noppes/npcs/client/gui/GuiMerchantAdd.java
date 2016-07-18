//

//

package noppes.npcs.client.gui;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.ServerEventsHandler;
import noppes.npcs.client.Client;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerMerchantAdd;

@SideOnly(Side.CLIENT)
public class GuiMerchantAdd extends GuiContainer {
	@SideOnly(Side.CLIENT)
	static class MerchantButton extends GuiButton {
		private final boolean field_146157_o;

		public MerchantButton(final int par1, final int par2, final int par3, final boolean par4) {
			super(par1, par2, par3, 12, 19, "");
			field_146157_o = par4;
		}

		@Override
		public void drawButton(final Minecraft p_146112_1_, final int p_146112_2_, final int p_146112_3_) {
			if (visible) {
				p_146112_1_.getTextureManager().bindTexture(GuiMerchantAdd.merchantGuiTextures);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				final boolean flag = (p_146112_2_ >= xPosition) && (p_146112_3_ >= yPosition)
						&& (p_146112_2_ < (xPosition + width)) && (p_146112_3_ < (yPosition + height));
				int k = 0;
				int l = 176;
				if (!enabled) {
					l += width * 2;
				} else if (flag) {
					l += width;
				}
				if (!field_146157_o) {
					k += height;
				}
				this.drawTexturedModalRect(xPosition, yPosition, l, k, width, height);
			}
		}
	}

	private static final ResourceLocation merchantGuiTextures;
	static {
		merchantGuiTextures = new ResourceLocation("textures/gui/container/villager.png");
	}

	static ResourceLocation func_110417_h() {
		return GuiMerchantAdd.merchantGuiTextures;
	}

	private IMerchant theIMerchant;
	private MerchantButton nextRecipeButtonIndex;

	private MerchantButton previousRecipeButtonIndex;

	private int currentRecipeIndex;

	private String field_94082_v;

	public GuiMerchantAdd() {
		super(new ContainerMerchantAdd(Minecraft.getMinecraft().thePlayer, ServerEventsHandler.Merchant,
				Minecraft.getMinecraft().theWorld));
		theIMerchant = ServerEventsHandler.Merchant;
		field_94082_v = I18n.format("entity.Villager.name", new Object[0]);
	}

	@Override
	protected void actionPerformed(final GuiButton par1GuiButton) {
		boolean flag = false;
		final Minecraft mc = Minecraft.getMinecraft();
		if (par1GuiButton == nextRecipeButtonIndex) {
			++currentRecipeIndex;
			flag = true;
		} else if (par1GuiButton == previousRecipeButtonIndex) {
			--currentRecipeIndex;
			flag = true;
		}
		if (par1GuiButton.id == 4) {
			final MerchantRecipeList merchantrecipelist = theIMerchant.getRecipes(mc.thePlayer);
			if (currentRecipeIndex < merchantrecipelist.size()) {
				merchantrecipelist.remove(currentRecipeIndex);
				if (currentRecipeIndex > 0) {
					--currentRecipeIndex;
				}
				Client.sendData(EnumPacketServer.MerchantUpdate, ServerEventsHandler.Merchant.getEntityId(),
						merchantrecipelist);
			}
		}
		if (par1GuiButton.id == 5) {
			ItemStack item1 = inventorySlots.getSlot(0).getStack();
			ItemStack item2 = inventorySlots.getSlot(1).getStack();
			ItemStack sold = inventorySlots.getSlot(2).getStack();
			if ((item1 == null) && (item2 != null)) {
				item1 = item2;
				item2 = null;
			}
			if ((item1 != null) && (sold != null)) {
				item1 = item1.copy();
				sold = sold.copy();
				if (item2 != null) {
					item2 = item2.copy();
				}
				final MerchantRecipe recipe = new MerchantRecipe(item1, item2, sold);
				recipe.increaseMaxTradeUses(2147483639);
				final MerchantRecipeList merchantrecipelist2 = theIMerchant.getRecipes(mc.thePlayer);
				merchantrecipelist2.add(recipe);
				Client.sendData(EnumPacketServer.MerchantUpdate, ServerEventsHandler.Merchant.getEntityId(),
						merchantrecipelist2);
			}
		}
		if (flag) {
			((ContainerMerchantAdd) inventorySlots).setCurrentRecipeIndex(currentRecipeIndex);
			final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
			packetbuffer.writeInt(currentRecipeIndex);
			this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", packetbuffer));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		final Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(GuiMerchantAdd.merchantGuiTextures);
		final int k = (width - xSize) / 2;
		final int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		final MerchantRecipeList merchantrecipelist = theIMerchant.getRecipes(mc.thePlayer);
		if ((merchantrecipelist != null) && !merchantrecipelist.isEmpty()) {
			final int i1 = currentRecipeIndex;
			final MerchantRecipe merchantrecipe = merchantrecipelist.get(i1);
			if (merchantrecipe.isRecipeDisabled()) {
				mc.getTextureManager().bindTexture(GuiMerchantAdd.merchantGuiTextures);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GlStateManager.disableLighting();
				this.drawTexturedModalRect(guiLeft + 83, guiTop + 21, 212, 0, 28, 21);
				this.drawTexturedModalRect(guiLeft + 83, guiTop + 51, 212, 0, 28, 21);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		fontRendererObj.drawString(field_94082_v, (xSize / 2) - (fontRendererObj.getStringWidth(field_94082_v) / 2), 6,
				CustomNpcResourceListener.DefaultTextColor);
		fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, (ySize - 96) + 2,
				CustomNpcResourceListener.DefaultTextColor);
	}

	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		super.drawScreen(par1, par2, par3);
		final Minecraft mc = Minecraft.getMinecraft();
		final MerchantRecipeList merchantrecipelist = theIMerchant.getRecipes(mc.thePlayer);
		if ((merchantrecipelist != null) && !merchantrecipelist.isEmpty()) {
			final int k = (width - xSize) / 2;
			final int l = (height - ySize) / 2;
			final int i1 = currentRecipeIndex;
			final MerchantRecipe merchantrecipe = merchantrecipelist.get(i1);
			GlStateManager.pushMatrix();
			final ItemStack itemstack = merchantrecipe.getItemToBuy();
			final ItemStack itemstack2 = merchantrecipe.getSecondItemToBuy();
			final ItemStack itemstack3 = merchantrecipe.getItemToSell();
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableColorMaterial();
			GlStateManager.enableLighting();
			itemRender.zLevel = 100.0f;
			itemRender.renderItemAndEffectIntoGUI(itemstack, k + 36, l + 24);
			itemRender.renderItemOverlays(fontRendererObj, itemstack, k + 36, l + 24);
			if (itemstack2 != null) {
				itemRender.renderItemAndEffectIntoGUI(itemstack2, k + 62, l + 24);
				itemRender.renderItemOverlays(fontRendererObj, itemstack2, k + 62, l + 24);
			}
			itemRender.renderItemAndEffectIntoGUI(itemstack3, k + 120, l + 24);
			itemRender.renderItemOverlays(fontRendererObj, itemstack3, k + 120, l + 24);
			itemRender.zLevel = 0.0f;
			GlStateManager.disableLighting();
			if (isPointInRegion(36, 24, 16, 16, par1, par2)) {
				renderToolTip(itemstack, par1, par2);
			} else if ((itemstack2 != null) && isPointInRegion(62, 24, 16, 16, par1, par2)) {
				renderToolTip(itemstack2, par1, par2);
			} else if (isPointInRegion(120, 24, 16, 16, par1, par2)) {
				renderToolTip(itemstack3, par1, par2);
			}
			GlStateManager.popMatrix();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
		}
	}

	public IMerchant getIMerchant() {
		return theIMerchant;
	}

	@Override
	public void initGui() {
		super.initGui();
		final int i = (width - xSize) / 2;
		final int j = (height - ySize) / 2;
		buttonList.add(nextRecipeButtonIndex = new MerchantButton(1, i + 120 + 27, (j + 24) - 1, true));
		buttonList.add(previousRecipeButtonIndex = new MerchantButton(2, (i + 36) - 19, (j + 24) - 1, false));
		buttonList.add(new GuiNpcButton(4, i + xSize, j + 20, 60, 20, "gui.remove"));
		buttonList.add(new GuiNpcButton(5, i + xSize, j + 50, 60, 20, "gui.add"));
		nextRecipeButtonIndex.enabled = false;
		previousRecipeButtonIndex.enabled = false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		final Minecraft mc = Minecraft.getMinecraft();
		final MerchantRecipeList merchantrecipelist = theIMerchant.getRecipes(mc.thePlayer);
		if (merchantrecipelist != null) {
			nextRecipeButtonIndex.enabled = (currentRecipeIndex < (merchantrecipelist.size() - 1));
			previousRecipeButtonIndex.enabled = (currentRecipeIndex > 0);
		}
	}
}
