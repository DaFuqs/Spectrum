package de.dafuqs.spectrum.blocks.conditional.amaranth;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AmaranthCropBlock extends TallCropBlock implements RevelationAware {
	
	public static final MapCodec<AmaranthCropBlock> CODEC = simpleCodec(AmaranthCropBlock::new);
	protected static final int LAST_SINGLE_BLOCK_AGE = 2;
	protected static final int MAX_AGE = 7;

	private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)
	};
	
	public AmaranthCropBlock(Properties settings) {
		super(settings, LAST_SINGLE_BLOCK_AGE);
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends AmaranthCropBlock> codec() {
		return CODEC;
	}
	
	@Override
	public int getMaxAge() {
		return MAX_AGE;
	}
	
	@Override
	protected ItemLike getBaseSeedId() {
		return SpectrumItems.AMARANTH_GRAINS;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_AMARANTH;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		BlockState smallFern = Blocks.FERN.defaultBlockState();
		BlockState largeFernLower = Blocks.LARGE_FERN.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER);
		BlockState largeFernUpper = Blocks.LARGE_FERN.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
		
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (int age = 0; age <= LAST_SINGLE_BLOCK_AGE; age++) {
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.LOWER), smallFern);
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.UPPER), smallFern);
		}
		for (int age = LAST_SINGLE_BLOCK_AGE + 1; age <= MAX_AGE; age++) {
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.LOWER), largeFernLower);
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.UPPER), largeFernUpper);
		}
		return map;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.LARGE_FERN.asItem());
	}
	
	@Override
	public void onUncloak() {
		if (SpectrumColorProviders.amaranthCropBlockColorProvider != null && SpectrumColorProviders.amaranthCropItemColorProvider != null) {
			SpectrumColorProviders.amaranthCropBlockColorProvider.setShouldApply(false);
			SpectrumColorProviders.amaranthCropItemColorProvider.setShouldApply(false);
		}
	}
	
	@Override
	public void onCloak() {
		if (SpectrumColorProviders.amaranthCropBlockColorProvider != null && SpectrumColorProviders.amaranthCropItemColorProvider != null) {
			SpectrumColorProviders.amaranthCropBlockColorProvider.setShouldApply(true);
			SpectrumColorProviders.amaranthCropItemColorProvider.setShouldApply(true);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			if (state.getValue(AGE) <= this.lastSingleBlockAge) {
				return AGE_TO_SHAPE[state.getValue(this.getAgeProperty())];
			} else {
				// Fill in the bottom block if the plant is two-tall
				return Shapes.block();
			}
		} else {
			return AGE_TO_SHAPE[state.getValue(this.getAgeProperty())];
		}
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(SpectrumBlockTags.AMARANTH_PLANTABLE);
	}
	
}
