package de.dafuqs.spectrum.compat.modonomicon.pages;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.conditions.context.BookConditionContext;
import com.klikli_dev.modonomicon.book.page.BookSpotlightPage;
import com.klikli_dev.modonomicon.util.BookGsonHelper;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconCompat;
import de.dafuqs.spectrum.helpers.NbtHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class BookNbtSpotlightPage extends BookSpotlightPage implements BookConditionalPage {

    private final BookCondition condition;

    public BookNbtSpotlightPage(BookTextHolder title, BookTextHolder text, Ingredient item, String anchor, BookCondition condition) {
        super(title, text, item, anchor);
        this.condition = condition;
    }

    public static BookNbtSpotlightPage fromJson(JsonObject json) {
        var title = BookGsonHelper.getAsBookTextHolder(json, "title", BookTextHolder.EMPTY);
        var item = getAsIngredientWithNbt(JsonHelper.getObject(json, "item"));
        var text = BookGsonHelper.getAsBookTextHolder(json, "text", BookTextHolder.EMPTY);
        var anchor = JsonHelper.getString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(JsonHelper.getObject(json, "condition"))
                : new BookNoneCondition();
        return new BookNbtSpotlightPage(title, text, item, anchor, condition);
    }

    public static BookNbtSpotlightPage fromNetwork(PacketByteBuf buffer) {
        var title = BookTextHolder.fromNetwork(buffer);
        var item = Ingredient.fromPacket(buffer);
        var text = BookTextHolder.fromNetwork(buffer);
        var anchor = buffer.readString();
        var condition = BookCondition.fromNetwork(buffer);
        return new BookNbtSpotlightPage(title, text, item, anchor, condition);
    }

    private static Ingredient getAsIngredientWithNbt(JsonObject item) {
        List<ItemStack> stacks = new ArrayList<>();

        if (item.has("item")) {
            stacks.add(ShapedRecipe.getItem(item).getDefaultStack());
        }

        if (item.has("tag")) {
            Identifier identifier = new Identifier(JsonHelper.getString(item, "tag"));
            TagKey<Item> key = TagKey.of(RegistryKeys.ITEM, identifier);

            for (RegistryEntry<Item> entry : Registries.ITEM.iterateEntries(key)) {
                stacks.add(entry.value().getDefaultStack());
            }
        }

        if (item.has("nbt")) {
            NbtCompound nbt = NbtHelper.fromJsonObject(JsonHelper.getObject(item, "nbt"));

            for (ItemStack stack : stacks) {
                stack.setNbt(nbt);
            }
        }

        return Ingredient.ofStacks(stacks.stream());
    }

    @Override
    public Identifier getType() {
        return ModonomiconCompat.NBT_SPOTLIGHT_PAGE;
    }

    @Override
    public void toNetwork(PacketByteBuf buffer) {
        title.toNetwork(buffer);
        item.write(buffer);
        text.toNetwork(buffer);
        buffer.writeString(this.anchor);
        BookCondition.toNetwork(condition, buffer);
    }

    @Override
    public boolean isPageUnlocked() {
//        MinecraftClient client = MinecraftClient.getInstance();
//        return condition.test(BookConditionContext.of(book, parentEntry), client.player);
        return true;
    }
}
