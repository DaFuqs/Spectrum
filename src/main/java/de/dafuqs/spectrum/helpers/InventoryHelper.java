package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.items.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public class InventoryHelper {
	
	public static int getItemCountInInventory(Container inventory, Item item) {
		int count = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.is(item)) {
				count += stack.getCount();
			}
		}
		return count;
	}
	
	public static boolean decrementInPlayerInventory(Player playerEntity, ItemStack stackToRemove) {
		if (playerEntity.isCreative()) {
			return true;
		}
		
		// count how many we have in the inv
		Container playerInventory = playerEntity.getInventory();
		List<ItemStack> matchingStacks = new ArrayList<>();
		int paymentStackItemCount = 0;
		for (int i = 0; i < playerInventory.getContainerSize(); i++) {
			ItemStack currentStack = playerInventory.getItem(i);
			
			ItemProvider itemProvider = ItemProviderRegistry.getProvider(currentStack);
			if (itemProvider == null) {
				if (ItemStack.isSameItem(currentStack, stackToRemove)) {
					matchingStacks.add(currentStack);
					paymentStackItemCount += currentStack.getCount();
				}
			} else {
				matchingStacks.add(currentStack);
				paymentStackItemCount += itemProvider.getItemCount(playerEntity, currentStack, stackToRemove.getItem());
			}
			
			if (paymentStackItemCount >= stackToRemove.getCount()) {
				break;
			}
		}
		
		// did we find enough?
		if (paymentStackItemCount < stackToRemove.getCount()) {
			return false;
		}
		
		// decrement the inventory
		int amountToRemove = stackToRemove.getCount();
		for (ItemStack matchingStack : matchingStacks) {
			ItemProvider itemProvider = ItemProviderRegistry.getProvider(matchingStack);
			if (itemProvider != null) {
				amountToRemove -= itemProvider.provideItems(playerEntity, matchingStack, stackToRemove.getItem(), amountToRemove);
			} else {
				int currentRemove = Math.min(matchingStack.getCount(), amountToRemove);
				matchingStack.shrink(currentRemove);
				amountToRemove -= currentRemove;
				if (amountToRemove <= 0) {
					return true;
				}
			}
		}
		return true;
	}
	
	public static boolean isItemCountInInventory(Container inventory, ItemStack itemVariant, int maxSearchAmount) {
		return ContainerHelper.clearOrCountMatchingItems(inventory, stack -> ItemStack.isSameItemSameComponents(itemVariant, stack), maxSearchAmount, true) >= maxSearchAmount;
	}
	
	// Kept for parity with other code that may use IItemHandler-like checks in the future.
	// If you need a dedicated IItemHandler adapter, implement it in the caller; this project avoids net.minecraftforge imports.
	public static Tuple<Integer, List<ItemStack>> getStackCountInInventory(ItemStack itemStack, IItemHandler inventory, int maxSearchAmount) {
		List<ItemStack> foundStacks = new ArrayList<>();
		int count = 0;
		for (int slot = 0; slot < inventory.getSlots(); slot++) {
			ItemStack inventoryStack = inventory.getStackInSlot(slot);
			if (ItemStack.isSameItemSameComponents(inventoryStack, itemStack)) {
				foundStacks.add(inventoryStack);
				count += inventoryStack.getCount();
				if (count >= maxSearchAmount) {
					return new Tuple<>(count, foundStacks);
				}
			}
		}
		return new Tuple<>(count, foundStacks);
	}
	
	/**
	 * Adds a single itemstack to an inventory
	 *
	 * @param itemStack the itemstack to add. The stack can have a size >
	 *                  maxStackSize and will be split accordingly
	 * @param inventory the inventory to add to
	 * @return The remaining stack that could not be added
	 */
	public static ItemStack smartAddToInventory(ItemStack itemStack, Container inventory, @Nullable Direction side) {
		if (inventory instanceof WorldlyContainer sidedInventory && side != null) {
			int[] acceptableSlots = sidedInventory.getSlotsForFace(side);
			for (int acceptableSlot : acceptableSlots) {
				if (sidedInventory.canPlaceItemThroughFace(acceptableSlot, itemStack, side)) {
					itemStack = setOrCombineStack(inventory, acceptableSlot, itemStack);
					if (itemStack.isEmpty()) {
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				itemStack = setOrCombineStack(inventory, i, itemStack);
				if (itemStack.isEmpty()) {
					break;
				}
			}
		}
		return itemStack;
	}
	
	public static ItemStack setOrCombineStack(Container inventory, int slot, ItemStack addingStack) {
		ItemStack existingStack = inventory.getItem(slot);
		int max = Math.min(addingStack.getMaxStackSize(), inventory.getMaxStackSize());
		if (existingStack.isEmpty()) {
			if (addingStack.getCount() > max) {
				ItemStack newStack = addingStack.copy();
				newStack.setCount(max);
				addingStack.shrink(max);
				inventory.setItem(slot, newStack);
			} else {
				inventory.setItem(slot, addingStack);
				return ItemStack.EMPTY;
			}
		} else {
			combineStacks(existingStack, addingStack, max);
		}
		return addingStack;
	}
	
	public static void combineStacks(ItemStack originalStack, ItemStack addingStack) {
		combineStacks(originalStack, addingStack, originalStack.getMaxStackSize());
	}
	
	public static void combineStacks(ItemStack originalStack, ItemStack addingStack, int max) {
		if (ItemStack.isSameItemSameComponents(originalStack, addingStack)) {
			int leftOverAmountInExistingStack = max - originalStack.getCount();
			if (leftOverAmountInExistingStack > 0) {
				int addAmount = Math.min(leftOverAmountInExistingStack, addingStack.getCount());
				originalStack.grow(addAmount);
				addingStack.shrink(addAmount);
			}
		}
	}
	
	/**
	 * Adds a single stacks to an inventory in a given slot range
	 *
	 * @param inventory  the inventory to add to
	 * @param stackToAdd the stack to add to the inventory
	 * @param rangeStart the start insert slot
	 * @param rangeEnd   the last insert slot
	 * @return false if the stack could not be completely added
	 */
	public static boolean addToInventory(Container inventory, ItemStack stackToAdd, int rangeStart, int rangeEnd) {
		for (int i = rangeStart; i < rangeEnd; i++) {
			ItemStack currentStack = inventory.getItem(i);
			if (currentStack.isEmpty()) {
				inventory.setItem(i, stackToAdd);
				return true;
			} else if (stackToAdd.isStackable()) {
				combineStacks(currentStack, stackToAdd, inventory.getMaxStackSize());
				if (stackToAdd.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds a list of stacks to an inventory in a given slot range
	 *
	 * @param inventory   the inventory to add to
	 * @param stacksToAdd the stacks to add to the inventory
	 * @param rangeStart  the start insert slot
	 * @param rangeEnd    the last insert slot
	 * @return false if not add stacksToAdd could be added
	 */
	public static boolean addToInventory(Container inventory, List<ItemStack> stacksToAdd, int rangeStart, int rangeEnd) {
		for (int i = rangeStart; i < rangeEnd; i++) {
			ItemStack inventoryStack = inventory.getItem(i);
			if (inventoryStack.isEmpty()) {
				inventory.setItem(i, stacksToAdd.getFirst());
				stacksToAdd.removeFirst();
				if (stacksToAdd.isEmpty()) {
					return true;
				}
			}
			for (int j = 0; j < stacksToAdd.size(); j++) {
				ItemStack stackToAdd = stacksToAdd.get(j);
				if (stackToAdd.isStackable()) {
					combineStacks(inventoryStack, stackToAdd, inventory.getMaxStackSize());
					if (stackToAdd.isEmpty()) {
						stacksToAdd.remove(j);
						if (stacksToAdd.isEmpty()) {
							return true;
						}
						j--;
					}
				}
			}
		}
		return false;
	}
	
	// TODO: lots of code overlap with hasInInventory()
	public static boolean hasIngredientStacksInInventory(List<IngredientStack> ingredients, Container inventory) {
		List<Ingredient> ingredientsToFind = new ArrayList<>();
		List<Integer> requiredIngredientAmounts = new ArrayList<>();
		for (IngredientStack ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			ingredientsToFind.add(ingredient.getIngredient());
			requiredIngredientAmounts.add(ingredient.getCount());
		}
		
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (ingredientsToFind.isEmpty()) {
				break;
			}
			ItemStack currentStack = inventory.getItem(i);
			if (!currentStack.isEmpty()) {
				int amount = currentStack.getCount();
				for (int j = 0; j < ingredientsToFind.size(); j++) {
					if (ingredientsToFind.get(j).test(currentStack)) {
						int ingredientCount = requiredIngredientAmounts.get(j);
						if (amount >= ingredientCount) {
							ingredientsToFind.remove(j);
							requiredIngredientAmounts.remove(j);
							j--;
						} else {
							requiredIngredientAmounts.set(j, requiredIngredientAmounts.get(j) - amount);
						}
						
						amount -= ingredientCount;
						if (amount < 1) {
							break;
						}
					}
				}
			}
		}
		
		return ingredientsToFind.isEmpty();
	}
	
	public static boolean hasInInventory(List<Ingredient> ingredients, Container inventory) {
		List<Ingredient> ingredientsToFind = new ArrayList<>();
		List<Integer> requiredIngredientAmounts = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			ingredientsToFind.add(ingredient);
			if (ingredient.getItems().length > 0) {
				requiredIngredientAmounts.add(ingredient.getItems()[0].getCount());
			} else {
				requiredIngredientAmounts.add(1);
			}
		}
		
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (ingredientsToFind.isEmpty()) {
				break;
			}
			ItemStack currentStack = inventory.getItem(i);
			if (!currentStack.isEmpty()) {
				int amount = currentStack.getCount();
				for (int j = 0; j < ingredientsToFind.size(); j++) {
					if (ingredientsToFind.get(j).test(currentStack)) {
						int ingredientCount = requiredIngredientAmounts.get(j);
						if (amount >= ingredientCount) {
							ingredientsToFind.remove(j);
							requiredIngredientAmounts.remove(j);
							j--;
						} else {
							requiredIngredientAmounts.set(j, requiredIngredientAmounts.get(j) - amount);
						}
						
						amount -= ingredientCount;
						if (amount < 1) {
							break;
						}
					}
				}
			}
		}
		
		return ingredientsToFind.isEmpty();
	}
	
	public static List<ItemStack> decrementInInventoryAndReturnRemainders(List<Ingredient> ingredients, Container inventory) {
		List<ItemStack> remainders = new ArrayList<>();
		
		List<Ingredient> requiredIngredients = new ArrayList<>();
		List<Integer> requiredIngredientAmounts = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			requiredIngredients.add(ingredient);
			if (ingredient.getItems().length > 0) {
				requiredIngredientAmounts.add(ingredient.getItems()[0].getCount());
			} else {
				requiredIngredientAmounts.add(1);
			}
		}
		
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (requiredIngredients.isEmpty()) {
				break;
			}
			
			ItemStack currentStack = inventory.getItem(i);
			if (!currentStack.isEmpty()) {
				for (int j = 0; j < requiredIngredients.size(); j++) {
					int currentStackCount = currentStack.getCount();
					if (requiredIngredients.get(j).test(currentStack)) {
						int ingredientCount = requiredIngredientAmounts.get(j);
						ItemStack remainder = currentStack.getCraftingRemainingItem();
						if (currentStackCount >= ingredientCount) {
							if (!remainder.isEmpty()) {
								remainder.setCount(requiredIngredientAmounts.get(j));
								remainders.add(remainder);
							}
							requiredIngredients.remove(j);
							requiredIngredientAmounts.remove(j);
							j--;
						} else {
							if (!remainder.isEmpty()) {
								remainder.setCount(currentStackCount);
								remainders.add(remainder);
							}
							
							requiredIngredientAmounts.set(j, requiredIngredientAmounts.get(j) - currentStackCount);
						}
						
						currentStack.setCount(currentStackCount - ingredientCount);
					}
				}
			}
		}
		
		return remainders;
	}
	
	// TODO lots of code overlap with removeFromInventoryWithRemainders()
	public static List<ItemStack> decrementIngredientStacksInInventoryAndReturnRemainders(List<IngredientStack> ingredients, Container inventory) {
		List<ItemStack> remainders = new ArrayList<>();
		
		List<Ingredient> requiredIngredients = new ArrayList<>();
		List<Integer> requiredIngredientAmounts = new ArrayList<>();
		for (IngredientStack ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			requiredIngredients.add(ingredient.getIngredient());
			requiredIngredientAmounts.add(ingredient.getCount());
		}
		
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (requiredIngredients.isEmpty()) {
				break;
			}
			
			ItemStack currentStack = inventory.getItem(i);
			if (!currentStack.isEmpty()) {
				for (int j = 0; j < requiredIngredients.size(); j++) {
					int currentStackCount = currentStack.getCount();
					if (requiredIngredients.get(j).test(currentStack)) {
						int ingredientCount = requiredIngredientAmounts.get(j);
						ItemStack remainder = currentStack.getCraftingRemainingItem();
						if (currentStackCount >= ingredientCount) {
							if (!remainder.isEmpty()) {
								remainder.setCount(requiredIngredientAmounts.get(j));
								remainders.add(remainder);
							}
							requiredIngredients.remove(j);
							requiredIngredientAmounts.remove(j);
							j--;
						} else {
							if (!remainder.isEmpty()) {
								remainder.setCount(currentStackCount);
								remainders.add(remainder);
							}
							
							requiredIngredientAmounts.set(j, requiredIngredientAmounts.get(j) - currentStackCount);
						}
						
						currentStack.setCount(currentStackCount - ingredientCount);
					}
				}
			}
		}
		
		return remainders;
	}
	
	/**
	 * Check whether a list of stacks can be fully inserted into the provided inventory list.
	 * This simulates insertion without modifying the real inventory.
	 *
	 * @param inventory the target inventory represented as a List<ItemStack> (mutable slots)
	 * @param stacks    the list of stacks to fit into the inventory
	 * @return true if all stacks can be fully placed into the inventory, false otherwise
	 */
	public static boolean canFitStacks(List<ItemStack> inventory, List<ItemStack> stacks) {
		if (stacks.isEmpty()) {
			return true;
		}
		
		for (ItemStack stack : stacks) {
			if (stack.isEmpty()) continue;
			ItemStack remaining = stack.copy();
			
			// Simulate insertion into a copy of the inventory
			List<ItemStack> sim = new ArrayList<>(inventory.size());
			for (ItemStack s : inventory) sim.add(s.copy());
			
			// First try to merge into existing stacks
			for (ItemStack slot : sim) {
				if (slot.isEmpty()) continue;
				if (ItemStack.isSameItemSameComponents(slot, remaining)) {
					int space = Math.min(slot.getMaxStackSize(), remaining.getMaxStackSize()) - slot.getCount();
					if (space > 0) {
						int toMove = Math.min(space, remaining.getCount());
						slot.grow(toMove);
						remaining.shrink(toMove);
						if (remaining.isEmpty()) break;
					}
				}
			}
			
			// Then fill empty slots
			if (!remaining.isEmpty()) {
				for (int i = 0; i < sim.size(); i++) {
					ItemStack slot = sim.get(i);
					if (slot.isEmpty()) {
						int toInsert = Math.min(remaining.getCount(), remaining.getMaxStackSize());
						sim.set(i, remaining.copyWithCount(toInsert));
						remaining.shrink(toInsert);
						if (remaining.isEmpty()) break;
					}
				}
			}
			
			if (!remaining.isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
	public static List<ItemStack> getRemainders(List<Ingredient> ingredients) {
		List<ItemStack> remainders = new ArrayList<>();
		
		for (Ingredient ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			if (ingredient.getItems().length > 0) {
				remainders.add(ingredient.getItems()[0].getCraftingRemainingItem());
			}
		}
		
		return remainders;
	}
	
	// returns recipe remainders
	public static List<ItemStack> decrementInInventoryAndReturnRemainders(ItemStack removeItemStack, Container inventory) {
		List<ItemStack> remainders = new ArrayList<>();
		
		int removeItemStackCount = removeItemStack.getCount();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack currentStack = inventory.getItem(i);
			if (ItemStack.isSameItemSameComponents(currentStack, removeItemStack)) {
				ItemStack remainder = currentStack.getCraftingRemainingItem();
				
				int amountAbleToDecrement = Math.min(currentStack.getCount(), removeItemStackCount);
				currentStack.shrink(amountAbleToDecrement);
				removeItemStackCount -= amountAbleToDecrement;
				
				if (!remainder.isEmpty()) {
					remainder.setCount(amountAbleToDecrement);
					remainders.add(remainder);
				}
			}
			if (removeItemStackCount == 0) {
				return remainders;
			}
		}
		return remainders;
	}
	
	public static boolean canCombineItemStacks(ItemStack currentItemStack, ItemStack additionalItemStack) {
		return currentItemStack.isEmpty() || additionalItemStack.isEmpty() || (ItemStack.isSameItemSameComponents(currentItemStack, additionalItemStack)
				&& (currentItemStack.getCount() + additionalItemStack.getCount() <= currentItemStack.getMaxStackSize()));
	}
	
	public static Optional<ItemStack> extractLastStack(Container inventory) {
		ItemStack currentStack;
		for (int i = inventory.getContainerSize() - 1; i >= 0; i--) {
			currentStack = inventory.getItem(i);
			if (!currentStack.isEmpty()) {
				inventory.setItem(i, ItemStack.EMPTY);
				return Optional.of(currentStack);
			}
		}
		return Optional.empty();
	}
	
	public static ItemStack addToInventoryUpToSingleStackWithMaxTotalCount(ItemStack itemStack, Container inventory, int maxTotalCount) {
		// check if a stack that can be combined is in the inventory already
		int itemCount = 0;
		int firstEmptySlot = -1;
		ItemStack matchingStack = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack slotStack = inventory.getItem(i);
			
			if (slotStack.isEmpty()) {
				if (firstEmptySlot == -1) {
					firstEmptySlot = i;
				}
			} else {
				itemCount += slotStack.getCount();
				if (ItemStack.isSameItemSameComponents(itemStack, slotStack)) {
					matchingStack = slotStack;
				}
			}
		}
		
		int storageLeft = maxTotalCount - itemCount;
		if (storageLeft <= 0) {
			return itemStack;
		}
		
		if (matchingStack != null) {
			int addedCount = Math.min(matchingStack.getMaxStackSize() - matchingStack.getCount(), itemStack.getCount());
			addedCount = Math.min(storageLeft, addedCount);
			if (addedCount > 0) {
				matchingStack.setCount(matchingStack.getCount() + addedCount);
				itemStack.shrink(addedCount);
			}
			return itemStack;
		}
		
		if (firstEmptySlot == -1) {
			return itemStack;
		}
		
		inventory.setItem(firstEmptySlot, itemStack.split(storageLeft));
		return itemStack;
	}
	
	public static int countItemsInInventory(Container inventory) {
		int contentCount = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			contentCount += stack.getCount();
		}
		return contentCount;
	}
	
}