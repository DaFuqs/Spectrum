package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PrivateChestBlock extends SpectrumChestBlock {
	
	public PrivateChestBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PrivateChestBlockEntity(pos, state);
	}
	
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? checkType(type, SpectrumBlockEntityRegistry.PRIVATE_CHEST, PrivateChestBlockEntity::clientTick) : null;
	}
	
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PrivateChestBlockEntity privateChestBlockEntity) {
			
			if (!privateChestBlockEntity.hasOwner()) {
				privateChestBlockEntity.setOwner(player);
			}
			
			if (!isChestBlocked(world, pos)) {
				// Permissions are handled with vanilla lock()
				// => TileEntities "checkUnlocked" function
				player.openHandledScreen(privateChestBlockEntity);
			}
		}
	}
	
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PrivateChestBlockEntity privateChestBlockEntity) {
				if (placer instanceof ServerPlayerEntity serverPlayerEntity) {
					privateChestBlockEntity.setOwner(serverPlayerEntity);
				}
				privateChestBlockEntity.setCustomName(itemStack.getName());
			}
		}
	}
	
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}
	
	/*
	The chest emits redstone power of strength...
	7: if the owner has it opened
	15: if it was tried to open by a non-owner in the last 20 ticks
	 */
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PrivateChestBlockEntity) {
			if (((PrivateChestBlockEntity) blockEntity).wasRecentlyTriedToOpenByNonOwner()) {
				return 15;
			}
			int lookingPlayers = PrivateChestBlockEntity.getPlayersLookingInChestCount(world, pos);
			if (lookingPlayers > 0) {
				return 7;
			}
		}
		return 0;
	}
	
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction == Direction.UP ? state.getWeakRedstonePower(world, pos, direction) : 0;
	}
	
	/*
	Only the chest owner may break it
	 */
	@Override
	public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		
		if (blockEntity instanceof PrivateChestBlockEntity privateChestBlockEntity) {
			if (privateChestBlockEntity.canBreak(player.getUuid())) {
				return super.calcBlockBreakingDelta(state, player, world, pos);
			}
		}
		return -1;
	}
	
}
