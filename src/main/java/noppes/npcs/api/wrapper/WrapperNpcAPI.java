//

//

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
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.controllers.faction.FactionController;
import noppes.npcs.controllers.recipies.RecipeController;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class WrapperNpcAPI extends NpcAPI {
	public static EventBus EVENT_BUS;
	private static NpcAPI instance;

	static {
		EVENT_BUS = new EventBus();
		WrapperNpcAPI.instance = null;
	}

	public static NpcAPI Instance() {
		if (WrapperNpcAPI.instance == null) {
			WrapperNpcAPI.instance = new WrapperNpcAPI();
		}
		return WrapperNpcAPI.instance;
	}

	private void checkWorld() {
		if ((MinecraftServer.getServer() == null) || MinecraftServer.getServer().isServerStopped()) {
			throw new CustomNPCsException("No world is loaded right now", new Object[0]);
		}
	}

	@Override
	public ICustomNpc createNPC(World world) {
		if (world.isRemote) {
			return null;
		}
		EntityCustomNpc npc = new EntityCustomNpc(world);
		return npc.wrappedNPC;
	}

	@Override
	public EventBus events() {
		return WrapperNpcAPI.EVENT_BUS;
	}

	@Override
	public IFactionHandler getFactions() {
		checkWorld();
		return FactionController.getInstance();
	}

	@Override
	public File getGlobalDir() {
		return CustomNpcs.Dir;
	}

	@Override
	public IBlock getIBlock(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		if ((block == null) || block.isAir(world, pos)) {
			return null;
		}
		if (block instanceof BlockScripted) {
			return new BlockScriptedWrapper(world, block, pos);
		}
		if (block instanceof BlockScriptedDoor) {
			return new BlockScriptedDoorWrapper(world, block, pos);
		}
		if (block instanceof BlockFluidBase) {
			return new BlockFluidContainerWrapper(world, block, pos);
		}
		return new BlockWrapper(world, block, pos);
	}

	@Override
	public IEntity getIEntity(Entity entity) {
		if ((entity == null) || entity.worldObj.isRemote) {
			return null;
		}
		if (entity instanceof EntityNPCInterface) {
			return ((EntityNPCInterface) entity).wrappedNPC;
		}
		WrapperEntityData data = (WrapperEntityData) entity.getExtendedProperties("NPCWrappedObject");
		if (data != null) {
			return data.base;
		}
		if (entity instanceof EntityPlayerMP) {
			data = new WrapperEntityData(new PlayerWrapper((EntityPlayerMP) entity));
		} else if (entity instanceof EntityAnimal) {
			data = new WrapperEntityData(new AnimalWrapper((EntityAnimal) entity));
		} else if (entity instanceof EntityMob) {
			data = new WrapperEntityData(new MonsterWrapper((EntityMob) entity));
		} else if (entity instanceof EntityLiving) {
			data = new WrapperEntityData(new EntityLivingWrapper((EntityLiving) entity));
		} else if (entity instanceof EntityLivingBase) {
			data = new WrapperEntityData(new EntityLivingBaseWrapper((EntityLivingBase) entity));
		} else {
			data = new WrapperEntityData(new EntityWrapper(entity));
		}
		entity.registerExtendedProperties("NPCWrappedObject", data);
		return data.base;
	}

	@Override
	public IItemStack getIItemStack(ItemStack itemstack) {
		if (itemstack == null) {
			return null;
		}
		return new ItemStackWrapper(itemstack);
	}

	@Override
	public IRecipeHandler getRecipes() {
		checkWorld();
		return RecipeController.instance;
	}

	@Override
	public File getWorldDir() {
		return CustomNpcs.getWorldSaveDirectory();
	}

	@Override
	public void registerCommand(CommandNoppesBase command) {
		CustomNpcs.NoppesCommand.registerCommand(command);
	}

	@Override
	public ICustomNpc spawnNPC(World world, int x, int y, int z) {
		if (world.isRemote) {
			return null;
		}
		EntityCustomNpc npc = new EntityCustomNpc(world);
		npc.setPositionAndRotation(x + 0.5, y, z + 0.5, 0.0f, 0.0f);
		npc.ai.setStartPos(new BlockPos(x, y, z));
		npc.setHealth(npc.getMaxHealth());
		world.spawnEntityInWorld(npc);
		return npc.wrappedNPC;
	}
}
