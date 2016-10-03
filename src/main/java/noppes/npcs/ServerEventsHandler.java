package noppes.npcs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Post;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.EventHooks;
import noppes.npcs.NPCSpawning;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Server;
import noppes.npcs.api.constants.EnumQuestType;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemShield;
import noppes.npcs.items.ItemSoulstoneEmpty;
import noppes.npcs.quests.QuestKill;
import noppes.npcs.roles.RoleFollower;

public class ServerEventsHandler {

   public static EntityVillager Merchant;
   public static Entity mounted;


   @SubscribeEvent
   public void invoke(EntityInteractEvent event) {
      ItemStack item = event.entityPlayer.getCurrentEquippedItem();
      if(item != null) {
         boolean isRemote = event.entityPlayer.worldObj.isRemote;
         boolean npcInteracted = event.target instanceof EntityNPCInterface;
         if(isRemote || !CustomNpcs.OpsOnly || MinecraftServer.getServer().getConfigurationManager().canSendCommands(event.entityPlayer.getGameProfile())) {
            if(!isRemote && item.getItem() == CustomItems.soulstoneEmpty && event.target instanceof EntityLivingBase) {
               ((ItemSoulstoneEmpty)item.getItem()).store((EntityLivingBase)event.target, item, event.entityPlayer);
            }

            CustomNpcsPermissions var10000;
            if(item.getItem() == CustomItems.wand && npcInteracted && !isRemote) {
               var10000 = CustomNpcsPermissions.Instance;
               if(!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.NPC_GUI)) {
                  return;
               }

               event.setCanceled(true);
               NoppesUtilServer.sendOpenGui(event.entityPlayer, EnumGuiType.MainMenuDisplay, (EntityNPCInterface)event.target);
            } else if(item.getItem() == CustomItems.cloner && !isRemote && !(event.target instanceof EntityPlayer)) {
               NBTTagCompound player1 = new NBTTagCompound();
               if(!event.target.writeToNBTOptional(player1)) {
                  return;
               }

               PlayerData merchantrecipelist1 = PlayerDataController.instance.getPlayerData(event.entityPlayer);
               ServerCloneController.Instance.cleanTags(player1);
               if(!Server.sendDataChecked((EntityPlayerMP)event.entityPlayer, EnumPacketClient.CLONE, new Object[]{player1})) {
                  event.entityPlayer.addChatMessage(new ChatComponentText("Entity too big to clone"));
               }

               merchantrecipelist1.cloned = player1;
               event.setCanceled(true);
            } else if(item.getItem() == CustomItems.scripter && !isRemote && npcInteracted) {
               var10000 = CustomNpcsPermissions.Instance;
               if(!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.NPC_GUI)) {
                  return;
               }

               NoppesUtilServer.setEditingNpc(event.entityPlayer, (EntityNPCInterface)event.target);
               event.setCanceled(true);
               Server.sendData((EntityPlayerMP)event.entityPlayer, EnumPacketClient.GUI, new Object[]{Integer.valueOf(EnumGuiType.Script.ordinal()), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)});
            } else if(item.getItem() == CustomItems.mount) {
               var10000 = CustomNpcsPermissions.Instance;
               if(!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.TOOL_MOUNTER)) {
                  return;
               }

