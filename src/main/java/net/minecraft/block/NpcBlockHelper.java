package net.minecraft.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

public final class NpcBlockHelper {

   public static Item GetCrop(BlockCrops crops) {
      return crops.getCrop();
   }
}
