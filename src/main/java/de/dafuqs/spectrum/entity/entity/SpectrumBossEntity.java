package de.dafuqs.spectrum.entity.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class SpectrumBossEntity extends PathAwareEntity {
	
	private final ServerBossBar bossBar;
	
	protected SpectrumBossEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.bossBar = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
		this.setHealth(this.getMaxHealth());
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
	public boolean cannotDespawn() {
		return super.cannotDespawn();
	}
	
	@Override
	protected boolean isDisallowedInPeaceful() {
		return super.isDisallowedInPeaceful();
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
	
	
}
