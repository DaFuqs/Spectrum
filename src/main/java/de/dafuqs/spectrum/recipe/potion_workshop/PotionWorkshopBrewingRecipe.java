package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class PotionWorkshopBrewingRecipe extends PotionWorkshopRecipe {

	protected final StatusEffect statusEffect;
	protected final int baseDurationTicks;
	protected final float potencyModifier;
	
	protected final boolean applicableToPotions;
	protected final boolean applicableToTippedArrows;

	public PotionWorkshopBrewingRecipe(Identifier id, String group, Ingredient baseIngredient, boolean consumeBaseIngredient, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, StatusEffect statusEffect, int baseDurationTicks, float potencyModifier, boolean applicableToPotions, boolean applicableToTippedArrows, Identifier requiredAdvancementIdentifier) {
		super(id, group, baseIngredient, consumeBaseIngredient, ingredient1, ingredient2, ingredient3, requiredAdvancementIdentifier);
		this.statusEffect = statusEffect;
		this.baseDurationTicks = baseDurationTicks;
		this.potencyModifier = potencyModifier;
		this.applicableToPotions = applicableToPotions;
		this.applicableToTippedArrows = applicableToTippedArrows;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_BREWING;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}
	
}
