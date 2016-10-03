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
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
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
      this.maxStackSize = 1;
      this.setCreativeTab(CustomItems.tab);
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if(!par2World.isRemote) {
         return par1ItemStack;
      } else {
         CustomNpcs.proxy.openGui((EntityNPCInterface)null, EnumGuiType.NpcDimensions);
         return par1ItemStack;
      }
   }

   public boolean onEntitySwing(EntityLivingBase par3EntityPlayer, ItemStack stack) {
      if(par3EntityPlayer.worldObj.isRemote) {
         return false;
      } else {
         float f = 1.0F;
         float f1 = par3EntityPlayer.prevRotationPitch + (par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f;
         float f2 = par3EntityPlayer.prevRotationYaw + (par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f;
         double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)f;
         double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)f + 1.62D;
         double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)f;
         Vec3 vec3 = new Vec3(d0, d1, d2);
         float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
         float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
         float f5 = -MathHelper.cos(-f1 * 0.017453292F);
         float f6 = MathHelper.sin(-f1 * 0.017453292F);
         float f7 = f4 * f5;
         float f8 = f3 * f5;
         double d3 = 80.0D;
         Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
         MovingObjectPosition movingobjectposition = par3EntityPlayer.worldObj.rayTraceBlocks(vec3, vec31, true);
         if(movingobjectposition == null) {
            return false;
         } else {
            Vec3 vec32 = par3EntityPlayer.getLook(f);
            boolean flag = false;
            float f9 = 1.0F;
            List list = par3EntityPlayer.worldObj.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer, par3EntityPlayer.getEntityBoundingBox().addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand((double)f9, (double)f9, (double)f9));

            for(int pos = 0; pos < list.size(); ++pos) {
               Entity entity = (Entity)list.get(pos);
               if(entity.canBeCollidedWith()) {
                  float f10 = entity.getCollisionBorderSize();
                  AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)f10, (double)f10, (double)f10);
                  if(axisalignedbb.isVecInside(vec3)) {
                     flag = true;
                  }
               }
            }

            if(flag) {
               return false;
            } else {
               if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
                  BlockPos var31;
                  for(var31 = movingobjectposition.getBlockPos(); par3EntityPlayer.worldObj.getBlockState(var31).getBlock() != Blocks.air; var31 = var31.up()) {
                     ;
                  }

                  par3EntityPlayer.setPositionAndUpdate((double)((float)var31.getX() + 0.5F), (double)((float)var31.getY() + 1.0F), (double)((float)var31.getZ() + 0.5F));
               }

               return true;
            }
         }
      }
   }

   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      return 9127187;
   }

   public Item setUnlocalizedName(String name) {
      GameRegistry.registerItem(this, name);
      CustomNpcs.proxy.registerItem(this, name, 0);
      return super.setUnlocalizedName(name);
   }

   public boolean isAllowed(EnumPacketServer e) {
      return e == EnumPacketServer.DimensionsGet || e == EnumPacketServer.DimensionTeleport;
   }
}
