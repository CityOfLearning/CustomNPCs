package noppes.npcs.blocks.tiles;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import noppes.npcs.blocks.tiles.TileNpcContainer;

public class TileWeaponRack extends TileNpcContainer {

   public boolean isItemValidForSlot(int var1, ItemStack itemstack) {
      return itemstack != null && itemstack.getItem() instanceof ItemBlock?false:super.isItemValidForSlot(var1, itemstack);
   }

   public int getSizeInventory() {
      return 3;
   }

   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + 2), (double)(this.pos.getZ() + 1));
   }

   public String getName() {
      return "tile.npcWeaponRack.name";
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      this.writeToNBT(compound);
      compound.removeTag("ExtraData");
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }

   public int powerProvided() {
      int power = 0;

      for(int i = 0; i < 3; ++i) {
         if(this.getStackInSlot(i) != null) {
            power += 5;
         }
      }

      return power;
   }
}
