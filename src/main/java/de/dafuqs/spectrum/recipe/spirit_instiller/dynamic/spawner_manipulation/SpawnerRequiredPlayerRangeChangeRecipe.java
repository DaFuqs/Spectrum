package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;


import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;

public class SpawnerRequiredPlayerRangeChangeRecipe extends SpawnerChangeRecipe {
	protected static final int DEFAULT_DETECTION_RANGE = 16;
	protected static final int MAX_DETECTION_RANGE = 64;
	
	public SpawnerRequiredPlayerRangeChangeRecipe() {
		super(IngredientStack.ofItems(SpectrumItems.STRATINE_GEM, 4));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(CustomData spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt == null) {
			return true;
		}
		if (spawnerBlockEntityNbt.contains("RequiredPlayerRange")) {
			return spawnerBlockEntityNbt.copyTag().getShort("RequiredPlayerRange") < MAX_DETECTION_RANGE;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SPIRIT_INSTILLER_SPAWNER_SPAWNER_PLAYER_RANGE_CHANGE;
	}
	
	@Override
	public Component getOutputLoreText() {
		return Component.translatable("recipe.spectrum.spawner.lore.increased_required_player_range");
	}
	
	@Override
	public CompoundTag getSpawnerResultNbt(CompoundTag spawnerBlockEntityNbt, ItemStack firstBowlStack, ItemStack secondBowlStack) {
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
		if (spawnerBlockEntityNbt.contains("RequiredPlayerRange", Tag.TAG_SHORT)) {
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
