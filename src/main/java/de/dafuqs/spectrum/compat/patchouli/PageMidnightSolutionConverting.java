package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fluid_converting.MidnightSolutionConvertingRecipe;
import net.minecraft.util.Identifier;

public class PageMidnightSolutionConverting extends PageFluidConverting<MidnightSolutionConvertingRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/midnight_solution.png");
	
	public PageMidnightSolutionConverting() {
		super(SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING);
	}
	
	@Override
	public Identifier getBackgroundTexture() {
		return BACKGROUND_TEXTURE;
	}
	
}