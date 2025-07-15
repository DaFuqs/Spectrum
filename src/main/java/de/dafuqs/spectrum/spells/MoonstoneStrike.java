package de.dafuqs.spectrum.spells;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MoonstoneStrike {
	
	private final Level world;
	private final double x;
	private final double y;
	private final double z;
	public final @Nullable Entity entity;
	public final float power;
	public final float knockbackMod;
	private final DamageSource damageSource;
	protected final Map<Player, Vec3> affectedPlayers;
	
	public MoonstoneStrike(Level world, @Nullable Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power, float knockbackMod) {
		this.affectedPlayers = Maps.newHashMap();
		this.world = world;
		this.entity = entity;
		this.power = power;
		this.knockbackMod = knockbackMod;
		this.x = x;
		this.y = y;
		this.z = z;
		this.damageSource = damageSource == null ? SpectrumDamageTypes.moonstoneStrike(world, this) : damageSource;
	}
	
	public static void create(Level world, Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power) {
		create(world, entity, damageSource, x, y, z, power, power);
	}
	
	public static void create(Level world, Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power, float knockbackMod) {
		MoonstoneStrike moonstoneStrike = new MoonstoneStrike(world, entity, damageSource, x, y, z, power, knockbackMod);
		
		if (world.isClientSide) {
			world.playLocalSound(x, y, z, SpectrumSoundEvents.MOONSTONE_STRIKE, SoundSource.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
			world.playLocalSound(x, y, z, SpectrumSoundEvents.SOFT_HUM, SoundSource.BLOCKS, 0.5F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
			world.addParticle(SpectrumParticleTypes.MOONSTONE_STRIKE, x, y, z, 1.0, 0.0, 0.0);
		} else {
			moonstoneStrike.damageAndKnockbackEntities();
			MoonstoneBlastPayload.sendMoonstoneBlast((ServerLevel) world, moonstoneStrike);
			moonstoneStrike.affectWorld();
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public float getPower() {
		return power;
	}
	
	public float getKnockbackMod() {
		return knockbackMod;
	}
	
	public DamageSource getDamageSource() {
		return this.damageSource;
	}
	
	public Map<Player, Vec3> getAffectedPlayers() {
		return this.affectedPlayers;
	}
	
	public void damageAndKnockbackEntities() {
		this.world.gameEvent(this.entity, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
		
		float reach = this.power * 2.0F;
		int minX = Mth.floor(this.x - (double) reach - 1.0);
		int maxX = Mth.floor(this.x + (double) reach + 1.0);
		int minY = Mth.floor(this.y - (double) reach - 1.0);
		int maxY = Mth.floor(this.y + (double) reach + 1.0);
		int minZ = Mth.floor(this.z - (double) reach - 1.0);
		int maxZ = Mth.floor(this.z + (double) reach + 1.0);
		Vec3 center = new Vec3(this.x, this.y, this.z);
		
		for (Entity entity : world.getEntities(this.entity, new AABB(minX, minY, minZ, maxX, maxY, maxZ))) {
			//TODO: Can we convert this into an explosion subclass?
			if (!entity.ignoreExplosion(null)) {
				double unitDist = Math.sqrt(entity.distanceToSqr(center)) / (double) reach;
				
				if (unitDist <= 1.0) { // Within a sphere of the explosion
					double dx = entity.getX() - this.x;
					double dy = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
					double dz = entity.getZ() - this.z;
					double dLen = Math.sqrt(dx * dx + dy * dy + dz * dz);
					
					if (dLen != 0.0) { // Don't explode the strike entity itself
						// Now these are a unit vector, the direction from the explosion to the entity's eyes
						dx /= dLen;
						dy /= dLen;
						dz /= dLen;
						
						double scaledExposure = (1.0F - unitDist) * Explosion.getSeenPercent(center, entity);
						entity.hurt(this.damageSource, (float) ((scaledExposure * scaledExposure + scaledExposure) / 2.0 * 7.0 * reach + 1.0));
						
						double knockback = scaledExposure * this.knockbackMod;
						if (entity instanceof LivingEntity livingEntity) {
							knockback *= 1.0 - livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE);
						}
						
						dx *= knockback;
						dy *= knockback;
						dz *= knockback;
						Vec3 impact = new Vec3(dx, dy, dz);
						entity.setDeltaMovement(entity.getDeltaMovement().add(impact));
						if (entity instanceof Player playerEntity) {
							if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
								this.affectedPlayers.put(playerEntity, impact);
							}
						}
						
						entity.onExplosionHit(this.entity);
					}
				}
			}
		}
	}
	
	public void affectWorld() {
		LivingEntity cause = getCausingEntity();
		int range = Math.max(2, (int) this.power / 2);
		for (BlockPos pos : BlockPos.withinManhattan(BlockPos.containing(this.x, this.y, this.z), range, range, range)) {
			BlockState blockState = world.getBlockState(pos);
			Block block = blockState.getBlock();
			if (block instanceof MoonstoneStrikeableBlock moonstoneStrikeableBlock) {
				moonstoneStrikeableBlock.onMoonstoneStrike(world, pos, cause);
			}
		}
	}
	
	public @Nullable LivingEntity getCausingEntity() {
		if (this.entity instanceof LivingEntity livingEntity) {
			return livingEntity;
		} else if (this.entity instanceof Projectile projectileEntity && projectileEntity.getOwner() instanceof LivingEntity livingEntity) {
			return livingEntity;
		}
		return null;
	}
	
}
