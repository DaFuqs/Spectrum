package de.dafuqs.pigment.recipe;

import de.dafuqs.pigment.blocks.PigmentBlocks;
import de.dafuqs.pigment.enums.GemColor;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.HashMap;

public class AltarCraftingRecipe implements Recipe<Inventory> {

    protected final Identifier id;
    protected final String group;

    protected final int width;
    protected final int height;

    protected final int tier;
    protected final DefaultedList<Ingredient> craftingInputs;
    protected final HashMap<GemColor, Integer> gemInputs;
    protected final ItemStack output;
    protected final float experience;
    protected final int craftingTime;

    public AltarCraftingRecipe(Identifier id, String group, int tier, int width, int height, DefaultedList<Ingredient> craftingInputs, HashMap<GemColor, Integer> gemInputs, ItemStack output, float experience, int craftingTime) {
        this.id = id;
        this.group = group;
        this.tier = tier;

        this.width = width;
        this.height = height;

        this.craftingInputs = craftingInputs;
        this.gemInputs = gemInputs;
        this.output = output;
        this.experience = experience;
        this.craftingTime = craftingTime;

    }

    @Override
    public boolean matches(Inventory inv, World world) {
        for(int i = 0; i < 9; i++) {
            if(!this.craftingInputs.get(i).test(inv.getStack(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return this.output.copy();
    }

    public DefaultedList<Ingredient> getPreviewInputs() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.addAll(this.craftingInputs);
        return defaultedList;
    }


    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(PigmentBlocks.ALTAR);
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PigmentRecipeTypes.ALTAR_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return PigmentRecipeTypes.ALTAR;
    }

    public int getGemColor(GemColor gemColor) {
        return gemInputs.getOrDefault(gemColor, 0);
    }

    public int getCraftingTime() {
        return craftingTime;
    }
}
