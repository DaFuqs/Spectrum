package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Random;

public class StarCookieRecipe extends PedestalCraftingRecipe {
	
	public static final RecipeSerializer<StarCookieRecipe> SERIALIZER = new SpecialRecipeSerializer<>(StarCookieRecipe::new);
	public static final Random RANDOM = new Random();
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("progression/unlock_star_cookie");
	public static final float ENCHANTED_STAR_COOKIE_CHANCE = 0.001F;
	public static final HashMap<BuiltinGemstoneColor, Integer> GEMSTONE_DUST_INPUTS = new HashMap<>() {{
		put(BuiltinGemstoneColor.CYAN, 0);
		put(BuiltinGemstoneColor.MAGENTA, 0);
		put(BuiltinGemstoneColor.YELLOW, 1);
		put(BuiltinGemstoneColor.BLACK, 0);
		put(BuiltinGemstoneColor.WHITE, 0);
	}};
	
	public StarCookieRecipe(Identifier id) {
		super(id, "", PedestalRecipeTier.SIMPLE, 3, 3, generateInputs(), GEMSTONE_DUST_INPUTS, SpectrumItems.STAR_COOKIE.getDefaultStack(), 1.0F, 20, false, false, UNLOCK_IDENTIFIER);
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		if(RANDOM.nextFloat() < ENCHANTED_STAR_COOKIE_CHANCE) {
			return SpectrumItems.ENCHANTED_STAR_COOKIE.getDefaultStack();
		} else {
			return this.output.copy();
		}
	}
	
	private static DefaultedList<Ingredient> generateInputs() {
		DefaultedList<Ingredient> inputs = DefaultedList.ofSize(9);
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		inputs.add(Ingredient.ofItems(SpectrumItems.STARDUST));
		inputs.add(Ingredient.ofItems(SpectrumItems.STARDUST));
		inputs.add(Ingredient.ofItems(SpectrumItems.STARDUST));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		inputs.add(Ingredient.ofItems(SpectrumItems.AMARANTH_GRAINS));
		return inputs;
	}
	
	
}
