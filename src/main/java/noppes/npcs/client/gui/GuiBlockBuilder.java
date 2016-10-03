package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import noppes.npcs.Schematic;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiBlockBuilder extends GuiNPCInterface implements IGuiData, ICustomScrollListener, IScrollData {

   private int x;
   private int y;
   private int z;
   private TileBuilder tile;
   private GuiCustomScroll scroll;
   private Schematic selected = null;


   public GuiBlockBuilder(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.setBackground("menubg.png");
      this.xSize = 256;
      this.ySize = 216;
      this.closeOnEsc = true;
      this.tile = (TileBuilder)this.player.worldObj.getTileEntity(new BlockPos(x, y, z));
   }

   public void initPacket() {
      Client.sendData(EnumPacketServer.SchematicsTile, new Object[]{Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z)});
   }

   public void initGui() {
      super.initGui();
      if(this.scroll == null) {
         this.scroll = new GuiCustomScroll(this, 0);
         this.scroll.setSize(125, 208);
      }

      this.scroll.guiLeft = this.guiLeft + 4;
      this.scroll.guiTop = this.guiTop + 4;
      this.addScroll(this.scroll);
      if(this.selected != null) {
         int y = this.guiTop + 4;
         if(this.selected.size < 125000) {
            this.addButton(new GuiNpcButtonYesNo(3, this.guiLeft + 200, y, TileBuilder.DrawPos != null && this.tile.getPos().equals(TileBuilder.DrawPos)));
            this.addLabel(new GuiNpcLabel(3, "schematic.preview", this.guiLeft + 130, y + 5));
         }

         GuiNpcLabel var10001 = new GuiNpcLabel;
         String var10004 = StatCollector.translateToLocal("schematic.width") + ": " + this.selected.width;
         int var10005 = this.guiLeft + 130;
         y += 22;
         var10001.<init>(0, var10004, var10005, y);
         this.addLabel(var10001);
         var10001 = new GuiNpcLabel;
         var10004 = StatCollector.translateToLocal("schematic.length") + ": " + this.selected.length;
         var10005 = this.guiLeft + 130;
         y += 12;
         var10001.<init>(1, var10004, var10005, y);
         this.addLabel(var10001);
         var10001 = new GuiNpcLabel;
         var10004 = StatCollector.translateToLocal("schematic.height") + ": " + this.selected.height;
         var10005 = this.guiLeft + 130;
         y += 12;
         var10001.<init>(2, var10004, var10005, y);
         this.addLabel(var10001);
         GuiNpcButtonYesNo var2 = new GuiNpcButtonYesNo;
         int var5 = this.guiLeft + 200;
         y += 16;
         var2.<init>(4, var5, y, this.tile.enabled);
         this.addButton(var2);
         this.addLabel(new GuiNpcLabel(4, StatCollector.translateToLocal("gui.enabled"), this.guiLeft + 130, y + 5));
         var2 = new GuiNpcButtonYesNo;
         var5 = this.guiLeft + 200;
         y += 23;
         var2.<init>(7, var5, y, this.tile.finished);
         this.addButton(var2);
         this.addLabel(new GuiNpcLabel(7, StatCollector.translateToLocal("gui.finished"), this.guiLeft + 130, y + 5));
         var2 = new GuiNpcButtonYesNo;
         var5 = this.guiLeft + 200;
         y += 23;
         var2.<init>(8, var5, y, this.tile.started);
         this.addButton(var2);
         this.addLabel(new GuiNpcLabel(8, StatCollector.translateToLocal("gui.started"), this.guiLeft + 130, y + 5));
         GuiNpcTextField var3 = new GuiNpcTextField;
         var10005 = this.guiLeft + 200;
         y += 23;
         var3.<init>(9, this, var10005, y, 50, 20, this.tile.yOffest + "");
         this.addTextField(var3);
         this.addLabel(new GuiNpcLabel(9, StatCollector.translateToLocal("gui.yoffset"), this.guiLeft + 130, y + 5));
         this.getTextField(9).numbersOnly = true;
         this.getTextField(9).setMinMaxDefault(-10, 10, 0);
         GuiNpcButton var4 = new GuiNpcButton;
         var5 = this.guiLeft + 200;
         y += 23;
         var4.<init>(5, var5, y, 50, 20, new String[]{"0", "90", "180", "270"}, this.tile.rotation);
         this.addButton(var4);
         this.addLabel(new GuiNpcLabel(5, StatCollector.translateToLocal("movement.rotation"), this.guiLeft + 130, y + 5));
         var4 = new GuiNpcButton;
         var5 = this.guiLeft + 130;
         y += 22;
         var4.<init>(6, var5, y, 120, 20, "availability.options");
         this.addButton(var4);
      }

   }

   protected void actionPerformed(GuiButton guibutton) {
      if(guibutton.id == 3) {
         GuiNpcButtonYesNo button = (GuiNpcButtonYesNo)guibutton;
         if(button.getBoolean()) {
            TileBuilder.SetDrawPos(new BlockPos(this.x, this.y, this.z));
            this.tile.setDrawSchematic(this.selected);
         } else {
            TileBuilder.SetDrawPos((BlockPos)null);
            this.tile.setDrawSchematic((Schematic)null);
         }
      }

      if(guibutton.id == 4) {
         this.tile.enabled = ((GuiNpcButtonYesNo)guibutton).getBoolean();
      }

      if(guibutton.id == 5) {
         this.tile.rotation = ((GuiNpcButton)guibutton).getValue();
      }

      if(guibutton.id == 6) {
         this.setSubGui(new SubGuiNpcAvailability(this.tile.availability));
      }

      if(guibutton.id == 7) {
         this.tile.finished = ((GuiNpcButtonYesNo)guibutton).getBoolean();
         Client.sendData(EnumPacketServer.SchematicsSet, new Object[]{Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), this.scroll.getSelected()});
      }

      if(guibutton.id == 8) {
         this.tile.started = ((GuiNpcButtonYesNo)guibutton).getBoolean();
      }

   }

   public void save() {
      if(this.getTextField(9) != null) {
         this.tile.yOffest = this.getTextField(9).getInteger();
      }

      Client.sendData(EnumPacketServer.SchematicsTileSave, new Object[]{Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), this.tile.writePartNBT(new NBTTagCompound())});
   }

   public void setGuiData(NBTTagCompound compound) {
      if(compound.hasKey("Width")) {
         this.selected = new Schematic(compound.getString("SchematicName"));
         this.selected.load(compound);
         if(TileBuilder.DrawPos != null && TileBuilder.DrawPos.equals(this.tile.getPos())) {
            this.tile.setDrawSchematic(this.selected);
         }

         this.scroll.setSelected(this.selected.name);
         this.scroll.scrollTo(this.selected.name);
      } else {
         this.tile.readPartNBT(compound);
      }

      this.initGui();
   }

   public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
      if(scroll.hasSelected()) {
         if(this.selected != null) {
            this.getButton(3).setDisplay(0);
         }

         TileBuilder.SetDrawPos((BlockPos)null);
         this.tile.setDrawSchematic((Schematic)null);
         Client.sendData(EnumPacketServer.SchematicsSet, new Object[]{Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), scroll.getSelected()});
      }
   }

   public void setData(Vector list, HashMap data) {
      this.scroll.setList(list);
      if(this.selected != null) {
         this.scroll.setSelected(this.selected.name);
      }

      this.initGui();
   }

   public void setSelected(String selected) {}
}
