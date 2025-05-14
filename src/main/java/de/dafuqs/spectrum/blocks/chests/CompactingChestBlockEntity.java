package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.c2s_payloads.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class CompactingChestBlockEntity extends SpectrumChestBlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {
	
	private static final Map<AutoCraftingMode, Map<ItemVariant, Optional<RecipeHolder<CraftingRecipe>>>> cache = new EnumMap<>(AutoCraftingMode.class);
	private AutoCraftingMode autoCraftingMode;
	private RecipeHolder<CraftingRecipe> lastCraftingRecipe; // cache
	private ItemVariant lastItemVariant; // cache
	private boolean hasToCraft, isOpen;
	private State state = State.CLOSED;
	float pistonPos, pistonTarget, lastPistonTarget, driverPos, driverTarget, lastDriverTarget, capPos, capTarget, lastCapTarget;
	long interpTicks, interpLength = 1, activeTicks, craftingTicks;
	
	private final ContainerData propertyDelegate = new ContainerData() {
		@Override
		public int get(int index) {
			if (index == 0) return autoCraftingMode.ordinal();
			return 0;
		}
		
		@Override
		public void set(int index, int value) {
			if (index == 0) autoCraftingMode = AutoCraftingMode.values()[value];
		}
		
		@Override
		public int getCount() {
			return 1;
		}
	};
	
	public CompactingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.COMPACTING_CHEST, blockPos, blockState);
		this.autoCraftingMode = AutoCraftingMode.ThreeXThree;
		this.lastItemVariant = null;
		this.lastCraftingRecipe = null;
		this.hasToCraft = false;
	}
	
	@SuppressWarnings("unused")
	public static void tick(Level world, BlockPos pos, BlockState state, CompactingChestBlockEntity chest) {
		if (!world.isClientSide()) {
			CompactingChestStatusUpdatePayload.sendCompactingChestStatusUpdate(chest);
		}
		
		if (world.isClientSide()) {
			if (chest.hasToCraft()) {
				chest.craftingTicks = 20;
			} else {
				chest.craftingTicks--;
			}
			
			if (chest.craftingTicks >= 0) {
				chest.activeTicks++;
			} else {
				chest.activeTicks = 0;
			}
			
			if (chest.isOpen()) {
				chest.changeState(State.OPEN);
				chest.interpLength = 5;
			} else if (chest.craftingTicks >= 0) {
				chest.changeState(State.CRAFTING);
				chest.interpLength = 20;
			} else {
				chest.changeState(State.CLOSED);
				chest.interpLength = 15;
			}
			if (chest.interpTicks < chest.interpLength) {
				chest.interpTicks++;
			}
		} else {
			if (chest.hasToCraft) {
				boolean couldCraft = chest.tryCraftOnce();
				if (!couldCraft) {
					chest.shouldCraft(false);
				}
				if (world.getGameTime() % 6 == 0) {
					chest.produceRunningEffects();
				}
			}
		}
	}
	
	public void produceRunningEffects() {
		if (level instanceof ServerLevel server) {
			var random = level.getRandom();
			if (random.nextFloat() < 0.125F) {
				server.playSound(null, worldPosition, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 0.05F + random.nextFloat() * 0.1F, 0.334F + random.nextFloat() / 2F);
				for (int i = 0; i < 4 + random.nextInt(5); i++) {
					server.sendParticles(ParticleTypes.CLOUD, worldPosition.getX() + random.nextFloat(), worldPosition.getY() + 0.975 + random.nextFloat() * 0.667F, worldPosition.getZ() + random.nextFloat(), 0, 0, random.nextFloat() / 20F + 0.02F, 0, 1);
				}
			}
		}
	}
	
	public void changeState(State state) {
		if (this.state != state) {
			this.state = state;
			lastPistonTarget = pistonPos;
			lastDriverTarget = driverPos;
			lastCapTarget = capPos;
			interpTicks = 0;
		}
	}
	
	public State getState() {
		return state;
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
						int maxStackCount = currentStack.getMaxStackSize();
						int maxAcceptCount = Math.min(additionStack.getCount(), maxStackCount);
						
						if (!test) {
							ItemStack newStack = additionStack.copy();
							newStack.setCount(maxAcceptCount);
							inventory.set(i, newStack);
						}
						additionStack.setCount(additionStack.getCount() - maxAcceptCount);
						doneStuff = true;
					} else if (ItemStack.isSameItemSameComponents(currentStack, additionStack)) {
						// add to stack;
						int maxStackCount = currentStack.getMaxStackSize();
						int canAcceptCount = maxStackCount - currentStack.getCount();
						
						if (canAcceptCount > 0) {
							if (!test) {
								inventory.get(i).grow(Math.min(additionStack.getCount(), canAcceptCount));
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
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.compacting_chest");
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		if (tag.contains("AutoCraftingMode", Tag.TAG_ANY_NUMERIC)) {
			int autoCraftingModeInt = tag.getInt("AutoCraftingMode");
			this.autoCraftingMode = AutoCraftingMode.values()[autoCraftingModeInt];
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		tag.putInt("AutoCraftingMode", this.autoCraftingMode.ordinal());
	}
	
	@Override
	public int getContainerSize() {
		return 27;
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		super.setItem(slot, stack);
		shouldCraft(true);
	}
	
	public void inventoryChanged() {
		shouldCraft(true);
	}
	
	private boolean tryCraftOnce() {
		Optional<RecipeHolder<CraftingRecipe>> optionalCraftingRecipe = Optional.empty();
		NonNullList<ItemStack> inventory = this.getItems();
		
		// try last recipe
		if (lastCraftingRecipe != null) {
			int requiredItemCount = this.autoCraftingMode.getSize();
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
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public void shouldCraft(boolean hasToCraft) {
		this.hasToCraft = hasToCraft;
	}
	
	@Override
	public boolean triggerEvent(int type, int data) {
		if (type == 1) {
			isOpen = data > 0;
		}
		return super.triggerEvent(type, data);
	}
	
	public boolean hasToCraft() {
		return hasToCraft;
	}
	
	public Optional<RecipeHolder<CraftingRecipe>> searchRecipeToCraft() {
		if (level == null)
			return Optional.empty();
		
		for (ItemStack itemStack : inventory) {
			if (itemStack.isEmpty()) {
				continue;
			}
			
			int requiredItemCount = this.autoCraftingMode.getSize();
			Tuple<Integer, List<ItemStack>> stackPair = InventoryHelper.getStackCountInInventory(itemStack, inventory, requiredItemCount);
			if (stackPair.getA() >= requiredItemCount) {
				var currentCache = cache.computeIfAbsent(autoCraftingMode, mode -> new HashMap<>());
				ItemVariant itemVariant = ItemVariant.of(itemStack);
				
				var recipe = currentCache.get(itemVariant);
				if (recipe != null) {
					if (recipe.isEmpty()) {
						continue;
					}
					this.lastItemVariant = itemVariant;
					return recipe;
				}
				
				CraftingInput input = this.autoCraftingMode.createRecipeInput(itemVariant).input();
				var optionalCraftingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
				if (optionalCraftingRecipe.isEmpty() || optionalCraftingRecipe.get().value().assemble(input, level.registryAccess()).isEmpty()) {
					optionalCraftingRecipe = Optional.empty();
					currentCache.put(itemVariant, optionalCraftingRecipe);
				} else {
					currentCache.put(itemVariant, optionalCraftingRecipe);
					
					this.lastItemVariant = itemVariant;
					return optionalCraftingRecipe;
				}
			}
		}
		
		return Optional.empty();
	}
	
	public boolean tryCraftInInventory(NonNullList<ItemStack> inventory, RecipeHolder<CraftingRecipe> craftingRecipe, ItemVariant itemVariant) {
		if (level == null)
			return false;
		
		ItemStack inputStack = itemVariant.toStack(this.autoCraftingMode.getSize());
		List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(inputStack, this);
		
		boolean spaceInInventory;
		
		List<ItemStack> additionItemStacks = new ArrayList<>();
		additionItemStacks.add(craftingRecipe.value().getResultItem(level.registryAccess()));
		additionItemStacks.addAll(remainders);
		
		spaceInInventory = smartAddToInventory(additionItemStacks, inventory, true);
		if (spaceInInventory) {
			// craft
			smartAddToInventory(additionItemStacks, inventory, false);
			this.setItems(inventory);
			
			// cache
			return true;
		} else {
			smartAddToInventory(List.of(inputStack), inventory, false);
			return false;
		}
	}
	
	@Override
	public SoundEvent getOpenSound() {
		return SpectrumSoundEvents.COMPACTING_CHEST_OPEN;
	}
	
	@Override
	public SoundEvent getCloseSound() {
		return SpectrumSoundEvents.COMPACTING_CHEST_CLOSE;
	}
	
	public void applySettings(ChangeCompactingChestSettingsPayload packet) {
		this.autoCraftingMode = packet.mode();
		this.lastCraftingRecipe = null;
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new CompactingChestScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
	
	@Override
	public BlockPos getScreenOpeningData(ServerPlayer serverPlayerEntity) {
		return worldPosition;
	}
	
	public enum State {
		OPEN,
		CRAFTING,
		CLOSED
	}
	
}
