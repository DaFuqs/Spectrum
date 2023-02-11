package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.SpectrumCommon;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class PatchouliPages {

	public static void register() {
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("anvil_crushing"), PageAnvilCrushing.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("pedestal_crafting"), PagePedestalCrafting.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("fusion_shrine_crafting"), PageFusionShrine.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("enchanter_crafting"), PageEnchanterRecipe.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("enchanter_upgrading"), PageEnchantmentUpgradeRecipe.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("potion_workshop_brewing"), PagePotionWorkshopBrewing.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("potion_workshop_crafting"), PagePotionWorkshopCrafting.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("spirit_instiller_crafting"), PageSpiritInstillerCrafting.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("midnight_solution_converting"), PageMidnightSolutionConverting.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("liquid_crystal_converting"), de.dafuqs.spectrum.compat.patchouli.PageLiquidCrystalConverting.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("crystallarieum_growing"), PageCrystallarieumGrowing.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("cinderhearth_smelting"), PageCinderhearthSmelting.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("titration_barrel_fermenting"), PageTitrationBarrelFermenting.class);
		
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("hint"), PageHint.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("checklist"), PageChecklist.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(SpectrumCommon.locate("confirmation_button"), PageConfirmationButton.class);
	}
	
}
