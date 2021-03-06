
package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.api.IItemStack;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.util.NoppesUtilServer;

public class BlockScriptedWrapper extends BlockWrapper implements IBlockScripted {
	private TileScripted tile;

	public BlockScriptedWrapper(World world, Block block, BlockPos pos) {
		super(world, block, pos);
		tile = (TileScripted) super.tile;
	}

	@Override
	public void executeCommand(String command) {
		NoppesUtilServer.runCommand(tile.getWorld(), tile.getPos(), "ScriptBlock: " + tile.getPos(), command, null);
	}

	@Override
	public boolean getIsLadder() {
		return tile.isLadder;
	}

	@Override
	public int getLight() {
		return tile.lightValue;
	}

	@Override
	public IItemStack getModel() {
		return new ItemStackWrapper(tile.itemModel);
	}

	@Override
	public int getRedstonePower() {
		return tile.powering;
	}

	@Override
	public int getRotationX() {
		return tile.rotationX;
	}

	@Override
	public int getRotationY() {
		return tile.rotationY;
	}

	@Override
	public int getRotationZ() {
		return tile.rotationZ;
	}

	@Override
	public float getScaleX() {
		return tile.scaleX;
	}

	@Override
	public float getScaleY() {
		return tile.scaleY;
	}

	@Override
	public float getScaleZ() {
		return tile.scaleZ;
	}

	@Override
	public ITimers getTimers() {
		return tile.timers;
	}

	@Override
	public void setIsLadder(boolean bo) {
		tile.isLadder = bo;
		tile.needsClientUpdate = true;
	}

	@Override
	public void setLight(int value) {
		tile.setLightValue(value);
	}

	@Override
	public void setModel(IItemStack item) {
		if (item == null) {
			tile.setItemModel(null);
		} else {
			tile.setItemModel(item.getMCItemStack());
		}
	}

	@Override
	public void setModel(String name) {
		if (name == null) {
			tile.setItemModel(null);
		} else {
			Item item = Item.itemRegistry.getObject(new ResourceLocation(name));
			if (item == null) {
				tile.setItemModel(null);
			} else {
				tile.setItemModel(new ItemStack(item));
			}
		}
	}

	@Override
	public void setRedstonePower(int strength) {
		tile.setRedstonePower(strength);
	}

	@Override
	public void setRotation(int x, int y, int z) {
		tile.setRotation(x % 360, y % 360, z % 360);
	}

	@Override
	public void setScale(float x, float y, float z) {
		tile.setScale(x, y, z);
	}
}
