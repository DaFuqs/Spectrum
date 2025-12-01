package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpawnerCreatureChangeRecipe extends SpawnerChangeRecipe {
	
	public SpawnerCreatureChangeRecipe() {
		super(IngredientStack.ofTag(SpectrumItemTags.SKULLS), IngredientStack.ofItems(SpectrumItems.DOWNSTONE_FRAGMENTS, 4), Optional.of(SpectrumAdvancements.SPAWNER_CREATURE_CHANGE));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SPIRIT_INSTILLER_SPAWNER_CREATURE_CHANGE;
	}
	
	@Override
	public boolean canCraftWithBlockEntityTag(InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput, CustomData spawnerBlockEntityNbt, ItemStack firstBowlStack, ItemStack secondBowlStack) {
		Optional<EntityType<?>> entityType = SpectrumSkullBlock.getEntityTypeOfSkullStack(firstBowlStack);
		entityType = entityType.isEmpty() ? SpectrumSkullBlock.getEntityTypeOfSkullStack(secondBowlStack) : entityType;
		
		if (entityType.isEmpty() || entityType.get().is(SpectrumEntityTypeTags.SPAWNER_MANIPULATION_BLACKLISTED)) {
			@Nullable Player player = recipeInput.getInstance().getOwnerIfOnline();
			if (player instanceof ServerPlayer serverPlayer) {
				Support.grantAdvancementCriterion(serverPlayer, SpectrumAdvancements.FAILED_CREATING_EMPTY_OR_BLACKLISTED_SPAWNER, "failed_creating_empty_or_blacklisted_spawner");
			}
			return false;
		}
		
		if (entityType.get() == EntityType.PLAYER) {
			@Nullable Player player = recipeInput.getInstance().getOwnerIfOnline();
			if (player instanceof ServerPlayer serverPlayer) {
				Support.grantAdvancementCriterion(serverPlayer, SpectrumAdvancements.FAILED_CREATING_PLAYER_SPAWNER, "failed_creating_player_spawner");
			}
			return false;
		}
		
		if (spawnerBlockEntityNbt == null) {
			return true;
		}
		
		if (spawnerBlockEntityNbt.contains("SpawnData")) {
			CompoundTag spawnData = spawnerBlockEntityNbt.copyTag().getCompound("SpawnData");
			if (spawnData.contains("entity")) {
				CompoundTag entity = spawnData.getCompound("entity");
				if (entity.contains("id")) {
					ResourceLocation entityTypeIdentifier = BuiltInRegistries.ENTITY_TYPE.getKey(entityType.get());
					return !entityTypeIdentifier.toString().equals(entity.getString("id"));
				}
			}
		}
		return true;
	}
	
	@Override
	public Component getOutputLoreText() {
		return Component.translatable("recipe.spectrum.spawner.lore.changed_creature");
	}
	
	@Override
	public CompoundTag getSpawnerResultNbt(CompoundTag nbt, ItemStack firstBowlStack, ItemStack secondBowlStack, InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput) {
		Optional<EntityType<?>> entityType = SpectrumSkullBlock.getEntityTypeOfSkullStack(firstBowlStack);
		entityType = entityType.isEmpty() ? SpectrumSkullBlock.getEntityTypeOfSkullStack(secondBowlStack) : entityType;
		
		if (entityType.isEmpty()) {
			return nbt;
		}
		
		ResourceLocation entityTypeIdentifier = BuiltInRegistries.ENTITY_TYPE.getKey(entityType.get());
		
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
		
		CompoundTag idCompound = new CompoundTag();
		idCompound.putString("id", entityTypeIdentifier.toString());
		CompoundTag entityCompound = new CompoundTag();
		entityCompound.put("entity", idCompound);
		nbt.put("SpawnData", entityCompound);
		
		if (nbt.contains("SpawnPotentials")) {
			nbt.remove("SpawnPotentials");
		}
		
		return nbt;
	}
	
}
