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

public class SpawnerSpawnCountChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerSpawnCountChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpawnerSpawnCountChangeRecipe::new);
	
	public SpawnerSpawnCountChangeRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public IngredientStack getIngredientStack() {
		return IngredientStack.of(Ingredient.ofItems(SpectrumItems.NEOLITH), 4);
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt.contains("SpawnCount")) {
			return spawnerBlockEntityNbt.getShort("SpawnCount") < 16;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public TranslatableText getOutputLoreText() {
		return new TranslatableText("recipe.spectrum.spawner.lore.increased_spawn_count");
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
		
		short spawnCount = 4;
		if (spawnerBlockEntityNbt.contains("SpawnCount", NbtElement.SHORT_TYPE)) {
			spawnCount = spawnerBlockEntityNbt.getShort("SpawnCount");
		}
		spawnerBlockEntityNbt.putShort("SpawnCount", (short) Math.min(16, spawnCount + 1));
		
		return spawnerBlockEntityNbt;
	}
	
}
