package de.dafuqs.spectrum.enchantments;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.gofish.GoFishCompat;
import de.dafuqs.spectrum.items.tools.MoltenRodItem;
import de.dafuqs.spectrum.items.tools.SpectrumFishingRodItem;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FoundryEnchantment extends SpectrumEnchantment {
	
	private static final AutoSmeltInventory autoSmeltInventory = new AutoSmeltInventory();
	
	public FoundryEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static ItemStack getAutoSmeltedItemStack(ItemStack inputItemStack, World world) {
		SmeltingRecipe smeltingRecipe = autoSmeltInventory.getRecipe(inputItemStack, world);
		if (smeltingRecipe != null) {
			ItemStack recipeOutputStack = smeltingRecipe.getOutput().copy();
			recipeOutputStack.setCount(recipeOutputStack.getCount() * inputItemStack.getCount());
			return recipeOutputStack;
		} else {
			return null;
		}
	}
	
	@NotNull
	public static List<ItemStack> applyAutoSmelt(World world, List<ItemStack> originalStacks) {
		List<ItemStack> returnItemStacks = new ArrayList<>();
		
		for (ItemStack is : originalStacks) {
			ItemStack smeltedStack = FoundryEnchantment.getAutoSmeltedItemStack(is, world);
			if (smeltedStack == null) {
				returnItemStacks.add(is);
			} else {
				while (smeltedStack.getCount() > 0) {
					int currentAmount = Math.min(smeltedStack.getCount(), smeltedStack.getItem().getMaxCount());
					ItemStack currentStack = smeltedStack.copy();
					currentStack.setCount(currentAmount);
					returnItemStacks.add(currentStack);
					smeltedStack.setCount(smeltedStack.getCount() - currentAmount);
				}
			}
		}
		return returnItemStacks;
	}
	
	@Override
	public int getMinPower(int level) {
		return 15;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 50;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.SILK_TOUCH && other != SpectrumEnchantments.RESONANCE && !GoFishCompat.isDeepfry(other);
	}
	
	@Override
	public boolean canEntityUse(Entity entity) {
		return super.canEntityUse(entity) || (entity instanceof PlayerEntity playerEntity && AdvancementHelper.hasAdvancement(playerEntity, MoltenRodItem.UNLOCK_IDENTIFIER));
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || stack.getItem() instanceof SpectrumFishingRodItem;
	}
	
	public static class AutoSmeltInventory implements Inventory, RecipeInputProvider {
		ItemStack input = ItemStack.EMPTY;
		
		@Override
		public int size() {
			return 1;
		}
		
		@Override
		public boolean isEmpty() {
			return input.isEmpty();
		}
		
		@Override
		public ItemStack getStack(int slot) {
			return input;
		}
		
		@Override
		public ItemStack removeStack(int slot, int amount) {
			return null;
		}
		
		@Override
		public ItemStack removeStack(int slot) {
			return null;
		}
		
		@Override
		public void setStack(int slot, ItemStack stack) {
			this.input = stack;
		}
		
		@Override
		public void markDirty() {
		}
		
		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			return false;
		}
		
		@Override
		public void clear() {
			input = ItemStack.EMPTY;
		}
		
		private SmeltingRecipe getRecipe(ItemStack itemStack, World world) {
			setStack(0, itemStack);
			return world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, this, world).orElse(null);
		}
		
		@Override
		public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
			recipeMatcher.addInput(input);
		}
		
	}
	
}