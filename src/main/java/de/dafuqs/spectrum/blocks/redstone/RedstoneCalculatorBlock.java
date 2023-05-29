package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.tick.*;
import org.jetbrains.annotations.*;

public class RedstoneCalculatorBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider {
	
	public static final EnumProperty<CalculationMode> CALCULATION_MODE = EnumProperty.of("calculation_mode", CalculationMode.class);
	
	public RedstoneCalculatorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(CALCULATION_MODE, CalculationMode.ADDITION));
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneCalculatorBlockEntity(pos, state);
	}
	
	@Override
	protected int getUpdateDelayInternal(BlockState state) {
		return 2;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, CALCULATION_MODE);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getAbilities().allowModifyWorld) {
			return ActionResult.PASS;
		} else {
			BlockState newModeState = state.cycle(CALCULATION_MODE);
			world.setBlockState(pos, newModeState, Block.NOTIFY_ALL);
			float pitch = 0.5F + state.get(CALCULATION_MODE).ordinal() * 0.05F;
			world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, pitch);
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				// since this triggers both on server and client side: just send the
				// message once, client side is enough, since it is pretty irrelevant on the server
				serverPlayerEntity.sendMessage(Text.translatable("block.spectrum.redstone_calculator.mode_set").append(Text.translatable(newModeState.get(CALCULATION_MODE).localizationString)), false);
			}
			this.updatePowered(world, pos, newModeState);
			return ActionResult.success(world.isClient);
		}
	}
	
	@Override
	public void updatePowered(World world, BlockPos pos, BlockState state) {
		int newSignal = Math.max(0, Math.min(15, this.calculateOutputSignal(world, pos, state)));
		BlockEntity blockEntity = world.getBlockEntity(pos);
		int lastSignal = 0;
		if (blockEntity instanceof RedstoneCalculatorBlockEntity redstoneCalculatorBlockEntity) {
			lastSignal = redstoneCalculatorBlockEntity.getOutputSignal();
			redstoneCalculatorBlockEntity.setOutputSignal(newSignal);
		}
		
		if (lastSignal != newSignal) {
			if (newSignal == 0) {
				world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
			} else {
				world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
			}
			
			this.updateTarget(world, pos, state);
		}
	}
	
	private int calculateOutputSignal(World world, BlockPos pos, BlockState state) {
		int power = this.getPower(world, pos, state);
		int powerSides = this.getMaxInputLevelSides(world, pos, state);
		
		switch (state.get(CALCULATION_MODE)) {
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
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		boolean bl = state.get(POWERED);
		boolean bl2 = this.hasPower(world, pos, state);
		if (bl && !bl2) {
			world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
		} else if (!bl) {
			world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
			if (!bl2) {
				world.scheduleBlockTick(pos, this, this.getUpdateDelayInternal(state), TickPriority.VERY_HIGH);
			}
		}
	}
	
	/**
	 * The block entity caches the output signal for performance
	 */
	@Override
	protected int getOutputLevel(@NotNull BlockView world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof RedstoneCalculatorBlockEntity ? ((RedstoneCalculatorBlockEntity) blockEntity).getOutputSignal() : 0;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = SpectrumBlocks.REDSTONE_CALCULATOR.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
		int signal = calculateOutputSignal(ctx.getWorld(), ctx.getBlockPos(), state);
		if (signal == 0) {
			return state;
		} else {
			return state.with(POWERED, true);
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (!world.isClient) {
			updatePowered(world, pos, state);
		}
	}
	
	public enum CalculationMode implements StringIdentifiable {
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
		public String asString() {
			return this.name;
		}
	}
	
}
