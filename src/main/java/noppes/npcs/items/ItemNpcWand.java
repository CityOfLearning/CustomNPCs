//

//

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
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.util.CustomNPCsScheduler;
import noppes.npcs.util.IPermission;

public class ItemNpcWand extends Item implements IPermission {
	public ItemNpcWand() {
		maxStackSize = 1;
		setCreativeTab(CustomItems.tab);
	}

	@Override
	public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
		return 9127187;
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return true;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World worldObj,
			final EntityPlayer par3EntityPlayer) {
		if (!worldObj.isRemote) {
			return par1ItemStack;
		}
		CustomNpcs.proxy.openGui(0, 0, 0, EnumGuiType.NpcRemote, par3EntityPlayer);
		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World world, final BlockPos bpos,
			final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (world.isRemote) {
			return true;
		}
		if (CustomNpcs.OpsOnly
				&& !MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile())) {
			player.addChatMessage(new ChatComponentTranslation("availability.permission", new Object[0]));
		} else {
			if (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.NPC_CREATE)) {
				final EntityCustomNpc npc = new EntityCustomNpc(world);
				npc.ai.setStartPos(bpos.up());
				npc.setLocationAndAngles(bpos.getX() + 0.5f, npc.getStartYPos(), bpos.getZ() + 0.5f, player.rotationYaw,
						player.rotationPitch);
				world.spawnEntityInWorld(npc);
				npc.setHealth(npc.getMaxHealth());
				CustomNPCsScheduler.runTack(new Runnable() {
					@Override
					public void run() {
						NoppesUtilServer.sendOpenGui(player, EnumGuiType.MainMenuDisplay, npc);
					}
				}, 100);
			} else {
				player.addChatMessage(new ChatComponentTranslation("availability.permission", new Object[0]));
			}
		}
		return true;
	}

	@Override
	public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
		return stack;
	}

	@Override
	public Item setUnlocalizedName(final String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
