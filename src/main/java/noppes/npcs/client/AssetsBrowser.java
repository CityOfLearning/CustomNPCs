//

//

package noppes.npcs.client;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class AssetsBrowser {
	public static String getRoot(String asset) {
		String mod = "minecraft";
		int index = asset.indexOf(":");
		if (index > 0) {
			mod = asset.substring(0, index);
			asset = asset.substring(index + 1);
		}
		if (asset.startsWith("/")) {
			asset = asset.substring(1);
		}
		String location = "/" + mod + "/" + asset;
		index = location.lastIndexOf("/");
		if (index > 0) {
			location = location.substring(0, index);
		}
		return location;
	}

	public boolean isRoot;
	private int depth;
	private String folder;
	public HashSet<String> folders;
	public HashSet<String> files;

	private String[] extensions;

	public AssetsBrowser(final String folder, final String[] extensions) {
		folders = new HashSet<String>();
		files = new HashSet<String>();
		this.extensions = extensions;
		setFolder(folder);
	}

	public AssetsBrowser(final String[] extensions) {
		folders = new HashSet<String>();
		files = new HashSet<String>();
		this.extensions = extensions;
	}

	private void checkFile(String name) {
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		if (!name.startsWith(folder)) {
			return;
		}
		final String[] split = name.split("/");
		final int count = split.length;
		if (count == (depth + 1)) {
			if (validExtension(name)) {
				files.add(split[depth]);
			}
		} else if ((depth + 1) < count) {
			folders.add(split[depth]);
		}
	}

	private void checkFolder(final File file, final int length) {
		final File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (final File f : files) {
			String name = f.getAbsolutePath().substring(length);
			name = name.replace("\\", "/");
			if (!name.startsWith("/")) {
				name = "/" + name;
			}
			if (f.isDirectory() && (folder.startsWith(name) || name.startsWith(folder))) {
				checkFile(name + "/");
				checkFolder(f, length);
			} else {
				checkFile(name);
			}
		}
	}

	public String getAsset(final String asset) {
		final String[] split = folder.split("/");
		if (split.length < 3) {
			return null;
		}
		String texture = split[2] + ":";
		texture = texture + folder.substring(texture.length() + 8) + asset;
		return texture;
	}

	private void getFiles() {
		folders.clear();
		files.clear();
		Minecraft.getMinecraft().getResourcePackRepository();
		final SimpleReloadableResourceManager simplemanager = (SimpleReloadableResourceManager) Minecraft.getMinecraft()
				.getResourceManager();
		final Map<String, IResourceManager> map = (Map<String, IResourceManager>) ObfuscationReflectionHelper
				.getPrivateValue((Class) SimpleReloadableResourceManager.class, simplemanager, 2);
		final HashSet<String> set = new HashSet<String>();
		for (final String name : map.keySet()) {
			if (!(map.get(name) instanceof FallbackResourceManager)) {
				continue;
			}
			final FallbackResourceManager manager = (FallbackResourceManager) map.get(name);
			final List<IResourcePack> list = (List<IResourcePack>) ObfuscationReflectionHelper
					.getPrivateValue((Class) FallbackResourceManager.class, manager, 1);
			for (final IResourcePack pack : list) {
				if (pack instanceof AbstractResourcePack) {
					final AbstractResourcePack p = (AbstractResourcePack) pack;
					final File file = new File((String) p.getResourceDomains().toArray()[0], p.getPackName());
					if (file == null) {
						continue;
					}
					set.add(file.getAbsolutePath());
				}
			}
		}
		for (final String file2 : set) {
			progressFile(new File(file2));
		}
		for (final ModContainer mod : Loader.instance().getModList()) {
			if (mod.getSource().exists()) {
				progressFile(mod.getSource());
			}
		}
	}

	private void progressFile(final File file) {
		try {
			if (!file.isDirectory() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))) {
				final ZipFile zip = new ZipFile(file);
				final Enumeration<? extends ZipEntry> entries = zip.entries();
				while (entries.hasMoreElements()) {
					final ZipEntry zipentry = entries.nextElement();
					final String entryName = zipentry.getName();
					checkFile(entryName);
				}
				zip.close();
			} else if (file.isDirectory()) {
				final int length = file.getAbsolutePath().length();
				checkFolder(file, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFolder(String folder) {
		if (!folder.endsWith("/")) {
			folder += "/";
		}
		isRoot = (folder.length() <= 1);
		this.folder = "/assets" + folder;
		depth = StringUtils.countMatches(this.folder, "/");
		getFiles();
	}

	private boolean validExtension(final String entryName) {
		final int index = entryName.indexOf(".");
		if (index < 0) {
			return false;
		}
		final String extension = entryName.substring(index + 1);
		for (final String ex : extensions) {
			if (ex.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}
}
