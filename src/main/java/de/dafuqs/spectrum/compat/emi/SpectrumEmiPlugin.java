package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_blocks.*;
import de.dafuqs.spectrum.compat.emi.recipes.*;
import de.dafuqs.spectrum.data_loaders.*;
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
		registry.addWorkstation(VanillaEmiRecipeCategories.BLASTING, EmiStack.of(SpectrumBlocks.CINDERHEARTH));
		
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
	}

	public void registerRecipes(EmiRegistry registry) {
		addAll(registry, SpectrumRecipeTypes.ANVIL_CRUSHING, AnvilCrushingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.FUSION_SHRINE, FusionShrineEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.ENCHANTER, r -> new EnchanterEmiRecipeGated(SpectrumEmiRecipeCategories.ENCHANTER, r));
		addAll(registry, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, r -> new EnchantmentUpgradeEmiRecipeGated(SpectrumEmiRecipeCategories.ENCHANTMENT_UPGRADE, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, r -> new PotionWorkshopEmiRecipeGated(SpectrumEmiRecipeCategories.POTION_WORKSHOP_BREWING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, r -> new PotionWorkshopEmiRecipeGated(SpectrumEmiRecipeCategories.POTION_WORKSHOP_CRAFTING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, PotionWorkshopReactingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.SPIRIT_INSTILLING, SpiritInstillingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.MUD_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.MUD_CONVERTING, r, MudConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.LIQUID_CRYSTAL_CONVERTING, r, LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING, r, MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.DRAGONROT_CONVERTING, r -> new FluidConvertingEmiRecipeGated(SpectrumEmiRecipeCategories.DRAGONROT_CONVERTING, r, DragonrotConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.INK_CONVERTING, InkConvertingEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.CRYSTALLARIEUM, CrystallarieumEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.CINDERHEARTH, CinderhearthEmiRecipeGated::new);
		addAll(registry, SpectrumRecipeTypes.TITRATION_BARREL, TitrationBarrelEmiRecipeGated::new);
		
		FreezingMobBlock.FREEZING_STATE_MAP.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key.getBlock());
			EmiStack out = EmiStack.of(value.getLeft().getBlock()).setChance(value.getRight());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("freezing", key.getBlock()); // The synthetic IDs generated here assume there will never be multiple conversions of the same block with different states
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.FREEZING, id, in, out, SpectrumCommon.locate("unlocks/blocks/mob_blocks")));
		});
		FreezingMobBlock.FREEZING_MAP.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key);
			EmiStack out = EmiStack.of(value.getLeft().getBlock()).setChance(value.getRight());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("freezing", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.FREEZING, id, in, out, SpectrumCommon.locate("unlocks/blocks/mob_blocks")));
		});
		FirestarterMobBlock.BURNING_MAP.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key);
			EmiStack out = EmiStack.of(value.getLeft().getBlock()).setChance(value.getRight());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("heating", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.HEATING, id, in, out, SpectrumCommon.locate("unlocks/blocks/mob_blocks")));
		});
		NaturesStaffConversionDataLoader.CONVERSIONS.forEach((key, value) -> {
			EmiStack in = EmiStack.of(key);
			EmiStack out = EmiStack.of(value.getBlock());
			if (in.isEmpty() || out.isEmpty()) {
				return;
			}
			Identifier id = syntheticId("natures_staff", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumEmiRecipeCategories.NATURES_STAFF, id, in, out, SpectrumCommon.locate("unlocks/items/natures_staff")));
		});
	}

	public static Identifier syntheticId(String type, Block block) {
		Identifier blockId = Registry.BLOCK.getId(block);
		// Note that all recipe ids here start with "spectrum:/" which is legal, but impossible to represent with real files
		return new Identifier("spectrum:/" + type + "/" + blockId.getNamespace() + "/" + blockId.getPath());
	}

	public <C extends Inventory, T extends Recipe<C>> void addAll(EmiRegistry registry, RecipeType<T> type, Function<T, EmiRecipe> constructor) {
		for (T recipe : registry.getRecipeManager().listAllOfType(type)) {
			if (recipe instanceof GatedRecipe gatedRecipe && gatedRecipe.isSecret()) {
				continue; // secret recipes should never be shown in recipe viewers
			}
			registry.addRecipe(constructor.apply(recipe));
		}
	}
}
