//

//

package noppes.npcs.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.LogWriter;

public class TextureCache extends SimpleTexture {
	private BufferedImage bufferedImage;
	private boolean textureUploaded;

	public TextureCache(ResourceLocation location) {
		super(location);
	}

	private void checkTextureUploaded() {
		if (!textureUploaded && (bufferedImage != null)) {
			if ((textureLocation != null) && (glTextureId != -1)) {
				TextureUtil.deleteTexture(glTextureId);
				glTextureId = -1;
			}
			TextureUtil.uploadTextureImage(super.getGlTextureId(), bufferedImage);
			textureUploaded = true;
		}
	}

	@Override
	public int getGlTextureId() {
		checkTextureUploaded();
		return super.getGlTextureId();
	}

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
	}

	public void setImage(ResourceLocation location) {
		try {
			IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
			BufferedImage bufferedimage = ImageIO.read(manager.getResource(location).getInputStream());
			int i = bufferedimage.getWidth();
			int j = bufferedimage.getHeight();
			bufferedImage = new BufferedImage(i * 4, j * 2, 1);
			Graphics g = bufferedImage.getGraphics();
			g.drawImage(bufferedimage, 0, 0, null);
			g.drawImage(bufferedimage, i, 0, null);
			g.drawImage(bufferedimage, i * 2, 0, null);
			g.drawImage(bufferedimage, i * 3, 0, null);
			g.drawImage(bufferedimage, 0, i, null);
			g.drawImage(bufferedimage, i, j, null);
			g.drawImage(bufferedimage, i * 2, j, null);
			g.drawImage(bufferedimage, i * 3, j, null);
			textureUploaded = false;
		} catch (Exception e) {
			LogWriter.error("Failed caching texture: " + location, e);
		}
	}
}
