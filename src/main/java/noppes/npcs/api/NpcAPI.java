//

//

package noppes.npcs.api;

import java.io.File;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IRecipeHandler;

public abstract class NpcAPI {
	private static NpcAPI instance;

	static {
		NpcAPI.instance = null;
	}

	public static NpcAPI Instance() {
		if (NpcAPI.instance != null) {
			return NpcAPI.instance;
		}
		if (!IsAvailable()) {
			return null;
		}
		try {
			Class c = Class.forName("noppes.npcs.api.wrapper.WrapperNpcAPI");
			NpcAPI.instance = (NpcAPI) c.getMethod("Instance", new Class[0]).invoke(null, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NpcAPI.instance;
	}

	public static boolean IsAvailable() {
		return Loader.isModLoaded("customnpcs");
	}

	public abstract ICustomNpc createNPC(World p0);

	public abstract EventBus events();

	public abstract IFactionHandler getFactions();

	public abstract File getGlobalDir();

	public abstract IBlock getIBlock(World p0, BlockPos p1);

	public abstract IEntity getIEntity(Entity p0);

	public abstract IItemStack getIItemStack(ItemStack p0);

	public abstract IRecipeHandler getRecipes();

	public abstract File getWorldDir();

	public abstract void registerCommand(CommandNoppesBase p0);

	public abstract ICustomNpc spawnNPC(World p0, int p1, int p2, int p3);
}
