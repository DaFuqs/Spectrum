package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;

public class ExplosionModifierRecipe extends PedestalCraftingRecipe {

    public ExplosionModifierRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, PedestalRecipeTier tier, int width, int height, DefaultedList<IngredientStack> craftingInputs, Map<BuiltinGemstoneColor, Integer> gemstonePowderInputs, ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades) {
        super(id, group, secret, requiredAdvancementIdentifier, tier, width, height, craftingInputs, gemstonePowderInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
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
        return currentModifiers.canAcceptModifier(newModifier);
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
        for (int i = 0; i < 9; i++) {
            var stack = inventory.getStack(i);
            if (ExplosionModifierProviders.get(stack) == null) {
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
