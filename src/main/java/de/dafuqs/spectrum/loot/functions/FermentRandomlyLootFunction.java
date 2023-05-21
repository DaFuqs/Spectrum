package de.dafuqs.spectrum.loot.functions;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import net.minecraft.item.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.number.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FermentRandomlyLootFunction extends ConditionalLootFunction {
	
	final @Nullable Identifier fermentationRecipeIdentifier;
	final @Nullable FermentationData fermentationData;
	final LootNumberProvider daysFermented;
	final LootNumberProvider thickness;
	final LootNumberProvider downfall;
	
	public FermentRandomlyLootFunction(LootCondition[] conditions, @NotNull Identifier fermentationRecipeIdentifier, LootNumberProvider daysFermented, LootNumberProvider thickness, LootNumberProvider downfall) {
		super(conditions);
		this.fermentationRecipeIdentifier = fermentationRecipeIdentifier;
		this.fermentationData = null;
		this.daysFermented = daysFermented;
		this.thickness = thickness;
		this.downfall = downfall;
	}
	
	public FermentRandomlyLootFunction(LootCondition[] conditions, @NotNull FermentationData fermentationData, LootNumberProvider daysFermented, LootNumberProvider thickness, LootNumberProvider downfall) {
		super(conditions);
		this.fermentationRecipeIdentifier = null;
		this.fermentationData = fermentationData;
		this.daysFermented = daysFermented;
		this.thickness = thickness;
		this.downfall = downfall;
	}
	
	@Override
	public LootFunctionType getType() {
		return SpectrumLootFunctionTypes.FERMENT_RANDOMLY;
	}
	
	@Override
	public ItemStack process(ItemStack stack, LootContext context) {
		FermentationData fermentationData = null;
		if (this.fermentationRecipeIdentifier != null) {
			Optional<? extends Recipe<?>> recipe = SpectrumCommon.minecraftServer.getRecipeManager().get(this.fermentationRecipeIdentifier);
			if (recipe.isPresent() && recipe.get() instanceof TitrationBarrelRecipe titrationBarrelRecipe) {
				fermentationData = titrationBarrelRecipe.getFermentationData();
			} else {
				SpectrumCommon.logError("A 'ferment_randomly' loot function has set an invalid 'fermentation_recipe_id': " + this.fermentationRecipeIdentifier + " It has to match an existing Titration Barrel recipe. ");
			}
		}
		if (fermentationData == null) {
			fermentationData = this.fermentationData;
		}
		if (fermentationData != null) {
			TitrationBarrelRecipe.getFermentedStack(fermentationData, this.thickness.nextInt(context), TimeHelper.secondsFromMinecraftDays(this.daysFermented.nextInt(context)), this.downfall.nextInt(context), stack);
		}
		return stack;
	}
	
	public static ConditionalLootFunction.Builder<?> builder(FermentationData fermentationData, LootNumberProvider daysFermented, LootNumberProvider thickness, LootNumberProvider downfall) {
		return builder((conditions) -> new FermentRandomlyLootFunction(conditions, fermentationData, daysFermented, thickness, downfall));
	}
	
	public static ConditionalLootFunction.Builder<?> builder(Identifier fermentationRecipeIdentifier, LootNumberProvider daysFermented, LootNumberProvider thickness, LootNumberProvider downfall) {
		return builder((conditions) -> new FermentRandomlyLootFunction(conditions, fermentationRecipeIdentifier, daysFermented, thickness, downfall));
	}
	
	public static class Serializer extends ConditionalLootFunction.Serializer<FermentRandomlyLootFunction> {
		
		@Override
		public void toJson(JsonObject jsonObject, FermentRandomlyLootFunction lootFunction, JsonSerializationContext jsonSerializationContext) {
			super.toJson(jsonObject, lootFunction, jsonSerializationContext);
			
			if (lootFunction.fermentationRecipeIdentifier != null) {
				jsonObject.addProperty("fermentation_recipe_id", lootFunction.fermentationRecipeIdentifier.toString());
			} else {
				jsonObject.add("fermentation_data", lootFunction.fermentationData.toJson());
			}
			jsonObject.add("days_fermented", jsonSerializationContext.serialize(lootFunction.daysFermented));
			jsonObject.add("thickness", jsonSerializationContext.serialize(lootFunction.thickness));
			jsonObject.add("downfall", jsonSerializationContext.serialize(lootFunction.downfall));
		}
		
		@Override
		public FermentRandomlyLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
			LootNumberProvider daysFermented = JsonHelper.deserialize(jsonObject, "days_fermented", jsonDeserializationContext, LootNumberProvider.class);
			LootNumberProvider thickness = JsonHelper.deserialize(jsonObject, "thickness", jsonDeserializationContext, LootNumberProvider.class);
			LootNumberProvider downfall = JsonHelper.deserialize(jsonObject, "downfall", jsonDeserializationContext, LootNumberProvider.class);
			
			if (jsonObject.has("fermentation_recipe_id")) {
				Identifier fermentationRecipeIdentifier = Identifier.tryParse(jsonObject.get("fermentation_recipe_id").getAsString());
				return new FermentRandomlyLootFunction(lootConditions, fermentationRecipeIdentifier, daysFermented, thickness, downfall);
			} else if (jsonObject.has("fermentation_data")) {
				FermentationData fermentationData = FermentationData.fromJson(jsonObject.get("fermentation_data").getAsJsonObject());
				return new FermentRandomlyLootFunction(lootConditions, fermentationData, daysFermented, thickness, downfall);
			}
			
			throw new JsonParseException("A 'ferment_randomly' loot function always needs to have either 'fermentation_data' or 'fermentation_recipe_id' set.");
		}
	}
}
