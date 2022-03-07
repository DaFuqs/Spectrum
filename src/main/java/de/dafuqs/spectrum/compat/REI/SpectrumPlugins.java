package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.SpectrumCommon;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.util.Identifier;

public class SpectrumPlugins {
	
	public static final CategoryIdentifier<PedestalCraftingRecipeDisplay> PEDESTAL_CRAFTING = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "pedestal_crafting"));
	public static final CategoryIdentifier<AnvilCrushingRecipeDisplay> ANVIL_CRUSHING = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "anvil_crushing"));
	public static final CategoryIdentifier<FusionShrineRecipeDisplay> FUSION_SHRINE = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "fusion_shrine"));
	public static final CategoryIdentifier<NaturesStaffConversionsDisplay> NATURES_STAFF = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "natures_staff_conversions"));
	public static final CategoryIdentifier<EnchanterRecipeDisplay> ENCHANTER = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "enchanter"));
	public static final CategoryIdentifier<EnchantmentUpgradeRecipeDisplay> ENCHANTMENT_UPGRADE = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "enchantment_upgrade"));
	public static final CategoryIdentifier<PotionWorkshopBrewingRecipeDisplay> POTION_WORKSHOP_BREWING = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "potion_workshop_brewing"));
	public static final CategoryIdentifier<PotionWorkshopCraftingRecipeDisplay> POTION_WORKSHOP_CRAFTING = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "potion_workshop_crafting"));
	public static final CategoryIdentifier<PotionWorkshopCraftingRecipeDisplay> SPIRIT_INSTILLER = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "spirit_instiller"));
	public static final CategoryIdentifier<PotionWorkshopCraftingRecipeDisplay> MIDNIGHT_SOLUTION_CONVERTING = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "midnight_solution_converting"));
	
}
