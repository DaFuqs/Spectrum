package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;

public class SpectrumBedBlock extends BedBlock {
	
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0, 3.0, 0.0, 16.0, 11.0, 16.0);
	protected static final VoxelShape NORTH_SHAPE = Shapes.or(TOP_SHAPE, LEG_NORTH_WEST, LEG_NORTH_EAST);
	protected static final VoxelShape SOUTH_SHAPE = Shapes.or(TOP_SHAPE, LEG_SOUTH_WEST, LEG_SOUTH_EAST);
	protected static final VoxelShape WEST_SHAPE = Shapes.or(TOP_SHAPE, LEG_NORTH_WEST, LEG_SOUTH_WEST);
	protected static final VoxelShape EAST_SHAPE = Shapes.or(TOP_SHAPE, LEG_NORTH_EAST, LEG_SOUTH_EAST);
	
	public SpectrumBedBlock(DyeColor color, Properties settings) {
		super(color, settings);
	}

//	@Override
//	public MapCodec<? extends SpectrumBedBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return null;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Direction direction = getConnectedDirection(state).getOpposite();
		return switch (direction) {
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			default -> EAST_SHAPE;
		};
	}
}
