package de.dafuqs.spectrum.recipe.spirit_instiller;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public interface ISpiritInstillerRecipe extends Recipe<Inventory>, GatedRecipe {
	
	public static final Identifier UNLOCK_SPIRIT_INSTILLER_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/build_spirit_instiller_structure");
	
	@Override
	public ItemStack getOutput();

	@Override
	public default boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public default ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.SPIRIT_INSTILLER);
	}
	
	@Override
	public Identifier getId();
	
	@Override
	public default RecipeType<?> getType() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE;
	}
	
	@Override
	public default ItemStack craft(Inventory inv) {
		return null;
	}
	
	@Override
	public default boolean matches(Inventory inv, World world) {
		List<IngredientStack> ingredientStacks = getIngredientStacks();
		if(inv.size() > 2) {
			if(ingredientStacks.get(2).test(inv.getStack(0))) {
				if(ingredientStacks.get(0).test(inv.getStack(1))) {
					return ingredientStacks.get(1).test(inv.getStack(2));
				} else if(ingredientStacks.get(0).test(inv.getStack(2))) {
					return ingredientStacks.get(1).test(inv.getStack(1));
				}
			}
		}
		return false;
	}
	
	@Deprecated
	@Override
	public DefaultedList<Ingredient> getIngredients();
	
	public List<IngredientStack> getIngredientStacks();
	
	@Override
	public default boolean fits(int width, int height) {
		return width * height >= 3;
	}
	
	@Override
	public default String getGroup() {
		return null;
	}
	
	public float getExperience();
	
	public int getCraftingTime();
	
	public Identifier getRequiredAdvancementIdentifier();
	
	public boolean areYieldAndEfficiencyUpgradesDisabled();
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity);
	
}
