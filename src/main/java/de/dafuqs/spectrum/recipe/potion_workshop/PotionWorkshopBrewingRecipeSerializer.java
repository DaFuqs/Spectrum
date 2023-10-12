package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.network.*;
import net.minecraft.util.*;

public class PotionWorkshopBrewingRecipeSerializer implements GatedRecipeSerializer<PotionWorkshopBrewingRecipe> {
	
	public final PotionWorkshopBrewingRecipeSerializer.RecipeFactory recipeFactory;
	
	public PotionWorkshopBrewingRecipeSerializer(PotionWorkshopBrewingRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory {
		PotionWorkshopBrewingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, int craftingTime, IngredientStack ingredient1, IngredientStack ingredient2, IngredientStack ingredient3, PotionRecipeEffect recipeData);
	}
	
	@Override
	public PotionWorkshopBrewingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		IngredientStack ingredient1 = RecipeParser.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "ingredient1"));
		IngredientStack ingredient2;
		if (JsonHelper.hasJsonObject(jsonObject, "ingredient2")) {
			ingredient2 = RecipeParser.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "ingredient2"));
		} else {
			ingredient2 = IngredientStack.EMPTY;
		}
		IngredientStack ingredient3;
		if (JsonHelper.hasJsonObject(jsonObject, "ingredient3")) {
			ingredient3 = RecipeParser.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "ingredient3"));
		} else {
			ingredient3 = IngredientStack.EMPTY;
		}
		
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		
		PotionRecipeEffect recipeData = PotionRecipeEffect.read(jsonObject);
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingTime, ingredient1, ingredient2, ingredient3, recipeData);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PotionWorkshopBrewingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeInt(recipe.craftingTime);
		recipe.ingredient1.write(packetByteBuf);
		recipe.ingredient2.write(packetByteBuf);
		recipe.ingredient3.write(packetByteBuf);
		recipe.recipeData.write(packetByteBuf);
	}
	
	@Override
	public PotionWorkshopBrewingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		int craftingTime = packetByteBuf.readInt();
		IngredientStack ingredient1 = IngredientStack.fromByteBuf(packetByteBuf);
		IngredientStack ingredient2 = IngredientStack.fromByteBuf(packetByteBuf);
		IngredientStack ingredient3 = IngredientStack.fromByteBuf(packetByteBuf);
		
		PotionRecipeEffect recipeData = PotionRecipeEffect.read(packetByteBuf);
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingTime, ingredient1, ingredient2, ingredient3, recipeData);
	}
	
}
