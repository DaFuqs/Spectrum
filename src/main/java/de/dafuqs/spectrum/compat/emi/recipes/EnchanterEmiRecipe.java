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
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.stream.*;

public class EnchanterEmiRecipe extends SpectrumEmiRecipe<GatedSpectrumRecipe> {
	private final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	private final Text description;
	private final int craftingTime;

	public EnchanterEmiRecipe(EmiRecipeCategory category, EnchanterRecipe recipe) {
		this(category, recipe, getCraftingTimeText(recipe.getCraftingTime()), recipe.getCraftingTime());
		input = Stream.concat(input.stream(), Stream.of(EmiStack.of(
			KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true)))).toList();
	}

	public EnchanterEmiRecipe(EmiRecipeCategory category, EnchantmentUpgradeRecipe recipe) {
		this(category, recipe, Text.translatable("container.spectrum.rei.enchantment_upgrade.required_item_count", recipe.getRequiredItemCount()), 0);
		input = Lists.newArrayList();
		input.add(EmiIngredient.of(recipe.getIngredients().get(0))); // the center stack
		int requiredItemCountSplit = recipe.getRequiredItemCount() / 8;
		int requiredItemCountModulo = recipe.getRequiredItemCount() % 8;
		for (int i = 0; i < 8; i++) {
			int addAmount = i < requiredItemCountModulo ? 1 : 0;
			input.add(EmiStack.of(recipe.getRequiredItem(), requiredItemCountSplit + addAmount));
		}
		input.add(EmiStack.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true)));
	}

	private EnchanterEmiRecipe(EmiRecipeCategory category, GatedSpectrumRecipe recipe, Text description, int craftingTime) {
		super(category, EnchanterRecipe.UNLOCK_IDENTIFIER, recipe, 132, 95);
		this.craftingTime = craftingTime;
		this.description = description;
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(BACKGROUND_TEXTURE, 12, 12, 54, 54, 0, 0);

		// Knowledge Gem and Enchanter
		widgets.addSlot(input.get(9), 111, 5);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.ENCHANTER), 111, 51).drawBack(false);

		// center input slot
		widgets.addSlot(input.get(0), 31, 31);

		// surrounding input slots
		widgets.addSlot(input.get(1), 18, 0);
		widgets.addSlot(input.get(2), 44, 0);
		widgets.addSlot(input.get(3), 62, 18);
		widgets.addSlot(input.get(4), 62, 44);
		widgets.addSlot(input.get(5), 44, 62);
		widgets.addSlot(input.get(6), 18, 62);
		widgets.addSlot(input.get(7), 0, 44);
		widgets.addSlot(input.get(8), 0, 18);

		widgets.addSlot(output.get(0), 106, 26).output(true).recipeContext(this);

		if (craftingTime != 0) {
			widgets.addFillingArrow(80, 31, craftingTime * 50);
		} else {
			widgets.addTexture(EmiTexture.EMPTY_ARROW, 80, 31);
		}

		widgets.addText(description, width / 2, 85, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
