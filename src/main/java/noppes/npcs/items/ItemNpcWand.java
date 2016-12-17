
package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.util.CustomNPCsScheduler;
import noppes.npcs.util.IPermission;
import noppes.npcs.util.NoppesUtilServer;

public class ItemNpcWand extends Item implements IPermission {
	public ItemNpcWand() {
		maxStackSize = 1;
		setCreativeTab(CustomItems.tab);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return 9127187;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public boolean isAllowed(EnumPacketServer e) {
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World worldObj, EntityPlayer par3EntityPlayer) {
		if (!worldObj.isRemote) {
			return par1ItemStack;
		}
		CustomNpcs.proxy.openGui(0, 0, 0, EnumGuiType.NpcRemote, par3EntityPlayer);
		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos bpos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		if (CustomNpcs.OpsOnly
				&& !MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile())) {
			player.addChatMessage(new ChatComponentTranslation("availability.permission", new Object[0]));
		} else {
			if (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.NPC_CREATE)) {
				EntityCustomNpc npc = new EntityCustomNpc(world);
				npc.ai.setStartPos(bpos.up());
				npc.setLocationAndAngles(bpos.getX() + 0.5f, npc.getStartYPos(), bpos.getZ() + 0.5f, player.rotationYaw,
						player.rotationPitch);
				world.spawnEntityInWorld(npc);
				npc.setHealth(npc.getMaxHealth());
				CustomNPCsScheduler
						.runTack(() -> NoppesUtilServer.sendOpenGui(player, EnumGuiType.MainMenuDisplay, npc), 100);
			} else {
				player.addChatMessage(new ChatComponentTranslation("availability.permission", new Object[0]));
			}
		}
		return true;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		return stack;
	}

	@Override
	public Item setUnlocalizedName(String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
