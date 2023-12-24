package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.context.BookConditionContext;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import de.dafuqs.spectrum.interfaces.GatedGuidebookPage;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public abstract class BookGatedRecipePage<T extends GatedRecipe> extends BookRecipePage<T> implements BookConditionalPage, GatedGuidebookPage {

    protected final BookCondition condition;

    public BookGatedRecipePage(RecipeType<T> recipeType, BookTextHolder title1, Identifier recipeId1, BookTextHolder title2, Identifier recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(recipeType, title1, recipeId1, title2, recipeId2, text, anchor);
        this.condition = condition;
    }

    @Override
    public boolean isPageUnlocked() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!condition.test(BookConditionContext.of(book, parentEntry), client.player)) {
            return false;
        }
        return (recipe1 != null && recipe1.canPlayerCraft(client.player))
                || (recipe2 != null && recipe2.canPlayerCraft(client.player));
    }

}
