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

public class SpiritVinesBodyBlock extends AbstractPlantBlock implements SpiritVines {
	
	private final BuiltinGemstoneColor gemstoneColor;
	
	public SpiritVinesBodyBlock(Settings settings, BuiltinGemstoneColor gemstoneColor) {
		super(settings, Direction.DOWN, SHAPE, false);
		this.setDefaultState((this.stateManager.getDefaultState()).with(YIELD, YieldType.NONE));
		this.gemstoneColor = gemstoneColor;
	}
	
	@Override
	protected AbstractPlantStemBlock getStem() {
		switch (gemstoneColor) {
			case MAGENTA -> {
				return (AbstractPlantStemBlock) SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_BODY;
			}
			case BLACK -> {
				return (AbstractPlantStemBlock) SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_BODY;
			}
			case CYAN -> {
				return (AbstractPlantStemBlock) SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_BODY;
			}
			case WHITE -> {
				return (AbstractPlantStemBlock) SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_BODY;
			}
			default -> {
				return (AbstractPlantStemBlock) SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_BODY;
			}
		}
	}
	
	@Override
	protected BlockState copyState(BlockState from, BlockState to) {
		return to.with(YIELD, from.get(YIELD));
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
		builder.add(YIELD);
	}
	
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
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
