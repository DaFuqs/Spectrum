package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumRecipeTypes {

    public static RecipeType<AltarRecipe> ALTAR;
    public static RecipeSerializer<AltarRecipe> ALTAR_RECIPE_SERIALIZER;

    public static void register() {
        ALTAR = Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, "altar"), new RecipeType<AltarRecipe>() {
            @Override
            public String toString() {return "altar";}
        });
        ALTAR_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(SpectrumCommon.MOD_ID, "altar"), new AltarRecipe.Serializer());
    }

}
