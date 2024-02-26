package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.helpers.NullableDyeColor;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumCatalyst;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Iterator;

public class BookCrystallarieumGrowingPageRenderer extends BookGatedRecipePageRenderer<CrystallarieumRecipe, BookGatedRecipePage<CrystallarieumRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/crystallarieum.png");

    private static BookTextHolder catalystText;

    public BookCrystallarieumGrowingPageRenderer(BookGatedRecipePage<CrystallarieumRecipe> page) {
        super(page);

        if (catalystText == null) {
            catalystText = new BookTextHolder(Text.translatable("container.spectrum.modonomicon.crystallarieum.catalyst"));
        }
    }

    @Override
    protected int getRecipeHeight() {
        return 90;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, CrystallarieumRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 3, recipeY, 0, 0, 53, 25, 128, 128);

        renderTitle(drawContext, recipeY, second);

        // the ingredient
        Ingredient ingredient = recipe.getIngredientStack();
        parentScreen.renderIngredient(drawContext, recipeX, recipeY + 5, mouseX, mouseY, ingredient);

        // growth stages
        Iterator<BlockState> it = recipe.getGrowthStages().iterator();
        BlockState growthState = it.next();
        parentScreen.renderItemStack(drawContext, recipeX + 23, recipeY - 2, mouseX, mouseY, growthState.getBlock().asItem().getDefaultStack());
        int x = 0;
        while (it.hasNext()) {
            parentScreen.renderItemStack(drawContext, recipeX + 52 + 16 * x, recipeY + 4, mouseX, mouseY, it.next().getBlock().asItem().getDefaultStack());
            x++;
        }

        // crystallarieum
        parentScreen.renderItemStack(drawContext, recipeX + 23, recipeY + 8, mouseX, mouseY, SpectrumBlocks.CRYSTALLARIEUM.asStackWithColor(NullableDyeColor.get(recipe.getInkColor().getDyeColor())));

        // catalyst text
        renderBookTextHolder(drawContext, catalystText, 0, 38, BookContentScreen.PAGE_WIDTH);

        // the catalysts
        x = 0;
        int startX = 26;
        int offsetPerReagent = 18;
        for (CrystallarieumCatalyst catalyst : recipe.getCatalysts()) {
            int offsetX = recipeX + startX + offsetPerReagent * x;
            parentScreen.renderIngredient(drawContext, recipeX + startX + offsetPerReagent * x, recipeY + 27, mouseX, mouseY, catalyst.ingredient);

            float growthAcceleration = catalyst.growthAccelerationMod;
            float inkConsumption = catalyst.inkConsumptionMod;
            float consumeChance = catalyst.consumeChancePerSecond;

            RenderSystem.enableBlend();
            int offsetU = growthAcceleration == 1 ? 97 : growthAcceleration >= 6 ? 85 : growthAcceleration > 1 ? 67 : growthAcceleration <= 0.25 ? 79 : 73;
            drawContext.drawTexture(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 45, offsetU, 6, 6, 6, 128, 128);

            offsetU = inkConsumption == 1 ? 97 : inkConsumption >= 8 ? 85 : inkConsumption > 1 ? 67 : inkConsumption <= 0.25 ? 79 : 73;
            drawContext.drawTexture(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 54, offsetU, 6, 6, 6, 128, 128);

            offsetU = consumeChance == 0 ? 97 : consumeChance >= 0.2 ? 85 : consumeChance >= 0.05 ? 67 : 91;
            drawContext.drawTexture(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 63, offsetU, 6, 6, 6, 128, 128);

            x++;
        }
    }

}
