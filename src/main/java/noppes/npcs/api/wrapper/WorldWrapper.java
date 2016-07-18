//

//

package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IScoreboard;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;

public class WorldWrapper implements IWorld {
	public static Map<String, Object> tempData;
	static {
		WorldWrapper.tempData = new HashMap<String, Object>();
	}
	public WorldServer world;
	private IData tempdata;

	private IData storeddata;

	public WorldWrapper(final World world) {
		tempdata = new IData() {
			@Override
			public void clear() {
				WorldWrapper.tempData.clear();
			}

			@Override
			public Object get(final String key) {
				return WorldWrapper.tempData.get(key);
			}

			@Override
			public boolean has(final String key) {
				return WorldWrapper.tempData.containsKey(key);
			}

			@Override
			public void put(final String key, final Object value) {
				WorldWrapper.tempData.put(key, value);
			}

			@Override
			public void remove(final String key) {
				WorldWrapper.tempData.remove(key);
			}
		};
		storeddata = new IData() {
			@Override
			public void clear() {
				ScriptController.Instance.compound = new NBTTagCompound();
				ScriptController.Instance.shouldSave = true;
			}

			@Override
			public Object get(final String key) {
				final NBTTagCompound compound = ScriptController.Instance.compound;
				if (!compound.hasKey(key)) {
					return null;
				}
				final NBTBase base = compound.getTag(key);
				if (base instanceof NBTBase.NBTPrimitive) {
					return ((NBTBase.NBTPrimitive) base).getDouble();
				}
				return ((NBTTagString) base).getString();
			}

			@Override
			public boolean has(final String key) {
				return ScriptController.Instance.compound.hasKey(key);
			}

			@Override
			public void put(final String key, final Object value) {
				final NBTTagCompound compound = ScriptController.Instance.compound;
				if (value instanceof Number) {
					compound.setDouble(key, ((Number) value).doubleValue());
				} else if (value instanceof String) {
					compound.setString(key, (String) value);
				}
				ScriptController.Instance.shouldSave = true;
			}

			@Override
			public void remove(final String key) {
				ScriptController.Instance.compound.removeTag(key);
				ScriptController.Instance.shouldSave = true;
			}
		};
		this.world = (WorldServer) world;
	}

