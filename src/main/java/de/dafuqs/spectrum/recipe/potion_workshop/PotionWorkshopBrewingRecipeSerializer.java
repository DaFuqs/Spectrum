package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class PotionWorkshopBrewingRecipeSerializer implements GatedRecipeSerializer<PotionWorkshopBrewingRecipe> {
	
	public final PotionWorkshopBrewingRecipeSerializer.RecipeFactory<PotionWorkshopBrewingRecipe> recipeFactory;
	
	public PotionWorkshopBrewingRecipeSerializer(PotionWorkshopBrewingRecipeSerializer.RecipeFactory<PotionWorkshopBrewingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<PotionWorkshopBrewingRecipe> {
		PotionWorkshopBrewingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, int craftingTime, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, int color, boolean applicableToPotions, boolean applicableToTippedArrows, boolean applicableToPotionFillabes);
	}
	
	@Override
	public PotionWorkshopBrewingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
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
		
		boolean applicableToPotions = JsonHelper.getBoolean(jsonObject, "applicable_to_potions", true);
		boolean applicableToTippedArrows = JsonHelper.getBoolean(jsonObject, "applicable_to_tipped_arrows", true);
		boolean applicableToPotionFillabes = JsonHelper.getBoolean(jsonObject, "applicable_to_potion_fillables", true);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		int baseDurationTicks = JsonHelper.getInt(jsonObject, "base_duration_ticks", 1600);
		int color = JsonHelper.getInt(jsonObject, "potion_color", -1);
		float potencyModifier = JsonHelper.getFloat(jsonObject, "potency_modifier", 1.0F);
		
		Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "effect"));
		if (!Registry.STATUS_EFFECT.containsId(statusEffectIdentifier)) {
			throw new JsonParseException("Potion Workshop Brewing Recipe " + identifier + " has a status effect set that does not exist or is disabled: " + statusEffectIdentifier); // otherwise, recipe sync would break multiplayer joining with the non-existing status effect
		}
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingTime, ingredient1, ingredient2, ingredient3, statusEffect, baseDurationTicks, potencyModifier, color, applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes);
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
		packetByteBuf.writeIdentifier(Registry.STATUS_EFFECT.getId(recipe.statusEffect));
		packetByteBuf.writeInt(recipe.baseDurationTicks);
		packetByteBuf.writeFloat(recipe.potencyModifier);
		packetByteBuf.writeInt(recipe.color);
		packetByteBuf.writeBoolean(recipe.applicableToPotions);
		packetByteBuf.writeBoolean(recipe.applicableToTippedArrows);
		packetByteBuf.writeBoolean(recipe.applicableToPotionFillabes);
	}
	
	@Override
	public PotionWorkshopBrewingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		int craftingTime = packetByteBuf.readInt();
		Ingredient ingredient1 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(packetByteBuf.readIdentifier());
		int baseDurationTicks = packetByteBuf.readInt();
		float potencyModifier = packetByteBuf.readFloat();
		int color = packetByteBuf.readInt();
		boolean applicableToPotions = packetByteBuf.readBoolean();
		boolean applicableToTippedArrows = packetByteBuf.readBoolean();
		boolean applicableToPotionFillabes = packetByteBuf.readBoolean();
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingTime, ingredient1, ingredient2, ingredient3, statusEffect, baseDurationTicks, potencyModifier, color, applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes);
	}
	
}
