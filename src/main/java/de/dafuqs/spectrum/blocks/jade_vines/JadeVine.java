package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public interface JadeVine {
	
	BooleanProperty DEAD = BooleanProperty.create("dead");
	VoxelShape BULB_SHAPE = Block.box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	VoxelShape TIP_SHAPE = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	static void spawnBloomParticlesClient(Level world, BlockPos blockPos) {
		spawnParticlesClient(world, blockPos, SpectrumParticleTypes.JADE_VINES_BLOOM);
		
		RandomSource random = world.random;
		double x = blockPos.getX() + 0.2 + (random.nextFloat() * 0.6);
		double y = blockPos.getY() + 0.2 + (random.nextFloat() * 0.6);
		double z = blockPos.getZ() + 0.2 + (random.nextFloat() * 0.6);
		world.addParticle(ColoredFallingSporeBlossomParticleEffect.PINK, x, y, z, 0.0D, 0.0D, 0.0D);
	}
	
	static void spawnParticlesClient(Level world, BlockPos blockPos) {
		spawnParticlesClient(world, blockPos, SpectrumParticleTypes.JADE_VINES);
	}
	
	private static void spawnParticlesClient(Level world, BlockPos blockPos, ParticleOptions particleType) {
		RandomSource random = world.random;
		double x = blockPos.getX() + 0.2 + (random.nextFloat() * 0.6);
		double y = blockPos.getY() + 0.2 + (random.nextFloat() * 0.6);
		double z = blockPos.getZ() + 0.2 + (random.nextFloat() * 0.6);
		
		double velX = 0.06 - random.nextFloat() * 0.12;
		double velY = 0.06 - random.nextFloat() * 0.12;
		double velZ = 0.06 - random.nextFloat() * 0.12;
		
		world.addParticle(particleType, x, y, z, velX, velY, velZ);
	}
	
	static void spawnParticlesServer(ServerLevel world, BlockPos blockPos, int amount) {
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, Vec3.atCenterOf(blockPos), SpectrumParticleTypes.JADE_VINES, amount, new Vec3(0.6, 0.6, 0.6), new Vec3(0.12, 0.12, 0.12));
	}
	
	static boolean isExposedToSunlight(@NotNull Level world, @NotNull BlockPos blockPos) {
		return world.getBrightness(LightLayer.SKY, blockPos) > 8 && TimeHelper.isBrightSunlight(world);
	}
	
	boolean setToAge(Level world, BlockPos blockPos, int age);
	
}
