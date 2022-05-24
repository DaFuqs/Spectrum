package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipeSerializer;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipeSerializer;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipeSerializer;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeSerializer;
import de.dafuqs.spectrum.recipe.midnight_solution_converting.MidnightSolutionConvertingRecipe;
import de.dafuqs.spectrum.recipe.midnight_solution_converting.MidnightSolutionConvertingRecipeSerializer;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipeSerializer;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipeSerializer;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipeSerializer;
import de.dafuqs.spectrum.recipe.spirit_instiller.ISpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumRecipeTypes {
	
	public static RecipeSerializer<PedestalCraftingRecipe> PEDESTAL_RECIPE_SERIALIZER;
	public static RecipeType<PedestalCraftingRecipe> PEDESTAL;

	public static RecipeSerializer<AnvilCrushingRecipe> ANVIL_CRUSHING_RECIPE_SERIALIZER;
	public static RecipeType<AnvilCrushingRecipe> ANVIL_CRUSHING;

	public static RecipeSerializer<FusionShrineRecipe> FUSION_SHRINE_RECIPE_SERIALIZER;
	public static RecipeType<FusionShrineRecipe> FUSION_SHRINE;

	public static RecipeSerializer<EnchanterRecipe> ENCHANTER_RECIPE_SERIALIZER;
	public static RecipeType<EnchanterRecipe> ENCHANTER;
	
	public static RecipeSerializer<EnchantmentUpgradeRecipe> ENCHANTMENT_UPGRADE_RECIPE_SERIALIZER;
	public static RecipeType<EnchantmentUpgradeRecipe> ENCHANTMENT_UPGRADE;
	
	public static RecipeSerializer<PotionWorkshopBrewingRecipe> POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER;
	public static RecipeType<PotionWorkshopBrewingRecipe> POTION_WORKSHOP_BREWING;
	
	public static RecipeSerializer<PotionWorkshopCraftingRecipe> POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER;
	public static RecipeType<PotionWorkshopCraftingRecipe> POTION_WORKSHOP_CRAFTING;
	
	public static RecipeSerializer<MidnightSolutionConvertingRecipe> MIDNIGHT_SOLUTION_CONVERTING_RECIPE_SERIALIZER;
	public static RecipeType<MidnightSolutionConvertingRecipe> MIDNIGHT_SOLUTION_CONVERTING_RECIPE;
	
	public static RecipeSerializer<SpiritInstillerRecipe> SPIRIT_INSTILLER_RECIPE_SERIALIZER;
	public static RecipeType<ISpiritInstillerRecipe> SPIRIT_INSTILLER_RECIPE;

	static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(SpectrumCommon.MOD_ID, id), serializer);
	}

	static <S extends RecipeType<T>, T extends Recipe<?>> S registerRecipeType(String id, S serializer) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(SpectrumCommon.MOD_ID, id), serializer);
	}

	public static void registerSerializer() {
		PEDESTAL_RECIPE_SERIALIZER = registerSerializer("pedestal", new PedestalCraftingRecipeSerializer(PedestalCraftingRecipe::new));
		PEDESTAL = registerRecipeType("pedestal", new RecipeType<PedestalCraftingRecipe>() {
			@Override
			public String toString() {return "spectrum:pedestal";}
		});

		ANVIL_CRUSHING_RECIPE_SERIALIZER = registerSerializer("anvil_crushing", new AnvilCrushingRecipeSerializer(AnvilCrushingRecipe::new));
		ANVIL_CRUSHING = registerRecipeType("anvil_crushing", new RecipeType<AnvilCrushingRecipe>() {
			@Override
			public String toString() {return "spectrum:anvil_crushing";}
		});

		FUSION_SHRINE_RECIPE_SERIALIZER = registerSerializer("fusion_shrine", new FusionShrineRecipeSerializer(FusionShrineRecipe::new));
		FUSION_SHRINE = registerRecipeType("fusion_shrine", new RecipeType<FusionShrineRecipe>() {
			@Override
			public String toString() {return "spectrum:fusion_shrine";}
		});
		
		ENCHANTER_RECIPE_SERIALIZER = registerSerializer("enchanter", new EnchanterRecipeSerializer(EnchanterRecipe::new));
		ENCHANTER = registerRecipeType("enchanter", new RecipeType<EnchanterRecipe>() {
			@Override
			public String toString() {return "spectrum:enchanter";}
		});
		
		ENCHANTMENT_UPGRADE_RECIPE_SERIALIZER = registerSerializer("enchantment_upgrade", new EnchantmentUpgradeRecipeSerializer(EnchantmentUpgradeRecipe::new));
		ENCHANTMENT_UPGRADE = registerRecipeType("enchantment_upgrade", new RecipeType<EnchantmentUpgradeRecipe>() {
			@Override
			public String toString() {return "spectrum:enchantment_upgrade";}
		});
		
		POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER = registerSerializer("potion_workshop_brewing", new PotionWorkshopBrewingRecipeSerializer(PotionWorkshopBrewingRecipe::new));
		POTION_WORKSHOP_BREWING = registerRecipeType("potion_workshop_brewing", new RecipeType<PotionWorkshopBrewingRecipe>() {
			@Override
			public String toString() {return "spectrum:potion_workshop_brewing";}
		});
		
		POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER = registerSerializer("potion_workshop_crafting", new PotionWorkshopCraftingRecipeSerializer(PotionWorkshopCraftingRecipe::new));
		POTION_WORKSHOP_CRAFTING = registerRecipeType("potion_workshop_crafting", new RecipeType<PotionWorkshopCraftingRecipe>() {
			@Override
			public String toString() {return "spectrum:potion_workshop_crafting";}
		});
		
		MIDNIGHT_SOLUTION_CONVERTING_RECIPE_SERIALIZER = registerSerializer("midnight_solution_converting", new MidnightSolutionConvertingRecipeSerializer(MidnightSolutionConvertingRecipe::new));
		MIDNIGHT_SOLUTION_CONVERTING_RECIPE = registerRecipeType("midnight_solution_converting", new RecipeType<MidnightSolutionConvertingRecipe>() {
			@Override
			public String toString() {return "spectrum:midnight_solution_converting";}
		});
		
		SPIRIT_INSTILLER_RECIPE_SERIALIZER = registerSerializer("spirit_instiller", new SpiritInstillerRecipeSerializer(SpiritInstillerRecipe::new));
		SPIRIT_INSTILLER_RECIPE = registerRecipeType("spirit_instiller", new RecipeType<ISpiritInstillerRecipe>() {
			@Override
			public String toString() {return "spectrum:spirit_instiller";}
		});

	}

}
