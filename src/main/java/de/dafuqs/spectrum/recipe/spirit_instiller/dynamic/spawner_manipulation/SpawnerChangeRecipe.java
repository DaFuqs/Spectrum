package de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.entity.*;
import org.jspecify.annotations.*;

import java.util.*;

public abstract class SpawnerChangeRecipe extends SpiritInstillerRecipe {
	
	public SpawnerChangeRecipe(IngredientStack ingredient, IngredientStack ingredient2, Optional<ResourceLocation> requiredAdvancementIdentifier) {
		super("spawner_manipulation", false, requiredAdvancementIdentifier,
				IngredientStack.ofItems(Items.SPAWNER), ingredient, ingredient2,
				Items.SPAWNER.getDefaultInstance(), 200, 0, true, false);
	}
	
	public SpawnerChangeRecipe(IngredientStack ingredient) {
		super("spawner_manipulation", false, Optional.of(SpectrumAdvancements.SPAWNER_MANIPULATION),
				IngredientStack.ofItems(Items.SPAWNER), ingredient, IngredientStack.ofItems(SpectrumItems.VEGETAL.get(), 4),
				Items.SPAWNER.getDefaultInstance(), 200, 0, true, false);
	}
	
	@Override
	public ItemStack assemble(InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput, HolderLookup.Provider drm) {
		SpiritInstillerBlockEntity spiritInstillerBlockEntity = recipeInput.getInstance();
		ItemStack resultStack = ItemStack.EMPTY;
		var world = spiritInstillerBlockEntity.getLevel();
		if (world == null) return ItemStack.EMPTY;
		BlockEntity leftBowlBlockEntity = world.getBlockEntity(SpiritInstillerBlockEntity.getItemBowlPos(spiritInstillerBlockEntity, false));
		BlockEntity rightBowlBlockEntity = world.getBlockEntity(SpiritInstillerBlockEntity.getItemBowlPos(spiritInstillerBlockEntity, true));
		if (leftBowlBlockEntity instanceof ItemBowlBlockEntity leftBowl && rightBowlBlockEntity instanceof ItemBowlBlockEntity rightBowl) {
			BlockPos pos = spiritInstillerBlockEntity.getBlockPos();
			
			ItemStack firstBowlStack = leftBowl.getItem(0);
			ItemStack secondBowlStack = rightBowl.getItem(0);
			ItemStack spawnerStack = spiritInstillerBlockEntity.getItem(0);
			
			CompoundTag spawnerNbt = spawnerStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
			if (!spawnerNbt.contains("id")) {
				spawnerNbt.putString("id", "minecraft:spawner");
			}
			spawnerNbt = getSpawnerResultNbt(spawnerNbt, firstBowlStack, secondBowlStack, recipeInput);
			resultStack = spawnerStack.copyWithCount(1);
			resultStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(spawnerNbt));
			
			spawnXPAndGrantAdvancements(resultStack, spiritInstillerBlockEntity, spiritInstillerBlockEntity.getUpgradeHolder(), world, pos);
		}
		return resultStack;
	}
	
	@Override
	public boolean canCraftWithStacks(InstanceRecipeInput<SpiritInstillerBlockEntity> inventory) {
		CustomData blockEntityComponent = inventory.getItem(0).getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
		return canCraftWithBlockEntityTag(inventory, blockEntityComponent, inventory.getItem(1), inventory.getItem(2));
	}
	
	public abstract boolean canCraftWithBlockEntityTag(InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput, @Nullable CustomData spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack);
	
	public abstract CompoundTag getSpawnerResultNbt(CompoundTag nbt, ItemStack firstBowlStack, ItemStack secondBowlStack, InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput);
	
	public abstract Component getOutputLoreText();
	
}
