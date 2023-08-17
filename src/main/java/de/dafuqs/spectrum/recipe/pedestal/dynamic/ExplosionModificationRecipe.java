package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;

public class ExplosionModificationRecipe extends PedestalCraftingRecipe {
    
    public static final RecipeSerializer<ExplosionModificationRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ExplosionModificationRecipe::new);
    
    public ExplosionModificationRecipe(Identifier id) {
        super(id, "", false, SpectrumCommon.locate("unlocks/blocks/advanced_explosives"), PedestalRecipeTier.BASIC, 1, 1, DefaultedList.ofSize(0), Map.of(), ItemStack.EMPTY, 0.0F, 200, false, true);
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
    public ItemStack craftAndDecrement(Inventory inv) {
        if (inv instanceof PedestalBlockEntity pedestal) {
            Triplet<Integer, Integer, Boolean> orientation = getRecipeOrientation(inv);
            if (orientation == null) {
                return ItemStack.EMPTY;
            }
    
            ExplosionModifier mod = findExplosionModifier(pedestal);
            ItemStack recipeOutput = getFirstNonModStack(pedestal).copy();
            decrementIngredientStacks(pedestal, orientation);
    
            PlayerEntity player = pedestal.getOwnerIfOnline();
            if (player != null) {
                recipeOutput.onCraft(pedestal.getWorld(), player, recipeOutput.getCount());
            }
    
            ExplosionModifierSet set = ExplosionModifierSet.getFromStack(recipeOutput);
            set.addModifier(mod);
            set.attachToStack(recipeOutput);
    
            return recipeOutput;
        }
    
        return ItemStack.EMPTY;
    }
    
    public ItemStack getFirstNonModStack(Inventory inventory) {
        for (int i = 0; i < PEDESTAL_CRAFTING_GRID_SIZE; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty() && ExplosionModifierProviders.get(stack) == null) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
    
    public @Nullable ExplosionModifier findExplosionModifier(Inventory inventory) {
        for (int i = 0; i < PEDESTAL_CRAFTING_GRID_SIZE; i++) {
            ItemStack stack = inventory.getStack(i);
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
