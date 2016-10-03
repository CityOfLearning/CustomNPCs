package noppes.npcs.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.entity.EntityNPCInterface;

public class MassBlockController {

   private static Queue queue;
   private static MassBlockController Instance;


   public MassBlockController() {
      queue = new LinkedList();
      Instance = this;
   }

   public static void Update() {
      MassBlockController var10000 = Instance;
      if(!queue.isEmpty()) {
         MassBlockController.IMassBlock imb = (MassBlockController.IMassBlock)queue.remove();
         World world = imb.getNpc().worldObj;
         BlockPos pos = imb.getNpc().getPosition();
         int range = imb.getRange();
         ArrayList list = new ArrayList();

         for(int x = -range; x < range; ++x) {
            for(int z = -range; z < range; ++z) {
               if(world.isBlockLoaded(new BlockPos(x + pos.getX(), 64, z + pos.getZ()))) {
                  for(int y = 0; y < range; ++y) {
                     BlockPos blockPos = pos.add(x, y - range / 2, z);
                     list.add(new BlockData(blockPos, world.getBlockState(blockPos), (NBTTagCompound)null));
                  }
               }
            }
         }

         imb.processed(list);
      }
   }

   public static void Queue(MassBlockController.IMassBlock imb) {
      MassBlockController var10000 = Instance;
      queue.add(imb);
   }

   public interface IMassBlock {

      EntityNPCInterface getNpc();

      int getRange();

      void processed(List var1);
   }
}
