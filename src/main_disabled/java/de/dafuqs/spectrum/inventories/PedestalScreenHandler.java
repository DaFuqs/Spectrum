package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import io.netty.buffer.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class PedestalScreenHandler extends RecipeBookMenu<RecipeInput, Recipe<RecipeInput>> {
	
	public record ScreenOpeningData(BlockPos pos, PedestalRecipeTier pedestalRecipeTier, PedestalRecipeTier maxRecipeTier) {
		public static final StreamCodec<ByteBuf, ScreenOpeningData> PACKET_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, ScreenOpeningData::pos,
				PedestalRecipeTier.PACKET_CODEC, ScreenOpeningData::pedestalRecipeTier,
				PedestalRecipeTier.PACKET_CODEC, ScreenOpeningData::maxRecipeTier,
				PedestalScreenHandler.ScreenOpeningData::new
		);
	}
	
	protected final Level world;
	private final PedestalBlockEntity blockEntity;
	private final ContainerData propertyDelegate;
	private final RecipeBookType category;
	
	private final PedestalRecipeTier pedestalRecipeTier;
	private final PedestalRecipeTier maxPedestalRecipeTier;
	
	// clientside
	public PedestalScreenHandler(int syncId, Inventory playerInventory, ScreenOpeningData screenOpeningData) {
		this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(screenOpeningData.pos, SpectrumBlockEntities.PEDESTAL).orElseThrow(), new SimpleContainerData(2), screenOpeningData.pedestalRecipeTier, screenOpeningData.maxRecipeTier);
	}
	
	// serverside
	public PedestalScreenHandler(int syncId, Inventory playerInventory, PedestalBlockEntity blockEntity, ContainerData propertyDelegate, PedestalRecipeTier pedestalRecipeTier, PedestalRecipeTier maxRecipeTier) {
		this(SpectrumScreenHandlerTypes.PEDESTAL, RecipeBookType.CRAFTING, syncId, playerInventory, blockEntity, propertyDelegate, pedestalRecipeTier, maxRecipeTier);
	}
	
	protected PedestalScreenHandler(MenuType<?> type, RecipeBookType recipeBookCategory, int i, Inventory playerInventory, PedestalBlockEntity blockEntity, ContainerData propertyDelegate, PedestalRecipeTier pedestalRecipeTier, PedestalRecipeTier maxRecipeTier) {
		super(type, i);
		this.category = recipeBookCategory;
		this.propertyDelegate = propertyDelegate;
		this.world = playerInventory.player.level();
		
		this.blockEntity = blockEntity;
		this.pedestalRecipeTier = pedestalRecipeTier;
		this.maxPedestalRecipeTier = maxRecipeTier;
		
		checkContainerSize(blockEntity, PedestalBlockEntity.INVENTORY_SIZE);
		checkContainerDataCount(propertyDelegate, 2);
		blockEntity.startOpen(playerInventory.player);
		
		// crafting slots
		for (int m = 0; m < 3; ++m) {
			for (int n = 0; n < 3; ++n) {
				addSlot(new Slot(blockEntity, n + m * 3, 30 + n * 18, 19 + m * 18));
			}
		}
		
		// gemstone powder slots
		switch (getPedestalRecipeTier()) {
			case BASIC, SIMPLE -> {
				this.addSlot(new StackFilterSlot(blockEntity, 9, 44 + 18, 77, SpectrumItems.TOPAZ_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 10, 44 + 2 * 18, 77, SpectrumItems.AMETHYST_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 11, 44 + 3 * 18, 77, SpectrumItems.CITRINE_POWDER));
				this.addSlot(new DisabledSlot(blockEntity, 12, -2000, 77));
				this.addSlot(new DisabledSlot(blockEntity, 13, -2000, 77));
			}
			case ADVANCED -> {
				this.addSlot(new StackFilterSlot(blockEntity, 9, 35 + 18, 77, SpectrumItems.TOPAZ_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 10, 35 + 2 * 18, 77, SpectrumItems.AMETHYST_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 11, 35 + 3 * 18, 77, SpectrumItems.CITRINE_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 12, 35 + 4 * 18, 77, SpectrumItems.ONYX_POWDER));
				this.addSlot(new DisabledSlot(blockEntity, 13, -2000, 77));
			}
			case COMPLEX -> {
				this.addSlot(new StackFilterSlot(blockEntity, 9, 44, 77, SpectrumItems.TOPAZ_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 10, 44 + 18, 77, SpectrumItems.AMETHYST_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 11, 44 + 2 * 18, 77, SpectrumItems.CITRINE_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 12, 44 + 3 * 18, 77, SpectrumItems.ONYX_POWDER));
				this.addSlot(new StackFilterSlot(blockEntity, 13, 44 + 4 * 18, 77, SpectrumItems.MOONSTONE_POWDER));
			}
		}
		
		// crafting tablet slot
		this.addSlot(new StackFilterSlot(blockEntity, PedestalBlockEntity.CRAFTING_TABLET_SLOT_ID, 93, 19, SpectrumItems.CRAFTING_TABLET));
		
		// preview slot
		this.addSlot(new PedestalPreviewSlot(blockEntity, 15, 127, 37));
		
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
		
		this.addDataSlots(propertyDelegate);
	}
	
	@Override
	public void fillCraftSlotsStackedContents(StackedContents recipeMatcher) {
		this.blockEntity.fillStackedContents(recipeMatcher);
	}
	
	@Override
	public void clearCraftingContent() {
		for (int i = 0; i < 9; i++) {
			this.getSlot(i).setByPlayer(ItemStack.EMPTY);
		}
	}
	
	@Override
	public boolean recipeMatches(RecipeHolder<Recipe<RecipeInput>> recipe) {
		return blockEntity != null && recipe.value().matches(blockEntity.createRecipeInput(), world);
	}
	
	@Override
	public int getResultSlotIndex() {
		return 16;
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
	public boolean stillValid(Player player) {
		return blockEntity.stillValid(player);
	}
	
	@Environment(EnvType.CLIENT)
	public int getCraftingProgress() {
		int craftingTime = getCraftingTime();
		int craftingTimeTotal = getCraftingTimeTotal();
		return craftingTimeTotal != 0 && craftingTime != 0 ? craftingTime * 24 / craftingTimeTotal : 0;
	}
	
	public boolean isCrafting() {
		return getCraftingTime() > 0;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public RecipeBookType getRecipeBookType() {
		return this.category;
	}
	
	@Override
	public boolean shouldMoveToInventory(int index) {
		return index != 1;
	}
	
	// Shift-Clicking
	// 0-8: crafting slots
	// 9-13: powder slots
	// 14: crafting tablet
	// 15: preview slot
	// 16: hidden output slot
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack clickedStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		
		blockEntity.setInventoryChanged();
		
		if (slot.hasItem()) {
			ItemStack clickedStack = slot.getItem();
			clickedStackCopy = clickedStack.copy();
			
			if (index < 15) {
				// pedestal => player inv
				if (!this.moveItemStackTo(clickedStack, 16, 51, false)) {
					return ItemStack.EMPTY;
				}
			} else if (clickedStackCopy.is(SpectrumItems.TOPAZ_POWDER)) {
				if (!this.moveItemStackTo(clickedStack, 9, 10, false)) {
					return ItemStack.EMPTY;
				}
			} else if (clickedStackCopy.is(SpectrumItems.AMETHYST_POWDER)) {
				if (!this.moveItemStackTo(clickedStack, 10, 11, false)) {
					return ItemStack.EMPTY;
				}
			} else if (clickedStackCopy.is(SpectrumItems.CITRINE_POWDER)) {
				if (!this.moveItemStackTo(clickedStack, 11, 12, false)) {
					return ItemStack.EMPTY;
				}
			} else if (clickedStackCopy.is(SpectrumItems.ONYX_POWDER)) {
				if (!this.moveItemStackTo(clickedStack, 12, 13, false)) {
					return ItemStack.EMPTY;
				}
			} else if (clickedStackCopy.is(SpectrumItems.MOONSTONE_POWDER)) {
				if (!this.moveItemStackTo(clickedStack, 13, 14, false)) {
					return ItemStack.EMPTY;
				}
			} else if (clickedStackCopy.is(SpectrumItems.CRAFTING_TABLET)) {
				if (!this.moveItemStackTo(clickedStack, PedestalBlockEntity.CRAFTING_TABLET_SLOT_ID, PedestalBlockEntity.CRAFTING_TABLET_SLOT_ID + 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			// crafting grid
			if (!this.moveItemStackTo(clickedStack, 0, 9, false)) {
				return ItemStack.EMPTY;
			}
			
			if (clickedStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			
			if (clickedStack.getCount() == clickedStackCopy.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, clickedStack);
		}
		
		return clickedStackCopy;
	}
	
	public int getCraftingTime() {
		return propertyDelegate.get(0);
	}
	
	public int getCraftingTimeTotal() {
		return propertyDelegate.get(1);
	}
	
	public PedestalRecipeTier getPedestalRecipeTier() {
		return this.pedestalRecipeTier;
	}
	
	public PedestalRecipeTier getMaxPedestalRecipeTier() {
		return this.maxPedestalRecipeTier;
	}
	
	public PedestalBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public BlockPos getBlockPos() {
		return blockEntity.getBlockPos();
	}
	
	@Override
	public void removed(Player player) {
		super.removed(player);
		blockEntity.stopOpen(player);
	}
	
}
