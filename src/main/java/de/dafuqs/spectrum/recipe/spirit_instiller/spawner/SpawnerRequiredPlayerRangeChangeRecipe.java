package de.dafuqs.spectrum.recipe.spirit_instiller.spawner;

import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.id.incubus_core.recipe.matchbook.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class SpawnerRequiredPlayerRangeChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerRequiredPlayerRangeChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpawnerRequiredPlayerRangeChangeRecipe::new);
	
	public SpawnerRequiredPlayerRangeChangeRecipe(Identifier identifier) {
		super(identifier, IngredientStack.of(Ingredient.ofItems(SpectrumItems.STRATINE_GEM), Matchbook.empty(), null, 4));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt.contains("RequiredPlayerRange")) {
			return spawnerBlockEntityNbt.getShort("RequiredPlayerRange") < 256;
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
		
		short requiredPlayerRange = 16;
		if (spawnerBlockEntityNbt.contains("RequiredPlayerRange", NbtElement.SHORT_TYPE)) {
			requiredPlayerRange = spawnerBlockEntityNbt.getShort("RequiredPlayerRange");
		}
		
		short newRequiredPlayerRange = (short) Math.pow(requiredPlayerRange, 1.02);
		if (newRequiredPlayerRange == requiredPlayerRange) {
			newRequiredPlayerRange = (short) (requiredPlayerRange + 1);
		}
		
		spawnerBlockEntityNbt.putShort("RequiredPlayerRange", (short) Math.min(256, newRequiredPlayerRange));
		
		return spawnerBlockEntityNbt;
	}
	
}
