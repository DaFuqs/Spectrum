package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
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