package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNpcColored extends ItemColored {

   private final Block coloredBlock;


   public ItemNpcColored(Block p_i45332_1_) {
      super(p_i45332_1_, true);
      this.coloredBlock = p_i45332_1_;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int renderPass) {
      return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(stack.getMetadata()));
   }
}
