package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.explosion.ExplosionEffectModifier;
import de.dafuqs.spectrum.explosion.ItemBoundModifier;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.ExplosiveArchetypeProvider;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;

import java.util.Map;
import java.util.Optional;

public class ExplosionModifierRecipe extends PedestalCraftingRecipe {

    public ExplosionModifierRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, PedestalRecipeTier tier, int width, int height, DefaultedList<IngredientStack> craftingInputs, Map<BuiltinGemstoneColor, Integer> gemstonePowderInputs, ItemStack output, float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades) {
        super(id, group, secret, requiredAdvancementIdentifier, tier, width, height, craftingInputs, gemstonePowderInputs, output, experience, craftingTime, skipRecipeRemainders, noBenefitsFromYieldUpgrades);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        if (super.matches(inv, world)) {
            var mod = getModifier((PedestalBlockEntity) inv, getRecipeOrientation(inv));
            if (mod.isEmpty())
                return false;

            if (!sanityCheck((PedestalBlockEntity) inv))
                return false;

            var explosionModifier = mod.get();
            var firstStack = getFirstNonModStack((PedestalBlockEntity) inv);

            if (firstStack.getItem() instanceof ExplosiveArchetypeProvider archetypeProvider && !explosionModifier.compatibleWithArchetype(archetypeProvider.getArchetype()))
                return false;

            var currentEffectModifiers = ExplosionEffectModifier.decodeStack(firstStack);
            return currentEffectModifiers.map(explosionModifier.family::canApplyTo).orElse(true);

        }
        return false;
    }

    @Override
    public ItemStack craftAndDecrement(Inventory inv) {
        if (inv instanceof PedestalBlockEntity pedestal) {
            Triplet<Integer, Integer, Boolean> orientation = getRecipeOrientation(inv);
            if (orientation == null) {
                return ItemStack.EMPTY;
            }

            var mod = getModifier(pedestal, orientation);
            var recipeOutput = getFirstNonModStack(pedestal).copy();
            decrementIngredientStacks(pedestal, orientation);

            recipeOutput.setCount(output.getCount());
            PlayerEntity player = pedestal.getOwnerIfOnline();
            if (player != null) {
                recipeOutput.onCraft(pedestal.getWorld(), player, recipeOutput.getCount());
            }
            
            var effectModifiers = ExplosionEffectModifier.decode(recipeOutput.getOrCreateNbt());
            effectModifiers.add(mod.get());
            ExplosionEffectModifier.encodeStack(recipeOutput, effectModifiers);
            
            return recipeOutput;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getFirstNonModStack(PedestalBlockEntity pedestal) {
        for (int i = 0; i < 10; i++) {
            var stack = pedestal.getStack(i);
            if (ItemBoundModifier.getFor(stack.getItem()).isEmpty())
                return stack;
        }
        SpectrumCommon.logError("Qhar ! ! ! This should not be possible. Something has gone horribly wrong with recipe " + id.toString() + " please revise!");
        return null;
    }

    public boolean sanityCheck(PedestalBlockEntity pedestal) {
        ItemStack lastStack = ItemStack.EMPTY;
        for (int i = 0; i < 9; i++) {
            var stack = pedestal.getStack(i);
            if (ItemBoundModifier.getFor(stack.getItem()).isPresent())
                continue;

            if (!stack.isOf(output.getItem())) {
                return false;
            }

            if (!ItemStack.canCombine(stack, lastStack) && !lastStack.isEmpty())
                return false;

            lastStack = stack;
        }

        return true;
    }

    public Optional<ItemBoundModifier> getModifier(PedestalBlockEntity pedestal, Triplet<Integer, Integer, Boolean> orientation) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                int invStackId = (x + orientation.getA()) + 3 * (y + orientation.getB());
                ItemStack invStack = pedestal.getStack(invStackId);

                if (!invStack.isEmpty()) {
                    var mod = ItemBoundModifier.getFor(invStack.getItem());
                    if (mod.isPresent())
                        return mod;
                }

            }
        }
        return Optional.empty();
    }
}
