package noppes.npcs;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.oredict.OreDictionary;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomTeleporter;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.QuestLogData;
import noppes.npcs.Server;
import noppes.npcs.api.constants.EnumOptionType;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.controllers.Bank;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.BankData;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.PlayerBankData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.PlayerTransportData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleDialog;
import noppes.npcs.roles.RoleFollower;

public class NoppesUtilPlayer {

   public static void changeFollowerState(EntityPlayerMP player, EntityNPCInterface npc) {
      if(npc.advanced.role == 2) {
         RoleFollower role = (RoleFollower)npc.roleInterface;
         EntityPlayer owner = role.owner;
         if(owner != null && owner.getName().equals(player.getName())) {
            role.isFollowing = !role.isFollowing;
         }
      }
   }

   public static void hireFollower(EntityPlayerMP player, EntityNPCInterface npc) {
      if(npc.advanced.role == 2) {
         Container con = player.openContainer;
         if(con != null && con instanceof ContainerNPCFollowerHire) {
            ContainerNPCFollowerHire container = (ContainerNPCFollowerHire)con;
            RoleFollower role = (RoleFollower)npc.roleInterface;
            followerBuy(role, container.currencyMatrix, player, npc);
         }
      }
   }

   public static void extendFollower(EntityPlayerMP player, EntityNPCInterface npc) {
      if(npc.advanced.role == 2) {
         Container con = player.openContainer;
         if(con != null && con instanceof ContainerNPCFollower) {
            ContainerNPCFollower container = (ContainerNPCFollower)con;
            RoleFollower role = (RoleFollower)npc.roleInterface;
            followerBuy(role, container.currencyMatrix, player, npc);
         }
      }
   }

   public static void transport(EntityPlayerMP player, EntityNPCInterface npc, String location) {
      TransportLocation loc = TransportController.getInstance().getTransport(location);
      PlayerTransportData playerdata = PlayerDataController.instance.getPlayerData(player).transportData;
      if(loc != null && (loc.isDefault() || playerdata.transports.contains(Integer.valueOf(loc.id)))) {
         RoleEvent.TransporterUseEvent event = new RoleEvent.TransporterUseEvent(player, npc.wrappedNPC);
         if(!EventHooks.onNPCRole(npc, event)) {
            teleportPlayer(player, loc.pos, loc.dimension);
         }
      }
   }

   public static void teleportPlayer(EntityPlayerMP player, BlockPos pos, int dimension) {
      if(player.dimension != dimension) {
         int dim = player.dimension;
         MinecraftServer server = MinecraftServer.getServer();
         WorldServer wor = server.worldServerForDimension(dimension);
         if(wor == null) {
            player.addChatMessage(new ChatComponentText("Broken transporter. Dimenion does not exist"));
            return;
         }

         player.setLocationAndAngles((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), player.rotationYaw, player.rotationPitch);
         server.getConfigurationManager().transferPlayerToDimension(player, dimension, new CustomTeleporter(wor));
         player.playerNetServerHandler.setPlayerLocation((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), player.rotationYaw, player.rotationPitch);
         if(!wor.playerEntities.contains(player)) {
            wor.spawnEntityInWorld(player);
         }
      } else {
         player.playerNetServerHandler.setPlayerLocation((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), player.rotationYaw, player.rotationPitch);
      }

      player.worldObj.updateEntityWithOptionalForce(player, false);
   }

