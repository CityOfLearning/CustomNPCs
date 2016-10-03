package noppes.npcs.blocks.tiles;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileNpcEntity extends TileEntity {

   public Map tempData = new HashMap();
   public NBTTagCompound extraData = new NBTTagCompound();


   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.extraData = compound.getCompoundTag("ExtraData");
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setTag("ExtraData", this.extraData);
   }
}
