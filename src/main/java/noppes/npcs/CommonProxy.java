//

//

package noppes.npcs;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerCarpentryBench;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.containers.ContainerMerchantAdd;
import noppes.npcs.containers.ContainerNPCBankLarge;
import noppes.npcs.containers.ContainerNPCBankSmall;
import noppes.npcs.containers.ContainerNPCBankUnlock;
import noppes.npcs.containers.ContainerNPCBankUpgrade;
import noppes.npcs.containers.ContainerNPCCompanion;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.containers.ContainerNPCFollowerSetup;
import noppes.npcs.containers.ContainerNPCInv;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.containers.ContainerNpcItemGiver;
import noppes.npcs.containers.ContainerNpcQuestReward;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;
import noppes.npcs.containers.ContainerTradingBlock;
import noppes.npcs.entity.EntityNPCInterface;

public class CommonProxy implements IGuiHandler {
	public boolean newVersionAvailable;
	public int revision;

	public CommonProxy() {
		newVersionAvailable = false;
		revision = 4;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public Container getContainer(EnumGuiType gui, EntityPlayer player, int x, int y, int z, EntityNPCInterface npc) {
		if (gui == EnumGuiType.MainMenuInv) {
			return new ContainerNPCInv(npc, player);
		}
		if (gui == EnumGuiType.PlayerBankSmall) {
			return new ContainerNPCBankSmall(player, x, y);
		}
		if (gui == EnumGuiType.PlayerBankUnlock) {
			return new ContainerNPCBankUnlock(player, x, y);
		}
		if (gui == EnumGuiType.PlayerBankUprade) {
			return new ContainerNPCBankUpgrade(player, x, y);
		}
		if (gui == EnumGuiType.PlayerBankLarge) {
			return new ContainerNPCBankLarge(player, x, y);
		}
		if (gui == EnumGuiType.PlayerFollowerHire) {
			return new ContainerNPCFollowerHire(npc, player);
		}
		if (gui == EnumGuiType.PlayerFollower) {
			return new ContainerNPCFollower(npc, player);
		}
		if (gui == EnumGuiType.PlayerTrader) {
			return new ContainerNPCTrader(npc, player);
		}
		if (gui == EnumGuiType.PlayerAnvil) {
			return new ContainerCarpentryBench(player.inventory, player.worldObj, new BlockPos(x, y, z));
		}
		if (gui == EnumGuiType.SetupItemGiver) {
			return new ContainerNpcItemGiver(npc, player);
		}
		if (gui == EnumGuiType.SetupTrader) {
			return new ContainerNPCTraderSetup(npc, player);
		}
		if (gui == EnumGuiType.SetupFollower) {
			return new ContainerNPCFollowerSetup(npc, player);
		}
		if (gui == EnumGuiType.QuestReward) {
			return new ContainerNpcQuestReward(player);
		}
		if (gui == EnumGuiType.QuestItem) {
			return new ContainerNpcQuestTypeItem(player);
		}
		if (gui == EnumGuiType.ManageRecipes) {
			return new ContainerManageRecipes(player, x);
		}
		if (gui == EnumGuiType.ManageBanks) {
			return new ContainerManageBanks(player);
		}
		if (gui == EnumGuiType.MerchantAdd) {
			return new ContainerMerchantAdd(player, ServerEventsHandler.Merchant, player.worldObj);
		}
		if (gui == EnumGuiType.PlayerMailman) {
			return new ContainerMail(player, x == 1, y == 1);
		}
		if (gui == EnumGuiType.CompanionInv) {
			return new ContainerNPCCompanion(npc, player);
		}
		if (gui == EnumGuiType.TradingBlock) {
			return new ContainerTradingBlock(player, new BlockPos(x, y, z));
		}
		return null;
	}

	public EntityPlayer getPlayer() {
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID > EnumGuiType.values().length) {
			return null;
		}
		EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
		EnumGuiType gui = EnumGuiType.values()[ID];
		return getContainer(gui, player, x, y, z, npc);
	}

	public ModelBiped getSkirtModel() {
		return null;
	}

	public boolean hasClient() {
		return false;
	}

	public void load() {
		CustomNpcs.Channel.register(new PacketHandlerServer());
		CustomNpcs.ChannelPlayer.register(new PacketHandlerPlayer());
	}

	public void openGui(EntityNPCInterface npc, EnumGuiType gui) {
	}

	public void openGui(EntityNPCInterface npc, EnumGuiType gui, int x, int y, int z) {
	}

	public void openGui(EntityPlayer player, Object guiscreen) {
	}

	public void openGui(int i, int j, int k, EnumGuiType gui, EntityPlayer player) {
	}

	public void postload() {
	}

	public void registerBlock(Block block, String name, int meta, Class<? extends ItemBlock> itemclass) {
		this.registerBlock(block, name, meta, itemclass, false);
	}

	public void registerBlock(Block block, String name, int meta, Class<? extends ItemBlock> itemclass,
			boolean seperateMetadata) {
		GameRegistry.registerBlock(block, itemclass, name);
	}

	public void registerItem(Item item, String name, int meta) {
	}

	public void spawnParticle(EntityLivingBase player, String string, Object... ob) {
	}

	public void spawnParticle(EnumParticleTypes type, double x, double y, double z, double motionX, double motionY,
			double motionZ, float scale) {
	}
}
