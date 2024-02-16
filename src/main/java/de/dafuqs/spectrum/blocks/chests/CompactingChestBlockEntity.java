package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class CompactingChestBlockEntity extends SpectrumChestBlockEntity implements ExtendedScreenHandlerFactory {
	
	private static final Map<AutoCompactingInventory.AutoCraftingMode, Map<ItemVariant, Optional<CraftingRecipe>>> cache = new EnumMap<>(AutoCompactingInventory.AutoCraftingMode.class);
	private final AutoCompactingInventory autoCompactingInventory = new AutoCompactingInventory();
	private AutoCompactingInventory.AutoCraftingMode autoCraftingMode;
	private CraftingRecipe lastCraftingRecipe; // cache
	private ItemVariant lastItemVariant; // cache
	private boolean hasToCraft, isOpen;
	private State state = State.CLOSED;
	float pistonPos, pistonTarget, lastPistonTarget, driverPos, driverTarget, lastDriverTarget, capPos, capTarget, lastCapTarget;
	long interpTicks, interpLength = 1, activeTicks, craftingTicks;

	
	public CompactingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.COMPACTING_CHEST, blockPos, blockState);
		this.autoCraftingMode = AutoCompactingInventory.AutoCraftingMode.ThreeXThree;
		this.lastItemVariant = null;
		this.lastCraftingRecipe = null;
		this.hasToCraft = false;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, CompactingChestBlockEntity chest) {
		if (chest.hasToCraft()) {
			chest.craftingTicks = 20;
		}
		else {
			chest.craftingTicks--;
		}

		if (chest.craftingTicks >= 0) {
			chest.activeTicks++;
		}
		else {
			chest.activeTicks = 0;
		}

		if (!world.isClient()) {
			SpectrumS2CPacketSender.sendCompactingChestStatusUpdate((ServerWorld) world, chest);
		}

		if (chest.isOpen()) {
			chest.changeState(State.OPEN);
			chest.interpLength = 5;
		}
		else if(chest.craftingTicks >= 0) {
			chest.changeState(State.CRAFTING);
			chest.interpLength = 20;
			if (chest.activeTicks % 3 == 0) {
				chest.produceRunningEffects();
			}
		}
		else {
			chest.changeState(State.CLOSED);
			chest.interpLength = 15;
		}
		if (chest.interpTicks < chest.interpLength) {
			chest.interpTicks++;
		}

		if (world.isClient) {
			chest.lidAnimator.step();
		} else {
			if (chest.hasToCraft) {
				boolean couldCraft = chest.tryCraftOnce();
				if (!couldCraft) {
					chest.shouldCraft(false);
				}
			}
		}
	}

	public void produceRunningEffects() {
		var random = world.getRandom();
		if (world.random.nextFloat() < 0.125F) {
			world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.05F + random.nextFloat() * 0.1F, 0.334F + random.nextFloat() / 2F, true);
			for (int i = 0; i < 4 + random.nextInt(5); i++) {
				world.addParticle(ParticleTypes.CLOUD, pos.getX() + random.nextFloat(), pos.getY() + 0.975 + random.nextFloat() * 0.667F, pos.getZ() + random.nextFloat(), 0, random.nextFloat() / 20F + 0.02F, 0);
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
						int maxStackCount = currentStack.getMaxCount();
						int maxAcceptCount = Math.min(additionStack.getCount(), maxStackCount);
						
						if (!test) {
							ItemStack newStack = additionStack.copy();
							newStack.setCount(maxAcceptCount);
							inventory.set(i, newStack);
						}
						additionStack.setCount(additionStack.getCount() - maxAcceptCount);
						doneStuff = true;
					} else if (ItemStack.canCombine(currentStack, additionStack)) {
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
	
	@Override
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.compacting_chest");
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		if (tag.contains("AutoCraftingMode", NbtElement.INT_TYPE)) {
			int autoCraftingModeInt = tag.getInt("AutoCraftingMode");
			this.autoCraftingMode = AutoCompactingInventory.AutoCraftingMode.values()[autoCraftingModeInt];
		}
	}
	
	@Override
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
		shouldCraft(true);
	}
	
	public void inventoryChanged() {
		shouldCraft(true);
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

	public boolean isOpen() {
		return isOpen;
	}

	public void shouldCraft(boolean hasToCraft) {
		this.hasToCraft = hasToCraft;
	}

	@Override
	public boolean onSyncedBlockEvent(int type, int data) {
		if (type == 1) {
			isOpen = data > 0;
		}
		return super.onSyncedBlockEvent(type, data);
	}

	public boolean hasToCraft() {
		return hasToCraft;
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
				if (optionalCraftingRecipe.isEmpty() || optionalCraftingRecipe.get().getOutput(world.getRegistryManager()).isEmpty()) {
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
		List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(inputStack, this);
		
		boolean spaceInInventory;
		
		List<ItemStack> additionItemStacks = new ArrayList<>();
		additionItemStacks.add(craftingRecipe.getOutput(world.getRegistryManager()));
		additionItemStacks.addAll(remainders);
		
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
		return SpectrumSoundEvents.COMPACTING_CHEST_OPEN;
	}
	
	@Override
	public SoundEvent getCloseSound() {
		return SpectrumSoundEvents.COMPACTING_CHEST_CLOSE;
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

	public enum State{
		OPEN,
		CRAFTING,
		CLOSED;
	}

}
