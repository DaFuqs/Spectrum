package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.book.page.BookEmptyPage;
import com.klikli_dev.modonomicon.client.render.page.*;
import com.klikli_dev.modonomicon.data.LoaderRegistry;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.modonomicon.client.pages.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.Identifier;

public class ModonomiconCompat extends SpectrumIntegrationPacks.ModIntegrationPack {

    public static final Identifier ANVIL_CRUSHING_PAGE = SpectrumCommon.locate("anvil_crushing");
    public static final Identifier PEDESTAL_CRAFTING_PAGE = SpectrumCommon.locate("pedestal_crafting");
    public static final Identifier FUSION_SHRINE_CRAFTING_PAGE = SpectrumCommon.locate("fusion_shrine_crafting");
    public static final Identifier ENCHANTER_CRAFTING_PAGE = SpectrumCommon.locate("enchanter_crafting");
    public static final Identifier ENCHANTER_UPGRADING_PAGE = SpectrumCommon.locate("enchanter_upgrading");
    public static final Identifier POTION_WORKSHOP_BREWING_PAGE = SpectrumCommon.locate("potion_workshop_brewing");
    public static final Identifier POTION_WORKSHOP_CRAFTING_PAGE = SpectrumCommon.locate("potion_workshop_crafting");
    public static final Identifier SPIRIT_INSTILLER_CRAFTING_PAGE = SpectrumCommon.locate("spirit_instiller_crafting");
    public static final Identifier LIQUID_CRYSTAL_CONVERTING_PAGE = SpectrumCommon.locate("liquid_crystal_converting");
    public static final Identifier MIDNIGHT_SOLUTION_CONVERTING_PAGE = SpectrumCommon.locate("midnight_solution_converting");
    public static final Identifier DRAGONROT_CONVERTING_PAGE = SpectrumCommon.locate("dragonrot_converting");
    public static final Identifier MUD_CONVERTING_PAGE = SpectrumCommon.locate("mud_converting");
    public static final Identifier CRYSTALLARIEUM_GROWING_PAGE = SpectrumCommon.locate("crystallarieum_growing");
    public static final Identifier CINDERHEARTH_SMELTING_PAGE = SpectrumCommon.locate("cinderhearth_smelting");
    public static final Identifier TITRATION_BARREL_FERMENTING_PAGE = SpectrumCommon.locate("titration_barrel_fermenting");
    public static final Identifier STATUS_EFFECT_PAGE = SpectrumCommon.locate("status_effect");
    public static final Identifier HINT_PAGE = SpectrumCommon.locate("hint");
    public static final Identifier CHECKLIST_PAGE = SpectrumCommon.locate("checklist");
    public static final Identifier CONFIRMATION_BUTTON_PAGE = SpectrumCommon.locate("confirmation_button");
    public static final Identifier SNIPPET_PAGE = SpectrumCommon.locate("snippet");
    public static final Identifier LINK_PAGE = SpectrumCommon.locate("link");
    public static final Identifier NBT_SPOTLIGHT_PAGE = SpectrumCommon.locate("nbt_spotlight");
    public static final Identifier COLLECTION_PAGE = SpectrumCommon.locate("collection");

    @Override
    public void register() {
        if (SpectrumItems.GUIDEBOOK instanceof GuidebookItem guidebook) {
            guidebook.registerProvider(new ModonomiconGuidebookProvider());
        }
        
        registerPages();
    }
    
