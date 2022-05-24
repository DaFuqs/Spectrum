package de.dafuqs.spectrum.recipe.spirit_instiller;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class SpiritInstillerSpawnerChangeRecipe implements ISpiritInstillerRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_spawner_changing");
	
	public static final RecipeSerializer<SpiritInstillerSpawnerChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpiritInstillerSpawnerChangeRecipe::new);
	
	public Identifier identifier;
	
	public SpiritInstillerSpawnerChangeRecipe(Identifier identifier) {
		super();
		this.identifier = identifier;
	}
	
	@Override
	public ItemStack getOutput() {
		return SpectrumItems.SPAWNER.getDefaultStack();
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		return SpectrumItems.SPAWNER.getDefaultStack();
	}
	
	@Override
	public Identifier getId() {
		return this.identifier;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(Ingredient.ofItems(Items.SPAWNER));
		defaultedList.add(Ingredient.fromTag(SpectrumItemTags.MOB_HEADS));
		defaultedList.add(Ingredient.ofItems(SpectrumItems.VEGETAL));
		return defaultedList;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		DefaultedList<IngredientStack> defaultedList = DefaultedList.of();
		defaultedList.add(IngredientStack.of(Ingredient.ofItems(Items.SPAWNER)));
		defaultedList.add(IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.MOB_HEADS)));
		defaultedList.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.VEGETAL), 4));
		return defaultedList;
	}
	
	@Override
	public float getExperience() {
		return 0;
	}
	
	@Override
	public int getCraftingTime() {
		return 400;
	}
	
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return true;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, SpiritInstillerRecipe.UNLOCK_SPIRIT_INSTILLER_ADVANCEMENT_IDENTIFIER) && Support.hasAdvancement(playerEntity, UNLOCK_IDENTIFIER);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
