package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipeSerializer;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipeSerializer;
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

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, id, serializer);
    }

    public static void register() {
        PEDESTAL_RECIPE_SERIALIZER = register("spectrum_pedestal", new PedestalCraftingRecipeSerializer(PedestalCraftingRecipe::new));
        PEDESTAL = Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, "pedestal"), new RecipeType<PedestalCraftingRecipe>() {
            @Override
            public String toString() {return "pedestal";}
        });

        ANVIL_CRUSHING_RECIPE_SERIALIZER = register("spectrum_anvil_crushing", new AnvilCrushingRecipeSerializer(AnvilCrushingRecipe::new));
        ANVIL_CRUSHING = Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, "anvil_crushing"), new RecipeType<AnvilCrushingRecipe>() {
            @Override
            public String toString() {return "anvil_crushing";}
        });


    }

}
