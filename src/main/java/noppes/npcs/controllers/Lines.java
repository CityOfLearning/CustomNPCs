package noppes.npcs.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.controllers.Line;

public class Lines {

   private static final Random random = new Random();
   private int lastLine = -1;
   public HashMap lines = new HashMap();


   public NBTTagCompound writeToNBT() {
      NBTTagCompound compound = new NBTTagCompound();
      NBTTagList nbttaglist = new NBTTagList();
      Iterator var3 = this.lines.keySet().iterator();

      while(var3.hasNext()) {
         int slot = ((Integer)var3.next()).intValue();
         Line line = (Line)this.lines.get(Integer.valueOf(slot));
         NBTTagCompound nbttagcompound = new NBTTagCompound();
         nbttagcompound.setInteger("Slot", slot);
         nbttagcompound.setString("Line", line.text);
         nbttagcompound.setString("Song", line.sound);
         nbttaglist.appendTag(nbttagcompound);
      }

      compound.setTag("Lines", nbttaglist);
      return compound;
   }

   public void readNBT(NBTTagCompound compound) {
      NBTTagList nbttaglist = compound.getTagList("Lines", 10);
      HashMap map = new HashMap();

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
         Line line = new Line();
         line.text = nbttagcompound.getString("Line");
         line.sound = nbttagcompound.getString("Song");
         map.put(Integer.valueOf(nbttagcompound.getInteger("Slot")), line);
      }

      this.lines = map;
   }

   public Line getLine(boolean isRandom) {
      if(this.lines.isEmpty()) {
         return null;
      } else if(isRandom) {
         ArrayList var3 = new ArrayList(this.lines.values());
         return (Line)var3.get(random.nextInt(var3.size()));
      } else {
         ++this.lastLine;

         while(true) {
            this.lastLine %= 8;
            Line line = (Line)this.lines.get(Integer.valueOf(this.lastLine));
            if(line != null) {
               return line;
            }

            ++this.lastLine;
         }
      }
   }

   public boolean isEmpty() {
      return this.lines.isEmpty();
   }

}
