package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;

public class PageMudConverting extends PageFluidConverting<MudConvertingRecipe> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/mud.png");

    public PageMudConverting() {
        super(SpectrumRecipeTypes.MUD_CONVERTING);
    }

    @Override
    public Identifier getBackgroundTexture() {
        return BACKGROUND_TEXTURE;
    }

}