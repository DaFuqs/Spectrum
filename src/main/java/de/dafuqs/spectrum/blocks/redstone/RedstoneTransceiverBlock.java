package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.event.listener.*;
import org.jetbrains.annotations.*;

public class RedstoneTransceiverBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider, ColorableBlock {

	public static final BooleanProperty SENDER = BooleanProperty.of("sender");
	public static final EnumProperty<DyeColor> CHANNEL = EnumProperty.of("channel", DyeColor.class);

	public RedstoneTransceiverBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(SENDER, true).with(CHANNEL, DyeColor.RED));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneTransceiverBlockEntity(pos, state);
	}
	
	@Override
	protected int getUpdateDelayInternal(BlockState state) {
		return 0;
	}
	
	@Override
	public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			if (!tryColorUsingStackInHand(world, pos, player, hand)) {
				toggleSendingMode(world, pos, state);
			}
			return ActionResult.CONSUME;
		}
	}
	
	public void toggleSendingMode(@NotNull World world, BlockPos blockPos, @NotNull BlockState state) {
		BlockState newState = state.with(SENDER, !state.get(SENDER));
		world.setBlockState(blockPos, newState, Block.NOTIFY_LISTENERS);
		
		if (newState.get(SENDER)) {
			world.playSound(null, blockPos, SpectrumSoundEvents.REDSTONE_MECHANISM_TRIGGER, SoundCategory.BLOCKS, 0.3F, 0.9F);
		} else {
			world.playSound(null, blockPos, SpectrumSoundEvents.REDSTONE_MECHANISM_TRIGGER, SoundCategory.BLOCKS, 0.3F, 1.1F);
		}
		updatePowered(world, blockPos, newState);
	}
	
	// Hmmm... my feelings tell me that using channels and sender/receiver as property might be a bit much (16 dye colors) * 2 states plus the already existing states
	// Better move it to the block entity and use a dynamic renderer?
	// ram usage <=> rendering impact
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, SENDER, CHANNEL);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
		return blockEntity instanceof RedstoneTransceiverBlockEntity ? ((RedstoneTransceiverBlockEntity) blockEntity).getEventListener() : null;
	}
	
	@Override
	public void updatePowered(World world, BlockPos pos, BlockState state) {
		int newSignal = world.getReceivedRedstonePower(pos);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof RedstoneTransceiverBlockEntity RedstoneTransceiverBlockEntity) {
			if (state.get(SENDER)) {
				int lastSignal = RedstoneTransceiverBlockEntity.getCurrentSignalStrength();
				if (newSignal != lastSignal) {
					RedstoneTransceiverBlockEntity.setSignalStrength(newSignal);
				}
				
				if (newSignal == 0) {
					world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
				} else {
					world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
				}
			}
		}
	}
	
	/**
	 * The block entity caches the output signal for performance
	 */
	@Override
	protected int getOutputLevel(@NotNull BlockView world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof RedstoneTransceiverBlockEntity ? ((RedstoneTransceiverBlockEntity) blockEntity).getCurrentSignal() : 0;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (!world.isClient) {
			updatePowered(world, pos, state);
		}
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : Support.checkType(type, SpectrumBlockEntities.REDSTONE_TRANSCEIVER, RedstoneTransceiverBlockEntity::serverTick);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(POWERED)) {
			double x = (double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			double y = (double) pos.getY() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			double z = (double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			world.addParticle(DustParticleEffect.DEFAULT, x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean color(World world, BlockPos pos, DyeColor color) {
		BlockState currentState = world.getBlockState(pos);
		if (getColor(currentState) == color) {
			return false;
		}
		world.setBlockState(pos, currentState.with(CHANNEL, color));
		return true;
	}

	@Override
	public DyeColor getColor(BlockState state) {
		return state.get(CHANNEL);
	}

}
