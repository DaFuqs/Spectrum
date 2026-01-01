package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.events.listeners.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.event.listener.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

@SuppressWarnings("UnstableApiUsage")
public class BlackHoleChestBlockEntity extends SpectrumChestBlockEntity implements ExtendedScreenHandlerFactory, SidedInventory, EventQueue.Callback<Object>, TagFilteringInventory {
	
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
	
	private final Object2BooleanMap<TagKey<Item>> filteredTags;
	private boolean allTagsDeny = true;
	
	public BlackHoleChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.BLACK_HOLE_CHEST, blockPos, blockState);
		this.itemAndExperienceEventQueue = new ItemAndExperienceEventQueue(new BlockPositionSource(this.pos), RANGE, this);
		this.filterItems = DefaultedList.ofSize(ITEM_FILTER_SLOT_COUNT, ItemVariant.blank());
		this.filteredTags = new Object2BooleanArrayMap<>();
	}

	public static void tick(@NotNull World world, BlockPos pos, BlockState state, BlackHoleChestBlockEntity chest) {
		chest.age++;

		if (chest.isOpen) {
			if (chest.canFunction()) {
				chest.changeState(State.OPEN_ACTIVE);
				chest.interpLength = 7;
			}
			else {
				chest.changeState(State.OPEN_INACTIVE);
				chest.interpLength = 5;
			}
		}
		else {
			if (chest.isFull) {
				chest.changeState(State.FULL);
				chest.interpLength = 12;
			}
			else if (chest.canFunction()) {
				chest.changeState(State.CLOSED_ACTIVE);
				chest.interpLength = 15;
			}
			else {
				chest.changeState(State.CLOSED_INACTIVE);
				chest.interpLength = 10;
			}
		}

		if (chest.interpTicks < chest.interpLength) {
			chest.interpTicks++;
		}

		if (world.isClient) {
			chest.lidAnimator.step();
		} else {
			chest.itemAndExperienceEventQueue.tick(world);
			if (world.getTime() % 80 == 0 && !SpectrumChestBlock.isChestBlocked(world, pos)) {
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
		if (!world.isClient()) {
			var wasFull = isFull;
			isFull = isFull();
			if (force || wasFull != isFull) {
				SpectrumS2CPacketSender.sendBlackHoleChestUpdate(this);
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
		return !SpectrumChestBlock.isChestBlocked(world, this.pos) && !isFull;
	}

	public boolean isFull() {
		for (int i = 0; i < inventory.size() - 1; i++) {
			var stack = inventory.get(i);
			if (stack.getCount() < stack.getMaxCount()) {
				return  false;
			}
		}

		if (canStoreExperience()) {
			var experienceStack = inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT);
			var experienceStorage = (ExperienceStorageItem) experienceStack.getItem();
			return ExperienceStorageItem.getStoredExperience(experienceStack) >= experienceStorage.getMaxStoredExperience(experienceStack);
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
		List<ItemEntity> itemEntities = blockEntity.getWorld().getEntitiesByType(EntityType.ITEM, getBoxWithRadius(blockEntity.pos, RANGE), Entity::isAlive);
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.isAlive() && !itemEntity.getStack().isEmpty()) {
				itemEntity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
			}
		}

		List<ExperienceOrbEntity> experienceOrbEntities = blockEntity.getWorld().getEntitiesByType(EntityType.EXPERIENCE_ORB, getBoxWithRadius(blockEntity.pos, RANGE), Entity::isAlive);
		for (ExperienceOrbEntity experienceOrbEntity : experienceOrbEntities) {
			if (experienceOrbEntity.isAlive()) {
				experienceOrbEntity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
			}
		}
	}
	
	@Contract("_, _ -> new")
	protected static @NotNull Box getBoxWithRadius(BlockPos blockPos, int radius) {
		return Box.of(Vec3d.ofCenter(blockPos), radius, radius, radius);
	}

	@Override
	public boolean onSyncedBlockEvent(int type, int data) {
		if (type == 1) {
			isOpen = data > 0;
		}
		return super.onSyncedBlockEvent(type, data);
	}
	
	@Override
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.black_hole_chest");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new BlackHoleChestScreenHandler(syncId, playerInventory, this);
	}
	
	@Override
	protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		super.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
		updateFullState(true);
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		FilterConfigurable.writeFilterNbt(tag, filterItems);
		tag.putLong("age", age);
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		FilterConfigurable.readFilterNbt(tag, filterItems);
		this.updateTagFilteringItems();
		age = tag.getLong("age");
	}
	
	@Override
	public int size() {
		return 27 + 1; // 3 rows, 1 knowledge gem, 5 item filters (they are not real slots, though)
	}
	
	public ItemAndExperienceEventQueue getEventListener() {
		return this.itemAndExperienceEventQueue;
	}
	
	@Override
	public boolean canAcceptEvent(World world, GameEventListener listener, GameEvent.Message event, Vec3d sourcePos) {
		if (SpectrumChestBlock.isChestBlocked(world, this.pos)) {
			return false;
		}
		Entity entity = event.getEmitter().sourceEntity();
		if (entity instanceof ItemEntity) {
			return true;
		}
		return entity instanceof ExperienceOrbEntity && hasExperienceStorageItem();
	}
	
	@Override
	public void triggerEvent(World world, GameEventListener listener, Object entry) {
		if (SpectrumChestBlock.isChestBlocked(world, pos)) {
			return;
		}
		
		if (entry instanceof ExperienceOrbEventQueue.EventEntry experienceEntry) {
			ExperienceOrbEntity experienceOrbEntity = experienceEntry.experienceOrbEntity;
			if (experienceOrbEntity != null && experienceOrbEntity.isAlive() && hasExperienceStorageItem()) {
				ExperienceStorageItem.addStoredExperience(this.inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT), experienceOrbEntity.getExperienceAmount()); // overflow experience is void, to not lag the world on large farms

				sendPlayExperienceOrbEntityAbsorbedParticle((ServerWorld) world, experienceOrbEntity);
				world.playSound(null, experienceOrbEntity.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
				experienceOrbEntity.remove(Entity.RemovalReason.DISCARDED);
			}
		} else if (entry instanceof ItemEntityEventQueue.EventEntry itemEntry) {
			ItemEntity itemEntity = itemEntry.itemEntity;
			if (itemEntity != null && itemEntity.isAlive() && !itemEntity.cannotPickup() && this.acceptsItem(itemEntity.getStack().getItem())) {
				int previousAmount = itemEntity.getStack().getCount();
				ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemEntity.getStack(), this, Direction.UP);
				
				if (remainingStack.isEmpty()) {
					sendPlayItemEntityAbsorbedParticle((ServerWorld) world, itemEntity);
					world.playSound(null, itemEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
					itemEntity.setStack(ItemStack.EMPTY);
					itemEntity.discard();
				} else {
					if (remainingStack.getCount() != previousAmount) {
						sendPlayItemEntityAbsorbedParticle((ServerWorld) world, itemEntity);
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
						itemEntity.setStack(remainingStack);
					}
				}
			}
		}
	}

	public static void sendPlayItemEntityAbsorbedParticle(ServerWorld world, @NotNull ItemEntity itemEntity) {
		SpectrumS2CPacketSender.playParticleWithExactVelocity(world, itemEntity.getPos(),
				SpectrumParticleTypes.BLUE_BUBBLE_POP,
				1, Vec3d.ZERO);
	}

	public static void sendPlayExperienceOrbEntityAbsorbedParticle(ServerWorld world, @NotNull ExperienceOrbEntity experienceOrbEntity) {
		SpectrumS2CPacketSender.playParticleWithExactVelocity(world, experienceOrbEntity.getPos(),
				SpectrumParticleTypes.GREEN_BUBBLE_POP,
				1, Vec3d.ZERO);
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
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
		FilterConfigurable.writeScreenOpeningData(buf, filterItems, 1, ITEM_FILTER_SLOT_COUNT, ITEM_FILTER_SLOT_COUNT);
	}

	public List<ItemVariant> getItemFilters() {
		return this.filterItems;
	}

	public void setFilterItem(int slot, ItemVariant item) {
		this.filterItems.set(slot, item);
		this.updateTagFilteringItems();
		this.markDirty();
	}
	
	public Object2BooleanMap<TagKey<Item>> getFilteredTags() { return this.filteredTags; }
	public boolean onlyDenyListTags() { return this.allTagsDeny; }
	public void setOnlyDenyListTags(boolean onlyDenyListTags) { this.allTagsDeny = onlyDenyListTags; }
	
	public boolean hasExperienceStorageItem() {
		return this.inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT).getItem() instanceof ExperienceStorageItem;
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		return IntStream.rangeClosed(0, EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT - 1).toArray();
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return true;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		super.setStack(slot, stack);
		updateFullState(false);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		var stack = super.removeStack(slot, amount);
		if (!stack.isEmpty())
			updateFullState(false);
		return stack;
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		var stack = super.removeStack(slot);
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
