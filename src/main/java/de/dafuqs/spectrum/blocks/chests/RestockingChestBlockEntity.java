package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.inventories.RestockingChestScreenHandler;
import de.dafuqs.spectrum.items.CraftingTabletItem;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RestockingChestBlockEntity extends SpectrumChestBlockEntity implements SidedInventory {
	
	public static final int INVENTORY_SIZE = 27 + 4 + 4; // 27 items, 4 crafting tablets, 4 result slots
	public static final int[] CHEST_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
	public static final int[] RECIPE_SLOTS = new int[]{27, 28, 29, 30};
	public static final int[] RESULT_SLOTS = new int[]{31, 32, 33, 34};
	private int coolDownTicks = 0;
	
	public RestockingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.RESTOCKING_CHEST, blockPos, blockState);
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
	
	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.restocking_chest");
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
				if (InventoryHelper.canCombineItemStacks(currentItemStack, outputItemStack) && InventoryHelper.removeFromInventory(ingredients, restockingChestBlockEntity, true)) {
					InventoryHelper.removeFromInventory(ingredients, restockingChestBlockEntity, false);
					
					if (currentItemStack.isEmpty()) {
						restockingChestBlockEntity.inventory.set(RESULT_SLOTS[index], outputItemStack.copy());
					} else {
						currentItemStack.increment(outputItemStack.getCount());
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
	
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("cooldown", coolDownTicks);
	}
	
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
