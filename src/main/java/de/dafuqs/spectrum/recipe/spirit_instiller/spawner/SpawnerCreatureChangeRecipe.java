package de.dafuqs.spectrum.recipe.spirit_instiller.spawner;

import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class SpawnerCreatureChangeRecipe extends SpawnerChangeRecipe {
	
	public static final RecipeSerializer<SpawnerCreatureChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpawnerCreatureChangeRecipe::new);
	
	public SpawnerCreatureChangeRecipe(Identifier identifier) {
		super(identifier, IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.SKULLS)));
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack firstBowlStack, ItemStack secondBowlStack) {
		Optional<EntityType<?>> entityType = SpectrumSkullBlockItem.getEntityTypeOfSkullStack(firstBowlStack);
		entityType = entityType.isEmpty() ? SpectrumSkullBlockItem.getEntityTypeOfSkullStack(secondBowlStack) : entityType;
		
		if (entityType.isEmpty()) {
			return false;
		}
		if (entityType.get().isIn(SpectrumEntityTypeTags.SPAWNER_MANIPULATION_BLACKLISTED)) {
			return false;
		}
		
		if (spawnerBlockEntityNbt.contains("SpawnData")) {
			NbtCompound spawnData = spawnerBlockEntityNbt.getCompound("SpawnData");
			if (spawnData.contains("entity")) {
				NbtCompound entity = spawnData.getCompound("entity");
				if (entity.contains("id")) {
					Identifier entityTypeIdentifier = Registry.ENTITY_TYPE.getId(entityType.get());
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
		Optional<EntityType<?>> entityType = SpectrumSkullBlockItem.getEntityTypeOfSkullStack(firstBowlStack);
		entityType = entityType.isEmpty() ? SpectrumSkullBlockItem.getEntityTypeOfSkullStack(secondBowlStack) : entityType;
		
		if (entityType.isEmpty()) {
			return spawnerBlockEntityNbt;
		}
		
		Identifier entityTypeIdentifier = Registry.ENTITY_TYPE.getId(entityType.get());
		
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
