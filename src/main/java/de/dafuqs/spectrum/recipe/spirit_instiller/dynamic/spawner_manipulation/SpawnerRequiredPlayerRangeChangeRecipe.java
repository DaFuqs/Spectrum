package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class SpawnerRequiredPlayerRangeChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerRequiredPlayerRangeChangeRecipe> SERIALIZER = new EmptyRecipeSerializer<>(SpawnerRequiredPlayerRangeChangeRecipe::new);
	protected static final int DEFAULT_DETECTION_RANGE = 16;
	protected static final int MAX_DETECTION_RANGE = 64;
	public SpawnerRequiredPlayerRangeChangeRecipe(Identifier identifier) {
		super(identifier, IngredientStack.ofItems(4, SpectrumItems.STRATINE_GEM));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt == null) {
			return true;
		}
		if (spawnerBlockEntityNbt.contains("RequiredPlayerRange")) {
			return spawnerBlockEntityNbt.getShort("RequiredPlayerRange") < MAX_DETECTION_RANGE;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public Text getOutputLoreText() {
		return Text.translatable("recipe.spectrum.spawner.lore.increased_required_player_range");
	}
	
	@Override
	public NbtCompound getSpawnerResultNbt(NbtCompound spawnerBlockEntityNbt, ItemStack firstBowlStack, ItemStack secondBowlStack) {
		// Default spawner tag:
		/* BlockEntityTag: {
			MaxNearbyEntities: 6s,
			RequiredPlayerRange: 16s,
			SpawnCount: 4s,
			SpawnData: {entity: {id: "minecraft:xxx"}},
			MaxSpawnDelay: 800s,
			SpawnRange: 4s,
			MinSpawnDelay: 200s,
			SpawnPotentials: []
		   }
		 */
		
		short requiredPlayerRange = DEFAULT_DETECTION_RANGE;
		if (spawnerBlockEntityNbt.contains("RequiredPlayerRange", NbtElement.SHORT_TYPE)) {
			requiredPlayerRange = spawnerBlockEntityNbt.getShort("RequiredPlayerRange");
		}
		
		short newRequiredPlayerRange = (short) Math.pow(requiredPlayerRange, 1.02);
		if (newRequiredPlayerRange == requiredPlayerRange) {
			newRequiredPlayerRange = (short) (requiredPlayerRange + 1);
		}
		
		spawnerBlockEntityNbt.putShort("RequiredPlayerRange", (short) Math.min(MAX_DETECTION_RANGE, newRequiredPlayerRange));
		
		return spawnerBlockEntityNbt;
	}
	
}