               event.setCanceled(true);
               mounted = event.target;
               if(isRemote) {
                  CustomNpcs.proxy.openGui(MathHelper.floor_double(mounted.posX), MathHelper.floor_double(mounted.posY), MathHelper.floor_double(mounted.posZ), EnumGuiType.MobSpawnerMounter, event.entityPlayer);
               }
            } else if(item.getItem() == CustomItems.wand && event.target instanceof EntityVillager) {
               var10000 = CustomNpcsPermissions.Instance;
               if(!CustomNpcsPermissions.hasPermission(event.entityPlayer, CustomNpcsPermissions.EDIT_VILLAGER)) {
                  return;
               }

               event.setCanceled(true);
               Merchant = (EntityVillager)event.target;
               if(!isRemote) {
                  EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
                  player.openGui(CustomNpcs.instance, EnumGuiType.MerchantAdd.ordinal(), player.worldObj, 0, 0, 0);
                  MerchantRecipeList merchantrecipelist = Merchant.getRecipes(player);
                  if(merchantrecipelist != null) {
                     Server.sendData(player, EnumPacketClient.VILLAGER_LIST, new Object[]{merchantrecipelist});
                  }
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void invoke(LivingHurtEvent event) {
      if(event.entityLiving instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.entityLiving;
         if(!event.source.isUnblockable() && !event.source.isFireDamage()) {
            if(player.isBlocking()) {
               ItemStack item = player.getCurrentEquippedItem();
               if(item != null && item.getItem() instanceof ItemShield) {
                  if(((ItemShield)item.getItem()).material.getDamageVsEntity() >= player.getRNG().nextInt(9)) {
                     float damage = (float)item.getItemDamage() + event.ammount;
                     item.damageItem((int)event.ammount, player);
                     if(damage > (float)item.getMaxDamage()) {
                        event.ammount = damage - (float)item.getMaxDamage();
                     } else {
                        event.ammount = 0.0F;
                        event.setCanceled(true);
                     }

                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void invoke(PlayerInteractEvent event) {
      if(event.pos != null) {
         EntityPlayer player = event.entityPlayer;
         BlockPos pos = event.pos;
         IBlockState state = player.worldObj.getBlockState(pos);
         Block block = state.getBlock();
         if(event.action == Action.LEFT_CLICK_BLOCK && player.getHeldItem() != null && player.getHeldItem().getItem() == CustomItems.teleporter) {
            event.setCanceled(true);
         }

         RecipeController item;
         NBTTagList meta;
         int tile;
         Iterator compound;
         RecipeCarpentry recipe;
         NBTTagCompound compound1;
         NBTTagCompound var14;
         if(block == Blocks.crafting_table && event.action == Action.RIGHT_CLICK_BLOCK && !player.worldObj.isRemote) {
            item = RecipeController.instance;
            meta = new NBTTagList();
            tile = 0;
            compound = item.globalRecipes.values().iterator();

            while(compound.hasNext()) {
               recipe = (RecipeCarpentry)compound.next();
               meta.appendTag(recipe.writeNBT());
               ++tile;
               if(tile % 10 == 0) {
                  compound1 = new NBTTagCompound();
                  compound1.setTag("recipes", meta);
                  Server.sendData((EntityPlayerMP)player, EnumPacketClient.SYNCRECIPES_ADD, new Object[]{compound1});
                  meta = new NBTTagList();
               }
            }

            if(tile % 10 != 0) {
               var14 = new NBTTagCompound();
               var14.setTag("recipes", meta);
               Server.sendData((EntityPlayerMP)player, EnumPacketClient.SYNCRECIPES_ADD, new Object[]{var14});
            }

            Server.sendData((EntityPlayerMP)player, EnumPacketClient.SYNCRECIPES_WORKBENCH, new Object[0]);
         }

         if(block == CustomItems.carpentyBench && event.action == Action.RIGHT_CLICK_BLOCK && !player.worldObj.isRemote) {
            item = RecipeController.instance;
            meta = new NBTTagList();
            tile = 0;
            compound = item.anvilRecipes.values().iterator();

            while(compound.hasNext()) {
               recipe = (RecipeCarpentry)compound.next();
               meta.appendTag(recipe.writeNBT());
               ++tile;
               if(tile % 10 == 0) {
                  compound1 = new NBTTagCompound();
                  compound1.setTag("recipes", meta);
                  Server.sendData((EntityPlayerMP)player, EnumPacketClient.SYNCRECIPES_ADD, new Object[]{compound1});
                  meta = new NBTTagList();
               }
            }

            if(tile % 10 != 0) {
               var14 = new NBTTagCompound();
               var14.setTag("recipes", meta);
               Server.sendData((EntityPlayerMP)player, EnumPacketClient.SYNCRECIPES_ADD, new Object[]{var14});
            }

            Server.sendData((EntityPlayerMP)player, EnumPacketClient.SYNCRECIPES_CARPENTRYBENCH, new Object[0]);
         }

         if((block == CustomItems.banner || block == CustomItems.wallBanner || block == CustomItems.sign) && event.action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack var12 = player.inventory.getCurrentItem();
            if(var12 == null || var12.getItem() == null) {
               return;
            }

            int var13 = block.getMetaFromState(state);
            if(var13 >= 7) {
               pos = pos.down();
            }

            TileBanner var15 = (TileBanner)player.worldObj.getTileEntity(pos);
            if(!var15.canEdit()) {
               if(var12.getItem() == CustomItems.wand && CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS)) {
                  var15.time = System.currentTimeMillis();
                  if(player.worldObj.isRemote) {
                     player.addChatComponentMessage(new ChatComponentTranslation("availability.editIcon", new Object[0]));
                  }
               }

               return;
            }

            if(!player.worldObj.isRemote) {
               var15.icon = var12.copy();
               player.worldObj.markBlockForUpdate(pos);
               event.setCanceled(true);
            }
         }

      }
   }

   @SubscribeEvent
   public void invoke(LivingDeathEvent event) {
      if(!event.entityLiving.worldObj.isRemote) {
         if(event.source.getEntity() != null) {
            if(event.source.getEntity() instanceof EntityNPCInterface && event.entityLiving != null) {
               EntityNPCInterface data = (EntityNPCInterface)event.source.getEntity();
               Line line = data.advanced.getKillLine();
               if(line != null) {
                  data.saySurrounding(line.formatTarget(event.entityLiving));
               }

               EventHooks.onNPCKills(data, event.entityLiving);
            }

            EntityPlayer data1 = null;
            if(event.source.getEntity() instanceof EntityPlayer) {
               data1 = (EntityPlayer)event.source.getEntity();
            } else if(event.source.getEntity() instanceof EntityNPCInterface && ((EntityNPCInterface)event.source.getEntity()).advanced.role == 2) {
               data1 = ((RoleFollower)((EntityNPCInterface)event.source.getEntity()).roleInterface).owner;
            }

            if(data1 != null) {
               this.doQuest(data1, event.entityLiving, true);
               if(event.entityLiving instanceof EntityNPCInterface) {
                  this.doFactionPoints(data1, (EntityNPCInterface)event.entityLiving);
               }
            }
         }

         if(event.entityLiving instanceof EntityPlayer) {
            PlayerData data2 = PlayerDataController.instance.getPlayerData((EntityPlayer)event.entityLiving);
            data2.saveNBTData((NBTTagCompound)null);
         }

      }
   }

   private void doFactionPoints(EntityPlayer player, EntityNPCInterface npc) {
      npc.advanced.factions.addPoints(player);
   }

   private void doQuest(EntityPlayer player, EntityLivingBase entity, boolean all) {
      PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
      boolean change = false;
      String entityName = EntityList.getEntityString(entity);
      Iterator var7 = playerdata.activeQuests.values().iterator();

      while(var7.hasNext()) {
         QuestData data = (QuestData)var7.next();
         if(data.quest.type == EnumQuestType.KILL || data.quest.type == EnumQuestType.AREA_KILL) {
            if(data.quest.type == EnumQuestType.AREA_KILL && all) {
               List name = player.worldObj.getEntitiesWithinAABB(EntityPlayer.class, entity.getEntityBoundingBox().expand(10.0D, 10.0D, 10.0D));
               Iterator quest = name.iterator();

               while(quest.hasNext()) {
                  EntityPlayer killed = (EntityPlayer)quest.next();
                  if(killed != player) {
                     this.doQuest(killed, entity, false);
                  }
               }
            }

            String name1 = entityName;
            QuestKill quest1 = (QuestKill)data.quest.questInterface;
            if(quest1.targets.containsKey(entity.getName())) {
               name1 = entity.getName();
            } else if(!quest1.targets.containsKey(entityName)) {
               continue;
            }

            HashMap killed1 = quest1.getKilled(data);
            if(!killed1.containsKey(name1) || ((Integer)killed1.get(name1)).intValue() < ((Integer)quest1.targets.get(name1)).intValue()) {
               int amount = 0;
               if(killed1.containsKey(name1)) {
                  amount = ((Integer)killed1.get(name1)).intValue();
               }

               killed1.put(name1, Integer.valueOf(amount + 1));
               quest1.setKilled(data, killed1);
               change = true;
            }
         }
      }

      if(change) {
         playerdata.checkQuestCompletion(player, EnumQuestType.KILL);
      }
   }

   @SubscribeEvent
   public void pickUp(EntityItemPickupEvent event) {
      if(!event.entityPlayer.worldObj.isRemote) {
         PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(event.entityPlayer).questData;
         playerdata.checkQuestCompletion(event.entityPlayer, EnumQuestType.ITEM);
      }
   }

   @SubscribeEvent
   public void world(EntityJoinWorldEvent event) {
      if(!event.world.isRemote && event.entity instanceof EntityPlayer) {
         PlayerData data = PlayerDataController.instance.getPlayerData((EntityPlayer)event.entity);
         data.updateCompanion(event.world);
      }
   }

   @SubscribeEvent
   public void populateChunk(Post event) {
      NPCSpawning.performWorldGenSpawning(event.world, event.chunkX, event.chunkZ, event.rand);
   }
}
