package de.dafuqs.spectrum.recipe.spirit_instiller.spawner;

import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

import java.util.*;

public class SpawnerCreatureChangeRecipe extends SpawnerChangeRecipe {

	public static final RecipeSerializer<SpawnerCreatureChangeRecipe> SERIALIZER = new EmptyRecipeSerializer<>(SpawnerCreatureChangeRecipe::new);
	
	public SpawnerCreatureChangeRecipe(Identifier identifier) {
		super(identifier, IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.MOB_HEADS)));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		ItemStack mobHeadStack;
		if (leftBowlStack.isIn(SpectrumItemTags.MOB_HEADS)) {
			mobHeadStack = leftBowlStack;
		} else if (rightBowlStack.isIn(SpectrumItemTags.MOB_HEADS)) {
			mobHeadStack = rightBowlStack;
		} else {
			return false;
		}
		
		Optional<EntityType> entityType = SpectrumSkullBlockItem.getEntityTypeOfSkullStack(mobHeadStack);
		if (entityType.isEmpty()) {
			return false;
		}
		
		if (spawnerBlockEntityNbt.contains("SpawnData")) {
			NbtCompound spawnData = spawnerBlockEntityNbt.getCompound("SpawnData");
			if (spawnData.contains("entity")) {
				NbtCompound entity = spawnData.getCompound("entity");
				if (entity.contains("id")) {
					Identifier entityTypeIdentifier = Registries.ENTITY_TYPE.getId(entityType.get());
					return !entityTypeIdentifier.toString().equals(entity.getString("id"));
				}
			}
		}
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public Text getOutputLoreText() {
		return Text.translatable("recipe.spectrum.spawner.lore.changed_creature");
	}
	
	@Override
	public NbtCompound getSpawnerResultNbt(NbtCompound spawnerBlockEntityNbt, ItemStack firstBowlStack, ItemStack secondBowlStack) {
		ItemStack mobHeadStack;
		if (firstBowlStack.isIn(SpectrumItemTags.MOB_HEADS)) {
			mobHeadStack = firstBowlStack;
		} else if (secondBowlStack.isIn(SpectrumItemTags.MOB_HEADS)) {
			mobHeadStack = secondBowlStack;
		} else {
			return spawnerBlockEntityNbt;
		}
		
		Optional<EntityType> entityType = SpectrumSkullBlockItem.getEntityTypeOfSkullStack(mobHeadStack);
		if (entityType.isEmpty()) {
			return spawnerBlockEntityNbt;
		}
		
		Identifier entityTypeIdentifier = Registries.ENTITY_TYPE.getId(entityType.get());
		
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
		
		NbtCompound idCompound = new NbtCompound();
		idCompound.putString("id", entityTypeIdentifier.toString());
		NbtCompound entityCompound = new NbtCompound();
		entityCompound.put("entity", idCompound);
		spawnerBlockEntityNbt.put("SpawnData", entityCompound);
		
		if (spawnerBlockEntityNbt.contains("SpawnPotentials")) {
			spawnerBlockEntityNbt.remove("SpawnPotentials");
		}
		
		return spawnerBlockEntityNbt;
	}
	
}
