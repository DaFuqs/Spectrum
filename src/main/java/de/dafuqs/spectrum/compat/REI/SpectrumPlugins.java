package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.util.Identifier;

public class SpectrumPlugins {
	
	public static final CategoryIdentifier<PedestalCraftingRecipeDisplay<PedestalCraftingRecipe>> PEDESTAL_CRAFTING = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "pedestal_crafting"));
	public static final CategoryIdentifier<AnvilCrushingRecipeDisplay<AnvilCrushingRecipe>> ANVIL_CRUSHING = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "anvil_crushing"));
	public static final CategoryIdentifier<FusionShrineRecipeDisplay<FusionShrineRecipe>> FUSION_SHRINE = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "fusion_shrine"));
	public static final CategoryIdentifier<NaturesStaffConversionsDisplay> NATURES_STAFF = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "natures_staff_conversions"));
	public static final CategoryIdentifier<EnchanterRecipeDisplay<EnchanterRecipe>> ENCHANTER = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "enchanter"));
	public static final CategoryIdentifier<EnchantmentUpgradeRecipeDisplay<EnchantmentUpgradeRecipe>> ENCHANTMENT_UPGRADE = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "enchantment_upgrade"));
	
	
}
