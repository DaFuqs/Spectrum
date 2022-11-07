package de.dafuqs.spectrum.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.id.incubus_core.json.RecipeParser;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;

import java.util.List;

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
		return RecipeParser.ingredientStacksFromJson(array, size);
	}
	
	public static IngredientStack ingredientStackFromJson(JsonObject json) {
		return RecipeParser.ingredientStackFromJson(json);
	}
	
	public static BlockState blockStateFromString(String string) throws CommandSyntaxException {
		return new BlockArgumentParser(new StringReader(string), true).parse(false).getBlockState();
	}
	
	public static String blockStateToString(BlockState state) {
		return BlockArgumentParser.stringifyBlockState(state);
	}
	
}
