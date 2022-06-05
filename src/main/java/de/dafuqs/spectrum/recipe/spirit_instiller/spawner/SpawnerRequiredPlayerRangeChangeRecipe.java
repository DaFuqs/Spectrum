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

public class SpawnerRequiredPlayerRangeChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerRequiredPlayerRangeChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpawnerRequiredPlayerRangeChangeRecipe::new);
	
	public SpawnerRequiredPlayerRangeChangeRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public IngredientStack getIngredientStack() {
		return IngredientStack.of(Ingredient.ofItems(SpectrumItems.SCARLET_GEM), 4);
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
	public TranslatableText getOutputLoreText() {
		return new TranslatableText("recipe.spectrum.spawner.lore.increased_required_player_range");
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
