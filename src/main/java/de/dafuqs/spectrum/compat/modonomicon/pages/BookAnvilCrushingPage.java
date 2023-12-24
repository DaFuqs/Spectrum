package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class BookAnvilCrushingPage extends BookGatedRecipePage<AnvilCrushingRecipe> {

    public BookAnvilCrushingPage(BookTextHolder title1, Identifier recipeId1, BookTextHolder title2, Identifier recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(SpectrumRecipeTypes.ANVIL_CRUSHING, title1, recipeId1, title2, recipeId2, text, anchor, condition);
    }

    public static BookAnvilCrushingPage fromJson(JsonObject json) {
        var common = BookRecipePage.commonFromJson(json);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var condition = json.has("condition") ?
                BookCondition.fromJson(JsonHelper.getObject(json, "condition"))
                : new BookNoneCondition();
        return new BookAnvilCrushingPage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    public static BookAnvilCrushingPage fromNetwork(PacketByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readString();
        var condition = BookCondition.fromNetwork(buffer);
        return new BookAnvilCrushingPage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.ANVIL_CRUSHING_PAGE;
    }

    @Override
    protected ItemStack getRecipeOutput(World level, AnvilCrushingRecipe recipe) {
        if (recipe == null) {
            return ItemStack.EMPTY;
        }
        return recipe.getOutput(level.getRegistryManager());
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        super.toNetwork(buffer);
        buffer.writeString(this.anchor);
        BookCondition.toNetwork(condition, buffer);
    }

}
