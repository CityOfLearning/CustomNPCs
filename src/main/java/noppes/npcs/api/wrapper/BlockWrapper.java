//

//

package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.blocks.tiles.TileNpcEntity;
import noppes.npcs.controllers.ScriptController;

public class BlockWrapper implements IBlock {
	protected final IWorld world;
	protected final Block block;
	protected final BlockPos pos;
	protected final TileEntity tile;
	private final IData tempdata;
	private final IData storeddata;

	public BlockWrapper(final World world, final Block block, final BlockPos pos) {
		tempdata = new IData() {
			@Override
			public void clear() {
				if (!(tile instanceof TileNpcEntity)) {
					return;
				}
				((TileNpcEntity) tile).tempData.clear();
			}

			@Override
			public Object get(final String key) {
				if (!(tile instanceof TileNpcEntity)) {
					return null;
				}
				return ((TileNpcEntity) tile).tempData.get(key);
			}

			@Override
			public boolean has(final String key) {
				return (tile instanceof TileNpcEntity) && ((TileNpcEntity) tile).tempData.containsKey(key);
			}

			@Override
			public void put(final String key, final Object value) {
				if (!(tile instanceof TileNpcEntity)) {
					return;
				}
				((TileNpcEntity) tile).tempData.put(key, value);
			}

			@Override
			public void remove(final String key) {
				if (!(tile instanceof TileNpcEntity)) {
					return;
				}
				((TileNpcEntity) tile).tempData.remove(key);
			}
		};
		storeddata = new IData() {
			@Override
			public void clear() {
				if (!(tile instanceof TileNpcEntity)) {
					return;
				}
				((TileNpcEntity) tile).extraData = new NBTTagCompound();
			}

			@Override
			public Object get(final String key) {
				if (!(tile instanceof TileNpcEntity)) {
					return null;
				}
				final NBTTagCompound compound = ((TileNpcEntity) tile).extraData;
				if (!compound.hasKey(key)) {
					return null;
				}
				final NBTBase base = compound.getTag(key);
				if (base instanceof NBTBase.NBTPrimitive) {
					return ((NBTBase.NBTPrimitive) base).getDouble();
				}
				return ((NBTTagString) base).getString();
			}

			@Override
			public boolean has(final String key) {
				return (tile instanceof TileNpcEntity) && ((TileNpcEntity) tile).extraData.hasKey(key);
			}

			@Override
			public void put(final String key, final Object value) {
				if (!(tile instanceof TileNpcEntity)) {
					return;
				}
				final NBTTagCompound compound = ((TileNpcEntity) tile).extraData;
				if (value instanceof Number) {
					compound.setDouble(key, ((Number) value).doubleValue());
				} else if (value instanceof String) {
					compound.setString(key, (String) value);
				}
				ScriptController.Instance.shouldSave = true;
			}

			@Override
			public void remove(final String key) {
				if (!(tile instanceof TileNpcEntity)) {
					return;
				}
				((TileNpcEntity) tile).extraData.removeTag(key);
			}
		};
		this.world = new WorldWrapper(world);
		this.block = block;
		this.pos = pos;
		tile = world.getTileEntity(pos);
	}

	@Override
	public boolean canStoreData() {
		return tile instanceof TileNpcEntity;
	}

	@Override
	public int getContainerSize() {
		if ((tile == null) || !(tile instanceof IInventory)) {
			return -1;
		}
		return ((IInventory) tile).getSizeInventory();
	}

	@Override
	public IItemStack getContainerSlot(final int slot) {
		if ((tile == null) || !(tile instanceof IInventory)) {
			return null;
		}
		final IInventory inv = (IInventory) tile;
		if ((slot < 0) || (slot >= inv.getSizeInventory())) {
			return null;
		}
		return new ItemStackWrapper(inv.getStackInSlot(slot));
	}

	@Override
	public Block getMCBlock() {
		return block;
	}

	@Override
	public TileEntity getMCTileEntity() {
		return tile;
	}

	@Override
	public String getName() {
		return Block.blockRegistry.getNameForObject(block) + "";
	}

	@Override
	public IData getStoreddata() {
		return storeddata;
	}

	@Override
	public IData getTempdata() {
		return tempdata;
	}

	@Override
	public IWorld getWorld() {
		return world;
	}

	@Override
	public int getX() {
		return pos.getX();
	}

	@Override
	public int getY() {
		return pos.getY();
	}

	@Override
	public int getZ() {
		return pos.getZ();
	}

	@Override
	public boolean isContainer() {
		return (tile != null) && (tile instanceof IInventory) && (((IInventory) tile).getSizeInventory() > 0);
	}

	@Override
	public boolean isRemoved() {
		final IBlockState state = world.getMCWorld().getBlockState(pos);
		return (state == null) || (state.getBlock() != block);
	}

	@Override
	public void remove() {
		world.getMCWorld().setBlockToAir(pos);
	}

	@Override
	public BlockWrapper setBlock(final IBlock block) {
		world.getMCWorld().setBlockState(pos, block.getMCBlock().getDefaultState());
		return new BlockWrapper(world.getMCWorld(), block.getMCBlock(), pos);
	}

	@Override
	public BlockWrapper setBlock(final String name) {
		final Block block = Block.blockRegistry.getObject(new ResourceLocation(name));
		if (block == null) {
			return this;
		}
		world.getMCWorld().setBlockState(pos, block.getDefaultState());
		return new BlockWrapper(world.getMCWorld(), block, pos);
	}

	@Override
	public void setContainerSlot(final int slot, final IItemStack item) {
		if ((tile == null) || !(tile instanceof IInventory)) {
			return;
		}
		final IInventory inv = (IInventory) tile;
		if ((slot < 0) || (slot >= inv.getSizeInventory())) {
			return;
		}
		inv.setInventorySlotContents(slot, (item == null) ? null : item.getMCItemStack());
	}
}
