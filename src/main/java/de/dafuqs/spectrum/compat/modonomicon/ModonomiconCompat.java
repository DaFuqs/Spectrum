package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.book.page.BookEmptyPage;
import com.klikli_dev.modonomicon.client.render.page.*;
import com.klikli_dev.modonomicon.data.LoaderRegistry;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.modonomicon.client.pages.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.cinderhearth.CinderhearthRecipe;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.fluid_converting.DragonrotConvertingRecipe;
import de.dafuqs.spectrum.recipe.fluid_converting.LiquidCrystalConvertingRecipe;
import de.dafuqs.spectrum.recipe.fluid_converting.MidnightSolutionConvertingRecipe;
import de.dafuqs.spectrum.recipe.fluid_converting.MudConvertingRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.recipe.RecipeType;
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
        registerGatedRecipePage(ANVIL_CRUSHING_PAGE, SpectrumRecipeTypes.ANVIL_CRUSHING);
        registerGatedRecipePage(PEDESTAL_CRAFTING_PAGE, SpectrumRecipeTypes.PEDESTAL);
        registerGatedRecipePage(FUSION_SHRINE_CRAFTING_PAGE, SpectrumRecipeTypes.FUSION_SHRINE);
        registerGatedRecipePage(ENCHANTER_CRAFTING_PAGE, SpectrumRecipeTypes.ENCHANTER);
        registerGatedRecipePage(ENCHANTER_UPGRADING_PAGE, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE);
        registerGatedRecipePage(POTION_WORKSHOP_BREWING_PAGE, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING);
        registerGatedRecipePage(POTION_WORKSHOP_CRAFTING_PAGE, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING);
        registerGatedRecipePage(SPIRIT_INSTILLER_CRAFTING_PAGE, SpectrumRecipeTypes.SPIRIT_INSTILLING);
        registerGatedRecipePage(LIQUID_CRYSTAL_CONVERTING_PAGE, SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING);
        registerGatedRecipePage(MIDNIGHT_SOLUTION_CONVERTING_PAGE, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING);
        registerGatedRecipePage(DRAGONROT_CONVERTING_PAGE, SpectrumRecipeTypes.DRAGONROT_CONVERTING);
        registerGatedRecipePage(MUD_CONVERTING_PAGE, SpectrumRecipeTypes.MUD_CONVERTING);
        registerGatedRecipePage(CRYSTALLARIEUM_GROWING_PAGE, SpectrumRecipeTypes.CRYSTALLARIEUM);
        registerGatedRecipePage(CINDERHEARTH_SMELTING_PAGE, SpectrumRecipeTypes.CINDERHEARTH);
        registerGatedRecipePage(TITRATION_BARREL_FERMENTING_PAGE, SpectrumRecipeTypes.TITRATION_BARREL);

        LoaderRegistry.registerPageLoader(STATUS_EFFECT_PAGE, BookStatusEffectPage::fromJson, BookStatusEffectPage::fromNetwork);
        LoaderRegistry.registerPageLoader(HINT_PAGE, BookHintPage::fromJson, BookHintPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CHECKLIST_PAGE, BookChecklistPage::fromJson, BookChecklistPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CONFIRMATION_BUTTON_PAGE, BookConfirmationButtonPage::fromJson, BookConfirmationButtonPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SNIPPET_PAGE, BookSnippetPage::fromJson, BookSnippetPage::fromNetwork);
        LoaderRegistry.registerPageLoader(LINK_PAGE, BookLinkPage::fromJson, BookLinkPage::fromNetwork);
        LoaderRegistry.registerPageLoader(NBT_SPOTLIGHT_PAGE, BookNbtSpotlightPage::fromJson, BookNbtSpotlightPage::fromNetwork);
        LoaderRegistry.registerPageLoader(COLLECTION_PAGE, BookCollectionPage::fromJson, BookCollectionPage::fromNetwork);
    }

    private void registerGatedRecipePage(Identifier id, RecipeType<? extends GatedRecipe> recipeType) {
        LoaderRegistry.registerPageLoader(id,
                json -> BookGatedRecipePage.fromJson(id, recipeType, json),
                buffer -> BookGatedRecipePage.fromNetwork(id, recipeType, buffer));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerClient() {
        PageRendererRegistry.registerPageRenderer(ANVIL_CRUSHING_PAGE, p -> new BookAnvilCrushingPageRenderer((BookGatedRecipePage<AnvilCrushingRecipe>) p));
        PageRendererRegistry.registerPageRenderer(PEDESTAL_CRAFTING_PAGE, p -> new BookPedestalCraftingPageRenderer((BookGatedRecipePage<PedestalRecipe>) p));
        PageRendererRegistry.registerPageRenderer(FUSION_SHRINE_CRAFTING_PAGE, p -> new BookFusionShrineCraftingPageRenderer((BookGatedRecipePage<FusionShrineRecipe>) p));
        PageRendererRegistry.registerPageRenderer(ENCHANTER_CRAFTING_PAGE, p -> new BookEnchanterCraftingPageRenderer((BookGatedRecipePage<EnchanterRecipe>) p));
        PageRendererRegistry.registerPageRenderer(ENCHANTER_UPGRADING_PAGE, p -> new BookEnchanterUpgradingPageRenderer((BookGatedRecipePage<EnchantmentUpgradeRecipe>) p));
        PageRendererRegistry.registerPageRenderer(POTION_WORKSHOP_BREWING_PAGE, p -> new BookPotionWorkshopPageRenderer<>((BookGatedRecipePage<PotionWorkshopBrewingRecipe>) p));
        PageRendererRegistry.registerPageRenderer(POTION_WORKSHOP_CRAFTING_PAGE, p -> new BookPotionWorkshopPageRenderer<>((BookGatedRecipePage<PotionWorkshopCraftingRecipe>) p));
        PageRendererRegistry.registerPageRenderer(SPIRIT_INSTILLER_CRAFTING_PAGE, p -> new BookSpiritInstillerCraftingPageRenderer((BookGatedRecipePage<SpiritInstillerRecipe>) p));
        PageRendererRegistry.registerPageRenderer(CRYSTALLARIEUM_GROWING_PAGE, p -> new BookCrystallarieumGrowingPageRenderer((BookGatedRecipePage<CrystallarieumRecipe>) p));
        PageRendererRegistry.registerPageRenderer(CINDERHEARTH_SMELTING_PAGE, p -> new BookCinderhearthSmeltingPageRenderer((BookGatedRecipePage<CinderhearthRecipe>) p));
        PageRendererRegistry.registerPageRenderer(TITRATION_BARREL_FERMENTING_PAGE, p -> new BookTitrationBarrelFermentingPageRenderer((BookGatedRecipePage<TitrationBarrelRecipe>) p));
        
        PageRendererRegistry.registerPageRenderer(STATUS_EFFECT_PAGE, p -> new BookStatusEffectPageRenderer((BookStatusEffectPage) p));
        PageRendererRegistry.registerPageRenderer(HINT_PAGE, p -> new BookHintPageRenderer((BookHintPage) p));
        PageRendererRegistry.registerPageRenderer(CHECKLIST_PAGE, p -> new BookChecklistPageRenderer((BookChecklistPage) p));
        PageRendererRegistry.registerPageRenderer(CONFIRMATION_BUTTON_PAGE, p -> new BookConfirmationButtonPageRenderer((BookConfirmationButtonPage) p));
        PageRendererRegistry.registerPageRenderer(SNIPPET_PAGE, p -> new BookSnippetPageRenderer((BookSnippetPage) p));
        PageRendererRegistry.registerPageRenderer(LINK_PAGE, p -> new BookLinkPageRenderer((BookLinkPage) p));
        PageRendererRegistry.registerPageRenderer(NBT_SPOTLIGHT_PAGE, p -> new BookSpotlightPageRenderer((BookNbtSpotlightPage) p));
        PageRendererRegistry.registerPageRenderer(COLLECTION_PAGE, p -> new BookCollectionPageRenderer((BookCollectionPage) p));

        PageRendererRegistry.registerPageRenderer(LIQUID_CRYSTAL_CONVERTING_PAGE, p -> new BookFluidConvertingPageRenderer<>((BookGatedRecipePage<LiquidCrystalConvertingRecipe>) p) {
            @Override
            public Identifier getBackgroundTexture() {
                return SpectrumCommon.locate("textures/gui/guidebook/liquid_crystal.png");
            }
        });

        PageRendererRegistry.registerPageRenderer(MIDNIGHT_SOLUTION_CONVERTING_PAGE, p -> new BookFluidConvertingPageRenderer<>((BookGatedRecipePage<MidnightSolutionConvertingRecipe>) p) {
            @Override
            public Identifier getBackgroundTexture() {
                return SpectrumCommon.locate("textures/gui/guidebook/midnight_solution.png");
            }
        });

        PageRendererRegistry.registerPageRenderer(DRAGONROT_CONVERTING_PAGE, p -> new BookFluidConvertingPageRenderer<>((BookGatedRecipePage<DragonrotConvertingRecipe>) p) {
            @Override
            public Identifier getBackgroundTexture() {
                return SpectrumCommon.locate("textures/gui/guidebook/dragonrot.png");
            }
        });

        PageRendererRegistry.registerPageRenderer(MUD_CONVERTING_PAGE, p -> new BookFluidConvertingPageRenderer<>((BookGatedRecipePage<MudConvertingRecipe>) p) {
            @Override
            public Identifier getBackgroundTexture() {
                return SpectrumCommon.locate("textures/gui/guidebook/mud.png");
            }
        });
    }

}
