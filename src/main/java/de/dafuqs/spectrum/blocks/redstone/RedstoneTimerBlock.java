package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RedstoneTimerBlock extends AbstractRedstoneGateBlock {
	
	public static EnumProperty<TimingStep> ACTIVE_TIME = EnumProperty.of("active_time", TimingStep.class);
	public static EnumProperty<TimingStep> INACTIVE_TIME = EnumProperty.of("inactive_time", TimingStep.class);
	public RedstoneTimerBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(ACTIVE_TIME, TimingStep.OneSecond).with(INACTIVE_TIME, TimingStep.OneSecond));
	}
	
	@Override
	protected int getUpdateDelayInternal(BlockState state) {
		if (state.get(POWERED)) {
			return state.get(ACTIVE_TIME).ticks;
		} else {
			return state.get(INACTIVE_TIME).ticks;
		}
	}
	
	public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, @NotNull PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getAbilities().allowModifyWorld) {
			return ActionResult.PASS;
		} else {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				stepTiming((ServerWorld) world, pos, (ServerPlayerEntity) player);
				return ActionResult.CONSUME;
			}
		}
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, ACTIVE_TIME, INACTIVE_TIME);
	}
	
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (world instanceof ServerWorld serverWorld) {
			// remove currently scheduled ticks at the blocks position
			// and schedule new ticks
			serverWorld.getBlockTickScheduler().clearNextTicks(new BlockBox(pos));
			serverWorld.createAndScheduleBlockTick(pos, state.getBlock(), getUpdateDelayInternal(state));
		}
	}
	
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockState newState = state.with(POWERED, !state.get(POWERED));
		world.setBlockState(pos, newState, 3);
		world.playSound(null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, 1.0F);
		world.createAndScheduleBlockTick(pos, this, this.getUpdateDelayInternal(state), TickPriority.NORMAL);
	}
	
	protected int getPower(World world, BlockPos pos, BlockState state) {
		return world.getBlockState(pos).get(POWERED) ? 15 : 0;
	}
	
	protected void updatePowered(World world, BlockPos pos, BlockState state) {
		boolean bl = state.get(POWERED);
		if (!world.getBlockTickScheduler().isTicking(pos, this)) {
			TickPriority tickPriority = TickPriority.HIGH;
			if (this.isTargetNotAligned(world, pos, state)) {
				tickPriority = TickPriority.EXTREMELY_HIGH;
			} else if (bl) {
				tickPriority = TickPriority.VERY_HIGH;
			}
			world.createAndScheduleBlockTick(pos, this, this.getUpdateDelayInternal(state), tickPriority);
		}
	}
	
	public void stepTiming(ServerWorld world, BlockPos pos, ServerPlayerEntity serverPlayerEntity) {
		if (serverPlayerEntity != null) {
			BlockState blockState = world.getBlockState(pos);
			if (serverPlayerEntity.isSneaking()) {
				// toggle inactive time
				TimingStep newStep = blockState.get(INACTIVE_TIME).next();
				serverPlayerEntity.sendMessage(new TranslatableText("block.spectrum.redstone_timer.setting.inactive").append(new TranslatableText(newStep.localizationString)), false);
				float pitch = 0.5F + newStep.ordinal() * 0.05F;
				world.playSound(null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, pitch);
				world.setBlockState(pos, world.getBlockState(pos).with(INACTIVE_TIME, newStep));
			} else {
				// toggle active time
				TimingStep newStep = blockState.get(ACTIVE_TIME).next();
				serverPlayerEntity.sendMessage(new TranslatableText("block.spectrum.redstone_timer.setting.active").append(new TranslatableText(newStep.localizationString)), false);
				float pitch = 0.5F + newStep.ordinal() * 0.05F;
				world.playSound(null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, pitch);
				world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE_TIME, newStep));
			}
			
			world.getBlockTickScheduler().clearNextTicks(new BlockBox(pos)); // remove currently scheduled ticks at the blocks position
			BlockState state = world.getBlockState(pos);
			updatePowered(world, pos, state);
		}
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(POWERED)) {
			Direction direction = state.get(FACING);
			double x = (double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			double y = (double) pos.getY() + 0.4D + (random.nextDouble() - 0.5D) * 0.2D;
			double z = (double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
			float g = -0.3F;
			double xOffset = (g * (float) direction.getOffsetX());
			double zOffset = (g * (float) direction.getOffsetZ());
			world.addParticle(DustParticleEffect.DEFAULT, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}
	
	public enum TimingStep implements StringIdentifiable {
		OneSecond("one_second", 20, "block.spectrum.redstone_timer.setting.one_second"),
		TenSeconds("ten_seconds", 10 * 20, "block.spectrum.redstone_timer.setting.ten_seconds"),
		OneMinute("one_minute", 60 * 20, "block.spectrum.redstone_timer.setting.one_minute"),
		TenMinutes("ten_minutes", 60 * 20 * 10, "block.spectrum.redstone_timer.setting.ten_minutes"),
		OneHour("one_hour", 60 * 60 * 20, "block.spectrum.redstone_timer.setting.one_hour");
		
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
		public String asString() {
			return name;
		}
	}
	
}
