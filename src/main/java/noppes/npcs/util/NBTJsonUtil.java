//

//

package noppes.npcs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;

import com.google.common.io.Files;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class NBTJsonUtil {
	public static class JsonException extends Exception {
		public JsonException(final String message, final JsonFile json) {
			super(message + ": " + json.getCurrentPos());
		}
	}

	static class JsonFile {
		private String original;
		private String text;

		public JsonFile(final String text) {
			this.text = text;
			original = text;
		}

		public String cut(final int i) {
			final String s = text.substring(0, i);
			text = text.substring(i).trim();
			return s;
		}

		public String cutDirty(final int i) {
			final String s = text.substring(0, i);
			text = text.substring(i);
			return s;
		}

		public boolean endsWith(final String s) {
			return text.endsWith(s);
		}

		public String getCurrentPos() {
			final int lengthOr = original.length();
			final int lengthCur = text.length();
			final int currentPos = lengthOr - lengthCur;
			final String done = original.substring(0, currentPos);
			final String[] lines = done.split("\r\n|\r|\n");
			int pos = 0;
			String line = "";
			if (lines.length > 0) {
				pos = lines[lines.length - 1].length();
				line = original.split("\r\n|\r|\n")[lines.length - 1].trim();
			}
			return "Line: " + lines.length + ", Pos: " + pos + ", Text: " + line;
		}

		public int indexOf(final String s) {
			return text.indexOf(s);
		}

		public boolean startsWith(final String... ss) {
			for (final String s : ss) {
				if (text.startsWith(s)) {
					return true;
				}
			}
			return false;
		}

		public String substring(final int beginIndex, final int endIndex) {
			return text.substring(beginIndex, endIndex);
		}
	}

	static class JsonLine {
		private String line;

		public JsonLine(final String line) {
			this.line = line;
		}

		public boolean increaseTab() {
			return line.endsWith("{") || line.endsWith("[");
		}

		public boolean reduceTab() {
			final int length = line.length();
			return ((length == 1) && (line.endsWith("}") || line.endsWith("]")))
					|| ((length == 2) && (line.endsWith("},") || line.endsWith("],")));
		}

		public void removeComma() {
			if (line.endsWith(",")) {
				line = line.substring(0, line.length() - 1);
			}
		}

		@Override
		public String toString() {
			return line;
		}
	}

	public static String Convert(final NBTTagCompound compound) {
		final List<JsonLine> list = new ArrayList<JsonLine>();
		final JsonLine line = ReadTag("", compound, list);
		line.removeComma();
		return ConvertList(list);
	}

	public static NBTTagCompound Convert(String json) throws JsonException {
		json = json.trim();
		final JsonFile file = new JsonFile(json);
		if (!json.startsWith("{") || !json.endsWith("}")) {
			throw new JsonException("Not properly incapsulated between { }", file);
		}
		final NBTTagCompound compound = new NBTTagCompound();
		FillCompound(compound, file);
		return compound;
	}

	private static String ConvertList(final List<JsonLine> list) {
		String json = "";
		int tab = 0;
		for (final JsonLine tag : list) {
			if (tag.reduceTab()) {
				--tab;
			}
			for (int i = 0; i < tab; ++i) {
				json += "    ";
			}
			json = json + tag + "\n";
			if (tag.increaseTab()) {
				++tab;
			}
		}
		return json;
	}

	public static void FillCompound(final NBTTagCompound compound, final JsonFile json) throws JsonException {
		if (json.startsWith("{") || json.startsWith(",")) {
			json.cut(1);
		}
		if (json.startsWith("}")) {
			return;
		}
		final int index = json.indexOf(":");
		if (index < 1) {
			throw new JsonException("Expected key after ,", json);
		}
		String key = json.substring(0, index);
		json.cut(index + 1);
		NBTBase base = ReadValue(json);
		if (base == null) {
			base = new NBTTagString();
		}
		if (key.startsWith("\"")) {
			key = key.substring(1);
		}
		if (key.endsWith("\"")) {
			key = key.substring(0, key.length() - 1);
		}
		compound.setTag(key, base);
		if (json.startsWith(",")) {
			FillCompound(compound, json);
		}
	}

	private static List<NBTBase> getListData(final NBTTagList list) {
		return (List<NBTBase>) ObfuscationReflectionHelper.getPrivateValue((Class) NBTTagList.class, list, 1);
	}

	public static NBTTagCompound LoadFile(final File file) throws IOException, JsonException {
		return Convert(Files.toString(file, Charsets.UTF_8));
	}

	private static JsonLine ReadTag(String name, final NBTBase base, final List<JsonLine> list) {
		if (!name.isEmpty()) {
			name = "\"" + name + "\": ";
		}
		if (base.getId() == 9) {
			list.add(new JsonLine(name + "["));
			final NBTTagList tags = (NBTTagList) base;
			JsonLine line = null;
			final List<NBTBase> data = getListData(tags);
			for (final NBTBase b : data) {
				line = ReadTag("", b, list);
			}
			if (line != null) {
				line.removeComma();
			}
			list.add(new JsonLine("]"));
		} else if (base.getId() == 10) {
			list.add(new JsonLine(name + "{"));
			final NBTTagCompound compound = (NBTTagCompound) base;
			JsonLine line = null;
			for (final Object key : compound.getKeySet()) {
				line = ReadTag(key.toString(), compound.getTag(key.toString()), list);
			}
			if (line != null) {
				line.removeComma();
			}
			list.add(new JsonLine("}"));
		} else if (base.getId() == 11) {
			list.add(new JsonLine(name + base.toString().replaceFirst(",]", "]")));
		} else {
			list.add(new JsonLine(name + base));
		}
		final JsonLine jsonLine;
		final JsonLine line2 = jsonLine = list.get(list.size() - 1);
		jsonLine.line += ",";
		return line2;
	}

	public static NBTBase ReadValue(final JsonFile json) throws JsonException {
		if (json.startsWith("{")) {
			final NBTTagCompound compound = new NBTTagCompound();
			FillCompound(compound, json);
			if (!json.startsWith("}")) {
				throw new JsonException("Expected }", json);
			}
			json.cut(1);
			return compound;
		} else if (json.startsWith("[")) {
			json.cut(1);
			final NBTTagList list = new NBTTagList();
			for (NBTBase value = ReadValue(json); value != null; value = ReadValue(json)) {
				list.appendTag(value);
				if (!json.startsWith(",")) {
					break;
				}
				json.cut(1);
			}
			if (!json.startsWith("]")) {
				throw new JsonException("Expected ]", json);
			}
			json.cut(1);
			if (list.getTagType() == 3) {
				final int[] arr = new int[list.tagCount()];
				int i = 0;
				while (list.tagCount() > 0) {
					arr[i] = ((NBTTagInt) list.removeTag(0)).getInt();
					++i;
				}
				return new NBTTagIntArray(arr);
			}
			if (list.getTagType() == 1) {
				final byte[] arr2 = new byte[list.tagCount()];
				int i = 0;
				while (list.tagCount() > 0) {
					arr2[i] = ((NBTTagByte) list.removeTag(0)).getByte();
					++i;
				}
				return new NBTTagByteArray(arr2);
			}
			return list;
		} else {
			if (json.startsWith("\"")) {
				json.cut(1);
				String s = "";
				String cut;
				for (boolean ignore = false; !json.startsWith("\"") || ignore; ignore = cut.equals("\\"), s += cut) {
					cut = json.cutDirty(1);
				}
				json.cut(1);
				return new NBTTagString(s.replace("\\\"", "\""));
			}
			String s = "";
			while (!json.startsWith(",", "]", "}")) {
				s += json.cut(1);
			}
			s = s.trim().toLowerCase();
			if (s.isEmpty()) {
				return null;
			}
			try {
				if (s.endsWith("d")) {
					return new NBTTagDouble(Double.parseDouble(s.substring(0, s.length() - 1)));
				}
				if (s.endsWith("f")) {
					return new NBTTagFloat(Float.parseFloat(s.substring(0, s.length() - 1)));
				}
				if (s.endsWith("b")) {
					return new NBTTagByte(Byte.parseByte(s.substring(0, s.length() - 1)));
				}
				if (s.endsWith("s")) {
					return new NBTTagShort(Short.parseShort(s.substring(0, s.length() - 1)));
				}
				if (s.endsWith("l")) {
					return new NBTTagLong(Long.parseLong(s.substring(0, s.length() - 1)));
				}
				if (s.contains(".")) {
					return new NBTTagDouble(Double.parseDouble(s));
				}
				return new NBTTagInt(Integer.parseInt(s));
			} catch (NumberFormatException ex) {
				throw new JsonException("Unable to convert: " + s + " to a number", json);
			}
		}
	}

	public static void SaveFile(final File file, final NBTTagCompound compound) throws IOException, JsonException {
		final String json = Convert(compound);
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
			writer.write(json);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
