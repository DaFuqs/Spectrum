package de.dafuqs.spectrum.blocks.spirit_sallow;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

public class SpiritVinesPlantBlock extends GrowingPlantBodyBlock implements SpiritVine {
	
	private final GemstoneColor gemstoneColor;
	
	public SpiritVinesPlantBlock(Properties settings, GemstoneColor gemstoneColor) {
		super(settings, Direction.DOWN, SHAPE, false);
		this.registerDefaultState((this.stateDefinition.any()).setValue(CRYSTALS, false));
		this.gemstoneColor = gemstoneColor;
	}

	@Override
	public MapCodec<? extends SpiritVinesPlantBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		switch (gemstoneColor) {
			case BuiltinGemstoneColor.CYAN -> {
				return SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES;
			}
			case BuiltinGemstoneColor.MAGENTA -> {
				return SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES;
			}
			case BuiltinGemstoneColor.YELLOW -> {
				return SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES;
			}
			case BuiltinGemstoneColor.BLACK -> {
				return SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES;
			}
			case BuiltinGemstoneColor.WHITE -> {
				return SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES;
			}
			default -> {
				return null;
			}
		}
	}
	
	@Override
	protected BlockState updateHeadAfterConvertedFromBody(BlockState from, BlockState to) {
		return to.setValue(CRYSTALS, from.getValue(CRYSTALS));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return new ItemStack(SpiritVine.getYieldItem(state));
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return SpiritVine.pick(state, world, pos);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CRYSTALS);
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return false;
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		world.setBlock(pos, state.setValue(CRYSTALS, false), 2);
	}
}
