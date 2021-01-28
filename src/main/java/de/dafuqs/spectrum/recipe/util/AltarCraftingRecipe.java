package de.dafuqs.spectrum.recipe.util;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.inventories.AltarBlockInventory;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.HashMap;

public class AltarCraftingRecipe implements Recipe<AltarBlockInventory> {

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
    public boolean matches(AltarBlockInventory inv, World world) {
        return false;
    }

    @Override
    public ItemStack craft(AltarBlockInventory inv) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return null;
    }

    @Override
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(SpectrumBlocks.ALTAR);
    }

    @Override
    public Identifier getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpectrumRecipeTypes.ALTAR_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return SpectrumRecipeTypes.ALTAR_RECIPE_TYPE;
    }

    public int getGemColor(GemColor gemColor) {
        return gemInputs.getOrDefault(gemColor, 0);
    }
}
