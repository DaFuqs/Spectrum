package de.dafuqs.pigment.recipe.anvil_crushing;

import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AnvilCrushingRecipe implements Recipe<Inventory> {

    protected final Identifier id;
    Ingredient inputIngredient;
    protected final ItemStack outputItemStack;
    protected final float crushedItemsPerPointOfDamage;
    protected final float experience;
    protected final Identifier particleEffect;
    protected final Identifier soundEvent;

    public AnvilCrushingRecipe(Identifier id, Ingredient inputIngredient, ItemStack outputItemStack, float crushedItemsPerPointOfDamage, float experience, Identifier particleEffectIdentifier, Identifier soundEventIdentifier) {
        this.id = id;
        this.inputIngredient = inputIngredient;
        this.outputItemStack = outputItemStack;
        this.crushedItemsPerPointOfDamage = crushedItemsPerPointOfDamage;
        this.experience = experience;
        this.particleEffect = particleEffectIdentifier;
        this.soundEvent = soundEventIdentifier;
    }

    public boolean matches(Inventory inv, World world) {
        return this.inputIngredient.test(inv.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return outputItemStack.copy();
    }

    public ItemStack getRecipeKindIcon() {
        return new ItemStack(Blocks.CHIPPED_ANVIL);
    }

    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return PigmentRecipeTypes.ANVIL_CRUSHING_RECIPE_SERIALIZER;
    }

    public RecipeType<?> getType() {
        return PigmentRecipeTypes.ANVIL_CRUSHING;
    }

}
