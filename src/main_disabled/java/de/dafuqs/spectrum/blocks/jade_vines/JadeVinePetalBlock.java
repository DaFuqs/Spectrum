package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class JadeVinePetalBlock extends Block {
	
	public static final MapCodec<JadeVinePetalBlock> CODEC = simpleCodec(JadeVinePetalBlock::new);
	
	public JadeVinePetalBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends JadeVinePetalBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.block();
	}
	
	// makes blocks like torches being unable to be placed against it
	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		return Shapes.empty();
	}
	
	@Override
	public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
		return 2;
	}
	
}
