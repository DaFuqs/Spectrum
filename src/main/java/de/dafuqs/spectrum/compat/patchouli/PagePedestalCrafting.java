package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PagePedestalCrafting extends PageDoubleRecipeRegistry<PedestalCraftingRecipe> {

	private static final Identifier BACKGROUND_TEXTURE1 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/pedestal_crafting1.png");
	private static final Identifier BACKGROUND_TEXTURE2 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/pedestal_crafting2.png");
	private static final Identifier BACKGROUND_TEXTURE3 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/pedestal_crafting3.png");
	private static final Identifier BACKGROUND_TEXTURE4 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/pedestal_crafting4.png");

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
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);

		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		parent.renderItemStack(ms, recipeX + 78, recipeY + 22, mouseX, mouseY, recipe.getOutput());

		switch (recipe.getTier()) {
			case COMPLEX -> {
				drawGemstonePowderSlots(recipe, PedestalRecipeTier.getAvailableGemstoneDustColors(recipe.getTier()), ms, 3, recipeX, recipeY, mouseX, mouseY);
			}
			case ADVANCED -> {
				drawGemstonePowderSlots(recipe, PedestalRecipeTier.getAvailableGemstoneDustColors(recipe.getTier()), ms, 11, recipeX, recipeY, mouseX, mouseY);
			}
			default -> {
				drawGemstonePowderSlots(recipe, PedestalRecipeTier.getAvailableGemstoneDustColors(recipe.getTier()), ms, 22, recipeX, recipeY, mouseX, mouseY);
			}
		}

		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		int wrap = recipe.getWidth();

		for (int i = 0; i < ingredients.size(); i++) {
			parent.renderIngredient(ms, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
		}

		//parent.renderItemStack(ms, recipeX + 78, recipeY + 41, mouseX, mouseY, recipe.createIcon());
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
		for(GemstoneColor color : colors) {
			int amount;
			if(recipe.getGemstonePowderInputs().containsKey(color)) {
				amount = recipe.getGemstonePowderInputs().get(color);
			} else {
				amount = 0;
			}

			if(amount > 0) {
				parent.renderItemStack(ms, recipeX + startX + h * 19, recipeY + 72, mouseX, mouseY, new ItemStack(SpectrumItems.getGemstoneShard(color), amount));
			}
			h++;
		}
	}


}