package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Random;

public class StarCandyRecipe extends PedestalCraftingRecipe {
	
	public static final RecipeSerializer<StarCandyRecipe> SERIALIZER = new SpecialRecipeSerializer<>(StarCandyRecipe::new);
	public static final Random RANDOM = new Random();
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("progression/unlock_star_candy");
	public static final float PURPLE_STAR_CANDY_CHANCE = 0.001F;
	public static final HashMap<BuiltinGemstoneColor, Integer> GEMSTONE_POWDER_INPUTS = new HashMap<>() {{
		put(BuiltinGemstoneColor.YELLOW, 1);
	}};
	
	public StarCandyRecipe(Identifier id) {
		super(id, "", false, UNLOCK_IDENTIFIER, PedestalRecipeTier.SIMPLE, 3, 3, generateInputs(), GEMSTONE_POWDER_INPUTS, SpectrumItems.STAR_CANDY.getDefaultStack(), 1.0F, 20, false, false);
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		if(RANDOM.nextFloat() < PURPLE_STAR_CANDY_CHANCE) {
			return SpectrumItems.PURPLE_STAR_CANDY.getDefaultStack();
		} else {
			return this.output.copy();
		}
	}
	
	private static DefaultedList<Ingredient> generateInputs() {
		DefaultedList<Ingredient> inputs = DefaultedList.ofSize(9);
		inputs.add(Ingredient.ofItems(Items.SUGAR));
		inputs.add(Ingredient.ofItems(Items.SUGAR));
		inputs.add(Ingredient.ofItems(Items.SUGAR));
		inputs.add(Ingredient.ofItems(SpectrumItems.STARDUST));
		inputs.add(Ingredient.ofItems(SpectrumItems.STARDUST));
		inputs.add(Ingredient.ofItems(SpectrumItems.STARDUST));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		return inputs;
	}
	
	
}
