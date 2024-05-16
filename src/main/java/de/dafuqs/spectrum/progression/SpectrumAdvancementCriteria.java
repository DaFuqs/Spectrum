package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.progression.advancement.*;
import net.minecraft.advancement.criterion.*;

public class SpectrumAdvancementCriteria {

	public static PedestalRecipeCalculatedCriterion PEDESTAL_RECIPE_CALCULATED;
	public static PedestalCraftingCriterion PEDESTAL_CRAFTING;
	public static FusionShrineCraftingCriterion FUSION_SHRINE_CRAFTING;
	public static CompletedMultiblockCriterion COMPLETED_MULTIBLOCK;
	public static BlockBrokenCriterion BLOCK_BROKEN;
	public static TreasureHunterDropCriterion TREASURE_HUNTER_DROP;
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
	public static ConsumedTeaWithSconeCriterion CONSUMED_TEA_WITH_SCONE;
	public static HummingstoneHymnCriterion CREATE_HUMMINGSTONE_HYMN;
	public static PastelNetworkCreationCriterion PASTEL_NETWORK_CREATING;
	public static PreservationCheckCriterion PRESERVATION_CHECK;
	public static FluidDippingCriterion FLUID_DIPPING;

	public static void register() {
		PEDESTAL_RECIPE_CALCULATED = Criteria.register(new PedestalRecipeCalculatedCriterion());
		PEDESTAL_CRAFTING = Criteria.register(new PedestalCraftingCriterion());
		FUSION_SHRINE_CRAFTING = Criteria.register(new FusionShrineCraftingCriterion());
		COMPLETED_MULTIBLOCK = Criteria.register(new CompletedMultiblockCriterion());
		BLOCK_BROKEN = Criteria.register(new BlockBrokenCriterion());
		TREASURE_HUNTER_DROP = Criteria.register(new TreasureHunterDropCriterion());
		NATURES_STAFF_USE = Criteria.register(new NaturesStaffConversionCriterion());
		ENCHANTER_CRAFTING = Criteria.register(new EnchanterCraftingCriterion());
		ENCHANTER_ENCHANTING = Criteria.register(new EnchanterEnchantingCriterion());
		ENCHANTER_UPGRADING = Criteria.register(new EnchantmentUpgradedCriterion());
		INERTIA_USED = Criteria.register(new InertiaUsedCriterion());
		AZURE_DIKE_CHARGE = Criteria.register(new AzureDikeChargeCriterion());
		TRINKET_CHANGE = Criteria.register(new TrinketChangeCriterion());
		POTION_WORKSHOP_BREWING = Criteria.register(new PotionWorkshopBrewingCriterion());
		POTION_WORKSHOP_CRAFTING = Criteria.register(new PotionWorkshopCraftingCriterion());
		TAKE_OFF_BELT_JUMP = Criteria.register(new TakeOffBeltJumpCriterion());
		INK_CONTAINER_INTERACTION = Criteria.register(new InkContainerInteractionCriterion());
		JEOPARDANT_KILL = Criteria.register(new JeopardantKillCriterion());
		MEMORY_MANIFESTING = Criteria.register(new MemoryManifestingCriterion());
		SPIRIT_INSTILLER_CRAFTING = Criteria.register(new SpiritInstillerCraftingCriterion());
		SLIME_SIZING = Criteria.register(new SlimeSizingCriterion());
		CRYSTAL_APOTHECARY_COLLECTING = Criteria.register(new CrystalApothecaryCollectingCriterion());
		UPGRADE_PLACING = Criteria.register(new UpgradePlaceCriterion());
		CRYSTALLARIEUM_GROWING = Criteria.register(new CrystallarieumGrownCriterion());
		CINDERHEARTH_SMELTING = Criteria.register(new CinderhearthSmeltingCriterion());
		KILLED_BY_INK_PROJECTILE = Criteria.register(new InkProjectileKillingCriterion());
		FISHING_ROD_HOOKED = Criteria.register(new SpectrumFishingRodHookedCriterion());
		TITRATION_BARREL_TAPPING = Criteria.register(new TitrationBarrelTappingCriterion());
		CONFIRMATION_BUTTON_PRESSED = Criteria.register(new ConfirmationButtonPressedCriterion());
		BLOOD_ORCHID_PLUCKING = Criteria.register(new BloodOrchidPluckingCriterion());
		DIVINITY_TICK = Criteria.register(new DivinityTickCriterion());
		CONSUMED_TEA_WITH_SCONE = Criteria.register(new ConsumedTeaWithSconeCriterion());
		CREATE_HUMMINGSTONE_HYMN = Criteria.register(new HummingstoneHymnCriterion());
		PASTEL_NETWORK_CREATING = Criteria.register(new PastelNetworkCreationCriterion());
		PRESERVATION_CHECK = Criteria.register(new PreservationCheckCriterion());
		FLUID_DIPPING = Criteria.register(new FluidDippingCriterion());
	}
	
}