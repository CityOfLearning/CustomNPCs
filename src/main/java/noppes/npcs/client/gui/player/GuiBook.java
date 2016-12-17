package noppes.npcs.client.gui.player;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.util.NoppesUtilPlayer;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen {
	@SideOnly(Side.CLIENT)
	static class NextPageButton extends GuiButton {
		private final boolean field_146151_o;

		public NextPageButton(int par1, int par2, int par3, boolean par4) {
			super(par1, par2, par3, 23, 13, "");
			field_146151_o = par4;
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (visible) {
				boolean flag = (p_146112_2_ >= xPosition) && (p_146112_3_ >= yPosition)
						&& (p_146112_2_ < (xPosition + width)) && (p_146112_3_ < (yPosition + height));
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				p_146112_1_.getTextureManager().bindTexture(GuiBook.bookGuiTextures);
				int k = 0;
				int l = 192;
				if (flag) {
					k += 23;
				}
				if (!field_146151_o) {
					l += 13;
				}
				drawTexturedModalRect(xPosition, yPosition, k, l, 23, 13);
			}
		}
	}

	private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
	private final EntityPlayer editingPlayer;
	private final ItemStack bookObj;
	private final boolean bookIsUnsigned;
	private boolean bookIsModified;
	private boolean bookGettingSigned;
	private int updateCount;
	private int bookImageWidth = 192;
	private int bookImageHeight = 192;
	private int bookTotalPages = 1;
	private int currPage;
	private NBTTagList bookPages;
	private String bookTitle = "";
	private NextPageButton buttonNextPage;
	private NextPageButton buttonPreviousPage;
	private GuiButton buttonDone;
	private GuiButton buttonSign;
	private GuiButton buttonFinalize;
	private GuiButton buttonCancel;
	private int x;
	private int y;

	private int z;

	public GuiBook(EntityPlayer par1EntityPlayer, ItemStack item, int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		editingPlayer = par1EntityPlayer;
		bookObj = item;
		bookIsUnsigned = (item.getItem() == Items.writable_book);
		if (item.hasTagCompound()) {
			NBTTagCompound nbttagcompound = item.getTagCompound();
			bookPages = nbttagcompound.getTagList("pages", 8);
			if (bookPages != null) {
				bookPages = ((NBTTagList) bookPages.copy());
				bookTotalPages = bookPages.tagCount();
				if (bookTotalPages < 1) {
					bookTotalPages = 1;
				}
			}
		}
		if ((bookPages == null) && (bookIsUnsigned)) {
			bookPages = new NBTTagList();
			bookPages.appendTag(new NBTTagString(""));
			bookTotalPages = 1;
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 0) {
				mc.displayGuiScreen((GuiScreen) null);
				sendBookToServer(false);
			} else if ((p_146284_1_.id == 3) && (bookIsUnsigned)) {
				bookGettingSigned = true;
			} else if (p_146284_1_.id == 1) {
				if (currPage < (bookTotalPages - 1)) {
					currPage += 1;
				} else if (bookIsUnsigned) {
					addNewPage();
					if (currPage < (bookTotalPages - 1)) {
						currPage += 1;
					}
				}
			} else if (p_146284_1_.id == 2) {
				if (currPage > 0) {
					currPage -= 1;
				}
			} else if ((p_146284_1_.id == 5) && (bookGettingSigned)) {
				sendBookToServer(true);
				mc.displayGuiScreen((GuiScreen) null);
			} else if ((p_146284_1_.id == 4) && (bookGettingSigned)) {
				bookGettingSigned = false;
			}
			updateButtons();
		}
	}

	private void addNewPage() {
		if ((bookPages != null) && (bookPages.tagCount() < 50)) {
			bookPages.appendTag(new NBTTagString(""));
			bookTotalPages += 1;
			bookIsModified = true;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(bookGuiTextures);
		int k = (width - bookImageWidth) / 2;
		byte b0 = 2;
		drawTexturedModalRect(k, b0, 0, 0, bookImageWidth, bookImageHeight);
		if (bookGettingSigned) {
			String s = bookTitle;
			if (bookIsUnsigned) {
				if (((updateCount / 6) % 2) == 0) {
					s = s + "" + EnumChatFormatting.BLACK + "_";
				} else {
					s = s + "" + EnumChatFormatting.GRAY + "_";
				}
			}
			String s1 = I18n.format("book.editTitle", new Object[0]);
			int l = fontRendererObj.getStringWidth(s1);
			fontRendererObj.drawString(s1, k + 36 + ((116 - l) / 2), b0 + 16 + 16, 0);
			int i1 = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, k + 36 + ((116 - i1) / 2), b0 + 48, 0);
			String s2 = I18n.format("book.byAuthor", new Object[] { editingPlayer.getName() });
			int j1 = fontRendererObj.getStringWidth(s2);
			fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + s2, k + 36 + ((116 - j1) / 2), b0 + 48 + 10, 0);
			String s3 = I18n.format("book.finalizeWarning", new Object[0]);
			fontRendererObj.drawSplitString(s3, k + 36, b0 + 80, 116, 0);
		} else {
			String s = I18n.format("book.pageIndicator",
					new Object[] { Integer.valueOf(currPage + 1), Integer.valueOf(bookTotalPages) });
			String s1 = "";
			if ((bookPages != null) && (currPage >= 0) && (currPage < bookPages.tagCount())) {
				s1 = bookPages.getStringTagAt(currPage);
			}
			if (bookIsUnsigned) {
				if (fontRendererObj.getBidiFlag()) {
					s1 = s1 + "_";
				} else if (((updateCount / 6) % 2) == 0) {
					s1 = s1 + "" + EnumChatFormatting.BLACK + "_";
				} else {
					s1 = s1 + "" + EnumChatFormatting.GRAY + "_";
				}
			}
			int l = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, ((k - l) + bookImageWidth) - 44, b0 + 16, 0);
			fontRendererObj.drawSplitString(s1, k + 36, b0 + 16 + 16, 116, 0);
		}
		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		if (bookIsUnsigned) {
			buttonList.add(buttonSign = new GuiButton(3, (width / 2) - 100, 4 + bookImageHeight, 98, 20,
					I18n.format("book.signButton", new Object[0])));
			buttonList.add(buttonDone = new GuiButton(0, (width / 2) + 2, 4 + bookImageHeight, 98, 20,
					I18n.format("gui.done", new Object[0])));
			buttonList.add(buttonFinalize = new GuiButton(5, (width / 2) - 100, 4 + bookImageHeight, 98, 20,
					I18n.format("book.finalizeButton", new Object[0])));
			buttonList.add(buttonCancel = new GuiButton(4, (width / 2) + 2, 4 + bookImageHeight, 98, 20,
					I18n.format("gui.cancel", new Object[0])));
		} else {
			buttonList.add(buttonDone = new GuiButton(0, (width / 2) - 100, 4 + bookImageHeight, 200, 20,
					I18n.format("gui.done", new Object[0])));
		}
		int i = (width - bookImageWidth) / 2;
		byte b0 = 2;
		buttonList.add(buttonNextPage = new NextPageButton(1, i + 120, b0 + 154, true));
		buttonList.add(buttonPreviousPage = new NextPageButton(2, i + 38, b0 + 154, false));
		updateButtons();
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);
		if (bookIsUnsigned) {
			if (bookGettingSigned) {
				keyTypedInTitle(par1, par2);
			} else {
				keyTypedInBook(par1, par2);
			}
		}
	}

	private void keyTypedInBook(char p_146463_1_, int p_146463_2_) {
		switch (p_146463_1_) {
		case '\026':
			pageInsertIntoCurrent(GuiScreen.getClipboardString());
			return;
		}
		switch (p_146463_2_) {
		case 14:
			String s = pageGetCurrent();
			if (s.length() > 0) {
				pageSetCurrent(s.substring(0, s.length() - 1));
			}
			return;
		case 28:
		case 156:
			pageInsertIntoCurrent("\n");
			return;
		}
		if (ChatAllowedCharacters.isAllowedCharacter(p_146463_1_)) {
			pageInsertIntoCurrent(Character.toString(p_146463_1_));
		}
	}

	private void keyTypedInTitle(char p_146460_1_, int p_146460_2_) {
		switch (p_146460_2_) {
		case 14:
			if (!bookTitle.isEmpty()) {
				bookTitle = bookTitle.substring(0, bookTitle.length() - 1);
				updateButtons();
			}
			return;
		case 28:
		case 156:
			if (!bookTitle.isEmpty()) {
				sendBookToServer(true);
				mc.displayGuiScreen((GuiScreen) null);
			}
			return;
		}
		if ((bookTitle.length() < 16) && (ChatAllowedCharacters.isAllowedCharacter(p_146460_1_))) {
			bookTitle += Character.toString(p_146460_1_);
			updateButtons();
			bookIsModified = true;
		}
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	private String pageGetCurrent() {
		return (bookPages != null) && (currPage >= 0) && (currPage < bookPages.tagCount())
				? bookPages.getStringTagAt(currPage) : "";
	}

	private void pageInsertIntoCurrent(String p_146459_1_) {
		String s1 = pageGetCurrent();
		String s2 = s1 + p_146459_1_;
		int i = fontRendererObj.splitStringWidth(s2 + "" + EnumChatFormatting.BLACK + "_", 118);
		if ((i <= 118) && (s2.length() < 256)) {
			pageSetCurrent(s2);
		}
	}

	private void pageSetCurrent(String p_146457_1_) {
		if ((bookPages != null) && (currPage >= 0) && (currPage < bookPages.tagCount())) {
			bookPages.set(currPage, new NBTTagString(p_146457_1_));
			bookIsModified = true;
		}
	}

	private void sendBookToServer(boolean sign) {
		if ((bookIsUnsigned) && (bookIsModified) && (bookPages != null)) {
			while (bookPages.tagCount() > 1) {
				String s = bookPages.getStringTagAt(bookPages.tagCount() - 1);
				if (s.length() != 0) {
					break;
				}
				bookPages.removeTag(bookPages.tagCount() - 1);
			}
			if (bookObj.hasTagCompound()) {
				NBTTagCompound nbttagcompound = bookObj.getTagCompound();
				nbttagcompound.setTag("pages", bookPages);
			} else {
				bookObj.setTagInfo("pages", bookPages);
			}
			if (sign) {
				bookObj.setTagInfo("author", new NBTTagString(editingPlayer.getName()));
				bookObj.setTagInfo("title", new NBTTagString(bookTitle.trim()));
				bookObj.setItem(Items.written_book);
			}
			NoppesUtilPlayer.sendData(EnumPlayerPacket.SaveBook, new Object[] { Integer.valueOf(x), Integer.valueOf(y),
					Integer.valueOf(z), Boolean.valueOf(sign), bookObj.writeToNBT(new NBTTagCompound()) });
		}
	}

	private void updateButtons() {
		buttonNextPage.visible = ((!bookGettingSigned) && ((currPage < (bookTotalPages - 1)) || (bookIsUnsigned)));
		buttonPreviousPage.visible = ((!bookGettingSigned) && (currPage > 0));
		buttonDone.visible = ((!bookIsUnsigned) || (!bookGettingSigned));
		if (bookIsUnsigned) {
			buttonSign.visible = (!bookGettingSigned);
			buttonCancel.visible = bookGettingSigned;
			buttonFinalize.visible = bookGettingSigned;
			buttonFinalize.enabled = (bookTitle.trim().length() > 0);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		updateCount += 1;
	}
}
