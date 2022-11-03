package de.dafuqs.spectrum.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.id.incubus_core.IncubusCore;
import net.id.incubus_core.recipe.IngredientStack;
import net.id.incubus_core.recipe.matchbook.Matchbook;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

import static net.id.incubus_core.json.JsonUtils.matchbookFromJson;

public class RecipeUtils {
	
	public static ItemStack itemStackWithNbtFromJson(JsonObject json) {
		Item item = ShapedRecipe.getItem(json);
		if (json.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			int count = JsonHelper.getInt(json, "count", 1);
			
			if (count < 1) {
				throw new JsonSyntaxException("Invalid output count: " + count);
			} else {
				ItemStack stack = new ItemStack(item, count);
				
				String nbt = JsonHelper.getString(json, "nbt", "");
				if (!nbt.isEmpty()) {
					try {
						NbtCompound compound = NbtHelper.fromNbtProviderString(nbt);
						compound.remove("palette"); // o_O why is that necessary?
						stack.setNbt(compound);
					} catch (CommandSyntaxException e) {
						throw new JsonSyntaxException("Invalid output nbt: " + nbt);
					}
				}
				
				return stack;
			}
		}
	}
	
	public static List<IngredientStack> ingredientStacksFromJson(JsonArray array, int size) {
		List<IngredientStack> ingredients = new ArrayList<>(size);
		int dif = size - array.size();
		for (int i = 0; i < array.size() && i < size; i++) {
			JsonObject object = array.get(i).getAsJsonObject();
			ingredients.add(ingredientStackFromJson(object));
		}
		if (dif > 0) {
			for (int i = 0; i < dif; i++) {
				ingredients.add(IngredientStack.EMPTY);
			}
		}
		return ingredients;
	}
	
	public static IngredientStack ingredientStackFromJson(JsonObject json) {
		Ingredient ingredient = Ingredient.fromJson(json);
		var matchbook = Matchbook.empty();
		NbtCompound recipeViewNbt = null;
		int count = json.has("count") ? json.get("count").getAsInt() : 1;

		if (json.has("matchbook")) {
			try {
				matchbook = matchbookFromJson(json.getAsJsonObject("matchbook"));
			} catch (MalformedJsonException e) {
				IncubusCore.LOG.error("RELAYED EXCEPTION. " + e);
			}
		}

		if (json.has("recipeViewNbt")) {
			try {
				recipeViewNbt = NbtHelper.fromNbtProviderString(json.get("recipeViewNbt").getAsString());
			} catch (CommandSyntaxException e) {
				IncubusCore.LOG.error("RELAYED EXCEPTION. " + e);
			}
		}

		return IngredientStack.of(ingredient, matchbook, recipeViewNbt, count);
	}
	
	public static BlockState blockStateFromString(String string) throws CommandSyntaxException {
		return new BlockArgumentParser(new StringReader(string), true).parse(false).getBlockState();
	}
	
	public static String blockStateToString(BlockState state) {
		return BlockArgumentParser.stringifyBlockState(state);
	}
	
}
