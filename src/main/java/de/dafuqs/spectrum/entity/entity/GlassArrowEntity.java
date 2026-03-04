package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.magic.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class GlassArrowEntity extends AbstractArrow {
	
	private static final String VARIANT_STRING = "variant";
	private static final EntityDataAccessor<GlassArrowVariant> VARIANT = SynchedEntityData.defineId(GlassArrowEntity.class, SpectrumTrackedDataHandlerRegistry.GLASS_ARROW_VARIANT);
	
	public static final float DAMAGE_MODIFIER = 1.5F;
	
	public GlassArrowEntity(EntityType<? extends GlassArrowEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	public GlassArrowEntity(Level world, LivingEntity owner, ItemStack stack, ItemStack shotFrom) {
		super(SpectrumEntityTypes.GLASS_ARROW.get(), owner, world, stack, shotFrom);
		setBaseDamage(getBaseDamage() * DAMAGE_MODIFIER);
	}
	
	public GlassArrowEntity(Level world, double x, double y, double z, ItemStack stack, ItemStack shotFrom) {
		super(SpectrumEntityTypes.GLASS_ARROW.get(), x, y, z, world, stack, shotFrom);
		setBaseDamage(getBaseDamage() * DAMAGE_MODIFIER);
	}
	
	@Override
	public void setBaseDamageFromMob(float damageModifier) {
		super.setBaseDamageFromMob(damageModifier);
		setBaseDamage(getBaseDamage() * DAMAGE_MODIFIER);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			if (!this.inGround || this.level().getGameTime() % 8 == 0) {
				spawnParticles(1);
			}
		}
	}
	
	private void spawnParticles(int amount) {
		ParticleOptions particleType = this.getVariant().getParticleEffect();
		if (particleType != null) {
			for (int j = 0; j < amount; ++j) {
				this.level().addParticle(particleType, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
			}
		}
	}
	
	@Override
	protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
		LivingEntity livingEntityToResetHurtTime = null;
		Level world = this.level();
		
		int invincibilityFrameStore = 0;
		
		// additional effects depending on arrow type
		// mundane glass arrows do not have additional effects
		GlassArrowVariant variant = getVariant();
		if (variant == GlassArrowVariant.TOPAZ) {
			if (!this.level().isClientSide() && this.getOwner() != null) {
				Entity entity = entityHitResult.getEntity();
				pullEntityClose(this.getOwner(), entity, 0.2);
			}
		} else if (variant == GlassArrowVariant.AMETHYST) {
			if (!this.level().isClientSide()) {
				Entity entity = entityHitResult.getEntity();
				entity.setTicksFrozen(200);
			}
		} else if (variant == GlassArrowVariant.ONYX) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof LivingEntity livingEntity) {
				// we're resetting hurtTime here for once so onEntityHit() can deal damage
				// and also resetting after that again so the target is damageable again after this
				invincibilityFrameStore = livingEntity.hurtTime;
				livingEntity.hurtTime = 0;
				livingEntityToResetHurtTime = livingEntity;
				livingEntity.hurtCurrentlyUsedShield(20);
				livingEntity.hurtArmor(world.damageSources().magic(), 20);
			}
		} else if (variant == GlassArrowVariant.MOONSTONE) {
			MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 4);
		}
		
		super.onHitEntity(entityHitResult);
		
		if (livingEntityToResetHurtTime != null) {
			livingEntityToResetHurtTime.hurtTime = invincibilityFrameStore;
		}
		
		this.playSound(SoundEvents.GLASS_BREAK, 0.75F, 0.9F + world.random.nextFloat() * 0.2F);
		this.remove(RemovalReason.DISCARDED);
	}
	
	@Override
	protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		if (getVariant() == GlassArrowVariant.MOONSTONE) {
			MoonstoneStrike.create(this.level(), this, null, this.getX(), this.getY(), this.getZ(), 4);
		}
	}
	
	protected static void pullEntityClose(Entity shooter, Entity entityToPull, double pullStrength) {
		double d = shooter.getX() - entityToPull.getX();
		double e = shooter.getY() - entityToPull.getY();
		double f = shooter.getZ() - entityToPull.getZ();
		
		double pullStrengthModifier = 1.0;
		if (entityToPull instanceof LivingEntity livingEntity) {
			pullStrengthModifier = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
		}
		
		Vec3 additionalVelocity = new Vec3(d * pullStrength, e * pullStrength + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * pullStrength).scale(pullStrengthModifier);
		entityToPull.push(additionalVelocity.x, additionalVelocity.y, additionalVelocity.z);
	}
	
	@Override
	protected @NotNull ItemStack getDefaultPickupItem() {
		return getVariant().getArrow().getDefaultInstance();
	}
	
	/**
	 * Glass Arrows pass through water almost effortlessly
	 */
	@Override
	protected float getWaterInertia() {
		return 0.85F;
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, GlassArrowVariant.MALACHITE);
	}
	
	public GlassArrowVariant getVariant() {
		return this.entityData.get(VARIANT);
	}
	
	@Override
	protected void doKnockback(@NotNull LivingEntity target, @NotNull DamageSource source) {
		double punch = getVariant() == GlassArrowVariant.CITRINE ? 5 : 0;
		punch += this.level() instanceof ServerLevel serverWorld ? EnchantmentHelper.modifyKnockback(serverWorld, getWeaponItem(), target, source, 0.0F) : 0.0F;
		if (punch > 0.0) {
			double e = Math.max(0.0, 1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
			Vec3 vec3d = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(punch * 0.6 * e);
			if (vec3d.lengthSqr() > 0.0) {
				target.push(vec3d.x, 0.1, vec3d.z);
			}
		}
	}
	
	public void setVariant(GlassArrowVariant variant) {
		this.entityData.set(VARIANT, variant);
	}
	
	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putString(VARIANT_STRING, SpectrumRegistries.GLASS_ARROW_VARIANT.getKey(this.getVariant()).toString());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		GlassArrowVariant variant = SpectrumRegistries.GLASS_ARROW_VARIANT.get(ResourceLocation.tryParse(nbt.getString(VARIANT_STRING)));
		if (variant != null) {
			this.setVariant(variant);
		}
	}
	
}
