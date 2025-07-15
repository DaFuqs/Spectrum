package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class RottenGroundBlock extends MudBlock {
	
	public static final MapCodec<RottenGroundBlock> CODEC = simpleCodec(RottenGroundBlock::new);
	
	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 13, 16);
	
	public RottenGroundBlock(Properties settings) {
		super(settings);
	}

//    @Override
//    public MapCodec<? extends RottenGroundBlock> getCodec() {
//        //TODO: Make the codec
//        return CODEC;
//    }
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
}
