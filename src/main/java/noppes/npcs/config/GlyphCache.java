
package noppes.npcs.config;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GlyphCache {
	static class Entry {
		public int textureName;
		public int width;
		public int height;
		public float u1;
		public float v1;
		public float u2;
		public float v2;
	}

	private static Color BACK_COLOR;
	static {
		GlyphCache.BACK_COLOR = new Color(255, 255, 255, 0);
	}
	private int fontSize;
	private boolean antiAliasEnabled;
	private BufferedImage stringImage;
	private Graphics2D stringGraphics;
	private BufferedImage glyphCacheImage;
	private Graphics2D glyphCacheGraphics;
	private FontRenderContext fontRenderContext;
	private int[] imageData;
	private IntBuffer imageBuffer;
	private IntBuffer singleIntBuffer;
	private List<Font> allFonts;
	protected List<Font> usedFonts;
	private int textureName;
	private LinkedHashMap<Font, Integer> fontCache;
	private LinkedHashMap<Long, Entry> glyphCache;
	private int cachePosX;

	private int cachePosY;

	private int cacheLineHeight;

	public GlyphCache() {
		fontSize = 18;
		antiAliasEnabled = false;
		glyphCacheImage = new BufferedImage(256, 256, 2);
		glyphCacheGraphics = glyphCacheImage.createGraphics();
		fontRenderContext = glyphCacheGraphics.getFontRenderContext();
		imageData = new int[65536];
		imageBuffer = ByteBuffer.allocateDirect(262144).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
		allFonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
		usedFonts = new ArrayList<Font>();
		fontCache = new LinkedHashMap<Font, Integer>();
		glyphCache = new LinkedHashMap<Long, Entry>();
		cachePosX = 1;
		cachePosY = 1;
		cacheLineHeight = 0;
		glyphCacheGraphics.setBackground(GlyphCache.BACK_COLOR);
		glyphCacheGraphics.setComposite(AlphaComposite.Src);
		allocateGlyphCacheTexture();
		allocateStringImage(256, 64);
		GraphicsEnvironment.getLocalGraphicsEnvironment().preferLocaleFonts();
		usedFonts.add(new Font("SansSerif", 0, 72));
	}

	private void allocateGlyphCacheTexture() {
		glyphCacheGraphics.clearRect(0, 0, 256, 256);
		singleIntBuffer.clear();
		GL11.glGenTextures(singleIntBuffer);
		textureName = singleIntBuffer.get(0);
		updateImageBuffer(0, 0, 256, 256);
		GlStateManager.bindTexture(textureName);
		GL11.glTexImage2D(3553, 0, 32828, 256, 256, 0, 6408, 5121, imageBuffer);
		GL11.glTexParameteri(3553, 10241, 9728);
		GL11.glTexParameteri(3553, 10240, 9728);
	}

	private void allocateStringImage(int width, int height) {
		stringImage = new BufferedImage(width, height, 2);
		stringGraphics = stringImage.createGraphics();
		setRenderingHints();
		stringGraphics.setBackground(GlyphCache.BACK_COLOR);
		stringGraphics.setPaint(Color.WHITE);
	}

	void cacheGlyphs(Font font, char[] text, int start, int limit, int layoutFlags) {
		GlyphVector vector = layoutGlyphVector(font, text, start, limit, layoutFlags);
		Rectangle vectorBounds = null;
		long fontKey = fontCache.get(font) << 32;
		int numGlyphs = vector.getNumGlyphs();
		Rectangle dirty = null;
		boolean vectorRendered = false;
		for (int index = 0; index < numGlyphs; ++index) {
			int glyphCode = vector.getGlyphCode(index);
			if (!glyphCache.containsKey(fontKey | glyphCode)) {
				if (!vectorRendered) {
					vectorRendered = true;
					for (int i = 0; i < numGlyphs; ++i) {
						Point2D pos = vector.getGlyphPosition(i);
						pos.setLocation(pos.getX() + (2 * i), pos.getY());
						vector.setGlyphPosition(i, pos);
					}
					vectorBounds = vector.getPixelBounds(fontRenderContext, 0.0f, 0.0f);
					if ((stringImage == null) || (vectorBounds.width > stringImage.getWidth())
							|| (vectorBounds.height > stringImage.getHeight())) {
						int width = Math.max(vectorBounds.width, stringImage.getWidth());
						int height = Math.max(vectorBounds.height, stringImage.getHeight());
						allocateStringImage(width, height);
					}
					stringGraphics.clearRect(0, 0, vectorBounds.width, vectorBounds.height);
					stringGraphics.drawGlyphVector(vector, -vectorBounds.x, -vectorBounds.y);
				}
				Rectangle rect = vector.getGlyphPixelBounds(index, null, -vectorBounds.x, -vectorBounds.y);
				if ((cachePosX + rect.width + 1) > 256) {
					cachePosX = 1;
					cachePosY += cacheLineHeight + 1;
					cacheLineHeight = 0;
				}
				if ((cachePosY + rect.height + 1) > 256) {
					updateTexture(dirty);
					dirty = null;
					allocateGlyphCacheTexture();
					boolean b = true;
					cachePosX = (b ? 1 : 0);
					cachePosY = (b ? 1 : 0);
					cacheLineHeight = 0;
				}
				if (rect.height > cacheLineHeight) {
					cacheLineHeight = rect.height;
				}
				glyphCacheGraphics.drawImage(stringImage, cachePosX, cachePosY, cachePosX + rect.width,
						cachePosY + rect.height, rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, null);
				rect.setLocation(cachePosX, cachePosY);
				Entry entry = new Entry();
				entry.textureName = textureName;
				entry.width = rect.width;
				entry.height = rect.height;
				entry.u1 = rect.x / 256.0f;
				entry.v1 = rect.y / 256.0f;
				entry.u2 = (rect.x + rect.width) / 256.0f;
				entry.v2 = (rect.y + rect.height) / 256.0f;
				glyphCache.put(fontKey | glyphCode, entry);
				if (dirty == null) {
					dirty = new Rectangle(cachePosX, cachePosY, rect.width, rect.height);
				} else {
					dirty.add(rect);
				}
				cachePosX += rect.width + 1;
			}
		}
		updateTexture(dirty);
	}

	int fontHeight(String s) {
		Font font = lookupFont(s.toCharArray(), 0, s.length(), 0);
		return (int) font.getLineMetrics(s, fontRenderContext).getHeight();
	}

	GlyphVector layoutGlyphVector(Font font, char[] text, int start, int limit, int layoutFlags) {
		if (!fontCache.containsKey(font)) {
			fontCache.put(font, fontCache.size());
		}
		return font.layoutGlyphVector(fontRenderContext, text, start, limit, layoutFlags);
	}

	Font lookupFont(char[] text, int start, int limit, int style) {
		for (Font font : usedFonts) {
			if (font.canDisplayUpTo(text, start, limit) != start) {
				return font.deriveFont(style, fontSize);
			}
		}
		for (Font font : allFonts) {
			if (font.canDisplayUpTo(text, start, limit) != start) {
				usedFonts.add(font);
				return font.deriveFont(style, fontSize);
			}
		}
		Font font = usedFonts.get(0);
		return font.deriveFont(style, fontSize);
	}

	Entry lookupGlyph(Font font, int glyphCode) {
		long fontKey = fontCache.get(font) << 32;
		return glyphCache.get(fontKey | glyphCode);
	}

	void setCustomFont(ResourceLocation location, int size, boolean antiAlias) throws Exception {
		InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font font = Font.createFont(0, stream);
		ge.registerFont(font);
		font = font.deriveFont(0, 72.0f);
		usedFonts.clear();
		usedFonts.add(font);
		fontSize = size;
		antiAliasEnabled = antiAlias;
		setRenderingHints();
	}

	void setDefaultFont(String name, int size, boolean antiAlias) {
		usedFonts.clear();
		usedFonts.add(new Font(name, 0, 72));
		fontSize = size;
		antiAliasEnabled = antiAlias;
		setRenderingHints();
	}

	private void setRenderingHints() {
		stringGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				antiAliasEnabled ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		stringGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				antiAliasEnabled ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		stringGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
	}

	private void updateImageBuffer(int x, int y, int width, int height) {
		glyphCacheImage.getRGB(x, y, width, height, imageData, 0, width);
		for (int i = 0; i < (width * height); ++i) {
			int color = imageData[i];
			imageData[i] = ((color << 8) | (color >>> 24));
		}
		imageBuffer.clear();
		imageBuffer.put(imageData);
		imageBuffer.flip();
	}

	private void updateTexture(Rectangle dirty) {
		if (dirty != null) {
			updateImageBuffer(dirty.x, dirty.y, dirty.width, dirty.height);
			GlStateManager.bindTexture(textureName);
			GL11.glTexSubImage2D(3553, 0, dirty.x, dirty.y, dirty.width, dirty.height, 6408, 5121, imageBuffer);
		}
	}
}
