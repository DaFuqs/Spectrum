package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.animation.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
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
import org.jspecify.annotations.*;

import java.util.*;

public class CompactingChestBlockEntity extends SpectrumChestBlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {
	
	private static final FlowAnimator.Factory<CompactingChestBlockEntity> FACTORY;
	
	private AutoCraftingMode mode = AutoCraftingMode.ThreeXThree;
	private @Nullable RecipeHolder<CraftingRecipe> lastCraftingRecipe; // cache
	private @Nullable ItemStack lastCraftedStack; // cache
	
	protected boolean isOpen;
	protected long craftingTimeStamp;
	
	protected @Nullable FlowAnimator animator;
	protected FlowData<Float> _piston = FlowData.NULL();
	protected FlowData<Float> _driver = FlowData.NULL();
	protected FlowData<Float> _cap = FlowData.NULL();
	
	private final ContainerData propertyDelegate = new ContainerData() {
		@Override
		public int get(int index) {
			if (index == 0) return mode.ordinal();
			return 0;
		}
		
		@Override
		public void set(int index, int value) {
			if (index == 0) mode = AutoCraftingMode.values()[value];
		}
		
		@Override
		public int getCount() {
			return 1;
		}
	};
	
	public CompactingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.COMPACTING_CHEST, blockPos, blockState);
	}
	
	public long getCraftingTimeStamp() {
		return craftingTimeStamp;
	}
	
	public void setCraftingTimeStamp(long craftingTimeStamp) {
		this.craftingTimeStamp = craftingTimeStamp;
	}
	
	@SuppressWarnings("unused")
	public static void tick(Level world, BlockPos pos, BlockState state, CompactingChestBlockEntity chest) {
		if (world.isClientSide())
			chest.updateAnimator();
		else
			chest.process();
	}
	
	private void updateAnimator() {
		if (animator == null)
			animator = FACTORY.create(FlowStates.CLOSED, this);
		
		animator.tick();
		
		if (isOpen) {
			animator.swapState(FlowStates.OPEN);
		} else if (level.getGameTime() - craftingTimeStamp < 20) {
			animator.swapState(FlowStates.ACTIVE);
		} else {
			animator.swapState(FlowStates.CLOSED);
		}
	}
	
	private void process() {
		int requiredItemCount = mode.getSize();
		
		// search for items
		Optional<RecipeHolder<CraftingRecipe>> optionalCraftingRecipe = Optional.empty();
		NonNullList<ItemStack> inventory = this.getItems();
		
		// try last recipe
		if (lastCraftingRecipe != null) {
			if (InventoryHelper.isItemCountInInventory(this, lastCraftedStack, requiredItemCount)) {
				optionalCraftingRecipe = Optional.ofNullable(lastCraftingRecipe);
			} else {
				lastCraftingRecipe = null;
				lastCraftedStack = null;
			}
		}
		// search for other recipes
		if (optionalCraftingRecipe.isEmpty()) {
			optionalCraftingRecipe = searchRecipeToCraft();
		}
		
		if (optionalCraftingRecipe.isPresent() && this.lastCraftedStack != null) {
			boolean success = tryCraftInInventory(inventory, optionalCraftingRecipe.get(), this.lastCraftedStack);
			if (success) {
				this.lastCraftingRecipe = optionalCraftingRecipe.get();
				craftingTimeStamp = level.getGameTime();
				produceRunningEffects();
				
				if (level.getGameTime() % 5 == 0) {
					CompactingChestStatusUpdatePayload.sendCompactingChestStatusUpdate(this);
					((ServerLevel) level).getChunkSource().blockChanged(worldPosition);
				}
			}
		}
	}
	
	public Optional<RecipeHolder<CraftingRecipe>> searchRecipeToCraft() {
		if (level == null) return Optional.empty();
		
		Set<AutoCraftingMode.ItemStackHash> triedHashes = new  HashSet<>();
		for (ItemStack itemStack : inventory) {
			if (itemStack.isEmpty()) continue;
			
			AutoCraftingMode.ItemStackHash hash = new AutoCraftingMode.ItemStackHash(itemStack);
			if(triedHashes.contains(hash)) continue; // don't try similar stacks more than once
			triedHashes.add(hash);
			
			int requiredItemCount = this.mode.getSize();
			Tuple<Integer, List<ItemStack>> stackPair = InventoryHelper.getStackCountInInventory(itemStack, inventory, requiredItemCount);
			if (stackPair.getA() < requiredItemCount) continue;
			
			Map<AutoCraftingMode.ItemStackHash, Optional<RecipeHolder<CraftingRecipe>>> currentCache = AutoCraftingMode.getCache(mode);
			ItemStack itemVariant = itemStack.copyWithCount(1);
			
			Optional<RecipeHolder<CraftingRecipe>> recipe = currentCache.get(hash);
			
			// not yet tried
			if (recipe == null) {
				CraftingInput input = this.mode.createRecipeInput(itemStack).input();
				var optionalCraftingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
				if (optionalCraftingRecipe.isEmpty() || optionalCraftingRecipe.get().value().assemble(input, level.registryAccess()).isEmpty())
					optionalCraftingRecipe = Optional.empty();
				currentCache.put(hash, optionalCraftingRecipe);
				this.lastCraftedStack = itemVariant;
				if (optionalCraftingRecipe.isPresent()) this.lastCraftingRecipe = optionalCraftingRecipe.get();
				return optionalCraftingRecipe;
			}
			
			// valid combination
			if (recipe.isPresent()) {
				this.lastCraftedStack = itemVariant;
				this.lastCraftingRecipe = recipe.get();
				return recipe;
			}
		}
		
		return Optional.empty();
	}
	
	public boolean tryCraftInInventory(NonNullList<ItemStack> inventory, RecipeHolder<CraftingRecipe> craftingRecipe, ItemStack itemVariant) {
		if (level == null)
			return false;
		
		ItemStack inputStack = itemVariant.copyWithCount(this.mode.getSize());
		List<ItemStack> remainders = InventoryHelper.decrementInInventoryAndReturnRemainders(inputStack, this);
		
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
	
	public void produceRunningEffects() {
		var random = level.getRandom();
		if (random.nextFloat() < 0.04F) {
			level.playSound(
					null, worldPosition, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS,
					0.05F + random.nextFloat() * 0.1F, 0.334F + random.nextFloat() / 2F
			);
			for (int i = 0; i < 4 + random.nextInt(5); i++) {
				((ServerLevel) level).sendParticles(
						ParticleTypes.CLOUD, worldPosition.getX() + random.nextFloat(),
						worldPosition.getY() + 1 + random.nextFloat() * 0.667F, worldPosition.getZ() + random.nextFloat(),
						0, 0, random.nextFloat() / 20F + 0.02F, 0, 1
				);
			}
		}
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
			this.mode = AutoCraftingMode.values()[autoCraftingModeInt];
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		tag.putInt("AutoCraftingMode", this.mode.ordinal());
	}
	
	@Override
	public boolean triggerEvent(int type, int data) {
		if (type == 1) {
			isOpen = data > 0;
		}
		return super.triggerEvent(type, data);
	}
	
	@Override
	public SoundEvent getOpenSound() {
		return SpectrumSoundEvents.COMPACTING_CHEST_OPEN;
	}
	
	@Override
	public SoundEvent getCloseSound() {
		return SpectrumSoundEvents.COMPACTING_CHEST_CLOSE;
	}
	
	public void applySettings(AutoCraftingMode mode) {
		if (this.mode == mode)
			return;
		
		this.mode = mode;
		this.lastCraftingRecipe = null;
		this.lastCraftedStack = null;
		
		setChanged();
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new CompactingChestScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
	
	@Override
	public BlockPos getScreenOpeningData(ServerPlayer serverPlayerEntity) {
		return worldPosition;
	}
	
	@Override
	public int getContainerSize() {
		return 27;
	}
	
	static {
		var builder = new FlowAnimator.Builder<>(CompactingChestBlockEntity.class);
		builder.stateInfo(FlowStates.OPEN, 5);
		builder.stateInfo(FlowStates.ACTIVE, 20);
		builder.stateInfo(FlowStates.CLOSED, 14);
		
		builder.handle("piston", FlowHandlers.FLOAT)
				.initial(0F)
				.loopback(FlowStates.CLOSED)
				.forStates(14F, FlowStates.OPEN)
				.forStates(KeyFrame.sine(0.1F, 5, 4), FlowStates.ACTIVE)
				.interpolate(Interpolation.CUBIC_IN)
				.push();
		
		builder.handle("driver", FlowHandlers.FLOAT)
				.initial(0F)
				.loopback(FlowStates.CLOSED)
				.forStates(6F, FlowStates.OPEN)
				.forStates(KeyFrame.sine(0.1F, 5, 5, 13), FlowStates.ACTIVE)
				.interpolate(Interpolation.CUBIC_IN)
				.push();
		
		builder.handle("cap", FlowHandlers.FLOAT)
				.initial(0F)
				.loopback(FlowStates.CLOSED, FlowStates.ACTIVE)
				.forStates(5F, FlowStates.OPEN)
				.interpolate(Interpolation.CUBIC_IN)
				.push();
		
		FACTORY = builder.build();
	}
}
