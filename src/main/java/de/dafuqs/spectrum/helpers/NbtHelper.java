package de.dafuqs.spectrum.helpers;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.nbt.*;
import org.apache.commons.lang3.math.*;

import java.math.*;
import java.util.*;

public class NbtHelper {
	public static Optional<NbtCompound> getNbtCompound(JsonElement json) {
		if (json == null || json.isJsonNull()) {
			return Optional.empty();
		}
		
		if (json.isJsonObject()) {
			return Optional.of(fromJsonObject(json.getAsJsonObject()));
		}
		
		if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
			try {
				return Optional.of(StringNbtReader.parse(json.getAsString()));
			} catch (CommandSyntaxException exception) {
				exception.printStackTrace();
			}
		}
		
		throw new UnsupportedOperationException("Nbt element is not an object or a string");
	}
	
	public static byte getJsonElementType(JsonElement element) {
		if (element == null)
			throw new UnsupportedOperationException("Null JSON NBT element");
		if (element.isJsonObject())
			return NbtElement.COMPOUND_TYPE;
		if (element.isJsonArray())
			return getJsonArrayType(element.getAsJsonArray());
		if (element.isJsonPrimitive())
			return getJsonPrimitiveType(element.getAsJsonPrimitive());
		
		throw new UnsupportedOperationException("Unknown JSON NBT element type");
	}
	
	public static byte getJsonArrayType(JsonArray array) {
		if (array == null) {
			throw new UnsupportedOperationException("Null JSON NBT element");
		}
		
		if (array.size() > 0) {
			JsonElement first = array.get(0);
			byte type = getJsonElementType(first);
			
			if (type == NbtElement.STRING_TYPE) {
				switch (first.getAsString()) {
					case "B;" -> {
						return NbtElement.BYTE_ARRAY_TYPE;
					}
					case "I;" -> {
						return NbtElement.INT_ARRAY_TYPE;
					}
					case "L;" -> {
						return NbtElement.LONG_ARRAY_TYPE;
					}
				}
			}
		}
		
		return NbtElement.LIST_TYPE;
	}
	
	public static byte getJsonPrimitiveType(JsonPrimitive primitive) {
		if (primitive == null) {
			throw new UnsupportedOperationException("Null JSON NBT element");
		}
		
		if (primitive.isBoolean()) {
			return NbtElement.BYTE_TYPE;
		}
		
		if (primitive.isNumber()) {
			// The numbers will either be integers or doubles. Since JSON
			// doesn't differentiate, we'll get it as a decimal and check
			// if there's a fractional part. It can't tell 1.0 versus 1,
			// but you can always specify with suffixes.
			
			BigDecimal bigDecimal = primitive.getAsBigDecimal();
			try {
				bigDecimal.intValueExact();
				return NbtElement.INT_TYPE;
			} catch (ArithmeticException exception) {
				return NbtElement.DOUBLE_TYPE;
			}
		}
		
		if (primitive.isString()) {
			String string = primitive.getAsString();
			if (string.length() > 1) {
				String numStr = string.substring(0, string.length()-1);
				if (NumberUtils.isParsable(numStr)) {
					switch (string.charAt(string.length() - 1)) {
						case 'b', 'B' -> {
							return NbtElement.BYTE_TYPE;
						}
						case 's', 'S' -> {
							return NbtElement.SHORT_TYPE;
						}
						case 'i', 'I' -> {
							return NbtElement.INT_TYPE;
						}
						case 'l', 'L' -> {
							return NbtElement.LONG_TYPE;
						}
						case 'f', 'F' -> {
							return NbtElement.FLOAT_TYPE;
						}
						case 'd', 'D' -> {
							return NbtElement.DOUBLE_TYPE;
						}
					}
				}
			}
			
			return NbtElement.STRING_TYPE;
		}
		
		throw new UnsupportedOperationException("Unknown JSON NBT primitive type");
	}
	
	public static NbtElement fromJson(JsonElement element) {
		if (element == null)
			throw new UnsupportedOperationException("Null JSON NBT element");
		if (element.isJsonObject())
			return fromJsonObject(element.getAsJsonObject());
		if (element.isJsonArray())
			return fromJsonArray(element.getAsJsonArray());
		if (element.isJsonPrimitive())
			return fromJsonPrimitive(element.getAsJsonPrimitive());
		
		throw new UnsupportedOperationException("Unknown JSON NBT element type");
	}
	
	public static NbtCompound fromJsonObject(JsonObject object) {
		if (object == null) {
			throw new UnsupportedOperationException("Null JSON NBT element");
		}
		
		NbtCompound result = new NbtCompound();
		
		object.entrySet().forEach(entry -> {
			String name = entry.getKey();
			JsonElement element = entry.getValue();
			if (element != null) {
				result.put(name, fromJson(element));
			}
		});
		
		return result;
	}
	
	public static AbstractNbtList<?> fromJsonArray(JsonArray array) {
		byte type = getJsonArrayType(array);
		
		if (type == NbtElement.LIST_TYPE) {
			NbtList list = new NbtList();
			for (int i = 0; i < array.size(); i++) {
				list.add(i, fromJson(array.get(i)));
			}
			return list;
		}
		
		AbstractNbtList<?> nbtArray = switch (type) {
			case NbtElement.BYTE_ARRAY_TYPE -> new NbtByteArray(new byte[0]);
			case NbtElement.INT_ARRAY_TYPE -> new NbtIntArray(new int[0]);
			case NbtElement.LONG_ARRAY_TYPE -> new NbtLongArray(new long[0]);
			default -> throw new UnsupportedOperationException("Unknown JSON NBT list type");
		};
		
		for (int i = 1; i < array.size(); i++) {
			nbtArray.addElement(i - 1, fromJson(array.get(i)));
		}
		
		return nbtArray;
	}
	
	public static NbtElement fromJsonPrimitive(JsonPrimitive primitive) {
		byte type = getJsonPrimitiveType(primitive);
		
		if (primitive.isBoolean()) {
			return NbtByte.of((byte)(primitive.getAsBoolean() ? 1 : 0));
		}
		
		if (primitive.isNumber()) {
			switch (type) {
				case NbtElement.INT_TYPE -> {
					return NbtInt.of(primitive.getAsInt());
				}
				case NbtElement.DOUBLE_TYPE -> {
					return NbtDouble.of(primitive.getAsDouble());
				}
			}
		}
		
		if (primitive.isString()) {
			String string = primitive.getAsString();
			if (string.length() > 1) {
				String numStr = string.substring(0, string.length()-1);
				switch (type) {
					case NbtElement.BYTE_TYPE -> {
						return NbtByte.of(Byte.parseByte(numStr));
					}
					case NbtElement.SHORT_TYPE -> {
						return NbtShort.of(Short.parseShort(numStr));
					}
					case NbtElement.INT_TYPE -> {
						return NbtInt.of(Integer.parseInt(numStr));
					}
					case NbtElement.LONG_TYPE -> {
						return NbtLong.of(Long.parseLong(numStr));
					}
					case NbtElement.FLOAT_TYPE -> {
						return NbtFloat.of(Float.parseFloat(numStr));
					}
					case NbtElement.DOUBLE_TYPE -> {
						return NbtDouble.of(Double.parseDouble(numStr));
					}
				}
			}
			
			return NbtString.of(string);
		}
		
		throw new UnsupportedOperationException("Unknown JSON NBT primitive type");
	}
	
	/**
	 * Writes the delta into the original, maintaining the previous data unless
	 * overwritten.
	 * <p>
	 * If the provided elements are primitives, the delta will be returned.
	 * If the provided elements are arrays or lists, the delta will be returned.
	 * If the provided elements are compound, new keys will be added and existing
	 * keys will be merged.
	 * Otherwise, the original will be returned.
	 */
	public static void mergeNbt(NbtElement original, NbtElement delta) {
		if (original.getType() != delta.getType()) {
			return;
		}
		
		switch (original.getType()) {
			case NbtElement.BYTE_TYPE, NbtElement.SHORT_TYPE, NbtElement.NUMBER_TYPE, NbtElement.LONG_TYPE, NbtElement.FLOAT_TYPE, NbtElement.DOUBLE_TYPE, NbtElement.STRING_TYPE, NbtElement.END_TYPE,
					NbtElement.BYTE_ARRAY_TYPE, NbtElement.INT_ARRAY_TYPE, NbtElement.LONG_ARRAY_TYPE, NbtElement.LIST_TYPE -> {
				
			}
			case NbtElement.COMPOUND_TYPE -> {
				NbtCompound originalCompound = (NbtCompound) original;
				NbtCompound deltaCompound = (NbtCompound) delta;
				
				deltaCompound.getKeys().forEach(key -> {
					NbtElement value = deltaCompound.get(key);
					
					if (originalCompound.contains(key)) {
						mergeNbt(originalCompound.get(key), value);
					} else {
						originalCompound.put(key, value);
					}
				});
				
			}
			default -> {
			}
		}
	}
}
