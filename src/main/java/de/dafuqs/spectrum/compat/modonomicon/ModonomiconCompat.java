package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.client.render.page.*;
import com.klikli_dev.modonomicon.data.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.modonomicon.client.pages.*;
import de.dafuqs.spectrum.compat.modonomicon.page_types.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.compat.modonomicon.unlock_conditions.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

public class ModonomiconCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
    
    // Entry Types
    public static final Identifier ENTRY_LINK_PAGE_TYPE = SpectrumCommon.locate("entry_link");
    
    // Page Types
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
    public static final Identifier PRIMORDIAL_FIRE_BURNING_PAGE = SpectrumCommon.locate("primordial_fire_burning");
    
    // Unlock Conditions
    public static final Identifier ENCHANTMENT_REGISTERED = SpectrumCommon.locate("enchantment_registered");
    public static final Identifier RECIPE_LOADED_AND_UNLOCKED = SpectrumCommon.locate("recipe_loaded_and_unlocked");
    public static final Identifier NOT = SpectrumCommon.locate("not");
    
    @Override
    public void register() {
        LoaderRegistry.registerEntryType(ENTRY_LINK_PAGE_TYPE, EntryLinkBookEntry::fromJson, EntryLinkBookEntry::fromNetwork);
        
        registerPages();
        registerUnlockConditions();
    }
	
	private void registerPages() {
        registerGatedRecipePage(ANVIL_CRUSHING_PAGE, SpectrumRecipeTypes.ANVIL_CRUSHING, false);
        registerGatedRecipePage(PEDESTAL_CRAFTING_PAGE, SpectrumRecipeTypes.PEDESTAL, false);
        registerGatedRecipePage(FUSION_SHRINE_CRAFTING_PAGE, SpectrumRecipeTypes.FUSION_SHRINE, false);
        registerGatedRecipePage(ENCHANTER_CRAFTING_PAGE, SpectrumRecipeTypes.ENCHANTER, false);
        registerGatedRecipePage(ENCHANTER_UPGRADING_PAGE, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, false);
        registerGatedRecipePage(POTION_WORKSHOP_BREWING_PAGE, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, false);
        registerGatedRecipePage(POTION_WORKSHOP_CRAFTING_PAGE, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, false);
        registerGatedRecipePage(SPIRIT_INSTILLER_CRAFTING_PAGE, SpectrumRecipeTypes.SPIRIT_INSTILLING, true);
        registerGatedRecipePage(LIQUID_CRYSTAL_CONVERTING_PAGE, SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING, false);
        registerGatedRecipePage(MIDNIGHT_SOLUTION_CONVERTING_PAGE, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, false);
        registerGatedRecipePage(DRAGONROT_CONVERTING_PAGE, SpectrumRecipeTypes.DRAGONROT_CONVERTING, false);
        registerGatedRecipePage(MUD_CONVERTING_PAGE, SpectrumRecipeTypes.MUD_CONVERTING, false);
        registerGatedRecipePage(CRYSTALLARIEUM_GROWING_PAGE, SpectrumRecipeTypes.CRYSTALLARIEUM, false);
        registerGatedRecipePage(CINDERHEARTH_SMELTING_PAGE, SpectrumRecipeTypes.CINDERHEARTH, false);
        registerGatedRecipePage(TITRATION_BARREL_FERMENTING_PAGE, SpectrumRecipeTypes.TITRATION_BARREL, true);
        registerGatedRecipePage(PRIMORDIAL_FIRE_BURNING_PAGE, SpectrumRecipeTypes.PRIMORDIAL_FIRE_BURNING, false);

        LoaderRegistry.registerPageLoader(STATUS_EFFECT_PAGE, BookStatusEffectPage::fromJson, BookStatusEffectPage::fromNetwork);
        LoaderRegistry.registerPageLoader(HINT_PAGE, BookHintPage::fromJson, BookHintPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CHECKLIST_PAGE, BookChecklistPage::fromJson, BookChecklistPage::fromNetwork);
        LoaderRegistry.registerPageLoader(CONFIRMATION_BUTTON_PAGE, BookConfirmationButtonPage::fromJson, BookConfirmationButtonPage::fromNetwork);
        LoaderRegistry.registerPageLoader(SNIPPET_PAGE, BookSnippetPage::fromJson, BookSnippetPage::fromNetwork);
        LoaderRegistry.registerPageLoader(LINK_PAGE, BookLinkPage::fromJson, BookLinkPage::fromNetwork);
        LoaderRegistry.registerPageLoader(NBT_SPOTLIGHT_PAGE, BookNbtSpotlightPage::fromJson, BookNbtSpotlightPage::fromNetwork);
        LoaderRegistry.registerPageLoader(COLLECTION_PAGE, BookCollectionPage::fromJson, BookCollectionPage::fromNetwork);
    }
    
    private void registerGatedRecipePage(Identifier id, RecipeType<? extends GatedRecipe<?>> recipeType, boolean supportsTwoRecipesOnOnePage) {
        LoaderRegistry.registerPageLoader(id,
                json -> BookGatedRecipePage.fromJson(id, recipeType, json, supportsTwoRecipesOnOnePage),
                buffer -> BookGatedRecipePage.fromNetwork(id, recipeType, buffer));
    }
	
	private void registerUnlockConditions() {
        LoaderRegistry.registerConditionLoader(ENCHANTMENT_REGISTERED, EnchantmentRegisteredCondition::fromJson, EnchantmentRegisteredCondition::fromNetwork);
        LoaderRegistry.registerConditionLoader(RECIPE_LOADED_AND_UNLOCKED, RecipesLoadedAndUnlockedCondition::fromJson, RecipesLoadedAndUnlockedCondition::fromNetwork);
        LoaderRegistry.registerConditionLoader(NOT, NotCondition::fromJson, NotCondition::fromNetwork);
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
        PageRendererRegistry.registerPageRenderer(PRIMORDIAL_FIRE_BURNING_PAGE, p -> new BookPrimordialFireBurningPageRenderer<>((BookGatedRecipePage<PrimordialFireBurningRecipe>) p));
        
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
