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
import net.minecraft.util.math.*;
import net.minecraft.world.biome.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FermentRandomlyLootFunction extends ConditionalLootFunction {
	
	final @Nullable Identifier fermentationRecipeIdentifier;
	final @Nullable FermentationData fermentationData;
	final LootNumberProvider daysFermented;
	final LootNumberProvider thickness;
	
	public FermentRandomlyLootFunction(LootCondition[] conditions, @NotNull Identifier fermentationRecipeIdentifier, LootNumberProvider daysFermented, LootNumberProvider thickness) {
		super(conditions);
		this.fermentationRecipeIdentifier = fermentationRecipeIdentifier;
		this.fermentationData = null;
		this.daysFermented = daysFermented;
		this.thickness = thickness;
	}
	
	public FermentRandomlyLootFunction(LootCondition[] conditions, @NotNull FermentationData fermentationData, LootNumberProvider daysFermented, LootNumberProvider thickness) {
		super(conditions);
		this.fermentationRecipeIdentifier = null;
		this.fermentationData = fermentationData;
		this.daysFermented = daysFermented;
		this.thickness = thickness;
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
				SpectrumCommon.logError("A 'spectrum:ferment_randomly' loot function has set an invalid 'fermentation_recipe_id': " + this.fermentationRecipeIdentifier + " It has to match an existing Titration Barrel recipe.");
			}
		}
		if (fermentationData == null) {
			fermentationData = this.fermentationData;
		}
		if (fermentationData != null) {
			Biome biome = context.getWorld().getBiome(new BlockPos(context.get(LootContextParameters.ORIGIN))).value();
			return TitrationBarrelRecipe.getFermentedStack(fermentationData, this.thickness.nextInt(context), TimeHelper.secondsFromMinecraftDays(this.daysFermented.nextInt(context)), biome.getDownfall(), stack);
		}
		return stack;
	}
	
	public static ConditionalLootFunction.Builder<?> builder(FermentationData fermentationData, LootNumberProvider daysFermented, LootNumberProvider thickness) {
		return builder((conditions) -> new FermentRandomlyLootFunction(conditions, fermentationData, daysFermented, thickness));
	}
	
	public static ConditionalLootFunction.Builder<?> builder(Identifier fermentationRecipeIdentifier, LootNumberProvider daysFermented, LootNumberProvider thickness) {
		return builder((conditions) -> new FermentRandomlyLootFunction(conditions, fermentationRecipeIdentifier, daysFermented, thickness));
	}
	
	public static class Serializer extends ConditionalLootFunction.Serializer<FermentRandomlyLootFunction> {
		
		private static final String FERMENTATION_RECIPE_ID_STRING = "fermentation_recipe_id";
		private static final String FERMENTATION_DATA_STRING = "fermentation_data";
		private static final String DAYS_FERMENTED_STRING = "days_fermented";
		private static final String THICKNESS_STRING = "thickness";
		
		@Override
		public void toJson(JsonObject jsonObject, FermentRandomlyLootFunction lootFunction, JsonSerializationContext jsonSerializationContext) {
			super.toJson(jsonObject, lootFunction, jsonSerializationContext);
			
			if (lootFunction.fermentationRecipeIdentifier != null) {
				jsonObject.addProperty(FERMENTATION_RECIPE_ID_STRING, lootFunction.fermentationRecipeIdentifier.toString());
			} else {
				jsonObject.add(FERMENTATION_DATA_STRING, lootFunction.fermentationData.toJson());
			}
			jsonObject.add(DAYS_FERMENTED_STRING, jsonSerializationContext.serialize(lootFunction.daysFermented));
			jsonObject.add(THICKNESS_STRING, jsonSerializationContext.serialize(lootFunction.thickness));
		}
		
		@Override
		public FermentRandomlyLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
			LootNumberProvider daysFermented = JsonHelper.deserialize(jsonObject, DAYS_FERMENTED_STRING, jsonDeserializationContext, LootNumberProvider.class);
			LootNumberProvider thickness = JsonHelper.deserialize(jsonObject, THICKNESS_STRING, jsonDeserializationContext, LootNumberProvider.class);
			
			if (jsonObject.has(FERMENTATION_RECIPE_ID_STRING)) {
				Identifier fermentationRecipeIdentifier = Identifier.tryParse(jsonObject.get(FERMENTATION_RECIPE_ID_STRING).getAsString());
				return new FermentRandomlyLootFunction(lootConditions, fermentationRecipeIdentifier, daysFermented, thickness);
			} else if (jsonObject.has(FERMENTATION_DATA_STRING)) {
				FermentationData fermentationData = FermentationData.fromJson(jsonObject.get(FERMENTATION_DATA_STRING).getAsJsonObject());
				return new FermentRandomlyLootFunction(lootConditions, fermentationData, daysFermented, thickness);
			}
			
			throw new JsonParseException("A 'ferment_randomly' loot function always needs to have either 'fermentation_data' or 'fermentation_recipe_id' set.");
		}
	}
}
