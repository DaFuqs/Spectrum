package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.inventories.PedestalScreen;
import de.dafuqs.spectrum.inventories.PotionWorkshopScreen;
import de.dafuqs.spectrum.items.magic_items.NaturesStaffItem;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.midnight_solution_converting.MidnightSolutionConvertingRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.ISpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.architectury.event.EventResult;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;

@Environment(EnvType.CLIENT)
public class REIClientIntegration implements REIClientPlugin {

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new PedestalCraftingCategory());
		registry.add(new AnvilCrushingCategory());
		registry.add(new FusionShrineCategory());
		registry.add(new NaturesStaffConversionsCategory());
		registry.add(new EnchanterCategory());
		registry.add(new EnchantmentUpgradeCategory());
		registry.add(new PotionWorkshopBrewingCategory());
		registry.add(new PotionWorkshopCraftingCategory());
		registry.add(new SpiritInstillerCategory());
		registry.add(new MidnightSolutionConvertingCategory());
		
		registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(SpectrumItems.CRAFTING_TABLET));
		
		EntryIngredient pedestalStacks = EntryIngredient.of(
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_TOPAZ),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_CITRINE),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_ALL_BASIC),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_ONYX),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_MOONSTONE)
		);
		registry.addWorkstations(BuiltinPlugin.CRAFTING, pedestalStacks);
		registry.addWorkstations(SpectrumPlugins.PEDESTAL_CRAFTING, pedestalStacks);
		registry.addWorkstations(SpectrumPlugins.ANVIL_CRUSHING, EntryStacks.of(Blocks.ANVIL), EntryStacks.of(SpectrumBlocks.BEDROCK_ANVIL), EntryStacks.of(SpectrumBlocks.SCARLET_FRAGMENT_BLOCK), EntryStacks.of(SpectrumBlocks.PALETUR_FRAGMENT_BLOCK));
		
		EntryIngredient fusionShrineStacks = EntryIngredient.of(
				EntryStacks.of(SpectrumBlocks.FUSION_SHRINE_BASALT),
				EntryStacks.of(SpectrumBlocks.FUSION_SHRINE_CALCITE)
		);
		registry.addWorkstations(SpectrumPlugins.FUSION_SHRINE, fusionShrineStacks);
		registry.addWorkstations(SpectrumPlugins.NATURES_STAFF, EntryStacks.of(SpectrumItems.NATURES_STAFF));
		registry.addWorkstations(SpectrumPlugins.ENCHANTER, EntryStacks.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstations(SpectrumPlugins.ENCHANTMENT_UPGRADE, EntryStacks.of(SpectrumBlocks.ENCHANTER));
		
		registry.addWorkstations(BuiltinPlugin.BREWING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_BREWING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_CRAFTING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		
		registry.addWorkstations(SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING, EntryStacks.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET));
		registry.addWorkstations(SpectrumPlugins.SPIRIT_INSTILLER, EntryStacks.of(SpectrumBlocks.SPIRIT_INSTILLER));

		// For item crushing and others are in-world recipes there is no gui to fill
		// therefore the plus button is obsolete
		registry.removePlusButton(SpectrumPlugins.ANVIL_CRUSHING);
		registry.removePlusButton(SpectrumPlugins.FUSION_SHRINE);
		registry.removePlusButton(SpectrumPlugins.NATURES_STAFF);
		registry.removePlusButton(SpectrumPlugins.ENCHANTER);
		registry.removePlusButton(SpectrumPlugins.ENCHANTMENT_UPGRADE);
		registry.removePlusButton(SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING);
		registry.removePlusButton(SpectrumPlugins.SPIRIT_INSTILLER);
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerFiller(AnvilCrushingRecipe.class, AnvilCrushingRecipeDisplay::new);
		registry.registerRecipeFiller(PedestalCraftingRecipe.class, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingRecipeDisplay::new);
		registry.registerRecipeFiller(FusionShrineRecipe.class, SpectrumRecipeTypes.FUSION_SHRINE, FusionShrineRecipeDisplay::new);
		NaturesStaffItem.BLOCK_CONVERSIONS.forEach((key, value) -> registry.add(new NaturesStaffConversionsDisplay(EntryStacks.of(key), EntryStacks.of(value.getBlock()))));
		registry.registerRecipeFiller(EnchanterRecipe.class, SpectrumRecipeTypes.ENCHANTER, EnchanterRecipeDisplay::new);
		registry.registerRecipeFiller(EnchantmentUpgradeRecipe.class, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, EnchantmentUpgradeRecipeDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopBrewingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, PotionWorkshopBrewingRecipeDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopCraftingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, PotionWorkshopCraftingRecipeDisplay::new);
		registry.registerRecipeFiller(ISpiritInstillerRecipe.class, SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE, SpiritInstillerRecipeDisplay::new);
		registry.registerRecipeFiller(MidnightSolutionConvertingRecipe.class, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING_RECIPE, MidnightSolutionConvertingDisplay::new);
		
		if(!SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			// do not list recipes in REI at all, until they are unlocked
			registry.registerVisibilityPredicate((category, display) -> {
				if(display instanceof GatedRecipeDisplay gatedRecipeDisplay && !gatedRecipeDisplay.isUnlocked()) {
					return EventResult.interruptFalse();
				}
				return EventResult.pass();
			});
		}

	}

	/**
	 * Where in the screens gui the player has to click
	 * to get to the recipe overview
	 * Only use for recipe types that are crafted in a gui
	 */
	@Override
	public void registerScreens(ScreenRegistry registry) {
		// Since the pedestal can craft both vanilla and pedestal recipes
		// we split the "arrow" part of the gui into two parts => faster access
		registry.registerContainerClickArea(new Rectangle(89, 37, 10, 15), PedestalScreen.class, BuiltinPlugin.CRAFTING);
		registry.registerContainerClickArea(new Rectangle(100, 37, 11, 15), PedestalScreen.class, SpectrumPlugins.PEDESTAL_CRAFTING);
		
		registry.registerContainerClickArea(new Rectangle(28, 41, 10, 42), PotionWorkshopScreen.class, SpectrumPlugins.POTION_WORKSHOP_BREWING);
		registry.registerContainerClickArea(new Rectangle(28, 41, 10, 42), PotionWorkshopScreen.class, SpectrumPlugins.POTION_WORKSHOP_CRAFTING);
	}

}
