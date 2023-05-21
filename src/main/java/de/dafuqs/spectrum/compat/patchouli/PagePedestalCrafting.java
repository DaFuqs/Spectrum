package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

public class PagePedestalCrafting extends PageDoubleRecipeRegistry<PedestalCraftingRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE1 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting1.png");
	private static final Identifier BACKGROUND_TEXTURE2 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting2.png");
	private static final Identifier BACKGROUND_TEXTURE3 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting3.png");
	private static final Identifier BACKGROUND_TEXTURE4 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting4.png");
	
	public PagePedestalCrafting() {
		super(SpectrumRecipeTypes.PEDESTAL);
	}
	
	@Override
	protected ItemStack getRecipeOutput(PedestalCraftingRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull PedestalCraftingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, getBackgroundTextureForTier(recipe.getTier()));
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 106, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		parent.renderItemStack(ms, recipeX + 78, recipeY + 22, mouseX, mouseY, recipe.getOutput());
		
		switch (recipe.getTier()) {
			case COMPLEX ->
					drawGemstonePowderSlots(recipe, PedestalRecipeTier.getAvailableGemstoneDustColors(recipe.getTier()), ms, 3, recipeX, recipeY, mouseX, mouseY);
			case ADVANCED ->
					drawGemstonePowderSlots(recipe, PedestalRecipeTier.getAvailableGemstoneDustColors(recipe.getTier()), ms, 12, recipeX, recipeY, mouseX, mouseY);
			default ->
					drawGemstonePowderSlots(recipe, PedestalRecipeTier.getAvailableGemstoneDustColors(recipe.getTier()), ms, 22, recipeX, recipeY, mouseX, mouseY);
		}
		
		DefaultedList<IngredientStack> ingredients = recipe.getIngredientStacks();
		int wrap = recipe.getWidth();
		for (int i = 0; i < ingredients.size(); i++) {
			PatchouliHelper.renderIngredientStack(parent, ms, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
		}
	}
	
	@Contract(pure = true)
	private Identifier getBackgroundTextureForTier(@NotNull PedestalRecipeTier pedestalRecipeTier) {
		switch (pedestalRecipeTier) {
			case BASIC -> {
				return BACKGROUND_TEXTURE1;
			}
			case SIMPLE -> {
				return BACKGROUND_TEXTURE2;
			}
			case ADVANCED -> {
				return BACKGROUND_TEXTURE3;
			}
			default -> {
				return BACKGROUND_TEXTURE4;
			}
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 108;
	}
	
	private void drawGemstonePowderSlots(PedestalCraftingRecipe recipe, GemstoneColor @NotNull [] colors, MatrixStack ms, int startX, int recipeX, int recipeY, int mouseX, int mouseY) {
		int h = 0;
		for (GemstoneColor color : colors) {
			int amount = recipe.getGemstonePowderInputs().getOrDefault(color, 0);
			if (amount > 0) {
				ItemStack stack = color.getGemstonePowderItem().getDefaultStack();
				stack.setCount(amount);
				parent.renderItemStack(ms, recipeX + startX + h * 19, recipeY + 72, mouseX, mouseY, stack);
			}
			h++;
		}
	}
	
	
}