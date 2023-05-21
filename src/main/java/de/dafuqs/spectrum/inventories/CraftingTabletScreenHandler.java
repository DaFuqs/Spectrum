package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CraftingTabletScreenHandler extends AbstractRecipeScreenHandler<Inventory> {
	
	private final CraftingTabletInventory craftingInventory;
	private final CraftingResultInventory craftingResultInventory;
	private final ScreenHandlerContext context;
	private final PlayerEntity player;
	private final World world;
	private final ItemStack craftingTabletItemStack;
	private final Optional<PedestalRecipeTier> highestUnlockedRecipeTier;
	
	private final LockableCraftingResultSlot lockableCraftingResultSlot;
	
	public CraftingTabletScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
	}
	
	public CraftingTabletScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ItemStack craftingTabletItemStack) {
		super(SpectrumScreenHandlerTypes.CRAFTING_TABLET, syncId);
		this.craftingInventory = new CraftingTabletInventory(this);
		this.craftingResultInventory = new CraftingResultInventory();
		this.context = context;
		this.world = playerInventory.player.getEntityWorld();
		this.craftingTabletItemStack = craftingTabletItemStack;
		this.player = playerInventory.player;
		
		this.highestUnlockedRecipeTier = PedestalRecipeTier.getHighestUnlockedRecipeTier(playerInventory.player);
		
		// crafting slots
		int m;
		int n;
		for (m = 0; m < 3; ++m) {
			for (n = 0; n < 3; ++n) {
				this.addSlot(new Slot(craftingInventory, n + m * 3, 30 + n * 18, 19 + m * 18));
			}
		}
		
		// gemstone powder slots
		if (highestUnlockedRecipeTier.isPresent()) {
			switch (highestUnlockedRecipeTier.get()) {
				case COMPLEX -> {
					this.addSlot(new ReadOnlySlot(craftingInventory, 9, 44, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 10, 44 + 18, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 11, 44 + 2 * 18, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 12, 44 + 3 * 18, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 13, 44 + 4 * 18, 77));
				}
				case ADVANCED -> {
					this.addSlot(new ReadOnlySlot(craftingInventory, 9, 44 + 9, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 10, 44 + 18 + 9, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 11, 44 + 2 * 18 + 9, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 12, 44 + 3 * 18 + 9, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 13, -2000, 77));
				}
				default -> {
					this.addSlot(new ReadOnlySlot(craftingInventory, 9, 44 + 18, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 10, 44 + 18 + 18, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 11, 44 + 2 * 18 + 18, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 12, -2000, 77));
					this.addSlot(new ReadOnlySlot(craftingInventory, 13, -2000, 77));
				}
			}
		} else {
			this.addSlot(new ReadOnlySlot(craftingInventory, 9, 44 + 18, 77));
			this.addSlot(new ReadOnlySlot(craftingInventory, 10, 44 + 18 + 18, 77));
			this.addSlot(new ReadOnlySlot(craftingInventory, 11, 44 + 2 * 18 + 18, 77));
			this.addSlot(new ReadOnlySlot(craftingInventory, 12, -2000, 77));
			this.addSlot(new ReadOnlySlot(craftingInventory, 13, -2000, 77));
		}
		
		// preview slot
		lockableCraftingResultSlot = new LockableCraftingResultSlot(craftingResultInventory, 0, 127, 37, playerInventory.player, craftingInventory, 0, 8);
		this.addSlot(lockableCraftingResultSlot);
		
		// player inventory
		int l;
		for (l = 0; l < 3; ++l) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, 112 + l * 18));
			}
		}
		
		// player hotbar
		for (l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 170));
		}
		
	}
	
	protected void updateResult(ScreenHandler handler, @NotNull World world, PlayerEntity player, CraftingTabletInventory inventory) {
		if (!world.isClient) {
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			
			inventory.setStack(9, new ItemStack(SpectrumItems.TOPAZ_POWDER, 64));
			inventory.setStack(10, new ItemStack(SpectrumItems.AMETHYST_POWDER, 64));
			inventory.setStack(11, new ItemStack(SpectrumItems.CITRINE_POWDER, 64));
			inventory.setStack(12, new ItemStack(SpectrumItems.ONYX_POWDER, 64));
			inventory.setStack(13, new ItemStack(SpectrumItems.MOONSTONE_POWDER, 64));
			Optional<PedestalCraftingRecipe> optionalPedestalCraftingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.PEDESTAL, inventory, world);
			if (optionalPedestalCraftingRecipe.isPresent()) {
				lockableCraftingResultSlot.lock();
				
				PedestalCraftingRecipe pedestalCraftingRecipe = optionalPedestalCraftingRecipe.get();
				ItemStack itemStack = pedestalCraftingRecipe.getOutput().copy();
				craftingResultInventory.setStack(0, itemStack);
				
				int magenta = pedestalCraftingRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.CYAN);
				if (magenta > 0) {
					inventory.setStack(9, new ItemStack(SpectrumItems.TOPAZ_POWDER, magenta));
				} else {
					inventory.setStack(9, ItemStack.EMPTY);
				}
				int yellow = pedestalCraftingRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.MAGENTA);
				if (yellow > 0) {
					inventory.setStack(10, new ItemStack(SpectrumItems.AMETHYST_POWDER, yellow));
				} else {
					inventory.setStack(10, ItemStack.EMPTY);
				}
				int cyan = pedestalCraftingRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.YELLOW);
				if (cyan > 0) {
					inventory.setStack(11, new ItemStack(SpectrumItems.CITRINE_POWDER, cyan));
				} else {
					inventory.setStack(11, ItemStack.EMPTY);
				}
				int black = pedestalCraftingRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.BLACK);
				if (black > 0) {
					inventory.setStack(12, new ItemStack(SpectrumItems.ONYX_POWDER, black));
				} else {
					inventory.setStack(12, ItemStack.EMPTY);
				}
				int white = pedestalCraftingRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.WHITE);
				if (white > 0) {
					inventory.setStack(13, new ItemStack(SpectrumItems.MOONSTONE_POWDER, white));
				} else {
					inventory.setStack(13, ItemStack.EMPTY);
				}
				
				handler.setPreviousTrackedSlot(0, itemStack);
				serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 14, itemStack));
				
				CraftingTabletItem.setStoredRecipe(craftingTabletItemStack, optionalPedestalCraftingRecipe.get());
			} else {
				inventory.setStack(9, ItemStack.EMPTY);
				inventory.setStack(10, ItemStack.EMPTY);
				inventory.setStack(11, ItemStack.EMPTY);
				inventory.setStack(12, ItemStack.EMPTY);
				inventory.setStack(13, ItemStack.EMPTY);
				
				ItemStack itemStack = ItemStack.EMPTY;
				Optional<CraftingRecipe> optionalCraftingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
				if (optionalCraftingRecipe.isPresent()) {
					lockableCraftingResultSlot.unlock();
					
					CraftingRecipe craftingRecipe = optionalCraftingRecipe.get();
					if (craftingResultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
						itemStack = craftingRecipe.craft(craftingInventory);
					}
					
					CraftingTabletItem.setStoredRecipe(craftingTabletItemStack, optionalCraftingRecipe.get());
				} else {
					CraftingTabletItem.clearStoredRecipe(craftingTabletItemStack);
				}
				
				craftingResultInventory.setStack(0, itemStack);
				handler.setPreviousTrackedSlot(0, itemStack);
				serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 14, itemStack));
			}
		}
	}
	
	@Override
	public void onContentChanged(Inventory inventory) {
		this.context.run((world, pos) -> updateResult(this, world, this.player, this.craftingInventory));
	}
	
	@Override
	public void populateRecipeFinder(RecipeMatcher recipeMatcher) {
		if (this.craftingInventory != null) {
			this.craftingInventory.provideRecipeInputs(recipeMatcher);
		}
	}
	
	@Override
	public void clearCraftingSlots() {
		this.craftingInventory.clear();
	}
	
	@Override
	public boolean matches(Recipe<? super Inventory> recipe) {
		return recipe.matches(this.craftingInventory, this.world);
	}
	
	@Override
	public void close(PlayerEntity playerEntity) {
		// put all items in the crafting grid back into the players inventory
		for (int i = 0; i < 9; i++) {
			ItemStack itemStack = this.craftingInventory.getStack(i);
			
			if (!itemStack.isEmpty()) {
				boolean insertInventorySuccess = playerEntity.getInventory().insertStack(itemStack);
				ItemEntity itemEntity;
				if (insertInventorySuccess && itemStack.isEmpty()) {
					itemStack.setCount(1);
					itemEntity = playerEntity.dropItem(itemStack, false);
					if (itemEntity != null) {
						itemEntity.setDespawnImmediately();
					}
					
					playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
					playerEntity.currentScreenHandler.sendContentUpdates();
				} else {
					itemEntity = playerEntity.dropItem(itemStack, false);
					if (itemEntity != null) {
						itemEntity.resetPickupDelay();
						itemEntity.setOwner(playerEntity.getUuid());
					}
				}
			}
		}
		super.close(player);
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		/*
			SLOTS:
			0-8: Crafting Input
			9-13: Readonly Gemstone Powder
			14: Crafting Output
			15-50: player inventory
				42-50: player inv 0++ (hotbar)
				15-41: player inv 9-35 (inventory)
		 */
		ItemStack transferStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack clickedSlotStack = slot.getStack();
			transferStack = clickedSlotStack.copy();
			if (index == 14) {
				// crafting result slot
				this.context.run((world, pos) -> clickedSlotStack.getItem().onCraft(clickedSlotStack, world, player));
				
				if (!this.insertItem(clickedSlotStack, 42, 51, false)) {
					if (!this.insertItem(clickedSlotStack, 15, 42, false)) {
						return ItemStack.EMPTY;
					}
				}
				
				slot.onQuickTransfer(clickedSlotStack, transferStack);
			} else if (index < 9) {
				// crafting grid
				if (!this.insertItem(clickedSlotStack, 42, 51, false)) {
					if (!this.insertItem(clickedSlotStack, 15, 42, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (index < 14) {
				// gemstone slots
				return ItemStack.EMPTY;
			} else if (!this.insertItem(clickedSlotStack, 0, 9, false)) {
				// player inventory
				return ItemStack.EMPTY;
			}
			
			if (clickedSlotStack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			
			if (clickedSlotStack.getCount() == transferStack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTakeItem(player, clickedSlotStack);
			if (index == 14) {
				player.dropItem(clickedSlotStack, false);
			}
		}
		
		return transferStack;
	}
	
	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return super.canInsertIntoSlot(stack, slot);
	}
	
	@Override
	public int getCraftingResultSlotIndex() {
		return 14;
	}
	
	@Override
	public int getCraftingWidth() {
		return 3;
	}
	
	@Override
	public int getCraftingHeight() {
		return 3;
	}
	
	@Override
	public int getCraftingSlotCount() {
		return 9;
	}
	
	@Override
	public RecipeBookCategory getCategory() {
		return RecipeBookCategory.CRAFTING;
	}
	
	@Override
	public boolean canInsertIntoSlot(int index) {
		return index != this.getCraftingResultSlotIndex();
	}
	
	public Optional<PedestalRecipeTier> getTier() {
		return this.highestUnlockedRecipeTier;
	}
	
}
