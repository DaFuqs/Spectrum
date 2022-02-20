package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopReagents;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PotionWorkshopRecipe implements Recipe<Inventory>, GatedRecipe {

	protected final Identifier id;
	protected final String group;
	
	protected final Ingredient baseIngredient;
	protected final boolean consumeBaseIngredient;
	
	protected final Ingredient ingredient1;
	protected final Ingredient ingredient2;
	protected final Ingredient ingredient3;
	
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;

	public PotionWorkshopRecipe(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.baseIngredient = baseIngredient;
		this.consumeBaseIngredient = consumeBaseIngredient;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}
	
	@Override
	public String getGroup() {
		return group;
	}
	
	public Ingredient getBaseIngredient() {
		return baseIngredient;
	}
	
	public boolean consumesBaseIngredient() {
		return consumeBaseIngredient;
	}
	
	public List<Ingredient> getOtherIngredients() {
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(ingredient1);
		if(!ingredient2.isEmpty()) {
			ingredients.add(ingredient2);
			if(!ingredient3.isEmpty()) {
				ingredients.add(ingredient3);
			}
		}
		return ingredients;
	}

	public boolean matches(@NotNull Inventory inv, World world) {
		if(inv.size() > 4 && inv.getStack(0).isOf(SpectrumItems.MERMAIDS_GEM) && this.baseIngredient.test(inv.getStack(1))) {
			// check reagents
			if(usesReagents()) {
				// check if all items in reagent slots are actually reagents
				for(int i : new int[]{5,6,7,8}) {
					ItemStack itemStack = inv.getStack(i);
					if(!itemStack.isEmpty() && !PotionWorkshopReagents.isReagent(itemStack.getItem())) {
						return false;
					}
				}
			} else {
				// check if all reagent slots are empty
				for(int i : new int[]{5,6,7,8}) {
					ItemStack itemStack = inv.getStack(i);
					if(!itemStack.isEmpty()) {
						return false;
					}
				}
			}
			
			// check ingredients
			for(Ingredient ingredient : this.getOtherIngredients()) {
				boolean found = false;
				for(int i = 2; i < 5; i++) {
					if(ingredient.test(inv.getStack(i))) {
						found = true;
						break;
					}
				}
				if(!found) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
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

	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.baseIngredient);
		defaultedList.add(this.ingredient1);
		defaultedList.add(this.ingredient2);
		defaultedList.add(this.ingredient3);
		return defaultedList;
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof PotionWorkshopRecipe) {
			return ((PotionWorkshopRecipe) object).getId().equals(this.getId());
		}
		return false;
	}
	
	/**
	 * The advancement the player has to have to let the recipe be craftable
	 * @return The advancement identifier. A null value means the player is always able to craft this recipe
	 */
	@Nullable
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	public abstract boolean usesReagents();
	
	public abstract int getMinOutputCount();
	
}
