package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

public class PotionWorkshopBrewingRecipeSerializer<T extends PotionWorkshopBrewingRecipe> implements RecipeSerializer<T> {

	public final PotionWorkshopBrewingRecipeSerializer.RecipeFactory<T> recipeFactory;

	public PotionWorkshopBrewingRecipeSerializer(PotionWorkshopBrewingRecipeSerializer.RecipeFactory<T> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public T read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
		Ingredient baseIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base_ingredient"));
		Ingredient ingredient1 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient1"));
		Ingredient ingredient2;
		if(JsonHelper.hasJsonObject(jsonObject, "ingredient2")) {
			ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient2"));
		} else {
			ingredient2 = Ingredient.EMPTY;
		}
		Ingredient ingredient3;
		if(JsonHelper.hasJsonObject(jsonObject, "ingredient3")) {
			ingredient3 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient3"));
		} else {
			ingredient3 = Ingredient.EMPTY;
		}
		
		boolean consumeBaseIngredient = JsonHelper.getBoolean(jsonObject, "use_up_base_ingredient", false);
		boolean applicableToPotions = JsonHelper.getBoolean(jsonObject, "applicable_to_potions", true);
		boolean applicableToTippedArrows = JsonHelper.getBoolean(jsonObject, "applicable_to_tipped_arrows", true);
		int baseDurationTicks = JsonHelper.getInt(jsonObject, "base_duration_ticks", 200);
		float potencyModifier = JsonHelper.getFloat(jsonObject, "potency_modifier", 1.0F);
		
		Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "effect"));
		if(!Registry.STATUS_EFFECT.containsId(statusEffectIdentifier)) {
			throw new JsonParseException("Potion Workshop Brewing Recipe " + identifier + " has a status effect set that does not exist or is disabled: " + statusEffectIdentifier); // otherwise, recipe sync would break multiplayer joining with the non-existing status effect
		}
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
		
		Identifier requiredAdvancementIdentifier;
		if(JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			SpectrumCommon.log(Level.WARN, "Potion Workshop Brewing Recipe " + identifier + " has no unlock advancement set. Will be set to the unlock pos of the Potion Workshop itself");
			requiredAdvancementIdentifier = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_potion_workshop");
		}

		return this.recipeFactory.create(identifier, group, baseIngredient, consumeBaseIngredient, ingredient1, ingredient2, ingredient3, statusEffect, baseDurationTicks, potencyModifier, applicableToPotions, applicableToTippedArrows, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, T potionWorkshopBrewingRecipe) {
		packetByteBuf.writeString(potionWorkshopBrewingRecipe.group);
		potionWorkshopBrewingRecipe.baseIngredient.write(packetByteBuf);
		packetByteBuf.writeBoolean(potionWorkshopBrewingRecipe.consumeBaseIngredient);
		potionWorkshopBrewingRecipe.ingredient1.write(packetByteBuf);
		potionWorkshopBrewingRecipe.ingredient2.write(packetByteBuf);
		potionWorkshopBrewingRecipe.ingredient3.write(packetByteBuf);
		packetByteBuf.writeIdentifier(Registry.STATUS_EFFECT.getId(potionWorkshopBrewingRecipe.statusEffect));
		packetByteBuf.writeInt(potionWorkshopBrewingRecipe.baseDurationTicks);
		packetByteBuf.writeFloat(potionWorkshopBrewingRecipe.potencyModifier);
		packetByteBuf.writeBoolean(potionWorkshopBrewingRecipe.applicableToPotions);
		packetByteBuf.writeBoolean(potionWorkshopBrewingRecipe.applicableToTippedArrows);
		packetByteBuf.writeIdentifier(potionWorkshopBrewingRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		Ingredient baseIngredient = Ingredient.fromPacket(packetByteBuf);
		boolean consumeBaseIngredient = packetByteBuf.readBoolean();
		Ingredient ingredient1 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
		Ingredient ingredient3 = Ingredient.fromPacket(packetByteBuf);
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(packetByteBuf.readIdentifier());
		int baseDurationTicks = packetByteBuf.readInt();
		float potencyModifier = packetByteBuf.readFloat();
		boolean applicableToPotions = packetByteBuf.readBoolean();
		boolean applicableToTippedArrows = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		return this.recipeFactory.create(identifier, group, baseIngredient, consumeBaseIngredient, ingredient1, ingredient2, ingredient3, statusEffect, baseDurationTicks, potencyModifier, applicableToPotions, applicableToTippedArrows, requiredAdvancementIdentifier);
	}

	
	public interface RecipeFactory<T extends PotionWorkshopBrewingRecipe> {
		T create(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, boolean applicableToPotions, boolean applicableToTippedArrows, Identifier requiredAdvancementIdentifier);
	}

}
