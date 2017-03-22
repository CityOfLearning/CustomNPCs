
package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.dyn.schematics.BlockData;
import com.dyn.schematics.Schematic;
import com.dyn.schematics.SchematicRenderingRegistry;

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
import noppes.npcs.ai.jobs.JobBuilder;
import noppes.npcs.controllers.Availability;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTTags;

public class TileBuilder extends TileEntity implements ITickable {
	public static BlockPos DrawPos;
	static {
		TileBuilder.DrawPos = null;
	}

	public static void SetDrawPos(BlockPos pos) {
		TileBuilder.DrawPos = pos;
	}

	private Schematic schematic;
	private int rotation;
	private int yOffest;

	private boolean enabled;

	private boolean started;

	private boolean finished;

	private Availability availability;

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
		positions = new Stack<>();
		positionsSecond = new Stack<>();
		ticks = 20;
	}

	public Availability getAvailability() {
		return availability;
	}

	public Stack<BlockData> getBlock() {
		if (!enabled || finished || !hasSchematic()) {
			return null;
		}
		boolean bo = positions.isEmpty();
		Stack<BlockData> list = new Stack<>();
		int size = (schematic.width * schematic.length) / 4;
		if (size > 30) {
			size = 30;
		}
		for (int i = 0; i < size; ++i) {
			if ((positions.isEmpty() && !bo) || (positionsSecond.isEmpty() && bo)) {
				return list;
			}
			int pos = bo ? positionsSecond.pop() : positions.pop();
			if (pos < schematic.blockArray.length) {
				Block b = Block.getBlockById(schematic.blockArray[pos]);
				if (b == null) {
					b = Blocks.air;
				}
				if (!b.isFullBlock() && !bo && (b != Blocks.air)) {
					positionsSecond.add(0, pos);
				} else {
					int meta = schematic.blockDataArray[pos];
					int x = pos % schematic.width;
					int z = ((pos - x) / schematic.width) % schematic.length;
					int y = (((pos - x) / schematic.width) - z) / schematic.length;
					BlockPos blockPos = getPos().add(1, yOffest, 1).add(schematic.rotatePos(x, y, z, rotation));
					IBlockState original = worldObj.getBlockState(blockPos);
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

	public Stack<Integer> getPositions() {
		return positions;
	}

	public Stack<Integer> getPositionsSecond() {
		return positionsSecond;
	}

	public int getRotation() {
		return rotation;
	}

	public Schematic getSchematic() {
		return schematic;
	}

	public int getTicks() {
		return ticks;
	}

	public int getYOffset() {
		return yOffest;
	}

	public boolean hasSchematic() {
		return schematic != null;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isStarted() {
		return started;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("SchematicName")) {
			schematic = SchematicController.instance.load(compound.getString("SchematicName"));
		}
		Stack<Integer> positions = new Stack<>();
		positions.addAll(NBTTags.getIntegerList(compound.getTagList("Positions", 10)));
		this.positions = positions;
		positions = new Stack<>();
		positions.addAll(NBTTags.getIntegerList(compound.getTagList("PositionsSecond", 10)));
		positionsSecond = positions;
		readPartNBT(compound);
	}

	public void readPartNBT(NBTTagCompound compound) {
		rotation = compound.getInteger("Rotation");
		yOffest = compound.getInteger("YOffset");
		enabled = compound.getBoolean("Enabled");
		started = compound.getBoolean("Started");
		finished = compound.getBoolean("Finished");
		availability.readFromNBT(compound.getCompoundTag("Availability"));
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	@SideOnly(Side.CLIENT)
	public void setDrawSchematic(Schematic schematic) {
		if (schematic != null) {
			SchematicRenderingRegistry.addSchematic(schematic, TileBuilder.DrawPos, rotation);
			if (this.schematic != null) {
				SchematicRenderingRegistry.removeSchematic(this.schematic);
			}
		}
		this.schematic = schematic;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setPositions(Stack<Integer> positions) {
		this.positions = positions;
	}

	public void setPositionsSecond(Stack<Integer> positionsSecond) {
		this.positionsSecond = positionsSecond;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public void setSchematic(Schematic schematics) {
		schematic = schematics;
		if (schematics == null) {
			positions.clear();
			positionsSecond.clear();
			return;
		}
		Stack<Integer> positions = new Stack<>();
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

	public void setStarted(boolean started) {
		this.started = started;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public void setyOffest(int yOffest) {
		this.yOffest = yOffest;
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
			for (EntityPlayer player : getPlayerList()) {
				if (availability.isAvailable(player)) {
					started = true;
					break;
				}
			}
			if (!started) {
				return;
			}
		}
		List<EntityNPCInterface> list = worldObj.getEntitiesWithinAABB((Class) EntityNPCInterface.class,
				new AxisAlignedBB(getPos(), getPos()).expand(32.0, 32.0, 32.0));
		for (EntityNPCInterface npc : list) {
			if (npc.advanced.job == 10) {
				JobBuilder job = (JobBuilder) npc.jobInterface;
				if (job.build != null) {
					continue;
				}
				job.build = this;
			}
		}
	}

	public NBTTagCompound writePartNBT(NBTTagCompound compound) {
		compound.setInteger("Rotation", rotation);
		compound.setInteger("YOffset", yOffest);
		compound.setBoolean("Enabled", enabled);
		compound.setBoolean("Started", started);
		compound.setBoolean("Finished", finished);
		compound.setTag("Availability", availability.writeToNBT(new NBTTagCompound()));
		return compound;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (schematic != null) {
			compound.setString("SchematicName", schematic.name);
		}
		compound.setTag("Positions", NBTTags.nbtIntegerCollection(new ArrayList<>(positions)));
		compound.setTag("PositionsSecond", NBTTags.nbtIntegerCollection(new ArrayList<>(positionsSecond)));
		writePartNBT(compound);
	}
}
