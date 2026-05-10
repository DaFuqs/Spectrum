package de.dafuqs.spectrum.blocks.spirit_sallow;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
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
import org.jspecify.annotations.Nullable;

public class SpiritVinesPlantStemBlock extends GrowingPlantHeadBlock implements SpiritVine {
	public static final MapCodec<SpiritVinesPlantStemBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			SpectrumRegistries.GEMSTONE_COLOR.byNameCodec().fieldOf("color").forGetter(b -> b.gemstoneColor)
	).apply(instance, SpiritVinesPlantStemBlock::new));
	
	private final GemstoneColor gemstoneColor;
	
	public SpiritVinesPlantStemBlock(Properties settings, GemstoneColor gemstoneColor) {
		super(settings, Direction.DOWN, SHAPE, false, 0.0D);
		this.registerDefaultState((this.stateDefinition.any()).setValue(CRYSTALS, false));
		this.gemstoneColor = gemstoneColor;
	}

	@Override
	public MapCodec<? extends SpiritVinesPlantStemBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
		return 1;
	}
	
	@Override
	protected boolean canGrowInto(BlockState state) {
		return state.isAir();
	}
	
	@Override
	protected Block getBodyBlock() {
		return switch (gemstoneColor) {
			case BuiltinGemstoneColor.MAGENTA -> SpectrumBlocks.MAGENTA_SPIRIT_SALLOW_VINES_PLANT;
			case BuiltinGemstoneColor.YELLOW  -> SpectrumBlocks.YELLOW_SPIRIT_SALLOW_VINES_PLANT;
			case BuiltinGemstoneColor.BLACK   -> SpectrumBlocks.BLACK_SPIRIT_SALLOW_VINES_PLANT;
			case BuiltinGemstoneColor.WHITE   -> SpectrumBlocks.WHITE_SPIRIT_SALLOW_VINES_PLANT;
			case BuiltinGemstoneColor.CYAN    -> SpectrumBlocks.CYAN_SPIRIT_SALLOW_VINES_PLANT;
			default -> throw new IllegalStateException("Unknown color: " + gemstoneColor);
		};
	}
	
	@Override
	protected BlockState updateBodyAfterConvertedFromHead(BlockState from, BlockState to) {
		return to.setValue(CRYSTALS, from.getValue(CRYSTALS));
	}
	
	@Override
	protected BlockState getGrowIntoState(BlockState state, RandomSource random) {
		return super.getGrowIntoState(state, random).setValue(CRYSTALS, false);
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
		super.createBlockStateDefinition(builder);
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
