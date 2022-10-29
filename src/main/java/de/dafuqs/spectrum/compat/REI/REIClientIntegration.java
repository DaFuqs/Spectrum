package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.mob_blocks.FirestarterMobBlock;
import de.dafuqs.spectrum.blocks.mob_blocks.FreezingMobBlock;
import de.dafuqs.spectrum.compat.REI.plugins.*;
import de.dafuqs.spectrum.inventories.CinderhearthScreen;
import de.dafuqs.spectrum.inventories.PedestalScreen;
import de.dafuqs.spectrum.inventories.PotionWorkshopScreen;
import de.dafuqs.spectrum.items.magic_items.NaturesStaffItem;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.cinderhearth.CinderhearthRecipe;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.ink_converting.InkConvertingRecipe;
import de.dafuqs.spectrum.recipe.midnight_solution_converting.MidnightSolutionConvertingRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopReactingRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
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
		registry.add(new PotionWorkshopReactingCategory());
		registry.add(new SpiritInstillingCategory());
		registry.add(new MidnightSolutionConvertingCategory());
		registry.add(new HeatingCategory());
		registry.add(new FreezingCategory());
		registry.add(new InkConvertingCategory());
		registry.add(new CrystallarieumCategory());
		registry.add(new CinderhearthCategory());
		registry.add(new TitrationBarrelCategory());
		
		registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(SpectrumItems.CRAFTING_TABLET));
		
		EntryIngredient pedestals = EntryIngredient.of(
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_TOPAZ),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_CITRINE),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_ALL_BASIC),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_ONYX),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_MOONSTONE)
		);
		registry.addWorkstations(BuiltinPlugin.CRAFTING, pedestals);
		registry.addWorkstations(SpectrumPlugins.PEDESTAL_CRAFTING, pedestals);
		
		registry.addWorkstations(SpectrumPlugins.ANVIL_CRUSHING, EntryStacks.of(Blocks.ANVIL), EntryStacks.of(SpectrumBlocks.BEDROCK_ANVIL), EntryStacks.of(SpectrumBlocks.SCARLET_FRAGMENT_BLOCK), EntryStacks.of(SpectrumBlocks.PALETUR_FRAGMENT_BLOCK));
		registry.addWorkstations(SpectrumPlugins.FUSION_SHRINE, EntryIngredient.of(EntryStacks.of(SpectrumBlocks.FUSION_SHRINE_BASALT), EntryStacks.of(SpectrumBlocks.FUSION_SHRINE_CALCITE)));
		registry.addWorkstations(SpectrumPlugins.NATURES_STAFF, EntryStacks.of(SpectrumItems.NATURES_STAFF));
		registry.addWorkstations(SpectrumPlugins.HEATING, EntryStacks.of(SpectrumBlocks.BLAZE_MOB_BLOCK));
		registry.addWorkstations(SpectrumPlugins.FREEZING, EntryStacks.of(SpectrumBlocks.POLAR_BEAR_MOB_BLOCK));
		registry.addWorkstations(SpectrumPlugins.ENCHANTER, EntryStacks.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstations(SpectrumPlugins.ENCHANTMENT_UPGRADE, EntryStacks.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstations(SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING, EntryStacks.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET));
		registry.addWorkstations(SpectrumPlugins.SPIRIT_INSTILLER, EntryStacks.of(SpectrumBlocks.SPIRIT_INSTILLER));
		registry.addWorkstations(SpectrumPlugins.INK_CONVERTING, EntryStacks.of(SpectrumBlocks.COLOR_PICKER));
		registry.addWorkstations(SpectrumPlugins.CRYSTALLARIEUM, EntryStacks.of(SpectrumBlocks.CRYSTALLARIEUM));
		
		registry.addWorkstations(BuiltinPlugin.BREWING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_BREWING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_CRAFTING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_REACTING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		
		registry.addWorkstations(BuiltinPlugin.BLASTING, EntryStacks.of(SpectrumBlocks.CINDERHEARTH));
		registry.addWorkstations(SpectrumPlugins.CINDERHEARTH, EntryStacks.of(SpectrumBlocks.CINDERHEARTH));
		registry.addWorkstations(SpectrumPlugins.TITRATION_BARREL, EntryStacks.of(SpectrumBlocks.TITRATION_BARREL));
		
		// For item crushing and others are in-world recipes there is no gui to fill
		// therefore the plus button is obsolete
		
		// this is getting removed for... whatever reason
		// registry.removePlusButton(SpectrumPlugins.ANVIL_CRUSHING);
		// registry.removePlusButton(SpectrumPlugins.FUSION_SHRINE);
		// registry.removePlusButton(SpectrumPlugins.NATURES_STAFF);
		// registry.removePlusButton(SpectrumPlugins.POTION_WORKSHOP_REACTING);
		// registry.removePlusButton(SpectrumPlugins.ENCHANTER);
		// registry.removePlusButton(SpectrumPlugins.ENCHANTMENT_UPGRADE);
		// registry.removePlusButton(SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING);
		// registry.removePlusButton(SpectrumPlugins.SPIRIT_INSTILLER);
		// registry.removePlusButton(SpectrumPlugins.HEATING);
		// registry.removePlusButton(SpectrumPlugins.FREEZING);
	}
	
	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerFiller(AnvilCrushingRecipe.class, AnvilCrushingDisplay::new);
		registry.registerRecipeFiller(PedestalCraftingRecipe.class, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingDisplay::new);
		registry.registerRecipeFiller(FusionShrineRecipe.class, SpectrumRecipeTypes.FUSION_SHRINE, FusionShrineDisplay::new);
		registry.registerRecipeFiller(EnchanterRecipe.class, SpectrumRecipeTypes.ENCHANTER, EnchanterDisplay::new);
		registry.registerRecipeFiller(EnchantmentUpgradeRecipe.class, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, EnchantmentUpgradeDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopBrewingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, PotionWorkshopBrewingDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopCraftingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, PotionWorkshopCraftingDisplay::new);
		registry.registerRecipeFiller(SpiritInstillerRecipe.class, SpectrumRecipeTypes.SPIRIT_INSTILLING, SpiritInstillingDisplay::new);
		registry.registerRecipeFiller(MidnightSolutionConvertingRecipe.class, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, MidnightSolutionConvertingDisplay::new);
		registry.registerRecipeFiller(InkConvertingRecipe.class, SpectrumRecipeTypes.INK_CONVERTING, InkConvertingDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopReactingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, PotionWorkshopReactingDisplay::new);
		registry.registerRecipeFiller(CrystallarieumRecipe.class, SpectrumRecipeTypes.CRYSTALLARIEUM, CrystallarieumDisplay::new);
		registry.registerRecipeFiller(CinderhearthRecipe.class, SpectrumRecipeTypes.CINDERHEARTH, CinderhearthDisplay::new);
		registry.registerRecipeFiller(ITitrationBarrelRecipe.class, SpectrumRecipeTypes.TITRATION_BARREL, TitrationBarrelDisplay::new);
		
		NaturesStaffItem.BLOCK_CONVERSIONS.forEach((key, value) -> registry.add(new NaturesStaffConversionsDisplay(EntryStacks.of(key), EntryStacks.of(value.getBlock()))));
		FreezingMobBlock.FREEZING_STATE_MAP.forEach((key, value) -> registry.add(new FreezingDisplay(BlockToBlockWithChanceDisplay.blockToEntryStack(key.getBlock()), BlockToBlockWithChanceDisplay.blockToEntryStack(value.getLeft().getBlock()), value.getRight())));
		FreezingMobBlock.FREEZING_MAP.forEach((key, value) -> registry.add(new FreezingDisplay(BlockToBlockWithChanceDisplay.blockToEntryStack(key), BlockToBlockWithChanceDisplay.blockToEntryStack(value.getLeft().getBlock()), value.getRight())));
		FirestarterMobBlock.BURNING_MAP.forEach((key, value) -> registry.add(new HeatingDisplay(BlockToBlockWithChanceDisplay.blockToEntryStack(key), BlockToBlockWithChanceDisplay.blockToEntryStack(value.getLeft().getBlock()), value.getRight())));
		
		
		registry.registerVisibilityPredicate((category, display) -> {
			// do not list recipes in REI at all, until they are unlocked
			// secret recipes are never shown
			if (display instanceof GatedRecipeDisplay gatedRecipeDisplay) {
				if (gatedRecipeDisplay.isSecret()) {
					return EventResult.interruptFalse();
				}
				if (!SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked && !gatedRecipeDisplay.isUnlocked()) {
					return EventResult.interruptFalse();
				}
			}
			return EventResult.pass();
		});
		
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
		
		registry.registerContainerClickArea(new Rectangle(35, 33, 22, 15), CinderhearthScreen.class, SpectrumPlugins.CINDERHEARTH);
		registry.registerContainerClickArea(new Rectangle(35, 33, 22, 15), CinderhearthScreen.class, BuiltinPlugin.BLASTING);
		
		registry.registerDecider(REIOverlayDecider.INSTANCE);
	}
	
}
