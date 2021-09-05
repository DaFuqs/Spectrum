package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.progression.ClientPedestalRecipeToastManager;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FusionShrineRecipe implements Recipe<Inventory> {

    protected final Identifier id;
    protected final String group;

    protected final DefaultedList<Ingredient> craftingInputs;
    protected final Fluid fluidInput;
    protected final ItemStack output;
    protected final float experience;
    protected final int craftingTime;

    protected final List<FusionShrineRecipeWorldCondition> worldConditions;
    protected final Identifier requiredAdvancementIdentifier;

    public FusionShrineRecipe(Identifier id, String group, DefaultedList<Ingredient> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, List<FusionShrineRecipeWorldCondition> worldConditions) {
        this.id = id;
        this.group = group;

        this.craftingInputs = craftingInputs;
        this.fluidInput = fluidInput;
        this.output = output;
        this.experience = experience;
        this.craftingTime = craftingTime;

        this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
        this.worldConditions = worldConditions;

        if(SpectrumClient.minecraftClient != null) {
            registerInClientToastManager();
        }
    }

    @Environment(EnvType.CLIENT)
    private void registerInClientToastManager() {
        ClientPedestalRecipeToastManager.registerUnlockableFusionShrineRecipe(this);
    }

    /**
     * Only tests the items. The required fluid has to be tested manually by the crafting block
     */
    @Override
    public boolean matches(Inventory inv, World world) {
        for(Ingredient ingredient : this.craftingInputs) {
            boolean found = false;
            for(int i = 0; i < inv.size(); i++) {
                if(ingredient.test(inv.getStack(i))) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return SpectrumRecipeTypes.FUSION_SHRINE_RECIPE_SERIALIZER;
    }

    public RecipeType<?> getType() {
        return SpectrumRecipeTypes.FUSION_SHRINE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.craftingInputs;
    }

    public float getExperience() {
        return experience;
    }

    /**
     * The advancement the player has to have to let the recipe be craftable in the pedestal
     * @return The advancement identifier. A null value means the player is always able to craft this recipe
     */
    @Nullable
    public Identifier getRequiredAdvancementIdentifier() {
        return requiredAdvancementIdentifier;
    }

    /**
     * Returns a boolean depending on if the recipes condition is set
     * This can be always true, a specific day or moon phase, or weather.
     */
    public boolean areConditionMetCurrently(ServerWorld world) {
        for(FusionShrineRecipeWorldCondition worldCondition : this.worldConditions) {
            if(worldCondition.isMetCurrently(world)) {
                return false;
            }
        }
        return true;
    }

    public Fluid getFluidInput() {
        return this.fluidInput;
    }

}
