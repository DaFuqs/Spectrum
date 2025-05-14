package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CraftingTabletScreenHandler extends RecipeBookMenu<RecipeInput, Recipe<RecipeInput>> {
	
	private final CraftingTabletInventory craftingInventory;
	private final ResultContainer craftingResultInventory;
	private final ContainerLevelAccess context;
	private final Player player;
	private final Level world;
	private final ItemStack craftingTabletItemStack;
	private final Optional<PedestalRecipeTier> highestUnlockedRecipeTier;
	
	private final LockableCraftingResultSlot lockableCraftingResultSlot;
	
	public CraftingTabletScreenHandler(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, ContainerLevelAccess.NULL, null);
	}
	
	public CraftingTabletScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context, ItemStack craftingTabletItemStack) {
		super(SpectrumScreenHandlerTypes.CRAFTING_TABLET, syncId);
		this.craftingInventory = new CraftingTabletInventory(this);
		this.craftingResultInventory = new ResultContainer();
		this.context = context;
		this.world = playerInventory.player.getCommandSenderWorld();
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
		lockableCraftingResultSlot = new LockableCraftingResultSlot(craftingResultInventory, 0, 127, 37, playerInventory.player, craftingInventory);
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
	
	protected void updateResult(AbstractContainerMenu handler, @NotNull Level world, Player player, CraftingTabletInventory inventory) {
		if (!world.isClientSide) {
			ServerPlayer serverPlayerEntity = (ServerPlayer) player;
			
			PedestalRecipeInput pedestalRecipeInput = PedestalRecipeInput.createWithFullGemstonePowder(inventory.getItems(), player);
			
			Optional<RecipeHolder<PedestalRecipe>> optionalPedestalCraftingRecipe = world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.PEDESTAL, pedestalRecipeInput, world);
			if (optionalPedestalCraftingRecipe.isPresent()) {
				lockableCraftingResultSlot.lock();
				
				PedestalRecipe pedestalRecipe = optionalPedestalCraftingRecipe.get().value();
				ItemStack itemStack = pedestalRecipe.getResultItem(world.registryAccess()).copy();
				craftingResultInventory.setItem(0, itemStack);
				
				int magenta = pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.CYAN);
				if (magenta > 0) {
					inventory.setItem(9, new ItemStack(SpectrumItems.TOPAZ_POWDER, magenta));
				} else {
					inventory.setItem(9, ItemStack.EMPTY);
				}
				int yellow = pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.MAGENTA);
				if (yellow > 0) {
					inventory.setItem(10, new ItemStack(SpectrumItems.AMETHYST_POWDER, yellow));
				} else {
					inventory.setItem(10, ItemStack.EMPTY);
				}
				int cyan = pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.YELLOW);
				if (cyan > 0) {
					inventory.setItem(11, new ItemStack(SpectrumItems.CITRINE_POWDER, cyan));
				} else {
					inventory.setItem(11, ItemStack.EMPTY);
				}
				int black = pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.BLACK);
				if (black > 0) {
					inventory.setItem(12, new ItemStack(SpectrumItems.ONYX_POWDER, black));
				} else {
					inventory.setItem(12, ItemStack.EMPTY);
				}
				int white = pedestalRecipe.getGemstonePowderAmount(BuiltinGemstoneColor.WHITE);
				if (white > 0) {
					inventory.setItem(13, new ItemStack(SpectrumItems.MOONSTONE_POWDER, white));
				} else {
					inventory.setItem(13, ItemStack.EMPTY);
				}
				
				handler.setRemoteSlot(0, itemStack);
				serverPlayerEntity.connection.send(new ClientboundContainerSetSlotPacket(handler.containerId, handler.incrementStateId(), 14, itemStack));
				
				CraftingTabletItem.setStoredRecipe(craftingTabletItemStack, optionalPedestalCraftingRecipe.get());
			} else {
				inventory.setItem(9, ItemStack.EMPTY);
				inventory.setItem(10, ItemStack.EMPTY);
				inventory.setItem(11, ItemStack.EMPTY);
				inventory.setItem(12, ItemStack.EMPTY);
				inventory.setItem(13, ItemStack.EMPTY);
				
				ItemStack itemStack = ItemStack.EMPTY;
				Optional<RecipeHolder<CraftingRecipe>> optionalCraftingRecipe = world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, pedestalRecipeInput.getCraftingGridInput(), world);
				if (optionalCraftingRecipe.isPresent()) {
					lockableCraftingResultSlot.unlock();
					
					if (craftingResultInventory.setRecipeUsed(world, serverPlayerEntity, optionalCraftingRecipe.get())) {
						itemStack = optionalCraftingRecipe.get().value().assemble(pedestalRecipeInput.getCraftingGridInput(), world.registryAccess());
					}
					
					CraftingTabletItem.setStoredRecipe(craftingTabletItemStack, optionalCraftingRecipe.get());
				} else {
					CraftingTabletItem.clearStoredRecipe(craftingTabletItemStack);
				}
				
				craftingResultInventory.setItem(0, itemStack);
				handler.setRemoteSlot(0, itemStack);
				serverPlayerEntity.connection.send(new ClientboundContainerSetSlotPacket(handler.containerId, handler.incrementStateId(), 14, itemStack));
			}
		}
	}
	
	@Override
	public void slotsChanged(Container inventory) {
		this.context.execute((world, pos) -> updateResult(this, world, this.player, this.craftingInventory));
	}
	
	@Override
	public void fillCraftSlotsStackedContents(StackedContents recipeMatcher) {
		if (this.craftingInventory != null) {
			this.craftingInventory.fillStackedContents(recipeMatcher);
		}
	}
	
	@Override
	public void clearCraftingContent() {
		this.craftingInventory.clearContent();
	}
	
	@Override
	public boolean recipeMatches(RecipeHolder recipe) {
		PedestalRecipeInput pedestalRecipeInput = PedestalRecipeInput.createWithFullGemstonePowder(this.craftingInventory.getItems(), player);
		if (recipe.value() instanceof PedestalRecipe pedestalRecipe)
			return pedestalRecipe.matches(pedestalRecipeInput, this.world);
		if (recipe.value() instanceof CraftingRecipe craftingRecipe)
			return craftingRecipe.matches(pedestalRecipeInput.getCraftingGridInput(), this.world);
		return false;
	}
	
	@Override
	public void removed(Player playerEntity) {
		// put all items in the crafting grid back into the player inventory
		for (int i = 0; i < 9; i++) {
			ItemStack itemStack = this.craftingInventory.getItem(i);
			
			if (!itemStack.isEmpty()) {
				boolean insertInventorySuccess = playerEntity.getInventory().add(itemStack);
				ItemEntity itemEntity;
				if (insertInventorySuccess && itemStack.isEmpty()) {
					itemStack.setCount(1);
					itemEntity = playerEntity.drop(itemStack, false);
					if (itemEntity != null) {
						itemEntity.makeFakeItem();
					}
					
					playerEntity.level().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
					playerEntity.containerMenu.broadcastChanges();
				} else {
					itemEntity = playerEntity.drop(itemStack, false);
					if (itemEntity != null) {
						itemEntity.setNoPickUpDelay();
						itemEntity.setTarget(playerEntity.getUUID());
					}
				}
			}
		}
		super.removed(player);
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
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
		if (slot.hasItem()) {
			ItemStack clickedSlotStack = slot.getItem();
			transferStack = clickedSlotStack.copy();
			if (index == 14) {
				// crafting result slot
				this.context.execute((world, pos) -> clickedSlotStack.getItem().onCraftedBy(clickedSlotStack, world, player));
				
				if (!this.moveItemStackTo(clickedSlotStack, 42, 51, false)) {
					if (!this.moveItemStackTo(clickedSlotStack, 15, 42, false)) {
						return ItemStack.EMPTY;
					}
				}
				
				slot.onQuickCraft(clickedSlotStack, transferStack);
			} else if (index < 9) {
				// crafting grid
				if (!this.moveItemStackTo(clickedSlotStack, 42, 51, false)) {
					if (!this.moveItemStackTo(clickedSlotStack, 15, 42, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (index < 14) {
				// gemstone slots
				return ItemStack.EMPTY;
			} else if (!this.moveItemStackTo(clickedSlotStack, 0, 9, false)) {
				// player inventory
				return ItemStack.EMPTY;
			}
			
			if (clickedSlotStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			
			if (clickedSlotStack.getCount() == transferStack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, clickedSlotStack);
			if (index == 14) {
				player.drop(clickedSlotStack, false);
			}
		}
		
		return transferStack;
	}
	
	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return super.canTakeItemForPickAll(stack, slot);
	}
	
	@Override
	public int getResultSlotIndex() {
		return 14;
	}
	
	@Override
	public int getGridWidth() {
		return 3;
	}
	
	@Override
	public int getGridHeight() {
		return 3;
	}
	
	@Override
	public int getSize() {
		return 9;
	}
	
	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}
	
	@Override
	public boolean shouldMoveToInventory(int index) {
		return index != this.getResultSlotIndex();
	}
	
	public Optional<PedestalRecipeTier> getTier() {
		return this.highestUnlockedRecipeTier;
	}
	
}
