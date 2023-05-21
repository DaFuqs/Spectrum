package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.text.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RestockingChestBlockEntity extends SpectrumChestBlockEntity implements SidedInventory {
	
	public static final int INVENTORY_SIZE = 27 + 4 + 4; // 27 items, 4 crafting tablets, 4 result slots
	public static final int[] CHEST_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
	public static final int[] RECIPE_SLOTS = new int[]{27, 28, 29, 30};
	public static final int[] RESULT_SLOTS = new int[]{31, 32, 33, 34};
	private int coolDownTicks = 0;
	
	public RestockingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.RESTOCKING_CHEST, blockPos, blockState);
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, RestockingChestBlockEntity restockingChestBlockEntity) {
		if (world.isClient) {
			restockingChestBlockEntity.lidAnimator.step();
		} else {
			if (tickCooldown(restockingChestBlockEntity)) {
				for (int i = 0; i < 4; i++) {
					ItemStack outputItemStack = restockingChestBlockEntity.inventory.get(RESULT_SLOTS[i]);
					ItemStack craftingTabletItemStack = restockingChestBlockEntity.inventory.get(RECIPE_SLOTS[i]);
					if (!craftingTabletItemStack.isEmpty() && (outputItemStack.isEmpty() || outputItemStack.getCount() < outputItemStack.getMaxCount())) {
						boolean couldCraft = restockingChestBlockEntity.tryCraft(restockingChestBlockEntity, i);
						if (couldCraft) {
							restockingChestBlockEntity.setCooldown(restockingChestBlockEntity, 20);
							restockingChestBlockEntity.markDirty();
							return;
						}
					}
				}
			}
		}
	}
	
	private static boolean tickCooldown(RestockingChestBlockEntity restockingChestBlockEntity) {
		restockingChestBlockEntity.coolDownTicks--;
		if (restockingChestBlockEntity.coolDownTicks > 0) {
			return false;
		} else {
			restockingChestBlockEntity.coolDownTicks = 0;
		}
		return true;
	}
	
	@Override
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.restocking_chest");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new RestockingChestScreenHandler(syncId, playerInventory, this);
	}
	
	private void setCooldown(RestockingChestBlockEntity restockingChestBlockEntity, int cooldownTicks) {
		restockingChestBlockEntity.coolDownTicks = cooldownTicks;
	}
	
	private boolean tryCraft(RestockingChestBlockEntity restockingChestBlockEntity, int index) {
		ItemStack craftingTabletItemStack = restockingChestBlockEntity.inventory.get(RECIPE_SLOTS[index]);
		if (craftingTabletItemStack.isOf(SpectrumItems.CRAFTING_TABLET)) {
			Recipe recipe = CraftingTabletItem.getStoredRecipe(world, craftingTabletItemStack);
			if (recipe instanceof ShapelessRecipe || recipe instanceof ShapedRecipe) {
				DefaultedList<Ingredient> ingredients = recipe.getIngredients();
				ItemStack outputItemStack = recipe.getOutput();
				ItemStack currentItemStack = restockingChestBlockEntity.inventory.get(RESULT_SLOTS[index]);
				if (InventoryHelper.canCombineItemStacks(currentItemStack, outputItemStack) && InventoryHelper.hasInInventory(ingredients, restockingChestBlockEntity)) {
					List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(ingredients, restockingChestBlockEntity);
					
					if (currentItemStack.isEmpty()) {
						restockingChestBlockEntity.inventory.set(RESULT_SLOTS[index], outputItemStack.copy());
					} else {
						currentItemStack.increment(outputItemStack.getCount());
					}
					
					for (ItemStack remainder : remainders) {
						InventoryHelper.smartAddToInventory(remainder, restockingChestBlockEntity, null);
					}
					return true;
				}
				
			}
		}
		return false;
	}
	
	@Override
	public int size() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("cooldown", coolDownTicks);
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		if (tag.contains("cooldown")) {
			coolDownTicks = tag.getInt("cooldown");
		}
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return RESULT_SLOTS;
		} else {
			return CHEST_SLOTS;
		}
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return slot <= CHEST_SLOTS[CHEST_SLOTS.length - 1];
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
}
