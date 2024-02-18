package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;

public class PageLiquidCrystalConverting extends PageFluidConverting<LiquidCrystalConvertingRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/liquid_crystal.png");
	
	public PageLiquidCrystalConverting() {
		super(SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING);
	}
	
	@Override
	public Identifier getBackgroundTexture() {
		return BACKGROUND_TEXTURE;
	}
	
}