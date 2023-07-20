package de.dafuqs.spectrum.helpers;

import java.math.BigDecimal;

import com.google.gson.*;
import net.minecraft.nbt.*;

public class NbtHelper {
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
					case "B;": return NbtElement.BYTE_ARRAY_TYPE;
					case "I;": return NbtElement.INT_ARRAY_TYPE;
					case "L;": return NbtElement.LONG_ARRAY_TYPE;
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
				switch (string.charAt(string.length()-1)) {
					case 'b': case 'B': return NbtElement.BYTE_TYPE;
					case 's': case 'S': return NbtElement.SHORT_TYPE;
					case 'i': case 'I': return NbtElement.INT_TYPE;
					case 'l': case 'L': return NbtElement.LONG_TYPE;
					case 'f': case 'F': return NbtElement.FLOAT_TYPE;
					case 'd': case 'D': return NbtElement.DOUBLE_TYPE;
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
		
		AbstractNbtList<?> nbtArray;
		switch (type) {
			case NbtElement.BYTE_ARRAY_TYPE: nbtArray = new NbtByteArray(new byte[0]); break;
			case NbtElement.INT_ARRAY_TYPE: nbtArray = new NbtIntArray(new int[0]); break;
			case NbtElement.LONG_ARRAY_TYPE: nbtArray = new NbtLongArray(new long[0]); break;
			default: throw new UnsupportedOperationException("Unknown JSON NBT list type");
		}
		
		for (int i = 1; i < array.size(); i++) {
			nbtArray.addElement(i-1, fromJson(array.get(i)));
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
				case NbtElement.INT_TYPE: return NbtInt.of(primitive.getAsInt());
				case NbtElement.DOUBLE_TYPE: return NbtDouble.of(primitive.getAsDouble());
			}
		}
		
		if (primitive.isString()) {
			String string = primitive.getAsString();
			if (string.length() > 1) {
				String numStr = string.substring(0, string.length()-1);
				switch (type) {
					case NbtElement.BYTE_TYPE: return NbtByte.of(Byte.valueOf(numStr));
					case NbtElement.SHORT_TYPE: return NbtShort.of(Short.valueOf(numStr));
					case NbtElement.INT_TYPE: return NbtInt.of(Integer.valueOf(numStr));
					case NbtElement.LONG_TYPE: return NbtLong.of(Long.valueOf(numStr));
					case NbtElement.FLOAT_TYPE: return NbtFloat.of(Float.valueOf(numStr));
					case NbtElement.DOUBLE_TYPE: return NbtDouble.of(Double.valueOf(numStr));
				}
			}
			
			return NbtString.of(string);
		}
		
		throw new UnsupportedOperationException("Unknown JSON NBT primitive type");
	}
	
	/**
	 * Writes the delta into the original, maintaining the previous data unless
	 * overwritten.
	 * 
	 * If the provided elements are primitives, the delta will be returned.
	 * If the provided elements are arrays or lists, the delta will be returned.
	 * If the provided elements are compound, new keys will be added and existing
	 * keys will be merged.
	 * Otherwise, the original will be returned.
	 */
	public static NbtElement mergeNbt(NbtElement original, NbtElement delta) {
		if (original.getType() != delta.getType()) {
			return original;
		}
		
		switch (original.getType()) {
			case NbtElement.BYTE_TYPE:
			case NbtElement.SHORT_TYPE:
			case NbtElement.INT_TYPE:
			case NbtElement.LONG_TYPE:
			case NbtElement.FLOAT_TYPE:
			case NbtElement.DOUBLE_TYPE:
			case NbtElement.STRING_TYPE:
			case NbtElement.END_TYPE:
				return delta;
			
			case NbtElement.BYTE_ARRAY_TYPE:
			case NbtElement.INT_ARRAY_TYPE:
			case NbtElement.LONG_ARRAY_TYPE:
			case NbtElement.LIST_TYPE:
				return delta;
			
			case NbtElement.COMPOUND_TYPE: {
				NbtCompound originalCompound = (NbtCompound)original;
				NbtCompound deltaCompound = (NbtCompound)delta;
				
				deltaCompound.getKeys().forEach(key -> {
					NbtElement value = deltaCompound.get(key);
					
					if (originalCompound.contains(key)) {
						mergeNbt(originalCompound.get(key), value);
					} else {
						originalCompound.put(key, value);
					}
				});
				
				return original;
			}
			
			default: return original;
		}
	}
}