   private static void followerBuy(RoleFollower role, IInventory currencyInv, EntityPlayerMP player, EntityNPCInterface npc) {
      ItemStack currency = currencyInv.getStackInSlot(0);
      if(currency != null) {
         HashMap cd = new HashMap();
         Iterator stackSize = role.inventory.items.keySet().iterator();

         int days;
         int possibleSize;
         while(stackSize.hasNext()) {
            days = ((Integer)stackSize.next()).intValue();
            ItemStack possibleDays = (ItemStack)role.inventory.items.get(Integer.valueOf(days));
            if(possibleDays != null && possibleDays.getItem() == currency.getItem() && (!possibleDays.getHasSubtypes() || possibleDays.getItemDamage() == currency.getItemDamage())) {
               possibleSize = 1;
               if(role.rates.containsKey(Integer.valueOf(days))) {
                  possibleSize = ((Integer)role.rates.get(Integer.valueOf(days))).intValue();
               }

               cd.put(possibleDays, Integer.valueOf(possibleSize));
            }
         }

         if(cd.size() != 0) {
            int stackSize1 = currency.stackSize;
            days = 0;
            int possibleDays1 = 0;
            possibleSize = stackSize1;

            while(true) {
               Iterator event = cd.keySet().iterator();

               while(event.hasNext()) {
                  ItemStack item = (ItemStack)event.next();
                  int rDays = ((Integer)cd.get(item)).intValue();
                  int rValue = item.stackSize;
                  if(rValue <= stackSize1) {
                     int newStackSize = stackSize1 % rValue;
                     int size = stackSize1 - newStackSize;
                     int posDays = size / rValue * rDays;
                     if(possibleDays1 <= posDays) {
                        possibleDays1 = posDays;
                        possibleSize = newStackSize;
                     }
                  }
               }

               if(stackSize1 == possibleSize) {
                  RoleEvent.FollowerHireEvent event1 = new RoleEvent.FollowerHireEvent(player, npc.wrappedNPC, days);
                  if(EventHooks.onNPCRole(npc, event1)) {
                     return;
                  }

                  if(event1.days == 0) {
                     return;
                  }

                  if(stackSize1 <= 0) {
                     currencyInv.setInventorySlotContents(0, (ItemStack)null);
                  } else {
                     currency.splitStack(stackSize1);
                  }

                  npc.say(player, new Line(NoppesStringUtils.formatText(role.dialogHire.replace("{days}", days + ""), new Object[]{player, npc})));
                  role.setOwner(player);
                  role.addDays(days);
                  return;
               }

               stackSize1 = possibleSize;
               days += possibleDays1;
               possibleDays1 = 0;
            }
         }
      }
   }

   public static void bankUpgrade(EntityPlayerMP player, EntityNPCInterface npc) {
      if(npc.advanced.role == 3) {
         Container con = player.openContainer;
         if(con != null && con instanceof ContainerNPCBankInterface) {
            ContainerNPCBankInterface container = (ContainerNPCBankInterface)con;
            Bank bank = BankController.getInstance().getBank(container.bankid);
            ItemStack item = bank.upgradeInventory.getStackInSlot(container.slot);
            if(item != null) {
               int price = item.stackSize;
               ItemStack currency = container.currencyMatrix.getStackInSlot(0);
               if(currency != null && price <= currency.stackSize) {
                  if(currency.stackSize - price == 0) {
                     container.currencyMatrix.setInventorySlotContents(0, (ItemStack)null);
                  } else {
                     currency.splitStack(price);
                  }

                  player.closeContainer();
                  PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
                  BankData bankData = data.getBank(bank.id);
                  bankData.upgradedSlots.put(Integer.valueOf(container.slot), Boolean.valueOf(true));
                  RoleEvent.BankUpgradedEvent event = new RoleEvent.BankUpgradedEvent(player, npc.wrappedNPC, container.slot);
                  EventHooks.onNPCRole(npc, event);
                  bankData.openBankGui(player, npc, bank.id, container.slot);
               }
            }
         }
      }
   }

