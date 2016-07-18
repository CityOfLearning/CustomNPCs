//

//

package noppes.npcs.client.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.ModelPony;
import noppes.npcs.client.model.ModelPonyArmor;
import noppes.npcs.entity.EntityNpcPony;

public class RenderNPCPony<T extends EntityNpcPony> extends RenderNPCInterface<T> {
	private ModelPony modelBipedMain;
	private ModelPonyArmor modelArmorChestplate;
	private ModelPonyArmor modelArmor;

	public RenderNPCPony() {
		super(new ModelPony(0.0f), 0.5f);
		this.modelBipedMain = (ModelPony) mainModel;
		this.modelArmorChestplate = new ModelPonyArmor(1.0f);
		this.modelArmor = new ModelPonyArmor(0.5f);
	}

	@Override
	public void doRender(final T pony, final double d, double d1, final double d2, final float f, final float f1) {
		final ItemStack itemstack = pony.getHeldItem();
		final ModelPonyArmor modelArmorChestplate = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor = this.modelArmor;
		final ModelPony modelBipedMain = this.modelBipedMain;
		final boolean heldItemRight;
		final boolean b = heldItemRight = (((itemstack == null) ? 0 : 1) != 0);
		modelBipedMain.heldItemRight = (b ? 1 : 0);
		modelArmor.heldItemRight = (b ? 1 : 0);
		modelArmorChestplate.heldItemRight = (heldItemRight ? 1 : 0);
		final ModelPonyArmor modelArmorChestplate2 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor2 = this.modelArmor;
		final ModelPony modelBipedMain2 = this.modelBipedMain;
		final boolean sneaking = pony.isSneaking();
		modelBipedMain2.isSneak = sneaking;
		modelArmor2.isSneak = sneaking;
		modelArmorChestplate2.isSneak = sneaking;
		final ModelPonyArmor modelArmorChestplate3 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor3 = this.modelArmor;
		final ModelPony modelBipedMain3 = this.modelBipedMain;
		final boolean isRiding = false;
		modelBipedMain3.isRiding = isRiding;
		modelArmor3.isRiding = isRiding;
		modelArmorChestplate3.isRiding = isRiding;
		final ModelPonyArmor modelArmorChestplate4 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor4 = this.modelArmor;
		final ModelPony modelBipedMain4 = this.modelBipedMain;
		final boolean playerSleeping = pony.isPlayerSleeping();
		modelBipedMain4.isSleeping = playerSleeping;
		modelArmor4.isSleeping = playerSleeping;
		modelArmorChestplate4.isSleeping = playerSleeping;
		final ModelPonyArmor modelArmorChestplate5 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor5 = this.modelArmor;
		final ModelPony modelBipedMain5 = this.modelBipedMain;
		final boolean isUnicorn = pony.isUnicorn;
		modelBipedMain5.isUnicorn = isUnicorn;
		modelArmor5.isUnicorn = isUnicorn;
		modelArmorChestplate5.isUnicorn = isUnicorn;
		final ModelPonyArmor modelArmorChestplate6 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor6 = this.modelArmor;
		final ModelPony modelBipedMain6 = this.modelBipedMain;
		final boolean isPegasus = pony.isPegasus;
		modelBipedMain6.isPegasus = isPegasus;
		modelArmor6.isPegasus = isPegasus;
		modelArmorChestplate6.isPegasus = isPegasus;
		if (pony.isSneaking()) {
			d1 -= 0.125;
		}
		super.doRender(pony, d, d1, d2, f, f1);
		final ModelPonyArmor modelArmorChestplate7 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor7 = this.modelArmor;
		final ModelPony modelBipedMain7 = this.modelBipedMain;
		final boolean aimedBow = false;
		modelBipedMain7.aimedBow = aimedBow;
		modelArmor7.aimedBow = aimedBow;
		modelArmorChestplate7.aimedBow = aimedBow;
		final ModelPonyArmor modelArmorChestplate8 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor8 = this.modelArmor;
		final ModelPony modelBipedMain8 = this.modelBipedMain;
		final boolean isRiding2 = false;
		modelBipedMain8.isRiding = isRiding2;
		modelArmor8.isRiding = isRiding2;
		modelArmorChestplate8.isRiding = isRiding2;
		final ModelPonyArmor modelArmorChestplate9 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor9 = this.modelArmor;
		final ModelPony modelBipedMain9 = this.modelBipedMain;
		final boolean isSneak = false;
		modelBipedMain9.isSneak = isSneak;
		modelArmor9.isSneak = isSneak;
		modelArmorChestplate9.isSneak = isSneak;
		final ModelPonyArmor modelArmorChestplate10 = this.modelArmorChestplate;
		final ModelPonyArmor modelArmor10 = this.modelArmor;
		final ModelPony modelBipedMain10 = this.modelBipedMain;
		final boolean heldItemRight2 = false;
		modelBipedMain10.heldItemRight = (heldItemRight2 ? 1 : 0);
		modelArmor10.heldItemRight = (heldItemRight2 ? 1 : 0);
		modelArmorChestplate10.heldItemRight = (heldItemRight2 ? 1 : 0);
	}

	@Override
	public ResourceLocation getEntityTexture(final T pony) {
		final boolean check = (pony.textureLocation == null) || (pony.textureLocation != pony.checked);
		final ResourceLocation loc = super.getEntityTexture(pony);
		if (check) {
			try {
				final IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(loc);
				final BufferedImage bufferedimage = ImageIO.read(resource.getInputStream());
				pony.isPegasus = false;
				pony.isUnicorn = false;
				final Color color = new Color(bufferedimage.getRGB(0, 0), true);
				final Color color2 = new Color(249, 177, 49, 255);
				final Color color3 = new Color(136, 202, 240, 255);
				final Color color4 = new Color(209, 159, 228, 255);
				final Color color5 = new Color(254, 249, 252, 255);
				if (color.equals(color2)) {
				}
				if (color.equals(color3)) {
					pony.isPegasus = true;
				}
				if (color.equals(color4)) {
					pony.isUnicorn = true;
				}
				if (color.equals(color5)) {
					pony.isPegasus = true;
					pony.isUnicorn = true;
				}
				pony.checked = loc;
			} catch (IOException ex) {
			}
		}
		return loc;
	}
}
