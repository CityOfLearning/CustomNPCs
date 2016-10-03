package noppes.npcs.api;

import java.io.File;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IRecipeHandler;

public abstract class NpcAPI {

   private static NpcAPI instance = null;


   public abstract ICustomNpc createNPC(World var1);

   public abstract ICustomNpc spawnNPC(World var1, int var2, int var3, int var4);

   public abstract IEntity getIEntity(Entity var1);

   public abstract IBlock getIBlock(World var1, BlockPos var2);

   public abstract IItemStack getIItemStack(ItemStack var1);

   public abstract IFactionHandler getFactions();

   public abstract IRecipeHandler getRecipes();

   public abstract EventBus events();

   public abstract void registerCommand(CommandNoppesBase var1);

   public abstract File getGlobalDir();

   public abstract File getWorldDir();

   public static boolean IsAvailable() {
      return Loader.isModLoaded("customnpcs");
   }

   public static NpcAPI Instance() {
      if(instance != null) {
         return instance;
      } else if(!IsAvailable()) {
         return null;
      } else {
         try {
            Class e = Class.forName("noppes.npcs.api.wrapper.WrapperNpcAPI");
            instance = (NpcAPI)e.getMethod("Instance", new Class[0]).invoke((Object)null, new Object[0]);
         } catch (Exception var1) {
            var1.printStackTrace();
         }

         return instance;
      }
   }

}
