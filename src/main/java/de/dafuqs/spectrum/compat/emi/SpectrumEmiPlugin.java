package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.compat.emi.handlers.*;
import de.dafuqs.spectrum.compat.emi.recipes.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.config.*;
import dev.emi.emi.runtime.*;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.fluid.*;
import net.minecraft.inventory.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

import java.util.*;
import java.util.function.*;

public class SpectrumEmiPlugin implements EmiPlugin {

	@Override
	public void register(EmiRegistry registry) {
		registerCategories(registry);
		registerRecipes(registry);
		registerRecipeHandlers(registry);
		registerDragDropHandlers(registry);
	}

	@SuppressWarnings("UnstableApiUsage")
    public void registerDragDropHandlers(EmiRegistry registry) {
		// Registering here since this is a trivial solution.
		var handlerOne = new EmiDragDropHandler.SlotBased<>((_ignored, slot) -> slot instanceof ShadowSlot && slot.inventory instanceof FilterConfigurable.FilterInventory,
				(screen, slot, ingredient) -> {
					if (ingredient instanceof ItemEmiStack stack)
						((FilterConfigurable.FilterInventory)slot.inventory).getClicker().clickShadowSlot(screen.getScreenHandler().syncId, slot, stack.getItemStack());
				});

		registerDragDropHandler(registry, BlackHoleChestScreen.class, handlerOne);
		registerDragDropHandler(registry, FilteringScreen.class, handlerOne);
	}

	// Type erasure BS
	private void registerDragDropHandler(EmiRegistry registry, Class<? extends HandledScreen<?>> clazz, EmiDragDropHandler<HandledScreen<?>> handler) {
		registry.addDragDropHandler((Class<HandledScreen<?>>)clazz, handler);
	}

