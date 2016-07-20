//

//

package noppes.npcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.controllers.spawn.SpawnController;
import noppes.npcs.controllers.spawn.SpawnData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCSpawning {
	private static Set<ChunkCoordIntPair> eligibleChunksForSpawning;

	static {
		NPCSpawning.eligibleChunksForSpawning = Sets.newHashSet();
	}

	public static boolean canCreatureTypeSpawnAtLocation(final SpawnData data, final World world, final BlockPos pos) {
		if (!world.getWorldBorder().contains(pos)) {
			return false;
		}
		if ((data.type == 1) && (world.getLight(pos) > 8)) {
			return false;
		}
		final Block block = world.getBlockState(pos).getBlock();
		if (data.liquid) {
			return block.getMaterial().isLiquid() && world.getBlockState(pos.down()).getBlock().getMaterial().isLiquid()
					&& !world.getBlockState(pos.up()).getBlock().isNormalCube();
		}
		final BlockPos blockpos1 = pos.down();
		if (!World.doesBlockHaveSolidTopSurface(world, blockpos1)) {
			return false;
		}
		final Block block2 = world.getBlockState(blockpos1).getBlock();
		boolean flag = (block2 != Blocks.bedrock) && (block2 != Blocks.barrier);
		final BlockPos down = blockpos1.down();
		flag |= world.getBlockState(down).getBlock().canCreatureSpawn(world, down,
				EntityLiving.SpawnPlacementType.ON_GROUND);
		return flag && !block.isNormalCube() && !block.getMaterial().isLiquid()
				&& !world.getBlockState(pos.up()).getBlock().isNormalCube();
	}

	public static int countNPCs(final World world) {
		int count = 0;
		final List<Entity> list = world.loadedEntityList;
		for (final Entity entity : list) {
			if (entity instanceof EntityNPCInterface) {
				++count;
			}
		}
		return count;
	}

	public static void findChunksForSpawning(final WorldServer world) {
		if (SpawnController.instance.data.isEmpty() || ((world.getWorldInfo().getWorldTotalTime() % 400L) != 0L)) {
			return;
		}
		NPCSpawning.eligibleChunksForSpawning.clear();
		for (int i = 0; i < world.playerEntities.size(); ++i) {
			final EntityPlayer entityplayer = world.playerEntities.get(i);
			if (!entityplayer.isSpectator()) {
				final int j = MathHelper.floor_double(entityplayer.posX / 16.0);
				final int k = MathHelper.floor_double(entityplayer.posZ / 16.0);
				final byte size = 7;
				for (int x = -size; x <= size; ++x) {
					for (int z = -size; z <= size; ++z) {
						final ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x + j, z + k);
						if (!NPCSpawning.eligibleChunksForSpawning.contains(chunkcoordintpair)
								&& world.getWorldBorder().contains(chunkcoordintpair)) {
							NPCSpawning.eligibleChunksForSpawning.add(chunkcoordintpair);
						}
					}
				}
			}
		}
		if (countNPCs(world) > (NPCSpawning.eligibleChunksForSpawning.size() / 16)) {
			return;
		}
		final ArrayList<ChunkCoordIntPair> tmp = new ArrayList<ChunkCoordIntPair>(
				NPCSpawning.eligibleChunksForSpawning);
		Collections.shuffle(tmp);
		for (final ChunkCoordIntPair chunkcoordintpair2 : tmp) {
			final BlockPos chunkposition = getChunk(world, chunkcoordintpair2.chunkXPos, chunkcoordintpair2.chunkZPos);
			final int j2 = chunkposition.getX();
			final int k2 = chunkposition.getY();
			final int l1 = chunkposition.getZ();
			for (int m = 0; m < 3; ++m) {
				int x2 = j2;
				int y = k2;
				int z2 = l1;
				final byte b1 = 6;
				x2 += world.rand.nextInt(b1) - world.rand.nextInt(b1);
				y += world.rand.nextInt(1) - world.rand.nextInt(1);
				z2 += world.rand.nextInt(b1) - world.rand.nextInt(b1);
				final BlockPos pos = new BlockPos(x2, y, z2);
				final Block block = world.getBlockState(pos).getBlock();
				final String name = world.getBiomeGenForCoords(pos).biomeName;
				final SpawnData data = SpawnController.instance.getRandomSpawnData(name,
						block.getMaterial() == Material.air);
				if ((data != null) && canCreatureTypeSpawnAtLocation(data, world, pos)) {
					if (world.getClosestPlayer(x2, y, z2, 24.0) == null) {
						spawnData(data, world, pos);
					}
				}
			}
		}
	}

	protected static BlockPos getChunk(final World world, final int x, final int z) {
		final Chunk chunk = world.getChunkFromChunkCoords(x, z);
		final int k = (x * 16) + world.rand.nextInt(16);
		final int l = (z * 16) + world.rand.nextInt(16);
		final int i1 = MathHelper.roundUp(chunk.getHeight(new BlockPos(k, 0, l)) + 1, 16);
		final int j1 = world.rand.nextInt((i1 > 0) ? i1 : ((chunk.getTopFilledSegment() + 16) - 1));
		return new BlockPos(k, j1, l);
	}

	public static void performWorldGenSpawning(final World world, final int x, final int z, final Random rand) {
		final BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(x + 8, 0, z + 8));
		while (rand.nextFloat() < biome.getSpawningChance()) {
			final SpawnData data = SpawnController.instance.getRandomSpawnData(biome.biomeName, true);
			if (data == null) {
				continue;
			}
			final int size = 16;
			int j1 = x + rand.nextInt(size);
			int k1 = z + rand.nextInt(size);
			final int l1 = j1;
			final int i2 = k1;
			for (int k2 = 0; k2 < 4; ++k2) {
				final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(j1, 0, k1));
				if (!canCreatureTypeSpawnAtLocation(data, world, pos)) {
					for (j1 += rand.nextInt(5) - rand.nextInt(5), k1 += rand.nextInt(5) - rand.nextInt(5); (j1 < x)
							|| (j1 >= (x + size)) || (k1 < z) || (k1 >= (z + size)); j1 = (l1 + rand.nextInt(5))
									- rand.nextInt(5), k1 = (i2 + rand.nextInt(5)) - rand.nextInt(5)) {
					}
				} else if (spawnData(data, world, pos)) {
					break;
				}
			}
		}
	}

	private static boolean spawnData(final SpawnData data, final World world, final BlockPos pos) {
		EntityLiving entityliving;
		try {
			final Entity entity = EntityList.createEntityFromNBT(data.compound1, world);
			if ((entity == null) || !(entity instanceof EntityLiving)) {
				return false;
			}
			entityliving = (EntityLiving) entity;
			if (entity instanceof EntityCustomNpc) {
				final EntityCustomNpc npc = (EntityCustomNpc) entity;
				npc.stats.spawnCycle = 3;
				npc.ai.returnToStart = false;
				npc.ai.setStartPos(pos);
			}
			entity.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.rand.nextFloat() * 360.0f,
					0.0f);
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
		final Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, world, pos.getX() + 0.5f,
				pos.getY(), pos.getZ() + 0.5f);
		if ((canSpawn == Event.Result.DENY)
				|| ((canSpawn == Event.Result.DEFAULT) && !entityliving.getCanSpawnHere())) {
			return false;
		}
		world.spawnEntityInWorld(entityliving);
		return true;
	}
}