   public static void bankUnlock(EntityPlayerMP player, EntityNPCInterface npc) {
      if(npc.advanced.role == 3) {
         Container con = player.openContainer;
         if(con != null && con instanceof ContainerNPCBankInterface) {
            ContainerNPCBankInterface container = (ContainerNPCBankInterface)con;
            Bank bank = BankController.getInstance().getBank(container.bankid);
            ItemStack item = bank.currencyInventory.getStackInSlot(container.slot);
            if(item != null) {
               int price = item.stackSize;
               ItemStack currency = container.currencyMatrix.getStackInSlot(0);
               if(currency != null && price <= currency.stackSize) {
                  if(currency.stackSize - price == 0) {
                     container.currencyMatrix.setInventorySlotContents(0, (ItemStack)null);
                  } else {
                     currency.splitStack(price);
                  }

                  player.closeContainer();
                  PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
                  BankData bankData = data.getBank(bank.id);
                  if(bankData.unlockedSlots + 1 <= bank.maxSlots) {
                     ++bankData.unlockedSlots;
                  }

                  RoleEvent.BankUnlockedEvent event = new RoleEvent.BankUnlockedEvent(player, npc.wrappedNPC, container.slot);
                  EventHooks.onNPCRole(npc, event);
                  bankData.openBankGui(player, npc, bank.id, container.slot);
               }
            }
         }
      }
   }

   public static void sendData(EnumPlayerPacket enu, Object ... obs) {
      PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

      try {
         if(!Server.fillBuffer(buffer, enu, obs)) {
            return;
         }

         CustomNpcs.ChannelPlayer.sendToServer(new FMLProxyPacket(buffer, "CustomNPCsPlayer"));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public static void dialogSelected(int dialogId, int optionId, EntityPlayerMP player, EntityNPCInterface npc) {
      if(dialogId < 0 && npc.advanced.role == 7) {
         String dialog1 = (String)((RoleDialog)npc.roleInterface).optionsTexts.get(Integer.valueOf(optionId));
         if(dialog1 != null && !dialog1.isEmpty()) {
            Dialog option1 = new Dialog();
            option1.text = dialog1;
            NoppesUtilServer.openDialog(player, npc, option1);
         }

      } else {
         Dialog dialog = (Dialog)DialogController.instance.dialogs.get(Integer.valueOf(dialogId));
         if(dialog != null) {
            if(dialog.hasDialogs(player) || dialog.hasOtherOptions()) {
               DialogOption option = (DialogOption)dialog.options.get(Integer.valueOf(optionId));
               if(option != null) {
                  if(EventHooks.onNPCDialogOption(npc, player, dialog, option)) {
                     Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
                  } else if((option.optionType != EnumOptionType.DIALOG_OPTION || option.isAvailable(player) && option.hasDialog()) && option.optionType != EnumOptionType.DISABLED && option.optionType != EnumOptionType.QUIT_OPTION) {
                     if(option.optionType == EnumOptionType.ROLE_OPTION) {
                        if(npc.roleInterface != null) {
                           npc.roleInterface.interact(player);
                        } else {
                           Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
                        }
                     } else if(option.optionType == EnumOptionType.DIALOG_OPTION) {
                        NoppesUtilServer.openDialog(player, npc, option.getDialog());
                     } else if(option.optionType == EnumOptionType.COMMAND_BLOCK) {
                        Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
                        NoppesUtilServer.runCommand(npc, npc.getName(), option.command, player);
                     } else {
                        Server.sendData(player, EnumPacketClient.GUI_CLOSE, new Object[0]);
                     }

                  }
               }
            }
         }
      }
   }

   public static void sendQuestLogData(EntityPlayerMP player) {
      if(PlayerQuestController.hasActiveQuests(player)) {
         QuestLogData data = new QuestLogData();
         data.setData(player);
         Server.sendData(player, EnumPacketClient.GUI_DATA, new Object[]{data.writeNBT()});
      }
   }

   public static void questCompletion(EntityPlayerMP player, int questId) {
      PlayerQuestData playerdata = PlayerDataController.instance.getPlayerData(player).questData;
      QuestData data = (QuestData)playerdata.activeQuests.get(Integer.valueOf(questId));
      if(data != null) {
         if(data.quest.questInterface.isCompleted(player)) {
            EventHooks.onQuestTurnedIn(player, data.quest);
            data.quest.questInterface.handleComplete(player);
            if(data.quest.rewardExp > 0) {
               player.worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 0.5F * ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.8F));
               player.addExperience(data.quest.rewardExp);
            }

            data.quest.factionOptions.addPoints(player);
            if(data.quest.mail.isValid()) {
               PlayerDataController.instance.addPlayerMessage(player.getName(), data.quest.mail);
            }

            if(!data.quest.randomReward) {
               Iterator list = data.quest.rewardItems.items.values().iterator();

               while(list.hasNext()) {
                  ItemStack item = (ItemStack)list.next();
                  NoppesUtilServer.GivePlayerItem(player, player, item);
               }
            } else {
               ArrayList list1 = new ArrayList();
               Iterator item2 = data.quest.rewardItems.items.values().iterator();

               while(item2.hasNext()) {
                  ItemStack item1 = (ItemStack)item2.next();
                  if(item1 != null && item1.getItem() != null) {
                     list1.add(item1);
                  }
               }

               if(!list1.isEmpty()) {
                  NoppesUtilServer.GivePlayerItem(player, player, (ItemStack)list1.get(player.getRNG().nextInt(list1.size())));
               }
            }

            if(!data.quest.command.isEmpty()) {
               NoppesUtilServer.runCommand(player, "QuestCompletion", data.quest.command);
            }

            PlayerQuestController.setQuestFinished(data.quest, player);
            if(data.quest.hasNewQuest()) {
               PlayerQuestController.addActiveQuest(data.quest.getNextQuest(), player);
            }

         }
      }
   }

