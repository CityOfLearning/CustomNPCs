package noppes.npcs.blocks.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import noppes.npcs.blocks.tiles.TileColorable;

public class TileBanner extends TileColorable {

   public ItemStack icon;
   public long time = 0L;


   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.icon = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("BannerIcon"));
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      if(this.icon != null) {
         compound.setTag("BannerIcon", this.icon.writeToNBT(new NBTTagCompound()));
      }

   }

   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + 2), (double)(this.pos.getZ() + 1));
   }

   public boolean canEdit() {
      return System.currentTimeMillis() - this.time < 10000L;
   }
}
