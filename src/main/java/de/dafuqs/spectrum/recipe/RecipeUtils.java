package de.dafuqs.spectrum.recipe;

import com.google.gson.*;
import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.block.*;
import net.minecraft.command.argument.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

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
	
	public static BlockState blockStateFromString(String string) throws CommandSyntaxException {
		return BlockArgumentParser.block(Registry.BLOCK, new StringReader(string), true).blockState();
	}
	
	public static String blockStateToString(BlockState state) {
		return BlockArgumentParser.stringifyBlockState(state);
	}
	
}
