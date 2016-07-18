//

//

package noppes.npcs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class Schematic {
	public static final int buildSize = 10000;
	public String name;
	public short width;
	public short height;
	public short length;
	private BlockPos offset;
	private BlockPos start;
	private Map<ChunkCoordIntPair, NBTTagCompound>[] tileEntities;
	private NBTTagList entityList;
	public NBTTagList tileList;
	public short[] blockArray;
	public byte[] blockDataArray;
	private World world;
	public boolean isBuilding;
	public boolean firstLayer;
	public int buildPos;
	public int size;
	private int rotation;

	public Schematic(final String name) {
		offset = BlockPos.ORIGIN;
		start = BlockPos.ORIGIN;
		isBuilding = false;
		firstLayer = true;
		rotation = 0;
		this.name = name;
	}

	public void build() {
		if ((world == null) || !isBuilding) {
			return;
		}
		long endPos = buildPos + 10000;
		if (endPos > size) {
			endPos = size;
		}
		while (buildPos < endPos) {
			final int x = buildPos % width;
			final int z = ((buildPos - x) / width) % length;
			final int y = (((buildPos - x) / width) - z) / length;
			if (firstLayer) {
				place(x, y, z, 1);
			} else {
				place(x, y, z, 2);
			}
			++buildPos;
		}
		if (buildPos >= size) {
			if (firstLayer) {
				firstLayer = false;
				buildPos = 0;
			} else {
				isBuilding = false;
			}
		}
	}

	public byte[][] getBlockBytes() {
		final byte[] blocks = new byte[blockArray.length];
		byte[] addBlocks = null;
		for (int i = 0; i < blocks.length; ++i) {
			final short id = blockArray[i];
			if (id > 255) {
				if (addBlocks == null) {
					addBlocks = new byte[(blocks.length >> 1) + 1];
				}
				if ((i & 0x1) == 0x0) {
					addBlocks[i >> 1] = (byte) ((addBlocks[i >> 1] & 0xF0) | ((id >> 8) & 0xF));
				} else {
					addBlocks[i >> 1] = (byte) ((addBlocks[i >> 1] & 0xF) | (((id >> 8) & 0xF) << 4));
				}
			}
			blocks[i] = (byte) id;
		}
		if (addBlocks == null) {
			return new byte[][] { blocks };
		}
		return new byte[][] { blocks, addBlocks };
	}

	public NBTTagCompound getNBTSmall() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setShort("Width", width);
		compound.setShort("Height", height);
		compound.setShort("Length", length);
		compound.setString("SchematicName", name);
		if (size < 125000) {
			final byte[][] arr = getBlockBytes();
			compound.setByteArray("Blocks", arr[0]);
			if (arr.length > 1) {
				compound.setByteArray("AddBlocks", arr[1]);
			}
			compound.setByteArray("Data", blockDataArray);
		}
		return compound;
	}

	public int getPercentage() {
		final double l = buildPos + (firstLayer ? 0 : size);
		return (int) ((l / size) * 50.0);
	}

	public NBTTagCompound getTileEntity(final int x, final int y, final int z, final BlockPos pos) {
		if ((y >= tileEntities.length) || (tileEntities[y] == null)) {
			return null;
		}
		NBTTagCompound compound = tileEntities[y].get(new ChunkCoordIntPair(x, z));
		if (compound == null) {
			return null;
		}
		compound = (NBTTagCompound) compound.copy();
		compound.setInteger("x", pos.getX());
		compound.setInteger("y", pos.getY());
		compound.setInteger("z", pos.getZ());
		return compound;
	}

	public void init(final BlockPos pos, final World world, final int rotation) {
		start = pos;
		this.world = world;
		this.rotation = rotation;
	}

	public void load(final NBTTagCompound compound) {
		width = compound.getShort("Width");
		height = compound.getShort("Height");
		length = compound.getShort("Length");
		size = width * height * length;
		final byte[] addId = compound.hasKey("AddBlocks") ? compound.getByteArray("AddBlocks") : new byte[0];
		setBlockBytes(compound.getByteArray("Blocks"), addId);
		blockDataArray = compound.getByteArray("Data");
		entityList = compound.getTagList("Entities", 10);
		tileEntities = new Map[height];
		tileList = compound.getTagList("TileEntities", 10);
		for (int i = 0; i < tileList.tagCount(); ++i) {
			final NBTTagCompound teTag = tileList.getCompoundTagAt(i);
			final int x = teTag.getInteger("x");
			final int y = teTag.getInteger("y");
			final int z = teTag.getInteger("z");
			Map<ChunkCoordIntPair, NBTTagCompound> map = tileEntities[y];
			if (map == null) {
				map = (tileEntities[y] = new HashMap<ChunkCoordIntPair, NBTTagCompound>());
			}
			map.put(new ChunkCoordIntPair(x, z), teTag);
		}
	}

	public void offset(final int x, final int y, final int z) {
		offset = new BlockPos(x, y, z);
	}

	public void place(final int x, final int y, final int z, final int flag) {
		final int i = xyzToIndex(x, y, z);
		final Block b = Block.getBlockById(blockArray[i]);
		if ((b == null) || ((flag == 1) && !b.isFullBlock() && (b != Blocks.air))
				|| ((flag == 2) && (b.isFullBlock() || (b == Blocks.air)))) {
			return;
		}
		final int rotation = this.rotation / 90;
		final BlockPos pos = start.add(rotatePos(x, y, z, rotation));
		IBlockState state = b.getStateFromMeta(blockDataArray[i]);
		state = rotationState(state, rotation);
		world.setBlockState(pos, state, 2);
		if (state.getBlock() instanceof ITileEntityProvider) {
			final TileEntity tile = world.getTileEntity(pos);
			if (tile != null) {
				final NBTTagCompound comp = getTileEntity(x, y, z, pos);
				if (comp != null) {
					tile.readFromNBT(comp);
				}
			}
		}
	}

	public BlockPos rotatePos(final int x, final int y, final int z, final int rotation) {
		if (rotation == 1) {
			return new BlockPos(length - z - 1, y, x);
		}
		if (rotation == 2) {
			return new BlockPos(width - x - 1, y, length - z - 1);
		}
		if (rotation == 3) {
			return new BlockPos(z, y, width - x - 1);
		}
		return new BlockPos(x, y, z);
	}

	public IBlockState rotationState(final IBlockState state, final int rotation) {
		if (rotation == 0) {
			return state;
		}
		final Set<IProperty> set = state.getProperties().keySet();
		for (final IProperty prop : set) {
			if (!(prop instanceof PropertyDirection)) {
				continue;
			}
			EnumFacing direction = (EnumFacing) state.getValue(prop);
			if (direction == EnumFacing.UP) {
				continue;
			}
			if (direction == EnumFacing.DOWN) {
				continue;
			}
			for (int i = 0; i < rotation; ++i) {
				direction = direction.rotateY();
			}
			return state.withProperty(prop, direction);
		}
		return state;
	}

	public NBTTagCompound save() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setShort("Width", width);
		compound.setShort("Height", height);
		compound.setShort("Length", length);
		final byte[][] arr = getBlockBytes();
		compound.setByteArray("Blocks", arr[0]);
		if (arr.length > 1) {
			compound.setByteArray("AddBlocks", arr[1]);
		}
		compound.setByteArray("Data", blockDataArray);
		compound.setTag("TileEntities", tileList);
		return compound;
	}

	public void setBlockBytes(final byte[] blockId, final byte[] addId) {
		blockArray = new short[blockId.length];
		for (int index = 0; index < blockId.length; ++index) {
			short id = (short) (blockId[index] & 0xFF);
			if ((index >> 1) < addId.length) {
				if ((index & 0x1) == 0x0) {
					id += (short) ((addId[index >> 1] & 0xF) << 8);
				} else {
					id += (short) ((addId[index >> 1] & 0xF0) << 4);
				}
			}
			blockArray[index] = id;
		}
	}

	public int xyzToIndex(final int x, final int y, final int z) {
		return (((y * length) + z) * width) + x;
	}
}
