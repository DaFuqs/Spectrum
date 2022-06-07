package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.inventories.AutoCompactingInventory;
import de.dafuqs.spectrum.inventories.CompactingChestScreenHandler;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class CompactingChestBlockEntity extends SpectrumChestBlockEntity implements ExtendedScreenHandlerFactory {
	
	private static Map<AutoCompactingInventory.AutoCraftingMode, Map<ItemVariant, Optional<CraftingRecipe>>> cache = new EnumMap<>(AutoCompactingInventory.AutoCraftingMode.class);
	AutoCompactingInventory autoCompactingInventory = new AutoCompactingInventory();
	AutoCompactingInventory.AutoCraftingMode autoCraftingMode;
	CraftingRecipe lastCraftingRecipe; // cache
	ItemVariant lastItemVariant; // cache
	boolean hasToCraft;
	
	public CompactingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.COMPACTING_CHEST, blockPos, blockState);
		this.autoCraftingMode = AutoCompactingInventory.AutoCraftingMode.ThreeXThree;
		this.lastItemVariant = null;
		this.lastCraftingRecipe = null;
		this.hasToCraft = false;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, CompactingChestBlockEntity compactingChestBlockEntity) {
		if (world.isClient) {
			compactingChestBlockEntity.lidAnimator.step();
		} else {
			if (compactingChestBlockEntity.hasToCraft) {
				boolean couldCraft = compactingChestBlockEntity.tryCraftOnce();
				if (!couldCraft) {
					compactingChestBlockEntity.hasToCraft = false;
				}
			}
		}
	}
	
	private static boolean smartAddToInventory(List<ItemStack> itemStacks, List<ItemStack> inventory, boolean test) {
		List<ItemStack> additionStacks = new ArrayList<>();
		for (ItemStack itemStack : itemStacks) {
			additionStacks.add(itemStack.copy());
		}
		
		boolean tryStackExisting = true;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack currentStack = inventory.get(i);
			for (ItemStack additionStack : additionStacks) {
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
					if (doneStuff) {
						boolean allEmpty = true;
						for (ItemStack itemStack : additionStacks) {
							if (itemStack.getCount() > 0) {
								allEmpty = false;
								break;
							}
						}
						if (allEmpty) {
							return true;
						}
					}
				}
			}
			
			if (tryStackExisting && !test && i == inventory.size() - 1) {
				tryStackExisting = false;
				i = -1;
			}
		}
		return false;
	}
	
	public static void clearCache() {
		cache.clear();
	}
	
	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.compacting_chest");
	}
	
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		if (tag.contains("AutoCraftingMode", NbtElement.INT_TYPE)) {
			int autoCraftingModeInt = tag.getInt("AutoCraftingMode");
			this.autoCraftingMode = AutoCompactingInventory.AutoCraftingMode.values()[autoCraftingModeInt];
		}
	}
	
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("AutoCraftingMode", this.autoCraftingMode.ordinal());
	}
	
	@Override
	public int size() {
		return 27;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		super.setStack(slot, stack);
		this.hasToCraft = true;
	}
	
	public void inventoryChanged() {
		this.hasToCraft = true;
	}
	
	private boolean tryCraftOnce() {
		Optional<CraftingRecipe> optionalCraftingRecipe = Optional.empty();
		DefaultedList<ItemStack> inventory = this.getInvStackList();
		
		// try last recipe
		if (lastCraftingRecipe != null) {
			int requiredItemCount = this.autoCraftingMode.getItemCount();
			if (InventoryHelper.isItemCountInInventory(inventory, lastItemVariant, requiredItemCount)) {
				optionalCraftingRecipe = Optional.ofNullable(lastCraftingRecipe);
			} else {
				lastCraftingRecipe = null;
				lastItemVariant = null;
			}
		}
		// search for other recipes
		if (optionalCraftingRecipe.isEmpty()) {
			optionalCraftingRecipe = searchRecipeToCraft();
		}
		
		if (optionalCraftingRecipe.isPresent() && this.lastItemVariant != null) {
			if (tryCraftInInventory(inventory, optionalCraftingRecipe.get(), this.lastItemVariant)) {
				this.lastCraftingRecipe = optionalCraftingRecipe.get();
				return true;
			}
		}
		return false;
	}
	
	public Optional<CraftingRecipe> searchRecipeToCraft() {
		for (ItemStack itemStack : inventory) {
			if (itemStack.isEmpty()) {
				continue;
			}
			
			int requiredItemCount = this.autoCraftingMode.getItemCount();
			Pair<Integer, List<ItemStack>> stackPair = InventoryHelper.getStackCountInInventory(itemStack, inventory, requiredItemCount);
			if (stackPair.getLeft() >= requiredItemCount) {
				Map<ItemVariant, Optional<CraftingRecipe>> currentCache = cache.computeIfAbsent(autoCraftingMode, mode -> new HashMap<>());
				ItemVariant itemKey = ItemVariant.of(itemStack);
				
				Optional<CraftingRecipe> recipe = currentCache.get(itemKey);
				if (recipe != null) {
					if (recipe.isEmpty()) {
						continue;
					}
					this.lastItemVariant = itemKey;
					return recipe;
				}
				
				autoCompactingInventory.setCompacting(autoCraftingMode, itemKey.toStack());
				Optional<CraftingRecipe> optionalCraftingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, autoCompactingInventory, world);
				if (optionalCraftingRecipe.isEmpty() || optionalCraftingRecipe.get().getOutput().isEmpty()) {
					optionalCraftingRecipe = Optional.empty();
					currentCache.put(itemKey, optionalCraftingRecipe);
				} else {
					currentCache.put(itemKey, optionalCraftingRecipe);
					
					this.lastItemVariant = itemKey;
					return optionalCraftingRecipe;
				}
			}
		}
		
		return Optional.empty();
	}
	
	public boolean tryCraftInInventory(DefaultedList<ItemStack> inventory, CraftingRecipe craftingRecipe, ItemVariant itemVariant) {
		ItemStack inputStack = itemVariant.toStack(this.autoCraftingMode.getItemCount());
		InventoryHelper.removeFromInventory(inputStack, inventory);
		
		boolean spaceInInventory;
		
		List<ItemStack> additionItemStacks = new ArrayList<>();
		additionItemStacks.add(craftingRecipe.getOutput());
		
		// room for output?
		ItemStack remainderStack;
		Item recipeRemainderItem = itemVariant.getItem().getRecipeRemainder();
		if (recipeRemainderItem != null) {
			remainderStack = recipeRemainderItem.getDefaultStack();
			remainderStack.setCount(this.autoCraftingMode.getItemCount());
			additionItemStacks.add(remainderStack);
		}
		
		spaceInInventory = smartAddToInventory(additionItemStacks, inventory, true);
		if (spaceInInventory) {
			// craft
			smartAddToInventory(additionItemStacks, inventory, false);
			this.setInvStackList(inventory);
			
			// cache
			return true;
		} else {
			smartAddToInventory(List.of(inputStack), inventory, false);
			return false;
		}
	}
	
	@Override
	public SoundEvent getOpenSound() {
		return SoundEvents.BLOCK_PISTON_EXTEND;
	}
	
	@Override
	public SoundEvent getCloseSound() {
		return SoundEvents.BLOCK_PISTON_CONTRACT;
	}
	
	public AutoCompactingInventory.AutoCraftingMode getAutoCraftingMode() {
		return this.autoCraftingMode;
	}
	
	public void applySettings(PacketByteBuf buf) {
		int autoCraftingModeInt = buf.readInt();
		this.autoCraftingMode = AutoCompactingInventory.AutoCraftingMode.values()[autoCraftingModeInt];
		this.lastCraftingRecipe = null;
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new CompactingChestScreenHandler(syncId, playerInventory, this);
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
		buf.writeInt(this.autoCraftingMode.ordinal());
	}
	
}
