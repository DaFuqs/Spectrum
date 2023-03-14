package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SpectrumRecipeCategories {
	public static final EmiRecipeCategory PEDESTAL_CRAFTING = new SpectrumCategory(SpectrumCommon.locate("pedestal_crafting"), EmiStack.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST));
	public static final EmiRecipeCategory ANVIL_CRUSHING = new SpectrumCategory(SpectrumCommon.locate("anvil_crushing"), EmiStack.of(Blocks.ANVIL));
	public static final EmiRecipeCategory FUSION_SHRINE = new SpectrumCategory(SpectrumCommon.locate("fusion_shrine"), EmiStack.of(SpectrumBlocks.FUSION_SHRINE_CALCITE), "block.spectrum.fusion_shrine");
	public static final EmiRecipeCategory NATURES_STAFF = new SpectrumCategory(SpectrumCommon.locate("natures_staff_conversions"), EmiStack.of(SpectrumItems.NATURES_STAFF), SpectrumItems.NATURES_STAFF.getTranslationKey());
	public static final EmiRecipeCategory ENCHANTER = new SpectrumCategory(SpectrumCommon.locate("enchanter"), EmiStack.of(SpectrumBlocks.ENCHANTER), "container.spectrum.rei.enchanting.title");
	public static final EmiRecipeCategory ENCHANTMENT_UPGRADE = new SpectrumCategory(SpectrumCommon.locate("enchantment_upgrade"), EmiStack.of(SpectrumBlocks.ENCHANTER), "container.spectrum.rei.enchantment_upgrading.title");
	public static final EmiRecipeCategory POTION_WORKSHOP_BREWING = new SpectrumCategory(SpectrumCommon.locate("potion_workshop_brewing"), EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
	public static final EmiRecipeCategory POTION_WORKSHOP_CRAFTING = new SpectrumCategory(SpectrumCommon.locate("potion_workshop_crafting"), EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
	public static final EmiRecipeCategory POTION_WORKSHOP_REACTING = new SpectrumCategory(SpectrumCommon.locate("potion_workshop_reacting"), EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
	public static final EmiRecipeCategory SPIRIT_INSTILLER = new SpectrumCategory(SpectrumCommon.locate("spirit_instiller"), EmiStack.of(SpectrumBlocks.SPIRIT_INSTILLER), SpectrumBlocks.SPIRIT_INSTILLER.getTranslationKey());
	public static final EmiRecipeCategory LIQUID_CRYSTAL_CONVERTING = new SpectrumCategory(SpectrumCommon.locate("liquid_crystal_converting"), EmiStack.of(SpectrumItems.LIQUID_CRYSTAL_BUCKET));
	public static final EmiRecipeCategory MIDNIGHT_SOLUTION_CONVERTING = new SpectrumCategory(SpectrumCommon.locate("midnight_solution_converting"), EmiStack.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET));
	public static final EmiRecipeCategory DRAGONROT_CONVERTING = new SpectrumCategory(SpectrumCommon.locate("dragonrot_converting"), EmiStack.of(SpectrumItems.DRAGONROT_BUCKET), "container.spectrum.rei.dragonrot_converting.title");
	public static final EmiRecipeCategory HEATING = new SpectrumCategory(SpectrumCommon.locate("heating"), EmiStack.of(SpectrumBlocks.BLAZE_MOB_BLOCK));
	public static final EmiRecipeCategory FREEZING = new SpectrumCategory(SpectrumCommon.locate("freezing"), EmiStack.of(SpectrumBlocks.POLAR_BEAR_MOB_BLOCK));
	public static final EmiRecipeCategory INK_CONVERTING = new SpectrumCategory(SpectrumCommon.locate("ink_converting"), EmiStack.of(SpectrumBlocks.COLOR_PICKER));
	public static final EmiRecipeCategory CRYSTALLARIEUM = new SpectrumCategory(SpectrumCommon.locate("crystallarieum"), EmiStack.of(SpectrumBlocks.CRYSTALLARIEUM), "block.spectrum.crystallarieum");
	public static final EmiRecipeCategory CINDERHEARTH = new SpectrumCategory(SpectrumCommon.locate("cinderhearth"), EmiStack.of(SpectrumBlocks.CINDERHEARTH), SpectrumBlocks.CINDERHEARTH.getTranslationKey());
	public static final EmiRecipeCategory TITRATION_BARREL = new SpectrumCategory(SpectrumCommon.locate("titration_barrel"), EmiStack.of(SpectrumBlocks.TITRATION_BARREL), SpectrumBlocks.TITRATION_BARREL.getTranslationKey());

	private static class SpectrumCategory extends EmiRecipeCategory {
		private final String key;

		public SpectrumCategory(Identifier id, EmiRenderable icon) {
			this(id, icon, "container."  + id.getNamespace() + ".rei." + id.getPath() + ".title");
		}

		public SpectrumCategory(Identifier id, EmiRenderable icon, String key) {
			super(id, icon, icon, EmiRecipeSorting.compareOutputThenInput());
			this.key = key;
		}

		@Override
		public Text getName() {
			return Text.translatable(key);
		}
	}
}
