package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

// this hurt to write
public class ExplosionModificationRecipe extends ShapelessPedestalRecipe {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/modular_explosives");
	
	public ExplosionModificationRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER), PedestalRecipeTier.BASIC, collectIngredients(), Map.of(), ItemStack.EMPTY, 0.0F, 40, false, true);
	}
	
	private static List<IngredientStack> collectIngredients() {
		List<ItemLike> providers = new ArrayList<>();
		BuiltInRegistries.ITEM.stream().filter(item -> item instanceof ModularExplosionProvider).forEach(providers::add);
		IngredientStack providerIngredient = IngredientStack.of(Ingredient.of(providers.toArray(new ItemLike[]{})));
		
		Set<Item> modifiers = ExplosionModifierProviders.getProviders();
		IngredientStack modifierIngredient = IngredientStack.of(Ingredient.of(modifiers.toArray(new ItemLike[]{})));
		
		return List.of(providerIngredient, modifierIngredient);
	}
	
	@Override
	public boolean matches(PedestalRecipeInput inventory, Level world) {
		ItemStack nonModStack = validateGridAndFindModularExplosiveStack(inventory);
		if (!(nonModStack.getItem() instanceof ModularExplosionProvider modularExplosionProvider)) {
			return false;
		}
		
		Pair<List<ExplosionArchetype>, List<ExplosionModifier>> pair = findArchetypeAndModifiers(inventory);
		ModularExplosionDefinition currentSet = ModularExplosionDefinition.cloneFromStack(nonModStack);
		List<ExplosionArchetype> archetypes = pair.getFirst();
		List<ExplosionModifier> mods = pair.getSecond();
		
		// if there are no new modifiers to add present, treat it
		// as a recipe to clear existing archetype and / or modifiers
		if (archetypes.isEmpty() && mods.isEmpty()) {
			return currentSet.getArchetype() != ExplosionArchetype.COSMETIC || currentSet.getModifierCount() > 0;
		}
		
		if (!archetypes.isEmpty()) {
			@Nullable ExplosionArchetype newArchetype = calculateExplosionArchetype(currentSet.getArchetype(), archetypes);
			if (newArchetype == null) {
				return false;
			}
			currentSet.setArchetype(newArchetype);
		}
		
		currentSet.addModifiers(mods);
		return currentSet.isValid(modularExplosionProvider);
	}
	
	/**
	 * Returns null if the combination of archetypes would result in something nonsensical
	 */
	private static @Nullable ExplosionArchetype calculateExplosionArchetype(ExplosionArchetype existingArchetype, List<ExplosionArchetype> newArchetypes) {
		ExplosionArchetype newArchetype = existingArchetype;
		int newArchetypesCount = newArchetypes.size();
		if (existingArchetype == ExplosionArchetype.ALL && newArchetypesCount > 0) {
			return null;
		}
		if (newArchetypes.contains(ExplosionArchetype.ALL) && newArchetypesCount > 1) {
			return null;
		}
		
		for (ExplosionArchetype archetype : newArchetypes) {
			if (newArchetype == ExplosionArchetype.ALL) {
				return null;
			}
			newArchetype = ExplosionArchetype.get(newArchetype.affectsBlocks || archetype.affectsBlocks, newArchetype.affectsEntities || archetype.affectsEntities);
		}
		return newArchetype;
	}
	
	@Override
	public ItemStack assemble(PedestalRecipeInput input, HolderLookup.Provider drm) {
		ItemStack output = validateGridAndFindModularExplosiveStack(input).copy();
		
		Pair<List<ExplosionArchetype>, List<ExplosionModifier>> pair = findArchetypeAndModifiers(input);
		List<ExplosionArchetype> archetypes = pair.getFirst();
		List<ExplosionModifier> mods = pair.getSecond();
		
		if (archetypes.isEmpty() && mods.isEmpty()) { // clearing existing modifiers
			ModularExplosionDefinition.removeFromStack(output);
			return output;
		}
		
		ModularExplosionDefinition set = ModularExplosionDefinition.cloneFromStack(output);
		
		// adding new modifiers
		if (!archetypes.isEmpty()) {
			ExplosionArchetype newArchetype = calculateExplosionArchetype(set.getArchetype(), pair.getFirst());
			if (newArchetype != null) { // should never happen, but better safe than sorry
				set.setArchetype(newArchetype);
			}
		}
		
		set.addModifiers(mods);
		set.attachToStack(output);
		
		return output;
	}
	
	@Override
	public void consumeIngredients(PedestalBlockEntity pedestal) {
		for (int slot : CRAFTING_GRID_SLOTS) {
			ItemStack slotStack = pedestal.getItem(slot);
			if (slotStack.getItem() instanceof ModularExplosionProvider) {
				pedestal.setItem(slot, ItemStack.EMPTY);
			} else {
				slotStack.shrink(1);
			}
		}
	}
	
	/**
	 * Iterates all stacks in the grid and returns the modular explosive
	 * if the grid only contains that one and modifiers
	 */
	public ItemStack validateGridAndFindModularExplosiveStack(PedestalRecipeInput recipeInput) {
		ItemStack foundStack = ItemStack.EMPTY;
		for (int slot : recipeInput.getCraftingGridSlots()) {
			ItemStack stack = recipeInput.getItem(slot);
			if (!stack.isEmpty()
					&& stack.getItem() instanceof ModularExplosionProvider
					&& ExplosionModifierProviders.getModifier(stack) == null
					&& ExplosionModifierProviders.getArchetype(stack) == null) {
				
				if (foundStack == ItemStack.EMPTY) {
					foundStack = stack;
				} else {
					return ItemStack.EMPTY; // multiple non-mod stacks found
				}
			}
		}
		
		return foundStack;
	}
	
	public Pair<List<ExplosionArchetype>, List<ExplosionModifier>> findArchetypeAndModifiers(PedestalRecipeInput recipeInput) {
		List<ExplosionModifier> modifiers = new ArrayList<>();
		List<ExplosionArchetype> archetypes = new ArrayList<>();
		for (int slot : recipeInput.getCraftingGridSlots()) {
			ItemStack stack = recipeInput.getItem(slot);
			if (!stack.isEmpty()) {
				ExplosionModifier modifier = ExplosionModifierProviders.getModifier(stack);
				if (modifier != null) {
					modifiers.add(modifier);
					continue;
				}
				ExplosionArchetype archetype = ExplosionModifierProviders.getArchetype(stack);
				if (archetype != null) {
					archetypes.add(archetype);
				}
			}
		}
		return new Pair<>(archetypes, modifiers);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.MODULAR_EXPLOSIVE_MODIFICATION;
	}
	
}
