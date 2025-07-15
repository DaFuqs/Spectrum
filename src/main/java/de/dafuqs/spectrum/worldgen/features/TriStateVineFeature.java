package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.feature.*;

public class TriStateVineFeature extends Feature<TriStateVineFeatureConfig> {
	
	public TriStateVineFeature(Codec<TriStateVineFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<TriStateVineFeatureConfig> context) {
		var world = context.level();
		var origin = context.origin();
		var random = context.random();
		var chunkGen = context.chunkGenerator();
		var config = context.config();
		
		var floorState = world.getBlockState(origin.above());
		
		if (!(floorState.is(BlockTags.DIRT) || floorState.is(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN)))
			return false;
		
		var berryChance = config.berryChance();
		var vineBlock = config.vineBlock();
		
		if (!(vineBlock instanceof TriStateVineBlock))
			throw new IllegalStateException("TriStateVineFeatures must use TriStateVineBlocks!");
		
		if (berryChance > 0 && !vineBlock.defaultBlockState().hasProperty(BlockStateProperties.BERRIES))
			throw new IllegalStateException("Attempted to generate fruits for a vine with no fruiting state!");
		
		var minHeight = config.minHeight().sample(random);
		var overgrowth = config.overgrowth().sample(random);
		
		// try out how far we can grow
		var stemHeight = 0;
		BlockPos.MutableBlockPos mutablePos = origin.mutable();
		while (stemHeight < minHeight * 3) {
			mutablePos.move(Direction.DOWN);
			
			if (mutablePos.getY() < chunkGen.getMinY() || !isReplaceable(world, mutablePos))
				break;
			
			if (stemHeight > minHeight && random.nextFloat() > overgrowth)
				break;
			
			stemHeight++;
		}
		
		if (stemHeight <= config.cutoff())
			return false;
		
		generateStem(world, random, origin, vineBlock, stemHeight, berryChance);
		return true;
	}
	
	private static boolean isReplaceable(LevelAccessor world, BlockPos pos) {
		return world.getBlockState(pos).isAir();
	}
	
	private void generateStem(LevelAccessor world, RandomSource random, BlockPos origin, Block vineBlock, int stemHeight, float berryChance) {
		var stemPointer = origin.mutable();
		var stemState = vineBlock.defaultBlockState().setValue(TriStateVineBlock.LIFE_STAGE, TriStateVineBlock.LifeStage.STALK);
		
		for (int height = 0; height <= stemHeight; height++) {
			if (height == stemHeight) {
				if (berryChance > 0 && random.nextFloat() <= berryChance) {
					this.setBlock(world, stemPointer, stemState.setValue(TriStateVineBlock.LIFE_STAGE, TriStateVineBlock.LifeStage.MATURE).setValue(BlockStateProperties.BERRIES, true));
				} else {
					this.setBlock(world, stemPointer, stemState.setValue(TriStateVineBlock.LIFE_STAGE, TriStateVineBlock.LifeStage.MATURE));
				}
			} else {
				if (berryChance > 0 && random.nextFloat() <= berryChance) {
					this.setBlock(world, stemPointer, stemState.setValue(BlockStateProperties.BERRIES, true));
				} else {
					this.setBlock(world, stemPointer, stemState);
				}
			}
			
			stemPointer.move(Direction.DOWN);
		}
	}
	
}
