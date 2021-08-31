package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipeSerializer;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumRecipeTypes {

    public static RecipeSerializer<PedestalCraftingRecipe> PEDESTAL_RECIPE_SERIALIZER;
    public static RecipeType<PedestalCraftingRecipe> PEDESTAL;

    public static RecipeSerializer<AnvilCrushingRecipe> ANVIL_CRUSHING_RECIPE_SERIALIZER;
    public static RecipeType<AnvilCrushingRecipe> ANVIL_CRUSHING;

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(SpectrumCommon.MOD_ID, id), serializer);
    }

    static <S extends RecipeType<T>, T extends Recipe<?>> S registerRecipeType(String id, S serializer) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, id), serializer);
    }

    public static void registerSerializer() {
        PEDESTAL_RECIPE_SERIALIZER = registerSerializer("pedestal", new PedestalCraftingRecipeSerializer(PedestalCraftingRecipe::new));
        PEDESTAL = registerRecipeType("pedestal", new RecipeType<PedestalCraftingRecipe>() {
            @Override
            public String toString() {return "spectrum:pedestal";}
        });

        ANVIL_CRUSHING_RECIPE_SERIALIZER = registerSerializer("anvil_crushing", new AnvilCrushingRecipeSerializer(AnvilCrushingRecipe::new));
        ANVIL_CRUSHING = registerRecipeType("anvil_crushing", new RecipeType<AnvilCrushingRecipe>() {
            @Override
            public String toString() {return "spectrum:anvil_crushing";}
        });


    }

}
