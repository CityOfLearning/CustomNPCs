//

//

package noppes.npcs.config;

import java.awt.Font;
import java.awt.Point;
import java.awt.font.GlyphVector;
import java.lang.ref.WeakReference;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class StringCache {
	private static class ColorCode implements Comparable<Integer> {
		public int stringIndex;
		public int stripIndex;
		public byte colorCode;
		public byte fontStyle;
		public byte renderStyle;

		@Override
		public int compareTo(final Integer i) {
			return (stringIndex == i) ? 0 : ((stringIndex < i) ? -1 : 1);
		}
	}

	private static class Entry {
		public WeakReference<Key> keyRef;
		public int advance;
		public Glyph[] glyphs;
		public ColorCode[] colors;
		public boolean specialRender;
	}

	private static class Glyph implements Comparable<Glyph> {
		public int stringIndex;
		public GlyphCache.Entry texture;
		public int x;
		public int y;
		public int advance;

		@Override
		public int compareTo(final Glyph o) {
			return (stringIndex == o.stringIndex) ? 0 : ((stringIndex < o.stringIndex) ? -1 : 1);
		}
	}

	private static class Key {
		public String str;

		@Override
		public boolean equals(final Object o) {
			if (o == null) {
				return false;
			}
			final String other = o.toString();
			final int length = str.length();
			if (length != other.length()) {
				return false;
			}
			boolean colorCode = false;
			for (int index = 0; index < length; ++index) {
				final char c1 = str.charAt(index);
				final char c2 = other.charAt(index);
				if ((c1 != c2) && ((c1 < '0') || (c1 > '9') || (c2 < '0') || (c2 > '9') || colorCode)) {
					return false;
				}
				colorCode = (c1 == '\u00a7');
			}
			return true;
		}

		@Override
		public int hashCode() {
			int code = 0;
			final int length = str.length();
			boolean colorCode = false;
			for (int index = 0; index < length; ++index) {
				char c = str.charAt(index);
				if ((c >= '0') && (c <= '9') && !colorCode) {
					c = '0';
				}
				code = (code * 31) + c;
				colorCode = (c == '\u00a7');
			}
			return code;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	class RGBA {
		int r;
		int g;
		int b;
		int a;

		public RGBA(final int color) {
			r = ((color >> 16) & 0xFF);
			g = ((color >> 8) & 0xFF);
			b = (color & 0xFF);
			a = ((color >> 24) & 0xFF);
			if (a == 0) {
				a = 255;
			}
		}
	}

	private GlyphCache glyphCache;
	private int[] colorTable;
	private WeakHashMap<Key, Entry> stringCache;
	private WeakHashMap<String, Key> weakRefCache;
	private Key lookupKey;

	private Glyph[][] digitGlyphs;

	private boolean digitGlyphsReady;

	private boolean antiAliasEnabled;

	private Thread mainThread;

	public int fontHeight;

	public StringCache() {
		stringCache = new WeakHashMap<Key, Entry>();
		weakRefCache = new WeakHashMap<String, Key>();
		lookupKey = new Key();
		digitGlyphs = new Glyph[4][];
		digitGlyphsReady = false;
		antiAliasEnabled = false;
		mainThread = Thread.currentThread();
		glyphCache = new GlyphCache();
		colorTable = new int[32];
		for (int i = 0; i < 32; ++i) {
			final int j = ((i >> 3) & 0x1) * 85;
			int k = (((i >> 2) & 0x1) * 170) + j;
			int l = (((i >> 1) & 0x1) * 170) + j;
			int i2 = (((i >> 0) & 0x1) * 170) + j;
			if (i == 6) {
				k += 85;
			}
			if (Minecraft.getMinecraft().gameSettings.anaglyph) {
				final int j2 = ((k * 30) + (l * 59) + (i2 * 11)) / 100;
				final int k2 = ((k * 30) + (l * 70)) / 100;
				final int l2 = ((k * 30) + (i2 * 70)) / 100;
				k = j2;
				l = k2;
				i2 = l2;
			}
			if (i >= 16) {
				k /= 4;
				l /= 4;
				i2 /= 4;
			}
			colorTable[i] = (((k & 0xFF) << 16) | ((l & 0xFF) << 8) | (i2 & 0xFF));
		}
		cacheDightGlyphs();
	}

	private void cacheDightGlyphs() {
		digitGlyphsReady = false;
		digitGlyphs[0] = cacheString("0123456789").glyphs;
		digitGlyphs[1] = cacheString("\u00a7l0123456789").glyphs;
		digitGlyphs[2] = cacheString("\u00a7o0123456789").glyphs;
		digitGlyphs[3] = cacheString("\u00a7l\u00a7o0123456789").glyphs;
		digitGlyphsReady = true;
	}

	private Entry cacheString(final String str) {
		Entry entry = null;
		if (mainThread == Thread.currentThread()) {
			lookupKey.str = str;
			entry = stringCache.get(lookupKey);
		}
		if (entry == null) {
			final char[] text = str.toCharArray();
			entry = new Entry();
			final int length = stripColorCodes(entry, str, text);
			final List<Glyph> glyphList = new ArrayList<Glyph>();
			entry.advance = layoutBidiString(glyphList, text, 0, length, entry.colors);
			entry.glyphs = new Glyph[glyphList.size()];
			Arrays.sort(entry.glyphs = glyphList.toArray(entry.glyphs));
			int colorIndex = 0;
			int shift = 0;
			for (int glyphIndex = 0; glyphIndex < entry.glyphs.length; ++glyphIndex) {
				Glyph glyph;
				for (glyph = entry.glyphs[glyphIndex]; (colorIndex < entry.colors.length) && ((glyph.stringIndex
						+ shift) >= entry.colors[colorIndex].stringIndex); shift += 2, ++colorIndex) {
				}
				final Glyph glyph2 = glyph;
				glyph2.stringIndex += shift;
			}
			if (mainThread == Thread.currentThread()) {
				final Key key = new Key();
				key.str = new String(str);
				entry.keyRef = new WeakReference<Key>(key);
				stringCache.put(key, entry);
			}
		}
		if (mainThread == Thread.currentThread()) {
			final Key oldKey = entry.keyRef.get();
			if (oldKey != null) {
				weakRefCache.put(str, oldKey);
			}
			lookupKey.str = null;
		}
		return entry;
	}

	private RGBA getColorCode(int colorCode, int color, final boolean shadowFlag) {
		if (colorCode != -1) {
			colorCode = (shadowFlag ? (colorCode + 16) : colorCode);
			color = ((colorTable[colorCode] & 0xFFFFFF) | (color & 0xFF000000));
		}
		return new RGBA(color);
	}

	public int getStringWidth(final String str) {
		if ((str == null) || str.isEmpty()) {
			return 0;
		}
		final Entry entry = cacheString(str);
		return entry.advance / 2;
	}

	private int layoutBidiString(final List<Glyph> glyphList, final char[] text, final int start, final int limit,
			final ColorCode[] colors) {
		int advance = 0;
		if (!Bidi.requiresBidi(text, start, limit)) {
			return layoutStyle(glyphList, text, start, limit, 0, advance, colors);
		}
		final Bidi bidi = new Bidi(text, start, null, 0, limit - start, -2);
		if (bidi.isRightToLeft()) {
			return layoutStyle(glyphList, text, start, limit, 1, advance, colors);
		}
		final int runCount = bidi.getRunCount();
		final byte[] levels = new byte[runCount];
		final Integer[] ranges = new Integer[runCount];
		for (int index = 0; index < runCount; ++index) {
			levels[index] = (byte) bidi.getRunLevel(index);
			ranges[index] = new Integer(index);
		}
		Bidi.reorderVisually(levels, 0, ranges, 0, runCount);
		for (final int logicalIndex : ranges) {
			final int layoutFlag = ((bidi.getRunLevel(logicalIndex) & 0x1) == 0x1) ? 1 : 0;
			advance = layoutStyle(glyphList, text, start + bidi.getRunStart(logicalIndex),
					start + bidi.getRunLimit(logicalIndex), layoutFlag, advance, colors);
		}
		return advance;
	}

	private int layoutFont(final List<Glyph> glyphList, final char[] text, final int start, final int limit,
			final int layoutFlags, int advance, final Font font) {
		if (mainThread == Thread.currentThread()) {
			glyphCache.cacheGlyphs(font, text, start, limit, layoutFlags);
		}
		final GlyphVector vector = glyphCache.layoutGlyphVector(font, text, start, limit, layoutFlags);
		Glyph glyph = null;
		final int numGlyphs = vector.getNumGlyphs();
		for (int index = 0; index < numGlyphs; ++index) {
			final Point position = vector.getGlyphPixelBounds(index, null, advance, 0.0f).getLocation();
			if (glyph != null) {
				glyph.advance = position.x - glyph.x;
			}
			glyph = new Glyph();
			glyph.stringIndex = start + vector.getGlyphCharIndex(index);
			glyph.texture = glyphCache.lookupGlyph(font, vector.getGlyphCode(index));
			glyph.x = position.x;
			glyph.y = position.y;
			glyphList.add(glyph);
		}
		advance += (int) vector.getGlyphPosition(numGlyphs).getX();
		if (glyph != null) {
			glyph.advance = advance - glyph.x;
		}
		return advance;
	}

	private int layoutString(final List<Glyph> glyphList, final char[] text, int start, final int limit,
			final int layoutFlags, int advance, final int style) {
		if (digitGlyphsReady) {
			for (int index = start; index < limit; ++index) {
				if ((text[index] >= '0') && (text[index] <= '9')) {
					text[index] = '0';
				}
			}
		}
		while (start < limit) {
			final Font font = glyphCache.lookupFont(text, start, limit, style);
			int next = font.canDisplayUpTo(text, start, limit);
			if (next == -1) {
				next = limit;
			}
			if (next == start) {
				++next;
			}
			advance = layoutFont(glyphList, text, start, next, layoutFlags, advance, font);
			start = next;
		}
		return advance;
	}

	private int layoutStyle(final List<Glyph> glyphList, final char[] text, int start, final int limit,
			final int layoutFlags, int advance, final ColorCode[] colors) {
		int currentFontStyle = 0;
		int colorIndex = Arrays.binarySearch(colors, start);
		if (colorIndex < 0) {
			colorIndex = -colorIndex - 2;
		}
		while (start < limit) {
			int next = limit;
			while ((colorIndex >= 0) && (colorIndex < (colors.length - 1))
					&& (colors[colorIndex].stripIndex == colors[colorIndex + 1].stripIndex)) {
				++colorIndex;
			}
			if ((colorIndex >= 0) && (colorIndex < colors.length)) {
				currentFontStyle = colors[colorIndex].fontStyle;
			}
			while (++colorIndex < colors.length) {
				if (colors[colorIndex].fontStyle != currentFontStyle) {
					next = colors[colorIndex].stripIndex;
					break;
				}
			}
			advance = layoutString(glyphList, text, start, next, layoutFlags, advance, currentFontStyle);
			start = next;
		}
		return advance;
	}

	public int renderString(final String str, final int startX, int startY, final int initialColor,
			final boolean shadowFlag) {
		if ((str == null) || str.isEmpty()) {
			return 0;
		}
		final Entry entry = cacheString(str);
		startY += 7;
		RGBA color = new RGBA(initialColor);
		int boundTextureName = 0;
		GL11.glTexEnvi(8960, 8704, 8448);
		GlStateManager.color(color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, 1.0f);
		if (antiAliasEnabled) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
		}
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
		int fontStyle = 0;
		int glyphIndex = 0;
		int colorIndex = 0;
		while (glyphIndex < entry.glyphs.length) {
			while ((colorIndex < entry.colors.length)
					&& (entry.glyphs[glyphIndex].stringIndex >= entry.colors[colorIndex].stringIndex)) {
				color = getColorCode(entry.colors[colorIndex].colorCode, initialColor, shadowFlag);
				fontStyle = entry.colors[colorIndex].fontStyle;
				++colorIndex;
			}
			final Glyph glyph = entry.glyphs[glyphIndex];
			GlyphCache.Entry texture = glyph.texture;
			int glyphX = glyph.x;
			final char c = str.charAt(glyph.stringIndex);
			if ((c >= '0') && (c <= '9')) {
				final int oldWidth = texture.width;
				texture = digitGlyphs[fontStyle][c - '0'].texture;
				final int newWidth = texture.width;
				glyphX += (oldWidth - newWidth) >> 1;
			}
			if (boundTextureName != texture.textureName) {
				tessellator.draw();
				worldRenderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
				GlStateManager.bindTexture(texture.textureName);
				boundTextureName = texture.textureName;
			}
			final float x1 = startX + (glyphX / 2.0f);
			final float x2 = startX + ((glyphX + texture.width) / 2.0f);
			final float y1 = startY + (glyph.y / 2.0f);
			final float y2 = startY + ((glyph.y + texture.height) / 2.0f);
			worldRenderer.pos(x1, y1, 0.0).tex(texture.u1, texture.v1).color(color.r, color.g, color.b, color.a)
					.endVertex();
			worldRenderer.pos(x1, y2, 0.0).tex(texture.u1, texture.v2).color(color.r, color.g, color.b, color.a)
					.endVertex();
			worldRenderer.pos(x2, y2, 0.0).tex(texture.u2, texture.v2).color(color.r, color.g, color.b, color.a)
					.endVertex();
			worldRenderer.pos(x2, y1, 0.0).tex(texture.u2, texture.v1).color(color.r, color.g, color.b, color.a)
					.endVertex();
			++glyphIndex;
		}
		tessellator.draw();
		if (entry.specialRender) {
			int renderStyle = 0;
			color = new RGBA(initialColor);
			GlStateManager.disableTexture2D();
			worldRenderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
			int glyphIndex2 = 0;
			int colorIndex2 = 0;
			while (glyphIndex2 < entry.glyphs.length) {
				while ((colorIndex2 < entry.colors.length)
						&& (entry.glyphs[glyphIndex2].stringIndex >= entry.colors[colorIndex2].stringIndex)) {
					color = getColorCode(entry.colors[colorIndex2].colorCode, initialColor, shadowFlag);
					renderStyle = entry.colors[colorIndex2].renderStyle;
					++colorIndex2;
				}
				final Glyph glyph2 = entry.glyphs[glyphIndex2];
				final int glyphSpace = glyph2.advance - glyph2.texture.width;
				if ((renderStyle & 0x1) != 0x0) {
					final float x3 = startX + ((glyph2.x - glyphSpace) / 2.0f);
					final float x4 = startX + ((glyph2.x + glyph2.advance) / 2.0f);
					final float y3 = startY + 0.5f;
					final float y4 = startY + 1.5f;
					worldRenderer.pos(x3, y3, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
					worldRenderer.pos(x3, y4, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
					worldRenderer.pos(x4, y4, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
					worldRenderer.pos(x4, y3, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
				}
				if ((renderStyle & 0x2) != 0x0) {
					final float x3 = startX + ((glyph2.x - glyphSpace) / 2.0f);
					final float x4 = startX + ((glyph2.x + glyph2.advance) / 2.0f);
					final float y3 = startY - 3.0f;
					final float y4 = startY - 2.0f;
					worldRenderer.pos(x3, y3, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
					worldRenderer.pos(x3, y4, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
					worldRenderer.pos(x4, y4, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
					worldRenderer.pos(x4, y3, 0.0).color(color.r, color.g, color.b, color.a).endVertex();
				}
				++glyphIndex2;
			}
			tessellator.draw();
			GlStateManager.enableTexture2D();
		}
		return entry.advance / 2;
	}

	public void setCustomFont(final ResourceLocation resource, final int fontSize, final boolean antiAlias)
			throws Exception {
		glyphCache.setCustomFont(resource, fontSize, antiAlias);
		antiAliasEnabled = antiAlias;
		weakRefCache.clear();
		stringCache.clear();
		cacheDightGlyphs();
		updateHeight();
	}

	public void setDefaultFont(final String fontName, final int fontSize, final boolean antiAlias) {
		glyphCache.setDefaultFont(fontName, fontSize, antiAlias);
		antiAliasEnabled = antiAlias;
		weakRefCache.clear();
		stringCache.clear();
		cacheDightGlyphs();
		updateHeight();
	}

	private int sizeString(final String str, int width, final boolean breakAtSpaces) {
		if ((str == null) || str.isEmpty()) {
			return 0;
		}
		width += width;
		final Glyph[] glyphs = cacheString(str).glyphs;
		int wsIndex = -1;
		int advance;
		int index;
		for (advance = 0, index = 0; (index < glyphs.length)
				&& (advance <= width); advance += glyphs[index].advance, ++index) {
			if (breakAtSpaces) {
				final char c = str.charAt(glyphs[index].stringIndex);
				if (c == ' ') {
					wsIndex = index;
				} else if (c == '\n') {
					wsIndex = index;
					break;
				}
			}
		}
		if ((index < glyphs.length) && (wsIndex != -1) && (wsIndex < index)) {
			index = wsIndex;
		}
		return (index < glyphs.length) ? glyphs[index].stringIndex : str.length();
	}

	public int sizeStringToWidth(final String str, final int width) {
		return sizeString(str, width, true);
	}

	private int stripColorCodes(final Entry cacheEntry, final String str, final char[] text) {
		final List<ColorCode> colorList = new ArrayList<ColorCode>();
		int start = 0;
		int shift = 0;
		byte fontStyle = 0;
		byte renderStyle = 0;
		byte colorCode = -1;
		int next;
		while (((next = str.indexOf(167, start)) != -1) && ((next + 1) < str.length())) {
			System.arraycopy(text, (next - shift) + 2, text, next - shift, text.length - next - 2);
			final int code = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(str.charAt(next + 1)));
			switch (code) {
			case 16: {
				break;
			}
			case 17: {
				fontStyle |= 0x1;
				break;
			}
			case 18: {
				renderStyle |= 0x2;
				cacheEntry.specialRender = true;
				break;
			}
			case 19: {
				renderStyle |= 0x1;
				cacheEntry.specialRender = true;
				break;
			}
			case 20: {
				fontStyle |= 0x2;
				break;
			}
			case 21: {
				fontStyle = 0;
				renderStyle = 0;
				colorCode = -1;
				break;
			}
			default: {
				if ((code >= 0) && (code <= 15)) {
					colorCode = (byte) code;
					fontStyle = 0;
					renderStyle = 0;
					break;
				}
				break;
			}
			}
			final ColorCode entry = new ColorCode();
			entry.stringIndex = next;
			entry.stripIndex = next - shift;
			entry.colorCode = colorCode;
			entry.fontStyle = fontStyle;
			entry.renderStyle = renderStyle;
			colorList.add(entry);
			start = next + 2;
			shift += 2;
		}
		cacheEntry.colors = new ColorCode[colorList.size()];
		cacheEntry.colors = colorList.toArray(cacheEntry.colors);
		return text.length - shift;
	}

	public String trimStringToWidth(String str, final int width, final boolean reverse) {
		final int length = sizeString(str, width, false);
		str = str.substring(0, length);
		if (reverse) {
			str = new StringBuilder(str).reverse().toString();
		}
		return str;
	}

	public void updateHeight() {
		int height = 0;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (final Glyph g : cacheString("AaBbCcDdEeHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz").glyphs) {
			if (g.texture.height > height) {
				height = g.texture.height;
			}
			if (g.y < minY) {
				minY = g.y;
			}
			if (g.y > maxY) {
				maxY = g.y;
			}
		}
		fontHeight = (int) (((maxY + height) / 2.0f) - (minY / 2.0f));
	}

	public Font usedFont() {
		return glyphCache.usedFonts.get(0);
	}
}
