package de.dafuqs.pigment.recipe.altar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.enums.PigmentColor;
import de.dafuqs.pigment.mixin.AccessorShapedRecipe;
import de.dafuqs.pigment.registries.PigmentDefaultEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltarCraftingRecipeSerializer<T extends AltarCraftingRecipe> implements RecipeSerializer<T> {

    public final AltarCraftingRecipeSerializer.RecipeFactory<T> recipeFactory;

    public AltarCraftingRecipeSerializer(AltarCraftingRecipeSerializer.RecipeFactory<T> recipeFactory) {
        this.recipeFactory = recipeFactory;
    }

    @Override
    public T read(Identifier identifier, JsonObject jsonObject) {
        String group = JsonHelper.getString(jsonObject, "group", "");
        Map<String, Ingredient> map = AccessorShapedRecipe.invokeReadSymbols(JsonHelper.getObject(jsonObject, "key"));
        String[] strings = AccessorShapedRecipe.invokeRemovePadding(AccessorShapedRecipe.invokeGetPattern(JsonHelper.getArray(jsonObject, "pattern")));
        int width = strings[0].length();
        int height = strings.length;
        DefaultedList<Ingredient> craftingInputs = AccessorShapedRecipe.invokeCreatePatternMatrix(strings, map, width, height);
        ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));

        if(PigmentDefaultEnchantments.hasDefaultEnchants(output.getItem())) {
            PigmentDefaultEnchantments.DefaultEnchantment enchantData = PigmentDefaultEnchantments.getDefaultEnchantment(output.getItem());
            output.addEnchantment(enchantData.enchantment, enchantData.level);
        }

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

        List<Identifier> requiredAdvancementIdentifiers = new ArrayList<>();
        if(JsonHelper.hasArray(jsonObject, "required_advancements")) {
            JsonArray requiredAdvancementsArray = JsonHelper.getArray(jsonObject, "required_advancements");
            for(int i = 0; i < requiredAdvancementsArray.size(); i++) {
                Identifier requiredAdvancementIdentifier = Identifier.tryParse(requiredAdvancementsArray.get(i).getAsString());
                if(PigmentCommon.minecraftServer != null && PigmentCommon.minecraftServer.getAdvancementLoader().get(requiredAdvancementIdentifier) == null) {
                    PigmentCommon.log(Level.ERROR, "Recipe " + identifier + " is set to require advancement " + requiredAdvancementIdentifier + ", but it does not exist!");
                } else {
                    requiredAdvancementIdentifiers.add(requiredAdvancementIdentifier);
                }
            }
        }

        Identifier unlockedAdvancementIdentifier;
        if(JsonHelper.hasString(jsonObject, "unlocks_advancement")) {
            unlockedAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "unlocks_advancement", ""));
            if(PigmentCommon.minecraftServer != null && PigmentCommon.minecraftServer.getAdvancementLoader().get(unlockedAdvancementIdentifier) == null) {
                PigmentCommon.log(Level.ERROR, "Recipe " + identifier + " is set to unlock the advancement " + unlockedAdvancementIdentifier + ", but it does not exist!");
            }
        } else {
            unlockedAdvancementIdentifier = null;
        }



        return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, requiredAdvancementIdentifiers, unlockedAdvancementIdentifier);
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

        float experience = packetByteBuf.readFloat();
        int craftingTime = packetByteBuf.readVarInt();

        int requiredAdvancementAmount = packetByteBuf.readInt();
        List<Identifier> requiredAdvancementIdentifiers = new ArrayList<>();
        for(int i = 0; i < requiredAdvancementAmount; i++) {
            requiredAdvancementIdentifiers.add(packetByteBuf.readIdentifier());
        }
        Identifier unlockedAdvancementIdentifier = packetByteBuf.readIdentifier();

        HashMap<PigmentColor, Integer> gemInputs = new HashMap<>();
        if(magenta > 0) { gemInputs.put(PigmentColor.MAGENTA, magenta); }
        if(cyan > 0   ) { gemInputs.put(PigmentColor.CYAN, cyan); }
        if(yellow > 0 ) { gemInputs.put(PigmentColor.YELLOW, yellow); }
        if(black > 0  ) { gemInputs.put(PigmentColor.BLACK, black); }
        if(white > 0  ) { gemInputs.put(PigmentColor.WHITE, white); }

        return this.recipeFactory.create(identifier, group, tier, width, height, craftingInputs, gemInputs, output, experience, craftingTime, requiredAdvancementIdentifiers, unlockedAdvancementIdentifier);
    }

    @Override
    public void write(PacketByteBuf packetByteBuf, T altarCraftingRecipe) {
        packetByteBuf.writeVarInt(altarCraftingRecipe.width);
        packetByteBuf.writeVarInt(altarCraftingRecipe.height);
        packetByteBuf.writeString(altarCraftingRecipe.group);

        for (Ingredient ingredient : altarCraftingRecipe.craftingInputs) {
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

        packetByteBuf.writeInt(altarCraftingRecipe.requiredAdvancementIdentifiers.size());
        for(int i = 0; i < altarCraftingRecipe.requiredAdvancementIdentifiers.size(); i++) {
            packetByteBuf.writeIdentifier(altarCraftingRecipe.requiredAdvancementIdentifiers.get(i));
        }
        packetByteBuf.writeIdentifier(altarCraftingRecipe.unlockedAdvancementOnCraft);
    }

    public interface RecipeFactory<T extends AltarCraftingRecipe> {
        T create(Identifier id, String group, int tier, int width, int height, DefaultedList<Ingredient> craftingInputs, HashMap<PigmentColor, Integer> gemInputs, ItemStack output, float experience, int craftingTime, List<Identifier> requiredAdvancementIdentifiers, Identifier unlockedAdvancementIdentifierOnCraft);
    }

}
