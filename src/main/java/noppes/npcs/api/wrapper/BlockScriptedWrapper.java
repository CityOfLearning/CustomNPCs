package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.wrapper.BlockWrapper;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.blocks.tiles.TileScripted;

public class BlockScriptedWrapper extends BlockWrapper implements IBlockScripted {

   private TileScripted tile;


   public BlockScriptedWrapper(World world, Block block, BlockPos pos) {
      super(world, block, pos);
      this.tile = (TileScripted)super.tile;
   }

   public void setModel(IItemStack item) {
      if(item == null) {
         this.tile.setItemModel((ItemStack)null);
      } else {
         this.tile.setItemModel(item.getMCItemStack());
      }

   }

   public void setModel(String name) {
      if(name == null) {
         this.tile.setItemModel((ItemStack)null);
      } else {
         Item item = (Item)Item.itemRegistry.getObject(new ResourceLocation(name));
         if(item == null) {
            this.tile.setItemModel((ItemStack)null);
         } else {
            this.tile.setItemModel(new ItemStack(item));
         }
      }

   }

   public IItemStack getModel() {
      return new ItemStackWrapper(this.tile.itemModel);
   }

   public void setRedstonePower(int strength) {
      this.tile.setRedstonePower(strength);
   }

   public int getRedstonePower() {
      return this.tile.powering;
   }

   public void setIsLadder(boolean bo) {
      this.tile.isLadder = bo;
      this.tile.needsClientUpdate = true;
   }

   public boolean getIsLadder() {
      return this.tile.isLadder;
   }

   public void setLight(int value) {
      this.tile.setLightValue(value);
   }

   public int getLight() {
      return this.tile.lightValue;
   }

   public void setScale(float x, float y, float z) {
      this.tile.setScale(x, y, z);
   }

   public float getScaleX() {
      return this.tile.scaleX;
   }

   public float getScaleY() {
      return this.tile.scaleY;
   }

   public float getScaleZ() {
      return this.tile.scaleZ;
   }

   public void setRotation(int x, int y, int z) {
      this.tile.setRotation(x % 360, y % 360, z % 360);
   }

   public int getRotationX() {
      return this.tile.rotationX;
   }

   public int getRotationY() {
      return this.tile.rotationY;
   }

   public int getRotationZ() {
      return this.tile.rotationZ;
   }

   public void executeCommand(String command) {
      NoppesUtilServer.runCommand(this.tile.getWorld(), this.tile.getPos(), "ScriptBlock: " + this.tile.getPos(), command, (EntityPlayer)null);
   }

   public ITimers getTimers() {
      return this.tile.timers;
   }
}