    public void registerPages() {
        LoaderRegistry.registerPageLoader(ANVIL_CRUSHING_PAGE, BookAnvilCrushingPage::fromJson, BookAnvilCrushingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(PEDESTAL_CRAFTING_PAGE, BookPedestalCraftingPage::fromJson, BookPedestalCraftingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(FUSION_SHRINE_CRAFTING_PAGE, BookFusionShrineCraftingPage::fromJson, BookFusionShrineCraftingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(ENCHANTER_CRAFTING_PAGE, BookEnchanterCraftingPage::fromJson, BookEnchanterCraftingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(ENCHANTER_UPGRADING_PAGE, BookEnchanterUpgradingPage::fromJson, BookEnchanterUpgradingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(POTION_WORKSHOP_BREWING_PAGE, BookPotionWorkshopBrewingPage::fromJson, BookPotionWorkshopBrewingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(POTION_WORKSHOP_CRAFTING_PAGE, BookPotionWorkshopCraftingPage::fromJson, BookPotionWorkshopCraftingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(SPIRIT_INSTILLER_CRAFTING_PAGE, BookSpiritInstillerCraftingPage::fromJson, BookSpiritInstillerCraftingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(LIQUID_CRYSTAL_CONVERTING_PAGE, BookLiquidCrystalConvertingPage::fromJson, BookLiquidCrystalConvertingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(MIDNIGHT_SOLUTION_CONVERTING_PAGE, BookMidnightSolutionConvertingPage::fromJson, BookMidnightSolutionConvertingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(DRAGONROT_CONVERTING_PAGE, BookDragonrotConvertingPage::fromJson, BookDragonrotConvertingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(MUD_CONVERTING_PAGE, BookMudConvertingPage::fromJson, BookMudConvertingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(CRYSTALLARIEUM_GROWING_PAGE, BookCrystallarieumGrowingPage::fromJson, BookCrystallarieumGrowingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(CINDERHEARTH_SMELTING_PAGE, BookCinderhearthSmeltingPage::fromJson, BookCinderhearthSmeltingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(TITRATION_BARREL_FERMENTING_PAGE, BookTitrationBarrelFermentingPage::fromJson, BookTitrationBarrelFermentingPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(STATUS_EFFECT_PAGE, BookStatusEffectPage::fromJson, BookStatusEffectPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(HINT_PAGE, BookHintPage::fromJson, BookHintPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(CHECKLIST_PAGE, BookChecklistPage::fromJson, BookChecklistPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(CONFIRMATION_BUTTON_PAGE, BookConfirmationButtonPage::fromJson, BookConfirmationButtonPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(SNIPPET_PAGE, BookSnippetPage::fromJson, BookSnippetPage::fromNetwork);
//        LoaderRegistry.registerPageLoader(LINK_PAGE, BookLinkPage::fromJson, BookLinkPage::fromNetwork);
        LoaderRegistry.registerPageLoader(NBT_SPOTLIGHT_PAGE, BookNbtSpotlightPage::fromJson, BookNbtSpotlightPage::fromNetwork);
        LoaderRegistry.registerPageLoader(COLLECTION_PAGE, BookCollectionPage::fromJson, BookCollectionPage::fromNetwork);

        LoaderRegistry.registerPageLoader(PEDESTAL_CRAFTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(FUSION_SHRINE_CRAFTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(ENCHANTER_CRAFTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(ENCHANTER_UPGRADING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(POTION_WORKSHOP_BREWING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(POTION_WORKSHOP_CRAFTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SPIRIT_INSTILLER_CRAFTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(LIQUID_CRYSTAL_CONVERTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(MIDNIGHT_SOLUTION_CONVERTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(DRAGONROT_CONVERTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(MUD_CONVERTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CRYSTALLARIEUM_GROWING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CINDERHEARTH_SMELTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(TITRATION_BARREL_FERMENTING_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(STATUS_EFFECT_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(HINT_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CHECKLIST_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CONFIRMATION_BUTTON_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SNIPPET_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(LINK_PAGE, BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
    }

    @Override
    public void registerClient() {
        PageRendererRegistry.registerPageRenderer(ANVIL_CRUSHING_PAGE, p -> new BookAnvilCrushingPageRenderer((BookAnvilCrushingPage) p));
//        PageRendererRegistry.registerPageRenderer(ANVIL_CRUSHING_PAGE, p -> new BookAnvilCrushingPageRenderer((BookAnvilCrushingPage) p));
//        PageRendererRegistry.registerPageRenderer(PEDESTAL_CRAFTING_PAGE, p -> new BookPedestalCraftingPageRenderer((BookPedestalCraftingPage) p));
//        PageRendererRegistry.registerPageRenderer(FUSION_SHRINE_CRAFTING_PAGE, p -> new BookFusionShrineCraftingPageRenderer((BookFusionShrineCraftingPage) p));
//        PageRendererRegistry.registerPageRenderer(ENCHANTER_CRAFTING_PAGE, p -> new BookEnchanterCraftingPageRenderer((BookEnchanterCraftingPage) p));
//        PageRendererRegistry.registerPageRenderer(ENCHANTER_UPGRADING_PAGE, p -> new BookEnchanterUpgradingPageRenderer((BookEnchanterUpgradingPage) p));
//        PageRendererRegistry.registerPageRenderer(POTION_WORKSHOP_BREWING_PAGE, p -> new BookPotionWorkshopBrewingPageRenderer((BookPotionWorkshopBrewingPage) p));
//        PageRendererRegistry.registerPageRenderer(POTION_WORKSHOP_CRAFTING_PAGE, p -> new BookPotionWorkshopCraftingPageRenderer((BookPotionWorkshopCraftingPage) p));
//        PageRendererRegistry.registerPageRenderer(SPIRIT_INSTILLER_CRAFTING_PAGE, p -> new BookSpiritInstillerCraftingPageRenderer((BookSpiritInstillerCraftingPage) p));
//        PageRendererRegistry.registerPageRenderer(LIQUID_CRYSTAL_CONVERTING_PAGE, p -> new BookLiquidCrystalConvertingPageRenderer((BookLiquidCrystalConvertingPage) p));
//        PageRendererRegistry.registerPageRenderer(MIDNIGHT_SOLUTION_CONVERTING_PAGE, p -> new BookMidnightSolutionConvertingPageRenderer((BookMidnightSolutionConvertingPage) p));
//        PageRendererRegistry.registerPageRenderer(DRAGONROT_CONVERTING_PAGE, p -> new BookDragonrotConvertingPageRenderer((BookDragonrotConvertingPage) p));
//        PageRendererRegistry.registerPageRenderer(MUD_CONVERTING_PAGE, p -> new BookMudConvertingPageRenderer((BookMudConvertingPage) p));
//        PageRendererRegistry.registerPageRenderer(CRYSTALLARIEUM_GROWING_PAGE, p -> new BookCrystallarieumGrowingPageRenderer((BookCrystallarieumGrowingPage) p));
//        PageRendererRegistry.registerPageRenderer(CINDERHEARTH_SMELTING_PAGE, p -> new BookCinderhearthSmeltingPageRenderer((BookCinderhearthSmeltingPage) p));
//        PageRendererRegistry.registerPageRenderer(TITRATION_BARREL_FERMENTING_PAGE, p -> new BookTitrationBarrelFermentingPageRenderer((BookTitrationBarrelFermentingPage) p));
//        PageRendererRegistry.registerPageRenderer(STATUS_EFFECT_PAGE, p -> new BookStatusEffectPageRenderer((BookStatusEffectPage) p));
//        PageRendererRegistry.registerPageRenderer(HINT_PAGE, p -> new BookHintPageRenderer((BookHintPage) p));
//        PageRendererRegistry.registerPageRenderer(CHECKLIST_PAGE, p -> new BookChecklistPageRenderer((BookChecklistPage) p));
//        PageRendererRegistry.registerPageRenderer(CONFIRMATION_BUTTON_PAGE, p -> new BookConfirmationButtonPageRenderer((BookConfirmationButtonPage) p));
//        PageRendererRegistry.registerPageRenderer(SNIPPET_PAGE, p -> new BookSnippetPageRenderer((BookSnippetPage) p));
//        PageRendererRegistry.registerPageRenderer(LINK_PAGE, p -> new BookLinkPageRenderer((BookLinkPage) p));
        PageRendererRegistry.registerPageRenderer(NBT_SPOTLIGHT_PAGE, p -> new BookNbtSpotlightPageRenderer((BookNbtSpotlightPage) p));
        PageRendererRegistry.registerPageRenderer(COLLECTION_PAGE, p -> new BookCollectionPageRenderer((BookCollectionPage) p));
    }
    
}
