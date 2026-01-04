package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.compat.emi.widgets.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

public class PrimordialFireBurningEmiRecipeGated extends GatedSpectrumEmiRecipe<PrimordialFireBurningRecipe> {
	
	private final static ResourceLocation FIRE_TEXTURE = SpectrumCommon.locate("textures/block/primordial_fire_0.png");
	
	public PrimordialFireBurningEmiRecipeGated(RecipeHolder<PrimordialFireBurningRecipe> entry) {
		super(SpectrumEmiRecipeCategories.PRIMORDIAL_FIRE_BURNING, entry, 80, 35);
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.getFirst(), 0, 0);
		widgets.add(new AnimatedTexturedWidget(FIRE_TEXTURE, 1, 19, 16, 176, 1000));
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 24, 9);
		widgets.addSlot(outputs.getFirst(), 54, 4).large(true).recipeContext(this);
	}
	
}
