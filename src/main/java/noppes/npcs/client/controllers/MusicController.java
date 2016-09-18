
package noppes.npcs.client.controllers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class MusicController {
	public static MusicController Instance;
	public PositionedSoundRecord playing;
	public ResourceLocation playingResource;
	public Entity playingEntity;

	public MusicController() {
		MusicController.Instance = this;
	}

	public boolean isPlaying(String music) {
		ResourceLocation resource = new ResourceLocation(music);
		return (playingResource != null) && playingResource.equals(resource)
				&& Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(playing);
	}

	public void playMusic(String music, Entity entity) {
		if (isPlaying(music)) {
			return;
		}
		stopMusic();
		playingResource = new ResourceLocation(music);
		playingEntity = entity;
		SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
		handler.playSound(playing = PositionedSoundRecord.create(playingResource));
	}

	public void playSound(String music, float x, float y, float z) {
		Minecraft.getMinecraft().theWorld.playSound(x, y, z, music, 1.0f, 1.0f, false);
	}

	public void playStreaming(String music, Entity entity) {
		if (isPlaying(music)) {
			return;
		}
		stopMusic();
		playingEntity = entity;
		playingResource = new ResourceLocation(music);
		SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
		handler.playSound(playing = PositionedSoundRecord.create(playingResource, (float) entity.posX,
				(float) entity.posY, (float) entity.posZ));
	}

	public void stopMusic() {
		SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
		if (playing != null) {
			handler.stopSound(playing);
		}
		handler.stopSounds();
		playingResource = null;
		playingEntity = null;
		playing = null;
	}
}
