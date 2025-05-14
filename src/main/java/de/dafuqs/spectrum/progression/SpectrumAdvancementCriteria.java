package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.progression.advancement.*;
import net.minecraft.advancements.*;

public class SpectrumAdvancementCriteria {

	public static PedestalRecipeCalculatedCriterion PEDESTAL_RECIPE_CALCULATED;
	public static PedestalCraftingCriterion PEDESTAL_CRAFTING;
	public static FusionShrineCraftingCriterion FUSION_SHRINE_CRAFTING;
	public static CompletedMultiblockCriterion COMPLETED_MULTIBLOCK;
	public static BlockBrokenCriterion BLOCK_BROKEN;
	public static LootFunctionTriggerCriterion LOOT_FUNCTION_TRIGGER;
	public static NaturesStaffConversionCriterion NATURES_STAFF_USE;
	public static EnchanterCraftingCriterion ENCHANTER_CRAFTING;
	public static EnchanterEnchantingCriterion ENCHANTER_ENCHANTING;
	public static EnchantmentUpgradedCriterion ENCHANTER_UPGRADING;
	public static InertiaUsedCriterion INERTIA_USED;
	public static AzureDikeChargeCriterion AZURE_DIKE_CHARGE;
	public static TrinketChangeCriterion TRINKET_CHANGE;
	public static PotionWorkshopBrewingCriterion POTION_WORKSHOP_BREWING;
	public static PotionWorkshopCraftingCriterion POTION_WORKSHOP_CRAFTING;
	public static TakeOffBeltJumpCriterion TAKE_OFF_BELT_JUMP;
	public static InkContainerInteractionCriterion INK_CONTAINER_INTERACTION;
	public static JeopardantKillCriterion JEOPARDANT_KILL;
	public static MemoryManifestingCriterion MEMORY_MANIFESTING;
	public static SpiritInstillerCraftingCriterion SPIRIT_INSTILLER_CRAFTING;
	public static SlimeSizingCriterion SLIME_SIZING;
	public static CrystalApothecaryCollectingCriterion CRYSTAL_APOTHECARY_COLLECTING;
	public static UpgradePlaceCriterion UPGRADE_PLACING;
	public static CrystallarieumGrownCriterion CRYSTALLARIEUM_GROWING;
	public static CinderhearthSmeltingCriterion CINDERHEARTH_SMELTING;
	public static InkProjectileKillingCriterion KILLED_BY_INK_PROJECTILE;
	public static SpectrumFishingRodHookedCriterion FISHING_ROD_HOOKED;
	public static TitrationBarrelTappingCriterion TITRATION_BARREL_TAPPING;
	public static ConfirmationButtonPressedCriterion CONFIRMATION_BUTTON_PRESSED;
	public static BloodOrchidPluckingCriterion BLOOD_ORCHID_PLUCKING;
	public static DivinityTickCriterion DIVINITY_TICK;
	public static PairedFoodEatenCriterion CONDITIONAL_FOOD_EATEN;
	public static HummingstoneHymnCriterion CREATE_HUMMINGSTONE_HYMN;
	public static PastelNetworkCreationCriterion PASTEL_NETWORK_CREATING;
	public static PastelNodeUpgradeCriterion PASTEL_NODE_UPGRADING;
	public static PreservationCheckCriterion PRESERVATION_CHECK;
	public static FluidDippingCriterion FLUID_DIPPING;

