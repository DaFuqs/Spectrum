package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.entity.effect.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

public class PotionWorkshopBrewingRecipeSerializer implements GatedRecipeSerializer<PotionWorkshopBrewingRecipe> {
	
	public final PotionWorkshopBrewingRecipeSerializer.RecipeFactory<PotionWorkshopBrewingRecipe> recipeFactory;
	
	public PotionWorkshopBrewingRecipeSerializer(PotionWorkshopBrewingRecipeSerializer.RecipeFactory<PotionWorkshopBrewingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<PotionWorkshopBrewingRecipe> {
		PotionWorkshopBrewingRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, int craftingTime, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3,
										   StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, boolean applicableToPotions, boolean applicableToTippedArrows, boolean applicableToPotionFillabes, InkColor inkColor, int inkCost);
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
		float potencyModifier = JsonHelper.getFloat(jsonObject, "potency_modifier", 1.0F);
		
		Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "effect"));
		if (!Registries.STATUS_EFFECT.containsId(statusEffectIdentifier)) {
			throw new JsonParseException("Potion Workshop Brewing Recipe " + identifier + " has a status effect set that does not exist or is disabled: " + statusEffectIdentifier); // otherwise, recipe sync would break multiplayer joining with the non-existing status effect
		}
		StatusEffect statusEffect = Registries.STATUS_EFFECT.get(statusEffectIdentifier);
		
		InkColor inkColor = InkColor.of(JsonHelper.getString(jsonObject, "ink_color"));
		int inkCost = JsonHelper.getInt(jsonObject, "ink_cost");
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingTime, ingredient1, ingredient2, ingredient3,
				statusEffect, baseDurationTicks, potencyModifier, applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes, inkColor, inkCost);
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
		packetByteBuf.writeIdentifier(Registries.STATUS_EFFECT.getId(recipe.statusEffect));
		packetByteBuf.writeInt(recipe.baseDurationTicks);
		packetByteBuf.writeFloat(recipe.potencyModifier);
		packetByteBuf.writeBoolean(recipe.applicableToPotions);
		packetByteBuf.writeBoolean(recipe.applicableToTippedArrows);
		packetByteBuf.writeBoolean(recipe.applicableToPotionFillabes);
		
		packetByteBuf.writeString(recipe.inkColor.toString());
		packetByteBuf.writeInt(recipe.inkAmount);
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
		StatusEffect statusEffect = Registries.STATUS_EFFECT.get(packetByteBuf.readIdentifier());
		int baseDurationTicks = packetByteBuf.readInt();
		float potencyModifier = packetByteBuf.readFloat();
		boolean applicableToPotions = packetByteBuf.readBoolean();
		boolean applicableToTippedArrows = packetByteBuf.readBoolean();
		boolean applicableToPotionFillabes = packetByteBuf.readBoolean();
		
		InkColor inkColor = InkColor.of(packetByteBuf.readString());
		int inkCost = packetByteBuf.readInt();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingTime, ingredient1, ingredient2, ingredient3,
				statusEffect, baseDurationTicks, potencyModifier, applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes, inkColor, inkCost);
	}
	
}
