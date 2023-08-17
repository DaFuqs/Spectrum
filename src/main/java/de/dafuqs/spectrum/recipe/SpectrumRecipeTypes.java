package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.recipe.ink_converting.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import net.minecraft.recipe.*;
import net.minecraft.util.registry.*;

public class SpectrumRecipeTypes {
	
	public static final String PEDESTAL_RECIPE_ID = "pedestal";
	public static RecipeSerializer<PedestalCraftingRecipe> PEDESTAL_RECIPE_SERIALIZER;
	public static RecipeType<PedestalCraftingRecipe> PEDESTAL;
	
	public static final String ANVIL_CRUSHING_ID = "anvil_crushing";
	public static RecipeSerializer<AnvilCrushingRecipe> ANVIL_CRUSHING_RECIPE_SERIALIZER;
	public static RecipeType<AnvilCrushingRecipe> ANVIL_CRUSHING;
	
	public static final String FUSION_SHRINE_ID = "fusion_shrine";
	public static RecipeSerializer<FusionShrineRecipe> FUSION_SHRINE_RECIPE_SERIALIZER;
	public static RecipeType<FusionShrineRecipe> FUSION_SHRINE;
	
	public static final String ENCHANTER_ID = "enchanter";
	public static RecipeSerializer<EnchanterRecipe> ENCHANTER_RECIPE_SERIALIZER;
	public static RecipeType<EnchanterRecipe> ENCHANTER;
	
	public static final String ENCHANTMENT_UPGRADE_ID = "enchantment_upgrade";
	public static RecipeSerializer<EnchantmentUpgradeRecipe> ENCHANTMENT_UPGRADE_RECIPE_SERIALIZER;
	public static RecipeType<EnchantmentUpgradeRecipe> ENCHANTMENT_UPGRADE;
	
	public static final String POTION_WORKSHOP_BREWING_ID = "potion_workshop_brewing";
	public static RecipeSerializer<PotionWorkshopBrewingRecipe> POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER;
	public static RecipeType<PotionWorkshopBrewingRecipe> POTION_WORKSHOP_BREWING;
	
	public static final String POTION_WORKSHOP_CRAFTING_ID = "potion_workshop_crafting";
	public static RecipeSerializer<PotionWorkshopCraftingRecipe> POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER;
	public static RecipeType<PotionWorkshopCraftingRecipe> POTION_WORKSHOP_CRAFTING;
	
	public static final String POTION_WORKSHOP_REACTING_ID = "potion_workshop_reacting";
	public static RecipeSerializer<PotionWorkshopReactingRecipe> POTION_WORKSHOP_REACTING_SERIALIZER;
	public static RecipeType<PotionWorkshopReactingRecipe> POTION_WORKSHOP_REACTING;
	
	public static final String MUD_CONVERTING_ID = "mud_converting";
	public static FluidConvertingRecipeSerializer<MudConvertingRecipe> MUD_CONVERTING_SERIALIZER;
	public static RecipeType<MudConvertingRecipe> MUD_CONVERTING;
	
	public static final String LIQUID_CRYSTAL_CONVERTING_ID = "liquid_crystal_converting";
	public static FluidConvertingRecipeSerializer<LiquidCrystalConvertingRecipe> LIQUID_CRYSTAL_CONVERTING_SERIALIZER;
	public static RecipeType<LiquidCrystalConvertingRecipe> LIQUID_CRYSTAL_CONVERTING;
	
	public static final String MIDNIGHT_SOLUTION_CONVERTING_ID = "midnight_solution_converting";
	public static FluidConvertingRecipeSerializer<MidnightSolutionConvertingRecipe> MIDNIGHT_SOLUTION_CONVERTING_SERIALIZER;
	public static RecipeType<MidnightSolutionConvertingRecipe> MIDNIGHT_SOLUTION_CONVERTING;
	
	public static final String DRAGONROT_CONVERTING_ID = "dragonrot_converting";
	public static FluidConvertingRecipeSerializer<DragonrotConvertingRecipe> DRAGONROT_CONVERTING_SERIALIZER;
	public static RecipeType<DragonrotConvertingRecipe> DRAGONROT_CONVERTING;

	public static final String SPIRIT_INSTILLING_ID = "spirit_instiller";
	public static RecipeSerializer<SpiritInstillerRecipe> SPIRIT_INSTILLING_SERIALIZER;
	public static RecipeType<SpiritInstillerRecipe> SPIRIT_INSTILLING;

	public static final String INK_CONVERTING_ID = "ink_converting";
	public static RecipeSerializer<InkConvertingRecipe> INK_CONVERTING_RECIPE_SERIALIZER;
	public static RecipeType<InkConvertingRecipe> INK_CONVERTING;

	public static final String CRYSTALLARIEUM_ID = "crystallarieum_growing";
	public static RecipeSerializer<CrystallarieumRecipe> CRYSTALLARIEUM_RECIPE_SERIALIZER;
	public static RecipeType<CrystallarieumRecipe> CRYSTALLARIEUM;
	
	public static final String CINDERHEARTH_ID = "cinderhearth";
	public static RecipeSerializer<CinderhearthRecipe> CINDERHEARTH_RECIPE_SERIALIZER;
	public static RecipeType<CinderhearthRecipe> CINDERHEARTH;
	
	public static final String TITRATION_BARREL_ID = "titration_barrel";
	public static RecipeSerializer<TitrationBarrelRecipe> TITRATION_BARREL_RECIPE_SERIALIZER;
	public static RecipeType<ITitrationBarrelRecipe> TITRATION_BARREL;
	
