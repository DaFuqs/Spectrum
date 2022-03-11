package de.dafuqs.spectrum.recipe.enchantment_upgrade;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
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

import java.util.Map;

public class EnchantmentUpgradeRecipe implements Recipe<Inventory>, GatedRecipe {
	
	public static final Identifier UNLOCK_FUSION_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/build_fusion_shrine");
	
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
		inputs.set(0, Ingredient.ofStacks(ingredientStack));
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
		if(inv.size() > 9) {
			if(!inputs.get(0).test(inv.getStack(0))) {
				return false;
			}
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(inv.getStack(0));
			if(!enchantments.containsKey(enchantment) || enchantments.get(enchantment) != enchantmentDestinationLevel - 1) {
				return false;
			}
			if(this.getRequiredExperience() > 0
					&& (!(inv.getStack(1).getItem() instanceof ExperienceStorageItem)
					|| !(ExperienceStorageItem.getStoredExperience(inv.getStack(1)) >= this.getRequiredExperience()))) {
				return false;
			}
			
			Ingredient inputIngredient = inputs.get(1);
			boolean ingredientFound = false;
			for (int i = 1; i < 9; i++) {
				ItemStack currentStack = inv.getStack(i + 1);
				
				if(!currentStack.isEmpty()) {
					if (inputIngredient.test(inv.getStack(i + 1))) {
						ingredientFound = true;
					} else {
						return false;
					}
				}
			}
			
			return ingredientFound;
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
	
	@Override
	public Identifier getId() {
		return this.id;
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
	
	public Enchantment getEnchantment() {
		return enchantment;
	}
	
	public int getEnchantmentDestinationLevel() {
		return enchantmentDestinationLevel;
	}
	
	public boolean requiresUnlockedOverEnchanting() {
		return this.enchantmentDestinationLevel > this.enchantment.getMaxLevel();
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, UNLOCK_FUSION_ADVANCEMENT_IDENTIFIER) && Support.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
}
