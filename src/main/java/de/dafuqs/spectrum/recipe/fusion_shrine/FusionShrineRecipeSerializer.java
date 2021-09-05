package de.dafuqs.spectrum.recipe.fusion_shrine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FusionShrineRecipeSerializer<T extends FusionShrineRecipe> implements RecipeSerializer<T> {

    public final FusionShrineRecipeSerializer.RecipeFactory<T> recipeFactory;

    public FusionShrineRecipeSerializer(FusionShrineRecipeSerializer.RecipeFactory<T> recipeFactory) {
        this.recipeFactory = recipeFactory;
    }

    @Override
    public T read(Identifier identifier, JsonObject jsonObject) {
        String group = JsonHelper.getString(jsonObject, "group", "");

        JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
        DefaultedList<Ingredient> craftingInputs = DefaultedList.ofSize(ingredientArray.size());
        for(int i = 0; i < ingredientArray.size(); i++) {
            craftingInputs.add(Ingredient.fromJson(ingredientArray.get(i)));
        }

        Fluid fluid = Fluids.EMPTY;
        if(JsonHelper.hasString(jsonObject, "fluid")) {
            Identifier fluidIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "fluid"));
            fluid = Registry.FLUID.get(fluidIdentifier);
        }

        ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
        float experience = JsonHelper.getFloat(jsonObject, "experience", 0);
        int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);

        Identifier requiredAdvancementIdentifier = null;
        if(JsonHelper.hasString(jsonObject, "")) {
            requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
            if(SpectrumCommon.minecraftServer != null && SpectrumCommon.minecraftServer.getAdvancementLoader().get(requiredAdvancementIdentifier) == null) {
                SpectrumCommon.log(Level.ERROR, "Fusion Shrine recipe " + identifier + " is set to require advancement " + requiredAdvancementIdentifier + ", but it does not exist!");
            }
        }
        List<FusionShrineRecipeWorldCondition> worldConditions = new ArrayList<>();
        if(JsonHelper.hasArray(jsonObject, "world_conditions")) {
            JsonArray conditionsArray = JsonHelper.getArray(jsonObject, "world_conditions");
            for(int i = 0; i < conditionsArray.size(); i++) {
                String conditionString = conditionsArray.get(i).getAsString().toUpperCase(Locale.ROOT);
                worldConditions.add(FusionShrineRecipeWorldCondition.valueOf(conditionString));
            }
        }

        return this.recipeFactory.create(identifier, group, craftingInputs, fluid, output, experience, craftingTime, requiredAdvancementIdentifier, worldConditions);
    }

    @Override
    public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
        String group = packetByteBuf.readString();
        short craftingInputCount = packetByteBuf.readShort();
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(craftingInputCount, Ingredient.EMPTY);
        for(short i = 0; i < craftingInputCount; i++) {
            ingredients.set(i, Ingredient.fromPacket(packetByteBuf));
        }
        Fluid fluid = Registry.FLUID.get(packetByteBuf.readIdentifier());
        ItemStack output = packetByteBuf.readItemStack();
        float experience = packetByteBuf.readFloat();
        int craftingTime = packetByteBuf.readInt();
        Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();

        short worldConditionCount = packetByteBuf.readShort();
        List<FusionShrineRecipeWorldCondition> worldConditions = new ArrayList<>();
        for(short i = 0; i < worldConditionCount; i++) {
            worldConditions.add(FusionShrineRecipeWorldCondition.values()[packetByteBuf.readInt()]);
        }

        return this.recipeFactory.create(identifier, group, ingredients, fluid, output, experience, craftingTime, requiredAdvancementIdentifier, worldConditions);
    }

    @Override
    public void write(PacketByteBuf packetByteBuf, T fusionShrineRecipe) {
        packetByteBuf.writeString(fusionShrineRecipe.group);

        packetByteBuf.writeShort(fusionShrineRecipe.craftingInputs.size());
        for(Ingredient ingredient : fusionShrineRecipe.craftingInputs) {
            ingredient.write(packetByteBuf);
        }

        packetByteBuf.writeIdentifier(Registry.FLUID.getId(fusionShrineRecipe.fluidInput));
        packetByteBuf.writeItemStack(fusionShrineRecipe.output);
        packetByteBuf.writeFloat(fusionShrineRecipe.experience);
        packetByteBuf.writeInt(fusionShrineRecipe.craftingTime);
        packetByteBuf.writeIdentifier(fusionShrineRecipe.requiredAdvancementIdentifier);

        packetByteBuf.writeShort(fusionShrineRecipe.worldConditions.size());
        for(FusionShrineRecipeWorldCondition worldCondition : fusionShrineRecipe.worldConditions) {
            packetByteBuf.writeInt(worldCondition.ordinal());
        }
    }

    public interface RecipeFactory<T extends FusionShrineRecipe> {
        T create(Identifier id, String group, DefaultedList<Ingredient> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, List<FusionShrineRecipeWorldCondition> worldConditions);
    }

}
