//

//

package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;

public class ItemMusic extends ItemNpcInterface {
	private boolean shouldRotate;

	public ItemMusic() {
		shouldRotate = false;
		setCreativeTab(CustomItems.tab);
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World, final EntityPlayer player) {
		if (par2World.isRemote) {
			return par1ItemStack;
		}
		final int note = par2World.rand.nextInt(24);
		final float var7 = (float) Math.pow(2.0, (note - 12) / 12.0);
		final String var8 = "harp";
		par2World.playSoundEffect(player.posX, player.posY, player.posZ, "note." + var8, 3.0f, var7);
		par2World.spawnParticle(EnumParticleTypes.NOTE, player.posY, player.posY + 1.2, player.posY, note / 24.0, 0.0,
				0.0, new int[0]);
		return par1ItemStack;
	}

	public Item setRotated() {
		shouldRotate = true;
		return this;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return shouldRotate;
	}
}
