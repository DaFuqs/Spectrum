package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.recipe.cinderhearth.CinderhearthRecipe;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BookCinderhearthSmeltingPageRenderer extends BookGatedRecipePageRenderer<CinderhearthRecipe, BookGatedRecipePage<CinderhearthRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/cinderhearth.png");

    private final List<BookTextHolder> chanceTexts1;
    private final List<BookTextHolder> chanceTexts2;

    public BookCinderhearthSmeltingPageRenderer(BookGatedRecipePage<CinderhearthRecipe> page) {
        super(page);

        chanceTexts1 = createChanceTexts(page.getRecipe1());
        chanceTexts2 = createChanceTexts(page.getRecipe2());
    }

    private List<BookTextHolder> createChanceTexts(CinderhearthRecipe recipe) {
        if (recipe == null) return null;

        World world = parentScreen.getMinecraft().world;
        if (world == null) return null;

        List<BookTextHolder> chanceTexts = new ArrayList<>();
        List<Pair<ItemStack, Float>> possibleOutputs = recipe.getOutputsWithChance(world.getRegistryManager());

        int chanceTextIndex = 0;
        for (Pair<ItemStack, Float> possibleOutput : possibleOutputs) {
            if (possibleOutput.getRight() < 1.0F) {
                if (chanceTexts.size() < chanceTextIndex + 1) {
                    chanceTexts.add(new BookTextHolder(Text.literal(String.format("%f.2%%", possibleOutput.getRight() * 100))));
                }
                chanceTextIndex++;
            }
        }

        return chanceTexts;
    }

    @Override
    protected int getRecipeHeight() {
        return 58;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, CinderhearthRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();

        List<Pair<ItemStack, Float>> possibleOutputs = recipe.getOutputsWithChance(world.getRegistryManager());
        recipeX = Math.max(recipeX, recipeX + 26 - possibleOutputs.size() * 10);

        int backgroundTextureWidth = 34 + possibleOutputs.size() * 24;
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 1, recipeY - 2, 0, 0, backgroundTextureWidth, 45, 128, 128);

        renderTitle(drawContext, recipeY, second);

        // the ingredient
        Ingredient ingredient = recipe.getIngredients().get(0);
        parentScreen.renderIngredient(drawContext, recipeX + 2, recipeY + 7, mouseX, mouseY, ingredient);

        // cinderhearth
        parentScreen.renderItemStack(drawContext, recipeX + 21, recipeY + 26, mouseX, mouseY, recipe.createIcon());

        // outputs
        int chanceTextIndex = 0;
        for (int i = 0; i < possibleOutputs.size(); i++) {
            Pair<ItemStack, Float> possibleOutput = possibleOutputs.get(i);
            int x = recipeX + 37 + i * 23;
            parentScreen.renderItemStack(drawContext, x, recipeY + 6, mouseX, mouseY, possibleOutput.getLeft());

            if (possibleOutput.getRight() < 1.0F) {
                BookTextHolder chanceText = second ? chanceTexts2.get(chanceTextIndex) : chanceTexts1.get(chanceTextIndex);
                renderBookTextHolder(drawContext, chanceText, x, recipeY + 24, BookContentScreen.PAGE_WIDTH);
                chanceTextIndex++;
            }
        }
    }

}
