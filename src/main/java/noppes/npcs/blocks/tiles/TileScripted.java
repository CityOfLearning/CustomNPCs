//

//

package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.util.ValueUtil;

public class TileScripted extends TileNpcEntity implements ITickable, IScriptBlockHandler {
	public List<ScriptContainer> scripts;
	public String scriptLanguage;
	public boolean enabled;
	private IBlock blockDummy;
	public DataTimers timers;
	public boolean hasInited;
	private short ticksExisted;
	public ItemStack itemModel;
	public boolean needsClientUpdate;
	public int powering;
	public int activePowering;
	public int newPower;
	public int prevPower;
	public boolean isLadder;
	public int lightValue;
	public int rotationX;
	public int rotationY;
	public int rotationZ;
	public float scaleX;
	public float scaleY;
	public float scaleZ;
	public TileEntity renderTile;
	public boolean renderTileErrored;
	public ITickable renderTileUpdate;

	public TileScripted() {
		scripts = new ArrayList<ScriptContainer>();
		scriptLanguage = "ECMAScript";
		enabled = false;
		blockDummy = null;
		timers = new DataTimers(this);
		hasInited = false;
		ticksExisted = 0;
		itemModel = new ItemStack(CustomItems.scripted);
		needsClientUpdate = false;
		powering = 0;
		activePowering = 0;
		newPower = 0;
		prevPower = 0;
		isLadder = false;
		lightValue = 0;
		rotationX = 0;
		rotationY = 0;
		rotationZ = 0;
		scaleX = 1.0f;
		scaleY = 1.0f;
		scaleZ = 1.0f;
		renderTileErrored = true;
		renderTileUpdate = null;
	}

