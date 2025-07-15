package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;
import org.apache.commons.lang3.mutable.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DraconicTwinswordEntity extends BidentBaseEntity implements NonLivingAttackable {
	
	private static final EntityDataAccessor<Boolean> HIT = SynchedEntityData.defineId(DraconicTwinswordEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> PROPELLED = SynchedEntityData.defineId(DraconicTwinswordEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> REBOUND = SynchedEntityData.defineId(DraconicTwinswordEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> MAX_PIERCE = SynchedEntityData.defineId(DraconicTwinswordEntity.class, EntityDataSerializers.INT);
	private static final EntityDimensions initialSize = EntityDimensions.scalable(1.5F, 1.5F);
	private static final EntityDimensions shortSize = EntityDimensions.scalable(1F, 1F);
	private static final EntityDimensions tallSize = EntityDimensions.scalable(1F, 1.8F);
	private final Set<Entity> piercedEntities = new HashSet<>();
	private int travelingTicks = 0, jiggleTicks = 20, jiggleIntensity = 8;
	private float damageMult = 1, velMult = 1;
	
	public DraconicTwinswordEntity(Level world) {
		this(SpectrumEntityTypes.DRACONIC_TWINSWORD, world);
	}
	
	public DraconicTwinswordEntity(EntityType<? extends ThrownTrident> entityType, Level world) {
		super(entityType, world);
	}
	
	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
	public void tick() {
		if (isPropelled() && !isRebounding()) {
			if (travelingTicks < 12) {
				travelingTicks++;
				
				if (travelingTicks > 6 && getDeltaMovement().lengthSqr() > 2) {
					setDeltaMovement(getDeltaMovement().scale(0.5));
					hasImpulse = true;
					hurtMarked = true;
				}
			}
		} else if (inGround) {
			
			damageMult = 1;
			velMult = 1;
			
			if (jiggleTicks < 15) {
				jiggleTicks++;
				
				var intensity = 1 - (jiggleTicks / 15F);
				
				xRotO = getXRot();
				setXRot(xRotO + jiggleIntensity * intensity / 2 * (random.nextInt(3) - 1));
				yRotO = getYRot();
				setYRot(yRotO + jiggleIntensity * intensity * (random.nextInt(3) - 1));
			}
			
			for (Entity thornCandidate : level().getEntities(this, makeBoundingBox(), this::canHitEntity)) {
				if (entityData.get(HIT)) {
					if (!(thornCandidate instanceof ItemEntity) && thornCandidate.hurt(damageSources().thorns(this), 4))
						playSound(SoundEvents.THORNS_HIT, 1, 0.9F + random.nextFloat() * 0.2F);
				}
			}
		}
		
		super.tick();
	}
	
	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.TRIDENT_HIT_GROUND;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		var striker = source.getEntity();
		
		if (striker == null)
			return false;
		
		if (!entityData.get(HIT)) {
			
			if (isPropelled()) {
				applyInertiaEffects(getTrackedStack());
			}
			
			travelingTicks = 0;
			this.setVelocity(striker.getXRot(), striker.getYRot(), 0.0F, 3F * velMult);
			setXRot(striker.getXRot());
			setYRot(striker.getYRot());
			xRotO = getXRot();
			yRotO = getYRot();
			setPropelled(true);
			setRebounding(false);
			((TridentEntityAccessor) this).spectrum$setDealtDamage(false);
			playSound(SpectrumSoundEvents.METAL_HIT, 0.8F, 0.8F + random.nextFloat() * 0.4F);
		} else {
			jiggleTicks = 0;
			jiggleIntensity = 8;
			playSound(SoundEvents.TRIDENT_HIT_GROUND, 1, 1);
		}
		
		return false;
	}
	
	@Override
	public boolean isAttackable() {
		return true;
	}
	
	@Override
	public AABB makeBoundingBox() {
		if (isPropelled()) {
			return super.makeBoundingBox();
		} else if (isRebounding()) {
			return shortSize.makeBoundingBox(position());
		}
		
		if (inGround) {
			var absPitch = Math.abs(getXRot());
			if (absPitch > 55)
				return tallSize.makeBoundingBox(position());
			return shortSize.makeBoundingBox(position());
		}
		
		return initialSize.makeBoundingBox(position());
	}
	
	public void setVelocity(float pitch, float yaw, float roll, float speed) {
		float f = -Mth.sin(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
		float g = -Mth.sin((pitch + roll) * (float) (Math.PI / 180.0));
		float h = Mth.cos(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
		setDeltaMovement(new Vec3(f, g, h).scale(speed));
		hasImpulse = true;
		hurtMarked = true;
	}
	
	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		Entity attacked = entityHitResult.getEntity();
		if (attacked.getType() == EntityType.ENDERMAN) {
			return;
		}
		
		var propelled = isPropelled();
		ItemStack stack = getTrackedStack();
		var channeling = SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.CHANNELING, stack);
		Entity owner = this.getOwner();
		
		if (piercedEntities.contains(attacked))
			return;
		
		float damage = propelled ? 2F : 1F;
		damage = adjustDamage(damage, channeling);
		boolean crit = false;
		
		DamageSource damageSource = SpectrumDamageTypes.impaling(level(), this, owner);
		if (level() instanceof ServerLevel serverWorld) {
			damage *= EnchantmentHelper.modifyDamage(serverWorld, stack, attacked, damageSource, getDamage(stack));
		}
		
		if (!attacked.onGround() && propelled) {
			damage *= 3 + ImprovedCriticalHelper.getAddtionalCritDamageMultiplier(level().registryAccess(), stack);
			crit = true;
		}
		
		if (attacked.hurt(damageSource, damage)) {
			if (attacked.getType() == EntityType.ENDERMAN) {
				return;
			}
			
			if (level() instanceof ServerLevel serverWorld) {
				EnchantmentHelper.doPostAttackEffectsWithItemSource(serverWorld, attacked, damageSource, stack);
			}
			
			if (attacked instanceof LivingEntity livingAttacked) {
				this.doKnockback(livingAttacked, damageSource);
				this.doPostHurtEffects(livingAttacked);
			}
		}
		
		if (crit) {
			this.playSound(SpectrumSoundEvents.CRITICAL_CRUNCH, 1F, 1.0F);
			this.playSound(SpectrumSoundEvents.IMPACT_BASE, 1.8F, 0.5F);
		} else {
			this.playSound(SpectrumSoundEvents.IMPALING_HIT, 1F, 0.9F + random.nextFloat() * 0.2F);
		}
		
		// We do a lil piercing
		if (getMaxPierce() > 0) {
			damageMult *= 0.8F;
			piercedEntities.add(attacked);
			setMaxPierce(getMaxPierce() - 1);
			return;
		}
		
		((TridentEntityAccessor) this).spectrum$setDealtDamage(true);
		
		applyChannelingAOE(channeling, damage, attacked, damageSource);
		applyInertiaEffects(stack);
		
		this.setDeltaMovement(this.getDeltaMovement().multiply(-1, -1, -1));
		travelingTicks = 0;
		
		setPropelled(false);
		if (owner != null) {
			rebound(owner.position(), 0.105, 0.15);
		}
	}
	
	private void applyInertiaEffects(ItemStack stack) {
		var inertia = SpectrumEnchantmentHelper.getLevel(level().registryAccess(), SpectrumEnchantments.INERTIA, stack);
		if (inertia > 0) {
			damageMult += inertia * 0.1675F;
			if (velMult < 2) {
				velMult += 0.1F;
			}
		}
	}
	
	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		var pos = blockHitResult.getBlockPos();
		var state = level().getBlockState(pos);
		var stack = getTrackedStack();
		var channeling = SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.CHANNELING, stack);
		var damage = adjustDamage(getDamage(stack), channeling);
		var damageSource = SpectrumDamageTypes.impaling(level(), this, getOwner());
		
		var slime = state.is(Blocks.SLIME_BLOCK);
		var bounce = (state.is(Blocks.SLIME_BLOCK) || state.getDestroySpeed(level(), pos) >= 25F) && !state.is(BlockTags.PLANKS) && !state.is(BlockTags.DIRT);
		var boost = getDeltaMovement().length() < 1 || slime ? 1.4 : 0.9;
		
		if (isPropelled() && bounce && getDeltaMovement().lengthSqr() > 1) {
			switch (blockHitResult.getDirection().getAxis()) {
				case X -> setDeltaMovement(getDeltaMovement().multiply(-boost, boost, boost));
				case Y -> setDeltaMovement(getDeltaMovement().multiply(boost, -boost, boost));
				case Z -> setDeltaMovement(getDeltaMovement().multiply(boost, boost, -boost));
			}
			playSound(SpectrumSoundEvents.METAL_TAP, 1, 1.5F);
			applyChannelingAOE(channeling, damage, null, damageSource);
			travelingTicks = 0;
			return;
		}
		
		if (!isRebounding() && !isPropelled() && bounce && getOwner() != null) {
			travelingTicks = 0;
			rebound(getOwner().position(), 0.105, 0.15);
			playSound(SpectrumSoundEvents.METAL_TAP, 1, 1.5F);
			return;
		}
		
		super.onHitBlock(blockHitResult);
		if (entityData.get(HIT) || isNoPhysics())
			return;
		
		if (isPropelled()) {
			applyChannelingAOE(channeling, damage * 2F, null, damageSource);
		}
		setRebounding(false);
		setPropelled(false);
		entityData.set(HIT, true);
		jiggleTicks = 0;
		jiggleIntensity = 4;
	}
	
	private float adjustDamage(float damage, int channeling) {
		damage *= damageMult * (channeling > 0 ? 0.75F : 1F);
		return damage;
	}
	
	private void applyChannelingAOE(int channeling, float damage, @Nullable Entity except, DamageSource damageSource) {
		if (channeling > 0 && !level().isClientSide()) {
			var world = (ServerLevel) level();
			var hitbox = makeBoundingBox().inflate(2.5 + channeling * 1.5);
			var entities = level().getEntities(this, hitbox);
			float spreadingDamage = damage * (1 - 1F / (channeling + 2F));
			var anyHit = false;
			for (Entity entity : entities) {
				if (entity instanceof LivingEntity living && living != except && living != getOwner()) {
					if (living.hurt(damageSource, spreadingDamage / (Math.max(entity.distanceTo(this) / 2F, 1)))) {
						for (int i = 0; i < 8; i++) {
							world.sendParticles(ParticleTypes.ENCHANTED_HIT,
									living.getRandomX(1.25),
									living.getY() + living.getBbHeight() * random.nextFloat(),
									living.getRandomZ(1.25),
									1 + random.nextInt(2), 0, random.nextFloat() / 6F, 0, 0);
						}
						anyHit = true;
					}
				}
			}
			
			if (anyHit) {
				for (int i = 0; i < 10 * channeling; i++) {
					world.sendParticles(ParticleTypes.GLOW,
							getRandomX(1),
							getY() + getBbHeight() * random.nextFloat(),
							getRandomZ(1),
							1 + random.nextInt(2), 0, random.nextFloat() + 0.25F, 0, 0);
				}
				
				world.playSeededSound(null, position().x, position().y, position().z, SpectrumSoundEvents.ELECTRIC_DISCHARGE, SoundSource.PLAYERS, 1F, 0.6F + random.nextFloat() * 0.2F, 0);
			}
		}
	}
	
	@Nullable
	@Override
	protected EntityHitResult findHitEntity(Vec3 currentPosition, Vec3 nextPosition) {
		return ProjectileUtil.getEntityHitResult(
				this.level(), this, currentPosition, nextPosition, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity
		);
	}
	
	private float getDamage(ItemStack stack) {
		//TODO can we use a built in function for this?
		var damage = new MutableDouble(0);
		var key = Attributes.ATTACK_DAMAGE.unwrapKey().orElse(null);
		var base = Attributes.ATTACK_DAMAGE.value().getDefaultValue();
		var modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		modifiers.forEach(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
			if (attribute.is(key)) {
				var value = modifier.amount();
				damage.addAndGet(switch (modifier.operation()) {
					case ADD_VALUE -> value;
					case ADD_MULTIPLIED_BASE -> value * base;
					case ADD_MULTIPLIED_TOTAL -> value * damage.getValue();
				});
			}
		});
		return damage.getValue().floatValue();
	}
	
	@Override
	public void tickDespawn() {
	}
	
	public boolean isPropelled() {
		return entityData.get(PROPELLED);
	}
	
	public boolean isRebounding() {
		return entityData.get(REBOUND);
	}
	
	public void setPropelled(boolean propelled) {
		entityData.set(PROPELLED, propelled);
	}
	
	public void setRebounding(boolean rebounding) {
		entityData.set(REBOUND, rebounding);
	}
	
	public void rebound(Vec3 target, double xMod, double yMod) {
		setRebounding(true);
		
		var yPos = this.position();
		var heightDif = Math.abs(yPos.y - target.y);
		var velocity = target.subtract(yPos);
		var finalMult = (velMult - 1) / 2 + 1;
		
		yMod = Math.max(0.0725, yMod * (1 - (heightDif * 0.024)));
		
		this.setDeltaMovement(velocity.multiply(xMod, yMod, xMod).scale(finalMult).add(0, 0.3, 0));
		this.setYRot(-getYRot());
		this.setXRot(-getXRot());
		this.hurtMarked = true;
		this.hasImpulse = true;
	}
	
	@Override
	public void remove(RemovalReason reason) {
		var rootStack = getRootStack();
		if (!rootStack.isEmpty()) {
			SlotReservingItem.free(rootStack);
		}
		super.remove(reason);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HIT, false);
		builder.define(PROPELLED, false);
		builder.define(REBOUND, false);
		builder.define(MAX_PIERCE, 0);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.entityData.set(HIT, nbt.getBoolean("hit"));
		setPropelled(nbt.getBoolean("propelled"));
		setRebounding(nbt.getBoolean("rebounding"));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("hit", this.entityData.get(HIT));
		nbt.putBoolean("propelled", isPropelled());
		nbt.putBoolean("rebounding", isRebounding());
	}
	
	public void setMaxPierce(int pierce) {
		this.entityData.set(MAX_PIERCE, pierce);
	}
	
	public int getMaxPierce() {
		return this.entityData.get(MAX_PIERCE);
	}
	
	private ItemStack getRootStack() {
		if (getOwner() instanceof Player player) {
			return DraconicTwinswordItem.findThrownStack(player, uuid);
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void playerTouch(Player player) {
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return tryPickup(player) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
	}
	
	@Override
	protected boolean tryPickup(Player player) {
		if (player != getOwner()) {
			player.hurt(damageSources().thorns(this), 20);
			playSound(SoundEvents.THORNS_HIT, 1, 0.9F + random.nextFloat() * 0.2F);
			return false;
		}
		
		var rootStack = DraconicTwinswordItem.findThrownStack(player, uuid);
		if (!rootStack.isEmpty()) {
			if (this.level().isClientSide())
				return true;
			
			SlotReservingItem.free(rootStack);
			player.take(this, 1);
			player.playSound(SoundEvents.ITEM_PICKUP, 1, 1);
			discard();
			return true;
		} else {
			discard();
		}
		return false;
	}
	
	@Nullable
	@Override
	public ItemEntity spawnAtLocation(ItemStack stack) {
		return null;
	}
	
	@Nullable
	@Override
	public ItemEntity spawnAtLocation(ItemStack stack, float yOffset) {
		return null;
	}
}