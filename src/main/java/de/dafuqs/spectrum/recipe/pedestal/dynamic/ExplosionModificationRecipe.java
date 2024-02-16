package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

// this hurt to write
public class ExplosionModificationRecipe extends ShapelessPedestalRecipe {
	
	public static final RecipeSerializer<ExplosionModificationRecipe> SERIALIZER = new EmptyRecipeSerializer<>(ExplosionModificationRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/modular_explosives");
	
	public ExplosionModificationRecipe(Identifier id) {
		super(id, "", false, UNLOCK_IDENTIFIER, PedestalRecipeTier.BASIC, collectIngredients(), Map.of(), ItemStack.EMPTY, 0.0F, 40, false, true);
	}
	
	private static List<IngredientStack> collectIngredients() {
		List<ItemConvertible> providers = new ArrayList<>();
		Registries.ITEM.stream().filter(item -> item instanceof ModularExplosionProvider).forEach(providers::add);
		IngredientStack providerIngredient = IngredientStack.of(Ingredient.ofItems(providers.toArray(new ItemConvertible[]{})));
		
		Set<Item> modifiers = ExplosionModifierProviders.getProviders();
		IngredientStack modifierIngredient = IngredientStack.of(Ingredient.ofItems(modifiers.toArray(new ItemConvertible[]{})));
		
		return List.of(providerIngredient, modifierIngredient);
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		ItemStack nonModStack = validateGridAndFindModularExplosiveStack(inventory);
		if (!(nonModStack.getItem() instanceof ModularExplosionProvider modularExplosionProvider)) {
			return false;
		}
		
		Pair<List<ExplosionArchetype>, List<ExplosionModifier>> pair = findArchetypeAndModifiers(inventory);
		ModularExplosionDefinition currentSet = ModularExplosionDefinition.getFromStack(nonModStack);
		List<ExplosionArchetype> archetypes = pair.getLeft();
		List<ExplosionModifier> mods = pair.getRight();

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
	public ItemStack craft(Inventory inventory, DynamicRegistryManager drm) {
		ItemStack output = validateGridAndFindModularExplosiveStack(inventory).copy();
		
		Pair<List<ExplosionArchetype>, List<ExplosionModifier>> pair = findArchetypeAndModifiers(inventory);
		List<ExplosionArchetype> archetypes = pair.getLeft();
		List<ExplosionModifier> mods = pair.getRight();
		
		if (archetypes.isEmpty() && mods.isEmpty()) { // clearing existing modifiers
			ModularExplosionDefinition.removeFromStack(output);
			return output;
		}
		
		ModularExplosionDefinition set = ModularExplosionDefinition.getFromStack(output);
		
		// adding new modifiers
		if (!archetypes.isEmpty()) {
			ExplosionArchetype newArchetype = calculateExplosionArchetype(set.getArchetype(), pair.getLeft());
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
			ItemStack slotStack = pedestal.getStack(slot);
			if (slotStack.getItem() instanceof ModularExplosionProvider) {
				pedestal.setStack(slot, ItemStack.EMPTY);
			} else {
				slotStack.decrement(1);
			}
		}
	}
	
	/**
	 * Iterates all stacks in the grid and returns the modular explosive
	 * if the grid only contains that one and modifiers
	 */
	public ItemStack validateGridAndFindModularExplosiveStack(Inventory inventory) {
		ItemStack foundStack = ItemStack.EMPTY;
		for (int slot : CRAFTING_GRID_SLOTS) {
			ItemStack stack = inventory.getStack(slot);
			if (!stack.isEmpty()
					&& stack.getItem() instanceof ModularExplosionProvider
					&& ExplosionModifierProviders.getModifier(stack) == null
					&& ExplosionModifierProviders.getArchetype(stack) == null) {
				
				if(foundStack == ItemStack.EMPTY) {
					foundStack = stack;
				} else {
					return ItemStack.EMPTY; // multiple non-mod stacks found
				}
			}
		}
		
		return foundStack;
	}
	
	public Pair<List<ExplosionArchetype>, List<ExplosionModifier>> findArchetypeAndModifiers(Inventory inventory) {
		List<ExplosionModifier> modifiers = new ArrayList<>();
		List<ExplosionArchetype> archetypes = new ArrayList<>();
		for (int slot : CRAFTING_GRID_SLOTS) {
			ItemStack stack = inventory.getStack(slot);
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
	
}
