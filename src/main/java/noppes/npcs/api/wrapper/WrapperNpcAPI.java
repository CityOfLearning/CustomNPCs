package noppes.npcs.api.wrapper;

import java.io.File;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.wrapper.AnimalWrapper;
import noppes.npcs.api.wrapper.BlockFluidContainerWrapper;
import noppes.npcs.api.wrapper.BlockScriptedDoorWrapper;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.BlockWrapper;
import noppes.npcs.api.wrapper.EntityLivingBaseWrapper;
import noppes.npcs.api.wrapper.EntityLivingWrapper;
import noppes.npcs.api.wrapper.EntityWrapper;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.api.wrapper.MonsterWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WrapperEntityData;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class WrapperNpcAPI extends NpcAPI {

   public static final EventBus EVENT_BUS = new EventBus();
   private static final String key = "NPCWrappedObject";
   private static NpcAPI instance = null;


   public IEntity getIEntity(Entity entity) {
      if(entity != null && !entity.worldObj.isRemote) {
         if(entity instanceof EntityNPCInterface) {
            return ((EntityNPCInterface)entity).wrappedNPC;
         } else {
            WrapperEntityData data = (WrapperEntityData)entity.getExtendedProperties("NPCWrappedObject");
            if(data != null) {
               return data.base;
            } else {
               if(entity instanceof EntityPlayerMP) {
                  data = new WrapperEntityData(new PlayerWrapper((EntityPlayerMP)entity));
               } else if(entity instanceof EntityAnimal) {
                  data = new WrapperEntityData(new AnimalWrapper((EntityAnimal)entity));
               } else if(entity instanceof EntityMob) {
                  data = new WrapperEntityData(new MonsterWrapper((EntityMob)entity));
               } else if(entity instanceof EntityLiving) {
                  data = new WrapperEntityData(new EntityLivingWrapper((EntityLiving)entity));
               } else if(entity instanceof EntityLivingBase) {
                  data = new WrapperEntityData(new EntityLivingBaseWrapper((EntityLivingBase)entity));
               } else {
                  data = new WrapperEntityData(new EntityWrapper(entity));
               }

               entity.registerExtendedProperties("NPCWrappedObject", data);
               return data.base;
            }
         }
      } else {
         return null;
      }
   }

   public ICustomNpc createNPC(World world) {
      if(world.isRemote) {
         return null;
      } else {
         EntityCustomNpc npc = new EntityCustomNpc(world);
         return npc.wrappedNPC;
      }
   }

   public ICustomNpc spawnNPC(World world, int x, int y, int z) {
      if(world.isRemote) {
         return null;
      } else {
         EntityCustomNpc npc = new EntityCustomNpc(world);
         npc.setPositionAndRotation((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
         npc.ai.setStartPos(new BlockPos(x, y, z));
         npc.setHealth(npc.getMaxHealth());
         world.spawnEntityInWorld(npc);
         return npc.wrappedNPC;
      }
   }

   public static NpcAPI Instance() {
      if(instance == null) {
         instance = new WrapperNpcAPI();
      }

      return instance;
   }

   public EventBus events() {
      return EVENT_BUS;
   }

   public IBlock getIBlock(World world, BlockPos pos) {
      Block block = world.getBlockState(pos).getBlock();
      return (IBlock)(block != null && !block.isAir(world, pos)?(block instanceof BlockScripted?new BlockScriptedWrapper(world, block, pos):(block instanceof BlockScriptedDoor?new BlockScriptedDoorWrapper(world, block, pos):(block instanceof BlockFluidBase?new BlockFluidContainerWrapper(world, block, pos):new BlockWrapper(world, block, pos)))):null);
   }

   public IItemStack getIItemStack(ItemStack itemstack) {
      return itemstack == null?null:new ItemStackWrapper(itemstack);
   }

   public IFactionHandler getFactions() {
      this.checkWorld();
      return FactionController.getInstance();
   }

   private void checkWorld() {
      if(MinecraftServer.getServer() == null || MinecraftServer.getServer().isServerStopped()) {
         throw new CustomNPCsException("No world is loaded right now", new Object[0]);
      }
   }

   public IRecipeHandler getRecipes() {
      this.checkWorld();
      return RecipeController.instance;
   }

   public File getGlobalDir() {
      return CustomNpcs.Dir;
   }

   public File getWorldDir() {
      return CustomNpcs.getWorldSaveDirectory();
   }

   public void registerCommand(CommandNoppesBase command) {
      CustomNpcs.NoppesCommand.registerCommand(command);
   }

}
