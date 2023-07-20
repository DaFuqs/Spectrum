package de.dafuqs.spectrum.helpers;

import java.math.BigDecimal;

import com.google.gson.*;
import net.minecraft.nbt.*;

/*

	This class parses JSON NBT, the format of which is described below, into
	Minecraft's NBT format. Unfortunately, due to the ambiguity of JSON,
	certain values can't currently be expressed with this. For example, the
	string "12b" will always be parsed as the byte 12, rather than as a
	string literal.

	JSON NBT Example:
	{
		"Compound": {
			"Shorts": [
				"32767s",
				"-32768S"
			],
			"Floats": [
				"-3.402823466e+38f",
				"3.402823466E+38F"
			],
			"Doubles": [
				"-1.7976931348623158e+308d",
				"1.7976931348623158E+308D",
				1000000000000000
			]
		},
		"List": [
			"This is a string",
			"This is another string"
		],
		"ByteArray": [
			"B;",
			"127b",
			"-128B",
			false,
			true
		],
		"IntArray": [
			"I;",
			"2147483647i",
			"-2147483648I",
			12.0,
		],
		"LongArray": [
			"L;",
			"9223372036854775807l",
			"-9223372036854775808L"
		]
	}

*/

public class JsonNbtHelper {
	public static byte getJsonElementType(JsonElement element) {
		if (element.isJsonObject())
			return NbtElement.COMPOUND_TYPE;
		if (element.isJsonArray())
			return getJsonArrayType(element.getAsJsonArray());
		if (element.isJsonPrimitive())
			return getJsonPrimitiveType(element.getAsJsonPrimitive());
		
		throw new UnsupportedOperationException("Unknown JSON NBT element type");
	}
	
	public static byte getJsonArrayType(JsonArray array) {
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
	
	public static NbtElement fromJson(JsonElement jsonElement) {
		if (jsonElement.isJsonObject())
			return fromJsonObject(jsonElement.getAsJsonObject());
		if (jsonElement.isJsonArray())
			return fromJsonArray(jsonElement.getAsJsonArray());
		if (jsonElement.isJsonPrimitive())
			return fromJsonPrimitive(jsonElement.getAsJsonPrimitive());
		
		throw new UnsupportedOperationException("Unknown JSON NBT element type");
	}
	
	public static NbtElement fromJsonObject(JsonObject jsonObject) {
		NbtCompound result = new NbtCompound();
		
		jsonObject.entrySet().forEach(entry -> {
			String name = entry.getKey();
			JsonElement element = entry.getValue();
			if (element != null) {
				result.put(name, fromJson(element));
			}
		});
		
		return result;
	}
	
	public static NbtElement fromJsonArray(JsonArray array) {
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
}
