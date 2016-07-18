//

//

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
	public static final PropertyInteger DAMAGE;

	static {
		DAMAGE = PropertyInteger.create("damage", 0, 15);
	}

	public BlockCrystal() {
		super(Material.glass, false);
		setLightLevel(0.8f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(final IBlockAccess world, final BlockPos pos, final int pass) {
		return getRenderColor(world.getBlockState(pos));
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockCrystal.DAMAGE });
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return state.getValue(BlockCrystal.DAMAGE);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	@Override
	public MapColor getMapColor(final IBlockState state) {
		return EnumDyeColor.byDyeDamage(damageDropped(state)).getMapColor();
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return damageDropped(state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(final IBlockState state) {
		return getMapColor(state).colorValue;
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(BlockCrystal.DAMAGE, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
		for (int i = 0; i < 16; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName() {
		return "item.npcCrystal";
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
