package de.dafuqs.spectrum.blocks.conditional.amaranth;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.id.incubus_core.block.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AmaranthCropBlock extends TallCropBlock implements RevelationAware {
	
	public static final Identifier ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("milestones/reveal_amaranth");
	protected static final int LAST_SINGLE_BLOCK_AGE = 2;
	protected static final int MAX_AGE = 7;
	
	private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)
	};
	
	public AmaranthCropBlock(Settings settings) {
		super(settings, LAST_SINGLE_BLOCK_AGE);
		RevelationAware.register(this);
	}
	
	public int getMaxAge() {
		return MAX_AGE;
	}
	
	@Override
	protected ItemConvertible getSeedsItem() {
		return SpectrumItems.AMARANTH_GRAINS;
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		BlockState smallFern = Blocks.FERN.getDefaultState();
		BlockState largeFernLower = Blocks.LARGE_FERN.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER);
		BlockState largeFernUpper = Blocks.LARGE_FERN.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER);
		
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (int age = 0; age <= LAST_SINGLE_BLOCK_AGE; age++) {
			map.put(this.withAge(age), smallFern);
		}
		for (int age = LAST_SINGLE_BLOCK_AGE + 1; age <= MAX_AGE; age++) {
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.LOWER), largeFernLower);
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.UPPER), largeFernUpper);
		}
		return map;
	}
	
	@Override
	public @Nullable Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.LARGE_FERN.asItem());
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
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			if (state.get(AGE) <= this.lastSingleBlockAge) {
				return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
			} else {
				// Fill in the bottom block if the plant is two-tall
				return VoxelShapes.fullCube();
			}
		} else {
			return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
		}
	}
	
}