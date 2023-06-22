package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.recipe.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.page.abstr.*;

public abstract class PageGatedRecipeDouble<T extends GatedRecipe> extends PageDoubleRecipeRegistry<T> {
	
	public PageGatedRecipeDouble(RecipeType<? extends T> recipeType) {
		super(recipeType);
	}
	
	@Override
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(entry, builder, pageNum);
		
		if (recipe2 == null) {
			GatedPatchouliPage.runSanityCheck(entry.getId(), pageNum, this.advancement, recipe1);
		} else {
			GatedPatchouliPage.runSanityCheck(entry.getId(), pageNum, this.advancement, recipe1, recipe2);
		}
	}
	
	@Override
	public boolean isPageUnlocked() {
		if (!super.isPageUnlocked()) {
			return false;
		}
		PlayerEntity player = MinecraftClient.getInstance().player;
		return (recipe1 != null && recipe1.canPlayerCraft(player)) || (recipe2 != null && recipe2.canPlayerCraft(player));
	}
	
}
