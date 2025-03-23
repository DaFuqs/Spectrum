package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class SpawnerSpawnDelayChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerSpawnDelayChangeRecipe> SERIALIZER = new EmptyRecipeSerializer<>(SpawnerSpawnDelayChangeRecipe::new);
	protected static final int DEFAULT_MIN_DELAY = 200;
	protected static final int DEFAULT_MAX_DELAY = 800;
	protected static final int MIN_DELAY = 20;
	public SpawnerSpawnDelayChangeRecipe(Identifier identifier) {
		super(identifier, IngredientStack.ofItems(4, SpectrumItems.MIDNIGHT_CHIP));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt == null) {
			return true;
		}
		if (spawnerBlockEntityNbt.contains("MinSpawnDelay") && spawnerBlockEntityNbt.contains("MaxSpawnDelay")) {
			return spawnerBlockEntityNbt.getShort("MinSpawnDelay") > MIN_DELAY
					&& spawnerBlockEntityNbt.getShort("MaxSpawnDelay") > MIN_DELAY;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public Text getOutputLoreText() {
		return Text.translatable("recipe.spectrum.spawner.lore.decreased_spawn_delay");
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
		
		// 800 => 700 => 614 => 540 => 476 => 421 => 373 => 331 => ... (down to a min of 1 each)
		// makes 40 recipes to match the min count for MaxSpawnDelay of 20 ticks
		short minSpawnDelay = DEFAULT_MIN_DELAY;
		if (spawnerBlockEntityNbt.contains("MinSpawnDelay", NbtElement.SHORT_TYPE)) {
			minSpawnDelay = spawnerBlockEntityNbt.getShort("MinSpawnDelay");
		}
		short maxSpawnDelay = DEFAULT_MAX_DELAY;
		if (spawnerBlockEntityNbt.contains("MaxSpawnDelay", NbtElement.SHORT_TYPE)) {
			maxSpawnDelay = spawnerBlockEntityNbt.getShort("MaxSpawnDelay");
		}
		
		short newMinSpawnDelay = (short) Math.pow(minSpawnDelay, 0.98);
		if (newMinSpawnDelay == minSpawnDelay) {
			newMinSpawnDelay = (short) (minSpawnDelay - 1);
		}
		
		short newMaxSpawnDelay = (short) Math.pow(maxSpawnDelay, 0.98);
		if (newMaxSpawnDelay == maxSpawnDelay) {
			newMaxSpawnDelay = (short) (maxSpawnDelay - 1);
		}
		
		spawnerBlockEntityNbt.putShort("MinSpawnDelay", (short) Math.max(MIN_DELAY, newMinSpawnDelay));
		spawnerBlockEntityNbt.putShort("MaxSpawnDelay", (short) Math.max(MIN_DELAY, newMaxSpawnDelay));
		
		return spawnerBlockEntityNbt;
	}
	
}
