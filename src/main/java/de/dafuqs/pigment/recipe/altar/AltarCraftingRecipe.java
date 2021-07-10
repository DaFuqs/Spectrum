package de.dafuqs.pigment.recipe.altar;

import de.dafuqs.pigment.registries.PigmentBlocks;
import de.dafuqs.pigment.enums.PigmentColor;
import de.dafuqs.pigment.registries.PigmentItems;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AltarCraftingRecipe implements Recipe<Inventory> {

    protected final Identifier id;
    @Nullable
    protected final String group;

    protected final int width;
    protected final int height;

    protected final int tier;
    protected final DefaultedList<Ingredient> craftingInputs;
    protected final HashMap<PigmentColor, Integer> pigmentInputs;
    protected final ItemStack output;
    protected final float experience;
    protected final int craftingTime;

    @Nullable
    protected final Identifier advancementIdentifier;

    public AltarCraftingRecipe(Identifier id, String group, int tier, int width, int height, DefaultedList<Ingredient> craftingInputs, HashMap<PigmentColor, Integer> pigmentInputs, ItemStack output, float experience, int craftingTime, Identifier advancementIdentifier) {
        this.id = id;
        this.group = group;
        this.tier = tier;

        this.width = width;
        this.height = height;

        this.craftingInputs = craftingInputs;
        this.pigmentInputs = pigmentInputs;
        this.output = output;
        this.experience = experience;
        this.craftingTime = craftingTime;
        this.advancementIdentifier = advancementIdentifier;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        if(!matchesGrid(inv)) {
            return false;
        }

        int magentaPigmentCount = this.pigmentInputs.get(PigmentColor.MAGENTA);
        int yellowPigmentCount = this.pigmentInputs.get(PigmentColor.YELLOW);
        int cyanPigmentCount = this.pigmentInputs.get(PigmentColor.CYAN);
        int blackPigmentCount = this.pigmentInputs.get(PigmentColor.BLACK);
        int whitePigmentCount = this.pigmentInputs.get(PigmentColor.WHITE);

        return ((magentaPigmentCount == 0 || isStackAtLeast(inv.getStack(9), PigmentItems.MAGENTA_GEMSTONE_POWDER, magentaPigmentCount))
            && (yellowPigmentCount == 0 || isStackAtLeast(inv.getStack(10), PigmentItems.YELLOW_GEMSTONE_POWDER, yellowPigmentCount))
            && (cyanPigmentCount == 0 || isStackAtLeast(inv.getStack(11), PigmentItems.CYAN_GEMSTONE_POWDER, cyanPigmentCount))
            && (blackPigmentCount == 0 || isStackAtLeast(inv.getStack(12), PigmentItems.BLACK_GEMSTONE_POWDER, blackPigmentCount))
            && (whitePigmentCount == 0 || isStackAtLeast(inv.getStack(13), PigmentItems.WHITE_GEMSTONE_POWDER, magentaPigmentCount)));
    }

    public boolean matchesGrid(Inventory inv) {
        for(int i = 0; i <= 3 - this.width; ++i) {
            for(int j = 0; j <= 3 - this.height; ++j) {
                if (this.matchesPattern(inv, i, j, true)) {
                    return true;
                }

                if (this.matchesPattern(inv, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean matchesPattern(Inventory inv, int offsetX, int offsetY, boolean flipped) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (flipped) {
                        ingredient = this.craftingInputs.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.craftingInputs.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(inv.getStack(i + j * 3))) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isStackAtLeast(ItemStack sourceItemStack, Item item, int amount) {
        return sourceItemStack.getItem().equals(item) && sourceItemStack.getCount() >= amount;
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
    public ItemStack createIcon() {
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

    public int getPigmentColor(PigmentColor pigmentColor) {
        return pigmentInputs.getOrDefault(pigmentColor, 0);
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    public float getExperience() {
        return this.experience;
    }

    public Identifier unlockedAdvancement() {
        return advancementIdentifier;
    }

    public boolean unlocksAdvancement() {
        return advancementIdentifier != null;
    }

    // REI COMPAT
    public DefaultedList<Ingredient> getCraftingInputs() {
        return this.craftingInputs;
    }

    public HashMap<PigmentColor, Integer> getPigmentInputs() {
        return this.pigmentInputs;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

}
