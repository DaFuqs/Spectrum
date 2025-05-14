package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.events.listeners.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class BlackHoleChestBlockEntity extends SpectrumChestBlockEntity implements FilterConfigurable, ExtendedScreenHandlerFactory<FilterConfigurable.ExtendedDataWithPos>, WorldlyContainer, EventQueue.Callback<Object> {
	
	public static final int INVENTORY_SIZE = 28;
	public static final int ITEM_FILTER_SLOT_COUNT = 5;
	public static final int EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT = 27;
	private static final int RANGE = 12;
	private final ItemAndExperienceEventQueue itemAndExperienceEventQueue;
	private final List<ItemVariant> filterItems;
	private State state = State.CLOSED_INACTIVE;
	private boolean isOpen, isFull, hasXPStorage;
	float storageTarget, storagePos, lastStorageTarget, capTarget, capPos, lastCapTarget, orbTarget, orbPos, lastOrbTarget, yawTarget, orbYaw, lastYawTarget;
	long interpTicks, interpLength = 1, age, storedXP, maxStoredXP;
	
	public BlackHoleChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.BLACK_HOLE_CHEST, blockPos, blockState);
		this.itemAndExperienceEventQueue = new ItemAndExperienceEventQueue(new BlockPositionSource(this.worldPosition), RANGE, this);
		this.filterItems = NonNullList.withSize(ITEM_FILTER_SLOT_COUNT, ItemVariant.blank());
	}
	
	@SuppressWarnings("unused")
	public static void tick(@NotNull Level world, BlockPos pos, BlockState state, BlackHoleChestBlockEntity chest) {
		chest.age++;
		
		if (chest.isOpen) {
			if (chest.canFunction()) {
				chest.changeState(State.OPEN_ACTIVE);
				chest.interpLength = 7;
			} else {
				chest.changeState(State.OPEN_INACTIVE);
				chest.interpLength = 5;
			}
		} else {
			if (chest.isFull) {
				chest.changeState(State.FULL);
				chest.interpLength = 12;
			} else if (chest.canFunction()) {
				chest.changeState(State.CLOSED_ACTIVE);
				chest.interpLength = 15;
			} else {
				chest.changeState(State.CLOSED_INACTIVE);
				chest.interpLength = 10;
			}
		}
		
		if (chest.interpTicks < chest.interpLength) {
			chest.interpTicks++;
		}
		
		if (world.isClientSide) {
			chest.lidAnimator.tickLid();
		} else {
			chest.itemAndExperienceEventQueue.tick(world);
			if (world.getGameTime() % 80 == 0 && !SpectrumChestBlock.isChestBlocked(world, pos)) {
				searchForNearbyEntities(chest);
			}
		}
	}
	
	public long getRenderTime() {
		return age % 50000;
	}
	
	public void changeState(State state) {
		if (this.state != state) {
			this.state = state;
			lastCapTarget = capPos;
			lastOrbTarget = orbPos;
			lastStorageTarget = storagePos;
			lastYawTarget = orbYaw;
			interpTicks = 0;
		}
	}
	
	public void updateFullState(boolean force) {
		if (level != null && !level.isClientSide()) {
			var wasFull = isFull;
			isFull = isFull();
			if (force || wasFull != isFull) {
				BlackHoleChestStatusUpdatePayload.sendBlackHoleChestUpdate(this);
			}
		}
	}
	
	public void setXPData(long xp, long max) {
		this.storedXP = xp;
		this.maxStoredXP = max;
	}
	
	public State getState() {
		return state;
	}
	
	public boolean canFunction() {
		return level != null && !SpectrumChestBlock.isChestBlocked(level, this.worldPosition) && !isFull;
	}
	
	public boolean isFull() {
		for (int i = 0; i < inventory.size() - 1; i++) {
			var stack = inventory.get(i);
			if (stack.getCount() < stack.getMaxStackSize()) {
				return false;
			}
		}
		
		if (canStoreExperience() && level != null) {
			var experienceStack = inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT);
			var experienceStorage = (ExperienceStorageItem) experienceStack.getItem();
			return ExperienceStorageItem.getStoredExperience(experienceStack) >= experienceStorage.getMaxStoredExperience(level.registryAccess(), experienceStack);
		}
		
		return true;
	}
	
	public boolean canStoreExperience() {
		return inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT).getItem() instanceof ExperienceStorageItem;
	}
	
	public boolean isFullServer() {
		return isFull;
	}
	
	public void setFull(boolean full) {
		isFull = full;
	}
	
	public void setHasXPStorage(boolean hasXPStorage) {
		this.hasXPStorage = hasXPStorage;
	}
	
	public boolean hasXPStorage() {
		return hasXPStorage;
	}
	
	private static void searchForNearbyEntities(@NotNull BlackHoleChestBlockEntity blockEntity) {
		var world = blockEntity.getLevel();
		if (world == null)
			return;
		
		List<ItemEntity> itemEntities = world.getEntities(EntityType.ITEM, getBoxWithRadius(blockEntity.worldPosition, RANGE), Entity::isAlive);
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.isAlive() && !itemEntity.getItem().isEmpty()) {
				itemEntity.gameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
			}
		}
		
		List<ExperienceOrb> experienceOrbEntities = world.getEntities(EntityType.EXPERIENCE_ORB, getBoxWithRadius(blockEntity.worldPosition, RANGE), Entity::isAlive);
		for (ExperienceOrb experienceOrbEntity : experienceOrbEntities) {
			if (experienceOrbEntity.isAlive()) {
				experienceOrbEntity.gameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
			}
		}
	}
	
	@Contract("_, _ -> new")
	protected static @NotNull AABB getBoxWithRadius(BlockPos blockPos, int radius) {
		return AABB.ofSize(Vec3.atCenterOf(blockPos), radius, radius, radius);
	}
	
	@Override
	public boolean triggerEvent(int type, int data) {
		if (type == 1) {
			isOpen = data > 0;
		}
		return super.triggerEvent(type, data);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.black_hole_chest");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new BlackHoleChestScreenHandler(syncId, playerInventory, this, new ExtendedData(this));
	}
	
	@Override
	protected void onInvOpenOrClose(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		super.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
		updateFullState(true);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		FilterConfigurable.writeFilterNbt(tag, filterItems);
		tag.putLong("age", age);
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		FilterConfigurable.readFilterNbt(tag, filterItems);
		age = tag.getLong("age");
	}
	
	@Override
	public int getContainerSize() {
		return 27 + 1; // 3 rows, 1 knowledge gem, 5 item filters (they are not real slots, though)
	}
	
	public ItemAndExperienceEventQueue getEventListener() {
		return this.itemAndExperienceEventQueue;
	}
	
	@Override
	public boolean canAcceptEvent(Level world, GameEventListener listener, GameEvent.ListenerInfo event, Vec3 sourcePos) {
		if (SpectrumChestBlock.isChestBlocked(world, this.worldPosition)) {
			return false;
		}
		Entity entity = event.context().sourceEntity();
		if (entity instanceof ItemEntity) {
			return true;
		}
		return entity instanceof ExperienceOrb && hasExperienceStorageItem();
	}
	
	@Override
	public void triggerEvent(Level world, GameEventListener listener, Object entry) {
		if (SpectrumChestBlock.isChestBlocked(world, worldPosition)) {
			return;
		}
		
		if (entry instanceof ExperienceOrbEventQueue.EventEntry experienceEntry) {
			ExperienceOrb experienceOrbEntity = experienceEntry.experienceOrbEntity;
			if (experienceOrbEntity != null && experienceOrbEntity.isAlive() && hasExperienceStorageItem()) {
				ExperienceStorageItem.addStoredExperience(world.registryAccess(), this.inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT), experienceOrbEntity.getValue()); // overflow experience is void, to not lag the world on large farms
				
				sendPlayExperienceOrbEntityAbsorbedParticle((ServerLevel) world, experienceOrbEntity);
				world.playSound(null, experienceOrbEntity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
				experienceOrbEntity.remove(Entity.RemovalReason.DISCARDED);
			}
		} else if (entry instanceof ItemEntityEventQueue.EventEntry itemEntry) {
			ItemEntity itemEntity = itemEntry.itemEntity;
			if (itemEntity != null && itemEntity.isAlive() && ((ItemEntityAccessor) itemEntity).getPickupDelay() != 32767 && acceptsItemStack(itemEntity.getItem())) {
				int previousAmount = itemEntity.getItem().getCount();
				ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemEntity.getItem(), this, Direction.UP);
				
				if (remainingStack.isEmpty()) {
					sendPlayItemEntityAbsorbedParticle((ServerLevel) world, itemEntity);
					world.playSound(null, itemEntity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
					itemEntity.setItem(ItemStack.EMPTY);
					itemEntity.discard();
				} else if (remainingStack.getCount() != previousAmount) {
					sendPlayItemEntityAbsorbedParticle((ServerLevel) world, itemEntity);
					world.playSound(null, itemEntity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
					itemEntity.setItem(remainingStack);
				}
			}
		}
	}
	
	public static void sendPlayItemEntityAbsorbedParticle(ServerLevel world, @NotNull ItemEntity itemEntity) {
		PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity(world, itemEntity.position(),
				SpectrumParticleTypes.BLUE_BUBBLE_POP,
				1, Vec3.ZERO);
	}
	
	public static void sendPlayExperienceOrbEntityAbsorbedParticle(ServerLevel world, @NotNull ExperienceOrb experienceOrbEntity) {
		PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity(world, experienceOrbEntity.position(),
				SpectrumParticleTypes.GREEN_BUBBLE_POP,
				1, Vec3.ZERO);
	}
	
	@Override
	public SoundEvent getOpenSound() {
		return SpectrumSoundEvents.BLACK_HOLE_CHEST_OPEN;
	}
	
	@Override
	public SoundEvent getCloseSound() {
		return SpectrumSoundEvents.BLACK_HOLE_CHEST_CLOSE;
	}
	
	@Override
	public FilterConfigurable.ExtendedDataWithPos getScreenOpeningData(ServerPlayer player) {
		return new ExtendedDataWithPos(worldPosition, this);
	}
	
	@Override
	public List<ItemVariant> getItemFilters() {
		return this.filterItems;
	}
	
	@Override
	public int getSlotsPerRow() {
		return ITEM_FILTER_SLOT_COUNT;
	}
	
	@Override
	public int getDrawnSlots() {
		return ITEM_FILTER_SLOT_COUNT;
	}
	
	public void setFilterItem(int slot, ItemVariant item) {
		this.filterItems.set(slot, item);
		this.setChanged();
	}
	
	public boolean acceptsItemStack(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return false;
		}
		
		boolean allAir = true;
		for (int i = 0; i < ITEM_FILTER_SLOT_COUNT; i++) {
			ItemVariant filterItem = this.filterItems.get(i);
			if (itemStack.is(filterItem.getItem())) {
				return true;
			} else if (!filterItem.isBlank()) {
				allAir = false;
			}
		}
		return allAir;
	}
	
	public boolean hasExperienceStorageItem() {
		return this.inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT).getItem() instanceof ExperienceStorageItem;
	}
	
	@Override
	public int[] getSlotsForFace(Direction side) {
		return IntStream.rangeClosed(0, EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT - 1).toArray();
	}
	
	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		return true;
	}
	
	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		super.setItem(slot, stack);
		updateFullState(false);
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		var stack = super.removeItem(slot, amount);
		if (!stack.isEmpty())
			updateFullState(false);
		return stack;
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		var stack = super.removeItemNoUpdate(slot);
		if (!stack.isEmpty())
			updateFullState(false);
		return stack;
	}
	
	public enum State {
		OPEN_INACTIVE,
		OPEN_ACTIVE,
		CLOSED_ACTIVE,
		CLOSED_INACTIVE,
		FULL
	}
	
}
