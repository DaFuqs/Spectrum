package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.events.listeners.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.event.listener.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class BlackHoleChestBlockEntity extends SpectrumChestBlockEntity implements ExtendedScreenHandlerFactory, SidedInventory, EventQueue.Callback {

	public static final int INVENTORY_SIZE = 28;
	public static final int ITEM_FILTER_SLOT_COUNT = 5;
	public static final int EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT = 27;
	private static final int RANGE = 12;
	private final ItemAndExperienceEventQueue itemAndExperienceEventQueue;
	private final List<Item> filterItems;

	public BlackHoleChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.BLACK_HOLE_CHEST, blockPos, blockState);
		this.itemAndExperienceEventQueue = new ItemAndExperienceEventQueue(new BlockPositionSource(this.pos), RANGE, this);
		this.filterItems = DefaultedList.ofSize(ITEM_FILTER_SLOT_COUNT, Items.AIR);
	}

	public static void tick(@NotNull World world, BlockPos pos, BlockState state, BlackHoleChestBlockEntity blockEntity) {
		if (world.isClient) {
			blockEntity.lidAnimator.step();
		} else {
			blockEntity.itemAndExperienceEventQueue.tick(world);
			if (world.getTime() % 80 == 0 && !SpectrumChestBlock.isChestBlocked(world, pos)) {
				searchForNearbyEntities(blockEntity);
			}
		}
	}

	private static void searchForNearbyEntities(@NotNull BlackHoleChestBlockEntity blockEntity) {
		List<ItemEntity> itemEntities = blockEntity.world.getEntitiesByType(EntityType.ITEM, getBoxWithRadius(blockEntity.pos, RANGE), Entity::isAlive);
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.isAlive() && !itemEntity.getStack().isEmpty()) {
				itemEntity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
			}
		}

		List<ExperienceOrbEntity> experienceOrbEntities = blockEntity.world.getEntitiesByType(EntityType.EXPERIENCE_ORB, getBoxWithRadius(blockEntity.pos, RANGE), Entity::isAlive);
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
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.black_hole_chest");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new BlackHoleChestScreenHandler(syncId, playerInventory, this);
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		for (int i = 0; i < ITEM_FILTER_SLOT_COUNT; i++) {
			tag.putString("Filter" + i, Registries.ITEM.getId(this.filterItems.get(i)).toString());
		}
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		for (int i = 0; i < ITEM_FILTER_SLOT_COUNT; i++) {
			if (tag.contains("Filter" + i, NbtElement.STRING_TYPE)) {
				this.filterItems.set(i, Registries.ITEM.get(new Identifier(tag.getString("Filter" + i))));
			}
		}
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
				world.playSound(null, experienceOrbEntity.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.9F + this.world.random.nextFloat() * 0.2F, 0.9F + this.world.random.nextFloat() * 0.2F);
				experienceOrbEntity.remove(Entity.RemovalReason.DISCARDED);
			}
		} else if (entry instanceof ItemEntityEventQueue.EventEntry itemEntry) {
			ItemEntity itemEntity = itemEntry.itemEntity;
			if (itemEntity != null && itemEntity.isAlive() && acceptsItemStack(itemEntity.getStack())) {
				int previousAmount = itemEntity.getStack().getCount();
				ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemEntity.getStack(), this, Direction.UP);
				
				if (remainingStack.isEmpty()) {
					sendPlayItemEntityAbsorbedParticle((ServerWorld) world, itemEntity);
					world.playSound(null, itemEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + this.world.random.nextFloat() * 0.2F, 0.9F + this.world.random.nextFloat() * 0.2F);
					itemEntity.setStack(ItemStack.EMPTY);
					itemEntity.discard();
				} else {
					if (remainingStack.getCount() != previousAmount) {
						sendPlayItemEntityAbsorbedParticle((ServerWorld) world, itemEntity);
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + this.world.random.nextFloat() * 0.2F, 0.9F + this.world.random.nextFloat() * 0.2F);
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
		FilterConfigurable.writeScreenOpeningData(buf, filterItems);
	}

	public List<Item> getItemFilters() {
		return this.filterItems;
	}

	public void setFilterItem(int slot, Item item) {
		this.filterItems.set(slot, item);
		this.markDirty();
	}
	
	public boolean acceptsItemStack(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return false;
		}
		
		boolean allAir = true;
		for (int i = 0; i < ITEM_FILTER_SLOT_COUNT; i++) {
			Item filterItem = this.filterItems.get(i);
			if (filterItem.equals(itemStack.getItem())) {
				return true;
			} else if (!filterItem.equals(Items.AIR)) {
				allAir = false;
			}
		}
		return allAir;
	}
	
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
}
