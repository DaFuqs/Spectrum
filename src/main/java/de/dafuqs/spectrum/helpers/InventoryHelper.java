package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.api.interaction.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InventoryHelper {

	public static int getItemCountInInventory(Inventory inventory, Item item) {
		int count = 0;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isOf(item)) {
				count += stack.getCount();
			}
		}
		return count;
	}
	
	public static boolean removeFromInventoryWithRemainders(@NotNull PlayerEntity playerEntity, @NotNull ItemStack stackToRemove) {
		if (playerEntity.isCreative()) {
			return true;
		}
		
		// count how many we have in the inv
		Inventory playerInventory = playerEntity.getInventory();
		List<ItemStack> matchingStacks = new ArrayList<>();
		int paymentStackItemCount = 0;
		for (int i = 0; i < playerInventory.size(); i++) {
			ItemStack currentStack = playerInventory.getStack(i);
			
			ItemProvider itemProvider = ItemProviderRegistry.getProvider(currentStack);
			if (itemProvider == null) {
				if (ItemStack.areItemsEqual(currentStack, stackToRemove)) {
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
				matchingStack.decrement(currentRemove);
				amountToRemove -= currentRemove;
				if (amountToRemove <= 0) {
					return true;
				}
			}
		}
		return true;
	}
	
	public static boolean isItemCountInInventory(List<ItemStack> inventory, ItemVariant itemVariant, int maxSearchAmount) {
		int count = 0;
		for (ItemStack inventoryStack : inventory) {
			if (itemVariant.matches(inventoryStack)) {
				count += inventoryStack.getCount();
				if (count >= maxSearchAmount) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Pair<Integer, List<ItemStack>> getStackCountInInventory(ItemStack itemStack, List<ItemStack> inventory, int maxSearchAmount) {
		List<ItemStack> foundStacks = new ArrayList<>();
		int count = 0;
		for (ItemStack inventoryStack : inventory) {
			if (ItemStack.canCombine(inventoryStack, itemStack)) {
				foundStacks.add(inventoryStack);
				count += inventoryStack.getCount();
				if (count >= maxSearchAmount) {
					return new Pair<>(count, foundStacks);
				}
			}
		}
		return new Pair<>(count, foundStacks);
	}

	/**
	 * Adds a single itemstack to an inventory
	 *
	 * @param itemStack the itemstack to add. The stack can have a size >
	 *                  maxStackSize and will be split accordingly
	 * @param inventory the inventory to add to
	 * @return The remaining stack that could not be added
	 */
	public static ItemStack smartAddToInventory(ItemStack itemStack, Inventory inventory, @Nullable Direction side) {
		if (inventory instanceof SidedInventory sidedInventory && side != null) {
			int[] acceptableSlots = sidedInventory.getAvailableSlots(side);
			for (int acceptableSlot : acceptableSlots) {
				if (sidedInventory.canInsert(acceptableSlot, itemStack, side)) {
					itemStack = setOrCombineStack(inventory, acceptableSlot, itemStack);
					if (itemStack.isEmpty()) {
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < inventory.size(); i++) {
				itemStack = setOrCombineStack(inventory, i, itemStack);
				if (itemStack.isEmpty()) {
					break;
				}
			}
		}
		return itemStack;
	}

	public static ItemStack setOrCombineStack(Inventory inventory, int slot, ItemStack addingStack) {
		ItemStack existingStack = inventory.getStack(slot);
		if (existingStack.isEmpty()) {
			if (addingStack.getCount() > addingStack.getMaxCount()) {
				int amount = addingStack.getMaxCount();
				amount = Math.min(amount, inventory.getMaxCountPerStack());
				ItemStack newStack = addingStack.copy();
				newStack.setCount(amount);
				addingStack.decrement(amount);
				inventory.setStack(slot, newStack);
			} else {
				inventory.setStack(slot, addingStack);
				return ItemStack.EMPTY;
			}
		} else {
			combineStacks(existingStack, addingStack);
		}
		return addingStack;
	}

	public static void combineStacks(ItemStack originalStack, ItemStack addingStack) {
		if (ItemStack.canCombine(originalStack, addingStack)) {
			int leftOverAmountInExistingStack = originalStack.getMaxCount() - originalStack.getCount();
			if (leftOverAmountInExistingStack > 0) {
				int addAmount = Math.min(leftOverAmountInExistingStack, addingStack.getCount());
				originalStack.increment(addAmount);
				addingStack.decrement(addAmount);
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
	public static boolean addToInventory(Inventory inventory, ItemStack stackToAdd, int rangeStart, int rangeEnd) {
		for (int i = rangeStart; i < rangeEnd; i++) {
			ItemStack currentStack = inventory.getStack(i);
			if (currentStack.isEmpty()) {
				inventory.setStack(i, stackToAdd);
				return true;
			} else if (stackToAdd.isStackable()) {
				combineStacks(currentStack, stackToAdd);
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
	public static boolean addToInventory(Inventory inventory, List<ItemStack> stacksToAdd, int rangeStart, int rangeEnd) {
		for (int i = rangeStart; i < rangeEnd; i++) {
			ItemStack inventoryStack = inventory.getStack(i);
			if (inventoryStack.isEmpty()) {
				inventory.setStack(i, stacksToAdd.get(0));
				stacksToAdd.remove(0);
				if (stacksToAdd.isEmpty()) {
					return true;
				}
			}
			for (int j = 0; j < stacksToAdd.size(); j++) {
				ItemStack stackToAdd = stacksToAdd.get(j);
				if (stackToAdd.isStackable()) {
					combineStacks(inventoryStack, stackToAdd);
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
	
	public static ItemStack addToInventory(List<ItemStack> inventory, ItemStack itemStack, int rangeStart, int rangeEnd) {
		for (int i = rangeStart; i < rangeEnd; i++) {
			ItemStack currentStack = inventory.get(i);
			if (currentStack.isEmpty()) {
				inventory.set(i, itemStack);
				return ItemStack.EMPTY;
			} else if (itemStack.isStackable()) {
				combineStacks(currentStack, itemStack);
				if (itemStack.isEmpty()) {
					return itemStack;
				}
			}
		}
		
		return itemStack;
	}
	
	public static boolean hasInInventory(List<Ingredient> ingredients, Inventory inventory) {
		List<Ingredient> ingredientsToFind = new ArrayList<>();
		List<Integer> requiredIngredientAmounts = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			ingredientsToFind.add(ingredient);
			if (ingredient.getMatchingStacks().length > 0) {
				requiredIngredientAmounts.add(ingredient.getMatchingStacks()[0].getCount());
			} else {
				requiredIngredientAmounts.add(1);
			}
		}
		
		for (int i = 0; i < inventory.size(); i++) {
			if (ingredientsToFind.isEmpty()) {
				break;
			}
			ItemStack currentStack = inventory.getStack(i);
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
	
	// return are the recipe remainders
	public static List<ItemStack> removeFromInventoryWithRemainders(List<Ingredient> ingredients, Inventory inventory) {
		List<ItemStack> remainders = new ArrayList<>();
		
		List<Ingredient> requiredIngredients = new ArrayList<>();
		List<Integer> requiredIngredientAmounts = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			requiredIngredients.add(ingredient);
			if (ingredient.getMatchingStacks().length > 0) {
				requiredIngredientAmounts.add(ingredient.getMatchingStacks()[0].getCount());
			} else {
				requiredIngredientAmounts.add(1);
			}
		}
		
		for (int i = 0; i < inventory.size(); i++) {
			if (requiredIngredients.isEmpty()) {
				break;
			}
			
			ItemStack currentStack = inventory.getStack(i);
			if (!currentStack.isEmpty()) {
				for (int j = 0; j < requiredIngredients.size(); j++) {
					int currentStackCount = currentStack.getCount();
					if (requiredIngredients.get(j).test(currentStack)) {
						int ingredientCount = requiredIngredientAmounts.get(j);
						ItemStack remainder = currentStack.getRecipeRemainder();
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

	@SuppressWarnings("UnstableApiUsage")
    public static boolean canFitStacks(List<ItemStack> stacks, Inventory inventory) {
		var storage = InventoryStorage.of(inventory, null);

		if (!storage.supportsInsertion())
			return false;

		for (ItemStack stack : stacks) {
			if (stack.isEmpty())
				continue;

			if (StorageUtil.simulateInsert(storage, ItemVariant.of(stack), stack.getMaxCount(), null) != stack.getCount())
				return false;
		}

		return true;
	}

	public static List<ItemStack> getRemainders(List<Ingredient> ingredients, Inventory inventory) {
		List<ItemStack> remainders = new ArrayList<>();

		for (Ingredient ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}

			if (ingredient.getMatchingStacks().length > 0) {
				remainders.add(ingredient.getMatchingStacks()[0].getRecipeRemainder());
			}
		}

		return remainders;
	}
	
	// returns recipe remainders
	public static List<ItemStack> removeFromInventoryWithRemainders(ItemStack removeItemStack, Inventory inventory) {
		List<ItemStack> remainders = new ArrayList<>();
		
		int removeItemStackCount = removeItemStack.getCount();
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack currentStack = inventory.getStack(i);
			if (ItemStack.canCombine(currentStack, removeItemStack)) {
				ItemStack remainder = currentStack.getRecipeRemainder();
				
				int amountAbleToDecrement = Math.min(currentStack.getCount(), removeItemStackCount);
				currentStack.decrement(amountAbleToDecrement);
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
	
	public static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
		return !(inv instanceof SidedInventory) || ((SidedInventory) inv).canExtract(slot, stack, facing);
	}
	
	public static boolean canCombineItemStacks(ItemStack currentItemStack, ItemStack additionalItemStack) {
		return currentItemStack.isEmpty() || additionalItemStack.isEmpty() || (ItemStack.canCombine(currentItemStack, additionalItemStack)
				&& (currentItemStack.getCount() + additionalItemStack.getCount() <= currentItemStack.getMaxCount()));
	}
	
	@Nullable
	public static Inventory getInventoryAt(World world, double x, double y, double z) {
		Inventory inventory = null;
		BlockPos blockPos = BlockPos.ofFloored(x, y, z);
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof InventoryProvider) {
			inventory = ((InventoryProvider) block).getInventory(blockState, world, blockPos);
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Inventory) {
				inventory = (Inventory) blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getInventory((ChestBlock) block, blockState, world, blockPos, true);
				}
			}
		}
		
		if (inventory == null) {
			List<Entity> list = world.getOtherEntities(null, new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.VALID_INVENTORIES);
			if (!list.isEmpty()) {
				inventory = (Inventory) list.get(world.random.nextInt(list.size()));
			}
		}
		
		return inventory;
	}
	
	public static Optional<ItemStack> extractLastStack(Inventory inventory) {
		ItemStack currentStack;
		for (int i = inventory.size() - 1; i >= 0; i--) {
			currentStack = inventory.getStack(i);
			if (!currentStack.isEmpty()) {
				inventory.setStack(i, ItemStack.EMPTY);
				return Optional.of(currentStack);
			}
		}
		return Optional.empty();
	}
	
	public static ItemStack addToInventoryUpToSingleStackWithMaxTotalCount(ItemStack itemStack, Inventory inventory, int maxTotalCount) {
		// check if a stack that can be combined is in the inventory already
		int itemCount = 0;
		int firstEmptySlot = -1;
		ItemStack matchingStack = null;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack slotStack = inventory.getStack(i);
			
			if (slotStack.isEmpty()) {
				if (firstEmptySlot == -1) {
					firstEmptySlot = i;
				}
			} else {
				itemCount += slotStack.getCount();
				if (ItemStack.canCombine(itemStack, slotStack)) {
					matchingStack = slotStack;
				}
			}
		}
		
		int storageLeft = maxTotalCount - itemCount;
		if (storageLeft <= 0) {
			return itemStack;
		}
		
		if (matchingStack != null) {
			int addedCount = Math.min(matchingStack.getMaxCount() - matchingStack.getCount(), itemStack.getCount());
			addedCount = Math.min(storageLeft, addedCount);
			if (addedCount > 0) {
				matchingStack.setCount(matchingStack.getCount() + addedCount);
				itemStack.decrement(addedCount);
			}
			return itemStack;
		}
		
		if (firstEmptySlot == -1) {
			return itemStack;
		}
		
		inventory.setStack(firstEmptySlot, itemStack.split(storageLeft));
		return itemStack;
	}
	
	public static int countItemsInInventory(Inventory inventory) {
		int contentCount = 0;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			contentCount += stack.getCount();
		}
		return contentCount;
	}
	
	public static int countItemsInInventory(List<ItemStack> inventory) {
		int contentCount = 0;
		for (ItemStack stack : inventory) {
			contentCount += stack.getCount();
		}
		return contentCount;
	}

}
