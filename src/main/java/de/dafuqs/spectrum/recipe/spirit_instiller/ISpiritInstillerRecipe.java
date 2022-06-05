package de.dafuqs.spectrum.recipe.spirit_instiller;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public interface ISpiritInstillerRecipe extends Recipe<Inventory>, GatedRecipe {
	
	Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/build_spirit_instiller_structure");
	
	@Override
	ItemStack getOutput();
	
	@Override
	default boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	default ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.SPIRIT_INSTILLER);
	}
	
	@Override
	Identifier getId();
	
	@Override
	default RecipeType<?> getType() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLING;
	}
	
	@Override
	default ItemStack craft(Inventory inv) {
		return null;
	}
	
	@Override
	default boolean matches(Inventory inv, World world) {
		List<IngredientStack> ingredientStacks = getIngredientStacks();
		if (inv.size() > 2) {
			if (ingredientStacks.get(2).test(inv.getStack(0))) {
				if (ingredientStacks.get(0).test(inv.getStack(1))) {
					return ingredientStacks.get(1).test(inv.getStack(2));
				} else if (ingredientStacks.get(0).test(inv.getStack(2))) {
					return ingredientStacks.get(1).test(inv.getStack(1));
				}
			}
		}
		return false;
	}
	
	// Use getIngredientStacks() instead
	// that includes counts in stacks
	@Deprecated
	@Override
	DefaultedList<Ingredient> getIngredients();
	
	List<IngredientStack> getIngredientStacks();
	
	@Override
	default boolean fits(int width, int height) {
		return width * height >= 3;
	}
	
	@Override
	default String getGroup() {
		return null;
	}
	
	float getExperience();
	
	int getCraftingTime();
	
	Identifier getRequiredAdvancementIdentifier();
	
	boolean areYieldAndEfficiencyUpgradesDisabled();
	
	@Override
	boolean canPlayerCraft(PlayerEntity playerEntity);
	
	boolean canCraftWithStacks(ItemStack instillerStack, ItemStack leftBowlStack, ItemStack rightBowlStack);
	
	static void grantPlayerSpiritInstillingAdvancementCriterion(World world, UUID playerUUID, ItemStack resultStack, int experience) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(world, playerUUID);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.SPIRIT_INSTILLER_CRAFTING.trigger(serverPlayerEntity, resultStack, experience);
		}
	}
	
	@Override
	default TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.spirit_instiller_recipe_unlocked.title");
	}
	
	@Override
	default TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.spirit_instiller_recipes_unlocked.title");
	}
	
}
