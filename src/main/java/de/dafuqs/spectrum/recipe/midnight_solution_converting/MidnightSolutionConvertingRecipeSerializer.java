package de.dafuqs.spectrum.recipe.midnight_solution_converting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class MidnightSolutionConvertingRecipeSerializer implements RecipeSerializer<MidnightSolutionConvertingRecipe> {
	
	public final MidnightSolutionConvertingRecipeSerializer.RecipeFactory<MidnightSolutionConvertingRecipe> recipeFactory;
	
	public MidnightSolutionConvertingRecipeSerializer(MidnightSolutionConvertingRecipeSerializer.RecipeFactory<MidnightSolutionConvertingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public MidnightSolutionConvertingRecipe read(Identifier identifier, JsonObject jsonObject) {
		JsonElement jsonElement = JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		return this.recipeFactory.create(identifier, ingredient, outputItemStack);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, MidnightSolutionConvertingRecipe anvilCrushingRecipe) {
		anvilCrushingRecipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeItemStack(anvilCrushingRecipe.outputItemStack);
	}
	
	@Override
	public MidnightSolutionConvertingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		return this.recipeFactory.create(identifier, ingredient, outputItemStack);
	}
	
	public interface RecipeFactory<MidnightSolutionConvertingRecipe> {
		MidnightSolutionConvertingRecipe create(Identifier id, Ingredient inputIngredient, ItemStack outputItemStack);
	}
	
}
