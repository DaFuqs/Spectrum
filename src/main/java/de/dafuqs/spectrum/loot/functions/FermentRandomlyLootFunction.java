package de.dafuqs.spectrum.loot.functions;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FermentRandomlyLootFunction extends LootItemConditionalFunction {
	
	public static final MapCodec<FermentRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(
			Codec.either(ResourceLocation.CODEC, FermentationData.CODEC).fieldOf("fermentation").forGetter(c -> c.fermentation),
			NumberProviders.CODEC.fieldOf("days_fermented").forGetter(c -> c.daysFermented),
			NumberProviders.CODEC.fieldOf("thickness").forGetter(c -> c.thickness)
	)).apply(i, FermentRandomlyLootFunction::new));
	
	private final Either<ResourceLocation, FermentationData> fermentation;
	private final NumberProvider daysFermented;
	private final NumberProvider thickness;
	
	public FermentRandomlyLootFunction(List<LootItemCondition> conditions, Either<ResourceLocation, FermentationData> fermentation, NumberProvider daysFermented, NumberProvider thickness) {
		super(conditions);
		this.fermentation = fermentation;
		this.daysFermented = daysFermented;
		this.thickness = thickness;
	}
	
	public FermentRandomlyLootFunction(List<LootItemCondition> conditions, @NotNull ResourceLocation fermentationRecipeIdentifier, NumberProvider daysFermented, NumberProvider thickness) {
		this(conditions, Either.left(fermentationRecipeIdentifier), daysFermented, thickness);
	}
	
	public FermentRandomlyLootFunction(List<LootItemCondition> conditions, @NotNull FermentationData fermentationData, NumberProvider daysFermented, NumberProvider thickness) {
		this(conditions, Either.right(fermentationData), daysFermented, thickness);
	}
	
	@Override
	public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
		return SpectrumLootFunctionTypes.FERMENT_RANDOMLY;
	}
	
	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		FermentationData fermentationData = this.fermentation.map(
				id -> {
					var recipe = context.getLevel().getRecipeManager().byKey(id);
					if (recipe.isPresent() && recipe.get().value() instanceof TitrationBarrelRecipe titrationBarrelRecipe) {
						return titrationBarrelRecipe.getFermentationData();
					} else {
						SpectrumCommon.logError("A 'spectrum:ferment_randomly' loot function has set an invalid 'fermentation_recipe_id': " + id + " It has to match an existing Titration Barrel recipe.");
						return null;
					}
				},
				data -> this.fermentation.right().orElse(null)
		);
		if (fermentationData != null) {
			var origin = context.getParamOrNull(LootContextParams.ORIGIN);
			if (origin != null) {
				BlockPos pos = BlockPos.containing(origin);
				Biome biome = context.getLevel().getBiome(pos).value();
				float downfall = ((BiomeAccessor) (Object) biome).getClimateSettings().downfall();
				return TitrationBarrelRecipe.getFermentedStack(fermentationData, this.thickness.getInt(context), TimeHelper.secondsFromMinecraftDays(this.daysFermented.getInt(context)), downfall, stack);
			} else {
				SpectrumCommon.logError("A 'spectrum:ferment_randomly' loot function does not have access to 'origin'.");
			}
		}
		return stack;
	}
	
	public static LootItemConditionalFunction.Builder<?> builder(FermentationData fermentationData, NumberProvider daysFermented, NumberProvider thickness) {
		return simpleBuilder((conditions) -> new FermentRandomlyLootFunction(conditions, fermentationData, daysFermented, thickness));
	}
	
	public static LootItemConditionalFunction.Builder<?> builder(ResourceLocation fermentationRecipeIdentifier, NumberProvider daysFermented, NumberProvider thickness) {
		return simpleBuilder((conditions) -> new FermentRandomlyLootFunction(conditions, fermentationRecipeIdentifier, daysFermented, thickness));
	}
	
}