   public static boolean compareItems(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT) {
      if(item2 != null && item != null) {
         boolean oreMatched = false;
         OreDictionary.itemMatches(item, item2, false);
         int[] ids = OreDictionary.getOreIDs(item);
         if(ids.length > 0) {
            int[] var6 = ids;
            int var7 = ids.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               int id = var6[var8];
               boolean match1 = false;
               boolean match2 = false;
               Iterator var12 = OreDictionary.getOres(OreDictionary.getOreName(id)).iterator();

               while(var12.hasNext()) {
                  ItemStack is = (ItemStack)var12.next();
                  if(compareItemDetails(item, is, ignoreDamage, ignoreNBT)) {
                     match1 = true;
                  }

                  if(compareItemDetails(item2, is, ignoreDamage, ignoreNBT)) {
                     match2 = true;
                  }
               }

               if(match1 && match2) {
                  return true;
               }
            }
         }

         return compareItemDetails(item, item2, ignoreDamage, ignoreNBT);
      } else {
         return false;
      }
   }

   private static boolean compareItemDetails(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT) {
      return item.getItem() != item2.getItem()?false:(!ignoreDamage && item.getItemDamage() != -1 && item.getItemDamage() != item2.getItemDamage()?false:(!ignoreNBT && item.getTagCompound() != null && (item2.getTagCompound() == null || !item.getTagCompound().equals(item2.getTagCompound()))?false:ignoreNBT || item2.getTagCompound() == null || item.getTagCompound() != null));
   }

   public static boolean compareItems(EntityPlayer player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
      int size = 0;
      ItemStack[] var5 = player.inventory.mainInventory;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ItemStack is = var5[var7];
         if(is != null && compareItems(item, is, ignoreDamage, ignoreNBT)) {
            size += is.stackSize;
         }
      }

      return size >= item.stackSize;
   }

   public static void consumeItem(EntityPlayer player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
      if(item != null) {
         int size = item.stackSize;

         for(int i = 0; i < player.inventory.mainInventory.length; ++i) {
            ItemStack is = player.inventory.mainInventory[i];
            if(is != null && compareItems(item, is, ignoreDamage, ignoreNBT)) {
               if(size < is.stackSize) {
                  player.inventory.mainInventory[i].splitStack(size);
                  break;
               }

               size -= is.stackSize;
               player.inventory.mainInventory[i] = null;
            }
         }

      }
   }
}
