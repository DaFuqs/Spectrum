package de.dafuqs.spectrum.entity.spawners;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;

public class MonstrositySpawner implements CustomSpawner {
	
	public static final MonstrositySpawner INSTANCE = new MonstrositySpawner();
	public static final float SPAWN_CHANCE = 0.001F;
	
	private MonstrositySpawner() {
	}
	
	@Override
	public int tick(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals) {
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
		for (Player playerEntity : world.getEntities(EntityType.PLAYER, player -> player.isAlive() && player.getY() < player.level().getMaxBuildHeight() - 64 && MonstrosityEntity.ENTITY_TARGETS.test(player))) {
			// a monstrosity should spawn for the player
			// do we already have one? If no create one
			if (MonstrosityEntity.theOneAndOnly == null) {
				MonstrosityEntity monstrosity = SpectrumEntityTypes.MONSTROSITY.create(world);
				DifficultyInstance localDifficulty = world.getCurrentDifficultyAt(playerEntity.blockPosition());
				monstrosity.finalizeSpawn(world, localDifficulty, MobSpawnType.NATURAL, null);
				world.addFreshEntityWithPassengers(monstrosity);
			}
			
			MonstrosityEntity.theOneAndOnly.setTarget(playerEntity);
			MonstrosityEntity.theOneAndOnly.moveTo(playerEntity.blockPosition(), 0.0F, 0.0F);
			MonstrosityEntity.theOneAndOnly.playAmbientSound();
			
			return 1;
		}
		
		return 0;
	}
	
}
