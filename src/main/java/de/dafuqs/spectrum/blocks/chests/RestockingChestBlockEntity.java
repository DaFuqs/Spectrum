package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.networking.*;
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
import java.util.stream.*;

public class RestockingChestBlockEntity extends SpectrumChestBlockEntity implements SidedInventory {
	
	public static final int INVENTORY_SIZE = 27 + 4 + 4; // 27 items, 4 crafting tablets, 4 result slots
	public static final int[] CHEST_SLOTS = IntStream.rangeClosed(0, 26).toArray();
	public static final int[] RECIPE_SLOTS = IntStream.rangeClosed(27, 30).toArray();
	public static final int[] RESULT_SLOTS = IntStream.rangeClosed(31, 34).toArray();
	private List<ItemStack> cachedOutputs = new ArrayList<>(4);
	private int coolDownTicks = 0;
	private boolean isOpen, isFull, hasValidRecipes;
	private State state = State.CLOSED;
	float rimTarget, rimPos, lastRimTarget, tabletTarget, tabletPos, lastTabletTarget,assemblyTarget, assemblyPos, lastAssemblyTarget, ringTarget, ringPos, lastRingTarget, itemTarget, itemPos, lastItemTarget, alphaTarget, alphaValue, lastAlphaTarget, yawModTarget, yawMod, lastYawModTarget, yaw, lastYaw;
	long interpTicks, interpLength = 1, age;
	
