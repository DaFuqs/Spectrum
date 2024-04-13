package de.dafuqs.spectrum.compat.patchouli.pages;

import com.google.gson.annotations.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;

public abstract class PageGatedRecipeDouble<T extends GatedRecipe> extends PageGatedRecipe<T> {
	
	@SerializedName("recipe2")
	Identifier recipe2Id;
	
	protected transient T recipe2;
	
	public PageGatedRecipeDouble(RecipeType<T> recipeType) {
		super(recipeType);
	}
	
	protected abstract void drawRecipe(DrawContext drawContext, World world, T recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second);
	
	@Override
	public void build(World world, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(world, entry, builder, pageNum);
		
		recipe2 = loadRecipe(builder, entry, recipe2Id);
		if (recipe2 != null) {
			GatedPatchouliPage.runSanityCheck(entry.getId(), pageNum, this.advancement, recipe2);
		}
		
		if (recipe == null && recipe2 != null) {
			recipe = recipe2;
			recipe2 = null;
		}
	}
	
	@Override
	public int getTextHeight() {
		return getY() + getRecipeHeight() * (recipe2 == null ? 1 : 2);
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		super.render(drawContext, mouseX, mouseY, tickDelta);
		
		if (recipe != null) {
			World world = MinecraftClient.getInstance().world;
			if (world == null) {
				return;
			}
			
			int recipeX = getX();
			int recipeY = getY();
			Text title = getTitle();
			parent.drawCenteredStringNoShadow(drawContext, title.asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
			
			drawRecipe(drawContext, world, recipe, recipeX, recipeY, mouseX, mouseY, false);
			if (recipe2 != null) {
				drawRecipe(drawContext, world, recipe2, recipeX, recipeY + getRecipeHeight(), mouseX, mouseY, true);
			}
		}
	}
	
	@Override
	public boolean isPageUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (!super.isPageUnlocked()) {
			return false;
		}
		PlayerEntity player = client.player;
		return (recipe != null && recipe.canPlayerCraft(player)) || (recipe2 != null && recipe2.canPlayerCraft(player));
	}
	
}
