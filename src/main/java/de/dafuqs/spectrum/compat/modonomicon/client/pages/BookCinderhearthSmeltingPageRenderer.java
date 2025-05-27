package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.data.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class BookCinderhearthSmeltingPageRenderer extends BookGatedRecipePageRenderer<CinderhearthRecipe, BookGatedRecipePage<CinderhearthRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/cinderhearth.png");

    private List<BookTextHolder> chanceTexts1 = null;
    private List<BookTextHolder> chanceTexts2 = null;

    public BookCinderhearthSmeltingPageRenderer(BookGatedRecipePage<CinderhearthRecipe> page) {
        super(page);
    }

    @Override
    public void onBeginDisplayPage(BookEntryScreen parentScreen, int left, int top) {
        super.onBeginDisplayPage(parentScreen, left, top);

        if (chanceTexts1 == null) {
			if (page.getRecipe1() != null)
            	chanceTexts1 = createChanceTexts(page.getRecipe1().value());
        }
        if (chanceTexts2 == null) {
			if (page.getRecipe2() != null)
            	chanceTexts2 = createChanceTexts(page.getRecipe2().value());
        }
    }

    private List<BookTextHolder> createChanceTexts(CinderhearthRecipe recipe) {
        if (recipe == null) return null;

        World world = MinecraftClient.getInstance().world;
        if (world == null) return null;
        
        Identifier font = BookDataManager.Client.get().safeFont(this.page.getBook().getFont());

        List<BookTextHolder> chanceTexts = new ArrayList<>();
        List<Pair<ItemStack, Float>> possibleOutputs = recipe.getResultsWithChance();

        int chanceTextIndex = 0;
        for (Pair<ItemStack, Float> possibleOutput : possibleOutputs) {
            if (possibleOutput.getRight() < 1.0F) {
                if (chanceTexts.size() < chanceTextIndex + 1) {
                    chanceTexts.add(new BookTextHolder(Text.literal(String.format("%f.2%%", possibleOutput.getRight() * 100)).styled(s -> s.withFont(font))));
                }
                chanceTextIndex++;
            }
        }

        return chanceTexts;
    }

    @Override
    protected int getRecipeHeight() {
        return 54;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, RecipeEntry<CinderhearthRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        CinderhearthRecipe recipe = recipeEntry.value();
        World world = MinecraftClient.getInstance().world;
        if (world == null) return;

        RenderSystem.enableBlend();

        List<Pair<ItemStack, Float>> possibleOutputs = recipe.getResultsWithChance();
        recipeX = Math.max(recipeX, recipeX + 26 - possibleOutputs.size() * 10);

        int backgroundTextureWidth = 34 + possibleOutputs.size() * 24;
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 1, recipeY - 2, 0, 0, backgroundTextureWidth, 45, 128, 128);

        renderTitle(drawContext, recipeY, second);

        // the ingredient
        var ingredientStack = recipe.getIngredientStacks().getFirst();
        parentScreen.renderIngredient(drawContext, recipeX + 2, recipeY + 7, mouseX, mouseY, ingredientStack.getIngredient(), ingredientStack.getCount());

        // cinderhearth
        parentScreen.renderItemStack(drawContext, recipeX + 21, recipeY + 26, mouseX, mouseY, recipe.createIcon());

        // outputs
        int chanceTextIndex = 0;
        for (int i = 0; i < possibleOutputs.size(); i++) {
            Pair<ItemStack, Float> possibleOutput = possibleOutputs.get(i);
            int x = recipeX + 37 + i * 23;
            parentScreen.renderItemStack(drawContext, x, recipeY + 6, mouseX, mouseY, possibleOutput.getLeft());
			
            if (possibleOutput.getRight() < 1.0F) {
				var chance = second ? chanceTexts2 : chanceTexts1;
				
				if (chance == null)
					continue;
				
				BookTextHolder chanceText = chance.get(chanceTextIndex);
                renderBookTextHolder(drawContext, chanceText, x, recipeY + 24, BookEntryScreen.PAGE_WIDTH);
                chanceTextIndex++;
            }
        }
    }

}
