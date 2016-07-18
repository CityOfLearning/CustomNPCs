//

//

package noppes.npcs.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class ItemNpcMovingPath extends Item implements IPermission {
	public ItemNpcMovingPath() {
		maxStackSize = 1;
		setCreativeTab(CustomItems.tab);
	}

	@Override
	public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
		return 9127187;
	}

	private EntityNPCInterface getNpc(final ItemStack item, final World world) {
		if (world.isRemote || (item.getTagCompound() == null)) {
			return null;
		}
		final Entity entity = world.getEntityByID(item.getTagCompound().getInteger("NPCID"));
		if ((entity == null) || !(entity instanceof EntityNPCInterface)) {
			return null;
		}
		return (EntityNPCInterface) entity;
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return (e == EnumPacketServer.MovingPathGet) || (e == EnumPacketServer.MovingPathSave);
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World,
			final EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			if (CustomNpcsPermissions.hasPermission(par3EntityPlayer, CustomNpcsPermissions.TOOL_MOUNTER)) {
				final EntityNPCInterface npc = getNpc(par1ItemStack, par2World);
				if (npc != null) {
					NoppesUtilServer.sendOpenGui(par3EntityPlayer, EnumGuiType.MovingPath, npc);
				}
				return par1ItemStack;
			}
		}
		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World world, final BlockPos bpos,
			final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!world.isRemote) {
			if (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.TOOL_MOUNTER)) {
				final EntityNPCInterface npc = getNpc(stack, world);
				if (npc == null) {
					return true;
				}
				final List<int[]> list = npc.ai.getMovingPath();
				final int[] pos = list.get(list.size() - 1);
				final int x = bpos.getX();
				final int y = bpos.getY();
				final int z = bpos.getZ();
				list.add(new int[] { x, y, z });
				final double d3 = x - pos[0];
				final double d4 = y - pos[1];
				final double d5 = z - pos[2];
				final double distance = MathHelper.sqrt_double((d3 * d3) + (d4 * d4) + (d5 * d5));
				player.addChatMessage(new ChatComponentText(
						"Added point x:" + x + " y:" + y + " z:" + z + " to npc " + npc.getName()));
				if (distance > CustomNpcs.NpcNavRange) {
					player.addChatMessage(new ChatComponentText(
							"Warning: point is too far away from previous point. Max block walk distance = "
									+ CustomNpcs.NpcNavRange));
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public Item setUnlocalizedName(final String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
