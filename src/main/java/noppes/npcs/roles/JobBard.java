//

//

package noppes.npcs.roles;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.entity.data.role.IJobBard;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.constants.EnumBardInstrument;
import noppes.npcs.entity.EntityNPCInterface;

public class JobBard extends JobInterface implements IJobBard {
	public int minRange;
	public int maxRange;
	public boolean isStreamer;
	public boolean hasOffRange;
	public String song;
	private EnumBardInstrument instrument;
	private long ticks;

	public JobBard(EntityNPCInterface npc) {
		super(npc);
		minRange = 2;
		maxRange = 64;
		isStreamer = true;
		hasOffRange = true;
		song = "";
		instrument = EnumBardInstrument.Banjo;
		ticks = 0L;
		if (CustomItems.banjo != null) {
			mainhand = new ItemStackWrapper(new ItemStack(CustomItems.banjo));
			boolean b = true;
			overrideOffHand = b;
			overrideMainHand = b;
		}
	}

	@Override
	public void delete() {
		if (npc.worldObj.isRemote && hasOffRange && MusicController.Instance.isPlaying(song)) {
			MusicController.Instance.stopMusic();
		}
	}

	public EnumBardInstrument getInstrument() {
		return instrument;
	}

	@Override
	public String getSong() {
		return song;
	}

	@Override
	public void killed() {
		delete();
	}

	public void onLivingUpdate() {
		if (!npc.isRemote()) {
			return;
		}
		++ticks;
		if ((ticks % 10L) != 0L) {
			return;
		}
		if (song.isEmpty()) {
			return;
		}
		if (!MusicController.Instance.isPlaying(song)) {
			List<EntityPlayer> list = npc.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
					npc.getEntityBoundingBox().expand(minRange, minRange / 2, minRange));
			if (!list.contains(CustomNpcs.proxy.getPlayer())) {
				return;
			}
			if (isStreamer) {
				MusicController.Instance.playStreaming(song, npc);
			} else {
				MusicController.Instance.playMusic(song, npc);
			}
		} else if (MusicController.Instance.playingEntity != npc) {
			EntityPlayer player = CustomNpcs.proxy.getPlayer();
			if (npc.getDistanceSqToEntity(player) < MusicController.Instance.playingEntity
					.getDistanceSqToEntity(player)) {
				MusicController.Instance.playingEntity = npc;
			}
		} else if (hasOffRange) {
			List<EntityPlayer> list = npc.worldObj.getEntitiesWithinAABB((Class) EntityPlayer.class,
					npc.getEntityBoundingBox().expand(maxRange, maxRange / 2, maxRange));
			if (!list.contains(CustomNpcs.proxy.getPlayer())) {
				MusicController.Instance.stopMusic();
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		song = nbttagcompound.getString("BardSong");
		minRange = nbttagcompound.getInteger("BardMinRange");
		maxRange = nbttagcompound.getInteger("BardMaxRange");
		setInstrument(nbttagcompound.getInteger("BardInstrument"));
		isStreamer = nbttagcompound.getBoolean("BardStreamer");
		hasOffRange = nbttagcompound.getBoolean("BardHasOff");
	}

	public void setInstrument(int i) {
		if (CustomItems.banjo == null) {
			return;
		}
		instrument = EnumBardInstrument.values()[i];
		boolean b = instrument != EnumBardInstrument.None;
		overrideOffHand = b;
		overrideMainHand = b;
		switch (instrument) {
		case None: {
			mainhand = null;
			offhand = null;
			break;
		}
		case Banjo: {
			mainhand = new ItemStackWrapper(new ItemStack(CustomItems.banjo));
			offhand = null;
			break;
		}
		case Violin: {
			mainhand = new ItemStackWrapper(new ItemStack(CustomItems.violin));
			offhand = new ItemStackWrapper(new ItemStack(CustomItems.violinbow));
			break;
		}
		case Guitar: {
			mainhand = new ItemStackWrapper(new ItemStack(CustomItems.guitar));
			offhand = null;
			break;
		}
		case Harp: {
			mainhand = new ItemStackWrapper(new ItemStack(CustomItems.harp));
			offhand = null;
			break;
		}
		case FrenchHorn: {
			mainhand = new ItemStackWrapper(new ItemStack(CustomItems.frenchHorn));
			offhand = null;
			break;
		}
		}
	}

	@Override
	public void setSong(String song) {
		this.song = song;
		npc.updateClient = true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("BardSong", song);
		nbttagcompound.setInteger("BardMinRange", minRange);
		nbttagcompound.setInteger("BardMaxRange", maxRange);
		nbttagcompound.setInteger("BardInstrument", instrument.ordinal());
		nbttagcompound.setBoolean("BardStreamer", isStreamer);
		nbttagcompound.setBoolean("BardHasOff", hasOffRange);
		return nbttagcompound;
	}
}