	public static void register() {
		PEDESTAL_RECIPE_CALCULATED = CriteriaTriggers.register(PedestalRecipeCalculatedCriterion.ID.toString(), new PedestalRecipeCalculatedCriterion());
		PEDESTAL_CRAFTING = CriteriaTriggers.register(PedestalCraftingCriterion.ID.toString(), new PedestalCraftingCriterion());
		FUSION_SHRINE_CRAFTING = CriteriaTriggers.register(FusionShrineCraftingCriterion.ID.toString(), new FusionShrineCraftingCriterion());
		COMPLETED_MULTIBLOCK = CriteriaTriggers.register(CompletedMultiblockCriterion.ID.toString(), new CompletedMultiblockCriterion());
		BLOCK_BROKEN = CriteriaTriggers.register(BlockBrokenCriterion.ID.toString(), new BlockBrokenCriterion());
		LOOT_FUNCTION_TRIGGER = CriteriaTriggers.register(LootFunctionTriggerCriterion.ID.toString(), new LootFunctionTriggerCriterion());
		NATURES_STAFF_USE = CriteriaTriggers.register(NaturesStaffConversionCriterion.ID.toString(), new NaturesStaffConversionCriterion());
		ENCHANTER_CRAFTING = CriteriaTriggers.register(EnchanterCraftingCriterion.ID.toString(), new EnchanterCraftingCriterion());
		ENCHANTER_ENCHANTING = CriteriaTriggers.register(EnchanterEnchantingCriterion.ID.toString(), new EnchanterEnchantingCriterion());
		ENCHANTER_UPGRADING = CriteriaTriggers.register(EnchantmentUpgradedCriterion.ID.toString(), new EnchantmentUpgradedCriterion());
		INERTIA_USED = CriteriaTriggers.register(InertiaUsedCriterion.ID.toString(), new InertiaUsedCriterion());
		AZURE_DIKE_CHARGE = CriteriaTriggers.register(AzureDikeChargeCriterion.ID.toString(), new AzureDikeChargeCriterion());
		TRINKET_CHANGE = CriteriaTriggers.register(TrinketChangeCriterion.ID.toString(), new TrinketChangeCriterion());
		POTION_WORKSHOP_BREWING = CriteriaTriggers.register(PotionWorkshopBrewingCriterion.ID.toString(), new PotionWorkshopBrewingCriterion());
		POTION_WORKSHOP_CRAFTING = CriteriaTriggers.register(PotionWorkshopCraftingCriterion.ID.toString(), new PotionWorkshopCraftingCriterion());
		TAKE_OFF_BELT_JUMP = CriteriaTriggers.register(TakeOffBeltJumpCriterion.ID.toString(), new TakeOffBeltJumpCriterion());
		INK_CONTAINER_INTERACTION = CriteriaTriggers.register(InkContainerInteractionCriterion.ID.toString(), new InkContainerInteractionCriterion());
		JEOPARDANT_KILL = CriteriaTriggers.register(JeopardantKillCriterion.ID.toString(), new JeopardantKillCriterion());
		MEMORY_MANIFESTING = CriteriaTriggers.register(MemoryManifestingCriterion.ID.toString(), new MemoryManifestingCriterion());
		SPIRIT_INSTILLER_CRAFTING = CriteriaTriggers.register(SpiritInstillerCraftingCriterion.ID.toString(), new SpiritInstillerCraftingCriterion());
		SLIME_SIZING = CriteriaTriggers.register(SlimeSizingCriterion.ID.toString(), new SlimeSizingCriterion());
		CRYSTAL_APOTHECARY_COLLECTING = CriteriaTriggers.register(CrystalApothecaryCollectingCriterion.ID.toString(), new CrystalApothecaryCollectingCriterion());
		UPGRADE_PLACING = CriteriaTriggers.register(UpgradePlaceCriterion.ID.toString(), new UpgradePlaceCriterion());
		CRYSTALLARIEUM_GROWING = CriteriaTriggers.register(CrystallarieumGrownCriterion.ID.toString(), new CrystallarieumGrownCriterion());
		CINDERHEARTH_SMELTING = CriteriaTriggers.register(CinderhearthSmeltingCriterion.ID.toString(), new CinderhearthSmeltingCriterion());
		KILLED_BY_INK_PROJECTILE = CriteriaTriggers.register(InkProjectileKillingCriterion.ID.toString(), new InkProjectileKillingCriterion());
		FISHING_ROD_HOOKED = CriteriaTriggers.register(SpectrumFishingRodHookedCriterion.ID.toString(), new SpectrumFishingRodHookedCriterion());
		TITRATION_BARREL_TAPPING = CriteriaTriggers.register(TitrationBarrelTappingCriterion.ID.toString(), new TitrationBarrelTappingCriterion());
		CONFIRMATION_BUTTON_PRESSED = CriteriaTriggers.register(ConfirmationButtonPressedCriterion.ID.toString(), new ConfirmationButtonPressedCriterion());
		BLOOD_ORCHID_PLUCKING = CriteriaTriggers.register(BloodOrchidPluckingCriterion.ID.toString(), new BloodOrchidPluckingCriterion());
		DIVINITY_TICK = CriteriaTriggers.register(DivinityTickCriterion.ID.toString(), new DivinityTickCriterion());
		CONDITIONAL_FOOD_EATEN = CriteriaTriggers.register(PairedFoodEatenCriterion.ID.toString(), new PairedFoodEatenCriterion());
		CREATE_HUMMINGSTONE_HYMN = CriteriaTriggers.register(HummingstoneHymnCriterion.ID.toString(), new HummingstoneHymnCriterion());
		PASTEL_NETWORK_CREATING = CriteriaTriggers.register(PastelNetworkCreationCriterion.ID.toString(), new PastelNetworkCreationCriterion());
		PASTEL_NODE_UPGRADING = CriteriaTriggers.register(PastelNodeUpgradeCriterion.ID.toString(), new PastelNodeUpgradeCriterion());
		PRESERVATION_CHECK = CriteriaTriggers.register(PreservationCheckCriterion.ID.toString(), new PreservationCheckCriterion());
		FLUID_DIPPING = CriteriaTriggers.register(FluidDippingCriterion.ID.toString(), new FluidDippingCriterion());
	}
	
}