	public void registerCategories(EmiRegistry registry) {
		registry.addCategory(SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING);
		registry.addCategory(SpectrumEmiRecipeCategories.ANVIL_CRUSHING);
		registry.addCategory(SpectrumEmiRecipeCategories.FUSION_SHRINE);
		registry.addCategory(SpectrumEmiRecipeCategories.NATURES_STAFF);
		registry.addCategory(SpectrumEmiRecipeCategories.ENCHANTER);
		registry.addCategory(SpectrumEmiRecipeCategories.ENCHANTMENT_UPGRADE);
		registry.addCategory(SpectrumEmiRecipeCategories.POTION_WORKSHOP_BREWING);
		registry.addCategory(SpectrumEmiRecipeCategories.POTION_WORKSHOP_CRAFTING);
		registry.addCategory(SpectrumEmiRecipeCategories.POTION_WORKSHOP_REACTING);
		registry.addCategory(SpectrumEmiRecipeCategories.SPIRIT_INSTILLER);
		registry.addCategory(SpectrumEmiRecipeCategories.MUD_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.LIQUID_CRYSTAL_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.DRAGONROT_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.HEATING);
		registry.addCategory(SpectrumEmiRecipeCategories.FREEZING);
		registry.addCategory(SpectrumEmiRecipeCategories.INK_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.CRYSTALLARIEUM);
		registry.addCategory(SpectrumEmiRecipeCategories.CINDERHEARTH);
		registry.addCategory(SpectrumEmiRecipeCategories.TITRATION_BARREL);
		registry.addCategory(SpectrumEmiRecipeCategories.PRIMORDIAL_FIRE_BURNING);
		
		EmiIngredient pedestals = EmiIngredient.of(List.of(
				EmiStack.of(SpectrumBlocks.PEDESTAL_BASIC_TOPAZ),
				EmiStack.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST),
				EmiStack.of(SpectrumBlocks.PEDESTAL_BASIC_CITRINE),
				EmiStack.of(SpectrumBlocks.PEDESTAL_ALL_BASIC),
				EmiStack.of(SpectrumBlocks.PEDESTAL_ONYX),
				EmiStack.of(SpectrumBlocks.PEDESTAL_MOONSTONE)
		));
		registry.addWorkstation(SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING, pedestals);
		if (SpectrumCommon.CONFIG.canPedestalCraftVanillaRecipes()) {
			registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, pedestals);
		}
		
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(SpectrumItems.CRAFTING_TABLET));
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(SpectrumBlocks.RESTOCKING_CHEST));
		registry.addWorkstation(VanillaEmiRecipeCategories.BLASTING, EmiStack.of(SpectrumBlocks.CINDERHEARTH));
		
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(Blocks.ANVIL));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.BEDROCK_ANVIL));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.STRATINE_FRAGMENT_BLOCK));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK));
		registry.addWorkstation(SpectrumEmiRecipeCategories.FUSION_SHRINE, EmiStack.of(SpectrumBlocks.FUSION_SHRINE_BASALT));
		registry.addWorkstation(SpectrumEmiRecipeCategories.FUSION_SHRINE, EmiStack.of(SpectrumBlocks.FUSION_SHRINE_CALCITE));
		registry.addWorkstation(SpectrumEmiRecipeCategories.NATURES_STAFF, EmiStack.of(SpectrumItems.NATURES_STAFF));
		registry.addWorkstation(SpectrumEmiRecipeCategories.HEATING, EmiStack.of(SpectrumBlocks.BLAZE_IDOL));
		registry.addWorkstation(SpectrumEmiRecipeCategories.FREEZING, EmiStack.of(SpectrumBlocks.POLAR_BEAR_IDOL));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ENCHANTER, EmiStack.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ENCHANTMENT_UPGRADE, EmiStack.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstation(SpectrumEmiRecipeCategories.MUD_CONVERTING, EmiStack.of(SpectrumItems.MUD_BUCKET));
		registry.addWorkstation(SpectrumEmiRecipeCategories.LIQUID_CRYSTAL_CONVERTING, EmiStack.of(SpectrumItems.LIQUID_CRYSTAL_BUCKET));
		registry.addWorkstation(SpectrumEmiRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING, EmiStack.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET));
		registry.addWorkstation(SpectrumEmiRecipeCategories.DRAGONROT_CONVERTING, EmiStack.of(SpectrumItems.DRAGONROT_BUCKET));
		registry.addWorkstation(SpectrumEmiRecipeCategories.SPIRIT_INSTILLER, EmiStack.of(SpectrumBlocks.SPIRIT_INSTILLER));
		registry.addWorkstation(SpectrumEmiRecipeCategories.INK_CONVERTING, EmiStack.of(SpectrumBlocks.COLOR_PICKER));
		registry.addWorkstation(SpectrumEmiRecipeCategories.CRYSTALLARIEUM, EmiStack.of(SpectrumBlocks.CRYSTALLARIEUM));
		registry.addWorkstation(SpectrumEmiRecipeCategories.POTION_WORKSHOP_BREWING, EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstation(SpectrumEmiRecipeCategories.POTION_WORKSHOP_CRAFTING, EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstation(SpectrumEmiRecipeCategories.POTION_WORKSHOP_REACTING, EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstation(SpectrumEmiRecipeCategories.CINDERHEARTH, EmiStack.of(SpectrumBlocks.CINDERHEARTH));
		registry.addWorkstation(SpectrumEmiRecipeCategories.TITRATION_BARREL, EmiStack.of(SpectrumBlocks.TITRATION_BARREL));
		registry.addWorkstation(SpectrumEmiRecipeCategories.PRIMORDIAL_FIRE_BURNING, EmiIngredient.of(List.of(EmiStack.of(SpectrumItems.DOOMBLOOM_SEED), EmiStack.of(SpectrumItems.PRIMORDIAL_LIGHTER), EmiStack.of(SpectrumBlocks.INCANDESCENT_AMALGAM), EmiStack.of(SpectrumItems.PIPE_BOMB))));
	}

	public void registerRecipes(EmiRegistry registry) {
		// TODO: Register our recipes ourselves
		// right now dev.emi.emi.VanillaPlugin handles them
		// which does not process the unlock check
		//addAll(registry, RecipeType.CRAFTING, ShapedGatedCraftingEMIRecipe::new);
		//addAll(registry, RecipeType.CRAFTING, ShapelessGatedCraftingEMIRecipe::new);

		addAll(registry, SpectrumRecipeTypes.ANVIL_CRUSHING, AnvilCrushingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.FUSION_SHRINE, FusionShrineEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.ENCHANTER, r -> new EnchanterEmiRecipeGated(SpectrumEmiRecipeCategories.ENCHANTER, r));
		addAll(registry, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, r -> new EnchantmentUpgradeEmiRecipeGated(SpectrumEmiRecipeCategories.ENCHANTMENT_UPGRADE, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, r -> new PotionWorkshopEmiRecipeGated(SpectrumEmiRecipeCategories.POTION_WORKSHOP_BREWING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, r -> new PotionWorkshopEmiRecipeGated(SpectrumEmiRecipeCategories.POTION_WORKSHOP_CRAFTING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, PotionWorkshopReactingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.SPIRIT_INSTILLING, SpiritInstillingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.MUD_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.MUD_CONVERTING, r));
		addAll(registry, SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.LIQUID_CRYSTAL_CONVERTING, r));
		addAll(registry, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING, r));
		addAll(registry, SpectrumRecipeTypes.DRAGONROT_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.DRAGONROT_CONVERTING, r));
		addAll(registry, SpectrumRecipeTypes.INK_CONVERTING, InkConvertingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.CRYSTALLARIEUM, CrystallarieumEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.CINDERHEARTH, CinderhearthEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.TITRATION_BARREL, TitrationBarrelEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.PRIMORDIAL_FIRE_BURNING, PrimordialFireBurningEmiRecipeGated::new);
		
		FreezingIdolBlock.FREEZING_STATE_MAP.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key.getBlock());
			EmiStack out = EmiStack.of(value.getLeft().getBlock()).setChance(value.getRight());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("freezing", key.getBlock()); // The synthetic IDs generated here assume there will never be multiple conversions of the same block with different states
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.FREEZING, id, in, out, SpectrumAdvancements.UNLOCK_IDOLS));
		});
		FreezingIdolBlock.FREEZING_MAP.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key);
			EmiStack out = EmiStack.of(value.getLeft().getBlock()).setChance(value.getRight());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("freezing", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.FREEZING, id, in, out, SpectrumAdvancements.UNLOCK_IDOLS));
		});
		FirestarterIdolBlock.BURNING_MAP.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key);
			EmiStack out = EmiStack.of(value.getLeft().getBlock()).setChance(value.getRight());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("heating", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.HEATING, id, in, out, SpectrumAdvancements.UNLOCK_IDOLS));
		});
		NaturesStaffConversionDataLoader.CONVERSIONS.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key);
			EmiStack out = EmiStack.of(value.getBlock());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("natures_staff", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.NATURES_STAFF, id, in, out, SpectrumAdvancements.UNLOCK_NATURES_STAFF));
		});
		
		//WorldInteractionRecipe
		EmiStack water = EmiStack.of(Fluids.WATER, FluidUnit.BUCKET);
		EmiStack lava = EmiStack.of(Fluids.LAVA, FluidUnit.BUCKET);
		EmiStack dragonrot = EmiStack.of(SpectrumFluids.DRAGONROT, FluidUnit.BUCKET);
		EmiStack liquidCrystal = EmiStack.of(SpectrumFluids.LIQUID_CRYSTAL, FluidUnit.BUCKET);
		EmiStack midnightSolution = EmiStack.of(SpectrumFluids.MIDNIGHT_SOLUTION, FluidUnit.BUCKET);
		EmiStack mud = EmiStack.of(SpectrumFluids.MUD, FluidUnit.BUCKET);
		EmiStack waterCatalyst = water.copy().setRemainder(water);
		EmiStack lavaCatalyst = lava.copy().setRemainder(lava);
		EmiStack dragonrotCatalyst = dragonrot.copy().setRemainder(dragonrot);
		EmiStack liquidCrystalCatalyst = liquidCrystal.copy().setRemainder(liquidCrystal);
		EmiStack midnightSolutionCatalyst = midnightSolution.copy().setRemainder(midnightSolution);
		EmiStack mudCatalyst = mud.copy().setRemainder(mud);
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", SpectrumBlocks.SLUSH))
				.leftInput(dragonrotCatalyst)
				.rightInput(waterCatalyst, false)
				.output(EmiStack.of(SpectrumBlocks.SLUSH))
				.requiredAdvancement(DragonrotConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.BLACKSTONE))
				.leftInput(dragonrotCatalyst)
				.rightInput(lavaCatalyst, false)
				.output(EmiStack.of(Blocks.BLACKSTONE))
				.requiredAdvancement(DragonrotConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.COARSE_DIRT))
				.leftInput(dragonrotCatalyst)
				.rightInput(mudCatalyst, false)
				.output(EmiStack.of(Blocks.COARSE_DIRT))
				.requiredAdvancement(DragonrotConvertingRecipe.UNLOCK_IDENTIFIER)
				.requiredAdvancement(MudConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", SpectrumBlocks.ROTTEN_GROUND))
				.leftInput(dragonrotCatalyst)
				.rightInput(liquidCrystalCatalyst, false)
				.output(EmiStack.of(SpectrumBlocks.ROTTEN_GROUND))
				.requiredAdvancement(DragonrotConvertingRecipe.UNLOCK_IDENTIFIER)
				.requiredAdvancement(LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", SpectrumBlocks.BLACK_SLUDGE))
				.leftInput(dragonrotCatalyst)
				.rightInput(midnightSolutionCatalyst, false)
				.output(EmiStack.of(SpectrumBlocks.BLACK_SLUDGE))
				.requiredAdvancement(DragonrotConvertingRecipe.UNLOCK_IDENTIFIER)
				.requiredAdvancement(MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", SpectrumBlocks.FROSTBITE_CRYSTAL))
				.leftInput(liquidCrystal)
				.rightInput(waterCatalyst, false)
				.output(EmiStack.of(SpectrumBlocks.FROSTBITE_CRYSTAL))
				.requiredAdvancement(LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.CALCITE))
				.leftInput(liquidCrystalCatalyst)
				.rightInput(waterCatalyst, false)
				.output(EmiStack.of(Blocks.CALCITE))
				.requiredAdvancement(LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", SpectrumBlocks.BLAZING_CRYSTAL))
				.leftInput(liquidCrystal)
				.rightInput(lavaCatalyst, false)
				.output(EmiStack.of(SpectrumBlocks.BLAZING_CRYSTAL))
				.requiredAdvancement(LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.COBBLED_DEEPSLATE))
				.leftInput(liquidCrystalCatalyst)
				.rightInput(lavaCatalyst, false)
				.output(EmiStack.of(Blocks.COBBLED_DEEPSLATE))
				.requiredAdvancement(LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.CLAY))
				.leftInput(liquidCrystalCatalyst)
				.rightInput(mudCatalyst, false)
				.output(EmiStack.of(Blocks.CLAY))
				.requiredAdvancement(LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER)
				.requiredAdvancement(MudConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.TERRACOTTA))
				.leftInput(midnightSolutionCatalyst)
				.rightInput(lavaCatalyst, false)
				.output(EmiStack.of(Blocks.TERRACOTTA))
				.requiredAdvancement(MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.DIRT))
				.leftInput(mudCatalyst)
				.rightInput(waterCatalyst, false)
				.output(EmiStack.of(Blocks.DIRT))
				.requiredAdvancement(MudConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
		addRecipeSafe(registry, () -> SpectrumWorldInteractionRecipe.customBuilder()
				.id(syntheticId("world/fluid_interaction", Blocks.MUD))
				.leftInput(mudCatalyst)
				.rightInput(lavaCatalyst, false)
				.output(EmiStack.of(Blocks.MUD))
				.requiredAdvancement(MudConvertingRecipe.UNLOCK_IDENTIFIER)
				.build());
	}

	public void registerRecipeHandlers(EmiRegistry registry) {
		registry.addRecipeHandler(SpectrumScreenHandlerTypes.PEDESTAL, new PedestalRecipeHandler());
		registry.addRecipeHandler(SpectrumScreenHandlerTypes.CRAFTING_TABLET, new CraftingTabletRecipeHandler());
		registry.addRecipeHandler(SpectrumScreenHandlerTypes.CINDERHEARTH, new CinderhearthRecipeHandler());
		registry.addRecipeHandler(SpectrumScreenHandlerTypes.POTION_WORKSHOP, new PotionWorkshopRecipeHandler());
	}

	public static Identifier syntheticId(String type, Block block) {
		Identifier blockId = Registries.BLOCK.getId(block);
		// Note that all recipe ids here start with "spectrum:/" which is legal, but impossible to represent with real files
		return new Identifier("spectrum:/" + type + "/" + blockId.getNamespace() + "/" + blockId.getPath());
	}

	public <C extends Inventory, T extends Recipe<C>> void addAll(EmiRegistry registry, RecipeType<T> type, Function<T, EmiRecipe> constructor) {
		for (T recipe : registry.getRecipeManager().listAllOfType(type)) {
			registry.addRecipe(constructor.apply(recipe));
		}
	}
	
	private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
		try {
			registry.addRecipe(supplier.get());
		} catch (Throwable e) {
			EmiReloadLog.warn("Exception thrown when parsing EMI recipe (no ID available)");
			EmiReloadLog.error(e);
		}
	}
	
}
