package noppes.npcs.controllers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import noppes.npcs.controllers.TransportCategory;

public class TransportLocation {

   public int id = -1;
   public String name = "default name";
   public BlockPos pos;
   public int type = 0;
   public int dimension = 0;
   public TransportCategory category;


   public void readNBT(NBTTagCompound compound) {
      if(compound != null) {
         this.id = compound.getInteger("Id");
         this.pos = new BlockPos(compound.getDouble("PosX"), compound.getDouble("PosY"), compound.getDouble("PosZ"));
         this.type = compound.getInteger("Type");
         this.dimension = compound.getInteger("Dimension");
         this.name = compound.getString("Name");
      }
   }

   public NBTTagCompound writeNBT() {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setInteger("Id", this.id);
      compound.setDouble("PosX", (double)this.pos.getX());
      compound.setDouble("PosY", (double)this.pos.getY());
      compound.setDouble("PosZ", (double)this.pos.getZ());
      compound.setInteger("Type", this.type);
      compound.setInteger("Dimension", this.dimension);
      compound.setString("Name", this.name);
      return compound;
   }

   public boolean isDefault() {
      return this.type == 1;
   }
}
