package noppes.npcs.blocks.tiles;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ITickable;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.blocks.tiles.TileNpcEntity;
import noppes.npcs.controllers.Availability;

public class TileBorder extends TileNpcEntity implements Predicate, ITickable {

   public Availability availability = new Availability();
   public AxisAlignedBB boundingbox;
   public int rotation = 0;
   public int height = 10;
   public String message = "availability.areaNotAvailble";


   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.readExtraNBT(compound);
      if(this.getWorld() != null) {
         this.getWorld().setBlockState(this.getPos(), CustomItems.border.getDefaultState().withProperty(BlockBorder.ROTATION, Integer.valueOf(this.rotation)));
      }

   }

   public void readExtraNBT(NBTTagCompound compound) {
      this.availability.readFromNBT(compound.getCompoundTag("BorderAvailability"));
      this.rotation = compound.getInteger("BorderRotation");
      this.height = compound.getInteger("BorderHeight");
      this.message = compound.getString("BorderMessage");
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      this.writeExtraNBT(compound);
   }

   public void writeExtraNBT(NBTTagCompound compound) {
      compound.setTag("BorderAvailability", this.availability.writeToNBT(new NBTTagCompound()));
      compound.setInteger("BorderRotation", this.rotation);
      compound.setInteger("BorderHeight", this.height);
      compound.setString("BorderMessage", this.message);
   }

   public void update() {
      if(!this.worldObj.isRemote) {
         AxisAlignedBB box = new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + this.height + 1), (double)(this.pos.getZ() + 1));
         List list = this.worldObj.getEntitiesWithinAABB(Entity.class, box, this);
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            Entity entity = (Entity)var3.next();
            if(entity instanceof EntityEnderPearl) {
               EntityEnderPearl player1 = (EntityEnderPearl)entity;
               if(player1.getThrower() instanceof EntityPlayer && !this.availability.isAvailable((EntityPlayer)player1.getThrower())) {
                  entity.isDead = true;
               }
            } else {
               EntityPlayer player = (EntityPlayer)entity;
               if(!this.availability.isAvailable(player)) {
                  BlockPos pos2 = new BlockPos(this.pos);
                  if(this.rotation == 0) {
                     pos2 = pos2.south();
                  } else if(this.rotation == 2) {
                     pos2 = pos2.north();
                  } else if(this.rotation == 1) {
                     pos2 = pos2.east();
                  } else if(this.rotation == 3) {
                     pos2 = pos2.west();
                  }

                  while(!this.worldObj.isAirBlock(pos2)) {
                     pos2 = pos2.up();
                  }

                  player.setPositionAndUpdate((double)pos2.getX() + 0.5D, (double)pos2.getY(), (double)pos2.getZ() + 0.5D);
                  if(!this.message.isEmpty()) {
                     player.addChatComponentMessage(new ChatComponentTranslation(this.message, new Object[0]));
                  }
               }
            }
         }

      }
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      NBTTagCompound compound = pkt.getNbtCompound();
      this.rotation = compound.getInteger("Rotation");
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setInteger("Rotation", this.rotation);
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }

   public boolean isEntityApplicable(Entity var1) {
      return var1 instanceof EntityPlayerMP || var1 instanceof EntityEnderPearl;
   }

   public boolean apply(Object ob) {
      return this.isEntityApplicable((Entity)ob);
   }
}
