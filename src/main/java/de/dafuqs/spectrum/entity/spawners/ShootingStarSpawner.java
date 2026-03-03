package de.dafuqs.spectrum.entity.spawners;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class ShootingStarSpawner implements CustomSpawner {
	
	public static final ShootingStarSpawner INSTANCE = new ShootingStarSpawner();
	
	private ShootingStarSpawner() {
	}
	
	@Override
	public int tick(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals) {
		int spawns = 0;
		
		for (Player playerEntity : world.getEntities(EntityType.PLAYER, Entity::isAlive)) {
			if (!playerEntity.isSpectator()
					&& AdvancementHelper.hasAdvancement(playerEntity, SpectrumItems.STAR_FRAGMENT.get().getCloakAdvancementIdentifier())
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
	protected static float getShootingStarChanceWithMultiplier(@NotNull Player playerEntity) {
		int multiplier = 1;
		for (ItemStack handStack : playerEntity.getHandSlots()) {
			if (handStack != null && handStack.is(Items.SPYGLASS)) {
				multiplier += 4;
				break;
			}
		}
		if (playerEntity.hasEffect(MobEffects.NIGHT_VISION)) {
			multiplier++;
		}
		return SpectrumConfig.CONFIG.ShootingStarSpawnChance.get() * multiplier;
	}
	
	public static void spawnShootingStar(ServerLevel serverWorld, @NotNull Player playerEntity) {
		RandomSource rs = serverWorld.getRandom();
		Vec3 spawnPos = playerEntity.position().add(rs.nextIntBetweenInclusive(-48, 48), 200, rs.nextIntBetweenInclusive(-48, 48));
		
		ShootingStarEntity shootingStarEntity = new ShootingStarEntity(serverWorld, spawnPos.x(), spawnPos.y(), spawnPos.z(), ShootingStar.Variant.getWeightedRandomType(rs), false, 3 + serverWorld.random.nextInt(5), false);
		shootingStarEntity.setDeltaMovement(5 - rs.nextFloat() * 10, 0, 5 - rs.nextFloat() * 10);
		serverWorld.addFreshEntity(shootingStarEntity);
	}
	
}
