package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;

public class GuiBlockCopy extends GuiNPCInterface implements IGuiData, ITextfieldListener {

   private int x;
   private int y;
   private int z;
   private TileCopy tile;


   public GuiBlockCopy(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.setBackground("menubg.png");
      this.xSize = 256;
      this.ySize = 216;
      this.closeOnEsc = true;
      this.tile = (TileCopy)this.player.worldObj.getTileEntity(new BlockPos(x, y, z));
      Client.sendData(EnumPacketServer.GetTileEntity, new Object[]{Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
   }

   public void initPacket() {}

   public void initGui() {
      super.initGui();
      int y = this.guiTop + 4;
      this.addTextField(new GuiNpcTextField(0, this, this.guiLeft + 104, y, 50, 20, this.tile.height + ""));
      this.addLabel(new GuiNpcLabel(0, "schematic.height", this.guiLeft + 5, y + 5));
      this.getTextField(0).numbersOnly = true;
      this.getTextField(0).setMinMaxDefault(0, 100, 10);
      GuiNpcTextField var10001 = new GuiNpcTextField;
      int var10005 = this.guiLeft + 104;
      y += 23;
      var10001.<init>(1, this, var10005, y, 50, 20, this.tile.width + "");
      this.addTextField(var10001);
      this.addLabel(new GuiNpcLabel(1, "schematic.width", this.guiLeft + 5, y + 5));
      this.getTextField(1).numbersOnly = true;
      this.getTextField(1).setMinMaxDefault(0, 100, 10);
      var10001 = new GuiNpcTextField;
      var10005 = this.guiLeft + 104;
      y += 23;
      var10001.<init>(2, this, var10005, y, 50, 20, this.tile.length + "");
      this.addTextField(var10001);
      this.addLabel(new GuiNpcLabel(2, "schematic.length", this.guiLeft + 5, y + 5));
      this.getTextField(2).numbersOnly = true;
      this.getTextField(2).setMinMaxDefault(0, 100, 10);
      var10001 = new GuiNpcTextField;
      var10005 = this.guiLeft + 104;
      y += 23;
      var10001.<init>(5, this, var10005, y, 100, 20, "");
      this.addTextField(var10001);
      this.addLabel(new GuiNpcLabel(5, "gui.name", this.guiLeft + 5, y + 5));
      GuiNpcButton var2 = new GuiNpcButton;
      int var10004 = this.guiLeft + 5;
      y += 30;
      var2.<init>(0, var10004, y, 60, 20, "gui.save");
      this.addButton(var2);
      this.addButton(new GuiNpcButton(1, this.guiLeft + 67, y, 60, 20, "gui.cancel"));
   }

   protected void actionPerformed(GuiButton guibutton) {
      if(guibutton.id == 0) {
         NBTTagCompound compound = new NBTTagCompound();
         this.tile.writeToNBT(compound);
         Client.sendData(EnumPacketServer.SchematicStore, new Object[]{this.getTextField(5).getText(), compound});
      }

      this.close();
   }

   public void save() {
      NBTTagCompound compound = new NBTTagCompound();
      this.tile.writeToNBT(compound);
      Client.sendData(EnumPacketServer.SaveTileEntity, new Object[]{compound});
   }

   public void setGuiData(NBTTagCompound compound) {
      this.tile.readFromNBT(compound);
      this.initGui();
   }

   public void unFocused(GuiNpcTextField textfield) {
      if(textfield.id == 0) {
         this.tile.height = (short)textfield.getInteger();
      }

      if(textfield.id == 1) {
         this.tile.width = (short)textfield.getInteger();
      }

      if(textfield.id == 2) {
         this.tile.length = (short)textfield.getInteger();
      }

   }
}
