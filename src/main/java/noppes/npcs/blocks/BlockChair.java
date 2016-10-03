package noppes.npcs.blocks;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noppes.npcs.blocks.BlockRotated;
import noppes.npcs.blocks.tiles.TileChair;
import noppes.npcs.entity.EntityChairMount;
import noppes.npcs.entity.EntityCustomNpc;

public class BlockChair extends BlockRotated {

   public BlockChair() {
      super(Blocks.planks);
      this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
   }

   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
      world.setBlockState(pos, state.withProperty(DAMAGE, Integer.valueOf(stack.getItemDamage())), 2);
      super.onBlockPlacedBy(world, pos, state, entity, stack);
   }

   public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
      return new AxisAlignedBB((double)((float)pos.getX() + 0.1F), (double)pos.getY(), (double)((float)pos.getZ() + 0.1F), (double)((float)pos.getX() + 0.9F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.9F));
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 4));
      par3List.add(new ItemStack(par1, 1, 5));
   }

   public int damageDropped(IBlockState state) {
      return ((Integer)state.getValue(DAMAGE)).intValue();
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileChair();
   }

   public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
      return MountBlock(world, pos, player);
   }

   public static boolean MountBlock(World world, BlockPos pos, EntityPlayer player) {
      if(world.isRemote) {
         return true;
      } else {
         List list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.fromBounds((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1)));
         Iterator mount = list.iterator();

         Entity entity;
         do {
            if(!mount.hasNext()) {
               EntityChairMount mount1 = new EntityChairMount(world);
               mount1.setPosition((double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)pos.getZ() + 0.5D);
               world.spawnEntityInWorld(mount1);
               player.mountEntity(mount1);
               return true;
            }

            entity = (Entity)mount.next();
         } while(!(entity instanceof EntityChairMount) && !(entity instanceof EntityCustomNpc));

         return false;
      }
   }
}