	@Override
	public void broadcast(final String message) {
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(message));
	}

	@Override
	public IItemStack createItem(final String name, final int damage, final int size) {
		final Item item = Item.itemRegistry.getObject(new ResourceLocation(name));
		if (item == null) {
			return null;
		}
		return new ItemStackWrapper(new ItemStack(item, size, damage));
	}

	@Override
	public void explode(final double x, final double y, final double z, final float range, final boolean fire,
			final boolean grief) {
		world.newExplosion((Entity) null, x, y, z, range, fire, grief);
	}

	@Override
	public IPlayer[] getAllPlayers() {
		final List<EntityPlayerMP> list = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		final IPlayer[] arr = new IPlayer[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			arr[i] = (IPlayer) NpcAPI.Instance().getIEntity(list.get(i));
		}
		return arr;
	}

	@Override
	public String getBiomeName(final int x, final int z) {
		return world.getBiomeGenForCoords(new BlockPos(x, 0, z)).biomeName;
	}

	@Override
	public IBlock getBlock(final int x, final int y, final int z) {
		return NpcAPI.Instance().getIBlock(world, new BlockPos(x, y, z));
	}

	private Class getClassForType(final int type) {
		Class cls = Entity.class;
		if (type == 5) {
			cls = EntityLivingBase.class;
		} else if (type == 1) {
			cls = EntityPlayer.class;
		} else if (type == 4) {
			cls = EntityAnimal.class;
		} else if (type == 3) {
			cls = EntityMob.class;
		} else if (type == 2) {
			cls = EntityNPCInterface.class;
		}
		return cls;
	}

	@Override
	public IEntity getClosestEntity(final int x, final int y, final int z, final int range, final int type) {
		final AxisAlignedBB bb = AxisAlignedBB.fromBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(x, y, z).expand(range,
				range, range);
		final List<Entity> entities = world.getEntitiesWithinAABB(getClassForType(type), bb);
		double distance = range * range * range;
		Entity entity = null;
		for (final Entity e : entities) {
			final double d0 = x - e.posX;
			final double d2 = y - e.posY;
			final double d3 = z - e.posZ;
			final double r = (d0 * d0) + (d2 * d2) + (d3 * d3);
			if (entity == null) {
				distance = r;
				entity = e;
			} else {
				if (r >= distance) {
					continue;
				}
				distance = r;
				entity = e;
			}
		}
		return NpcAPI.Instance().getIEntity(entity);
	}

	@Override
	public float getLightValue(final int x, final int y, final int z) {
		return world.getLight(new BlockPos(x, y, z)) / 16.0f;
	}

	@Override
	public World getMCWorld() {
		return world;
	}

	@Override
	public IEntity[] getNearbyEntities(final int x, final int y, final int z, final int range, final int type) {
		final AxisAlignedBB bb = AxisAlignedBB.fromBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(x, y, z).expand(range,
				range, range);
		final List<Entity> entities = world.getEntitiesWithinAABB(getClassForType(type), bb);
		final List<IEntity> list = new ArrayList<IEntity>();
		for (final Entity living : entities) {
			list.add(NpcAPI.Instance().getIEntity(living));
		}
		return list.toArray(new IEntity[list.size()]);
	}

	@Override
	public IPlayer getPlayer(final String name) {
		final EntityPlayer player = world.getPlayerEntityByName(name);
		if (player == null) {
			return null;
		}
		return (IPlayer) NpcAPI.Instance().getIEntity(player);
	}

	@Override
	public int getRedstonePower(final int x, final int y, final int z) {
		return world.getStrongPower(new BlockPos(x, y, z));
	}

	@Override
	public IScoreboard getScoreboard() {
		return new ScoreboardWrapper();
	}

	@Override
	public IData getStoreddata() {
		return storeddata;
	}

	@Override
	public IData getTempdata() {
		return tempdata;
	}

	@Override
	public long getTime() {
		return world.getWorldTime();
	}

	@Override
	public long getTotalTime() {
		return world.getTotalWorldTime();
	}

	@Override
	public boolean isDay() {
		return (world.getWorldTime() % 24000L) < 12000L;
	}

	@Override
	public boolean isRaining() {
		return world.getWorldInfo().isRaining();
	}

	@Override
	public void removeBlock(final int x, final int y, final int z) {
		world.setBlockToAir(new BlockPos(x, y, z));
	}

	@Override
	public void setBlock(final int x, final int y, final int z, final String name, final int meta) {
		final Block block = Block.getBlockFromName(name);
		if (block == null) {
			throw new CustomNPCsException("There is no such block: %s", new Object[0]);
		}
		world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(meta));
	}

	@Override
	public void setRaining(final boolean bo) {
		world.getWorldInfo().setRaining(bo);
	}

	@Override
	public void setTime(final long time) {
		world.setWorldTime(time);
	}

	@Override
	public IEntity spawnClone(final int x, final int y, final int z, final int tab, final String name) {
		final NBTTagCompound compound = ServerCloneController.Instance.getCloneData(null, name, tab);
		if (compound == null) {
			return null;
		}
		final Entity entity = NoppesUtilServer.spawnClone(compound, x, y, z, world);
		if (entity == null) {
			return null;
		}
		return NpcAPI.Instance().getIEntity(entity);
	}

	@Override
	public void spawnParticle(final String particle, final double x, final double y, final double z, final double dx,
			final double dy, final double dz, final double speed, final int count) {
		EnumParticleTypes particleType = null;
		for (final EnumParticleTypes enumParticle : EnumParticleTypes.values()) {
			if (enumParticle.hasArguments()) {
				if (particle.startsWith(enumParticle.getParticleName())) {
					particleType = enumParticle;
					break;
				}
			} else if (particle.equals(enumParticle.getParticleName())) {
				particleType = enumParticle;
				break;
			}
		}
		if (particleType != null) {
			world.spawnParticle(particleType, x, y, z, count, dx, dy, dz, speed, new int[0]);
		}
	}

	@Override
	public void thunderStrike(final double x, final double y, final double z) {
		world.addWeatherEffect(new EntityLightningBolt(world, x, y, z));
	}
}
