package de.dafuqs.spectrum.blocks.deeper_down;

import com.mojang.serialization.MapCodec;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class BlackSludgePlantBlock extends BushBlock {
	
	public static final MapCodec<BlackSludgePlantBlock> CODEC = simpleCodec(BlackSludgePlantBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0);
	
	public BlackSludgePlantBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends BlackSludgePlantBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(SpectrumBlockTags.BLACK_SLUDGE_BLOCKS) || super.mayPlaceOn(floor, world, pos);
	}
	
	@Override
	public float getMaxHorizontalOffset() {
		return 0.08F;
	}
	
}
