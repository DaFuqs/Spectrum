package de.dafuqs.spectrum;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InventoryHelper {
	
	public static boolean removeFromInventory(@NotNull PlayerEntity playerEntity, @NotNull ItemStack stackToRemove) {
		if(playerEntity.isCreative()) {
			return true;
		} else {
			Inventory playerInventory = playerEntity.getInventory();
			List<Pair<Integer, ItemStack>> matchingStacks = new ArrayList<>();
			int paymentStackItemCount = 0;
			for (int i = 0; i < playerInventory.size(); i++) {
				ItemStack currentStack = playerInventory.getStack(i);
				if (currentStack.getItem().equals(stackToRemove.getItem())) {
					matchingStacks.add(new Pair<>(i, currentStack));
					paymentStackItemCount += currentStack.getCount();
					if (paymentStackItemCount >= stackToRemove.getCount()) {
						break;
					}
				}
			}
			
			if (paymentStackItemCount < stackToRemove.getCount()) {
				return false;
			} else {
				int amountToRemove = stackToRemove.getCount();
				for (Pair<Integer, ItemStack> matchingStack : matchingStacks) {
					if (matchingStack.getRight().getCount() <= amountToRemove) {
						amountToRemove -= matchingStack.getRight().getCount();
						playerEntity.getInventory().setStack(matchingStack.getLeft(), ItemStack.EMPTY);
						if(amountToRemove <= 0) {
							break;
						}
					} else {
						matchingStack.getRight().decrement(amountToRemove);
						return true;
					}
				}
				return true;
			}
		}
	}
	
	public static Pair<Integer, List<ItemStack>> getStackCountInInventory(ItemStack itemStack, List<ItemStack> inventory) {
		List<ItemStack> foundStacks = new ArrayList<>();
		int count = 0;
		for(ItemStack inventoryStack : inventory) {
			if(inventoryStack.isItemEqual(itemStack)) {
				foundStacks.add(inventoryStack);
				count += inventoryStack.getCount();
			}
		}
		return new Pair(count, foundStacks);
	}

	public static boolean existsStackInInventory(ItemStack itemStack, List<ItemStack> inventory) {
		for(ItemStack inventoryStack : inventory) {
			if(inventoryStack.isItemEqual(itemStack)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a single itemstack to an inventory
	 * @param itemStack the itemstack to add
	 * @param inventory the inventory to add to
	 * @return The remaining stack that could not be added
	 */
	public static ItemStack smartAddToInventory(ItemStack itemStack, Inventory inventory, @Nullable Direction side) {
		if(inventory instanceof SidedInventory && side != null) {
			int[] acceptableSlots = ((SidedInventory) inventory).getAvailableSlots(side);
			for(int acceptableSlot : acceptableSlots) {
				if (((SidedInventory) inventory).canInsert(acceptableSlot, itemStack, side)) {
					ItemStack existingItemStack = inventory.getStack(acceptableSlot);
					inventory.setStack(acceptableSlot, combineStacks(existingItemStack, itemStack));
					if (itemStack.isEmpty()) {
						break;
					}
				}
			}
		} else {
			for(int i = 0; i < inventory.size(); i++) {
				ItemStack existingItemStack = inventory.getStack(i);
				if(existingItemStack.isEmpty()) {
					inventory.setStack(i, itemStack);
					return ItemStack.EMPTY;
				} else {
					inventory.setStack(i, combineStacks(existingItemStack, itemStack));
					if(itemStack.isEmpty()) {
						break;
					}
				}
			}
		}
		return itemStack;
	}

	public static ItemStack combineStacks(ItemStack originalStack, ItemStack addingStack) {
		if(ItemStack.canCombine(originalStack, addingStack)) {
			int leftOverAmountInExistingStack = originalStack.getMaxCount() - originalStack.getCount();
			if(leftOverAmountInExistingStack > 0) {
				int addAmount = Math.min(leftOverAmountInExistingStack, addingStack.getCount());
				originalStack.increment(addAmount);
				addingStack.decrement(addAmount);
			}
		}
		return originalStack;
	}

	public static boolean smartAddToInventory(List<ItemStack> itemStacks, List<ItemStack> inventory, boolean test) {
		List<ItemStack> additionStacks = new ArrayList<>();
		for(ItemStack itemStack : itemStacks) {
			additionStacks.add(itemStack.copy());
		}

		boolean tryStackExisting = true;
		for(int i = 0; i < inventory.size(); i++) {
			ItemStack currentStack = inventory.get(i);
			for(ItemStack additionStack : additionStacks) {
				boolean doneStuff = false;
				if (additionStack.getCount() > 0) {
					if (currentStack.isEmpty() && (test || !tryStackExisting)) {
						int maxStackCount = currentStack.getMaxCount();
						int maxAcceptCount = Math.min(additionStack.getCount(), maxStackCount);

						if (!test) {
							ItemStack newStack = additionStack.copy();
							newStack.setCount(maxAcceptCount);
							inventory.set(i, newStack);
						}
						additionStack.setCount(additionStack.getCount() - maxAcceptCount);
						doneStuff = true;
					} else if (additionStack.isItemEqual(currentStack)) {
						// add to stack;
						int maxStackCount = currentStack.getMaxCount();
						int canAcceptCount = maxStackCount - currentStack.getCount();

						if (canAcceptCount > 0) {
							if (!test) {
								inventory.get(i).increment(Math.min(additionStack.getCount(), canAcceptCount));
							}
							if (canAcceptCount >= additionStack.getCount()) {
								additionStack.setCount(0);
							} else {
								additionStack.setCount(additionStack.getCount() - canAcceptCount);
							}
							doneStuff = true;
						}
					}

					// if there were changes: check if all stacks have count 0
					if(doneStuff) {
						boolean allEmpty = true;
						for(ItemStack itemStack : additionStacks) {
							if (itemStack.getCount() > 0) {
								allEmpty = false;
								break;
							}
						}
						if(allEmpty) {
							return true;
						}
					}
				}
			}
			
			if(tryStackExisting && !test && i == inventory.size() - 1) {
				tryStackExisting = false;
				i = -1;
			}
		}
		return false;
	}

	public static boolean removeFromInventory(List<Ingredient> ingredients, Inventory inventory, boolean test) {
		List<Ingredient> ingredientsToFind = new ArrayList<>(ingredients);

		for(int i = 0; i < inventory.size(); i++) {
			if(ingredientsToFind.size() == 0) {
				break;
			}
			ItemStack currentStack = inventory.getStack(i);

			int amount = currentStack.getCount();
			for(int j = 0; j < ingredientsToFind.size(); j++) {
				Ingredient ingredient = ingredientsToFind.get(j);

				if (amount > 0 && ingredient.test(currentStack)) {
					int ingredientCount = ingredient.getMatchingStacks()[0].getCount();
					if(amount >= ingredientCount) {
						ingredientsToFind.remove(j);
					} else {
						ingredientsToFind.get(j).getMatchingStacks()[0].setCount(ingredientCount-amount);
					}
					j--;

					amount -= ingredientCount;
					if(!test) {
						if(amount > 0) {
							currentStack.setCount(amount);
						} else {
							inventory.setStack(i, ItemStack.EMPTY);
						}
					}
				}
			}
		}

		return ingredientsToFind.size() == 0;
	}

	public static boolean removeFromInventory(ItemStack removeItemStack, List<ItemStack> inventory) {
		int removeItemStackCount = removeItemStack.getCount();
		for(int i = 0; i < inventory.size(); i++) {
			ItemStack currentStack = inventory.get(i);
			if(removeItemStack.isItemEqual(currentStack)) {
				int currentStackCount = currentStack.getCount();
				if(currentStackCount >= removeItemStackCount) {
					currentStack.decrement(removeItemStackCount);
					inventory.set(i, currentStack);
					removeItemStackCount = 0;
				} else {
					removeItemStackCount -= currentStackCount;
					inventory.set(i, ItemStack.EMPTY);
				}
			}
			if(removeItemStackCount == 0) {
				return true;
			}
		}
		return false;
	}

	public static IntStream getAvailableSlots(Inventory inventory, Direction side) {
		return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
	}

	public static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
		return !(inv instanceof SidedInventory) || ((SidedInventory)inv).canExtract(slot, stack, facing);
	}

	private static boolean isInventoryEmpty(Inventory inv, Direction facing) {
		return getAvailableSlots(inv, facing).allMatch((i) -> inv.getStack(i).isEmpty());
	}

	public static boolean canCombineItemStacks(ItemStack currentItemStack, ItemStack additionalItemStack) {
		return currentItemStack.isEmpty() || additionalItemStack.isEmpty() || (currentItemStack.isItemEqual(additionalItemStack) && (currentItemStack.getCount() + additionalItemStack.getCount() <= currentItemStack.getMaxCount()));
	}

	@Nullable
	public static Inventory getInventoryAt(World world, double x, double y, double z) {
		Inventory inventory = null;
		BlockPos blockPos = new BlockPos(x, y, z);
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof InventoryProvider) {
			inventory = ((InventoryProvider)block).getInventory(blockState, world, blockPos);
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Inventory) {
				inventory = (Inventory)blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
				}
			}
		}

		if (inventory == null) {
			List<Entity> list = world.getOtherEntities(null, new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.VALID_INVENTORIES);
			if (!list.isEmpty()) {
				inventory = (Inventory)list.get(world.random.nextInt(list.size()));
			}
		}

		return inventory;
	}

}
