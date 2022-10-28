package de.dafuqs.spectrum.recipe.midnight_solution_converting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class MidnightSolutionConvertingRecipeSerializer implements GatedRecipeSerializer<MidnightSolutionConvertingRecipe> {
	
	public final MidnightSolutionConvertingRecipeSerializer.RecipeFactory<MidnightSolutionConvertingRecipe> recipeFactory;
	
	public MidnightSolutionConvertingRecipeSerializer(MidnightSolutionConvertingRecipeSerializer.RecipeFactory<MidnightSolutionConvertingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<MidnightSolutionConvertingRecipe> {
		MidnightSolutionConvertingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, ItemStack outputItemStack);
	}
	
	@Override
	public MidnightSolutionConvertingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonElement jsonElement = JsonHelper.getObject(jsonObject, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonElement);
		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, outputItemStack);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, MidnightSolutionConvertingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeItemStack(recipe.outputItemStack);
	}
	
	@Override
	public MidnightSolutionConvertingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredient, outputItemStack);
	}
	
}
