package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.data.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class BookCrystallarieumGrowingPageRenderer extends BookGatedRecipePageRenderer<CrystallarieumRecipe, BookGatedRecipePage<CrystallarieumRecipe>> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/crystallarieum.png");
	private static final int LINE_HEIGHT = 9;

    private static BookTextHolder catalystText;
	private static BookTextHolder speedText;
	private static BookTextHolder inkDrainText;
	private static BookTextHolder depletionText;
    private BookTextHolder craftingTimeText1 = null;
    private BookTextHolder craftingTimeText2 = null;

    public BookCrystallarieumGrowingPageRenderer(BookGatedRecipePage<CrystallarieumRecipe> page) {
        super(page);
		
		ResourceLocation font = BookDataManager.Client.get().safeFont(this.page.getBook().getFont());
		
		if (catalystText == null) {
			catalystText = new BookTextHolder(Component.translatable("container.spectrum.rei.crystallarieum.catalyst").withStyle(s -> s.withFont(font)));
			speedText = new BookTextHolder(Component.translatable("container.spectrum.rei.crystallarieum.speed").withStyle(s -> s.withFont(font)));
			inkDrainText = new BookTextHolder(Component.translatable("container.spectrum.rei.crystallarieum.ink_drain").withStyle(s -> s.withFont(font)));
			depletionText = new BookTextHolder(Component.translatable("container.spectrum.rei.crystallarieum.depletion").withStyle(s -> s.withFont(font)));
        }

        if (page.getRecipe1() != null) {
			craftingTimeText1 = new BookTextHolder(Component.translatable(page.getRecipe1().value().growsWithoutCatalyst()
					? "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_optional"
					: "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", page.getRecipe1().value().getSecondsPerGrowthStage()).withStyle(s -> s.withFont(font)));
		}
		
		if (page.getRecipe2() != null) {
			craftingTimeText2 = new BookTextHolder(Component.translatable(page.getRecipe2().value().growsWithoutCatalyst()
					? "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_optional"
					: "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", page.getRecipe2().value().getSecondsPerGrowthStage()).withStyle(s -> s.withFont(font)));
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 100;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<CrystallarieumRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		CrystallarieumRecipe recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		recipeY += 2;
		
		RenderSystem.enableBlend();
		
		renderTitle(drawContext, recipeY, second);
		
		// the ingredient
		int startX = 26;
		int offsetPerReagent = 18;
		Ingredient ingredient = recipe.getIngredientStack();
		parentScreen.renderIngredient(drawContext, recipeX + startX, recipeY + 5, mouseX, mouseY, ingredient);
		parentScreen.renderIngredient(drawContext, recipeX + startX - offsetPerReagent - 4, recipeY + 5, mouseX, mouseY, FluidRendering.fluidIngredientAsBucket(recipe.getFluidIngredient()));
		drawContext.blit(BACKGROUND_TEXTURE, recipeX + startX - offsetPerReagent - 7, recipeY + 1, 0, 0, 53, 25, 128, 128);
		
		
		// growth stages
		Iterator<BlockState> it = recipe.getGrowthStages().iterator();
		BlockState growthState = it.next();
		parentScreen.renderItemStack(drawContext, recipeX + startX + offsetPerReagent, recipeY - 1, mouseX, mouseY, growthState.getBlock().asItem().getDefaultInstance());
		int x = 0;
		while (it.hasNext()) {
			parentScreen.renderItemStack(drawContext, recipeX + 62 + offsetPerReagent * x, recipeY + 4, mouseX, mouseY, it.next().getBlock().asItem().getDefaultInstance());
            x++;
        }

        // crystallarieum
        parentScreen.renderItemStack(drawContext, recipeX + startX + offsetPerReagent, recipeY + 8, mouseX, mouseY, CrystallarieumBlock.withColor(SpectrumBlocks.CRYSTALLARIEUM.asItem().getDefaultInstance(), recipe.getInkColor()));

        // catalyst text
        renderBookTextHolder(drawContext, catalystText, 0, 42, BookEntryScreen.PAGE_WIDTH);
		renderBookTextHolder(drawContext, speedText, 0, 45 + LINE_HEIGHT, BookEntryScreen.PAGE_WIDTH);
		renderBookTextHolder(drawContext, inkDrainText, 0, 45 + LINE_HEIGHT * 2, BookEntryScreen.PAGE_WIDTH);
		renderBookTextHolder(drawContext, depletionText, 0, 45 + LINE_HEIGHT * 3, BookEntryScreen.PAGE_WIDTH);
		renderBookTextHolder(drawContext, second ? craftingTimeText2 : craftingTimeText1, 0, 82, BookEntryScreen.PAGE_WIDTH);
		
		// the catalysts
		x = 0;
		recipeY += 4;
        for (CrystallarieumCatalyst catalyst : recipe.getCatalysts()) {
            int offsetX = recipeX + startX + offsetPerReagent * x;
            parentScreen.renderIngredient(drawContext, recipeX + startX + offsetPerReagent * x, recipeY + 27, mouseX, mouseY, catalyst.ingredient());

            RenderSystem.enableBlend();
			
			int offsetU = CrystallarieumRecipe.growthSpeedOffsetU(catalyst);
			drawContext.blit(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 45, 7, 7, offsetU, CrystallarieumRecipe.GROWTH_SPEED_V, 7, 7, 128, 128);
			
			offsetU = CrystallarieumRecipe.consumptionOffsetU(catalyst, offsetU);
			drawContext.blit(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 54, 7, 7, offsetU, CrystallarieumRecipe.CONSUMPTION_V, 7, 7, 128, 128);
			
			offsetU = CrystallarieumRecipe.consumeChanceOffsetU(catalyst, offsetU);
			drawContext.blit(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 63, 7, 7, offsetU, CrystallarieumRecipe.CONSUME_CHANCE_V, 7, 7, 128, 128);

            x++;
        }
    }
	
}
