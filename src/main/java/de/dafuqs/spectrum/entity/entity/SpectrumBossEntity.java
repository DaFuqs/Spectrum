package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumBossEntity extends PathAwareEntity {
	
	private static final TrackedData<Integer> INVINCIBILITY_TICKS = DataTracker.registerData(SpectrumBossEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private final ServerBossBar bossBar;
	
	protected SpectrumBossEntity(EntityType<? extends SpectrumBossEntity> entityType, World world) {
		super(entityType, world);
		this.bossBar = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
		this.setHealth(this.getMaxHealth());
	}
	
	public static boolean isRealPlayer(Entity entity) {
		// this should filter out most fake players (kibe, FAPI)
		return entity instanceof PlayerEntity && entity.getClass().getCanonicalName().startsWith("net.minecraft");
	}
	
	public boolean hasInvincibilityTicks() {
		return this.dataTracker.get(INVINCIBILITY_TICKS) > 0;
	}
	
	public void setInvincibilityTicks(int ticks) {
		this.dataTracker.set(INVINCIBILITY_TICKS, ticks);
	}
	
	public void tickInvincibility() {
		dataTracker.set(INVINCIBILITY_TICKS, Math.max(0, this.dataTracker.get(INVINCIBILITY_TICKS) - 1));
	}
	
	@Override
	public boolean canHit() {
		return super.canHit() && !hasInvincibilityTicks();
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(INVINCIBILITY_TICKS, 0);
	}
	
	@Override
	public boolean shouldRender(double distance) {
		return true;
	}
	
	@Override
	public boolean canTarget(EntityType<?> type) {
		return true;
	}
	
	@Override
	protected void applyDamage(DamageSource source, float amount) {
		// when damage was dealt
		Entity dealer = source.getAttacker();
		if (!hasInvincibilityTicks() && dealer instanceof PlayerEntity && isRealPlayer(dealer)) {
			super.applyDamage(source, amount);
			this.setInvincibilityTicks(20);
		}
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (this.hasCustomName()) {
			this.bossBar.setName(this.getDisplayName());
		}
	}
	
	@Override
	public void onDeath(DamageSource damageSource) {
		super.onDeath(damageSource);
		
		// grant the kill to all players that attacked recently
		// => should they battle in a team the kill counts for all players
		// instead of just the one that did the killing blow like in vanilla
		List<DamageRecord> recentDamage = ((DamageTrackerAccessor) this.getDamageTracker()).getRecentDamage();
		for (DamageRecord damageRecord : recentDamage) {
			if (damageRecord.getAttacker() instanceof ServerPlayerEntity player) {
				Criteria.ENTITY_KILLED_PLAYER.trigger(player, this, damageSource);
			}
		}
	}
	
	@Override
	public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
		return true;
	}
	
	@Override
	public boolean cannotDespawn() {
		return true;
	}
	
	@Override
	protected boolean isDisallowedInPeaceful() {
		return false;
	}
	
	@Override
	public void setCustomName(@Nullable Text name) {
		super.setCustomName(name);
		this.bossBar.setName(this.getDisplayName());
	}
	
	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		this.bossBar.addPlayer(player);
	}
	
	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		this.bossBar.removePlayer(player);
	}
	
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}
	
	@Override
	public void checkDespawn() {
		if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
			this.discard();
		} else {
			this.despawnCounter = 0;
		}
	}
	
	@Override
	protected void mobTick() {
		super.mobTick();
		this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
	}
	
	@Override
	protected float getSoundVolume() {
		return 4.0F;
	}
	
	@Override
	public boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source) {
		return false;
	}
	
	@Override
	protected boolean canStartRiding(Entity entity) {
		return false;
	}
	
	@Override
	public boolean canUsePortals() {
		return false;
	}
	
	@Override
	public boolean canTarget(LivingEntity target) {
		return target.canTakeDamage();
	}
	
	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_ENDER_DRAGON_HURT;
	}
	
	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_HOSTILE_SWIM;
	}
	
	@Override
	protected SoundEvent getSplashSound() {
		return SoundEvents.ENTITY_HOSTILE_SPLASH;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_HOSTILE_DEATH;
	}
	
	@Override
	public LivingEntity.FallSounds getFallSounds() {
		return new LivingEntity.FallSounds(SoundEvents.ENTITY_HOSTILE_SMALL_FALL, SoundEvents.ENTITY_HOSTILE_BIG_FALL);
	}
	
	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}
	
	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}
	
	@Override
	public void slowMovement(BlockState state, Vec3d multiplier) {
	}
	
	@Override
	public boolean shouldDropXp() {
		return true;
	}
	
	@Override
	protected boolean shouldDropLoot() {
		return true;
	}
	
}
