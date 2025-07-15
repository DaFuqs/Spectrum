package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.progression.advancement.*;
import net.minecraft.advancements.*;
import net.minecraft.core.registries.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumAdvancementCriteria {
	
	private static final DeferredRegister<CriterionTrigger<?>> REGISTRAR = DeferredRegister.create(Registries.TRIGGER_TYPE, SpectrumCommon.MOD_ID);
	
	public static PedestalRecipeCalculatedCriterion PEDESTAL_RECIPE_CALCULATED = new PedestalRecipeCalculatedCriterion();
	public static PedestalCraftingCriterion PEDESTAL_CRAFTING = new PedestalCraftingCriterion();
	public static FusionShrineCraftingCriterion FUSION_SHRINE_CRAFTING = new FusionShrineCraftingCriterion();
	public static CompletedMultiblockCriterion COMPLETED_MULTIBLOCK = new CompletedMultiblockCriterion();
	public static BlockBrokenCriterion BLOCK_BROKEN = new BlockBrokenCriterion();
	public static LootFunctionTriggerCriterion LOOT_FUNCTION_TRIGGER = new LootFunctionTriggerCriterion();
	public static NaturesStaffConversionCriterion NATURES_STAFF_CONVERSION = new NaturesStaffConversionCriterion();
	public static EnchanterCraftingCriterion ENCHANTER_CRAFTING = new EnchanterCraftingCriterion();
	public static EnchanterEnchantingCriterion ENCHANTER_ENCHANTING = new EnchanterEnchantingCriterion();
	public static EnchantmentUpgradedCriterion ENCHANTER_UPGRADING = new EnchantmentUpgradedCriterion();
	public static InertiaUsedCriterion INERTIA_USED = new InertiaUsedCriterion();
	public static AzureDikeChargeCriterion AZURE_DIKE_CHARGE = new AzureDikeChargeCriterion();
	public static TrinketChangeCriterion TRINKET_CHANGE = new TrinketChangeCriterion();
	public static PotionWorkshopBrewingCriterion POTION_WORKSHOP_BREWING = new PotionWorkshopBrewingCriterion();
	public static PotionWorkshopCraftingCriterion POTION_WORKSHOP_CRAFTING = new PotionWorkshopCraftingCriterion();
	public static TakeOffBeltJumpCriterion TAKE_OFF_BELT_JUMP = new TakeOffBeltJumpCriterion();
	public static InkContainerInteractionCriterion INK_CONTAINER_INTERACTION = new InkContainerInteractionCriterion();
	public static JeopardantKillCriterion JEOPARDANT_KILL = new JeopardantKillCriterion();
	public static MemoryManifestingCriterion MEMORY_MANIFESTING = new MemoryManifestingCriterion();
	public static SpiritInstillerCraftingCriterion SPIRIT_INSTILLER_CRAFTING = new SpiritInstillerCraftingCriterion();
	public static SlimeSizingCriterion SLIME_SIZING = new SlimeSizingCriterion();
	public static CrystalApothecaryCollectingCriterion CRYSTAL_APOTHECARY_COLLECTING = new CrystalApothecaryCollectingCriterion();
	public static UpgradePlacingCriterion UPGRADE_PLACING = new UpgradePlacingCriterion();
	public static CrystallarieumGrownCriterion CRYSTALLARIEUM_GROWING = new CrystallarieumGrownCriterion();
	public static CinderhearthSmeltingCriterion CINDERHEARTH_SMELTING = new CinderhearthSmeltingCriterion();
	public static InkProjectileKillingCriterion KILLED_BY_INK_PROJECTILE = new InkProjectileKillingCriterion();
	public static SpectrumFishingRodHookedCriterion FISHING_ROD_HOOKED = new SpectrumFishingRodHookedCriterion();
	public static TitrationBarrelTappingCriterion TITRATION_BARREL_TAPPING = new TitrationBarrelTappingCriterion();
	public static ConfirmationButtonPressedCriterion CONFIRMATION_BUTTON_PRESSED = new ConfirmationButtonPressedCriterion();
	public static BloodOrchidPluckingCriterion BLOOD_ORCHID_PLUCKING = new BloodOrchidPluckingCriterion();
	public static DivinityTickCriterion DIVINITY_TICK = new DivinityTickCriterion();
	public static PairedFoodEatenCriterion PAIRED_FOOD_EATEN = new PairedFoodEatenCriterion();
	public static HummingstoneHymnCriterion CREATE_HUMMINGSTONE_HYMN = new HummingstoneHymnCriterion();
	public static PastelNetworkCreationCriterion PASTEL_NETWORK_CREATING = new PastelNetworkCreationCriterion();
	public static PastelNodeUpgradeCriterion PASTEL_NODE_UPGRADING = new PastelNodeUpgradeCriterion();
	public static PreservationCheckCriterion PRESERVATION_CHECK = new PreservationCheckCriterion();
	public static FluidDippingCriterion FLUID_DIPPING = new FluidDippingCriterion();
	public static DeeperDownPortalOpeningCriterion DEEPER_DOWN_PORTAL_OPENING = new DeeperDownPortalOpeningCriterion();
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(PedestalRecipeCalculatedCriterion.NAME, () -> PEDESTAL_RECIPE_CALCULATED);
		REGISTRAR.register(PedestalCraftingCriterion.NAME, () -> PEDESTAL_CRAFTING);
		
		REGISTRAR.register(FusionShrineCraftingCriterion.NAME, () -> FUSION_SHRINE_CRAFTING);
		REGISTRAR.register(CompletedMultiblockCriterion.NAME, () -> COMPLETED_MULTIBLOCK);
		REGISTRAR.register(BlockBrokenCriterion.NAME, () -> BLOCK_BROKEN);
		REGISTRAR.register(LootFunctionTriggerCriterion.NAME, () -> LOOT_FUNCTION_TRIGGER);
		REGISTRAR.register(NaturesStaffConversionCriterion.NAME, () -> NATURES_STAFF_CONVERSION);
		REGISTRAR.register(EnchanterCraftingCriterion.NAME, () -> ENCHANTER_CRAFTING);
		REGISTRAR.register(EnchanterEnchantingCriterion.NAME, () -> ENCHANTER_ENCHANTING);
		REGISTRAR.register(EnchantmentUpgradedCriterion.NAME, () -> ENCHANTER_UPGRADING);
		REGISTRAR.register(InertiaUsedCriterion.NAME, () -> INERTIA_USED);
		REGISTRAR.register(AzureDikeChargeCriterion.NAME, () -> AZURE_DIKE_CHARGE);
		REGISTRAR.register(TrinketChangeCriterion.NAME, () -> TRINKET_CHANGE);
		REGISTRAR.register(PotionWorkshopBrewingCriterion.NAME, () -> POTION_WORKSHOP_BREWING);
		REGISTRAR.register(PotionWorkshopCraftingCriterion.NAME, () -> POTION_WORKSHOP_CRAFTING);
		REGISTRAR.register(TakeOffBeltJumpCriterion.NAME, () -> TAKE_OFF_BELT_JUMP);
		REGISTRAR.register(InkContainerInteractionCriterion.NAME, () -> INK_CONTAINER_INTERACTION);
		REGISTRAR.register(JeopardantKillCriterion.NAME, () -> JEOPARDANT_KILL);
		REGISTRAR.register(MemoryManifestingCriterion.NAME, () -> MEMORY_MANIFESTING);
		REGISTRAR.register(SpiritInstillerCraftingCriterion.NAME, () -> SPIRIT_INSTILLER_CRAFTING);
		REGISTRAR.register(SlimeSizingCriterion.NAME, () -> SLIME_SIZING);
		REGISTRAR.register(CrystalApothecaryCollectingCriterion.NAME, () -> CRYSTAL_APOTHECARY_COLLECTING);
		REGISTRAR.register(UpgradePlacingCriterion.NAME, () -> UPGRADE_PLACING);
		REGISTRAR.register(CrystallarieumGrownCriterion.NAME, () -> CRYSTALLARIEUM_GROWING);
		REGISTRAR.register(CinderhearthSmeltingCriterion.NAME, () -> CINDERHEARTH_SMELTING);
		REGISTRAR.register(InkProjectileKillingCriterion.NAME, () -> KILLED_BY_INK_PROJECTILE);
		REGISTRAR.register(SpectrumFishingRodHookedCriterion.NAME, () -> FISHING_ROD_HOOKED);
		REGISTRAR.register(TitrationBarrelTappingCriterion.NAME, () -> TITRATION_BARREL_TAPPING);
		REGISTRAR.register(ConfirmationButtonPressedCriterion.NAME, () -> CONFIRMATION_BUTTON_PRESSED);
		REGISTRAR.register(BloodOrchidPluckingCriterion.NAME, () -> BLOOD_ORCHID_PLUCKING);
		REGISTRAR.register(DivinityTickCriterion.NAME, () -> DIVINITY_TICK);
		REGISTRAR.register(PairedFoodEatenCriterion.NAME, () -> PAIRED_FOOD_EATEN);
		REGISTRAR.register(HummingstoneHymnCriterion.NAME, () -> CREATE_HUMMINGSTONE_HYMN);
		REGISTRAR.register(PastelNetworkCreationCriterion.NAME, () -> PASTEL_NETWORK_CREATING);
		REGISTRAR.register(PastelNodeUpgradeCriterion.NAME, () -> PASTEL_NODE_UPGRADING);
		REGISTRAR.register(PreservationCheckCriterion.NAME, () -> PRESERVATION_CHECK);
		REGISTRAR.register(FluidDippingCriterion.NAME, () -> FLUID_DIPPING);
		REGISTRAR.register(DeeperDownPortalOpeningCriterion.NAME, () -> DEEPER_DOWN_PORTAL_OPENING);
		
		REGISTRAR.register(modBus);
	}
	
}
