package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import noppes.npcs.blocks.tiles.TileNpcContainer;

public class TilePedestal extends TileNpcContainer {

   public String getName() {
      return "tile.npcPedestal.name";
   }

   public int getSizeInventory() {
      return 1;
   }

   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + 2), (double)(this.pos.getZ() + 1));
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      this.writeToNBT(compound);
      compound.removeTag("ExtraData");
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }

   public int powerProvided() {
      return this.getStackInSlot(0) == null?0:15;
   }
}
