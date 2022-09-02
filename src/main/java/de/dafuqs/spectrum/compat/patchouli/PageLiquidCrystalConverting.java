package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fluid_converting.LiquidCrystalConvertingRecipe;
import net.minecraft.util.Identifier;

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