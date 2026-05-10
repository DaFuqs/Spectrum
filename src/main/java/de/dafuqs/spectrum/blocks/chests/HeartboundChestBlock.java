package de.dafuqs.spectrum.blocks.chests;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import javax.annotation.*;

public class HeartboundChestBlock extends SpectrumChestBlock {
	
	public static final MapCodec<HeartboundChestBlock> CODEC = simpleCodec(HeartboundChestBlock::new);
	
	public HeartboundChestBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	
	@Override
	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HeartboundChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide() ? createTickerHelper(type, SpectrumBlockEntities.HEARTBOUND_CHEST.get(), HeartboundChestBlockEntity::clientTick) : null;
	}
	
	@Override
	public void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
			
			if (!heartboundChestBlockEntity.hasOwner()) {
				heartboundChestBlockEntity.setOwner(player);
			}
			
			if (!isChestBlocked(world, pos)) {
				// Permissions are handled with vanilla lock()
				// => BlockEntities "checkUnlocked" method
				player.openMenu(heartboundChestBlockEntity);
			}
		}
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
			if (placer instanceof ServerPlayer serverPlayerEntity) {
				heartboundChestBlockEntity.setOwner(serverPlayerEntity);
			}
		}
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}
	
	/*
	The chest emits redstone power of strength...
	7: if the owner has it opened
	15: if it was tried to open by a non-owner in the last 20 ticks
	 */
	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof HeartboundChestBlockEntity) {
			if (((HeartboundChestBlockEntity) blockEntity).wasRecentlyTriedToOpenByNonOwner()) {
				return 15;
			}
			int lookingPlayers = HeartboundChestBlockEntity.getPlayersLookingInChestCount(world, pos);
			if (lookingPlayers > 0) {
				return 7;
			}
		}
		return 0;
	}
	
	@Override
	public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return direction == Direction.UP ? state.getSignal(world, pos, direction) : 0;
	}
	
	/*
	Only the chest owner may break it
	 */
	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		
		if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
			if (heartboundChestBlockEntity.canBreak(player.getUUID())) {
				float hardness = 20.0F;
				int i = player.hasCorrectToolForDrops(state) ? 30 : 100;
				return player.getDestroySpeed(state) / hardness / (float) i;
			}
		}
		return -1;
	}
	
	@Override
	public float defaultDestroyTime() {
		return -1;
	}
	
	
}
