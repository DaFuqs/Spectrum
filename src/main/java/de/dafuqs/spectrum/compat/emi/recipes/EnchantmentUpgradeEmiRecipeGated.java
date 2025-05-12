package de.dafuqs.spectrum.compat.emi.recipes;

import com.google.common.collect.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.compat.emi.widgets.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class EnchantmentUpgradeEmiRecipeGated extends GatedSpectrumEmiRecipe<EnchantmentUpgradeRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	private static final int NORMAL_COLOR = 0x4d3655;
	private static final int OVERCHANT_COLOR = 0xdb3564;
	
	private static final int SWITCH_TIME = 30;
	private static final int XP_INDEX = 8;
	private static final int BOOK_INDEXES_START = 9;
	
	private final Text transKey;
	private final int levelCap;
	private final int maxNormal;
	private final RecipeScaling.ScalingData itemScaling;
	private final RecipeScaling.ScalingData xpScaling;
	private int indexer = 1;
	
	public EnchantmentUpgradeEmiRecipeGated(EmiRecipeCategory category, RecipeEntry<EnchantmentUpgradeRecipe> entry) {
		super(category, entry, 132, 90);
		this.itemScaling = recipe.getItemScaling();
		this.xpScaling = recipe.getXPScaling();
		
		inputs = Lists.newArrayList();
		
		var enchant = recipe.getEnchantment();
		levelCap = recipe.getLevelCap();
		maxNormal = enchant.value().getMaxLevel();
		transKey = enchant.value().description().copy().styled(s -> s.withItalic(true));
		
		
		// Pigments first due to funny bullshit
		for (int i = 0; i < 8; i++) {
			inputs.add(EmiStack.of(recipe.getBulkItem(), 1));
		}
		
		// Then the xp
		inputs.add(EmiStack.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getBaseXPCost(), true)));
		
		this.outputs = new ArrayList<>();
		
		// Last the book
		for (int i = 1; i <= levelCap; i++) {
			var enchStack = new ItemStack(Items.ENCHANTED_BOOK);
			var builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
			
			builder.set(enchant, i);
			enchStack.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
			inputs.add(EmiStack.of(enchStack.copy()));
			
			if (i > 1) {
				enchStack.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
				outputs.add(EmiStack.of(enchStack));
			}
		}
		
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		var overEnchant = AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, SpectrumAdvancements.OVERENCHANTING);
		
		// Reset the indexer
		indexer = 1;
		
		widgets.addTexture(BACKGROUND_TEXTURE, 13, 13, 54, 54, 0, 0);
		if (overEnchant && levelCap > maxNormal)
			widgets.addTexture(BACKGROUND_TEXTURE, 0, 0, 16, 16, 64, 0).tooltipText(List.of(Text.translatable(EnchanterBlockEntity.OVERCHANTING_TOOLTIP).styled(s -> s.withColor(OVERCHANT_COLOR))));
		
		// Knowledge Gem and Enchanter
		final var gem = new DynamicStackWidget(c -> {
			var xp = xpScaling.apply(indexer);
			return EmiStack.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(xp, false));
		}, 0, 111, 5);
		widgets.add(gem);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.ENCHANTER), 111, 51).drawBack(false);
		
		var cap = overEnchant ? levelCap : maxNormal;
		// Indexing buttons
		var minus = new SaneButtonWidget(84, 18, 8, 8, 64, 16, BACKGROUND_TEXTURE,() -> false, (mX, mY, b) -> {
			indexer = Math.clamp(indexer - 1, 1, cap - 1);
		}).tooltipText(List.of(Text.translatable(EnchanterBlockEntity.CYCLING)));
		var plus = new SaneButtonWidget(94, 18, 8, 8, 72, 16, BACKGROUND_TEXTURE,() -> false, (mX, mY, b) -> {
			indexer = Math.clamp(indexer + 1, 1, cap - 1);
		}).tooltipText(List.of(Text.translatable(EnchanterBlockEntity.CYCLING)));
		
		widgets.add(minus);
		widgets.add(plus);
		
		// surrounding input slots
		widgets.addSlot(inputs.get(0), 18, 0);
		widgets.addSlot(inputs.get(1), 44, 0);
		widgets.addSlot(inputs.get(2), 62, 18);
		widgets.addSlot(inputs.get(3), 62, 44);
		widgets.addSlot(inputs.get(4), 44, 62);
		widgets.addSlot(inputs.get(5), 18, 62);
		widgets.addSlot(inputs.get(6), 0, 44);
		widgets.addSlot(inputs.get(7), 0, 18);
		
		// Center Slot
		final var in = new DynamicStackWidget(c -> inputs.get(BOOK_INDEXES_START + indexer - 1), 0, 31, 31);
		widgets.add(in);
		
		// Output
		final var out = new DynamicStackWidget(c -> inputs.get(BOOK_INDEXES_START + indexer), 0, 106, 26);
		out.large(true).recipeContext(this);
		widgets.add(out);
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 80, 31);
		
		// Info
		final var lv = new DynamicTextWidget(c -> {
			var color = NORMAL_COLOR;
			if (indexer + 1 > maxNormal)
				color = OVERCHANT_COLOR;
			
			
			return new Pair<>(Text.translatable(EnchanterBlockEntity.LEVEL_TRANS, indexer, indexer + 1).asOrderedText(), color);
		}, 67, 2, false);
		widgets.add(lv);
		
		
		final var itemUse = new DynamicTextWidget(c -> new Pair<>(Text.translatable(EnchanterBlockEntity.ITEM_TRANS, itemScaling.apply(indexer)).asOrderedText(), NORMAL_COLOR), 67, 70, false);
		widgets.add(itemUse);
		
		widgets.addText(transKey, 3, 82, NORMAL_COLOR, false);
	}
	
	@Override
	public List<EmiStack> getOutputs() {
		return super.getOutputs();
	}
	
	@Override
	public List<EmiIngredient> getInputs() {
		return super.getInputs();
	}
}
