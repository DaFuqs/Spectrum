package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.enums.SpectrumTier;
import de.dafuqs.spectrum.events.ItemEntityTransferListener;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.inventories.GenericSpectrumContainerScreenHandler;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuckingChestBlockEntity extends SpectrumChestBlockEntity implements ItemEntityTransferListener.Callback {

	private final ItemEntityTransferListener listener;
	private static final int RANGE = 12;

	public SuckingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.SUCKING_CHEST, blockPos, blockState);
		this.listener = new ItemEntityTransferListener(new BlockPositionSource(this.pos), RANGE, this);
	}

	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.sucking_chest");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this, SpectrumTier.TIER3);
	}

	public static void tick(@NotNull World world, BlockPos pos, BlockState state, SuckingChestBlockEntity blockEntity) {
		if(world.isClient) {
			blockEntity.lidAnimator.step();
		} else {
			blockEntity.listener.tick(world);
			if(world.getTime() % 40 == 0 && !SpectrumChestBlock.isChestBlocked(world, pos)) {
				checkForNearbyItemEntities(blockEntity);
			}
		}
	}

	private static void checkForNearbyItemEntities(@NotNull SuckingChestBlockEntity blockEntity) {
		List<ItemEntity> itemEntities = blockEntity.world.getEntitiesByType(EntityType.ITEM, getBoxWithRadius(blockEntity.pos, RANGE), Entity::isAlive);

		for(ItemEntity itemEntity : itemEntities) {
			if(itemEntity.isAlive() && !itemEntity.getStack().isEmpty()) {
				itemEntity.emitGameEvent(SpectrumGameEvents.ITEM_TRANSFER);
			}
		}
	}

	@Contract("_, _ -> new")
	protected static @NotNull Box getBoxWithRadius(BlockPos blockPos, int radius) {
		return Box.of(Vec3d.ofCenter(blockPos), radius, radius, radius);
	}

	@Override
	public int size() {
		return 27;
	}

	public ItemEntityTransferListener getEventListener() {
		return this.listener;
	}

	@Override
	public boolean accepts(World world, GameEventListener listener, BlockPos pos, GameEvent event, Entity entity) {
		boolean isItemEvent = event == SpectrumGameEvents.ITEM_TRANSFER;
		return isItemEvent && !SpectrumChestBlock.isChestBlocked(this.world, this.pos);
	}

	@Override
	public void accept(World world, GameEventListener listener, GameEvent event, int distance) {
		if(listener instanceof ItemEntityTransferListener) {
			if(!SpectrumChestBlock.isChestBlocked(world, pos)) {
				ItemEntityTransferListener itemEntityTransferListener = (ItemEntityTransferListener)  listener;
				ItemEntity itemEntity = itemEntityTransferListener.getItemEntity();
				if (itemEntity != null && itemEntity.isAlive() && !itemEntity.getStack().isEmpty()) { //&& itemEntity.cannotPickup()) { // risky. But that is always false for newly dropped items
					int previousAmount = itemEntity.getStack().getCount();
					ItemStack remainingStack = InventoryHelper.addToInventory(itemEntity.getStack(), this, Direction.UP);

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

}
