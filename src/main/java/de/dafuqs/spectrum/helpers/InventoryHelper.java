package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
	
	public static boolean removeFromInventoryWithRemainders(@NotNull Player playerEntity, @NotNull ItemStack stackToRemove) {
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
	
	public static Tuple<Integer, List<ItemStack>> getStackCountInInventory(ItemStack itemStack, List<ItemStack> inventory, int maxSearchAmount) {
		List<ItemStack> foundStacks = new ArrayList<>();
		int count = 0;
		for (ItemStack inventoryStack : inventory) {
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
				inventory.setItem(i, stacksToAdd.get(0));
				stacksToAdd.remove(0);
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
	
	// return are the recipe remainders
	public static List<ItemStack> removeFromInventoryWithRemainders(List<Ingredient> ingredients, Container inventory) {
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
	
	// return are the recipe remainders
	// TODO lots of code overlap with removeFromInventoryWithRemainders()
	public static List<ItemStack> removeIngredientStacksFromInventoryWithRemainders(List<IngredientStack> ingredients, Container inventory) {
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
	public static boolean canFitStacks(List<ItemStack> stacks, Container inventory) {
		var storage = InventoryStorage.of(inventory, null);

		if (!storage.supportsInsertion())
			return false;

		for (ItemStack stack : stacks) {
			if (stack.isEmpty())
				continue;
			
			if (StorageUtil.simulateInsert(storage, ItemVariant.of(stack), stack.getMaxStackSize(), null) != stack.getCount())
				return false;
		}

		return true;
	}
	
	public static List<ItemStack> getRemainders(List<Ingredient> ingredients, Container inventory) {
		List<ItemStack> remainders = new ArrayList<>();

		for (Ingredient ingredient : ingredients) {
			if (ingredient.isEmpty()) {
				continue;
			}
			
			if (ingredient.getItems().length > 0) {
				remainders.add(ingredient.getItems()[0].getRecipeRemainder());
			}
		}

		return remainders;
	}
	
	// returns recipe remainders
	public static List<ItemStack> removeFromInventoryWithRemainders(ItemStack removeItemStack, Container inventory) {
		List<ItemStack> remainders = new ArrayList<>();
		
		int removeItemStackCount = removeItemStack.getCount();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack currentStack = inventory.getItem(i);
			if (ItemStack.isSameItemSameComponents(currentStack, removeItemStack)) {
				ItemStack remainder = currentStack.getRecipeRemainder();
				
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
	
	public static boolean canExtract(Container inv, ItemStack stack, int slot, Direction facing) {
		return !(inv instanceof WorldlyContainer) || ((WorldlyContainer) inv).canTakeItemThroughFace(slot, stack, facing);
	}
	
	public static boolean canCombineItemStacks(ItemStack currentItemStack, ItemStack additionalItemStack) {
		return currentItemStack.isEmpty() || additionalItemStack.isEmpty() || (ItemStack.isSameItemSameComponents(currentItemStack, additionalItemStack)
				&& (currentItemStack.getCount() + additionalItemStack.getCount() <= currentItemStack.getMaxStackSize()));
	}
	
	@Nullable
	public static Container getInventoryAt(Level world, double x, double y, double z) {
		Container inventory = null;
		BlockPos blockPos = BlockPos.containing(x, y, z);
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof WorldlyContainerHolder) {
			inventory = ((WorldlyContainerHolder) block).getContainer(blockState, world, blockPos);
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Container) {
				inventory = (Container) blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getContainer((ChestBlock) block, blockState, world, blockPos, true);
				}
			}
		}
		
		if (inventory == null) {
			List<Entity> list = world.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelector.CONTAINER_ENTITY_SELECTOR);
			if (!list.isEmpty()) {
				inventory = (Container) list.get(world.getRandom().nextInt(list.size()));
			}
		}
		
		return inventory;
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
	
	public static int countItemsInInventory(List<ItemStack> inventory) {
		int contentCount = 0;
		for (ItemStack stack : inventory) {
			contentCount += stack.getCount();
		}
		return contentCount;
	}

}
