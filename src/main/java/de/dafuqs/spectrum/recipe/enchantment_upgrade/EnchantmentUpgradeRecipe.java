package de.dafuqs.spectrum.recipe.enchantment_upgrade;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;

import java.util.*;

public class EnchantmentUpgradeRecipe extends GatedSpectrumRecipe {
	
	protected final Enchantment enchantment;
	protected final int enchantmentDestinationLevel;
	protected final int requiredExperience;
	protected final Item requiredItem;
	protected final int requiredItemCount;
	
	protected final DefaultedList<Ingredient> inputs;
	protected final ItemStack output;
	
	public EnchantmentUpgradeRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Enchantment enchantment, int enchantmentDestinationLevel, int requiredExperience, Item requiredItem, int requiredItemCount) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.enchantment = enchantment;
		this.enchantmentDestinationLevel = enchantmentDestinationLevel;
		this.requiredExperience = requiredExperience;
		this.requiredItem = requiredItem;
		this.requiredItemCount = requiredItemCount;
		
		DefaultedList<Ingredient> inputs = DefaultedList.ofSize(2, Ingredient.EMPTY);
		
		ItemStack ingredientStack = new ItemStack(Items.ENCHANTED_BOOK);
		EnchantedBookItem.addEnchantment(ingredientStack, new EnchantmentLevelEntry(enchantment, enchantmentDestinationLevel - 1));
		inputs.set(0, Ingredient.ofStacks(ingredientStack));
		inputs.set(1, Ingredient.ofStacks(new ItemStack(requiredItem)));
		this.inputs = inputs;
		
		ItemStack outputStack = new ItemStack(Items.ENCHANTED_BOOK);
		EnchantedBookItem.addEnchantment(outputStack, new EnchantmentLevelEntry(enchantment, enchantmentDestinationLevel));
		this.output = outputStack;
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		if (inv.size() > 9) {
			if (!inputs.get(0).test(inv.getStack(0))) {
				return false;
			}
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(inv.getStack(0));
			if (!enchantments.containsKey(enchantment) || enchantments.get(enchantment) != enchantmentDestinationLevel - 1) {
				return false;
			}
			if (this.getRequiredExperience() > 0
					&& (!(inv.getStack(1).getItem() instanceof ExperienceStorageItem)
					|| !(ExperienceStorageItem.getStoredExperience(inv.getStack(1)) >= this.getRequiredExperience()))) {
				return false;
			}
			
			Ingredient inputIngredient = inputs.get(1);
			int ingredientsFound = 0;
			for (int i = 1; i < 9; i++) {
				ItemStack currentStack = inv.getStack(i + 1);
				
				if (!currentStack.isEmpty()) {
					ItemStack slotStack = inv.getStack(i + 1);
					if (inputIngredient.test(slotStack)) {
						ingredientsFound += slotStack.getCount();
					} else {
						return false;
					}
				}
			}
			
			return ingredientsFound >= requiredItemCount;
		}
		return false;
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		return null;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager drm) {
		return output;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.ENCHANTER);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ENCHANTMENT_UPGRADE_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ENCHANTMENT_UPGRADE;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return EnchanterRecipe.UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.ENCHANTMENT_UPGRADE_ID;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return inputs;
	}
	
	public int getRequiredExperience() {
		return requiredExperience;
	}
	
	public Item getRequiredItem() {
		return requiredItem;
	}
	
	public int getRequiredItemCount() {
		return requiredItemCount;
	}
	
	public Enchantment getEnchantment() {
		return enchantment;
	}
	
	public int getEnchantmentDestinationLevel() {
		return enchantmentDestinationLevel;
	}
	
	public boolean requiresUnlockedOverEnchanting() {
		return this.enchantmentDestinationLevel > this.enchantment.getMaxLevel();
	}
	
}
