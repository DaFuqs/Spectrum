package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.events.ExperienceOrbEntityTransferListener;
import de.dafuqs.spectrum.events.ItemEntityTransferListener;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.events.SuckingChestEventListener;
import de.dafuqs.spectrum.inventories.SuckingChestScreenHandler;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuckingChestBlockEntity extends SpectrumChestBlockEntity implements ExtendedScreenHandlerFactory, SuckingChestEventListener.Callback {
	
	private static final int RANGE = 12;
	public static final int INVENTORY_SIZE = 28;
	public static final int ITEM_FILTER_SLOTS = 5;
	public static final int EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT = 27;
	
	private final SuckingChestEventListener suckingChestEventListener;
	private final List<Item> filterItems;

	public SuckingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.SUCKING_CHEST, blockPos, blockState);
		this.suckingChestEventListener = new SuckingChestEventListener(new BlockPositionSource(this.pos), RANGE, this);
		this.filterItems = DefaultedList.ofSize(ITEM_FILTER_SLOTS, Items.AIR);
	}

	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.sucking_chest");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new SuckingChestScreenHandler(syncId, playerInventory, this);
	}
	
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		for(int i = 0; i < ITEM_FILTER_SLOTS; i++) {
			tag.putString("Filter"+i, Registry.ITEM.getId(this.filterItems.get(i)).toString());
		}
	}
	
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		for(int i = 0; i < ITEM_FILTER_SLOTS; i++) {
			if(tag.contains("Filter"+i, NbtElement.STRING_TYPE)) {
				this.filterItems.set(i, Registry.ITEM.get(new Identifier(tag.getString("Filter"+i))));
			}
		}
	}

	public static void tick(@NotNull World world, BlockPos pos, BlockState state, SuckingChestBlockEntity blockEntity) {
		if(world.isClient) {
			blockEntity.lidAnimator.step();
		} else {
			blockEntity.suckingChestEventListener.tick(world);
			if(world.getTime() % 40 == 0 && !SpectrumChestBlock.isChestBlocked(world, pos)) {
				triggerNearbyEntities(blockEntity);
			}
		}
	}

	private static void triggerNearbyEntities(@NotNull SuckingChestBlockEntity blockEntity) {
		List<ItemEntity> itemEntities = blockEntity.world.getEntitiesByType(EntityType.ITEM, getBoxWithRadius(blockEntity.pos, RANGE), Entity::isAlive);
		for(ItemEntity itemEntity : itemEntities) {
			if(itemEntity.isAlive() && !itemEntity.getStack().isEmpty()) {
				itemEntity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
			}
		}
		
		List<ExperienceOrbEntity> experienceOrbEntities = blockEntity.world.getEntitiesByType(EntityType.EXPERIENCE_ORB, getBoxWithRadius(blockEntity.pos, RANGE), Entity::isAlive);
		for(ExperienceOrbEntity experienceOrbEntity : experienceOrbEntities) {
			if(experienceOrbEntity.isAlive()) {
				experienceOrbEntity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
			}
		}
	}

	@Contract("_, _ -> new")
	protected static @NotNull Box getBoxWithRadius(BlockPos blockPos, int radius) {
		return Box.of(Vec3d.ofCenter(blockPos), radius, radius, radius);
	}

	@Override
	public int size() {
		return 27+1; // 3 rows, 1 knowledge gem, 5 item filters (they are not real slots, though)
	}

	public SuckingChestEventListener getEventListener() {
		return this.suckingChestEventListener;
	}

	@Override
	public boolean accepts(World world, GameEventListener listener, BlockPos pos, GameEvent event, Entity entity) {
		boolean isEntitySpawnEvent = event == SpectrumGameEvents.ENTITY_SPAWNED;
		return isEntitySpawnEvent
				&& !SpectrumChestBlock.isChestBlocked(this.world, this.pos)
				&& (
						(entity instanceof ItemEntity itemEntity && acceptsItemStack(itemEntity.getStack()))
					 || (entity instanceof ExperienceOrbEntity && hasExperienceStorageItem())
				);
	}

	@Override
	public void accept(World world, GameEventListener listener, GameEvent event, int distance) {
		if(listener instanceof ItemEntityTransferListener itemEntityTransferListener) {
			if(!SpectrumChestBlock.isChestBlocked(world, pos)) {
				ItemEntity itemEntity = itemEntityTransferListener.getItemEntity();
				if (itemEntity != null && itemEntity.isAlive() && acceptsItemStack(itemEntity.getStack())) { //&& itemEntity.cannotPickup()) { // risky. But that is always false for newly dropped items
					int previousAmount = itemEntity.getStack().getCount();
					ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemEntity.getStack(), this, Direction.UP);

					if (remainingStack.isEmpty()) {
						SpectrumS2CPackets.sendPlayItemEntityAbsorbedParticle(world, itemEntity);
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + this.world.random.nextFloat() * 0.2F, 0.9F + this.world.random.nextFloat() * 0.2F);
						itemEntity.setStack(ItemStack.EMPTY);
					} else {
						if(remainingStack.getCount() != previousAmount) {
							SpectrumS2CPackets.sendPlayItemEntityAbsorbedParticle(world, itemEntity);
							world.playSound(null, itemEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + this.world.random.nextFloat() * 0.2F, 0.9F + this.world.random.nextFloat() * 0.2F);
							itemEntity.setStack(remainingStack);
						}
					}
				}
			}
		} else if(listener instanceof ExperienceOrbEntityTransferListener experienceOrbEntityTransferListener) {
			if(!SpectrumChestBlock.isChestBlocked(world, pos)) {
				ExperienceOrbEntity experienceOrbEntity = experienceOrbEntityTransferListener.getExperienceOrbEntity();
				if (experienceOrbEntity != null && experienceOrbEntity.isAlive() && hasExperienceStorageItem()) {
					ExperienceStorageItem.addStoredExperience(this.inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT), experienceOrbEntity.getExperienceAmount()); // overflow experience is void, to not lag the world on large farms
					
					SpectrumS2CPackets.sendPlayExperienceOrbEntityAbsorbedParticle(world, experienceOrbEntity);
					world.playSound(null, experienceOrbEntity.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.9F + this.world.random.nextFloat() * 0.2F, 0.9F + this.world.random.nextFloat() * 0.2F);
					experienceOrbEntity.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}
	}
	
	@Override
	public SoundEvent getOpenSound() {
		return SpectrumSoundEvents.SUCKING_CHEST_OPEN;
	}

	@Override
	public SoundEvent getCloseSound() {
		return SpectrumSoundEvents.SUCKING_CHEST_CLOSE;
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
		for(Item filterItem : this.filterItems) {
			buf.writeIdentifier(Registry.ITEM.getId(filterItem));
		}
	}
	
	public List<Item> getItemFilters() {
		return this.filterItems;
	}
	
	public void setFilterItem(int slot, Item item) {
		this.filterItems.set(slot, item);
		this.markDirty();
	}
	
	public boolean acceptsItemStack(ItemStack itemStack) {
		if(itemStack.isEmpty()) {
			return false;
		}
		
		boolean allAir = true;
		for(int i = 0; i < ITEM_FILTER_SLOTS; i++) {
			Item filterItem = this.filterItems.get(i);
			if(filterItem.equals(itemStack.getItem())) {
				return true;
			} else if(!filterItem.equals(Items.AIR)) {
				allAir = false;
			}
		}
		return allAir;
	}
	
	public boolean hasExperienceStorageItem() {
		return this.inventory.get(EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT).getItem() instanceof ExperienceStorageItem;
	}
	
}
