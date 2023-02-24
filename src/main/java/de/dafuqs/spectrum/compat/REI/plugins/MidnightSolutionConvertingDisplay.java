package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.util.*;

public class MidnightSolutionConvertingDisplay extends FluidConvertingDisplay {

    public MidnightSolutionConvertingDisplay(MidnightSolutionConvertingRecipe recipe) {
        super(recipe);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING;
    }

    @Override
    public Identifier getUnlockIdentifier() {
        return MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER;
    }

}