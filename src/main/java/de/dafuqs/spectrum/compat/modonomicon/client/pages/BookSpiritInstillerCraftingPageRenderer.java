package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconHelper;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.matchbooks.recipe.IngredientStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class BookSpiritInstillerCraftingPageRenderer extends BookGatedRecipePageRenderer<SpiritInstillerRecipe, BookGatedRecipePage<SpiritInstillerRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/spirit_instiller.png");
    private static final ItemStack ITEM_BOWL_STACK = SpectrumBlocks.ITEM_BOWL_CALCITE.asItem().getDefaultStack();

    public BookSpiritInstillerCraftingPageRenderer(BookGatedRecipePage<SpiritInstillerRecipe> page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 58;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, SpiritInstillerRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);

        renderTitle(drawContext, recipeY, second);

        // the ingredients
        List<IngredientStack> ingredients = recipe.getIngredientStacks();
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 3, recipeY + 8, mouseX, mouseY, ingredients.get(1)); // left
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 23, recipeY + 11, mouseX, mouseY, ingredients.get(0)); // center
        ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 44, recipeY + 8, mouseX, mouseY, ingredients.get(2)); // right

        // spirit instiller
        parentScreen.renderItemStack(drawContext, recipeX + 23, recipeY + 25, mouseX, mouseY, recipe.createIcon());

        // item bowls
        parentScreen.renderItemStack(drawContext, recipeX + 3, recipeY + 25, mouseX, mouseY, ITEM_BOWL_STACK);
        parentScreen.renderItemStack(drawContext, recipeX + 44, recipeY + 25, mouseX, mouseY, ITEM_BOWL_STACK);

        // the output
        parentScreen.renderItemStack(drawContext, recipeX + 79, recipeY + 8, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
    }

}
