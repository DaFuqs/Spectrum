package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.ticks.*;
import org.jetbrains.annotations.*;

public class RedstoneCalculatorBlock extends DiodeBlock implements EntityBlock {
	
	public static final MapCodec<RedstoneCalculatorBlock> CODEC = simpleCodec(RedstoneCalculatorBlock::new);
	
	public static final EnumProperty<CalculationMode> CALCULATION_MODE = EnumProperty.create("calculation_mode", CalculationMode.class);
	
	public RedstoneCalculatorBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(CALCULATION_MODE, CalculationMode.ADDITION));
	}

	@Override
	public MapCodec<? extends RedstoneCalculatorBlock> codec() {
		return CODEC;
	}

	@Override
	protected int getDelay(BlockState state) {
		return 2;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, CALCULATION_MODE);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneCalculatorBlockEntity(pos, state);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		} else {
			BlockState newModeState = state.cycle(CALCULATION_MODE);
			world.setBlock(pos, newModeState, Block.UPDATE_ALL);
			float pitch = 0.5F + state.getValue(CALCULATION_MODE).ordinal() * 0.05F;
			world.playSound(player, pos, SpectrumSoundEvents.REDSTONE_MECHANISM_TRIGGER, SoundSource.BLOCKS, 0.3F, pitch);
			if (player instanceof ServerPlayer serverPlayerEntity) {
				// since this triggers both on server and client side: just send the
				// message once, client side is enough, since it is pretty irrelevant on the server
				serverPlayerEntity.displayClientMessage(Component.translatable("block.spectrum.redstone_calculator.mode_set").append(Component.translatable(newModeState.getValue(CALCULATION_MODE).localizationString)), true);
			}

			this.update(world, pos, state);
			
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		this.update(world, pos, state);
	}
	
	private void update(Level world, BlockPos pos, BlockState state) {
		int newSignal = this.calculateOutputSignal(world, pos, state);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		int previousSignal = 0;
		if (blockEntity instanceof RedstoneCalculatorBlockEntity redstoneCalculatorBlockEntity) {
			previousSignal = redstoneCalculatorBlockEntity.getOutputSignal();
			redstoneCalculatorBlockEntity.setOutputSignal(newSignal);
		}
		
		if (previousSignal != newSignal) {
			boolean bl = newSignal != 0;
			boolean bl2 = state.getValue(POWERED);
			if (bl2 && !bl) {
				world.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_CLIENTS);
			} else if (!bl2 && bl) {
				world.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_CLIENTS);
			}
			
			this.updateNeighborsInFront(world, pos, state);
		}
	}
	
	@Override
	protected void checkTickOnNeighbor(Level world, BlockPos pos, BlockState state) {
		if (!world.getBlockTicks().willTickThisTick(pos, this)) {
			int previousSignal = world.getBlockEntity(pos) instanceof RedstoneCalculatorBlockEntity redstoneCalculatorBlockEntity ? redstoneCalculatorBlockEntity.getOutputSignal() : 0;
			int newSignal = this.calculateOutputSignal(world, pos, state);

			if (newSignal != previousSignal) {
				TickPriority tickPriority = this.shouldPrioritize(world, pos, state) ? TickPriority.HIGH : TickPriority.NORMAL;
				world.scheduleTick(pos, this, getDelay(state), tickPriority);
			}
		}
	}
	
	private int calculateOutputSignal(Level world, BlockPos pos, BlockState state) {
		int power = this.getInputSignal(world, pos, state);
		int powerSides = this.getAlternateSignal(world, pos, state);
		
		switch (state.getValue(CALCULATION_MODE)) {
			case ADDITION -> {
				return power + powerSides;
			}
			case SUBTRACTION -> {
				return power - powerSides;
			}
			case MULTIPLICATION -> {
				return power * powerSides;
			}
			case DIVISION -> {
				if (powerSides == 0) {
					return 0;
				} else {
					return power / powerSides;
				}
			}
			case MIN -> {
				return Math.min(power, powerSides);
			}
			case MAX -> {
				return Math.max(power, powerSides);
			}
			default -> {
				if (powerSides == 0) {
					return 0;
				} else {
					return power % powerSides;
				}
			}
		}
	}

	@Override
	protected int getOutputSignal(@NotNull BlockGetter world, BlockPos pos, BlockState state) {
		return world.getBlockEntity(pos) instanceof RedstoneCalculatorBlockEntity redstoneCalculatorBlockEntity ? redstoneCalculatorBlockEntity.getOutputSignal() : 0;
	}
	
	public enum CalculationMode implements StringRepresentable {
		ADDITION("addition", "block.spectrum.redstone_calculator.mode.addition"),
		SUBTRACTION("subtraction", "block.spectrum.redstone_calculator.mode.subtraction"),
		MULTIPLICATION("multiplication", "block.spectrum.redstone_calculator.mode.multiplication"),
		DIVISION("division", "block.spectrum.redstone_calculator.mode.division"),
		MODULO("modulo", "block.spectrum.redstone_calculator.mode.modulo"),
		MIN("min", "block.spectrum.redstone_calculator.mode.min"),
		MAX("max", "block.spectrum.redstone_calculator.mode.max");
		
		public final String localizationString;
		private final String name;
		
		CalculationMode(String name, String localizationString) {
			this.name = name;
			this.localizationString = localizationString;
		}
		
		public String toString() {
			return this.name;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
	
}
