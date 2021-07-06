package de.dafuqs.pigment.recipe.anvil_crushing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class AnvilCrushingRecipeSerializer<T extends AnvilCrushingRecipe> implements RecipeSerializer<T> {

    public final AnvilCrushingRecipeSerializer.RecipeFactory<T> recipeFactory;

    public AnvilCrushingRecipeSerializer(AnvilCrushingRecipeSerializer.RecipeFactory<T> recipeFactory) {
        this.recipeFactory = recipeFactory;
    }

    @Override
    public T read(Identifier identifier, JsonObject jsonObject) {
        JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(jsonElement);

        ItemStack outputItemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
        float crushedItemsPerPointOfDamage = JsonHelper.getFloat(jsonObject, "crushedItemsPerPointOfDamage");
        float experience = JsonHelper.getFloat(jsonObject, "experience");

        String particleEffectString = JsonHelper.getString(jsonObject, "particleEffectIdentifier");
        Identifier particleEffectIdentifier = new Identifier(particleEffectString);

        String soundEventString = JsonHelper.getString(jsonObject, "soundEventIdentifier");
        Identifier soundEventIdentifier = new Identifier(soundEventString);

        return this.recipeFactory.create(identifier, ingredient, outputItemStack, crushedItemsPerPointOfDamage, experience, particleEffectIdentifier, soundEventIdentifier);
    }

    @Override
    public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
        Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
        ItemStack outputItemStack = packetByteBuf.readItemStack();
        float crushedItemsPerPointOfDamage = packetByteBuf.readFloat();
        float experience = packetByteBuf.readFloat();
        Identifier particleEffectIdentifier = packetByteBuf.readIdentifier();
        Identifier soundEventIdentifier = packetByteBuf.readIdentifier();
        return this.recipeFactory.create(identifier, ingredient, outputItemStack, crushedItemsPerPointOfDamage, experience, particleEffectIdentifier, soundEventIdentifier);
    }

    @Override
    public void write(PacketByteBuf packetByteBuf, T anvilCrushingRecipe) {
        anvilCrushingRecipe.inputIngredient.write(packetByteBuf);
        packetByteBuf.writeItemStack(anvilCrushingRecipe.outputItemStack);
        packetByteBuf.writeFloat(anvilCrushingRecipe.crushedItemsPerPointOfDamage);
        packetByteBuf.writeFloat(anvilCrushingRecipe.experience);
        packetByteBuf.writeIdentifier(anvilCrushingRecipe.particleEffect);
        packetByteBuf.writeIdentifier(anvilCrushingRecipe.soundEvent);
    }

    public interface RecipeFactory<T extends AnvilCrushingRecipe> {
        T create(Identifier id, Ingredient inputIngredient, ItemStack outputItemStack, float crushedItemsPerPointOfDamage, float experience, Identifier particleEffectIdentifier, Identifier soundEventIdentifier);
    }

}
