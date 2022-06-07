package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class RedstoneWirelessBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider {
	
	public static final BooleanProperty SENDER = BooleanProperty.of("sender");
	public static final EnumProperty<DyeColor> CHANNEL = EnumProperty.of("channel", DyeColor.class);
	
	public RedstoneWirelessBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(SENDER, true).with(CHANNEL, DyeColor.RED));
	}
	
	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneWirelessBlockEntity(pos, state);
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
			ItemStack handStack = player.getStackInHand(hand);
			Optional<DyeColor> itemInHandColor = ColorHelper.getDyeColorOfItemStack(handStack);
			if (itemInHandColor.isPresent()) {
				DyeColor currentChannel = state.get(CHANNEL);
				if (itemInHandColor.get() != currentChannel) {
					world.setBlockState(pos, world.getBlockState(pos).with(CHANNEL, itemInHandColor.get()));
					if (!player.isCreative()) {
						handStack.decrement(1);
					}
				}
			} else {
				toggleSendingMode(world, pos, state);
			}
			return ActionResult.CONSUME;
		}
	}
	
	public void toggleSendingMode(@NotNull World world, BlockPos blockPos, @NotNull BlockState state) {
		BlockState newState = state.with(SENDER, !state.get(SENDER));
		world.setBlockState(blockPos, newState, Block.NOTIFY_LISTENERS);
		
		if (newState.get(SENDER)) {
			world.playSound(null, blockPos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, 0.9F);
		} else {
			world.playSound(null, blockPos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, 1.1F);
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
	public <T extends BlockEntity> GameEventListener getGameEventListener(World world, T blockEntity) {
		return blockEntity instanceof RedstoneWirelessBlockEntity ? ((RedstoneWirelessBlockEntity) blockEntity).getEventListener() : null;
	}
	
	@Override
	public void updatePowered(World world, BlockPos pos, BlockState state) {
		int newSignal = world.getReceivedRedstonePower(pos);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof RedstoneWirelessBlockEntity redstoneWirelessBlockEntity) {
			if (state.get(SENDER)) {
				int lastSignal = redstoneWirelessBlockEntity.getCurrentSignalStrength();
				if (newSignal != lastSignal) {
					redstoneWirelessBlockEntity.setSignalStrength(newSignal);
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
	protected int getOutputLevel(@NotNull BlockView world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof RedstoneWirelessBlockEntity ? ((RedstoneWirelessBlockEntity) blockEntity).getCurrentSignal() : 0;
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
		if (!world.isClient) {
			return checkType(type, SpectrumBlockEntityRegistry.REDSTONE_WIRELESS, RedstoneWirelessBlockEntity::serverTick);
		}
		return null;
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
	
}
