package noppes.npcs;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
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
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.SpawnData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCSpawning {

   private static Set eligibleChunksForSpawning = Sets.newHashSet();


   public static void findChunksForSpawning(WorldServer world) {
      if(!SpawnController.instance.data.isEmpty() && world.getWorldInfo().getWorldTotalTime() % 400L == 0L) {
         eligibleChunksForSpawning.clear();

         int k1;
         int l1;
         for(int tmp = 0; tmp < world.playerEntities.size(); ++tmp) {
            EntityPlayer iterator = (EntityPlayer)world.playerEntities.get(tmp);
            if(!iterator.isSpectator()) {
               int chunkcoordintpair1 = MathHelper.floor_double(iterator.posX / 16.0D);
               int chunkposition = MathHelper.floor_double(iterator.posZ / 16.0D);
               byte j1 = 7;

               for(k1 = -j1; k1 <= j1; ++k1) {
                  for(l1 = -j1; l1 <= j1; ++l1) {
                     ChunkCoordIntPair i = new ChunkCoordIntPair(k1 + chunkcoordintpair1, l1 + chunkposition);
                     if(!eligibleChunksForSpawning.contains(i) && world.getWorldBorder().contains(i)) {
                        eligibleChunksForSpawning.add(i);
                     }
                  }
               }
            }
         }

         if(countNPCs(world) <= eligibleChunksForSpawning.size() / 16) {
            ArrayList var17 = new ArrayList(eligibleChunksForSpawning);
            Collections.shuffle(var17);
            Iterator var18 = var17.iterator();

            while(var18.hasNext()) {
               ChunkCoordIntPair var19 = (ChunkCoordIntPair)var18.next();
               BlockPos var20 = getChunk(world, var19.chunkXPos, var19.chunkZPos);
               int var21 = var20.getX();
               k1 = var20.getY();
               l1 = var20.getZ();

               for(int var22 = 0; var22 < 3; ++var22) {
                  byte b1 = 6;
                  int x = var21 + (world.rand.nextInt(b1) - world.rand.nextInt(b1));
                  int y = k1 + (world.rand.nextInt(1) - world.rand.nextInt(1));
                  int z = l1 + (world.rand.nextInt(b1) - world.rand.nextInt(b1));
                  BlockPos pos = new BlockPos(x, y, z);
                  Block block = world.getBlockState(pos).getBlock();
                  String name = world.getBiomeGenForCoords(pos).biomeName;
                  SpawnData data = SpawnController.instance.getRandomSpawnData(name, block.getMaterial() == Material.air);
                  if(data != null && canCreatureTypeSpawnAtLocation(data, world, pos) && world.getClosestPlayer((double)x, (double)y, (double)z, 24.0D) == null) {
                     spawnData(data, world, pos);
                  }
               }
            }

         }
      }
   }

   public static int countNPCs(World world) {
      int count = 0;
      List list = world.loadedEntityList;
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if(entity instanceof EntityNPCInterface) {
            ++count;
         }
      }

      return count;
   }

   protected static BlockPos getChunk(World world, int x, int z) {
      Chunk chunk = world.getChunkFromChunkCoords(x, z);
      int k = x * 16 + world.rand.nextInt(16);
      int l = z * 16 + world.rand.nextInt(16);
      int i1 = MathHelper.roundUp(chunk.getHeight(new BlockPos(k, 0, l)) + 1, 16);
      int j1 = world.rand.nextInt(i1 > 0?i1:chunk.getTopFilledSegment() + 16 - 1);
      return new BlockPos(k, j1, l);
   }

   public static void performWorldGenSpawning(World world, int x, int z, Random rand) {
      BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(x + 8, 0, z + 8));

      while(rand.nextFloat() < biome.getSpawningChance()) {
         SpawnData data = SpawnController.instance.getRandomSpawnData(biome.biomeName, true);
         if(data != null) {
            byte size = 16;
            int j1 = x + rand.nextInt(size);
            int k1 = z + rand.nextInt(size);
            int l1 = j1;
            int i2 = k1;

            for(int k2 = 0; k2 < 4; ++k2) {
               BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(j1, 0, k1));
               if(!canCreatureTypeSpawnAtLocation(data, world, pos)) {
                  j1 += rand.nextInt(5) - rand.nextInt(5);

                  for(k1 += rand.nextInt(5) - rand.nextInt(5); j1 < x || j1 >= x + size || k1 < z || k1 >= z + size; k1 = i2 + rand.nextInt(5) - rand.nextInt(5)) {
                     j1 = l1 + rand.nextInt(5) - rand.nextInt(5);
                  }
               } else if(spawnData(data, world, pos)) {
                  break;
               }
            }
         }
      }

   }

   private static boolean spawnData(SpawnData data, World world, BlockPos pos) {
      EntityLiving entityliving;
      try {
         Entity canSpawn = EntityList.createEntityFromNBT(data.compound1, world);
         if(canSpawn == null || !(canSpawn instanceof EntityLiving)) {
            return false;
         }

         entityliving = (EntityLiving)canSpawn;
         if(canSpawn instanceof EntityCustomNpc) {
            EntityCustomNpc npc = (EntityCustomNpc)canSpawn;
            npc.stats.spawnCycle = 3;
            npc.ai.returnToStart = false;
            npc.ai.setStartPos(pos);
         }

         canSpawn.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }

      Result canSpawn1 = ForgeEventFactory.canEntitySpawn(entityliving, world, (float)pos.getX() + 0.5F, (float)pos.getY(), (float)pos.getZ() + 0.5F);
      if(canSpawn1 != Result.DENY && (canSpawn1 != Result.DEFAULT || entityliving.getCanSpawnHere())) {
         world.spawnEntityInWorld(entityliving);
         return true;
      } else {
         return false;
      }
   }

   public static boolean canCreatureTypeSpawnAtLocation(SpawnData data, World world, BlockPos pos) {
      if(!world.getWorldBorder().contains(pos)) {
         return false;
      } else if(data.type == 1 && world.getLight(pos) > 8) {
         return false;
      } else {
         Block block = world.getBlockState(pos).getBlock();
         if(data.liquid) {
            return block.getMaterial().isLiquid() && world.getBlockState(pos.down()).getBlock().getMaterial().isLiquid() && !world.getBlockState(pos.up()).getBlock().isNormalCube();
         } else {
            BlockPos blockpos1 = pos.down();
            if(!World.doesBlockHaveSolidTopSurface(world, blockpos1)) {
               return false;
            } else {
               Block block1 = world.getBlockState(blockpos1).getBlock();
               boolean flag = block1 != Blocks.bedrock && block1 != Blocks.barrier;
               BlockPos down = blockpos1.down();
               flag |= world.getBlockState(down).getBlock().canCreatureSpawn(world, down, SpawnPlacementType.ON_GROUND);
               return flag && !block.isNormalCube() && !block.getMaterial().isLiquid() && !world.getBlockState(pos.up()).getBlock().isNormalCube();
            }
         }
      }
   }

}