	static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, SpectrumCommon.locate(id), serializer);
	}
	
	static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String id) {
		return Registry.register(Registry.RECIPE_TYPE, SpectrumCommon.locate(id), new RecipeType<T>() {
			@Override
			public String toString() {
				return "spectrum:" + id;
			}
		});
	}
	
	public static void registerSerializer() {
		PEDESTAL_RECIPE_SERIALIZER = registerSerializer(PEDESTAL_RECIPE_ID, new PedestalCraftingRecipeSerializer(PedestalCraftingRecipe::new));
		PEDESTAL = registerRecipeType(PEDESTAL_RECIPE_ID);
		
		ANVIL_CRUSHING_RECIPE_SERIALIZER = registerSerializer(ANVIL_CRUSHING_ID, new AnvilCrushingRecipeSerializer(AnvilCrushingRecipe::new));
		ANVIL_CRUSHING = registerRecipeType(ANVIL_CRUSHING_ID);
		
		FUSION_SHRINE_RECIPE_SERIALIZER = registerSerializer(FUSION_SHRINE_ID, new FusionShrineRecipeSerializer(FusionShrineRecipe::new));
		FUSION_SHRINE = registerRecipeType(FUSION_SHRINE_ID);
		
		ENCHANTER_RECIPE_SERIALIZER = registerSerializer(ENCHANTER_ID, new EnchanterRecipeSerializer(EnchanterRecipe::new));
		ENCHANTER = registerRecipeType(ENCHANTER_ID);
		
		ENCHANTMENT_UPGRADE_RECIPE_SERIALIZER = registerSerializer(ENCHANTMENT_UPGRADE_ID, new EnchantmentUpgradeRecipeSerializer(EnchantmentUpgradeRecipe::new));
		ENCHANTMENT_UPGRADE = registerRecipeType(ENCHANTMENT_UPGRADE_ID);
		
		POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER = registerSerializer(POTION_WORKSHOP_BREWING_ID, new PotionWorkshopBrewingRecipeSerializer(PotionWorkshopBrewingRecipe::new));
		POTION_WORKSHOP_BREWING = registerRecipeType(POTION_WORKSHOP_BREWING_ID);
		
		POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER = registerSerializer(POTION_WORKSHOP_CRAFTING_ID, new PotionWorkshopCraftingRecipeSerializer(PotionWorkshopCraftingRecipe::new));
		POTION_WORKSHOP_CRAFTING = registerRecipeType(POTION_WORKSHOP_CRAFTING_ID);
		
		POTION_WORKSHOP_REACTING_SERIALIZER = registerSerializer(POTION_WORKSHOP_REACTING_ID, new PotionWorkshopReactingRecipeSerializer(PotionWorkshopReactingRecipe::new));
		POTION_WORKSHOP_REACTING = registerRecipeType(POTION_WORKSHOP_REACTING_ID);
		
		MUD_CONVERTING_SERIALIZER = registerSerializer(MUD_CONVERTING_ID, new FluidConvertingRecipeSerializer<>(MudConvertingRecipe::new));
		MUD_CONVERTING = registerRecipeType(MUD_CONVERTING_ID);
		
		LIQUID_CRYSTAL_CONVERTING_SERIALIZER = registerSerializer(LIQUID_CRYSTAL_CONVERTING_ID, new FluidConvertingRecipeSerializer<>(LiquidCrystalConvertingRecipe::new));
		LIQUID_CRYSTAL_CONVERTING = registerRecipeType(LIQUID_CRYSTAL_CONVERTING_ID);
		
		MIDNIGHT_SOLUTION_CONVERTING_SERIALIZER = registerSerializer(MIDNIGHT_SOLUTION_CONVERTING_ID, new FluidConvertingRecipeSerializer<>(MidnightSolutionConvertingRecipe::new));
		MIDNIGHT_SOLUTION_CONVERTING = registerRecipeType(MIDNIGHT_SOLUTION_CONVERTING_ID);
		
		DRAGONROT_CONVERTING_SERIALIZER = registerSerializer(DRAGONROT_CONVERTING_ID, new FluidConvertingRecipeSerializer<>(DragonrotConvertingRecipe::new));
		DRAGONROT_CONVERTING = registerRecipeType(DRAGONROT_CONVERTING_ID);

		SPIRIT_INSTILLING_SERIALIZER = registerSerializer(SPIRIT_INSTILLING_ID, new SpiritInstillerRecipeSerializer(SpiritInstillerRecipe::new));
		SPIRIT_INSTILLING = registerRecipeType(SPIRIT_INSTILLING_ID);

		INK_CONVERTING_RECIPE_SERIALIZER = registerSerializer(INK_CONVERTING_ID, new InkConvertingRecipeSerializer(InkConvertingRecipe::new));
		INK_CONVERTING = registerRecipeType(INK_CONVERTING_ID);

		CRYSTALLARIEUM_RECIPE_SERIALIZER = registerSerializer(CRYSTALLARIEUM_ID, new CrystallarieumRecipeSerializer(CrystallarieumRecipe::new));
		CRYSTALLARIEUM = registerRecipeType(CRYSTALLARIEUM_ID);
		
		CINDERHEARTH_RECIPE_SERIALIZER = registerSerializer(CINDERHEARTH_ID, new CinderhearthRecipeSerializer(CinderhearthRecipe::new));
		CINDERHEARTH = registerRecipeType(CINDERHEARTH_ID);
		
		TITRATION_BARREL_RECIPE_SERIALIZER = registerSerializer(TITRATION_BARREL_ID, new TitrationBarrelRecipeSerializer(TitrationBarrelRecipe::new));
		TITRATION_BARREL = registerRecipeType(TITRATION_BARREL_ID);
		
	}
	
}
