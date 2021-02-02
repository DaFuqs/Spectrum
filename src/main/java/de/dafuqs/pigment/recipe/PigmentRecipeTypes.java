package de.dafuqs.pigment.recipe;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PigmentRecipeTypes {

    public static RecipeSerializer<AltarCraftingRecipe> ALTAR_RECIPE_SERIALIZER;
    public static RecipeType<AltarCraftingRecipe> ALTAR;

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, id, serializer);
    }

    public static void register() {
        ALTAR_RECIPE_SERIALIZER = register("pigment_altar", new AltarCraftingRecipeSerializer(AltarCraftingRecipe::new));
        ALTAR = Registry.register(Registry.RECIPE_TYPE, new Identifier(PigmentCommon.MOD_ID, "altar"), new RecipeType<AltarCraftingRecipe>() {
            @Override
            public String toString() {return "altar";}
        });
    }

}
