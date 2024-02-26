package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class BookEnchanterCraftingPageRenderer extends BookGatedRecipePageRenderer<EnchanterRecipe, BookGatedRecipePage<EnchanterRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/enchanter_crafting.png");

    public BookEnchanterCraftingPageRenderer(BookGatedRecipePage<EnchanterRecipe> page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 94;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, EnchanterRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX, recipeY, 0, 0, 100, 80, 256, 256);

        renderTitle(drawContext, recipeY, second);

        // the ingredients
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();

        int ingredientX = recipeX - 3;

        // surrounding input slots
        parentScreen.renderIngredient(drawContext, ingredientX + 16, recipeY, mouseX, mouseY, ingredients.get(1));
        parentScreen.renderIngredient(drawContext, ingredientX + 40, recipeY, mouseX, mouseY, ingredients.get(2));
        parentScreen.renderIngredient(drawContext, ingredientX + 56, recipeY + 16, mouseX, mouseY, ingredients.get(3));
        parentScreen.renderIngredient(drawContext, ingredientX + 56, recipeY + 40, mouseX, mouseY, ingredients.get(4));
        parentScreen.renderIngredient(drawContext, ingredientX + 40, recipeY + 56, mouseX, mouseY, ingredients.get(5));
        parentScreen.renderIngredient(drawContext, ingredientX + 16, recipeY + 56, mouseX, mouseY, ingredients.get(6));
        parentScreen.renderIngredient(drawContext, ingredientX, recipeY + 40, mouseX, mouseY, ingredients.get(7));
        parentScreen.renderIngredient(drawContext, ingredientX, recipeY + 16, mouseX, mouseY, ingredients.get(8));

        // center input slot
        parentScreen.renderIngredient(drawContext, ingredientX + 28, recipeY + 28, mouseX, mouseY, ingredients.get(0));

        // Knowledge Gem and Enchanter
        ItemStack knowledgeDropStackWithXP = KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true);
        parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 9, mouseX, mouseY, knowledgeDropStackWithXP);
        parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 46, mouseX, mouseY, SpectrumBlocks.ENCHANTER.asItem().getDefaultStack());

        // the output
        parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 31, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
    }

}
