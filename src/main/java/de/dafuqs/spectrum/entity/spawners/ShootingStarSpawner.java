package de.dafuqs.spectrum.entity.spawners;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.world.spawner.*;
import org.jetbrains.annotations.*;

public class ShootingStarSpawner implements Spawner {
	
	public static final ShootingStarSpawner INSTANCE = new ShootingStarSpawner();
	
	private ShootingStarSpawner() {
	}
	
	@Override
	public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
		int spawns = 0;
		
		for (PlayerEntity playerEntity : world.getEntitiesByType(EntityType.PLAYER, Entity::isAlive)) {
			if (!playerEntity.isSpectator()
					&& AdvancementHelper.hasAdvancement(playerEntity, SpectrumItems.STAR_FRAGMENT.getCloakAdvancementIdentifier())
					&& world.getRandom().nextFloat() < getShootingStarChanceWithMultiplier(playerEntity)) {
				
				// 1 % chance for each cycle to spawn a lot of shooting stars for the player
				// making it an amazing display
				if (world.getRandom().nextFloat() < 0.01F) {
					for (int i = 0; i < 5; i++) {
						spawnShootingStar(world, playerEntity);
					}
					spawns += 5;
				} else {
					spawnShootingStar(world, playerEntity);
					spawns++;
				}
			}
		}
		
		return spawns;
	}
	
	// If the player explicitly searches for shooting stars give them a small boost :)
	// That these things increase the visibility of shooting stars is explicitly stated
	// in the guidebook, just not that these actually give a boost, too
	protected static float getShootingStarChanceWithMultiplier(@NotNull PlayerEntity playerEntity) {
		int multiplier = 1;
		for (ItemStack handStack : playerEntity.getHandItems()) {
			if (handStack != null && handStack.isOf(Items.SPYGLASS)) {
				multiplier += 4;
				break;
			}
		}
		if (playerEntity.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
			multiplier++;
		}
		return SpectrumCommon.CONFIG.ShootingStarChance * multiplier;
	}
	
	public static void spawnShootingStar(ServerWorld serverWorld, @NotNull PlayerEntity playerEntity) {
		ShootingStarEntity shootingStarEntity = new ShootingStarEntity(serverWorld, playerEntity.getPos().getX(), playerEntity.getPos().getY() + 200, playerEntity.getPos().getZ());
		shootingStarEntity.setVelocity(serverWorld.random.nextDouble() * 0.2D - 0.1D, 0.0D, serverWorld.random.nextDouble() * 0.2D - 0.1D);
		shootingStarEntity.setShootingStarType(ShootingStarBlock.Type.getWeightedRandomType(serverWorld.getRandom()), false, false);
		shootingStarEntity.addVelocity(5 - serverWorld.random.nextFloat() * 10, 0, 5 - serverWorld.random.nextFloat() * 10);
		serverWorld.spawnEntity(shootingStarEntity);
	}
	
}
