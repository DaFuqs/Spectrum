package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

public class DragonboneBlock extends PillarBlock implements MoonstoneStrikeableBlock {
	
	public DragonboneBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		crack(world, pos);
	}
	
	@Override
	public void onMoonstoneStrike(World world, BlockPos pos, @Nullable LivingEntity striker) {
		crack(world, pos);
	}
	
	public void crack(World world, BlockPos pos) {
		world.setBlockState(pos, SpectrumBlocks.CRACKED_DRAGONBONE.getDefaultState().with(PillarBlock.AXIS, world.getBlockState(pos).get(PillarBlock.AXIS)));
		if (world.isClient) {
			world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
		}
	}
	
	@Override
	public float getBlastResistance() {
		return super.getBlastResistance();
	}
	
}
