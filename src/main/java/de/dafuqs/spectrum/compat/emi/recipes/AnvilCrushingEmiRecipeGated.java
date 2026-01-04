package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

public class AnvilCrushingEmiRecipeGated extends GatedSpectrumEmiRecipe<AnvilCrushingRecipe> {
	private final static ResourceLocation WALL_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");
	
	public AnvilCrushingEmiRecipeGated(RecipeHolder<AnvilCrushingRecipe> entry) {
		super(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, entry, 116, 64);
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.getFirst(), 21, 30);
		widgets.addTexture(WALL_TEXTURE, 0, 0, 16, 48, 0, 0); // dirt wall
		widgets.addSlot(EmiStack.of(Items.ANVIL), 21, 10).drawBack(false);
		widgets.addTexture(WALL_TEXTURE, 22, 1, 16, 16, 16, 1); // falling stripes for anvil
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 50, 16);
		widgets.addSlot(outputs.getFirst(), 90, 11).large(true).recipeContext(this);
		
		widgets.addText(Component.translatable("container.spectrum.rei.anvil_crushing.plus_xp", recipe.getExperience()),
				width, 40, 0x3f3f3f, false).horizontalAlign(Alignment.END);
		
		widgets.addText(getForceText(), width / 2, 54, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
	
	public Component getForceText() {
		if (recipe.getCrushedItemsPerPointOfDamage() >= 1) {
			return Component.translatable("container.spectrum.rei.anvil_crushing.low_force_required");
		} else if (recipe.getCrushedItemsPerPointOfDamage() >= 0.5) {
			return Component.translatable("container.spectrum.rei.anvil_crushing.medium_force_required");
		} else {
			return Component.translatable("container.spectrum.rei.anvil_crushing.high_force_required");
		}
	}
}
