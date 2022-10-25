package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PotionWorkshopRecipe implements Recipe<Inventory>, GatedRecipe {
	
	public static final Identifier UNLOCK_POTION_WORKSHOP_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_potion_workshop");
	public static final int[] INGREDIENT_SLOTS = new int[]{2, 3, 4};
	
	protected final Identifier id;
	protected final String group;
	
	protected final int craftingTime;
	protected final int color;
	
	protected final Ingredient ingredient1;
	protected final Ingredient ingredient2;
	protected final Ingredient ingredient3;
	
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;
	
	public PotionWorkshopRecipe(Identifier id, String group, int craftingTime, int color, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, @Nullable Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.color = color;
		this.craftingTime = craftingTime;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}
	
	@Override
	public String getGroup() {
		return group;
	}
	
	public List<Ingredient> getOtherIngredients() {
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(ingredient1);
		if (!ingredient2.isEmpty()) {
			ingredients.add(ingredient2);
			if (!ingredient3.isEmpty()) {
				ingredients.add(ingredient3);
			}
		}
		return ingredients;
	}
	
	public boolean matches(@NotNull Inventory inv, World world) {
		if (inv.size() > 4 && inv.getStack(0).isOf(SpectrumItems.MERMAIDS_GEM) && isValidBaseIngredient(inv.getStack(1))) {
			
			if (usesReagents()) {
				if (!areStacksInReagentSlotsAllReagents(inv)) return false;
			} else {
				if (!areReagentSlotsEmpty(inv)) return false;
			}
			
			// check ingredients
			return matchIngredientStacksExclusively(inv, getOtherIngredients());
		} else {
			return false;
		}
	}
	
	private boolean areStacksInReagentSlotsAllReagents(@NotNull Inventory inv) {
		for (int i : new int[]{5, 6, 7, 8}) {
			ItemStack itemStack = inv.getStack(i);
			if (!itemStack.isEmpty() && !PotionWorkshopReactingRecipe.isReagent(itemStack.getItem())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean areReagentSlotsEmpty(@NotNull Inventory inv) {
		for (int i : new int[]{5, 6, 7, 8}) {
			ItemStack itemStack = inv.getStack(i);
			if (!itemStack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean matchIngredientStacksExclusively(Inventory inv, List<Ingredient> ingredients) {
		int inputStackCount = 0;
		for (int slot : INGREDIENT_SLOTS) {
			if (!inv.getStack(slot).isEmpty()) {
				inputStackCount++;
			}
		}
		if (inputStackCount != ingredients.size()) {
			return false;
		}
		
		for (Ingredient ingredient : ingredients) {
			boolean found = false;
			for (int slot : INGREDIENT_SLOTS) {
				if (ingredient.test(inv.getStack(slot))) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		
		return true;
	}
	
	public abstract boolean isValidBaseIngredient(ItemStack itemStack);
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	public boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	public ItemStack createIcon() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack();
	}
	
	public Identifier getId() {
		return this.id;
	}
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING_RECIPE_SERIALIZER;
	}
	
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof PotionWorkshopRecipe) {
			return ((PotionWorkshopRecipe) object).getId().equals(this.getId());
		}
		return false;
	}
	
	/**
	 * The advancement the player has to have to let the recipe be craftable
	 *
	 * @return The advancement identifier. A null value means the player is always able to craft this recipe
	 */
	@Nullable
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, UNLOCK_POTION_WORKSHOP_ADVANCEMENT_IDENTIFIER) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	public abstract boolean usesReagents();
	
	public abstract int getMinOutputCount(ItemStack baseItemStack);
	
	public int getColor() {
		return this.color;
	}
	
	@Override
	public TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.potion_workshop_recipe_unlocked.title");
	}
	
	@Override
	public TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.potion_workshop_recipes_unlocked.title");
	}
	
}
