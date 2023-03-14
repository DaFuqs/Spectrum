package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_blocks.*;
import de.dafuqs.spectrum.compat.emi.recipes.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.emi.api.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import net.minecraft.block.*;
import net.minecraft.inventory.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;
import java.util.function.*;

public class SpectrumEmiPlugin implements EmiPlugin {

	@Override
	public void register(EmiRegistry registry) {
		registerCategories(registry);
		registerRecipes(registry);
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
		registry.addCategory(SpectrumEmiRecipeCategories.LIQUID_CRYSTAL_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.DRAGONROT_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.HEATING);
		registry.addCategory(SpectrumEmiRecipeCategories.FREEZING);
		registry.addCategory(SpectrumEmiRecipeCategories.INK_CONVERTING);
		registry.addCategory(SpectrumEmiRecipeCategories.CRYSTALLARIEUM);
		registry.addCategory(SpectrumEmiRecipeCategories.CINDERHEARTH);
		registry.addCategory(SpectrumEmiRecipeCategories.TITRATION_BARREL);
		
		EmiIngredient pedestals = EmiIngredient.of(List.of(
				EmiStack.of(SpectrumBlocks.PEDESTAL_BASIC_TOPAZ),
				EmiStack.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST),
				EmiStack.of(SpectrumBlocks.PEDESTAL_BASIC_CITRINE),
				EmiStack.of(SpectrumBlocks.PEDESTAL_ALL_BASIC),
				EmiStack.of(SpectrumBlocks.PEDESTAL_ONYX),
				EmiStack.of(SpectrumBlocks.PEDESTAL_MOONSTONE)
		));
		
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(SpectrumItems.CRAFTING_TABLET));
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, pedestals);
		registry.addWorkstation(VanillaEmiRecipeCategories.BREWING, EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstation(VanillaEmiRecipeCategories.BLASTING, EmiStack.of(SpectrumBlocks.CINDERHEARTH));
		
		registry.addWorkstation(SpectrumEmiRecipeCategories.PEDESTAL_CRAFTING, pedestals);
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(Blocks.ANVIL));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.BEDROCK_ANVIL));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.STRATINE_FRAGMENT_BLOCK));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK));
		registry.addWorkstation(SpectrumEmiRecipeCategories.FUSION_SHRINE, EmiStack.of(SpectrumBlocks.FUSION_SHRINE_BASALT));
		registry.addWorkstation(SpectrumEmiRecipeCategories.FUSION_SHRINE, EmiStack.of(SpectrumBlocks.FUSION_SHRINE_CALCITE));
		registry.addWorkstation(SpectrumEmiRecipeCategories.NATURES_STAFF, EmiStack.of(SpectrumItems.NATURES_STAFF));
		registry.addWorkstation(SpectrumEmiRecipeCategories.HEATING, EmiStack.of(SpectrumBlocks.BLAZE_MOB_BLOCK));
		registry.addWorkstation(SpectrumEmiRecipeCategories.FREEZING, EmiStack.of(SpectrumBlocks.POLAR_BEAR_MOB_BLOCK));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ENCHANTER, EmiStack.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstation(SpectrumEmiRecipeCategories.ENCHANTMENT_UPGRADE, EmiStack.of(SpectrumBlocks.ENCHANTER));
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
	}

	public void registerRecipes(EmiRegistry registry) {
		addAll(registry, SpectrumRecipeTypes.ANVIL_CRUSHING, AnvilCrushingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.FUSION_SHRINE, FusionShrineEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.ENCHANTER, r -> new EnchanterEmiRecipe(SpectrumEmiRecipeCategories.ENCHANTER, r));
		addAll(registry, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, r -> new EnchanterEmiRecipe(SpectrumEmiRecipeCategories.ENCHANTMENT_UPGRADE, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, r -> new PotionWorkshopEmiRecipe(SpectrumEmiRecipeCategories.POTION_WORKSHOP_BREWING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, r -> new PotionWorkshopEmiRecipe(SpectrumEmiRecipeCategories.POTION_WORKSHOP_CRAFTING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, PotionWorkshopReactingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.SPIRIT_INSTILLING, SpiritInstillingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING, r -> new FluidConvertingEmiRecipe(SpectrumEmiRecipeCategories.LIQUID_CRYSTAL_CONVERTING, r, LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, r -> new FluidConvertingEmiRecipe(SpectrumEmiRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING, r, MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.DRAGONROT_CONVERTING, r -> new FluidConvertingEmiRecipe(SpectrumEmiRecipeCategories.DRAGONROT_CONVERTING, r, DragonrotConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.INK_CONVERTING, InkConvertingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.CRYSTALLARIEUM, CrystallarieumEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.CINDERHEARTH, CinderhearthEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.TITRATION_BARREL, TitrationBarrelEmiRecipe::new);
		
		FreezingMobBlock.FREEZING_STATE_MAP.forEach((key, value) -> {
			// The synthetic IDs generated here assume there will never be multiple conversions of the same block with different states
			Identifier id = syntheticId("freezing", key.getBlock());
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.FREEZING, id, blockStack(key.getBlock()),
					blockStack(value.getLeft().getBlock()).setChance(value.getRight()), SpectrumCommon.locate("progression/unlock_mob_blocks")));
		});
		FreezingMobBlock.FREEZING_MAP.forEach((key, value) -> {
			Identifier id = syntheticId("freezing", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.FREEZING, id, blockStack(key),
					blockStack(value.getLeft().getBlock()).setChance(value.getRight()), SpectrumCommon.locate("progression/unlock_mob_blocks")));
		});
		FirestarterMobBlock.BURNING_MAP.forEach((key, value) -> {
			Identifier id = syntheticId("heating", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.HEATING, id, blockStack(key),
					blockStack(value.getLeft().getBlock()).setChance(value.getRight()), SpectrumCommon.locate("progression/unlock_mob_blocks")));
		});
		NaturesStaffConversionDataLoader.CONVERSIONS.forEach((key, value) -> {
			Identifier id = syntheticId("natures_staff", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.NATURES_STAFF, id, blockStack(key),
					blockStack(value.getBlock()), SpectrumCommon.locate("progression/unlock_natures_staff")));
		});
	}

	public static Identifier syntheticId(String type, Block block) {
		Identifier blockId = Registry.BLOCK.getId(block);
		// Note that all recipe ids here start with "spectrum:/" which is legal, but impossible to represent with real files
		return new Identifier("spectrum:/" + type + "/" + blockId.getNamespace() + "/" + blockId.getPath());
	}

	public static EmiStack blockStack(Block block) {
		if (block instanceof FluidBlock fluid) {
			return EmiStack.of(((FluidBlockAccessor) fluid).getFlowableFluid());
		} else {
			return EmiStack.of(block);
		}
		
	}

	public <C extends Inventory, T extends Recipe<C>> void addAll(EmiRegistry registry, RecipeType<T> type, Function<T, EmiRecipe> constructor) {
		for (T recipe : registry.getRecipeManager().listAllOfType(type)) {
			registry.addRecipe(constructor.apply(recipe));
		}
	}
}
