package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.*;

public class FabricationChestBlockEntity extends SpectrumChestBlockEntity implements WorldlyContainer {
	
	public static final int INVENTORY_SIZE = 27 + 4 + 4; // 27 items, 4 crafting tablets, 4 result slots
	public static final int[] CHEST_SLOTS = IntStream.rangeClosed(0, 26).toArray();
	public static final int[] RECIPE_SLOTS = IntStream.rangeClosed(27, 30).toArray();
	public static final int[] RESULT_SLOTS = IntStream.rangeClosed(31, 34).toArray();
	private List<ItemStack> cachedOutputs = new ArrayList<>(4);
	private int coolDownTicks = 0;
	private boolean isOpen, isFull, hasValidRecipes;
	private State state = State.CLOSED;
	float rimTarget, rimPos, lastRimTarget, tabletTarget, tabletPos, lastTabletTarget, assemblyTarget, assemblyPos, lastAssemblyTarget, ringTarget, ringPos, lastRingTarget, itemTarget, itemPos, lastItemTarget, alphaTarget, alphaValue, lastAlphaTarget, yawModTarget, yawMod, lastYawModTarget, yaw, lastYaw;
	long interpTicks, interpLength = 1, age;
	
	public FabricationChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.FABRICATION_CHEST.get(), blockPos, blockState);
	}
	
	@SuppressWarnings("unused")
	public static void tick(Level world, BlockPos pos, BlockState state, FabricationChestBlockEntity chest) {
		chest.age++;
		// TODO: that should run in `clientTick() instead` (same for other chests)
        if (world.isClientSide()) {

            chest.lastYaw = chest.yaw;
            chest.yaw += chest.yawMod;

            if (chest.isOpen) {
                if (chest.canFunction()) {
                    chest.changeState(State.OPEN_CRAFTING);
                    chest.interpLength = 5;
                } else {
                    chest.changeState(State.OPEN);
                    chest.interpLength = 7;
                }
            } else {
                if (chest.isFull) {
                    chest.changeState(State.FULL);
                    chest.interpLength = 9;
                } else if (chest.canFunction()) {
                    chest.changeState(State.CLOSED_CRAFTING);
                    chest.interpLength = 7;
                } else {
                    chest.changeState(State.CLOSED);
                    chest.interpLength = 13;
                }
            }

            if (chest.interpTicks < chest.interpLength) {
                chest.interpTicks++;
            }

            chest.lidAnimator.tickLid();
        } else {
            if (tickCooldown(chest)) {
                for (int i = 0; i < 4; i++) {
                    ItemStack outputItemStack = chest.inventory.get(RESULT_SLOTS[i]);
                    ItemStack craftingTabletItemStack = chest.inventory.get(RECIPE_SLOTS[i]);
                    if (!craftingTabletItemStack.isEmpty() && (outputItemStack.isEmpty() || outputItemStack.getCount() < outputItemStack.getMaxStackSize())) {
                        boolean couldCraft = chest.tryCraft(chest, i);
                        if (couldCraft) {
                            chest.setCooldown(chest, 20);
                            chest.setChanged();
                            chest.updateFullState(false);
                            return;
                        }
                    }
                }
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
	public boolean triggerEvent(int type, int data) {
		if (type == 1) {
			isOpen = data > 0;
		}
		return super.triggerEvent(type, data);
	}
	
	private static boolean tickCooldown(FabricationChestBlockEntity fabricationChestBlockEntity) {
		fabricationChestBlockEntity.coolDownTicks--;
		if (fabricationChestBlockEntity.coolDownTicks > 0) {
			return false;
		} else {
			fabricationChestBlockEntity.coolDownTicks = 0;
		}
		return true;
	}
	
	public List<ItemStack> getRecipeOutputs() {
		if (level == null || level.isClientSide()) {
			return cachedOutputs;
		}
		
		List<ItemStack> list = new ArrayList<>();
		
		for (int slot : RECIPE_SLOTS) {
			ItemStack tablet = inventory.get(slot);
			
			if (!tablet.is(SpectrumItems.CRAFTING_TABLET))
				continue;

			RecipeHolder<?> recipeHolder = CraftingTabletItem.getStoredRecipe(level, tablet);
			if (recipeHolder == null)
				continue;

			Recipe<?> recipe = recipeHolder.value();
			if (!isRecipeValid(recipe))
				continue;
			
			ItemStack output = recipe.getResultItem(level.registryAccess());
			
			if (!output.isEmpty())
				list.add(output);
		}
		
		return list;
	}
	
	public long getRenderTime() {
		return age % 50000;
	}
	
	public boolean hasValidRecipes() {
		if (level == null || level.isClientSide()) {
			return hasValidRecipes;
		}
		
		for (int i = 0; i < 4; i++) {
			ItemStack tablet = inventory.get(RECIPE_SLOTS[i]);
			if (!tablet.is(SpectrumItems.CRAFTING_TABLET))
				continue;
			
			RecipeHolder<?> recipeHolder = CraftingTabletItem.getStoredRecipe(level, tablet);
			if (recipeHolder == null)
				continue;

			Recipe<?> recipe = recipeHolder.value();

			if (recipe != null && isRecipeValid(recipe) && isRecipeCraftable(recipe) && canSlotFitCraftingOutput(inventory.get(RESULT_SLOTS[i]), recipe))
				return true;
		}
		return false;
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.fabrication_chest");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new FabricationChestScreenHandler(syncId, playerInventory, this);
	}
	
	private void setCooldown(FabricationChestBlockEntity fabricationChestBlockEntity, int cooldownTicks) {
		fabricationChestBlockEntity.coolDownTicks = cooldownTicks;
	}
	
	private boolean tryCraft(FabricationChestBlockEntity chest, int index) {
		ItemStack craftingTabletItemStack = chest.inventory.get(RECIPE_SLOTS[index]);
		if (craftingTabletItemStack.is(SpectrumItems.CRAFTING_TABLET)) {
			var recipe = CraftingTabletItem.getStoredRecipe(level, craftingTabletItemStack);
			if (recipe != null && isRecipeValid(recipe.value())) {
				NonNullList<Ingredient> ingredients = recipe.value().getIngredients();
				ItemStack outputItemStack = recipe.value().getResultItem(level.registryAccess());
				ItemStack currentItemStack = chest.inventory.get(RESULT_SLOTS[index]);
				if (InventoryHelper.canCombineItemStacks(currentItemStack, outputItemStack) && InventoryHelper.hasInInventory(ingredients, chest)) {
					List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(ingredients, chest);
					
					if (currentItemStack.isEmpty()) {
						chest.inventory.set(RESULT_SLOTS[index], outputItemStack.copy());
					} else {
						currentItemStack.grow(outputItemStack.getCount());
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
	
	private boolean isRecipeCraftable(Recipe<?> recipe) {
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		
		if (!InventoryHelper.hasInInventory(ingredients, this))
			return false;
		
		List<ItemStack> remainders = InventoryHelper.getRemainders(ingredients);
		
		return InventoryHelper.canFitStacks(this.inventory, remainders);
	}
	
	@Override
	public int getContainerSize() {
		return INVENTORY_SIZE;
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		tag.putInt("cooldown", coolDownTicks);
		tag.putLong("age", age);
		if (level != null && level.isClientSide()) {
			tag.putFloat("yaw", yaw);
			tag.putFloat("lastYaw", lastYaw);
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
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
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN) {
			return RESULT_SLOTS;
		} else {
			return CHEST_SLOTS;
		}
	}
	
	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		return slot <= CHEST_SLOTS[CHEST_SLOTS.length - 1];
	}
	
	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
	public boolean canFunction() {
		return !isFull && hasValidRecipes();
	}
	
	public void updateFullState(boolean force) {
		if (level != null && !level.isClientSide()) {
			var wasFull = isFull;
			isFull = isFull();
			var hadValidRecipes = hasValidRecipes;
			hasValidRecipes = hasValidRecipes();
			if (force || wasFull != isFull || hadValidRecipes != hasValidRecipes) {
				FabricationChestStatusUpdatePayload.sendFabricationChestStatusUpdate(this);
			}
		}
	}
	
	public boolean isFullServer() {
		return isFull;
	}
	
	public boolean isFull() {
		int invalids = 0;
		
		for (int i = 0; i < 4; i++) {
			ItemStack tablet = inventory.get(RECIPE_SLOTS[i]);
			
			if (!tablet.is(SpectrumItems.CRAFTING_TABLET))
				continue;
			
			var recipe = CraftingTabletItem.getStoredRecipe(level, tablet);
			
			if (recipe == null || !isRecipeValid(recipe.value())) {
				invalids++;
				continue;
			}
			
			ItemStack outputSlot = inventory.get(RESULT_SLOTS[i]);
			
			if (canSlotFitCraftingOutput(outputSlot, recipe.value())) {
				return false;
			}
		}
		return invalids != 4;
	}
	
	public boolean canSlotFitCraftingOutput(ItemStack slot, Recipe<?> recipe) {
		if (level == null)
			return false;
		return slot.isEmpty() || slot.getCount() + recipe.getResultItem(level.registryAccess()).getCount() < slot.getMaxStackSize();
	}
	
	@Override
	public void setChanged() {
		updateFullState(false);
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
