package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.compat.emi.widgets.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;
import java.util.stream.*;

public class EnchantmentUpgradeEmiRecipeGated extends GatedSpectrumEmiRecipe<EnchantmentUpgradeRecipe> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	private static final int NORMAL_COLOR = 0x4d3655;
	private static final int OVERCHANT_COLOR = 0xdb3564;
	
	private final Component transKey;
	private final int levelCap;
	private final int maxNormal;
	private final List<EnchantmentUpgradeRecipe.LevelData> levelData;
	private int selectedSourceLevel = 1;
	
	public EnchantmentUpgradeEmiRecipeGated(EmiRecipeCategory category, RecipeHolder<EnchantmentUpgradeRecipe> entry) {
		super(category, entry, 132, 90);
		this.levelData = recipe.getLevelData();
		
		Holder<Enchantment> enchant = recipe.getEnchantment();
		levelCap = recipe.getLevelCap();
		maxNormal = enchant.value().getMaxLevel();
		transKey = enchant.value().description().copy().withStyle(s -> s.withItalic(true));
		
		this.inputs = recipe.getIngredientStacks().stream()
				.map(s -> EmiIngredient.of(s.getItems().map(EmiStack::of).toList()))
				.collect(Collectors.toList());
		
		// Then the xp
		inputs.add(EmiStack.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getLevelData().getFirst().experience(), true)));
		
		this.outputs = new ArrayList<>();
		
		// Last the book
		for (int i = 1; i < levelCap; i++) {
			inputs.add(EmiStack.of(SpectrumEnchantmentHelper.getEnchantedBookStackWith(enchant, i)));
			outputs.add(EmiStack.of(SpectrumEnchantmentHelper.getEnchantedBookStackWith(enchant, i+1)));
		}
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		var overEnchant = AdvancementHelper.hasAdvancement(Minecraft.getInstance().player, SpectrumAdvancements.OVERENCHANTING);
		
		// Reset the indexer
		selectedSourceLevel = 1;
		
		widgets.addTexture(BACKGROUND_TEXTURE, 13, 13, 54, 54, 0, 0);
		if (overEnchant && levelCap > maxNormal)
			widgets.addTexture(BACKGROUND_TEXTURE, 0, 0, 16, 16, 64, 0).tooltipText(List.of(Component.translatable(EnchanterBlockEntity.OVERCHANTING_TOOLTIP).withStyle(s -> s.withColor(OVERCHANT_COLOR))));
		
		// Knowledge Gem and Enchanter
		final var gem = new DynamicStackWidget(c -> {
			int xp =  levelData.get(selectedSourceLevel - 1).experience();
			return EmiStack.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(xp, false));
		}, 0, 111, 5);
		widgets.add(gem);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.ENCHANTER), 111, 51).drawBack(false);
		
		var cap = overEnchant ? levelCap : maxNormal;
		// Indexing buttons
		var minus = new SaneButtonWidget(84, 18, 8, 8, 64, 16, BACKGROUND_TEXTURE, () -> false, (mX, mY, b) -> {
			selectedSourceLevel = Math.clamp(selectedSourceLevel - 1, 1, cap - 1);
		}).tooltipText(List.of(Component.translatable(EnchanterBlockEntity.CYCLING)));
		var plus = new SaneButtonWidget(94, 18, 8, 8, 72, 16, BACKGROUND_TEXTURE, () -> false, (mX, mY, b) -> {
			selectedSourceLevel = Math.clamp(selectedSourceLevel + 1, 1, cap - 1);
		}).tooltipText(List.of(Component.translatable(EnchanterBlockEntity.CYCLING)));
		
		widgets.add(minus);
		widgets.add(plus);
		
		// surrounding input slots
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 18, 0));
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 44, 0));
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 62, 18));
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 62, 44));
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 44, 62));
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 18, 62));
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 0, 44));
		widgets.add(new DynamicStackWidget(c -> inputs.get(selectedSourceLevel), 0, 0, 18));
		
		// Center Slot
		widgets.add(new DynamicStackWidget(c -> inputs.get(cap + selectedSourceLevel), 0, 31, 31));
		
		// Output
		widgets.add(new DynamicStackWidget(c -> outputs.get(selectedSourceLevel - 1), 0, 106, 26).large(true).recipeContext(this));
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 80, 31);
		
		// Info
		widgets.add(new DynamicTextWidget(c -> {
			var color = NORMAL_COLOR;
			if (selectedSourceLevel + 1 > maxNormal)
				color = OVERCHANT_COLOR;
			
			return new Tuple<>(Component.translatable(EnchanterBlockEntity.LEVEL_TRANS, selectedSourceLevel, selectedSourceLevel + 1).getVisualOrderText(), color);
		}, 67, 2, false));
		
		// Item Use Text
		widgets.add(new DynamicTextWidget(c -> new Tuple<>(Component.translatable(EnchanterBlockEntity.ITEM_TRANS, levelData.get(selectedSourceLevel-1).countPerBowl()*8).getVisualOrderText(), NORMAL_COLOR), 67, 70, false));
		
		widgets.addText(transKey, 3, 82, NORMAL_COLOR, false);
	}
	
}
