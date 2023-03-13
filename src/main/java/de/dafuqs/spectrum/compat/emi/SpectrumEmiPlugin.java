package de.dafuqs.spectrum.compat.emi;

import java.util.List;
import java.util.function.Function;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.mob_blocks.FirestarterMobBlock;
import de.dafuqs.spectrum.blocks.mob_blocks.FreezingMobBlock;
import de.dafuqs.spectrum.compat.emi.recipes.AnvilCrushingEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.BlockToBlockWithChanceEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.CinderhearthEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.CrystallarieumEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.EnchanterEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.FluidConvertingEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.FusionShrineEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.GatedInfoEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.InkConvertingEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.PedestalCraftingEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.PotionWorkshopEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.SpiritInstillingEmiRecipe;
import de.dafuqs.spectrum.compat.emi.recipes.TitrationBarrelEmiRecipe;
import de.dafuqs.spectrum.data_loaders.NaturesStaffConversionDataLoader;
import de.dafuqs.spectrum.mixin.accessors.FluidBlockAccessor;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fluid_converting.DragonrotConvertingRecipe;
import de.dafuqs.spectrum.recipe.fluid_converting.LiquidCrystalConvertingRecipe;
import de.dafuqs.spectrum.recipe.fluid_converting.MidnightSolutionConvertingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumEmiPlugin implements EmiPlugin {

	@Override
	public void register(EmiRegistry registry) {
		registerCategories(registry);
		registerRecipes(registry);
	}

	public void registerCategories(EmiRegistry registry) {
		registry.addCategory(SpectrumRecipeCategories.PEDESTAL_CRAFTING);
		registry.addCategory(SpectrumRecipeCategories.ANVIL_CRUSHING);
		registry.addCategory(SpectrumRecipeCategories.FUSION_SHRINE);
		registry.addCategory(SpectrumRecipeCategories.NATURES_STAFF);
		registry.addCategory(SpectrumRecipeCategories.ENCHANTER);
		registry.addCategory(SpectrumRecipeCategories.ENCHANTMENT_UPGRADE);
		registry.addCategory(SpectrumRecipeCategories.POTION_WORKSHOP_BREWING);
		registry.addCategory(SpectrumRecipeCategories.POTION_WORKSHOP_CRAFTING);
		registry.addCategory(SpectrumRecipeCategories.POTION_WORKSHOP_REACTING);
		registry.addCategory(SpectrumRecipeCategories.SPIRIT_INSTILLER);
		registry.addCategory(SpectrumRecipeCategories.LIQUID_CRYSTAL_CONVERTING);
		registry.addCategory(SpectrumRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING);
		registry.addCategory(SpectrumRecipeCategories.DRAGONROT_CONVERTING);
		registry.addCategory(SpectrumRecipeCategories.HEATING);
		registry.addCategory(SpectrumRecipeCategories.FREEZING);
		registry.addCategory(SpectrumRecipeCategories.INK_CONVERTING);
		registry.addCategory(SpectrumRecipeCategories.CRYSTALLARIEUM);
		registry.addCategory(SpectrumRecipeCategories.CINDERHEARTH);
		registry.addCategory(SpectrumRecipeCategories.TITRATION_BARREL);
		
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
		
		registry.addWorkstation(SpectrumRecipeCategories.PEDESTAL_CRAFTING, pedestals);
		registry.addWorkstation(SpectrumRecipeCategories.ANVIL_CRUSHING, EmiStack.of(Blocks.ANVIL));
		registry.addWorkstation(SpectrumRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.BEDROCK_ANVIL));
		registry.addWorkstation(SpectrumRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.STRATINE_FRAGMENT_BLOCK));
		registry.addWorkstation(SpectrumRecipeCategories.ANVIL_CRUSHING, EmiStack.of(SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK));
		registry.addWorkstation(SpectrumRecipeCategories.FUSION_SHRINE, EmiStack.of(SpectrumBlocks.FUSION_SHRINE_BASALT));
		registry.addWorkstation(SpectrumRecipeCategories.FUSION_SHRINE, EmiStack.of(SpectrumBlocks.FUSION_SHRINE_CALCITE));
		registry.addWorkstation(SpectrumRecipeCategories.NATURES_STAFF, EmiStack.of(SpectrumItems.NATURES_STAFF));
		registry.addWorkstation(SpectrumRecipeCategories.HEATING, EmiStack.of(SpectrumBlocks.BLAZE_MOB_BLOCK));
		registry.addWorkstation(SpectrumRecipeCategories.FREEZING, EmiStack.of(SpectrumBlocks.POLAR_BEAR_MOB_BLOCK));
		registry.addWorkstation(SpectrumRecipeCategories.ENCHANTER, EmiStack.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstation(SpectrumRecipeCategories.ENCHANTMENT_UPGRADE, EmiStack.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstation(SpectrumRecipeCategories.LIQUID_CRYSTAL_CONVERTING, EmiStack.of(SpectrumItems.LIQUID_CRYSTAL_BUCKET));
		registry.addWorkstation(SpectrumRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING, EmiStack.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET));
		registry.addWorkstation(SpectrumRecipeCategories.DRAGONROT_CONVERTING, EmiStack.of(SpectrumItems.DRAGONROT_BUCKET));
		registry.addWorkstation(SpectrumRecipeCategories.SPIRIT_INSTILLER, EmiStack.of(SpectrumBlocks.SPIRIT_INSTILLER));
		registry.addWorkstation(SpectrumRecipeCategories.INK_CONVERTING, EmiStack.of(SpectrumBlocks.COLOR_PICKER));
		registry.addWorkstation(SpectrumRecipeCategories.CRYSTALLARIEUM, EmiStack.of(SpectrumBlocks.CRYSTALLARIEUM));
		registry.addWorkstation(SpectrumRecipeCategories.POTION_WORKSHOP_BREWING, EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstation(SpectrumRecipeCategories.POTION_WORKSHOP_CRAFTING, EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstation(SpectrumRecipeCategories.POTION_WORKSHOP_REACTING, EmiStack.of(SpectrumBlocks.POTION_WORKSHOP));
		
		registry.addWorkstation(SpectrumRecipeCategories.CINDERHEARTH, EmiStack.of(SpectrumBlocks.CINDERHEARTH));
		registry.addWorkstation(SpectrumRecipeCategories.TITRATION_BARREL, EmiStack.of(SpectrumBlocks.TITRATION_BARREL));
	}

	public void registerRecipes(EmiRegistry registry) {
		addAll(registry, SpectrumRecipeTypes.ANVIL_CRUSHING, AnvilCrushingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.FUSION_SHRINE, FusionShrineEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.ENCHANTER, r -> new EnchanterEmiRecipe(SpectrumRecipeCategories.ENCHANTER, r));
		addAll(registry, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, r -> new EnchanterEmiRecipe(SpectrumRecipeCategories.ENCHANTMENT_UPGRADE, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, r -> new PotionWorkshopEmiRecipe(SpectrumRecipeCategories.POTION_WORKSHOP_BREWING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, r -> new PotionWorkshopEmiRecipe(SpectrumRecipeCategories.POTION_WORKSHOP_CRAFTING, r));
		addAll(registry, SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, GatedInfoEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.SPIRIT_INSTILLING, SpiritInstillingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING, r -> new FluidConvertingEmiRecipe(SpectrumRecipeCategories.LIQUID_CRYSTAL_CONVERTING, r, LiquidCrystalConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, r -> new FluidConvertingEmiRecipe(SpectrumRecipeCategories.MIDNIGHT_SOLUTION_CONVERTING, r, MidnightSolutionConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.DRAGONROT_CONVERTING, r -> new FluidConvertingEmiRecipe(SpectrumRecipeCategories.DRAGONROT_CONVERTING, r, DragonrotConvertingRecipe.UNLOCK_IDENTIFIER));
		addAll(registry, SpectrumRecipeTypes.INK_CONVERTING, InkConvertingEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.CRYSTALLARIEUM, CrystallarieumEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.CINDERHEARTH, CinderhearthEmiRecipe::new);
		addAll(registry, SpectrumRecipeTypes.TITRATION_BARREL, TitrationBarrelEmiRecipe::new);

		FreezingMobBlock.FREEZING_STATE_MAP.forEach((key, value) -> {
			// The synthetic IDs generated here assume there will never be multiple conversions of the same block with different states
			Identifier id = syntheticId("freezing", key.getBlock());
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumRecipeCategories.FREEZING, id, blockStack(key.getBlock()),
				blockStack(value.getLeft().getBlock()).setChance(value.getRight()), SpectrumCommon.locate("progression/unlock_mob_blocks")));
		});
		FreezingMobBlock.FREEZING_MAP.forEach((key, value) -> {
			Identifier id = syntheticId("freezing", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumRecipeCategories.FREEZING, id, blockStack(key),
				blockStack(value.getLeft().getBlock()).setChance(value.getRight()), SpectrumCommon.locate("progression/unlock_mob_blocks")));
		});
		FirestarterMobBlock.BURNING_MAP.forEach((key, value) -> {
			Identifier id = syntheticId("heating", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumRecipeCategories.HEATING, id, blockStack(key),
				blockStack(value.getLeft().getBlock()).setChance(value.getRight()), SpectrumCommon.locate("progression/unlock_mob_blocks")));
		});
		NaturesStaffConversionDataLoader.CONVERSIONS.forEach((key, value) -> {
			Identifier id = syntheticId("natures_staff", key);
			registry.addRecipe(new BlockToBlockWithChanceEmiRecipe(SpectrumRecipeCategories.NATURES_STAFF, id, blockStack(key),
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
