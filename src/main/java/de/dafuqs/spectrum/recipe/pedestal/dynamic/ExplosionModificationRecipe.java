package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExplosionModificationRecipe extends ShapelessPedestalRecipe {
	
	public static final RecipeSerializer<ExplosionModificationRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ExplosionModificationRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/advanced_explosives");
	
	public ExplosionModificationRecipe(Identifier id) {
		super(id, "", false, UNLOCK_IDENTIFIER, PedestalRecipeTier.BASIC, collectIngredients(), Map.of(), ItemStack.EMPTY, 0.0F, 40, false, true);
	}
	
	private static List<IngredientStack> collectIngredients() {
		List<ItemConvertible> providers = new ArrayList<>();
		Registry.ITEM.stream().filter(item -> item instanceof ExplosionArchetypeProvider).forEach(providers::add);
		IngredientStack providerIngredient = IngredientStack.of(Ingredient.ofItems(providers.toArray(new ItemConvertible[]{})));
		
		Set<Item> modifiers = ExplosionModifierProviders.getProviders();
		IngredientStack modifierIngredient = IngredientStack.of(Ingredient.ofItems(modifiers.toArray(new ItemConvertible[]{})));
		
		return List.of(providerIngredient, modifierIngredient);
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		ItemStack nonModStack = getFirstNonModStack(inventory);
		if (!(nonModStack.getItem() instanceof ExplosionArchetypeProvider archetypeProvider)) {
			return false;
		}
		
		ExplosionModifier newModifier = findExplosionModifier(inventory);
		if (newModifier == null) {
			return false;
		}
		
		if (!newModifier.isCompatibleWithArchetype(archetypeProvider.getArchetype())) {
			return false;
		}
		
		if (!enoughPowderPresent(inventory)) {
			return false;
		}
		
		ExplosionModifierSet currentModifiers = ExplosionModifierSet.getFromStack(nonModStack);
		return currentModifiers.canAcceptModifier(newModifier, archetypeProvider);
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		ExplosionModifier mod = findExplosionModifier(inv);
		ItemStack recipeOutput = getFirstNonModStack(inv).copy();
		
		ExplosionModifierSet set = ExplosionModifierSet.getFromStack(recipeOutput);
		set.addModifier(mod);
		set.attachToStack(recipeOutput);
		
		return recipeOutput;
	}
	
	@Override
	public void consumeIngredients(PedestalBlockEntity pedestal) {
		for (int slot : CRAFTING_GRID_SLOTS) {
			ItemStack slotStack = pedestal.getStack(slot);
			if (slotStack.getItem() instanceof ExplosionArchetypeProvider) {
				pedestal.setStack(slot, ItemStack.EMPTY);
			} else {
				slotStack.decrement(1);
			}
		}
	}
	
	public ItemStack getFirstNonModStack(Inventory inventory) {
		for (int slot : CRAFTING_GRID_SLOTS) {
			ItemStack stack = inventory.getStack(slot);
			if (!stack.isEmpty() && ExplosionModifierProviders.get(stack) == null) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	public @Nullable ExplosionModifier findExplosionModifier(Inventory inventory) {
		for (int slot : CRAFTING_GRID_SLOTS) {
			ItemStack stack = inventory.getStack(slot);
			if (!stack.isEmpty()) {
				ExplosionModifier modifier = ExplosionModifierProviders.get(stack);
				if (modifier != null) {
					return modifier;
				}
			}
		}
		return null;
	}
	
}
