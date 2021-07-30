package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.spectrum.recipe.altar.AltarCraftingRecipeSerializer;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumRecipeTypes {

    public static RecipeSerializer<AltarCraftingRecipe> ALTAR_RECIPE_SERIALIZER;
    public static RecipeType<AltarCraftingRecipe> ALTAR;

    public static RecipeSerializer<AnvilCrushingRecipe> ANVIL_CRUSHING_RECIPE_SERIALIZER;
    public static RecipeType<AnvilCrushingRecipe> ANVIL_CRUSHING;

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, id, serializer);
    }

    public static void register() {
        ALTAR_RECIPE_SERIALIZER = register("spectrum_altar", new AltarCraftingRecipeSerializer(AltarCraftingRecipe::new));
        ALTAR = Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, "altar"), new RecipeType<AltarCraftingRecipe>() {
            @Override
            public String toString() {return "altar";}
        });

        ANVIL_CRUSHING_RECIPE_SERIALIZER = register("spectrum_anvil_crushing", new AnvilCrushingRecipeSerializer(AnvilCrushingRecipe::new));
        ANVIL_CRUSHING = Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, "anvil_crushing"), new RecipeType<AnvilCrushingRecipe>() {
            @Override
            public String toString() {return "anvil_crushing";}
        });


    }

}