	@Override
	public IBlock getBlock() {
		if (blockDummy == null) {
			blockDummy = new BlockScriptedWrapper(getWorld(), getBlockType(), getPos());
		}
		return blockDummy;
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound compound = new NBTTagCompound();
		getDisplayNBT(compound);
		final S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	public NBTTagCompound getDisplayNBT(final NBTTagCompound compound) {
		final NBTTagCompound itemcompound = new NBTTagCompound();
		itemModel.writeToNBT(itemcompound);
		compound.setTag("ScriptBlockModel", itemcompound);
		compound.setInteger("LightValue", lightValue);
		compound.setBoolean("IsLadder", isLadder);
		compound.setInteger("RotationX", rotationX);
		compound.setInteger("RotationY", rotationY);
		compound.setInteger("RotationZ", rotationZ);
		compound.setFloat("ScaleX", scaleX);
		compound.setFloat("ScaleY", scaleY);
		compound.setFloat("ScaleZ", scaleZ);
		return compound;
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public String getLanguage() {
		return scriptLanguage;
	}

	public NBTTagCompound getNBT(final NBTTagCompound compound) {
		compound.setTag("Scripts", NBTTags.NBTScript(scripts));
		compound.setString("ScriptLanguage", scriptLanguage);
		compound.setBoolean("ScriptEnabled", enabled);
		compound.setInteger("BlockPowering", powering);
		compound.setInteger("BlockPrevPower", prevPower);
		return compound;
	}

	@Override
	public List<ScriptContainer> getScripts() {
		return scripts;
	}

	@Override
	public boolean isClient() {
		return getWorld().isRemote;
	}

	private boolean isEnabled() {
		return enabled && ScriptController.HasStart && !worldObj.isRemote;
	}

	@Override
	public String noticeString() {
		final BlockPos pos = getPos();
		return Objects.toStringHelper(this).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		final int light = lightValue;
		setDisplayNBT(pkt.getNbtCompound());
		if (light != lightValue) {
			worldObj.checkLight(pos);
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		setNBT(compound);
		setDisplayNBT(compound);
		timers.readFromNBT(compound);
	}

	@Override
	public void runScript(final EnumScriptType type, final Event event) {
		if (!isEnabled()) {
			return;
		}
		if (!hasInited) {
			hasInited = true;
			EventHooks.onScriptBlockInit(this);
		}
		for (final ScriptContainer script : scripts) {
			if (!script.errored) {
				if (!script.hasCode()) {
					continue;
				}
				script.setEngine(scriptLanguage);
				if (script.engine == null) {
					continue;
				}
				script.run(type, event);
			}
		}
	}

	public void setDisplayNBT(final NBTTagCompound compound) {
		itemModel = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("ScriptBlockModel"));
		if ((itemModel == null) || (itemModel.getItem() == null)) {
			itemModel = new ItemStack(CustomItems.scripted);
		}
		renderTileUpdate = null;
		renderTile = null;
		renderTileErrored = false;
		lightValue = compound.getInteger("LightValue");
		isLadder = compound.getBoolean("IsLadder");
		rotationX = compound.getInteger("RotationX");
		rotationY = compound.getInteger("RotationY");
		rotationZ = compound.getInteger("RotationZ");
		scaleX = compound.getFloat("ScaleX");
		scaleY = compound.getFloat("ScaleY");
		scaleZ = compound.getFloat("ScaleZ");
		if (scaleX <= 0.0f) {
			scaleX = 1.0f;
		}
		if (scaleY <= 0.0f) {
			scaleY = 1.0f;
		}
		if (scaleZ <= 0.0f) {
			scaleZ = 1.0f;
		}
	}

	@Override
	public void setEnabled(final boolean bo) {
		enabled = bo;
	}

	public void setItemModel(ItemStack item) {
		if ((item == null) || (item.getItem() == null)) {
			item = new ItemStack(CustomItems.scripted);
		}
		if (NoppesUtilPlayer.compareItems(item, itemModel, false, false)) {
			return;
		}
		itemModel = item;
		needsClientUpdate = true;
	}

	@Override
	public void setLanguage(final String lang) {
		scriptLanguage = lang;
	}

	public void setLightValue(final int value) {
		if (value == lightValue) {
			return;
		}
		lightValue = ValueUtil.CorrectInt(value, 0, 15);
		needsClientUpdate = true;
	}

	public void setNBT(final NBTTagCompound compound) {
		scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
		scriptLanguage = compound.getString("ScriptLanguage");
		enabled = compound.getBoolean("ScriptEnabled");
		final int integer = compound.getInteger("BlockPowering");
		powering = integer;
		activePowering = integer;
		prevPower = compound.getInteger("BlockPrevPower");
	}

	public void setRedstonePower(final int strength) {
		if (powering == strength) {
			return;
		}
		final int correctInt = ValueUtil.CorrectInt(strength, 0, 15);
		activePowering = correctInt;
		prevPower = correctInt;
		worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
		powering = activePowering;
	}

	public void setRotation(final int x, final int y, final int z) {
		if ((rotationX == x) && (rotationY == y) && (rotationZ == z)) {
			return;
		}
		rotationX = ValueUtil.CorrectInt(x, 0, 359);
		rotationY = ValueUtil.CorrectInt(y, 0, 359);
		rotationZ = ValueUtil.CorrectInt(z, 0, 359);
		needsClientUpdate = true;
	}

	public void setScale(final float x, final float y, final float z) {
		if ((scaleX == x) && (scaleY == y) && (scaleZ == z)) {
			return;
		}
		scaleX = ValueUtil.correctFloat(x, 0.0f, 10.0f);
		scaleY = ValueUtil.correctFloat(y, 0.0f, 10.0f);
		scaleZ = ValueUtil.correctFloat(z, 0.0f, 10.0f);
		needsClientUpdate = true;
	}

	@Override
	public void update() {
		if (renderTileUpdate != null) {
			try {
				renderTileUpdate.update();
			} catch (Exception e) {
				renderTileUpdate = null;
			}
		}
		++ticksExisted;
		if ((prevPower != newPower) && (powering <= 0)) {
			EventHooks.onScriptBlockRedstonePower(this, prevPower, newPower);
			prevPower = newPower;
		}
		timers.update();
		if (ticksExisted >= 10) {
			EventHooks.onScriptBlockUpdate(this);
			ticksExisted = 0;
			if (needsClientUpdate) {
				worldObj.markBlockForUpdate(pos);
				needsClientUpdate = false;
			}
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		getNBT(compound);
		getDisplayNBT(compound);
		timers.writeToNBT(compound);
	}
}
