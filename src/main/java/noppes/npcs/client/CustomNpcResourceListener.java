//

//

package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.CustomNpcs;

public class CustomNpcResourceListener implements IResourceManagerReloadListener {
	public static int DefaultTextColor;

	static {
		CustomNpcResourceListener.DefaultTextColor = 4210752;
	}

	private void createTextureCache() {
		enlargeTexture("planks_oak");
		enlargeTexture("planks_big_oak");
		enlargeTexture("planks_birch");
		enlargeTexture("planks_jungle");
		enlargeTexture("planks_spruce");
		enlargeTexture("planks_acacia");
		enlargeTexture("iron_block");
		enlargeTexture("diamond_block");
		enlargeTexture("stone");
		enlargeTexture("gold_block");
		enlargeTexture("wool_colored_white");
	}

	private void enlargeTexture(String texture) {
		TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		if (manager == null) {
			return;
		}
		ResourceLocation location = new ResourceLocation("customnpcs:textures/cache/" + texture + ".png");
		TextureCache ob = (TextureCache) manager.getTexture(location);
		if (ob == null) {
			ob = new TextureCache(location);
			manager.loadTexture(location, ob);
		}
		ob.setImage(new ResourceLocation("textures/blocks/" + texture + ".png"));
	}

	@Override
	public void onResourceManagerReload(IResourceManager var1) {
		if (var1 instanceof SimpleReloadableResourceManager) {
			createTextureCache();
			SimpleReloadableResourceManager simplemanager = (SimpleReloadableResourceManager) var1;
			FolderResourcePack pack = new FolderResourcePack(CustomNpcs.Dir);
			simplemanager.reloadResourcePack(pack);
			try {
				CustomNpcResourceListener.DefaultTextColor = Integer
						.parseInt(StatCollector.translateToLocal("customnpcs.defaultTextColor"), 16);
			} catch (NumberFormatException e) {
				CustomNpcResourceListener.DefaultTextColor = 4210752;
			}
		}
	}
}
