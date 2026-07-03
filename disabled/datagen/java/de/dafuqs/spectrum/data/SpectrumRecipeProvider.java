package de.dafuqs.spectrum.data;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import javax.annotation.*;

import java.util.*;
import java.util.concurrent.*;

import static de.dafuqs.spectrum.registries.SpectrumAdvancements.*;
import static de.dafuqs.spectrum.registries.SpectrumEnchantments.*;
import static de.dafuqs.spectrum.registries.SpectrumItems.*;

public class SpectrumRecipeProvider extends FabricRecipeProvider {
	
	public SpectrumRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	public void buildRecipes(RecipeOutput ctx) {
		generateCrystallarieumRecipes(ctx);
		generateEnchantmentUpgradeRecipes(ctx);
	}
	
	private void generateCrystallarieumRecipes(RecipeOutput ctx) {
		generateCrystallarieumRecipe(ctx, "minecraft/coal", Items.COAL, null, null, 60, InkColors.BROWN, 1, false,
				List.of(SpectrumBlocks.SMALL_COAL_BUD, SpectrumBlocks.LARGE_COAL_BUD, SpectrumBlocks.COAL_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.CHARCOAL), 2.0f, 0.4f, 0.2f),
						new CrystallarieumCatalyst(Ingredient.of(INCANDESCENT_ESSENCE), 16.0f, 2.0f, 0.05f),
						new CrystallarieumCatalyst(Ingredient.of(VEGETAL), 0.75f, 0.05f, 0.4f)
				),
				List.of(PURE_COAL.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/copper", Items.RAW_COPPER, null, null, 60, InkColors.BROWN, 2, false,
				List.of(SpectrumBlocks.SMALL_COPPER_BUD, SpectrumBlocks.LARGE_COPPER_BUD, SpectrumBlocks.COPPER_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(RAW_MALACHITE), 4.0f, 0.5f, 0.2f),
						new CrystallarieumCatalyst(Ingredient.of(Items.HONEYCOMB), 8.0f, 2.0f, 0.05f),
						new CrystallarieumCatalyst(Ingredient.of(NEOLITH), 1.5f, 0.25f, 0.02f)
				),
				List.of(PURE_COPPER.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/diamond", Items.DIAMOND, null, null, 480, InkColors.CYAN, 3, false,
				List.of(SpectrumBlocks.SMALL_DIAMOND_BUD, SpectrumBlocks.LARGE_DIAMOND_BUD, SpectrumBlocks.DIAMOND_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.COAL), 8.0f, 0.25f, 0.2f),
						new CrystallarieumCatalyst(Ingredient.of(Items.COAL_BLOCK), 10.0f, 0.25f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(Items.CHARCOAL), 16.0f, 0.25f, 1.0f)
				),
				List.of(PURE_DIAMOND.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/echo", Items.ECHO_SHARD, SpectrumFluids.MIDNIGHT_SOLUTION, null, 960, InkColors.BROWN, 3, false,
				List.of(SpectrumBlocks.SMALL_ECHO_BUD, SpectrumBlocks.LARGE_ECHO_BUD, SpectrumBlocks.ECHO_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(FROSTBITE_ESSENCE), 1.5f, 2.0f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(Items.ENDER_PEARL), 1.0f, 0.25f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(Items.EXPERIENCE_BOTTLE), 8.0f, 2.0f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(Items.SCULK), 2.0f, 0.125f, 0.02f)
				),
				List.of(PURE_ECHO.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/emerald", Items.EMERALD, null, null, 60, InkColors.CYAN, 3, false,
				List.of(SpectrumBlocks.SMALL_EMERALD_BUD, SpectrumBlocks.LARGE_EMERALD_BUD, SpectrumBlocks.EMERALD_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.GUNPOWDER), 4.0f, 3.0f, 0.04f),
						new CrystallarieumCatalyst(Ingredient.of(FROSTBITE_ESSENCE), 1.0f, 0.125f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(MIDNIGHT_CHIP), 16.0f, 4.0f, 0.2f)
				),
				List.of(PURE_EMERALD.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/glowstone", Items.GLOWSTONE, null, null, 120, InkColors.YELLOW, 2, false,
				List.of(SpectrumBlocks.SMALL_GLOWSTONE_BUD, SpectrumBlocks.LARGE_GLOWSTONE_BUD, SpectrumBlocks.GLOWSTONE_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(SHIMMERSTONE_GEM), 16.0f, 1.0f, 0.1f),
						new CrystallarieumCatalyst(Ingredient.of(FROSTBITE_ESSENCE), 4.0f, 0.25f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(MOONSTONE_SHARD), 1.0f, 0.01f, 0.05f)
				),
				List.of(PURE_GLOWSTONE.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/gold", Items.RAW_GOLD, null, null, 60, InkColors.BROWN, 2, false,
				List.of(SpectrumBlocks.SMALL_GOLD_BUD, SpectrumBlocks.LARGE_GOLD_BUD, SpectrumBlocks.GOLD_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.GOLD_NUGGET), 4.0f, 0.5f, 0.2f),
						new CrystallarieumCatalyst(Ingredient.of(SHIMMERSTONE_GEM), 8.0f, 2.0f, 0.05f),
						new CrystallarieumCatalyst(Ingredient.of(NEOLITH), 1.5f, 0.25f, 0.02f)
				),
				List.of(PURE_GOLD.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/iron", Items.RAW_IRON, null, null, 60, InkColors.BROWN, 2, false,
				List.of(SpectrumBlocks.SMALL_IRON_BUD, SpectrumBlocks.LARGE_IRON_BUD, SpectrumBlocks.IRON_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.IRON_NUGGET), 4.0f, 0.5f, 0.2f),
						new CrystallarieumCatalyst(Ingredient.of(BEDROCK_DUST), 8.0f, 2.0f, 0.05f),
						new CrystallarieumCatalyst(Ingredient.of(NEOLITH), 1.5f, 0.25f, 0.02f)
				),
				List.of(PURE_IRON.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/lapis", Items.LAPIS_LAZULI, null, null, 60, InkColors.PURPLE, 2, false,
				List.of(SpectrumBlocks.SMALL_LAPIS_BUD, SpectrumBlocks.LARGE_LAPIS_BUD, SpectrumBlocks.LAPIS_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.EXPERIENCE_BOTTLE), 8.0f, 4.0f, 0.05f),
						new CrystallarieumCatalyst(Ingredient.of(RAW_AZURITE), 0.5f, 0.1f, 0.004f),
						new CrystallarieumCatalyst(Ingredient.of(MIDNIGHT_CHIP), 1.2f, 1.5f, 0.2f)
				),
				List.of(PURE_LAPIS.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/netherite_scrap", Items.NETHERITE_SCRAP, Fluids.LAVA, null, 960, InkColors.BROWN, 3, false,
				List.of(SpectrumBlocks.SMALL_NETHERITE_SCRAP_BUD, SpectrumBlocks.LARGE_NETHERITE_SCRAP_BUD, SpectrumBlocks.NETHERITE_SCRAP_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(INCANDESCENT_ESSENCE), 1.5f, 2.0f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(Items.FIRE_CHARGE), 1.0f, 0.25f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(Items.GOLD_INGOT), 16.0f, 2.5f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(STRATINE_FRAGMENTS), 2.0f, 0.125f, 0.02f)
				),
				List.of(PURE_NETHERITE_SCRAP.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/prismarine_crystal", Items.PRISMARINE_CRYSTALS, Fluids.WATER, null, 60, InkColors.CYAN, 2, false,
				List.of(SpectrumBlocks.SMALL_PRISMARINE_BUD, SpectrumBlocks.LARGE_PRISMARINE_BUD, SpectrumBlocks.PRISMARINE_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.PRISMARINE_SHARD), 2.0f, 0.5f, 0.04f),
						new CrystallarieumCatalyst(Ingredient.of(Items.WET_SPONGE), 1.0f, 0.7f, 0.0002f),
						new CrystallarieumCatalyst(Ingredient.of(MERMAIDS_GEM), 32.0f, 2.0f, 0.1f)
				),
				List.of(PURE_PRISMARINE.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/quartz", Items.QUARTZ, Fluids.WATER, null, 180, InkColors.CYAN, 2, false,
				List.of(SpectrumBlocks.SMALL_QUARTZ_BUD, SpectrumBlocks.LARGE_QUARTZ_BUD, SpectrumBlocks.QUARTZ_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.SAND), 3.0f, 2.0f, 0.25f),
						new CrystallarieumCatalyst(Ingredient.of(MIDNIGHT_CHIP), 1.0f, 0.25f, 0.01f),
						new CrystallarieumCatalyst(Ingredient.of(SpectrumBlocks.ROCK_CRYSTAL), 12.0f, 0.5f, 0.1f)
				),
				List.of(PURE_QUARTZ.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "minecraft/redstone", Items.REDSTONE, null, null, 60, InkColors.YELLOW, 2, false,
				List.of(SpectrumBlocks.SMALL_REDSTONE_BUD, SpectrumBlocks.LARGE_REDSTONE_BUD, SpectrumBlocks.REDSTONE_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(STORM_STONE), 8.0f, 2.0f, 0.01f),
						new CrystallarieumCatalyst(Ingredient.of(SHIMMERSTONE_GEM), 1.0f, 0.25f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(STARDUST), 1.5f, 0.5f, 0.005f)
				),
				List.of(PURE_REDSTONE.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "spectrum/azurite", RAW_AZURITE, null, COLLECT_AZURITE, 300, InkColors.BLUE, 4, false,
				List.of(SpectrumBlocks.SMALL_AZURITE_BUD, SpectrumBlocks.LARGE_AZURITE_BUD, SpectrumBlocks.AZURITE_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.RAW_COPPER), 7.5f, 10.0f, 0.05f),
						new CrystallarieumCatalyst(Ingredient.of(Items.COPPER_INGOT), 1.0f, 0.5f, 0.15f),
						new CrystallarieumCatalyst(Ingredient.of(PURE_COPPER), 1.708f, 0.707f, 0.01f)
				),
				List.of(PURE_AZURITE.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "spectrum/bismuth", BISMUTH_FLAKE, null, null, 120, InkColors.CYAN, 4, false,
				List.of(SpectrumBlocks.SMALL_BISMUTH_BUD, SpectrumBlocks.LARGE_BISMUTH_BUD, SpectrumBlocks.BISMUTH_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(BISMUTH_FLAKE), 8.0f, 1.0f, 0.2f),
						new CrystallarieumCatalyst(Ingredient.of(STARDUST), 2.0f, 0.25f, 0.02f),
						new CrystallarieumCatalyst(Ingredient.of(STAR_FRAGMENT), 1.25f, 1.0f, 0.0002f)
				),
				List.of(BISMUTH_CRYSTAL.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "spectrum/bloodstone", RAW_BLOODSTONE, null, UNLOCK_BLOODSTONE, 300, InkColors.RED, 4, false,
				List.of(SpectrumBlocks.SMALL_BLOODSTONE_BUD, SpectrumBlocks.LARGE_BLOODSTONE_BUD, SpectrumBlocks.BLOODSTONE_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(Items.RAW_COPPER), 7.5f, 10.0f, 0.05f),
						new CrystallarieumCatalyst(Ingredient.of(Items.COPPER_INGOT), 1.0f, 0.5f, 0.15f),
						new CrystallarieumCatalyst(Ingredient.of(PURE_COPPER), 1.708f, 0.707f, 0.01f)
				),
				List.of(PURE_BLOODSTONE.getDefaultInstance()));
		
		generateCrystallarieumRecipe(ctx, "spectrum/malachite", RAW_MALACHITE, null, COLLECT_MALACHITE, 300, InkColors.WHITE, 4, false,
				List.of(SpectrumBlocks.SMALL_MALACHITE_BUD, SpectrumBlocks.LARGE_MALACHITE_BUD, SpectrumBlocks.MALACHITE_CLUSTER),
				List.of(
						new CrystallarieumCatalyst(Ingredient.of(MOONSTONE_POWDER), 1.0f, 1.0f, 0.04f)
				),
				List.of(PURE_MALACHITE.getDefaultInstance()));
	}
	
	private void generateEnchantmentUpgradeRecipes(RecipeOutput ctx) {
		//TODO These could benefit from a revisit
		
		// Spectrum
		generateEnchantmentUpgradeRecipe(ctx, "", BIG_CATCH, ENCHANTMENTS_BIG_CATCH, LIGHT_BLUE_PIGMENT, 3, RecipeScaling.doubling(400), RecipeScaling.indices(32, 128));
		generateEnchantmentUpgradeRecipe(ctx, "", CLOVERS_FAVOR, ENCHANTMENTS_CLOVERS_FAVOR, LIGHT_BLUE_PIGMENT,6, RecipeScaling.indices(200, 400, 2000, 10000, 40000), RecipeScaling.indices(8, 32, 128, 512, 512));
		generateEnchantmentUpgradeRecipe(ctx, "", DISARMING, ENCHANTMENTS_DISARMING, RED_PIGMENT, 4, RecipeScaling.indices(400, 2000, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "", EXUBERANCE, ENCHANTMENTS_EXUBERANCE, PURPLE_PIGMENT, 10, RecipeScaling.linear(400, 200, 1.0F), RecipeScaling.indices(8, 16, 32, 64, 128, 256, 512, 512, 512));
		generateEnchantmentUpgradeRecipe(ctx, "", FIRST_STRIKE, ENCHANTMENTS_FIRST_STRIKE, PINK_PIGMENT, 5, RecipeScaling.indices(200, 400, 2000, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "", IMPROVED_CRITICAL, ENCHANTMENTS_IMPROVED_CRITICAL, BLACK_PIGMENT, 4, RecipeScaling.indices(400, 2000, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "", INERTIA, ENCHANTMENTS_INERTIA, BROWN_PIGMENT, 5, RecipeScaling.indices(200, 400, 2000, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "", RAZING, ENCHANTMENTS_RAZING, GRAY_PIGMENT, 5, RecipeScaling.indices(400, 2000, 10000, 10000), RecipeScaling.indices(32, 128, 256, 512));
		generateEnchantmentUpgradeRecipe(ctx, "", SERENDIPITY_REEL, ENCHANTMENTS_SERENDIPITY_REEL, LIGHT_BLUE_PIGMENT, 3, RecipeScaling.doubling(400), RecipeScaling.indices(32, 128));
		generateEnchantmentUpgradeRecipe(ctx, "", SNIPING, ENCHANTMENTS_SNIPING, GREEN_PIGMENT, 5, RecipeScaling.indices(200, 1000, 5000, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "", TIGHT_GRIP, ENCHANTMENTS_TIGHT_GRIP, YELLOW_PIGMENT, 4, RecipeScaling.indices(400, 2000, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "", TREASURE_HUNTER, ENCHANTMENTS_TREASURE_HUNTER, LIGHT_BLUE_PIGMENT, 5, RecipeScaling.indices(200, 400, 2000, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		
		// Vanilla
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.BANE_OF_ARTHROPODS, ENCHANTMENTS_VANILLA_DAMAGE, BLACK_PIGMENT, 8, RecipeScaling.doubling(100), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.BLAST_PROTECTION, ENCHANTMENTS_VANILLA_PROTECTION, PINK_PIGMENT, 8, RecipeScaling.doubling(100), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.DEPTH_STRIDER, ENCHANTMENTS_VANILLA_WATER, BLUE_PIGMENT, 3, RecipeScaling.doubling(200), RecipeScaling.indices(8, 32));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.EFFICIENCY, ENCHANTMENTS_VANILLA_QUITOXIC, YELLOW_PIGMENT, 8, RecipeScaling.indices(200, 400, 600, 800, 1600, 2600, 4000), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.FEATHER_FALLING, ENCHANTMENTS_VANILLA_QUITOXIC, BLUE_PIGMENT, 6, RecipeScaling.doubling(250), RecipeScaling.indices(8, 16, 32, 64, 256));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.FIRE_ASPECT, ENCHANTMENTS_VANILLA_DAMAGE, RED_PIGMENT, 4, RecipeScaling.doubling(200, 200, 2.0F), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.FIRE_PROTECTION, ENCHANTMENTS_VANILLA_PROTECTION, PINK_PIGMENT, 8, RecipeScaling.indices(100, 200, 300, 400, 800, 1300, 2000), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.FORTUNE, ENCHANTMENTS_VANILLA_LUCK, LIGHT_BLUE_PIGMENT, 5, RecipeScaling.indices(100, 400, 3000, 10000), RecipeScaling.indices(32, 128, 256, 512));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.FROST_WALKER, ENCHANTMENTS_VANILLA_TREASURE, LIGHT_GRAY_PIGMENT, 4, RecipeScaling.indices(400, 1600, 3200), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.IMPALING, ENCHANTMENTS_VANILLA_TRIDENT, BROWN_PIGMENT, 8, RecipeScaling.indices(100, 200, 300, 400, 800, 1300, 2000), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.KNOCKBACK, ENCHANTMENTS_VANILLA_DAMAGE, BLACK_PIGMENT, 5, RecipeScaling.indices(200, 1600, 3200, 6400), RecipeScaling.indices(8, 32, 128, 256));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.LOOTING, ENCHANTMENTS_VANILLA_LUCK, LIGHT_BLUE_PIGMENT, 6, RecipeScaling.indices(200, 500, 2400, 10000, 40000), RecipeScaling.indices(8, 32, 128, 512, 512));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.LOYALTY, ENCHANTMENTS_VANILLA_TRIDENT, BROWN_PIGMENT, 4, RecipeScaling.doubling(0, 200, 2.0F), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.LUCK_OF_THE_SEA, ENCHANTMENTS_VANILLA_WATER_LUCK, LIGHT_BLUE_PIGMENT, 5, RecipeScaling.indices(200, 400, 2000, 4000), RecipeScaling.indices(8, 32, 128, 256));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.LURE, ENCHANTMENTS_VANILLA_WATER, BLUE_PIGMENT, 5, RecipeScaling.indices(200, 400, 2000, 4000), RecipeScaling.indices(8, 32, 128, 256));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.PIERCING, ENCHANTMENTS_VANILLA_PROJECTILE, RED_PIGMENT, 8, RecipeScaling.indices(100, 200, 300, 400, 800, 1300, 2000), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.POWER, ENCHANTMENTS_VANILLA_PROJECTILE, RED_PIGMENT, 8, RecipeScaling.doubling(200), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.PROJECTILE_PROTECTION, ENCHANTMENTS_VANILLA_PROTECTION, PINK_PIGMENT, 8, RecipeScaling.indices(100, 200, 300, 400, 800, 1300, 2000), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.PROTECTION, ENCHANTMENTS_VANILLA_PROTECTION, PINK_PIGMENT, 8, RecipeScaling.indices(200, 400, 600, 800, 1600, 4000, 8000), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.PUNCH, ENCHANTMENTS_VANILLA_PROJECTILE, RED_PIGMENT, 5, RecipeScaling.indices(200, 1600, 3200, 6400), RecipeScaling.indices(8, 32, 128, 256));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.QUICK_CHARGE, ENCHANTMENTS_VANILLA_PROJECTILE, RED_PIGMENT, 5, RecipeScaling.indices(200, 1600, 5000, 10000), RecipeScaling.indices(8, 32, 512, 512));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.RESPIRATION, ENCHANTMENTS_VANILLA_WATER, BLUE_PIGMENT, 6, RecipeScaling.indices(100, 200, 1600, 4800, 10000), RecipeScaling.indices(8, 32, 128, 256, 512));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.RIPTIDE, ENCHANTMENTS_VANILLA_TRIDENT, BROWN_PIGMENT, 3, RecipeScaling.indices(200, 2400, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.SHARPNESS, ENCHANTMENTS_VANILLA_DAMAGE, BLACK_PIGMENT, 8, RecipeScaling.doubling(75), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.SMITE, ENCHANTMENTS_VANILLA_DAMAGE, BLACK_PIGMENT, 8, RecipeScaling.indices(100, 200, 300, 400, 800, 1300, 2000), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.SOUL_SPEED, ENCHANTMENTS_VANILLA_TREASURE, LIGHT_GRAY_PIGMENT, 3, RecipeScaling.indices(200, 2400, 10000), RecipeScaling.doubling(0, 8, 2.0F));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.SWEEPING_EDGE, ENCHANTMENTS_VANILLA_DAMAGE, RED_PIGMENT, 7, RecipeScaling.indices(100, 400, 1000, 2000, 5000, 10000), RecipeScaling.indices(8, 32, 64, 128, 256, 512));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.SWIFT_SNEAK, ENCHANTMENTS_VANILLA_SWIFT_SNEAK, LIGHT_BLUE_PIGMENT, 5, RecipeScaling.indices(200, 600, 2000, 5000), RecipeScaling.indices(8, 32, 128, 256));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.THORNS, ENCHANTMENTS_VANILLA_PROTECTION, PINK_PIGMENT, 6, RecipeScaling.indices(100, 400, 2000, 4000, 10000), RecipeScaling.indices(8, 32, 128, 256, 512));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.UNBREAKING, ENCHANTMENTS_VANILLA_UNBREAKING, CYAN_PIGMENT, 6, RecipeScaling.indices(100, 400, 2000, 4000, 10000), RecipeScaling.indices(8, 32, 256, 512, 512));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.WIND_BURST, ENCHANTMENTS_VANILLA_DAMAGE, YELLOW_PIGMENT, 5, RecipeScaling.doubling(200), RecipeScaling.doubling(16));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.BREACH, ENCHANTMENTS_VANILLA_BREACHING, RED_PIGMENT, 5, RecipeScaling.doubling(200), RecipeScaling.doubling(8));
		generateEnchantmentUpgradeRecipe(ctx, "vanilla_enchantment_upgrades", Enchantments.DENSITY, ENCHANTMENTS_VANILLA_TRIAL, CYAN_PIGMENT, 8, RecipeScaling.doubling(400), RecipeScaling.doubling(16));
		
	}
	
	private void generateCrystallarieumRecipe(RecipeOutput ctx, String id, Item base, @Nullable Fluid medium, @Nullable ResourceLocation advancement, int secondsPerStage, InkColor inkColor, int inkCostTier, boolean growsWithoutCatalyst, List<Block> stages, List<CrystallarieumCatalyst> catalysts, List<ItemStack> additionalResults) {
		generateRecipe(ctx, "crystallarieum/" + id, new CrystallarieumRecipe("", false, Optional.ofNullable(advancement), Ingredient.of(base),
				stages.stream().map(s -> s.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP)).toList(),
				secondsPerStage, inkColor, 1 << (inkCostTier - 1), growsWithoutCatalyst,
				catalysts,
				FluidIngredient.of(medium == null ? SpectrumFluids.LIQUID_CRYSTAL : medium),
				additionalResults));
	}
	
	private void generateEnchantmentUpgradeRecipe(RecipeOutput ctx, String group, ResourceKey<Enchantment> enchantment, ResourceLocation advancement, Item bulkItem, int levelCap, RecipeScaling.ScalingData xpScaling, RecipeScaling.ScalingData itemScaling) {
		ctx = withConditions(ctx, new SpectrumResourceConditions.EnchantmentsExistResourceCondition(List.of(enchantment)));
		String namespace = enchantment.location().getNamespace();
		String base = "enchantment_upgrade/" + namespace + "/" + enchantment.location().getPath().replace("/", ".");
		generateRecipe(ctx, base, new EnchantmentUpgradeRecipe(group, false, Optional.of(advancement), Either.right(enchantment), levelCap, Ingredient.of(bulkItem), xpScaling, itemScaling));
	}
	
	private void generateRecipe(RecipeOutput ctx, String id, Recipe<?> recipe) {
		ctx.accept(SpectrumCommon.locate(id), recipe, null);
	}
	
}
