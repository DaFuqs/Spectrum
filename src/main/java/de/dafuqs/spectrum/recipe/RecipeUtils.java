package de.dafuqs.spectrum.recipe;

import com.google.gson.*;
import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.serialization.*;
import net.minecraft.commands.arguments.blocks.*;
import net.minecraft.core.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;

public class RecipeUtils {
	
	public static ItemStack itemStackWithNbtFromJson(JsonObject json) {
		Item item = BuiltInRegistries.ITEM.byNameCodec().parse(JsonOps.INSTANCE, json).getOrThrow();
		if (json.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			int count = GsonHelper.getAsInt(json, "count", 1);
			
			if (count < 1) {
				throw new JsonSyntaxException("Invalid output count: " + count);
			} else {
				ItemStack stack = new ItemStack(item, count);

				// TODO - Replace this with component handling instead?
				//Optional<NbtCompound> nbt = NbtHelper.getNbtCompound(json.get("nbt"));
				//nbt.ifPresent(stack::setNbt);
				
				return stack;
			}
		}
	}
	
	public static BlockState blockStateFromString(String string) throws CommandSyntaxException {
		return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), new StringReader(string), true).blockState();
	}
	
	public static DataResult<BlockState> blockStateDataFromString(String string) {
		try {
			return DataResult.success(blockStateFromString(string));
		} catch (CommandSyntaxException e) {
			return DataResult.error(e::getMessage);
		}
	}
	
	public static String blockStateToString(BlockState state) {
		return BlockStateParser.serialize(state);
	}

	/* TODO - Remove
	public static List<IngredientStack> createIngredientStackPatternMatrix(String[] pattern, Map<String, IngredientStack> symbols, int width, int height) {
		List<IngredientStack> list = DefaultedList.ofSize(width * height, IngredientStack.EMPTY);
		Set<String> set = Sets.newHashSet(symbols.keySet());
		set.remove(" ");

		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String string = pattern[i].substring(j, j + 1);
				var ingredient = symbols.get(string);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
				}

				set.remove(string);
				list.set(j + width * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return list;
		}
	}

	 */

}
