package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.gui.GuiBook;

import java.util.ArrayList;
import java.util.List;

public class BookEnchanterUpgradingPageRenderer extends BookGatedRecipePageRenderer<EnchantmentUpgradeRecipe, BookGatedRecipePage<EnchantmentUpgradeRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/enchanter_crafting.png");

    public BookEnchanterUpgradingPageRenderer(BookGatedRecipePage<EnchantmentUpgradeRecipe> page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 94;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, EnchantmentUpgradeRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX, recipeY, 0, 0, 100, 80, 256, 256);

        renderTitle(drawContext, recipeY, second);

        // the ingredients
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();

        int ingredientX = recipeX - 3;

        // surrounding input slots
        List<ItemStack> inputStacks = new ArrayList<>();
        int requiredItemCountSplit = recipe.getRequiredItemCount() / 8;
        int requiredItemCountModulo = recipe.getRequiredItemCount() % 8;
        for (int i = 0; i < 8; i++) {
            int addAmount = i < requiredItemCountModulo ? 1 : 0;
            inputStacks.add(new ItemStack(recipe.getRequiredItem(), requiredItemCountSplit + addAmount));
        }

        parentScreen.renderItemStack(drawContext, ingredientX + 16, recipeY, mouseX, mouseY, inputStacks.get(0));
        parentScreen.renderItemStack(drawContext, ingredientX + 40, recipeY, mouseX, mouseY, inputStacks.get(1));
        parentScreen.renderItemStack(drawContext, ingredientX + 56, recipeY + 16, mouseX, mouseY, inputStacks.get(2));
        parentScreen.renderItemStack(drawContext, ingredientX + 56, recipeY + 40, mouseX, mouseY, inputStacks.get(3));
        parentScreen.renderItemStack(drawContext, ingredientX + 40, recipeY + 56, mouseX, mouseY, inputStacks.get(4));
        parentScreen.renderItemStack(drawContext, ingredientX + 16, recipeY + 56, mouseX, mouseY, inputStacks.get(5));
        parentScreen.renderItemStack(drawContext, ingredientX, recipeY + 40, mouseX, mouseY, inputStacks.get(6));
        parentScreen.renderItemStack(drawContext, ingredientX, recipeY + 16, mouseX, mouseY, inputStacks.get(7));

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
