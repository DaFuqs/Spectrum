package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

public class PotionWorkshopCraftingRecipeSerializer implements GatedRecipeSerializer<PotionWorkshopCraftingRecipe> {
	
	public final PotionWorkshopCraftingRecipeSerializer.RecipeFactory<PotionWorkshopCraftingRecipe> recipeFactory;
	
	public PotionWorkshopCraftingRecipeSerializer(PotionWorkshopCraftingRecipeSerializer.RecipeFactory<PotionWorkshopCraftingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<PotionWorkshopCraftingRecipe> {
		PotionWorkshopCraftingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient baseIngredient, boolean consumeBaseIngredient, int requiredExperience, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, ItemStack output, int craftingTime, int color);
	}
	
	@Override
	public PotionWorkshopCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		Ingredient baseIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base_ingredient"));
		Ingredient ingredient1 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient1"));
		Ingredient ingredient2;
		if (JsonHelper.hasJsonObject(jsonObject, "ingredient2")) {
			ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient2"));
		} else {
			ingredient2 = Ingredient.EMPTY;
		}
		Ingredient ingredient3;
		if (JsonHelper.hasJsonObject(jsonObject, "ingredient3")) {
			ingredient3 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient3"));
		} else {
			ingredient3 = Ingredient.EMPTY;
		}
		
		int requiredExperience = JsonHelper.getInt(jsonObject, "required_experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		int color = JsonHelper.getInt(jsonObject, "color", 0xc03058);
		boolean consumeBaseIngredient = JsonHelper.getBoolean(jsonObject, "use_up_base_ingredient", true);
		ItemStack output = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, baseIngredient, consumeBaseIngredient, requiredExperience, ingredient1, ingredient2, ingredient3, output, craftingTime, color);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PotionWorkshopCraftingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.baseIngredient.write(packetByteBuf);
		packetByteBuf.writeBoolean(recipe.consumeBaseIngredient);
		packetByteBuf.writeInt(recipe.requiredExperience);
		recipe.ingredient1.write(packetByteBuf);
		recipe.ingredient2.write(packetByteBuf);
		recipe.ingredient3.write(packetByteBuf);
		packetByteBuf.writeItemStack(recipe.output);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeInt(recipe.color);
	}
	
	@Override
	public PotionWorkshopCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Ingredient baseIngredient = Ingredient.fromPacket(packetByteBuf);
		boolean consumeBaseIngredient = packetByteBuf.readBoolean();
		int requiredExperience = packetByteBuf.readInt();
		Ingredient ingredient1 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
		ItemStack output = packetByteBuf.readItemStack();
		int craftingTime = packetByteBuf.readInt();
		int color = packetByteBuf.readInt();
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, baseIngredient, consumeBaseIngredient, requiredExperience, ingredient1, ingredient2, ingredient3, output, craftingTime, color);
	}
	
}
