package de.dafuqs.spectrum.mixin.client.compat.modonomicon.present;

import com.klikli_dev.modonomicon.api.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.client.render.page.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.recipe.crafting.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.crafting.*;
import org.apache.commons.lang3.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

// Mixing into Modonomicon to make GatedCraftingRecipes work
// BookCraftingRecipePageRenderer assumes only ShapedRecipes are, well, shaped. Though SpecialRecipes can be shaped, too!
@Mixin(BookCraftingRecipePageRenderer.class)
public abstract class BookCraftingRecipePageRendererMixin extends BookRecipePageRenderer<Recipe<?>, BookCraftingRecipePage> {
	
	public BookCraftingRecipePageRendererMixin(BookCraftingRecipePage page) {
		super(page);
	}
	
	@Inject(method = "drawRecipe(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/crafting/RecipeHolder;IIIIZ)V", at = @At("HEAD"), cancellable = true)
	private void spectrum$drawRecipe(GuiGraphics guiGraphics, RecipeHolder<Recipe<?>> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second, CallbackInfo ci) {
		if (recipe.value() instanceof GatedCraftingRecipe gatedCraftingRecipe) {
			if (!second) {
				if (!this.page.getTitle1().isEmpty()) {
					this.renderTitle(guiGraphics, this.page.getTitle1(), false, BookEntryScreen.PAGE_WIDTH / 2, -5);
				}
			} else {
				if (!this.page.getTitle2().isEmpty()) {
					this.renderTitle(guiGraphics, this.page.getTitle2(), false, BookEntryScreen.PAGE_WIDTH / 2, recipeY - (this.page.getTitle2().getString().isEmpty() ? 10 : 0) - 10);
				}
			}
			
			RenderSystem.enableBlend();
			guiGraphics.blit(this.page.getBook().getCraftingTexture(), recipeX - 2, recipeY - 2, 0, 0, 100, 62, 128, 256);
			
			boolean shaped = gatedCraftingRecipe instanceof ShapedGatedCraftingRecipe;
			if (!shaped) {
				int iconX = recipeX + 62;
				int iconY = recipeY + 2;
				guiGraphics.blit(this.page.getBook().getCraftingTexture(), iconX, iconY, 0, 64, 11, 11, 128, 256);
				if (this.parentScreen.isMouseInRange(mouseX, mouseY, iconX, iconY, 11, 11)) {
					this.parentScreen.setTooltip(Component.translatable(ModonomiconConstants.I18n.Tooltips.RECIPE_CRAFTING_SHAPELESS));
				}
			}
			
			this.parentScreen.renderItemStack(guiGraphics, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()));
			
			NonNullList<Ingredient> ingredients = recipe.value().getIngredients();
			int wrap = 3;
			if (shaped) {
				wrap = ((ShapedGatedCraftingRecipe) recipe.value()).getWidth();
			}
			
			for (int i = 0; i < ingredients.size(); i++) {
				this.parentScreen.renderIngredient(guiGraphics, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
			}
			
			this.parentScreen.renderItemStack(guiGraphics, recipeX + 79, recipeY + 41, mouseX, mouseY, recipe.value().getToastSymbol());
			
			ci.cancel();
		}
	}
	
}
