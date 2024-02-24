package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookEntry;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.compat.patchouli.PatchouliHelper;
import de.dafuqs.spectrum.recipe.pedestal.PedestalRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalRecipeTier;
import de.dafuqs.spectrum.recipe.pedestal.color.GemstoneColor;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.List;

public class BookPedestalCraftingPageRenderer extends BookGatedRecipePageRenderer<PedestalRecipe, BookGatedRecipePage<PedestalRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE1 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting1.png");
    private static final Identifier BACKGROUND_TEXTURE2 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting2.png");
    private static final Identifier BACKGROUND_TEXTURE3 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting3.png");
    private static final Identifier BACKGROUND_TEXTURE4 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting4.png");

    public BookPedestalCraftingPageRenderer(BookGatedRecipePage<PedestalRecipe> page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 110;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, PedestalRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(getBackgroundTextureForTier(recipe.getTier()), recipeX - 2, recipeY - 2, 0, 0, 106, 97, 128, 256);

        super.renderTitle(drawContext, recipeY, second);

        // the output
        parentScreen.renderItemStack(drawContext, recipeX + 78, recipeY + 22, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));

        // the powders
        switch (recipe.getTier()) {
            case COMPLEX -> drawGemstonePowderSlots(drawContext, recipe, recipe.getTier().getAvailableGemstoneColors(), 3, recipeX, recipeY, mouseX, mouseY);
            case ADVANCED -> drawGemstonePowderSlots(drawContext, recipe, recipe.getTier().getAvailableGemstoneColors(),12, recipeX, recipeY, mouseX, mouseY);
            default -> drawGemstonePowderSlots(drawContext, recipe, recipe.getTier().getAvailableGemstoneColors(),22, recipeX, recipeY, mouseX, mouseY);
        }

        // the ingredients
        List<IngredientStack> ingredients = recipe.getIngredientStacks();
        int wrap = recipe.getWidth();
        for (int i = 0; i < ingredients.size(); i++) {
            renderIngredientStack(drawContext, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
        }
    }

    @Contract(pure = true)
    private Identifier getBackgroundTextureForTier(@NotNull PedestalRecipeTier pedestalRecipeTier) {
        return switch (pedestalRecipeTier) {
            case BASIC -> BACKGROUND_TEXTURE1;
            case SIMPLE -> BACKGROUND_TEXTURE2;
            case ADVANCED -> BACKGROUND_TEXTURE3;
            default -> BACKGROUND_TEXTURE4;
        };
    }

    private void drawGemstonePowderSlots(DrawContext drawContext, PedestalRecipe recipe, GemstoneColor @NotNull [] colors, int startX, int recipeX, int recipeY, int mouseX, int mouseY) {
        int h = 0;
        for (GemstoneColor color : colors) {
            int amount = recipe.getPowderInputs().getOrDefault(color, 0);
            if (amount > 0) {
                ItemStack stack = color.getGemstonePowderItem().getDefaultStack();
                stack.setCount(amount);
                parentScreen.renderItemStack(drawContext, recipeX + startX + h * 19, recipeY + 72, mouseX, mouseY, stack);
            }
            h++;
        }
    }

    public void renderIngredientStack(DrawContext drawContext, int x, int y, int mouseX, int mouseY, IngredientStack ingredientStack) {
        List<ItemStack> stacks = ingredientStack.getStacks();
        if (!stacks.isEmpty()) {
            parentScreen.renderItemStack(drawContext, x, y, mouseX, mouseY, stacks.get(parentScreen.ticksInBook / 20 % stacks.size()));
        }
    }

}
