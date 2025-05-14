package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;

import java.util.stream.*;

public class EnchanterEmiRecipeGated extends GatedSpectrumEmiRecipe<EnchanterRecipe> {
	
	private final static ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	private final Component description;
	private final int craftingTime;
	
	public EnchanterEmiRecipeGated(EmiRecipeCategory category, RecipeHolder<EnchanterRecipe> entry) {
		super(category, entry, 132, 80);
		this.craftingTime = recipe.getCraftingTime();
		this.description = getCraftingTimeText(craftingTime);
		
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		inputs = Stream.concat(inputs.stream(), Stream.of(EmiStack.of(
				KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true)))).toList();
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(BACKGROUND_TEXTURE, 13, 13, 54, 54, 0, 0);
		
		// Knowledge Gem and Enchanter
		widgets.addSlot(inputs.get(9), 111, 5);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.ENCHANTER), 111, 51).drawBack(false);
		
		// center input slot
		widgets.addSlot(inputs.get(0), 31, 31);
		
		// surrounding input slots
		widgets.addSlot(inputs.get(1), 18, 0);
		widgets.addSlot(inputs.get(2), 44, 0);
		widgets.addSlot(inputs.get(3), 62, 18);
		widgets.addSlot(inputs.get(4), 62, 44);
		widgets.addSlot(inputs.get(5), 44, 62);
		widgets.addSlot(inputs.get(6), 18, 62);
		widgets.addSlot(inputs.get(7), 0, 44);
		widgets.addSlot(inputs.get(8), 0, 18);
		
		widgets.addSlot(outputs.getFirst(), 106, 26).large(true).recipeContext(this);
		
		if (craftingTime != 0) {
			widgets.addFillingArrow(80, 31, craftingTime * 50);
		} else {
			widgets.addTexture(EmiTexture.EMPTY_ARROW, 80, 31);
		}
		
		widgets.addText(description, 67, 70, 0x3f3f3f, false);
	}
}
