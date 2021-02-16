package de.dafuqs.pigment.recipe.altar;

import com.google.gson.JsonObject;
import de.dafuqs.pigment.enums.PigmentColor;
import de.dafuqs.pigment.mixin.AccessorShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AltarCraftingRecipeSerializer<T extends AltarCraftingRecipe> implements RecipeSerializer<T> {

    public final AltarCraftingRecipeSerializer.RecipeFactory<T> recipeFactory;

    public AltarCraftingRecipeSerializer(AltarCraftingRecipeSerializer.RecipeFactory<T> recipeFactory) {
        this.recipeFactory = recipeFactory;
    }

    @Override
    public T read(Identifier identifier, JsonObject jsonObject) {
        String group = JsonHelper.getString(jsonObject, "group", "");
        Map<String, Ingredient> map = AccessorShapedRecipe.invokeGetComponents(JsonHelper.getObject(jsonObject, "key"));
        String[] strings = AccessorShapedRecipe.invokeCombinePattern(AccessorShapedRecipe.invokeGetPattern(JsonHelper.getArray(jsonObject, "pattern")));
        int width = strings[0].length();
        int height = strings.length;
        DefaultedList<Ingredient> craftingInputs = AccessorShapedRecipe.invokeGetIngredients(strings, map, width, height);
        ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));

        int tier = JsonHelper.getInt(jsonObject, "tier", 0);
        float experience = JsonHelper.getFloat(jsonObject, "experience", 0);
        int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);

        HashMap<PigmentColor, Integer> gemInputs = new HashMap<>();
        if(JsonHelper.hasPrimitive(jsonObject, "cyan")) {
            int amount = JsonHelper.getInt(jsonObject, "cyan", 0);
            gemInputs.put(PigmentColor.CYAN, amount);
        }
        if(JsonHelper.hasPrimitive(jsonObject, "magenta")) {
            int amount = JsonHelper.getInt(jsonObject, "magenta", 0);
            gemInputs.put(PigmentColor.MAGENTA, amount);
        }
        if(JsonHelper.hasPrimitive(jsonObject, "yellow")) {
            int amount = JsonHelper.getInt(jsonObject, "yellow", 0);
            gemInputs.put(PigmentColor.YELLOW, amount);
        }
        if(JsonHelper.hasPrimitive(jsonObject, "white")) {
            int amount = JsonHelper.getInt(jsonObject, "white", 0);
            gemInputs.put(PigmentColor.WHITE, amount);
        }
        if(JsonHelper.hasPrimitive(jsonObject, "black")) {
            int amount = JsonHelper.getInt(jsonObject, "black", 0);
            gemInputs.put(PigmentColor.BLACK, amount);
        }

        return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime);
    }

    @Override
    public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
        int width = packetByteBuf.readVarInt();
        int height = packetByteBuf.readVarInt();
        String group = packetByteBuf.readString(32767);
        DefaultedList<Ingredient> craftingInputs = DefaultedList.ofSize(width * height, Ingredient.EMPTY);

        for(int k = 0; k < craftingInputs.size(); ++k) {
            craftingInputs.set(k, Ingredient.fromPacket(packetByteBuf));
        }
        ItemStack output = packetByteBuf.readItemStack();

        int tier = packetByteBuf.readVarInt();

        int magenta = packetByteBuf.readVarInt();
        int cyan = packetByteBuf.readVarInt();
        int yellow = packetByteBuf.readVarInt();
        int black = packetByteBuf.readVarInt();
        int white = packetByteBuf.readVarInt();

        HashMap<PigmentColor, Integer> gemInputs = new HashMap<>();
        if(magenta > 0) { gemInputs.put(PigmentColor.MAGENTA, magenta); }
        if(cyan > 0   ) { gemInputs.put(PigmentColor.CYAN, cyan); }
        if(yellow > 0 ) { gemInputs.put(PigmentColor.YELLOW, yellow); }
        if(black > 0  ) { gemInputs.put(PigmentColor.BLACK, black); }
        if(white > 0  ) { gemInputs.put(PigmentColor.WHITE, white); }

        float experience = packetByteBuf.readFloat();
        int craftingTime = packetByteBuf.readVarInt();

        return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime);
    }

    @Override
    public void write(PacketByteBuf packetByteBuf, T altarCraftingRecipe) {
        packetByteBuf.writeVarInt(altarCraftingRecipe.width);
        packetByteBuf.writeVarInt(altarCraftingRecipe.height);
        packetByteBuf.writeString(altarCraftingRecipe.group);
        Iterator var3 = altarCraftingRecipe.craftingInputs.iterator();

        while(var3.hasNext()) {
            Ingredient ingredient = (Ingredient)var3.next();
            ingredient.write(packetByteBuf);
        }

        packetByteBuf.writeItemStack(altarCraftingRecipe.output);

        packetByteBuf.writeInt(altarCraftingRecipe.tier);
        packetByteBuf.writeInt(altarCraftingRecipe.getPigmentColor(PigmentColor.MAGENTA));
        packetByteBuf.writeInt(altarCraftingRecipe.getPigmentColor(PigmentColor.CYAN));
        packetByteBuf.writeInt(altarCraftingRecipe.getPigmentColor(PigmentColor.YELLOW));
        packetByteBuf.writeInt(altarCraftingRecipe.getPigmentColor(PigmentColor.BLACK));
        packetByteBuf.writeInt(altarCraftingRecipe.getPigmentColor(PigmentColor.WHITE));

        packetByteBuf.writeFloat(altarCraftingRecipe.experience);
        packetByteBuf.writeInt(altarCraftingRecipe.craftingTime);
    }

    public interface RecipeFactory<T extends AltarCraftingRecipe> {
        T create(Identifier id, String group, int tier, int width, int height, DefaultedList<Ingredient> craftingInputs, HashMap<PigmentColor, Integer> gemInputs, ItemStack output, float experience, int craftingTime);
    }

}
