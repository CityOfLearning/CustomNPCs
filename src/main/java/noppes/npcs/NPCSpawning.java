
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

	public static boolean canCreatureTypeSpawnAtLocation(SpawnData data, World world, BlockPos pos) {
		if (!world.getWorldBorder().contains(pos)) {
			return false;
		}
		if ((data.type == 1) && (world.getLight(pos) > 8)) {
			return false;
		}
		Block block = world.getBlockState(pos).getBlock();
		if (data.liquid) {
			return block.getMaterial().isLiquid() && world.getBlockState(pos.down()).getBlock().getMaterial().isLiquid()
					&& !world.getBlockState(pos.up()).getBlock().isNormalCube();
		}
		BlockPos blockpos1 = pos.down();
		if (!World.doesBlockHaveSolidTopSurface(world, blockpos1)) {
			return false;
		}
		Block block2 = world.getBlockState(blockpos1).getBlock();
		boolean flag = (block2 != Blocks.bedrock) && (block2 != Blocks.barrier);
		BlockPos down = blockpos1.down();
		flag |= world.getBlockState(down).getBlock().canCreatureSpawn(world, down,
				EntityLiving.SpawnPlacementType.ON_GROUND);
		return flag && !block.isNormalCube() && !block.getMaterial().isLiquid()
				&& !world.getBlockState(pos.up()).getBlock().isNormalCube();
	}

	public static int countNPCs(World world) {
		int count = 0;
		List<Entity> list = world.loadedEntityList;
		for (Entity entity : list) {
			if (entity instanceof EntityNPCInterface) {
				++count;
			}
		}
		return count;
	}

	public static void findChunksForSpawning(WorldServer world) {
		if (SpawnController.instance.data.isEmpty() || ((world.getWorldInfo().getWorldTotalTime() % 400L) != 0L)) {
			return;
		}
		NPCSpawning.eligibleChunksForSpawning.clear();
		for (int i = 0; i < world.playerEntities.size(); ++i) {
			EntityPlayer entityplayer = world.playerEntities.get(i);
			if (!entityplayer.isSpectator()) {
				int j = MathHelper.floor_double(entityplayer.posX / 16.0);
				int k = MathHelper.floor_double(entityplayer.posZ / 16.0);
				byte size = 7;
				for (int x = -size; x <= size; ++x) {
					for (int z = -size; z <= size; ++z) {
						ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x + j, z + k);
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
		ArrayList<ChunkCoordIntPair> tmp = new ArrayList<ChunkCoordIntPair>(NPCSpawning.eligibleChunksForSpawning);
		Collections.shuffle(tmp);
		for (ChunkCoordIntPair chunkcoordintpair2 : tmp) {
			BlockPos chunkposition = getChunk(world, chunkcoordintpair2.chunkXPos, chunkcoordintpair2.chunkZPos);
			int j2 = chunkposition.getX();
			int k2 = chunkposition.getY();
			int l1 = chunkposition.getZ();
			for (int m = 0; m < 3; ++m) {
				int x2 = j2;
				int y = k2;
				int z2 = l1;
				byte b1 = 6;
				x2 += world.rand.nextInt(b1) - world.rand.nextInt(b1);
				y += world.rand.nextInt(1) - world.rand.nextInt(1);
				z2 += world.rand.nextInt(b1) - world.rand.nextInt(b1);
				BlockPos pos = new BlockPos(x2, y, z2);
				Block block = world.getBlockState(pos).getBlock();
				String name = world.getBiomeGenForCoords(pos).biomeName;
				SpawnData data = SpawnController.instance.getRandomSpawnData(name, block.getMaterial() == Material.air);
				if ((data != null) && canCreatureTypeSpawnAtLocation(data, world, pos)) {
					if (world.getClosestPlayer(x2, y, z2, 24.0) == null) {
						spawnData(data, world, pos);
					}
				}
			}
		}
	}

	protected static BlockPos getChunk(World world, int x, int z) {
		Chunk chunk = world.getChunkFromChunkCoords(x, z);
		int k = (x * 16) + world.rand.nextInt(16);
		int l = (z * 16) + world.rand.nextInt(16);
		int i1 = MathHelper.roundUp(chunk.getHeight(new BlockPos(k, 0, l)) + 1, 16);
		int j1 = world.rand.nextInt((i1 > 0) ? i1 : ((chunk.getTopFilledSegment() + 16) - 1));
		return new BlockPos(k, j1, l);
	}

	public static void performWorldGenSpawning(World world, int x, int z, Random rand) {
		BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(x + 8, 0, z + 8));
		while (rand.nextFloat() < biome.getSpawningChance()) {
			SpawnData data = SpawnController.instance.getRandomSpawnData(biome.biomeName, true);
			if (data == null) {
				continue;
			}
			int size = 16;
			int j1 = x + rand.nextInt(size);
			int k1 = z + rand.nextInt(size);
			int l1 = j1;
			int i2 = k1;
			for (int k2 = 0; k2 < 4; ++k2) {
				BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(j1, 0, k1));
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

	private static boolean spawnData(SpawnData data, World world, BlockPos pos) {
		EntityLiving entityliving;
		try {
			Entity entity = EntityList.createEntityFromNBT(data.compound1, world);
			if ((entity == null) || !(entity instanceof EntityLiving)) {
				return false;
			}
			entityliving = (EntityLiving) entity;
			if (entity instanceof EntityCustomNpc) {
				EntityCustomNpc npc = (EntityCustomNpc) entity;
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
		Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, world, pos.getX() + 0.5f, pos.getY(),
				pos.getZ() + 0.5f);
		if ((canSpawn == Event.Result.DENY)
				|| ((canSpawn == Event.Result.DEFAULT) && !entityliving.getCanSpawnHere())) {
			return false;
		}
		world.spawnEntityInWorld(entityliving);
		return true;
	}
}
