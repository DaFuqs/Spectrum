package de.dafuqs.spectrum.recipe.fusion_shrine;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class FusionShrineRecipeSerializer implements GatedRecipeSerializer<FusionShrineRecipe> {
	
	public final FusionShrineRecipeSerializer.RecipeFactory<FusionShrineRecipe> recipeFactory;
	
	public FusionShrineRecipeSerializer(FusionShrineRecipeSerializer.RecipeFactory<FusionShrineRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	public interface RecipeFactory<FusionShrineRecipe> {
		FusionShrineRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
								  List<IngredientStack> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, boolean noBenefitsFromYieldUpgrades, boolean playCraftingFinishedEffects, boolean copyNbt,
								  List<WorldConditionPredicate> worldConditions, FusionShrineRecipeWorldEffect startWorldEffect, List<FusionShrineRecipeWorldEffect> duringWorldEffects, FusionShrineRecipeWorldEffect finishWorldEffect, Text description);
	}
	
	@Override
	public FusionShrineRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);

		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		List<IngredientStack> craftingInputs = RecipeParser.ingredientStacksFromJson(ingredientArray, ingredientArray.size());
		if (craftingInputs.size() > 7) {
			throw new JsonParseException("Recipe cannot have more than 7 ingredients. Has " + craftingInputs.size());
		}

		Fluid fluid = Fluids.EMPTY;
		if (JsonHelper.hasString(jsonObject, "fluid")) {
			Identifier fluidIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "fluid"));
			fluid = Registry.FLUID.get(fluidIdentifier);
			if (fluid.getDefaultState().isEmpty()) {
				throw new JsonParseException("Recipe specifies fluid " + fluidIdentifier + " that does not exist! This recipe will not be craftable.");
			}
		}
		
		ItemStack output;
		if (JsonHelper.hasJsonObject(jsonObject, "result")) {
			output = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		} else {
			output = ItemStack.EMPTY;
		}
		float experience = JsonHelper.getFloat(jsonObject, "experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		boolean noBenefitsFromYieldUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		boolean playCraftingFinishedEffects = JsonHelper.getBoolean(jsonObject, "play_crafting_finished_effects", true);
		
		List<WorldConditionPredicate> worldConditions = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, "world_conditions")) {
			for (JsonElement element : JsonHelper.getArray(jsonObject, "world_conditions")) {
				worldConditions.add(WorldConditionPredicate.fromJson(element));
			}
		}
		
		FusionShrineRecipeWorldEffect startWorldEffect;
		if (JsonHelper.hasString(jsonObject, "start_crafting_effect")) {
			startWorldEffect = FusionShrineRecipeWorldEffect.valueOf(JsonHelper.getString(jsonObject, "start_crafting_effect").toUpperCase(Locale.ROOT));
		} else {
			startWorldEffect = FusionShrineRecipeWorldEffect.NOTHING;
		}
		List<FusionShrineRecipeWorldEffect> duringWorldEffects = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, "during_crafting_effects")) {
			JsonArray worldEffectsArray = JsonHelper.getArray(jsonObject, "during_crafting_effects");
			for (int i = 0; i < worldEffectsArray.size(); i++) {
				String effectString = worldEffectsArray.get(i).getAsString().toUpperCase(Locale.ROOT);
				duringWorldEffects.add(FusionShrineRecipeWorldEffect.valueOf(effectString));
			}
		}
		FusionShrineRecipeWorldEffect finishWorldEffect;
		if (JsonHelper.hasString(jsonObject, "finish_crafting_effect")) {
			finishWorldEffect = FusionShrineRecipeWorldEffect.valueOf(JsonHelper.getString(jsonObject, "finish_crafting_effect").toUpperCase(Locale.ROOT));
		} else {
			finishWorldEffect = FusionShrineRecipeWorldEffect.NOTHING;
		}
		Text description;
		if (JsonHelper.hasString(jsonObject, "description")) {
			description = Text.translatable(JsonHelper.getString(jsonObject, "description"));
		} else {
			description = null;
		}
		boolean copyNbt = JsonHelper.getBoolean(jsonObject, "copy_nbt", false);
		if (copyNbt && output.isEmpty()) {
			throw new JsonParseException("Recipe does have copy_nbt set, but has no output!");
		}

		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier,
				craftingInputs, fluid, output, experience, craftingTime, noBenefitsFromYieldUpgrades, playCraftingFinishedEffects, copyNbt,
				worldConditions, startWorldEffect, duringWorldEffects, finishWorldEffect, description);
	}
	
	
	@Override
	public void write(PacketByteBuf packetByteBuf, FusionShrineRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		packetByteBuf.writeShort(recipe.craftingInputs.size());
		for (IngredientStack ingredientStack : recipe.craftingInputs) {
			ingredientStack.write(packetByteBuf);
		}
		
		packetByteBuf.writeIdentifier(Registry.FLUID.getId(recipe.fluidInput));
		packetByteBuf.writeItemStack(recipe.output);
		packetByteBuf.writeFloat(recipe.experience);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeBoolean(recipe.noBenefitsFromYieldUpgrades);
		packetByteBuf.writeBoolean(recipe.playCraftingFinishedEffects);
		
		packetByteBuf.writeInt(recipe.startWorldEffect.ordinal());
		packetByteBuf.writeInt(recipe.duringWorldEffects.size());
		for (FusionShrineRecipeWorldEffect effect : recipe.duringWorldEffects) {
			packetByteBuf.writeInt(effect.ordinal());
		}
		packetByteBuf.writeInt(recipe.finishWorldEffect.ordinal());
		if (recipe.getDescription().isEmpty()) {
			packetByteBuf.writeText(Text.literal(""));
		} else {
			packetByteBuf.writeText(recipe.getDescription().get());
		}
		packetByteBuf.writeBoolean(recipe.copyNbt);
	}
	
	
	@Override
	public FusionShrineRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		short craftingInputCount = packetByteBuf.readShort();
		List<IngredientStack> ingredients = IngredientStack.decodeByteBuf(packetByteBuf, craftingInputCount);
		
		Fluid fluid = Registry.FLUID.get(packetByteBuf.readIdentifier());
		ItemStack output = packetByteBuf.readItemStack();
		float experience = packetByteBuf.readFloat();
		int craftingTime = packetByteBuf.readInt();
		boolean noBenefitsFromYieldUpgrades = packetByteBuf.readBoolean();
		boolean playCraftingFinishedEffects = packetByteBuf.readBoolean();
		
		FusionShrineRecipeWorldEffect startWorldEffect = FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()];
		int duringWorldEventCount = packetByteBuf.readInt();
		List<FusionShrineRecipeWorldEffect> duringWorldEffects = new ArrayList<>();
		for (int i = 0; i < duringWorldEventCount; i++) {
			duringWorldEffects.add(FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()]);
		}
		FusionShrineRecipeWorldEffect finishWorldEffect = FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()];

		Text description = packetByteBuf.readText();
		boolean copyNbt = packetByteBuf.readBoolean();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier,
				ingredients, fluid, output, experience, craftingTime, noBenefitsFromYieldUpgrades, playCraftingFinishedEffects, copyNbt,
				List.of(), startWorldEffect, duringWorldEffects, finishWorldEffect, description);
	}
	
}
