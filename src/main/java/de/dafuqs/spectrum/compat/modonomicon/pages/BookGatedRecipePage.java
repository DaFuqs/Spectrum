package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.page.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.modonomicon.unlock_conditions.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class BookGatedRecipePage<T extends GatedRecipe> extends BookRecipePage<T> implements de.dafuqs.spectrum.interfaces.GatedGuidebookPage {
	
	private final Identifier pageType;
	
	public BookGatedRecipePage(RecipeType<T> recipeType, Identifier pageType, BookTextHolder title1, Identifier recipeId1, BookTextHolder title2, Identifier recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
		super(recipeType, title1, recipeId1, title2, recipeId2, text, anchor, getConditionWithRecipes(condition, recipeId1, recipeId2));
		this.pageType = pageType;
	}
	
	public static BookCondition getConditionWithRecipes(BookCondition condition, Identifier recipeId1, Identifier recipeId2) {
		List<Identifier> list = new ArrayList<>();
		if (recipeId1 != null) {
			list.add(recipeId1);
		}
		if (recipeId2 != null) {
			list.add(recipeId2);
		}
		BookCondition[] conditions = {condition, new RecipesLoadedAndUnlockedCondition(null, list)};
		return new BookAndCondition(null, conditions);
	}
	
	public static <T extends GatedRecipe> BookGatedRecipePage<T> fromJson(Identifier pageType, RecipeType<T> recipeType, JsonObject json) {
		var common = BookRecipePage.commonFromJson(json);
		var anchor = JsonHelper.getString(json, "anchor", "");
		var condition = json.has("condition")
				? BookCondition.fromJson(json.getAsJsonObject("condition"))
				: new BookNoneCondition();
		return new BookGatedRecipePage<>(recipeType, pageType, common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
	}
	
	public static <T extends GatedRecipe> BookGatedRecipePage<T> fromNetwork(Identifier pageType, RecipeType<T> recipeType, PacketByteBuf buffer) {
		var common = BookRecipePage.commonFromNetwork(buffer);
		var anchor = buffer.readString();
		var condition = BookCondition.fromNetwork(buffer);
		return new BookGatedRecipePage<>(recipeType, pageType, common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, T recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		}
		return recipe.getOutput(world.getRegistryManager());
	}
	
	@Override
	public Identifier getType() {
		return this.pageType;
	}
	
}
