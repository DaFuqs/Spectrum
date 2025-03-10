package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class SpawnerSpawnCountChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerSpawnCountChangeRecipe> SERIALIZER = new EmptyRecipeSerializer<>(SpawnerSpawnCountChangeRecipe::new);
	protected static final int DEFAULT_SPAWN_COUNT = 4;
	protected static final int MAX_SPAWN_COUNT = 16;
	public SpawnerSpawnCountChangeRecipe(Identifier identifier) {
		super(identifier, IngredientStack.ofItems(4, SpectrumItems.NEOLITH));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt == null) {
			return true;
		}
		if (spawnerBlockEntityNbt.contains("SpawnCount")) {
			return spawnerBlockEntityNbt.getShort("SpawnCount") < MAX_SPAWN_COUNT;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public Text getOutputLoreText() {
		return Text.translatable("recipe.spectrum.spawner.lore.increased_spawn_count");
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
		
		short spawnCount = DEFAULT_SPAWN_COUNT;
		if (spawnerBlockEntityNbt.contains("SpawnCount", NbtElement.SHORT_TYPE)) {
			spawnCount = spawnerBlockEntityNbt.getShort("SpawnCount");
		}
		spawnerBlockEntityNbt.putShort("SpawnCount", (short) Math.min(MAX_SPAWN_COUNT, spawnCount + 1));
		
		return spawnerBlockEntityNbt;
	}
	
}
