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
import org.jetbrains.annotations.NotNull;
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
    @NotNull protected final FusionShrineRecipeWorldEffect startWorldEffect;
    @NotNull protected final List<FusionShrineRecipeWorldEffect> duringWorldEffects;
    @NotNull protected final FusionShrineRecipeWorldEffect finishWorldEffect;
    @Nullable protected final Identifier requiredAdvancementIdentifier;

    public FusionShrineRecipe(Identifier id, String group, DefaultedList<Ingredient> craftingInputs, Fluid fluidInput, ItemStack output, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, List<FusionShrineRecipeWorldCondition> worldConditions, FusionShrineRecipeWorldEffect startWorldEffect, List<FusionShrineRecipeWorldEffect> duringWorldEffects, FusionShrineRecipeWorldEffect finishWorldEffect) {
        this.id = id;
        this.group = group;

        this.craftingInputs = craftingInputs;
        this.fluidInput = fluidInput;
        this.output = output;
        this.experience = experience;
        this.craftingTime = craftingTime;

        this.worldConditions = worldConditions;
        this.startWorldEffect = startWorldEffect;
        this.duringWorldEffects = duringWorldEffects;
        this.finishWorldEffect = finishWorldEffect;
        this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;

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
            if(!worldCondition.isMetCurrently(world)) {
                return false;
            }
        }
        return true;
    }

    public Fluid getFluidInput() {
        return this.fluidInput;
    }

    public int getCraftingTime() {
        return this.craftingTime;
    }

    /**
     *
     * @param tick The crafting tick if the fusion shrine recipe
     * @return The effect that should be played for the given recipe tick
     */
    public FusionShrineRecipeWorldEffect getWorldEffectForTick(int tick) {
        if(tick == 1) {
            return this.startWorldEffect;
        } else if(tick == this.craftingTime) {
            return this.finishWorldEffect;
        } else {
            if(this.duringWorldEffects.size() == 0) {
                return null;
            } else if(this.duringWorldEffects.size() == 1) {
                return this.duringWorldEffects.get(0);
            } else {
                // we really have to calculate the current effect, huh?
                float parts = (float) this.craftingTime / this.duringWorldEffects.size();
                int index = (int) (tick / (parts));
                FusionShrineRecipeWorldEffect effect = this.duringWorldEffects.get(index);
                if(effect.isOneTimeEffect(effect)) {
                    if(index != (int) parts) {
                        return null;
                    }
                }
                return effect;
            }
        }
    }

}
