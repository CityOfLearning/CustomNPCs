//

//

package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.NBTTags;
import noppes.npcs.Schematic;
import noppes.npcs.controllers.Availability;
import noppes.npcs.controllers.BlockData;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBuilder;

public class TileBuilder extends TileEntity implements ITickable {
	public static BlockPos DrawPos;
	public static boolean Compiled;
	static {
		TileBuilder.DrawPos = null;
		TileBuilder.Compiled = false;
	}

	public static void SetDrawPos(final BlockPos pos) {
		TileBuilder.DrawPos = pos;
		TileBuilder.Compiled = false;
	}

	private Schematic schematic;
	public int rotation;
	public int yOffest;
	public boolean enabled;
	public boolean started;
	public boolean finished;
	public Availability availability;
	private Stack<Integer> positions;

	private Stack<Integer> positionsSecond;

	private int ticks;

	public TileBuilder() {
		schematic = null;
		rotation = 0;
		yOffest = 0;
		enabled = false;
		started = false;
		finished = false;
		availability = new Availability();
		positions = new Stack<Integer>();
		positionsSecond = new Stack<Integer>();
		ticks = 20;
	}

	public Stack<BlockData> getBlock() {
		if (!enabled || finished || !hasSchematic()) {
			return null;
		}
		final boolean bo = positions.isEmpty();
		final Stack<BlockData> list = new Stack<BlockData>();
		int size = (schematic.width * schematic.length) / 4;
		if (size > 30) {
			size = 30;
		}
		for (int i = 0; i < size; ++i) {
			if ((positions.isEmpty() && !bo) || (positionsSecond.isEmpty() && bo)) {
				return list;
			}
			final int pos = bo ? positionsSecond.pop() : positions.pop();
			if (pos < schematic.blockArray.length) {
				Block b = Block.getBlockById(schematic.blockArray[pos]);
				if (b == null) {
					b = Blocks.air;
				}
				if (!b.isFullBlock() && !bo && (b != Blocks.air)) {
					positionsSecond.add(0, pos);
				} else {
					final int meta = schematic.blockDataArray[pos];
					final int x = pos % schematic.width;
					final int z = ((pos - x) / schematic.width) % schematic.length;
					final int y = (((pos - x) / schematic.width) - z) / schematic.length;
					final BlockPos blockPos = getPos().add(1, yOffest, 1).add(schematic.rotatePos(x, y, z, rotation));
					final IBlockState original = worldObj.getBlockState(blockPos);
					if (original.getBlock() == b) {
						if (b == Blocks.air) {
							continue;
						}
						if (b.getMetaFromState(original) == meta) {
							continue;
						}
					}
					IBlockState state = b.getStateFromMeta(meta);
					state = schematic.rotationState(state, rotation);
					NBTTagCompound tile = null;
					if (b instanceof ITileEntityProvider) {
						tile = schematic.getTileEntity(x, y, z, blockPos);
					}
					list.add(0, new BlockData(blockPos, state, tile));
				}
			}
		}
		return list;
	}

	private List<EntityPlayer> getPlayerList() {
		return worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
				new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)
						.expand(10.0, 10.0, 10.0));
	}

	public Schematic getSchematic() {
		return schematic;
	}

	public boolean hasSchematic() {
		return schematic != null;
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("SchematicName")) {
			schematic = SchematicController.Instance.load(compound.getString("SchematicName"));
		}
		Stack<Integer> positions = new Stack<Integer>();
		positions.addAll(NBTTags.getIntegerList(compound.getTagList("Positions", 10)));
		this.positions = positions;
		positions = new Stack<Integer>();
		positions.addAll(NBTTags.getIntegerList(compound.getTagList("PositionsSecond", 10)));
		positionsSecond = positions;
		readPartNBT(compound);
	}

	public void readPartNBT(final NBTTagCompound compound) {
		rotation = compound.getInteger("Rotation");
		yOffest = compound.getInteger("YOffset");
		enabled = compound.getBoolean("Enabled");
		started = compound.getBoolean("Started");
		finished = compound.getBoolean("Finished");
		availability.readFromNBT(compound.getCompoundTag("Availability"));
	}

	@SideOnly(Side.CLIENT)
	public void setDrawSchematic(final Schematic schematics) {
		schematic = schematics;
	}

	public void setSchematic(final Schematic schematics) {
		schematic = schematics;
		if (schematics == null) {
			positions.clear();
			positionsSecond.clear();
			return;
		}
		final Stack<Integer> positions = new Stack<Integer>();
		for (int y = 0; y < schematics.height; ++y) {
			for (int z = 0; z < (schematics.length / 2); ++z) {
				for (int x = 0; x < (schematics.width / 2); ++x) {
					positions.add(0, schematics.xyzToIndex(x, y, z));
				}
			}
			for (int z = 0; z < (schematics.length / 2); ++z) {
				for (int x = schematics.width / 2; x < schematics.width; ++x) {
					positions.add(0, schematics.xyzToIndex(x, y, z));
				}
			}
			for (int z = schematics.length / 2; z < schematics.length; ++z) {
				for (int x = 0; x < (schematics.width / 2); ++x) {
					positions.add(0, schematics.xyzToIndex(x, y, z));
				}
			}
			for (int z = schematics.length / 2; z < schematics.length; ++z) {
				for (int x = schematics.width / 2; x < schematics.width; ++x) {
					positions.add(0, schematics.xyzToIndex(x, y, z));
				}
			}
		}
		this.positions = positions;
		positionsSecond.clear();
	}

	@Override
	public void update() {
		if (worldObj.isRemote || !hasSchematic() || finished) {
			return;
		}
		--ticks;
		if (ticks > 0) {
			return;
		}
		ticks = 200;
		if (positions.isEmpty() && positionsSecond.isEmpty()) {
			finished = true;
			return;
		}
		if (!started) {
			for (final EntityPlayer player : getPlayerList()) {
				if (availability.isAvailable(player)) {
					started = true;
					break;
				}
			}
			if (!started) {
				return;
			}
		}
		final List<EntityNPCInterface> list = worldObj.getEntitiesWithinAABB((Class) EntityNPCInterface.class,
				new AxisAlignedBB(getPos(), getPos()).expand(32.0, 32.0, 32.0));
		for (final EntityNPCInterface npc : list) {
			if (npc.advanced.job == 10) {
				final JobBuilder job = (JobBuilder) npc.jobInterface;
				if (job.build != null) {
					continue;
				}
				job.build = this;
			}
		}
	}

	public NBTTagCompound writePartNBT(final NBTTagCompound compound) {
		compound.setInteger("Rotation", rotation);
		compound.setInteger("YOffset", yOffest);
		compound.setBoolean("Enabled", enabled);
		compound.setBoolean("Started", started);
		compound.setBoolean("Finished", finished);
		compound.setTag("Availability", availability.writeToNBT(new NBTTagCompound()));
		return compound;
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (schematic != null) {
			compound.setString("SchematicName", schematic.name);
		}
		compound.setTag("Positions", NBTTags.nbtIntegerCollection(new ArrayList<Integer>(positions)));
		compound.setTag("PositionsSecond", NBTTags.nbtIntegerCollection(new ArrayList<Integer>(positionsSecond)));
		writePartNBT(compound);
	}
}