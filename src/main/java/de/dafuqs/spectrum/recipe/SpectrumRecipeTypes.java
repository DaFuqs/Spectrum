package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.util.AltarCraftingRecipe;
import de.dafuqs.spectrum.recipe.util.AltarCraftingRecipeSerializer;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumRecipeTypes {

    public static RecipeSerializer<AltarCraftingRecipe> ALTAR_RECIPE_SERIALIZER;
    public static RecipeType<AltarCraftingRecipe> ALTAR_RECIPE_TYPE;

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, id, serializer);
    }

    public static void register() {
        ALTAR_RECIPE_SERIALIZER = register("spectrum_altar", new AltarCraftingRecipeSerializer(AltarCraftingRecipe::new));
        ALTAR_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, "altar"), new RecipeType<AltarCraftingRecipe>() {
            @Override
            public String toString() {return "altar";}
        });
    }

}
