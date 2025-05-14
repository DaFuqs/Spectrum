package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.ticks.*;
import org.jetbrains.annotations.*;

public class RedstoneTimerBlock extends DiodeBlock {
	
	public static final MapCodec<RedstoneTimerBlock> CODEC = simpleCodec(RedstoneTimerBlock::new);
	
	public static final EnumProperty<TimingStep> ACTIVE_TIME = EnumProperty.create("active_time", TimingStep.class);
	public static final EnumProperty<TimingStep> INACTIVE_TIME = EnumProperty.create("inactive_time", TimingStep.class);
	
	public RedstoneTimerBlock(BlockBehaviour.Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(ACTIVE_TIME, TimingStep.ONE_SECOND).setValue(INACTIVE_TIME, TimingStep.ONE_SECOND));
	}
	
	@Override
	public MapCodec<? extends RedstoneTimerBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected int getDelay(BlockState state) {
		if (state.getValue(POWERED)) {
			return state.getValue(ACTIVE_TIME).ticks;
		} else {
			return state.getValue(INACTIVE_TIME).ticks;
		}
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, @NotNull Level world, BlockPos pos, @NotNull Player player, BlockHitResult hit) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		} else {
			if (world.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				stepTiming((ServerLevel) world, pos, (ServerPlayer) player);
				return InteractionResult.CONSUME;
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, ACTIVE_TIME, INACTIVE_TIME);
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onPlace(state, world, pos, oldState, notify);
		if (world instanceof ServerLevel serverWorld) {
			// remove currently scheduled ticks at the blocks position
			// and schedule new ticks
			serverWorld.getBlockTicks().clearArea(new BoundingBox(pos));
			serverWorld.scheduleTick(pos, state.getBlock(), getDelay(state));
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		BlockState newState = state.setValue(POWERED, !state.getValue(POWERED));
		world.setBlock(pos, newState, 3);
		world.playSound(null, pos, SpectrumSoundEvents.REDSTONE_MECHANISM_TRIGGER, SoundSource.BLOCKS, 0.3F, 1.0F);
		world.scheduleTick(pos, this, this.getDelay(state), TickPriority.NORMAL);
	}
	
	@Override
	protected int getInputSignal(Level world, BlockPos pos, BlockState state) {
		return world.getBlockState(pos).getValue(POWERED) ? 15 : 0;
	}
	
	@Override
	protected void checkTickOnNeighbor(Level world, BlockPos pos, BlockState state) {
		boolean bl = state.getValue(POWERED);
		if (!world.getBlockTicks().willTickThisTick(pos, this)) {
			TickPriority tickPriority = TickPriority.HIGH;
			if (this.shouldPrioritize(world, pos, state)) {
				tickPriority = TickPriority.EXTREMELY_HIGH;
			} else if (bl) {
				tickPriority = TickPriority.VERY_HIGH;
			}
			world.scheduleTick(pos, this, this.getDelay(state), tickPriority);
		}
	}
	
	public void stepTiming(ServerLevel world, BlockPos pos, ServerPlayer serverPlayerEntity) {
		if (serverPlayerEntity != null) {
			BlockState blockState = world.getBlockState(pos);
			if (serverPlayerEntity.isShiftKeyDown()) {
				// toggle inactive time
				TimingStep newStep = blockState.getValue(INACTIVE_TIME).next();
				serverPlayerEntity.displayClientMessage(Component.translatable("block.spectrum.redstone_timer.setting.inactive").append(Component.translatable(newStep.localizationString)), true);
				float pitch = 0.5F + newStep.ordinal() * 0.05F;
				world.playSound(null, pos, SpectrumSoundEvents.REDSTONE_MECHANISM_TRIGGER, SoundSource.BLOCKS, 0.3F, pitch);
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(INACTIVE_TIME, newStep));
			} else {
				// toggle active time
				TimingStep newStep = blockState.getValue(ACTIVE_TIME).next();
				serverPlayerEntity.displayClientMessage(Component.translatable("block.spectrum.redstone_timer.setting.active").append(Component.translatable(newStep.localizationString)), true);
				float pitch = 0.5F + newStep.ordinal() * 0.05F;
				world.playSound(null, pos, SpectrumSoundEvents.REDSTONE_MECHANISM_TRIGGER, SoundSource.BLOCKS, 0.3F, pitch);
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ACTIVE_TIME, newStep));
			}
			
			world.getBlockTicks().clearArea(new BoundingBox(pos)); // remove currently scheduled ticks at the blocks position
			BlockState state = world.getBlockState(pos);
			checkTickOnNeighbor(world, pos, state);
		}
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(POWERED)) {
			Direction direction = state.getValue(FACING);
			double x = (double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			double y = (double) pos.getY() + 0.4D + (random.nextDouble() - 0.5D) * 0.2D;
			double z = (double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			float g = -0.3F;
			double xOffset = (g * (float) direction.getStepX());
			double zOffset = (g * (float) direction.getStepZ());
			world.addParticle(DustParticleOptions.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}
	
	public enum TimingStep implements StringRepresentable {
		FOUR_TICKS("four_ticks", 4, "block.spectrum.redstone_timer.setting.four_ticks"),
		ONE_SECOND("one_second", 20, "block.spectrum.redstone_timer.setting.one_second"),
		TEN_SECONDS("ten_seconds", 10 * 20, "block.spectrum.redstone_timer.setting.ten_seconds"),
		ONE_MINUTE("one_minute", 60 * 20, "block.spectrum.redstone_timer.setting.one_minute"),
		TEN_MINUTES("ten_minutes", 60 * 20 * 10, "block.spectrum.redstone_timer.setting.ten_minutes");
		
		public final int ticks;
		public final String localizationString;
		private final String name;
		
		TimingStep(String name, int ticks, String localizationString) {
			this.name = name;
			this.ticks = ticks;
			this.localizationString = localizationString;
		}
		
		public TimingStep next() {
			return values()[(this.ordinal() + 1) % values().length];
		}
		
		@Override
		public String getSerializedName() {
			return name;
		}
	}
	
}
