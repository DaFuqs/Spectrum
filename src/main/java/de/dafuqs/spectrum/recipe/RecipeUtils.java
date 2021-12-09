package de.dafuqs.spectrum.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;

public class RecipeUtils {
	
	public static ItemStack outputWithNbtFromJson(JsonObject json) {
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
	
}
