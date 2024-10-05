package de.dafuqs.spectrum.recipe.fusion_shrine;

import com.google.gson.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.predicate.world.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class FusionShrineRecipeSerializer implements GatedRecipeSerializer<FusionShrineRecipe> {
	
	public final FusionShrineRecipeSerializer.RecipeFactory recipeFactory;
	
	public FusionShrineRecipeSerializer(FusionShrineRecipeSerializer.RecipeFactory recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	public interface RecipeFactory {
		FusionShrineRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
								  List<IngredientStack> craftingInputs, FluidIngredient fluid, ItemStack output, float experience, int craftingTime, boolean noBenefitsFromYieldUpgrades, boolean playCraftingFinishedEffects, boolean copyNbt,
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
		
		FluidIngredient fluid = FluidIngredient.EMPTY;
		if (JsonHelper.hasJsonObject(jsonObject, "fluid")) {
			JsonObject fluidObject = JsonHelper.getObject(jsonObject, "fluid");
			FluidIngredient.JsonParseResult result = FluidIngredient.fromJson(fluidObject);
			fluid = result.result();
			if (result.malformed()) {
				// Currently handling malformed input leniently. May throw an error in the future.
				SpectrumCommon.logError("Fusion Recipe " + identifier + "contains a malformed fluid input tag! This recipe will not be craftable.");
			} else if (result.result() == FluidIngredient.EMPTY && !result.isTag()) { // tags get populated after recipes are
				SpectrumCommon.logError("Fusion Recipe " + identifier + " specifies fluid " + result.id() + " that does not exist! This recipe will not be craftable.");
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
		boolean yieldUpgradesDisabled = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		boolean playCraftingFinishedEffects = JsonHelper.getBoolean(jsonObject, "play_crafting_finished_effects", true);
		
		List<WorldConditionPredicate> worldConditions = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, "world_conditions")) {
			for (JsonElement element : JsonHelper.getArray(jsonObject, "world_conditions")) {
				worldConditions.add(WorldConditionPredicate.fromJson(element));
			}
		}
		
		FusionShrineRecipeWorldEffect startWorldEffect = FusionShrineRecipeWorldEffect.fromString(JsonHelper.getString(jsonObject, "start_crafting_effect", null));
		List<FusionShrineRecipeWorldEffect> duringWorldEffects = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, "during_crafting_effects")) {
			JsonArray worldEffectsArray = JsonHelper.getArray(jsonObject, "during_crafting_effects");
			for (int i = 0; i < worldEffectsArray.size(); i++) {
				duringWorldEffects.add(FusionShrineRecipeWorldEffect.fromString(worldEffectsArray.get(i).getAsString()));
			}
		}
		FusionShrineRecipeWorldEffect finishWorldEffect = FusionShrineRecipeWorldEffect.fromString(JsonHelper.getString(jsonObject, "finish_crafting_effect", null));
		
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
				craftingInputs, fluid, output, experience, craftingTime, yieldUpgradesDisabled, playCraftingFinishedEffects, copyNbt,
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
		
		writeFluidIngredient(packetByteBuf, recipe.fluid);
		packetByteBuf.writeItemStack(recipe.output);
		packetByteBuf.writeFloat(recipe.experience);
		packetByteBuf.writeInt(recipe.craftingTime);
		packetByteBuf.writeBoolean(recipe.yieldUpgradesDisabled);
		packetByteBuf.writeBoolean(recipe.playCraftingFinishedEffects);
		
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
		
		FluidIngredient fluid = readFluidIngredient(packetByteBuf);
		ItemStack output = packetByteBuf.readItemStack();
		float experience = packetByteBuf.readFloat();
		int craftingTime = packetByteBuf.readInt();
		boolean yieldUpgradesDisabled = packetByteBuf.readBoolean();
		boolean playCraftingFinishedEffects = packetByteBuf.readBoolean();

		Text description = packetByteBuf.readText();
		boolean copyNbt = packetByteBuf.readBoolean();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier,
				ingredients, fluid, output, experience, craftingTime, yieldUpgradesDisabled, playCraftingFinishedEffects, copyNbt,
				List.of(), FusionShrineRecipeWorldEffect.NOTHING, List.of(), FusionShrineRecipeWorldEffect.NOTHING, description);
	}
	
}
