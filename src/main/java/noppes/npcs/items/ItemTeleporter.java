
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
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return 9127187;
	}

	@Override
	public boolean isAllowed(EnumPacketServer e) {
		return (e == EnumPacketServer.DimensionsGet) || (e == EnumPacketServer.DimensionTeleport);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase par3EntityPlayer, ItemStack stack) {
		if (par3EntityPlayer.worldObj.isRemote) {
			return false;
		}
		float f = 1.0f;
		float f2 = par3EntityPlayer.prevRotationPitch
				+ ((par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f);
		float f3 = par3EntityPlayer.prevRotationYaw
				+ ((par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f);
		double d0 = par3EntityPlayer.prevPosX + ((par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * f);
		double d2 = par3EntityPlayer.prevPosY + ((par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * f) + 1.62;
		double d3 = par3EntityPlayer.prevPosZ + ((par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * f);
		Vec3 vec3 = new Vec3(d0, d2, d3);
		float f4 = MathHelper.cos((-f3 * 0.017453292f) - 3.1415927f);
		float f5 = MathHelper.sin((-f3 * 0.017453292f) - 3.1415927f);
		float f6 = -MathHelper.cos(-f2 * 0.017453292f);
		float f7 = MathHelper.sin(-f2 * 0.017453292f);
		float f8 = f5 * f6;
		float f9 = f4 * f6;
		double d4 = 80.0;
		Vec3 vec4 = vec3.addVector(f8 * d4, f7 * d4, f9 * d4);
		MovingObjectPosition movingobjectposition = par3EntityPlayer.worldObj.rayTraceBlocks(vec3, vec4, true);
		if (movingobjectposition == null) {
			return false;
		}
		Vec3 vec5 = par3EntityPlayer.getLook(f);
		boolean flag = false;
		float f10 = 1.0f;
		List list = par3EntityPlayer.worldObj.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer,
				par3EntityPlayer.getEntityBoundingBox().addCoord(vec5.xCoord * d4, vec5.yCoord * d4, vec5.zCoord * d4)
						.expand(f10, f10, f10));
		for (int i = 0; i < list.size(); ++i) {
			Entity entity = (Entity) list.get(i);
			if (entity.canBeCollidedWith()) {
				float f11 = entity.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f11, f11, f11);
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
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			return par1ItemStack;
		}
		CustomNpcs.proxy.openGui((EntityNPCInterface) null, EnumGuiType.NpcDimensions);
		return par1ItemStack;
	}

	@Override
	public Item setUnlocalizedName(String name) {
		GameRegistry.registerItem(this, name);
		CustomNpcs.proxy.registerItem(this, name, 0);
		return super.setUnlocalizedName(name);
	}
}
