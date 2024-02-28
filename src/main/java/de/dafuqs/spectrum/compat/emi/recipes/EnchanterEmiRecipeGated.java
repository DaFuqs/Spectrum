package de.dafuqs.spectrum.compat.emi.recipes;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.stream.*;

public class EnchanterEmiRecipeGated extends GatedSpectrumEmiRecipe<GatedSpectrumRecipe> {
	private final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	private final Text description;
	private final int craftingTime;
	
	public EnchanterEmiRecipeGated(EmiRecipeCategory category, EnchanterRecipe recipe) {
		this(category, recipe, getCraftingTimeText(recipe.getCraftingTime()), recipe.getCraftingTime());
		inputs = Stream.concat(inputs.stream(), Stream.of(EmiStack.of(
				KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true)))).toList();
	}
	
	public EnchanterEmiRecipeGated(EmiRecipeCategory category, EnchantmentUpgradeRecipe recipe) {
		this(category, recipe, Text.translatable("container.spectrum.rei.enchantment_upgrade.required_item_count", recipe.getRequiredItemCount()), 0);
		inputs = Lists.newArrayList();
		inputs.add(EmiIngredient.of(recipe.getIngredients().get(0))); // the center stack
		int requiredItemCountSplit = recipe.getRequiredItemCount() / 8;
		int requiredItemCountModulo = recipe.getRequiredItemCount() % 8;
		for (int i = 0; i < 8; i++) {
			int addAmount = i < requiredItemCountModulo ? 1 : 0;
			inputs.add(EmiStack.of(recipe.getRequiredItem(), requiredItemCountSplit + addAmount));
		}
		inputs.add(EmiStack.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true)));
	}
	
	private EnchanterEmiRecipeGated(EmiRecipeCategory category, GatedSpectrumRecipe recipe, Text description, int craftingTime) {
		super(category, recipe, 132, 80);
		this.craftingTime = craftingTime;
		this.description = description;
		
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
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
		
		widgets.addSlot(outputs.get(0), 106, 26).large(true).recipeContext(this);
		
		if (craftingTime != 0) {
			widgets.addFillingArrow(80, 31, craftingTime * 50);
		} else {
			widgets.addTexture(EmiTexture.EMPTY_ARROW, 80, 31);
		}
		
		widgets.addText(description, 67, 70, 0x3f3f3f, false);
	}
}
