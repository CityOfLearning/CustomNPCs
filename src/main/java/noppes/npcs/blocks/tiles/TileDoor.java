package noppes.npcs.blocks.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileNpcEntity;

public class TileDoor extends TileNpcEntity implements ITickable {

   public int ticksExisted = 0;
   public Block blockModel;
   public boolean needsClientUpdate;
   public TileEntity renderTile;
   public boolean renderTileErrored;
   public ITickable renderTileUpdate;


   public TileDoor() {
      this.blockModel = CustomItems.scriptedDoor;
      this.needsClientUpdate = false;
      this.renderTileErrored = true;
      this.renderTileUpdate = null;
   }

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
      return oldState.getBlock() != newState.getBlock();
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.setDoorNBT(compound);
   }

   public void setDoorNBT(NBTTagCompound compound) {
      this.blockModel = (Block)Block.blockRegistry.getObject(new ResourceLocation(compound.getString("ScriptDoorBlockModel")));
      if(this.blockModel == null || !(this.blockModel instanceof BlockDoor)) {
         this.blockModel = CustomItems.scriptedDoor;
      }

      this.renderTileUpdate = null;
      this.renderTile = null;
      this.renderTileErrored = false;
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      this.getDoorNBT(compound);
   }

   public NBTTagCompound getDoorNBT(NBTTagCompound compound) {
      compound.setString("ScriptDoorBlockModel", Block.blockRegistry.getNameForObject(this.blockModel) + "");
      return compound;
   }

   public void setItemModel(Block block) {
      if(block == null || !(block instanceof BlockDoor)) {
         block = CustomItems.scriptedDoor;
      }

      if(this.blockModel != block) {
         this.blockModel = block;
         this.needsClientUpdate = true;
      }
   }

   public void update() {
      if(this.renderTileUpdate != null) {
         try {
            this.renderTileUpdate.update();
         } catch (Exception var2) {
            this.renderTileUpdate = null;
         }
      }

      ++this.ticksExisted;
      if(this.ticksExisted >= 10) {
         this.ticksExisted = 0;
         if(this.needsClientUpdate) {
            this.worldObj.markBlockForUpdate(this.pos);
            this.needsClientUpdate = false;
         }
      }

   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      this.setDoorNBT(pkt.getNbtCompound());
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound compound = new NBTTagCompound();
      this.getDoorNBT(compound);
      S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(this.pos, 0, compound);
      return packet;
   }
}
