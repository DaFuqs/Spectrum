package de.dafuqs.spectrum.compat.modonomicon;

import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;

public class ModonomiconCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
    
    @Override
    public void register() {
        if (SpectrumItems.GUIDEBOOK instanceof GuidebookItem guidebook) {
            guidebook.registerProvider(new ModonomiconGuidebookProvider());
        }
        
        registerPages();
    }
    
    public void registerPages() {
        /*LoaderRegistry.registerPageLoader(SpectrumCommon.locate("anvil_crushing"), PageAnvilCrushing::fromJson, PageAnvilCrushing::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("pedestal_crafting"), PagePedestalCrafting::fromJson, PagePedestalCrafting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("fusion_shrine_crafting"), PageFusionShrine::fromJson, PageFusionShrine::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("enchanter_crafting"), PageEnchanterRecipe::fromJson, PageEnchanterRecipe::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("enchanter_upgrading"), PageEnchantmentUpgradeRecipe::fromJson, PageEnchantmentUpgradeRecipe::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("potion_workshop_brewing"), PagePotionWorkshopBrewing::fromJson, PagePotionWorkshopBrewing::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("potion_workshop_crafting"), PagePotionWorkshopCrafting::fromJson, PagePotionWorkshopCrafting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("spirit_instiller_crafting"), PageSpiritInstillerCrafting::fromJson, PageSpiritInstillerCrafting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("liquid_crystal_converting"), PageLiquidCrystalConverting::fromJson, PageLiquidCrystalConverting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("midnight_solution_converting"), PageMidnightSolutionConverting::fromJson, PageMidnightSolutionConverting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("dragonrot_converting"), PageDragonrotConverting::fromJson, PageDragonrotConverting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("mud_converting"), PageMudConverting::fromJson, PageMudConverting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("crystallarieum_growing"), PageCrystallarieumGrowing::fromJson, PageCrystallarieumGrowing::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("cinderhearth_smelting"), PageCinderhearthSmelting::fromJson, PageCinderhearthSmelting::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("titration_barrel_fermenting"), PageTitrationBarrelFermenting::fromJson, PageTitrationBarrelFermenting::fromNetwork);
    
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("status_effect"), PageStatusEffect::fromJson, PageStatusEffect::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("hint"), PageHint::fromJson, PageHint::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("checklist"), PageChecklist::fromJson, PageChecklist::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("confirmation_button"), PageConfirmationButton::fromJson, PageConfirmationButton::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("snippet"), PageSnippet::fromJson, PageSnippet::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("collection"), PageCollection::fromJson, PageCollection::fromNetwork);*/
    }
    
    @Override
    public void registerClient() {
        //PageRendererRegistry.registerPageRenderer(SpectrumCommon.locate("anvil_crushing"),  p -> new BookTextPageRenderer((BookTextPage) p) { });
    }
    
}
