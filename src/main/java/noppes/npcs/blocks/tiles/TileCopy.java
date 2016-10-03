package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileCopy extends TileEntity {

   public short length = 10;
   public short width = 10;
   public short height = 10;
   public String name = "";


   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.length = compound.getShort("Length");
      this.width = compound.getShort("Width");
      this.height = compound.getShort("Height");
      this.name = compound.getString("Name");
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setShort("Length", this.length);
      compound.setShort("Width", this.width);
      compound.setShort("Height", this.height);
      compound.setString("Name", this.name);
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      NBTTagCompound compound = pkt.getNbtCompound();
      this.length = compound.getShort("Length");
      this.width = compound.getShort("Width");
      this.height = compound.getShort("Height");
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setShort("Length", this.length);
      compound.setShort("Width", this.width);
      compound.setShort("Height", this.height);
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }

   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + this.width + 1), (double)(this.pos.getY() + this.height + 1), (double)(this.pos.getZ() + this.length + 1));
   }
}
