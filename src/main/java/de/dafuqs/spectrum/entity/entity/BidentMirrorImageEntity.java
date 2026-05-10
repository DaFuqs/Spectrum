package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.magic.*;
import net.minecraft.sounds.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import javax.annotation.*;

public class BidentMirrorImageEntity extends BidentBaseEntity {
	public BidentMirrorImageEntity(Level world) {
		this(SpectrumEntityTypes.BIDENT_MIRROR_IMAGE.get(), world);
	}
	
	public BidentMirrorImageEntity(EntityType<? extends ThrownTrident> entityType, Level world) {
		super(entityType, world);
		this.pickup = Pickup.DISALLOWED;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.level().addParticle(SpectrumParticleTypes.MIRROR_IMAGE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		Level world = this.level();
		world.playSound(null, entityHitResult.getEntity().blockPosition(), SpectrumSoundEvents.MEDIUM_CRYSTAL_RING, SoundSource.PLAYERS, 1.334F, 0.9F + random.nextFloat() * 0.334F);
		world.playSound(null, entityHitResult.getEntity().blockPosition(), SpectrumSoundEvents.SHATTER_HEAVY, SoundSource.PLAYERS, 0.75F, 1.0F + random.nextFloat() * 0.2F);
		MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 1);
		if (!world.isClientSide()) {
			processHit(entityHitResult.getEntity(), 1F);
		}
		this.discard();
	}
	
	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		Level world = this.level();
		world.playSound(null, blockHitResult.getBlockPos(), SpectrumSoundEvents.SHATTER_HEAVY, SoundSource.PLAYERS, 0.75F, 1.0F);
		MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 1);
		if (!world.isClientSide()) {
			processHit(null, 0.667F);
		}
		this.discard();
	}
	
	private void processHit(@Nullable Entity target, float effectMult) {
		var drm = level().registryAccess();
		var stack = getPickupItemStackOrigin();
		var power = SpectrumEnchantmentHelper.getLevel(drm, Enchantments.POWER, stack) * 0.3F + 1;
		var efficiency = SpectrumEnchantmentHelper.getLevel(drm, Enchantments.EFFICIENCY, stack);
		var world = this.level();
		var user = getOwner() instanceof LivingEntity livingOwner ? livingOwner : null;
		
		LightShardEntity.summonBarrage(world, user, this.position(), target instanceof LivingEntity livingEntity ? livingEntity : null, livingEntity -> livingEntity != user, UniformInt.of(5, 8 + 2 * efficiency),
				() -> new LightShardEntity(world, user, effectMult * power, 200 + 40 * efficiency / effectMult));
	}
	
}
