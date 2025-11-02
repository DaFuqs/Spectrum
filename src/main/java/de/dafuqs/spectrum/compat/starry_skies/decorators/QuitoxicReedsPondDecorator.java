package de.dafuqs.spectrum.compat.starry_skies.decorators;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.starryskies.*;
import de.dafuqs.starryskies.worldgen.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class QuitoxicReedsPondDecorator extends SphereDecorator<SphereDecoratorConfig.DefaultSphereDecoratorConfig> {
	
	private static final float QUITOXIC_REEDS_CHANCE = 0.25F;
	
	public QuitoxicReedsPondDecorator(Codec<SphereDecoratorConfig.DefaultSphereDecoratorConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean generate(SphereFeatureContext<SphereDecoratorConfig.DefaultSphereDecoratorConfig> context) {
		WorldGenLevel world = context.getWorld();
		PlacedSphere<?> sphere = context.getSphere();
		ChunkPos origin = context.getChunkPos();
		RandomSource random = context.getRandom();
		
		if (!sphere.isCenterInChunk(origin)) {
			return false;
		}
		
		// doesn't make sense on small spheres
		if (sphere.getRadius() > 9) {
			int pondRadius = (int) (sphere.getRadius() / 2.5);
			BlockPos spherePos = sphere.getPosition();
			BlockPos sphereTop = spherePos.above(sphere.getRadius() + 1);
			int waterLevelY = determineWaterY(world, sphereTop, pondRadius);
			
			// if there is not enough room for at least a decent amount of water: don't generate
			if (waterLevelY - sphere.getPosition().getY() < pondRadius * 1.5) {
				return false;
			}
			
			BlockState water = Blocks.WATER.defaultBlockState();
			BlockState air = Blocks.AIR.defaultBlockState();
			BlockState clay = Blocks.CLAY.defaultBlockState();
			
			BlockState quitoxic = SpectrumBlocks.QUITOXIC_REEDS.defaultBlockState();
			BlockState quitoxicWater = quitoxic.setValue(QuitoxicReedsBlock.LOGGED, FluidLogging.State.WATER);
			
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			int pond15 = (int) Math.round(pondRadius * 1.5);
			for (int x = -pond15; x <= pond15; x++) {
				for (int z = -pond15; z <= pond15; z++) {
					mutable.set(spherePos.getX() + x, waterLevelY, spherePos.getZ() + z);
					
					double distance = Support.getDistance(mutable, sphereTop);
					if (distance > pond15) {
						continue;
					}
					
					if (distance < pond15 - 1 && random.nextFloat() < QUITOXIC_REEDS_CHANCE) {
						int quitoxicHeight = random.nextInt(QuitoxicReedsBlock.MAX_GROWTH_HEIGHT_WATER);
						int setBlockHeight = Math.max(waterLevelY + quitoxicHeight, sphereTop.getY());
						
						mutable.set(spherePos.getX() + x, waterLevelY, spherePos.getZ() + z);
						world.setBlock(mutable, quitoxicWater, 3);
						
						for (int y = waterLevelY + 1; y < setBlockHeight; y++) {
							mutable.set(spherePos.getX() + x, y, spherePos.getZ() + z);
							world.setBlock(mutable, quitoxic, 3);
						}
					} else {
						world.setBlock(mutable, water, 3);
						
						for (int y = waterLevelY + 1; y < sphereTop.getY(); y++) {
							mutable.set(spherePos.getX() + x, y, spherePos.getZ() + z);
							world.setBlock(mutable, air, 3);
						}
					}
					
					mutable.set(spherePos.getX() + x, waterLevelY - 1, spherePos.getZ() + z);
					world.setBlock(mutable, clay, 3);
				}
			}
			
		}
		
		return true;
	}
	
	public int determineWaterY(WorldGenLevel world, BlockPos sphereTop, int pondRadius) {
		for (int x = -pondRadius - 1; x <= pondRadius; x++) {
			for (int y = -pondRadius; y < 1; y++) {
				for (int z = -pondRadius - 1; z <= pondRadius; z++) {
					BlockPos currentBlockPos = sphereTop.offset(x, y, z);
					if (world.getBlockState(currentBlockPos).isAir()) {
						return currentBlockPos.getY() - 1;
					}
				}
			}
		}
		return sphereTop.getY();
	}
	
}
