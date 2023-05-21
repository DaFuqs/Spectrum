package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class SpiritVinesHeadBlock extends AbstractPlantStemBlock implements SpiritVines {
	
	private final BuiltinGemstoneColor gemstoneColor;
	
	public SpiritVinesHeadBlock(Settings settings, BuiltinGemstoneColor gemstoneColor) {
		super(settings, Direction.DOWN, SHAPE, false, 0.0D);
		this.setDefaultState((this.stateManager.getDefaultState()).with(YIELD, YieldType.NONE));
		this.gemstoneColor = gemstoneColor;
	}
	
	@Override
	protected int getGrowthLength(Random random) {
		return 1;
	}
	
	@Override
	protected boolean chooseStemState(BlockState state) {
		return state.isAir();
	}
	
	@Override
	protected Block getPlant() {
		switch (gemstoneColor) {
			case MAGENTA -> {
				return SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_HEAD;
			}
			case BLACK -> {
				return SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_HEAD;
			}
			case CYAN -> {
				return SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_HEAD;
			}
			case WHITE -> {
				return SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_HEAD;
			}
			default -> {
				return SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_HEAD;
			}
		}
	}
	
	@Override
	protected BlockState copyState(BlockState from, BlockState to) {
		return to.with(YIELD, from.get(YIELD));
	}
	
	@Override
	protected BlockState age(BlockState state, Random random) {
		return super.age(state, random).with(YIELD, YieldType.NONE);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(SpiritVines.getYieldItem(state, true));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return SpiritVines.pick(state, world, pos);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(YIELD);
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}
	
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state.with(YIELD, YieldType.NONE), 2);
	}
}
