package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.book.page.BookEmptyPage;
import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.modonomicon.data.LoaderRegistry;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.modonomicon.client.pages.BookCollectionPageRenderer;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookCollectionPage;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookNbtSpotlightPage;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.Identifier;

public class ModonomiconCompat extends SpectrumIntegrationPacks.ModIntegrationPack {

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
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("anvil_crushing"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("pedestal_crafting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("fusion_shrine_crafting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("enchanter_crafting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("enchanter_upgrading"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("potion_workshop_brewing"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("potion_workshop_crafting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("spirit_instiller_crafting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("liquid_crystal_converting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("midnight_solution_converting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("dragonrot_converting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("mud_converting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("crystallarieum_growing"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("cinderhearth_smelting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("titration_barrel_fermenting"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("status_effect"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("hint"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("checklist"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("confirmation_button"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("snippet"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SpectrumCommon.locate("link"), BookEmptyPage::fromJson, BookEmptyPage::fromNetwork);
        LoaderRegistry.registerPageLoader(NBT_SPOTLIGHT_PAGE, BookNbtSpotlightPage::fromJson, BookNbtSpotlightPage::fromNetwork);
        LoaderRegistry.registerPageLoader(COLLECTION_PAGE, BookCollectionPage::fromJson, BookCollectionPage::fromNetwork);
    }

    @Override
    public void registerClient() {
//        PageRendererRegistry.registerPageRenderer(NBT_SPOTLIGHT_PAGE, page -> page instanceof BookSpotlightPage p ? new BookSpotlightPageRenderer(p) : null);
        PageRendererRegistry.registerPageRenderer(COLLECTION_PAGE, page -> page instanceof BookCollectionPage p ? new BookCollectionPageRenderer(p) : null);
    }
    
}
