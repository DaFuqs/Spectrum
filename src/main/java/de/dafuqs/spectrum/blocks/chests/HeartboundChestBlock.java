package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class HeartboundChestBlock extends SpectrumChestBlock {
	
	public HeartboundChestBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new HeartboundChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? checkType(type, SpectrumBlockEntities.HEARTBOUND_CHEST, HeartboundChestBlockEntity::clientTick) : null;
	}
	
	@Override
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
			
			if (!heartboundChestBlockEntity.hasOwner()) {
				heartboundChestBlockEntity.setOwner(player);
			}
			
			if (!isChestBlocked(world, pos)) {
				// Permissions are handled with vanilla lock()
				// => TileEntities "checkUnlocked" function
				player.openHandledScreen(heartboundChestBlockEntity);
			}
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
				if (placer instanceof ServerPlayerEntity serverPlayerEntity) {
					heartboundChestBlockEntity.setOwner(serverPlayerEntity);
				}
				heartboundChestBlockEntity.setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}
	
	/*
	The chest emits redstone power of strength...
	7: if the owner has it opened
	15: if it was tried to open by a non-owner in the last 20 ticks
	 */
	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
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
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction == Direction.UP ? state.getWeakRedstonePower(world, pos, direction) : 0;
	}
	
	/*
	Only the chest owner may break it
	 */
	@Override
	public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		
		if (blockEntity instanceof HeartboundChestBlockEntity heartboundChestBlockEntity) {
			if (heartboundChestBlockEntity.canBreak(player.getUuid())) {
				float hardness = 20.0F;
				int i = player.canHarvest(state) ? 30 : 100;
				return player.getBlockBreakingSpeed(state) / hardness / (float) i;
			}
		}
		return -1;
	}
	
	@Override
	public float getHardness() {
		return -1;
	}
	
	
}
