
package noppes.npcs.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;

public class ItemNpcInterface extends Item implements ItemRenderInterface {
	private boolean damageAble;

	public ItemNpcInterface() {
		damageAble = true;
		setCreativeTab(CustomItems.tab);
	}

	public ItemNpcInterface(int par1) {
		this();
	}

	public boolean consumeItem(EntityPlayer player, Item item) {
		return player.inventory.consumeInventoryItem(item);
	}

	@Override
	public int getItemEnchantability() {
		return super.getItemEnchantability();
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		subItems.add(new ItemStack(itemIn, 1, 0));
	}

	public boolean hasItem(EntityPlayer player, Item item) {
		return player.inventory.hasItem(item);
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving,
			EntityLivingBase par3EntityLiving) {
		if (par2EntityLiving.getHealth() <= 0.0f) {
			return false;
		}
		if (damageAble) {
			par1ItemStack.damageItem(1, par3EntityLiving);
		}
		return true;
	}

	@Override
	public void renderSpecial() {
		GlStateManager.scale(0.66f, 0.66f, 0.66f);
		GlStateManager.translate(0.0f, 0.3f, 0.0f);
	}

	public void setUnDamageable() {
		damageAble = false;
	}

	@Override
	public Item setUnlocalizedName(String name) {
		super.setUnlocalizedName(name);
		GameRegistry.registerItem(this, name);
		if (hasSubtypes) {
			List<ItemStack> list = new ArrayList<>();
			getSubItems(this, null, list);
			for (ItemStack stack : list) {
				CustomNpcs.proxy.registerItem(this, name, stack.getItemDamage());
			}
		} else {
			CustomNpcs.proxy.registerItem(this, name, 0);
		}
		return this;
	}
}
