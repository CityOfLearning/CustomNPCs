
package noppes.npcs.client.gui.player;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiClose;
import noppes.npcs.client.gui.util.IGuiError;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.mail.PlayerMail;
import noppes.npcs.util.NoppesUtilPlayer;

@SideOnly(Side.CLIENT)
public class GuiMailmanWrite extends GuiContainerNPCInterface
		implements ITextfieldListener, IGuiError, IGuiClose, GuiYesNoCallback {
	private static ResourceLocation bookGuiTextures;
	private static ResourceLocation bookWidgets;
	private static ResourceLocation bookInventory;
	public static GuiScreen parent;
	public static PlayerMail mail;
	static {
		bookGuiTextures = new ResourceLocation("textures/gui/book.png");
		bookWidgets = new ResourceLocation("textures/gui/widgets.png");
		bookInventory = new ResourceLocation("textures/gui/container/inventory.png");
		GuiMailmanWrite.mail = new PlayerMail();
	}
	private int updateCount;
	private int bookImageWidth;
	private int bookImageHeight;
	private int bookTotalPages;
	private int currPage;
	private NBTTagList bookPages;
	private GuiButtonNextPage buttonNextPage;
	private GuiButtonNextPage buttonPreviousPage;
	private boolean canEdit;
	private boolean canSend;
	private Minecraft mc;
	private String username;

	private GuiNpcLabel error;

	public GuiMailmanWrite(ContainerMail container, boolean canEdit, boolean canSend) {
		super(null, container);
		bookImageWidth = 192;
		bookImageHeight = 192;
		bookTotalPages = 1;
		mc = Minecraft.getMinecraft();
		username = "";
		title = "";
		this.canEdit = canEdit;
		this.canSend = canSend;
		if (GuiMailmanWrite.mail.message.hasKey("pages")) {
			bookPages = GuiMailmanWrite.mail.message.getTagList("pages", 8);
		}
		if (bookPages != null) {
			bookPages = (NBTTagList) bookPages.copy();
			bookTotalPages = bookPages.tagCount();
			if (bookTotalPages < 1) {
				bookTotalPages = 1;
			}
		} else {
			(bookPages = new NBTTagList()).appendTag(new NBTTagString(""));
			bookTotalPages = 1;
		}
		xSize = 360;
		ySize = 260;
		drawDefaultBackground = false;
		closeOnEsc = true;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			int id = par1GuiButton.id;
			if (id == 0) {
				GuiMailmanWrite.mail.message.setTag("pages", bookPages);
				if (canSend) {
					NoppesUtilPlayer.sendData(EnumPlayerPacket.MailSend, username, GuiMailmanWrite.mail.writeNBT());
				} else {
					close();
				}
			}
			if (id == 3) {
				close();
			}
			if (id == 4) {
				GuiYesNo guiyesno = new GuiYesNo(this, "Confirm", StatCollector.translateToLocal("gui.delete"), 0);
				displayGuiScreen(guiyesno);
			} else if (id == 1) {
				if (currPage < (bookTotalPages - 1)) {
					++currPage;
				} else if (canEdit) {
					addNewPage();
					if (currPage < (bookTotalPages - 1)) {
						++currPage;
					}
				}
			} else if ((id == 2) && (currPage > 0)) {
				--currPage;
			}
			updateButtons();
		}
	}

	private void addNewPage() {
		if ((bookPages != null) && (bookPages.tagCount() < 50)) {
			bookPages.appendTag(new NBTTagString(""));
			++bookTotalPages;
		}
	}

	@Override
	public void close() {
		mc.displayGuiScreen(GuiMailmanWrite.parent);
		GuiMailmanWrite.parent = null;
		GuiMailmanWrite.mail = new PlayerMail();
	}

	@Override
	public void confirmClicked(boolean flag, int i) {
		if (flag) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.MailDelete, GuiMailmanWrite.mail.time,
					GuiMailmanWrite.mail.sender);
			close();
		} else {
			NoppesUtil.openGUI(player, this);
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawWorldBackground(0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(GuiMailmanWrite.bookGuiTextures);
		this.drawTexturedModalRect(guiLeft + 130, guiTop + 22, 0, 0, bookImageWidth, bookImageHeight / 3);
		this.drawTexturedModalRect(guiLeft + 130, guiTop + 22 + (bookImageHeight / 3), 0, bookImageHeight / 2,
				bookImageWidth, bookImageHeight / 2);
		this.drawTexturedModalRect(guiLeft, guiTop + 2, 0, 0, bookImageWidth, bookImageHeight);
		mc.getTextureManager().bindTexture(GuiMailmanWrite.bookInventory);
		this.drawTexturedModalRect(guiLeft + 20, guiTop + 173, 0, 82, 180, 55);
		this.drawTexturedModalRect(guiLeft + 20, guiTop + 228, 0, 140, 180, 28);
		String s = I18n.format("book.pageIndicator", new Object[] { currPage + 1, bookTotalPages });
		String s2 = "";
		if ((bookPages != null) && (currPage >= 0) && (currPage < bookPages.tagCount())) {
			s2 = bookPages.getStringTagAt(currPage);
		}
		if (canEdit) {
			if (mc.fontRendererObj.getBidiFlag()) {
				s2 += "_";
			} else if (((updateCount / 6) % 2) == 0) {
				s2 = s2 + "" + EnumChatFormatting.BLACK + "_";
			} else {
				s2 = s2 + "" + EnumChatFormatting.GRAY + "_";
			}
		}
		int l = mc.fontRendererObj.getStringWidth(s);
		mc.fontRendererObj.drawString(s, ((guiLeft - l) + bookImageWidth) - 44, guiTop + 18, 0);
		mc.fontRendererObj.drawSplitString(s2, guiLeft + 36, guiTop + 18 + 16, 116, 0);
		drawGradientRect(guiLeft + 175, guiTop + 136, guiLeft + 269, guiTop + 154, -1072689136, -804253680);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(GuiMailmanWrite.bookWidgets);
		for (int i = 0; i < 4; ++i) {
			this.drawTexturedModalRect(guiLeft + 175 + (i * 24), guiTop + 134, 0, 22, 24, 24);
		}
		super.drawScreen(par1, par2, par3);
	}

	private String func_74158_i() {
		if ((bookPages != null) && (currPage >= 0) && (currPage < bookPages.tagCount())) {
			return bookPages.getStringTagAt(currPage);
		}
		return "";
	}

	private void func_74159_a(String par1Str) {
		if ((bookPages != null) && (currPage >= 0) && (currPage < bookPages.tagCount())) {
			bookPages.set(currPage, new NBTTagString(par1Str));
		}
	}

	private void func_74160_b(String par1Str) {
		String s1 = func_74158_i();
		String s2 = s1 + par1Str;
		int i = mc.fontRendererObj.splitStringWidth(s2 + "" + EnumChatFormatting.BLACK + "_", 118);
		if ((i <= 118) && (s2.length() < 256)) {
			func_74159_a(s2);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		if (canEdit && !canSend) {
			addLabel(new GuiNpcLabel(0, "mailbox.sender", guiLeft + 170, guiTop + 32, 0));
		} else {
			addLabel(new GuiNpcLabel(0, "mailbox.username", guiLeft + 170, guiTop + 32, 0));
		}
		if (canEdit && !canSend) {
			addTextField(new GuiNpcTextField(2, this, fontRendererObj, guiLeft + 170, guiTop + 42, 114, 20,
					GuiMailmanWrite.mail.sender));
		} else if (canEdit) {
			addTextField(new GuiNpcTextField(0, this, fontRendererObj, guiLeft + 170, guiTop + 42, 114, 20, username));
		} else {
			addLabel(new GuiNpcLabel(10, GuiMailmanWrite.mail.sender, guiLeft + 170, guiTop + 42, 0));
		}
		addLabel(new GuiNpcLabel(1, "mailbox.subject", guiLeft + 170, guiTop + 72, 0));
		if (canEdit) {
			addTextField(new GuiNpcTextField(1, this, fontRendererObj, guiLeft + 170, guiTop + 82, 114, 20,
					GuiMailmanWrite.mail.subject));
		} else {
			addLabel(new GuiNpcLabel(11, GuiMailmanWrite.mail.subject, guiLeft + 170, guiTop + 82, 0));
		}
		addLabel(error = new GuiNpcLabel(2, "", guiLeft + 170, guiTop + 114, 16711680));
		if (canEdit && !canSend) {
			addButton(new GuiNpcButton(0, guiLeft + 200, guiTop + 171, 60, 20, "gui.done"));
		} else if (canEdit) {
			addButton(new GuiNpcButton(0, guiLeft + 200, guiTop + 171, 60, 20, "mailbox.send"));
		}
		if (!canEdit && !canSend) {
			addButton(new GuiNpcButton(4, guiLeft + 200, guiTop + 171, 60, 20, "selectWorld.deleteButton"));
		}
		if (!canEdit || canSend) {
			addButton(new GuiNpcButton(3, guiLeft + 200, guiTop + 194, 60, 20, "gui.cancel"));
		}
		buttonList.add(buttonNextPage = new GuiButtonNextPage(1, guiLeft + 120, guiTop + 156, true));
		buttonList.add(buttonPreviousPage = new GuiButtonNextPage(2, guiLeft + 38, guiTop + 156, false));
		updateButtons();
	}

	@Override
	public void keyTyped(char par1, int par2) {
		if (!GuiNpcTextField.isActive() && canEdit) {
			keyTypedInBook(par1, par2);
		} else {
			super.keyTyped(par1, par2);
		}
	}

	private void keyTypedInBook(char par1, int par2) {
		switch (par1) {
		case '\u0016': {
			func_74160_b(GuiScreen.getClipboardString());
		}
		default: {
			switch (par2) {
			case 14: {
				String s = func_74158_i();
				if (s.length() > 0) {
					func_74159_a(s.substring(0, s.length() - 1));
				}
				return;
			}
			case 28:
			case 156: {
				func_74160_b("\n");
				return;
			}
			default: {
				if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
					func_74160_b(Character.toString(par1));
				}
				return;
			}
			}
		}
		}
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void save() {
	}

	@Override
	public void setClose(int i, NBTTagCompound data) {
		player.addChatMessage(
				new ChatComponentTranslation("mailbox.succes", new Object[] { data.getString("username") }));
	}

	@Override
	public void setError(int i, NBTTagCompound data) {
		if (i == 0) {
			error.label = StatCollector.translateToLocal("mailbox.errorUsername");
		}
		if (i == 1) {
			error.label = StatCollector.translateToLocal("mailbox.errorSubject");
		}
	}

	@Override
	public void unFocused(GuiNpcTextField textField) {
		if (textField.id == 0) {
			username = textField.getText();
		}
		if (textField.id == 1) {
			GuiMailmanWrite.mail.subject = textField.getText();
		}
		if (textField.id == 2) {
			GuiMailmanWrite.mail.sender = textField.getText();
		}
	}

	private void updateButtons() {
		buttonNextPage.setVisible((currPage < (bookTotalPages - 1)) || canEdit);
		buttonPreviousPage.setVisible(currPage > 0);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		++updateCount;
	}
}
