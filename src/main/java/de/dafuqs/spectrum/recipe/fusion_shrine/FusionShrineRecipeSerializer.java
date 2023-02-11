package de.dafuqs.spectrum.recipe.fusion_shrine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.id.incubus_core.json.RecipeParser;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FusionShrineRecipeSerializer implements GatedRecipeSerializer<FusionShrineRecipe> {
	
	public final FusionShrineRecipeSerializer.RecipeFactory<FusionShrineRecipe> recipeFactory;
	
	public FusionShrineRecipeSerializer(FusionShrineRecipeSerializer.RecipeFactory<FusionShrineRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<FusionShrineRecipe> {
		FusionShrineRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, boolean noBenefitsFromYieldUpgrades,
		                          List<FusionShrineRecipeWorldCondition> worldConditions, FusionShrineRecipeWorldEffect startWorldEffect, List<FusionShrineRecipeWorldEffect> duringWorldEffects, FusionShrineRecipeWorldEffect finishWorldEffect, Text description);
	}
	
	@Override
	public FusionShrineRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		List<IngredientStack> craftingInputs = RecipeParser.ingredientStacksFromJson(ingredientArray, ingredientArray.size());
		
		Fluid fluid = Fluids.EMPTY;
		if (JsonHelper.hasString(jsonObject, "fluid")) {
			Identifier fluidIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "fluid"));
			fluid = Registry.FLUID.get(fluidIdentifier);
			if (fluid.getDefaultState().isEmpty()) {
				SpectrumCommon.logError("Fusion Shrine Recipe " + identifier + " specifies fluid " + fluidIdentifier + " that does not exist! This recipe will not be craftable.");
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
		
		boolean noBenefitsFromYieldUpgrades = false;
		if (JsonHelper.hasPrimitive(jsonObject, "disable_yield_upgrades")) {
			noBenefitsFromYieldUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		}
		
		List<FusionShrineRecipeWorldCondition> worldConditions = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, "world_conditions")) {
			JsonArray conditionsArray = JsonHelper.getArray(jsonObject, "world_conditions");
			for (int i = 0; i < conditionsArray.size(); i++) {
				String conditionString = conditionsArray.get(i).getAsString().toUpperCase(Locale.ROOT);
				worldConditions.add(FusionShrineRecipeWorldCondition.valueOf(conditionString));
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
		TranslatableText description;
		if (JsonHelper.hasString(jsonObject, "description")) {
			description = new TranslatableText(JsonHelper.getString(jsonObject, "description"));
		} else {
			description = null;
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, craftingInputs, fluid, output, experience, craftingTime, noBenefitsFromYieldUpgrades, worldConditions, startWorldEffect, duringWorldEffects, finishWorldEffect, description);
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
		
		packetByteBuf.writeShort(recipe.worldConditions.size());
		for (FusionShrineRecipeWorldCondition worldCondition : recipe.worldConditions) {
			packetByteBuf.writeInt(worldCondition.ordinal());
		}
		
		packetByteBuf.writeInt(recipe.startWorldEffect.ordinal());
		packetByteBuf.writeInt(recipe.duringWorldEffects.size());
		for (FusionShrineRecipeWorldEffect effect : recipe.duringWorldEffects) {
			packetByteBuf.writeInt(effect.ordinal());
		}
		packetByteBuf.writeInt(recipe.finishWorldEffect.ordinal());
		if (recipe.getDescription().isEmpty()) {
			packetByteBuf.writeText(new LiteralText(""));
		} else {
			packetByteBuf.writeText(recipe.getDescription().get());
		}
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
		
		short worldConditionCount = packetByteBuf.readShort();
		List<FusionShrineRecipeWorldCondition> worldConditions = new ArrayList<>();
		for (short i = 0; i < worldConditionCount; i++) {
			worldConditions.add(FusionShrineRecipeWorldCondition.values()[packetByteBuf.readInt()]);
		}
		
		FusionShrineRecipeWorldEffect startWorldEffect = FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()];
		int duringWorldEventCount = packetByteBuf.readInt();
		List<FusionShrineRecipeWorldEffect> duringWorldEffects = new ArrayList<>();
		for (int i = 0; i < duringWorldEventCount; i++) {
			duringWorldEffects.add(FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()]);
		}
		FusionShrineRecipeWorldEffect finishWorldEffect = FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()];
		
		Text description = packetByteBuf.readText();
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, ingredients, fluid, output, experience, craftingTime, noBenefitsFromYieldUpgrades, worldConditions, startWorldEffect, duringWorldEffects, finishWorldEffect, description);
	}
	
}
