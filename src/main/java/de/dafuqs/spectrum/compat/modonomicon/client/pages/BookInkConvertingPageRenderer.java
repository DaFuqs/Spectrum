package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.data.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class BookInkConvertingPageRenderer extends BookGatedRecipePageRenderer<InkConvertingRecipe, BookGatedRecipePage<InkConvertingRecipe>> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/ink_converting.png");
	private BookTextHolder inkText1;
	private BookTextHolder inkText2;
	private BookTextHolder amountText1;
	private BookTextHolder amountText2;
	
	public BookInkConvertingPageRenderer(BookGatedRecipePage<InkConvertingRecipe> page) {
		super(page);
		
		ResourceLocation font = BookDataManager.Client.get().safeFont(this.page.getBook().getFont());
		
		InkConvertingRecipe recipe1 = page.getRecipe1().value();
		if (recipe1 != null) {
			Component colorText = Component.translatable("container.spectrum.rei.ink_converting.color", recipe1.getInkColor().getName()).withStyle(s -> s.withFont(font));
			Component amountText = Component.translatable("container.spectrum.rei.ink_converting.amount", recipe1.getInkAmount()).withStyle(s -> s.withFont(font));
			inkText1 = new BookTextHolder(colorText);
			amountText1 = new BookTextHolder(amountText);
		}
		
		InkConvertingRecipe recipe2 = page.getRecipe2().value();
		if (recipe2 != null) {
			Component colorText = Component.translatable("container.spectrum.rei.ink_converting.color", recipe2.getInkColor().getName()).withStyle(s -> s.withFont(font));
			Component amountText = Component.translatable("container.spectrum.rei.ink_converting.amount", recipe2.getInkAmount()).withStyle(s -> s.withFont(font));
			inkText2 = new BookTextHolder(colorText);
			amountText2 = new BookTextHolder(amountText);
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 34;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<InkConvertingRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		InkConvertingRecipe recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		
		RenderSystem.enableBlend();
		drawContext.blit(BACKGROUND_TEXTURE, recipeX, recipeY, 0, 0, 35, 30, 128, 128);
		
		if (!second) {
			renderTitle(drawContext, recipeY, second);
		}
		
		// the ingredient
		parentScreen.renderIngredient(drawContext, recipeX + 3, recipeY + 3, mouseX, mouseY, recipe.getIngredients().getFirst());
		
		this.renderBookTextHolder(drawContext, second ? inkText2 : inkText1, 50, recipeY + 1, BookEntryScreen.PAGE_WIDTH);
		this.renderBookTextHolder(drawContext, second ? amountText2 : amountText1, 50, recipeY + 11, BookEntryScreen.PAGE_WIDTH);
	}
	
}
