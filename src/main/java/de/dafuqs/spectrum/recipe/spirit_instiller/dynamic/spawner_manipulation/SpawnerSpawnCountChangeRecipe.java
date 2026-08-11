package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;


import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.*;

public class SpawnerSpawnCountChangeRecipe extends SpawnerChangeRecipe {
	protected static final int DEFAULT_SPAWN_COUNT = 4;
	protected static final int MAX_SPAWN_COUNT = 16;
	
	public SpawnerSpawnCountChangeRecipe() {
		super(IngredientStack.ofItems(SpectrumItems.NEOLITH.get(), 4));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput, @Nullable CustomData spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (spawnerBlockEntityNbt == null) {
			return true;
		}
		if (spawnerBlockEntityNbt.contains("SpawnCount")) {
			return spawnerBlockEntityNbt.copyTag().getShort("SpawnCount") < MAX_SPAWN_COUNT;
		}
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SPIRIT_INSTILLER_SPAWNER_SPAWN_COUNT_CHANGE;
	}
	
	@Override
	public Component getOutputLoreText() {
		return Component.translatable("recipe.spectrum.spawner.lore.increased_spawn_count");
	}
	
	@Override
	public CompoundTag getSpawnerResultNbt(CompoundTag nbt, ItemStack firstBowlStack, ItemStack secondBowlStack, InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput) {
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
		if (nbt.contains("SpawnCount", Tag.TAG_SHORT)) {
			spawnCount = nbt.getShort("SpawnCount");
		}
		nbt.putShort("SpawnCount", (short) Math.min(MAX_SPAWN_COUNT, spawnCount + 1));
		
		return nbt;
	}
	
}
