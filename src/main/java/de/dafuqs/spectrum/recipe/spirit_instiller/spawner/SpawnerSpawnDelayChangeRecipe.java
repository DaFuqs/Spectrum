package de.dafuqs.spectrum.recipe.spirit_instiller.spawner;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class SpawnerSpawnDelayChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerSpawnDelayChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpawnerSpawnDelayChangeRecipe::new);
	
	public SpawnerSpawnDelayChangeRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public IngredientStack getIngredientStack() {
		return IngredientStack.of(Ingredient.ofItems(SpectrumItems.MIDNIGHT_CHIP), 4);
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt.contains("MinSpawnDelay") && spawnerBlockEntityNbt.contains("MaxSpawnDelay")) {
			return spawnerBlockEntityNbt.getShort("MinSpawnDelay") > 20 && spawnerBlockEntityNbt.getShort("MaxSpawnDelay") > 20;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public TranslatableText getOutputLoreText() {
		return new TranslatableText("recipe.spectrum.spawner.lore.decreased_spawn_delay");
	}
	
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
		short minSpawnDelay = 200;
		if (spawnerBlockEntityNbt.contains("MinSpawnDelay", NbtElement.SHORT_TYPE)) {
			minSpawnDelay = spawnerBlockEntityNbt.getShort("MinSpawnDelay");
		}
		short maxSpawnDelay = 800;
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
		
		spawnerBlockEntityNbt.putShort("MinSpawnDelay", (short) Math.max(20, newMinSpawnDelay));
		spawnerBlockEntityNbt.putShort("MaxSpawnDelay", (short) Math.max(20, newMaxSpawnDelay));
		
		return spawnerBlockEntityNbt;
	}
	
}
