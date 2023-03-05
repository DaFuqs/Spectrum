package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.util.*;

public class PageDragonrotConverting extends PageFluidConverting<DragonrotConvertingRecipe> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/dragonrot.png");

    public PageDragonrotConverting() {
        super(SpectrumRecipeTypes.DRAGONROT_CONVERTING);
    }

    @Override
    public Identifier getBackgroundTexture() {
        return BACKGROUND_TEXTURE;
    }

}