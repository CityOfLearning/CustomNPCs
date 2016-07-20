//

//

package noppes.npcs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTabNpcs extends CreativeTabs {
	public Item item;
	public int meta;

	public CreativeTabNpcs(String label) {
		super(label);
		item = Items.bowl;
		meta = 0;
	}

	@Override
	public int getIconItemDamage() {
		return meta;
	}

	@Override
	public Item getTabIconItem() {
		return item;
	}
}
