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

public class SpawnerMaxNearbyEntitiesChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerMaxNearbyEntitiesChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpawnerMaxNearbyEntitiesChangeRecipe::new);
	
	public SpawnerMaxNearbyEntitiesChangeRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public IngredientStack getIngredientStack() {
		return IngredientStack.of(Ingredient.ofItems(SpectrumItems.MERMAIDS_GEM), 4);
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt.contains("MaxNearbyEntities")) {
			return spawnerBlockEntityNbt.getShort("MaxNearbyEntities") < 40;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public TranslatableText getOutputLoreText() {
		return new TranslatableText("recipe.spectrum.spawner.lore.increased_max_nearby_entities");
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
		
		short maxNearbyEntities = 6;
		if (spawnerBlockEntityNbt.contains("MaxNearbyEntities", NbtElement.SHORT_TYPE)) {
			maxNearbyEntities = spawnerBlockEntityNbt.getShort("MaxNearbyEntities");
		}
		spawnerBlockEntityNbt.putShort("MaxNearbyEntities", (short) Math.min(40, maxNearbyEntities + 1));
		
		return spawnerBlockEntityNbt;
	}
	
}
