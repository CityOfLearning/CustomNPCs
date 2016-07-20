//

//

package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class BlockBuilder extends BlockContainer implements IPermission {
	public static PropertyInteger ROTATION;

	static {
		ROTATION = PropertyInteger.create("rotation", 0, 3);
	}

	public BlockBuilder() {
		super(Material.rock);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if ((TileBuilder.DrawPos != null) && TileBuilder.DrawPos.equals(pos)) {
			TileBuilder.SetDrawPos(null);
		}
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { BlockBuilder.ROTATION });
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileBuilder();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockBuilder.ROTATION);
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockBuilder.ROTATION, meta);
	}

	@Override
	public boolean isAllowed(EnumPacketServer e) {
		return (e == EnumPacketServer.SchematicsSet) || (e == EnumPacketServer.SchematicsTile)
				|| (e == EnumPacketServer.SchematicsTileSave);
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (par1World.isRemote) {
			return true;
		}
		ItemStack currentItem = player.inventory.getCurrentItem();
		if ((currentItem != null) && (currentItem.getItem() == CustomItems.wand)) {
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.BuilderBlock, null, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
			ItemStack stack) {
		int var6 = MathHelper.floor_double((entity.rotationYaw / 90.0f) + 0.5) & 0x3;
		world.setBlockState(pos, state.withProperty(BlockBuilder.ROTATION, var6), 2);
		if ((entity instanceof EntityPlayer) && !world.isRemote) {
			NoppesUtilServer.sendOpenGui((EntityPlayer) entity, EnumGuiType.BuilderBlock, null, pos.getX(), pos.getY(),
					pos.getZ());
		}
	}
}
