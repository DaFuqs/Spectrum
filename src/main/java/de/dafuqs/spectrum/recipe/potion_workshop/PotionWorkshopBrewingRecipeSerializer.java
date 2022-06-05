package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class PotionWorkshopBrewingRecipeSerializer implements RecipeSerializer<PotionWorkshopBrewingRecipe> {
	
	public final PotionWorkshopBrewingRecipeSerializer.RecipeFactory<PotionWorkshopBrewingRecipe> recipeFactory;
	
	public PotionWorkshopBrewingRecipeSerializer(PotionWorkshopBrewingRecipeSerializer.RecipeFactory<PotionWorkshopBrewingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public PotionWorkshopBrewingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
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
		
		Identifier requiredAdvancementIdentifier;
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// Potion Workshop Recipe has no unlock advancement set. Will be set to the unlock advancement of the Potion Workshop itself
			requiredAdvancementIdentifier = PotionWorkshopBlock.UNLOCK_IDENTIFIER;
		}
		
		return this.recipeFactory.create(identifier, group, craftingTime, ingredient1, ingredient2, ingredient3, statusEffect, baseDurationTicks, potencyModifier, color, applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, PotionWorkshopBrewingRecipe potionWorkshopBrewingRecipe) {
		packetByteBuf.writeString(potionWorkshopBrewingRecipe.group);
		packetByteBuf.writeInt(potionWorkshopBrewingRecipe.craftingTime);
		potionWorkshopBrewingRecipe.ingredient1.write(packetByteBuf);
		potionWorkshopBrewingRecipe.ingredient2.write(packetByteBuf);
		potionWorkshopBrewingRecipe.ingredient3.write(packetByteBuf);
		packetByteBuf.writeIdentifier(Registry.STATUS_EFFECT.getId(potionWorkshopBrewingRecipe.statusEffect));
		packetByteBuf.writeInt(potionWorkshopBrewingRecipe.baseDurationTicks);
		packetByteBuf.writeFloat(potionWorkshopBrewingRecipe.potencyModifier);
		packetByteBuf.writeInt(potionWorkshopBrewingRecipe.color);
		packetByteBuf.writeBoolean(potionWorkshopBrewingRecipe.applicableToPotions);
		packetByteBuf.writeBoolean(potionWorkshopBrewingRecipe.applicableToTippedArrows);
		packetByteBuf.writeBoolean(potionWorkshopBrewingRecipe.applicableToPotionFillabes);
		packetByteBuf.writeIdentifier(potionWorkshopBrewingRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public PotionWorkshopBrewingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
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
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		return this.recipeFactory.create(identifier, group, craftingTime, ingredient1, ingredient2, ingredient3, statusEffect, baseDurationTicks, potencyModifier, color, applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes, requiredAdvancementIdentifier);
	}
	
	
	public interface RecipeFactory<PotionWorkshopBrewingRecipe> {
		PotionWorkshopBrewingRecipe create(Identifier id, String group, int craftingTime, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, int color, boolean applicableToPotions, boolean applicableToTippedArrows, boolean applicableToPotionFillabes, Identifier requiredAdvancementIdentifier);
	}
	
}
