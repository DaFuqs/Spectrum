package de.dafuqs.spectrum.blocks.flammable;

import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import javax.annotation.*;

public class FlammableFlowerBlock extends FlowerBlock {
	
	public FlammableFlowerBlock(Holder<MobEffect> effect, float seconds, BlockBehaviour.Properties properties) {
		super(effect, seconds, properties);
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 20;
	}
	
}
