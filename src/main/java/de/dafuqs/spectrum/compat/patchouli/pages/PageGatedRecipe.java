package de.dafuqs.spectrum.compat.patchouli.pages;

import com.google.gson.annotations.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

/**
 * Like PageGatedRecipeDouble, but only displays a single recipe
 */
public abstract class PageGatedRecipe<T extends GatedRecipe> extends PageWithText implements GatedPatchouliPage {
	
	private final RecipeType<T> recipeType;
	
	@SerializedName("recipe")
	Identifier recipeId;
	String title;
	
	protected transient T recipe;
	protected transient Text titleText;
	
	public PageGatedRecipe(RecipeType<T> recipeType) {
		this.recipeType = recipeType;
	}
	
	@SuppressWarnings({"unchecked"})
	private @Nullable T getRecipe(Identifier id) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null) {
			return null;
		}
		RecipeManager manager = client.world.getRecipeManager();
		return (T) manager.get(id).filter(recipe -> recipe.getType() == recipeType).orElse(null);
	}
	
	protected T loadRecipe(BookContentsBuilder builder, BookEntry entry, Identifier identifier) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (identifier == null || client.world == null) {
			return null;
		}
		T recipe = getRecipe(identifier);
		if (recipe != null) {
			entry.addRelevantStack(builder, recipe.getOutput(client.world.getRegistryManager()), pageNum);
			return recipe;
		}
		PatchouliAPI.LOGGER.warn("Recipe {} (of type {}) not found", identifier, Registries.RECIPE_TYPE.getId(recipeType));
		return null;
	}
	
	@Override
	public boolean isPageUnlocked() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (!super.isPageUnlocked() || recipe == null) {
			return false;
		}
		return recipe.canPlayerCraft(client.player);
	}
	
	@Override
	public void build(World world, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(world, entry, builder, pageNum);
		
		recipe = loadRecipe(builder, entry, recipeId);
		
		boolean customTitle = title != null && !title.isEmpty();
		titleText = !customTitle ? getRecipeOutput(world, recipe).getName() : i18nText(title);
		
		GatedPatchouliPage.runSanityCheck(entry.getId(), pageNum, advancement, recipe);
	}
	
	@Override
	public int getTextHeight() {
		return getY() + getRecipeHeight() - 13;
	}
	
	@Override
	public boolean shouldRenderText() {
		return getTextHeight() + 10 < GuiBook.PAGE_HEIGHT;
	}
	
	protected abstract ItemStack getRecipeOutput(World world, T recipe);
	
	protected abstract int getRecipeHeight();
	
	protected int getX() {
		return GuiBook.PAGE_WIDTH / 2 - 49;
	}
	
	protected int getY() {
		return 4;
	}
	
	protected Text getTitle() {
		return titleText;
	}
	
}
