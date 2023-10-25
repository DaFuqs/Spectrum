package de.dafuqs.spectrum.entity.spawners;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
import net.minecraft.world.*;
import net.minecraft.world.spawner.*;

public class MonstrositySpawner implements Spawner {
	
	public static final MonstrositySpawner INSTANCE = new MonstrositySpawner();
	public static final float SPAWN_CHANCE = 0.001F;
	
	private MonstrositySpawner() {
	}
	
	@Override
	public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
		// if we already have a Monstrosity that has a valid target
		// If that is true, let that one do its thing
		if (MonstrosityEntity.theOneAndOnly != null && MonstrosityEntity.theOneAndOnly.hasValidTarget()) {
			return 0;
		}
		// chance to spawn
		// we calculate that before the entity query,
		// since that one is much more computationally expensive
		if (world.getRandom().nextFloat() > SPAWN_CHANCE) {
			return 0;
		}
		
		// Iterate all players in the dimension and test if any of them
		// are able to lure the monstrosity to them
		for (PlayerEntity playerEntity : world.getEntitiesByType(EntityType.PLAYER, player -> player.isAlive() && MonstrosityEntity.ENTITY_TARGETS.test(player))) {
			// a monstrosity should spawn for the player
			// do we already have one? If no create one
			if (MonstrosityEntity.theOneAndOnly == null) {
				MonstrosityEntity monstrosity = SpectrumEntityTypes.MONSTROSITY.create(world);
				LocalDifficulty localDifficulty = world.getLocalDifficulty(playerEntity.getBlockPos());
				monstrosity.initialize(world, localDifficulty, SpawnReason.NATURAL, null, null);
				world.spawnEntityAndPassengers(monstrosity);
			}
			
			MonstrosityEntity.theOneAndOnly.setTarget(playerEntity);
			MonstrosityEntity.theOneAndOnly.refreshPositionAndAngles(playerEntity.getBlockPos(), 0.0F, 0.0F);
			MonstrosityEntity.theOneAndOnly.playAmbientSound();
			
			return 1;
		}
		
		return 0;
	}
	
}
