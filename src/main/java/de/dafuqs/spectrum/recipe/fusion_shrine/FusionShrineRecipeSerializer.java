package de.dafuqs.spectrum.recipe.fusion_shrine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlock;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FusionShrineRecipeSerializer implements RecipeSerializer<FusionShrineRecipe> {

	public final FusionShrineRecipeSerializer.RecipeFactory<FusionShrineRecipe> recipeFactory;

	public FusionShrineRecipeSerializer(FusionShrineRecipeSerializer.RecipeFactory<FusionShrineRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public FusionShrineRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");

		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		DefaultedList<Ingredient> craftingInputs = DefaultedList.ofSize(ingredientArray.size());
		for(int i = 0; i < ingredientArray.size(); i++) {
			craftingInputs.add(Ingredient.fromJson(ingredientArray.get(i)));
		}

		Fluid fluid = Fluids.EMPTY;
		if(JsonHelper.hasString(jsonObject, "fluid")) {
			Identifier fluidIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "fluid"));
			fluid = Registry.FLUID.get(fluidIdentifier);
			if(fluid.getDefaultState().isEmpty()) {
				SpectrumCommon.log(Level.ERROR, "Fusion Shrine Recipe " + identifier + " specifies fluid " + fluidIdentifier + " that does not exist! This recipe will not be craftable.");
			}
		}

		ItemStack output;
		if(JsonHelper.hasJsonObject(jsonObject, "result")) {
			output = RecipeUtils.outputWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		} else {
			output = ItemStack.EMPTY;
		}
		float experience = JsonHelper.getFloat(jsonObject, "experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		
		boolean noBenefitsFromYieldUpgrades = false;
		if(JsonHelper.hasPrimitive(jsonObject, "disable_yield_upgrades")) {
			noBenefitsFromYieldUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_upgrades", false);
		}
		
		Identifier requiredAdvancementIdentifier;
		if(JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// No unlock advancement set. Will be set to the unlock advancement of the block itself
			requiredAdvancementIdentifier = FusionShrineBlock.UNLOCK_IDENTIFIER;
		}
		
		List<FusionShrineRecipeWorldCondition> worldConditions = new ArrayList<>();
		if(JsonHelper.hasArray(jsonObject, "world_conditions")) {
			JsonArray conditionsArray = JsonHelper.getArray(jsonObject, "world_conditions");
			for(int i = 0; i < conditionsArray.size(); i++) {
				String conditionString = conditionsArray.get(i).getAsString().toUpperCase(Locale.ROOT);
				worldConditions.add(FusionShrineRecipeWorldCondition.valueOf(conditionString));
			}
		}

		FusionShrineRecipeWorldEffect startWorldEffect;
		if(JsonHelper.hasString(jsonObject, "start_crafting_effect")) {
			startWorldEffect = FusionShrineRecipeWorldEffect.valueOf(JsonHelper.getString(jsonObject, "start_crafting_effect").toUpperCase(Locale.ROOT));
		} else {
			startWorldEffect = FusionShrineRecipeWorldEffect.NOTHING;
		}
		List<FusionShrineRecipeWorldEffect> duringWorldEffects = new ArrayList<>();
		if(JsonHelper.hasArray(jsonObject, "during_crafting_effects")) {
			JsonArray worldEffectsArray = JsonHelper.getArray(jsonObject, "during_crafting_effects");
			for(int i = 0; i < worldEffectsArray.size(); i++) {
				String effectString = worldEffectsArray.get(i).getAsString().toUpperCase(Locale.ROOT);
				duringWorldEffects.add(FusionShrineRecipeWorldEffect.valueOf(effectString));
			}
		}
		FusionShrineRecipeWorldEffect finishWorldEffect;
		if(JsonHelper.hasString(jsonObject, "finish_crafting_effect")) {
			finishWorldEffect = FusionShrineRecipeWorldEffect.valueOf(JsonHelper.getString(jsonObject, "finish_crafting_effect").toUpperCase(Locale.ROOT));
		} else {
			finishWorldEffect = FusionShrineRecipeWorldEffect.NOTHING;
		}
		TranslatableText description;
		if(JsonHelper.hasString(jsonObject, "description")) {
			description = new TranslatableText(JsonHelper.getString(jsonObject, "description"));
		} else {
			description = null;
		}

		return this.recipeFactory.create(identifier, group, craftingInputs, fluid, output, experience, craftingTime, noBenefitsFromYieldUpgrades, requiredAdvancementIdentifier, worldConditions, startWorldEffect, duringWorldEffects, finishWorldEffect, description);
	}

	@Override
	public void write(PacketByteBuf packetByteBuf, FusionShrineRecipe fusionShrineRecipe) {
		packetByteBuf.writeString(fusionShrineRecipe.group);

		packetByteBuf.writeShort(fusionShrineRecipe.craftingInputs.size());
		for(Ingredient ingredient : fusionShrineRecipe.craftingInputs) {
			ingredient.write(packetByteBuf);
		}

		packetByteBuf.writeIdentifier(Registry.FLUID.getId(fusionShrineRecipe.fluidInput));
		packetByteBuf.writeItemStack(fusionShrineRecipe.output);
		packetByteBuf.writeFloat(fusionShrineRecipe.experience);
		packetByteBuf.writeInt(fusionShrineRecipe.craftingTime);
		packetByteBuf.writeBoolean(fusionShrineRecipe.noBenefitsFromYieldUpgrades);
		packetByteBuf.writeIdentifier(fusionShrineRecipe.requiredAdvancementIdentifier);

		packetByteBuf.writeShort(fusionShrineRecipe.worldConditions.size());
		for(FusionShrineRecipeWorldCondition worldCondition : fusionShrineRecipe.worldConditions) {
			packetByteBuf.writeInt(worldCondition.ordinal());
		}

		packetByteBuf.writeInt(fusionShrineRecipe.startWorldEffect.ordinal());
		packetByteBuf.writeInt(fusionShrineRecipe.duringWorldEffects.size());
		for(FusionShrineRecipeWorldEffect effect : fusionShrineRecipe.duringWorldEffects) {
			packetByteBuf.writeInt(effect.ordinal());
		}
		packetByteBuf.writeInt(fusionShrineRecipe.finishWorldEffect.ordinal());
		if(fusionShrineRecipe.getDescription().isEmpty()) {
			packetByteBuf.writeText(new LiteralText(""));
		} else {
			packetByteBuf.writeText(fusionShrineRecipe.getDescription().get());
		}
	}
	
	
	@Override
	public FusionShrineRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		short craftingInputCount = packetByteBuf.readShort();
		DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(craftingInputCount, Ingredient.EMPTY);
		for(short i = 0; i < craftingInputCount; i++) {
			ingredients.set(i, Ingredient.fromPacket(packetByteBuf));
		}
		Fluid fluid = Registry.FLUID.get(packetByteBuf.readIdentifier());
		ItemStack output = packetByteBuf.readItemStack();
		float experience = packetByteBuf.readFloat();
		int craftingTime = packetByteBuf.readInt();
		boolean noBenefitsFromYieldUpgrades = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		short worldConditionCount = packetByteBuf.readShort();
		List<FusionShrineRecipeWorldCondition> worldConditions = new ArrayList<>();
		for(short i = 0; i < worldConditionCount; i++) {
			worldConditions.add(FusionShrineRecipeWorldCondition.values()[packetByteBuf.readInt()]);
		}
		
		FusionShrineRecipeWorldEffect startWorldEffect = FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()];
		int duringWorldEventCount = packetByteBuf.readInt();
		List<FusionShrineRecipeWorldEffect> duringWorldEffects = new ArrayList<>();
		for(int i = 0; i < duringWorldEventCount; i++) {
			duringWorldEffects.add(FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()]);
		}
		FusionShrineRecipeWorldEffect finishWorldEffect = FusionShrineRecipeWorldEffect.values()[packetByteBuf.readInt()];
		
		Text description = packetByteBuf.readText();
		
		return this.recipeFactory.create(identifier, group, ingredients, fluid, output, experience, craftingTime, noBenefitsFromYieldUpgrades, requiredAdvancementIdentifier, worldConditions, startWorldEffect, duringWorldEffects, finishWorldEffect, description);
	}
	
	public interface RecipeFactory<FusionShrineRecipe> {
		FusionShrineRecipe create(Identifier id, String group, DefaultedList<Ingredient> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, boolean noBenefitsFromYieldUpgrades, Identifier requiredAdvancementIdentifier,
				 List<FusionShrineRecipeWorldCondition> worldConditions, FusionShrineRecipeWorldEffect startWorldEffect, List<FusionShrineRecipeWorldEffect> duringWorldEffects, FusionShrineRecipeWorldEffect finishWorldEffect, Text description);
	}

}
