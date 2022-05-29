package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface JadeVine {
	
	BooleanProperty DEAD = BooleanProperty.of("dead");
	VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	Identifier PETAL_HARVESTING_LOOT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "dynamic/jade_vine_petal_harvesting");
	Identifier NECTAR_HARVESTING_LOOT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "dynamic/jade_vine_nectar_harvesting");
	
	static void spawnParticles(World world, BlockPos blockPos) {
		Random random = world.random;
		double x = blockPos.getX() + 0.2 + (random.nextFloat() * 0.6);
		double y = blockPos.getY() + 0.2 + (random.nextFloat() * 0.6);
		double z = blockPos.getZ() + 0.2 + (random.nextFloat() * 0.6);
		
		double velX = 0.06 - random.nextFloat() * 0.12;
		double velY = 0.06 - random.nextFloat() * 0.12;
		double velZ = 0.06 - random.nextFloat() * 0.12;
		
		world.addParticle(SpectrumParticleTypes.JADE_VINES, x, y, z, velX, velY, velZ);
	}
	
	static boolean doesDie(@NotNull World world, @NotNull BlockPos blockPos) {
		return world.getLightLevel(LightType.SKY, blockPos) > 8 && TimeHelper.isBrightSunlight(world);
	}
	
	void setToAge(World world, BlockPos blockPos, int age);
	
}
