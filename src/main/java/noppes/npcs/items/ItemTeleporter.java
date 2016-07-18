//

//

package noppes.npcs.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class ItemTeleporter extends Item implements IPermission {
	public ItemTeleporter() {
		maxStackSize = 1;
		setCreativeTab(CustomItems.tab);
	}

	@Override
	public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
		return 9127187;
	}

	@Override
	public boolean isAllowed(final EnumPacketServer e) {
		return (e == EnumPacketServer.DimensionsGet) || (e == EnumPacketServer.DimensionTeleport);
	}

	@Override
	public boolean onEntitySwing(final EntityLivingBase par3EntityPlayer, final ItemStack stack) {
		if (par3EntityPlayer.worldObj.isRemote) {
			return false;
		}
		final float f = 1.0f;
		final float f2 = par3EntityPlayer.prevRotationPitch
				+ ((par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f);
		final float f3 = par3EntityPlayer.prevRotationYaw
				+ ((par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f);
		final double d0 = par3EntityPlayer.prevPosX + ((par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * f);
		final double d2 = par3EntityPlayer.prevPosY + ((par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * f) + 1.62;
		final double d3 = par3EntityPlayer.prevPosZ + ((par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * f);
		final Vec3 vec3 = new Vec3(d0, d2, d3);
		final float f4 = MathHelper.cos((-f3 * 0.017453292f) - 3.1415927f);
		final float f5 = MathHelper.sin((-f3 * 0.017453292f) - 3.1415927f);
		final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
		final float f7 = MathHelper.sin(-f2 * 0.017453292f);
		final float f8 = f5 * f6;
		final float f9 = f4 * f6;
		final double d4 = 80.0;
		final Vec3 vec4 = vec3.addVector(f8 * d4, f7 * d4, f9 * d4);
		final MovingObjectPosition movingobjectposition = par3EntityPlayer.worldObj.rayTraceBlocks(vec3, vec4, true);
		if (movingobjectposition == null) {
			return false;
		}
		final Vec3 vec5 = par3EntityPlayer.getLook(f);
		boolean flag = false;
		final float f10 = 1.0f;
		final List list = par3EntityPlayer.worldObj.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer,
				par3EntityPlayer.getEntityBoundingBox().addCoord(vec5.xCoord * d4, vec5.yCoord * d4, vec5.zCoord * d4)
						.expand(f10, f10, f10));
		for (int i = 0; i < list.size(); ++i) {
			final Entity entity = (Entity) list.get(i);
			if (entity.canBeCollidedWith()) {
				final float f11 = entity.getCollisionBorderSize();
				final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f11, f11, f11);
				if (axisalignedbb.isVecInside(vec3)) {
					flag = true;
				}
			}
		}
		if (flag) {
			return false;
		}
		if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			BlockPos pos;
			for (pos = movingobjectposition.getBlockPos(); par3EntityPlayer.worldObj.getBlockState(pos)
					.getBlock() != Blocks.air; pos = pos.up()) {
			}
			par3EntityPlayer.setPositionAndUpdate(pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f);
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World,
			final EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			return par1ItemStack;
		}
		CustomNpcs.proxy.openGui((EntityNPCInterface) null, EnumGuiType.NpcDimensions);
		return par1ItemStack;
	}

	@Override
	public Item setUnlocalizedName(final String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
