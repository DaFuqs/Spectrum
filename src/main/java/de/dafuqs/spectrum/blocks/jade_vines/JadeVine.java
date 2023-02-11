package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface JadeVine {
	
	BooleanProperty DEAD = BooleanProperty.of("dead");
	VoxelShape BULB_SHAPE = Block.createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	VoxelShape TIP_SHAPE = Block.createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	Identifier PETAL_HARVESTING_LOOT_IDENTIFIER = SpectrumCommon.locate("gameplay/jade_vine_petal_harvesting");
	Identifier NECTAR_HARVESTING_LOOT_IDENTIFIER = SpectrumCommon.locate("gameplay/jade_vine_nectar_harvesting");
	
	static void spawnBloomParticlesClient(World world, BlockPos blockPos) {
		spawnParticlesClient(world, blockPos, SpectrumParticleTypes.JADE_VINES_BLOOM);
		
		Random random = world.random;
		double x = blockPos.getX() + 0.2 + (random.nextFloat() * 0.6);
		double y = blockPos.getY() + 0.2 + (random.nextFloat() * 0.6);
		double z = blockPos.getZ() + 0.2 + (random.nextFloat() * 0.6);
		world.addParticle(SpectrumParticleTypes.PINK_FALLING_SPORE_BLOSSOM, x, y, z, 0.0D, 0.0D, 0.0D);
	}
	
	static void spawnParticlesClient(World world, BlockPos blockPos) {
		spawnParticlesClient(world, blockPos, SpectrumParticleTypes.JADE_VINES);
	}
	
	private static void spawnParticlesClient(World world, BlockPos blockPos, ParticleEffect particleType) {
		Random random = world.random;
		double x = blockPos.getX() + 0.2 + (random.nextFloat() * 0.6);
		double y = blockPos.getY() + 0.2 + (random.nextFloat() * 0.6);
		double z = blockPos.getZ() + 0.2 + (random.nextFloat() * 0.6);
		
		double velX = 0.06 - random.nextFloat() * 0.12;
		double velY = 0.06 - random.nextFloat() * 0.12;
		double velZ = 0.06 - random.nextFloat() * 0.12;
		
		world.addParticle(particleType, x, y, z, velX, velY, velZ);
	}
	
	static void spawnParticlesServer(ServerWorld world, BlockPos blockPos, int amount) {
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, Vec3d.ofCenter(blockPos), SpectrumParticleTypes.JADE_VINES, amount, new Vec3d(0.6, 0.6, 0.6), new Vec3d(0.12, 0.12, 0.12));
	}
	
	static boolean isExposedToSunlight(@NotNull World world, @NotNull BlockPos blockPos) {
		return world.getLightLevel(LightType.SKY, blockPos) > 8 && TimeHelper.isBrightSunlight(world);
	}
	
	boolean setToAge(World world, BlockPos blockPos, int age);
	
}
