package noppes.npcs.blocks;

import java.util.List;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystal extends BlockBreakable {

   public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 15);


   public BlockCrystal() {
      super(Material.glass, false);
      this.setLightLevel(0.8F);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.TRANSLUCENT;
   }

   public int getMetaFromState(IBlockState state) {
      return this.damageDropped(state);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(DAMAGE, Integer.valueOf(meta));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{DAMAGE});
   }

   public int damageDropped(IBlockState state) {
      return ((Integer)state.getValue(DAMAGE)).intValue();
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs tab, List list) {
      for(int i = 0; i < 16; ++i) {
         list.add(new ItemStack(item, 1, i));
      }

   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess world, BlockPos pos, int pass) {
      return this.getRenderColor(world.getBlockState(pos));
   }

   @SideOnly(Side.CLIENT)
   public int getRenderColor(IBlockState state) {
      return this.getMapColor(state).colorValue;
   }

   public MapColor getMapColor(IBlockState state) {
      return EnumDyeColor.byDyeDamage(this.damageDropped(state)).getMapColor();
   }

   public String getUnlocalizedName() {
      return "item.npcCrystal";
   }

}
