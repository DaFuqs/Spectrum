package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.conditions.context.BookConditionContext;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import de.dafuqs.spectrum.interfaces.GatedGuidebookPage;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class BookGatedRecipePage<T extends GatedRecipe> extends BookRecipePage<T> implements BookConditionalPage, GatedGuidebookPage {

    private final Identifier pageType;
    protected final BookCondition condition;

    public BookGatedRecipePage(RecipeType<T> recipeType, Identifier pageType, BookTextHolder title1, Identifier recipeId1, BookTextHolder title2, Identifier recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(recipeType, title1, recipeId1, title2, recipeId2, text, anchor);
        this.pageType = pageType;
        this.condition = condition;
    }

    public static <T extends GatedRecipe> BookGatedRecipePage<T> fromJson(Identifier pageType, RecipeType<T> recipeType, JsonObject json) {
        var common = BookRecipePage.commonFromJson(json);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var condition = json.has("condition") ?
                BookCondition.fromJson(JsonHelper.getObject(json, "condition"))
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
    protected ItemStack getRecipeOutput(World level, T recipe) {
        if (recipe == null) {
            return ItemStack.EMPTY;
        }
        return recipe.getOutput(level.getRegistryManager());
    }

    @Override
    public Identifier getType() {
        return this.pageType;
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeString(this.anchor);
        BookCondition.toNetwork(condition, buffer);
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
