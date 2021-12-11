package de.dafuqs.spectrum.recipe.enchantment_upgrade;

import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnchantmentUpgradeRecipe implements Recipe<Inventory> {

	protected final Identifier id;
	
	protected final Enchantment enchantment;
	protected final int enchantmentDestinationLevel;
	protected final int requiredExperience;
	protected final Item requiredItem;
	protected final int requiredItemCount;
	@Nullable protected final Identifier requiredAdvancementIdentifier;
	
	protected final DefaultedList<Ingredient> inputs;
	protected final ItemStack output;
	
	public EnchantmentUpgradeRecipe(Identifier id, Enchantment enchantment, int enchantmentDestinationLevel, int requiredExperience, Item requiredItem, int requiredItemCount, @Nullable Identifier requiredAdvancementIdentifier) {
		this.id = id;
		
		this.enchantment = enchantment;
		this.enchantmentDestinationLevel = enchantmentDestinationLevel;
		this.requiredExperience = requiredExperience;
		this.requiredItem = requiredItem;
		this.requiredItemCount = requiredItemCount;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		
		DefaultedList<Ingredient> inputs = DefaultedList.ofSize(2, Ingredient.EMPTY);
		
		ItemStack ingredientStack = new ItemStack(Items.ENCHANTED_BOOK);
		ingredientStack.addEnchantment(enchantment, enchantmentDestinationLevel -1);
		inputs.set(0, Ingredient.ofStacks(ingredientStack)); // TODO: Does that work in multiplayer?
		inputs.set(1, Ingredient.ofStacks(new ItemStack(requiredItem)));
		this.inputs = inputs;
		
		ItemStack outputStack = new ItemStack(Items.ENCHANTED_BOOK);
		outputStack.addEnchantment(enchantment, enchantmentDestinationLevel);
		this.output = outputStack;
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof EnchantmentUpgradeRecipe) {
			return ((EnchantmentUpgradeRecipe) object).getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		// TODO
		if(inv.size() > 8) {
			for(int i = 0; i < 9; i++) {
				if(!inputs.get(i).test(inv.getStack(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return null;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.ENCHANTER);
	}

	public Identifier getId() {
		return this.id;
	}

	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ENCHANTMENT_UPGRADE_RECIPE_SERIALIZER;
	}

	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ENCHANTMENT_UPGRADE;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return inputs;
	}

	public int getRequiredExperience() {
		return requiredExperience;
	}

	/**
	 * The advancement the player has to have to let the recipe be craftable in the pedestal
	 * @return The advancement identifier. A null value means the player is always able to craft this recipe
	 */
	@Nullable
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
	public Item getRequiredItem() {
		return requiredItem;
	}
	
	public int getRequiredItemCount() {
		return requiredItemCount;
	}
	
}
