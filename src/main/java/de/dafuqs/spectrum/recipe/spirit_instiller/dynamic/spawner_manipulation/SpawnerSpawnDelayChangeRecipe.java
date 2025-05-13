package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.component.type.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;

public class SpawnerSpawnDelayChangeRecipe extends SpawnerChangeRecipe {
	
	protected static final int DEFAULT_MIN_DELAY = 200;
	protected static final int DEFAULT_MAX_DELAY = 800;
	
	protected static final int MIN_MIN_DELAY = 20;
	protected static final int MIN_MAX_DELAY = 40;
	
	protected static final float EXPONENT = 0.98F;
	
	public SpawnerSpawnDelayChangeRecipe() {
		super(IngredientStack.ofItems(SpectrumItems.MIDNIGHT_CHIP, 4));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtComponent spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt == null) {
			return true;
		}
		NbtCompound nbt = spawnerBlockEntityNbt.getNbt();
		return (!nbt.contains("MinSpawnDelay") || nbt.getShort("MinSpawnDelay") > MIN_MIN_DELAY)
				&& (!nbt.contains("MaxSpawnDelay") || nbt.getShort("MaxSpawnDelay") > MIN_MAX_DELAY);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SPIRIT_INSTILLER_SPAWNER_SPAWN_DELAY_CHANGE;
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
		
		// 800 => 700 => 614 => 540 => 476 => 421 => 373 => 331 => ... => MIN_DELAY
		short minSpawnDelay = DEFAULT_MIN_DELAY;
		if (spawnerBlockEntityNbt.contains("MinSpawnDelay", NbtElement.SHORT_TYPE)) {
			minSpawnDelay = spawnerBlockEntityNbt.getShort("MinSpawnDelay");
		}
		short maxSpawnDelay = DEFAULT_MAX_DELAY;
		if (spawnerBlockEntityNbt.contains("MaxSpawnDelay", NbtElement.SHORT_TYPE)) {
			maxSpawnDelay = spawnerBlockEntityNbt.getShort("MaxSpawnDelay");
		}
		
		short newMinSpawnDelay = (short) Math.pow(minSpawnDelay, EXPONENT);
		if (newMinSpawnDelay == minSpawnDelay) {
			newMinSpawnDelay = (short) (minSpawnDelay - 1);
		}
		
		short newMaxSpawnDelay = (short) Math.pow(maxSpawnDelay, EXPONENT);
		if (newMaxSpawnDelay == maxSpawnDelay) {
			newMaxSpawnDelay = (short) (maxSpawnDelay - 1);
		}
		
		spawnerBlockEntityNbt.putShort("MinSpawnDelay", (short) Math.max(MIN_MIN_DELAY, newMinSpawnDelay));
		spawnerBlockEntityNbt.putShort("MaxSpawnDelay", (short) Math.max(MIN_MAX_DELAY, newMaxSpawnDelay));
		
		return spawnerBlockEntityNbt;
	}
	
}