	public RestockingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.RESTOCKING_CHEST, blockPos, blockState);
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, RestockingChestBlockEntity chest) {
		chest.age++;
		// TODO: that should run in `clientTick() instead` (same for other chests)
		if (world.isClient) {

			chest.lastYaw = chest.yaw;
			chest.yaw += chest.yawMod;

			if (chest.isOpen) {
				if (chest.canFunction()) {
					chest.changeState(State.OPEN_CRAFTING);
					chest.interpLength = 5;
				}
				else {
					chest.changeState(State.OPEN);
					chest.interpLength = 7;
				}
			}
			else {
				if (chest.isFull) {
					chest.changeState(State.FULL);
					chest.interpLength = 9;
				}
				else if (chest.canFunction()) {
					chest.changeState(State.CLOSED_CRAFTING);
					chest.interpLength = 7;
				}
				else {
					chest.changeState(State.CLOSED);
					chest.interpLength = 13;
				}
			}

			if (chest.interpTicks < chest.interpLength) {
				chest.interpTicks++;
			}

			chest.lidAnimator.step();
		} else {
			if (tickCooldown(chest)) {
				for (int i = 0; i < 4; i++) {
					ItemStack outputItemStack = chest.inventory.get(RESULT_SLOTS[i]);
					ItemStack craftingTabletItemStack = chest.inventory.get(RECIPE_SLOTS[i]);
					if (!craftingTabletItemStack.isEmpty() && (outputItemStack.isEmpty() || outputItemStack.getCount() < outputItemStack.getMaxCount())) {
						boolean couldCraft = chest.tryCraft(chest, i);
						if (couldCraft) {
							chest.setCooldown(chest, 20);
							chest.markDirty();
							chest.updateFullState();
							return;
						}
					}
				}
				chest.updateFullState();
			}
		}
	}

	public void changeState(State state) {
		if (this.state != state) {
			this.state = state;
			lastRimTarget = rimPos;
			lastTabletTarget = tabletPos;
			lastAssemblyTarget = assemblyPos;
			lastRingTarget = ringPos;
			lastItemTarget = itemPos;
			lastAlphaTarget = alphaValue;
			lastYawModTarget = yawMod;
			interpTicks = 0;
		}
	}

	@Override
	public boolean onSyncedBlockEvent(int type, int data) {
		if (type == 1) {
			isOpen = data > 0;
		}
		return super.onSyncedBlockEvent(type, data);
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
	
	public List<ItemStack> getRecipeOutputs() {
		if (world.isClient()) {
			return cachedOutputs;
		}

		var list = new ArrayList<ItemStack>();

		for (int slot : RECIPE_SLOTS) {
			var tablet = inventory.get(slot);

			if (!tablet.isOf(SpectrumItems.CRAFTING_TABLET))
				continue;

			var recipe = CraftingTabletItem.getStoredRecipe(world, tablet);


			if (!isRecipeValid(recipe))
				continue;

			var output = recipe.getOutput(world.getRegistryManager());

			if (!output.isEmpty())
				list.add(output);
		}

		return list;
	}

	public long getRenderTime() {
		return age % 50000;
	}

	public boolean hasValidRecipes() {
		if (world.isClient()) {
			return hasValidRecipes;
		}

		for (int i = 0; i < 4; i++) {
			ItemStack tablet = inventory.get(RECIPE_SLOTS[i]);
			if (!tablet.isOf(SpectrumItems.CRAFTING_TABLET))
				continue;

			Recipe<?> recipe = CraftingTabletItem.getStoredRecipe(world, tablet);
			if (isRecipeValid(recipe) && isRecipeCraftable(recipe, i) && canSlotFitCraftingOutput(inventory.get(RESULT_SLOTS[i]), recipe))
				return true;
		}
		return false;
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
	
	private boolean tryCraft(RestockingChestBlockEntity chest, int index) {
		ItemStack craftingTabletItemStack = chest.inventory.get(RECIPE_SLOTS[index]);
		if (craftingTabletItemStack.isOf(SpectrumItems.CRAFTING_TABLET)) {
			Recipe<?> recipe = CraftingTabletItem.getStoredRecipe(world, craftingTabletItemStack);
			if (isRecipeValid(recipe)) {
				DefaultedList<Ingredient> ingredients = recipe.getIngredients();
				ItemStack outputItemStack = recipe.getOutput(world.getRegistryManager());
				ItemStack currentItemStack = chest.inventory.get(RESULT_SLOTS[index]);
				if (InventoryHelper.canCombineItemStacks(currentItemStack, outputItemStack) && InventoryHelper.hasInInventory(ingredients, chest)) {
					List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(ingredients, chest);
					
					if (currentItemStack.isEmpty()) {
						chest.inventory.set(RESULT_SLOTS[index], outputItemStack.copy());
					} else {
						currentItemStack.increment(outputItemStack.getCount());
					}
					
					for (ItemStack remainder : remainders) {
						InventoryHelper.smartAddToInventory(remainder, chest, null);
					}
					return true;
				}
				
			}
		}
		return false;
	}

	private static boolean isRecipeValid(Recipe<?> recipe) {
		return recipe instanceof ShapelessRecipe || recipe instanceof ShapedRecipe;
	}

	private boolean isRecipeCraftable(Recipe<?> recipe, int index) {
		var ingredients = recipe.getIngredients();

		if (!InventoryHelper.hasInInventory(ingredients, this))
			return false;

		var remainders = InventoryHelper.getRemainders(ingredients, this);

		return InventoryHelper.canFitStacks(remainders, this);
	}
	
	@Override
	public int size() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("cooldown", coolDownTicks);
		tag.putLong("age", age);
		if (world.isClient()) {
			tag.putFloat("yaw", yaw);
			tag.putFloat("lastYaw", lastYaw);
		}
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		if (tag.contains("cooldown")) {
			coolDownTicks = tag.getInt("cooldown");
		}
		if (tag.contains("age")) {
			age = tag.getLong("age");
		}
		if (tag.contains("yaw")) {
			yaw = tag.getFloat("yaw");
		}
		if (tag.contains("lastYaw")) {
			lastYaw = tag.getFloat("lastYaw");
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

	public boolean canFunction() {
		return !isFull && hasValidRecipes();
	}

	public void updateFullState() {
		if (!world.isClient()) {
			isFull = isFull();
			hasValidRecipes = hasValidRecipes();
			SpectrumS2CPacketSender.sendRestockingChestStatusUpdate(this);
		}
	}

	public boolean isFullServer() {
		return isFull;
	}

	public boolean isFull() {
		int invalids = 0;

		for (int i = 0; i < 4; i++) {
			ItemStack tablet = inventory.get(RECIPE_SLOTS[i]);

			if (!tablet.isOf(SpectrumItems.CRAFTING_TABLET))
				continue;

			Recipe<?> recipe = CraftingTabletItem.getStoredRecipe(world, tablet);

			if (!isRecipeValid(recipe)) {
				invalids++;
				continue;
			}

			ItemStack outputSlot = inventory.get(RESULT_SLOTS[i]);

			if (canSlotFitCraftingOutput(outputSlot, recipe)) {
				return false;
			}
		}
        return invalids != 4;
    }

	public boolean canSlotFitCraftingOutput(ItemStack slot, Recipe<?> recipe) {
        return slot.isEmpty() || slot.getCount() + recipe.getOutput(world.getRegistryManager()).getCount() < slot.getMaxCount();
    }

	public void updateState(boolean full, boolean hasValidRecipes, List<ItemStack> cachedOutputs) {
		this.isFull = full;
		this.hasValidRecipes = hasValidRecipes;
		this.cachedOutputs = cachedOutputs;
	}

	public State getState() {
		return state;
	}

	public enum State {
		OPEN,
		OPEN_CRAFTING,
		CLOSED_CRAFTING,
		CLOSED,
		FULL
	}
}
