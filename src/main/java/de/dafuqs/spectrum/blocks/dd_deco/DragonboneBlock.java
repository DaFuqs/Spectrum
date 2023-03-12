package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;

public class DragonboneBlock extends PillarBlock {
	
	public DragonboneBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		world.setBlockState(pos, SpectrumBlocks.CRACKED_DRAGONBONE.getDefaultState());
		if (world.isClient) {
			world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
		}
	}
	
}
