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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public abstract class SpawnerChangeRecipe extends SpiritInstillerRecipe {
	
	public SpawnerChangeRecipe(IngredientStack ingredient, IngredientStack ingredient2, Optional<ResourceLocation> requiredAdvancementIdentifier) {
		super("spawner_manipulation", false, requiredAdvancementIdentifier,
				IngredientStack.ofItems(Items.SPAWNER), ingredient, ingredient2,
				Items.SPAWNER.getDefaultInstance(), 200, 0, true);
	}
	
	public SpawnerChangeRecipe(IngredientStack ingredient) {
		super("spawner_manipulation", false, Optional.of(SpectrumAdvancements.SPAWNER_MANIPULATION),
				IngredientStack.ofItems(Items.SPAWNER), ingredient, IngredientStack.ofItems(SpectrumItems.VEGETAL, 4),
				Items.SPAWNER.getDefaultInstance(), 200, 0, true);
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
			
			// TODO - Review
			CustomData spawnerNbt = spawnerStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
			
			CompoundTag blockEntityTag;
			if (spawnerNbt.contains("BlockEntityTag")) {
				blockEntityTag = spawnerNbt.copyTag().getCompound("BlockEntityTag");
			} else {
				blockEntityTag = new CompoundTag();
			}
			
			blockEntityTag = getSpawnerResultNbt(blockEntityTag, firstBowlStack, secondBowlStack);
			
			resultStack = spawnerStack.copy();
			resultStack.setCount(1);
			
			resultStack.set(DataComponents.CUSTOM_DATA, CustomData.of(blockEntityTag));
			
			spawnXPAndGrantAdvancements(resultStack, spiritInstillerBlockEntity, spiritInstillerBlockEntity.getUpgradeHolder(), world, pos);
		}
		return resultStack;
	}
	
	@Override
	public boolean canCraftWithStacks(RecipeInput inventory) {
		CustomData blockEntityComponent = inventory.getItem(0).getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
		return canCraftWithBlockEntityTag(blockEntityComponent, inventory.getItem(1), inventory.getItem(2));
	}
	
	public abstract boolean canCraftWithBlockEntityTag(CustomData spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack);
	
	public abstract CompoundTag getSpawnerResultNbt(CompoundTag spawnerBlockEntityNbt, ItemStack secondBowlStack, ItemStack centerStack);
	
	public abstract Component getOutputLoreText();
	
}
