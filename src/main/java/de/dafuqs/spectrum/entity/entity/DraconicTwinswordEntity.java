package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DraconicTwinswordEntity extends BidentBaseEntity implements NonLivingAttackable {
	
	private static final TrackedData<Boolean> HIT = DataTracker.registerData(DraconicTwinswordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> PROPELLED = DataTracker.registerData(DraconicTwinswordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> REBOUND = DataTracker.registerData(DraconicTwinswordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> MAX_PIERCE = DataTracker.registerData(DraconicTwinswordEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final EntityDimensions initialSize = EntityDimensions.changing(1.5F, 1.5F);
	private static final EntityDimensions shortSize = EntityDimensions.changing(1F, 1F);
	private static final EntityDimensions tallSize = EntityDimensions.changing(1F, 1.8F);
	private final Set<Entity> piercedEntities = new HashSet<>();
	private int travelingTicks = 0, jiggleTicks = 20, jiggleIntensity = 8;
	private float damageMult = 1, velMult = 1;
	
	
	public DraconicTwinswordEntity(World world) {
		this(SpectrumEntityTypes.DRACONIC_TWINSWORD, world);
	}
	
	public DraconicTwinswordEntity(EntityType<? extends TridentEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public boolean canHit() {
		return true;
	}
	
	@Override
	public void tick() {
		if (isPropelled() && !isRebounding()) {
			if (travelingTicks < 12) {
				travelingTicks++;
				
				if (travelingTicks > 6 && getVelocity().lengthSquared() > 2) {
					setVelocity(getVelocity().multiply(0.5));
					velocityDirty = true;
					velocityModified = true;
				}
			}
		} else if (inGround) {
			
			damageMult = 1;
			velMult = 1;
			
			if (jiggleTicks < 15) {
				jiggleTicks++;
				
				var intensity = 1 - (jiggleTicks / 15F);
				
				prevPitch = getPitch();
				setPitch(prevPitch + jiggleIntensity * intensity / 2 * (random.nextInt(3) - 1));
				prevYaw = getYaw();
				setYaw(prevYaw + jiggleIntensity * intensity * (random.nextInt(3) - 1));
			}
			
			for (Entity thornCandidate : getWorld().getOtherEntities(this, calculateBoundingBox(), this::canHit)) {
				if (dataTracker.get(HIT)) {
					if (!(thornCandidate instanceof ItemEntity) && thornCandidate.damage(getDamageSources().thorns(this), 4))
						playSound(SoundEvents.ENCHANT_THORNS_HIT, 1, 0.9F + random.nextFloat() * 0.2F);
				}
			}
		}
		
		super.tick();
	}
	
	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
	}
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		var striker = source.getAttacker();
		
		if (striker == null)
			return false;
		
		if (!dataTracker.get(HIT)) {
			
			if (isPropelled()) {
				applyInertiaEffects(getTrackedStack());
			}
			
			travelingTicks = 0;
			this.setVelocity(striker.getPitch(), striker.getYaw(), 0.0F, 3F * velMult);
			setPitch(striker.getPitch());
			setYaw(striker.getYaw());
			prevPitch = getPitch();
			prevYaw = getYaw();
			setPropelled(true);
			setRebounding(false);
			((TridentEntityAccessor) this).spectrum$setDealtDamage(false);
			playSound(SpectrumSoundEvents.METAL_HIT, 0.8F, 0.8F + random.nextFloat() * 0.4F);
		} else {
			jiggleTicks = 0;
			jiggleIntensity = 8;
			playSound(SoundEvents.ITEM_TRIDENT_HIT_GROUND, 1, 1);
		}
		
		return false;
	}
	
	@Override
	public boolean isAttackable() {
		return true;
	}
	
	@Override
	public Box calculateBoundingBox() {
		if (isPropelled()) {
			return super.calculateBoundingBox();
		} else if (isRebounding()) {
			return shortSize.getBoxAt(getPos());
		}
		
		if (inGround) {
			var absPitch = Math.abs(getPitch());
			if (absPitch > 55)
				return tallSize.getBoxAt(getPos());
			return shortSize.getBoxAt(getPos());
		}
		
		return initialSize.getBoxAt(getPos());
	}
	
	public void setVelocity(float pitch, float yaw, float roll, float speed) {
		float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
		float g = -MathHelper.sin((pitch + roll) * (float) (Math.PI / 180.0));
		float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
		setVelocity(new Vec3d(f, g, h).multiply(speed));
		velocityDirty = true;
		velocityModified = true;
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity attacked = entityHitResult.getEntity();
		if (attacked.getType() == EntityType.ENDERMAN) {
			return;
		}
		
		var propelled = isPropelled();
		ItemStack stack = getTrackedStack();
		var channeling = EnchantmentHelper.getLevel(Enchantments.CHANNELING, stack);
		Entity owner = this.getOwner();
		
		if (piercedEntities.contains(attacked))
			return;
		
		float damage = propelled ? 2F : 1F;
		damage = adjustDamage(damage, channeling);
		boolean crit = false;
		
		if (attacked instanceof LivingEntity livingAttacked) {
			damage *= (getDamage(stack) + EnchantmentHelper.getAttackDamage(stack, livingAttacked.getGroup()));
		}
		
		if (!attacked.isOnGround() && propelled) {
			damage *= 3 + ImprovedCriticalEnchantment.getAddtionalCritDamageMultiplier(SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.IMPROVED_CRITICAL, stack, owner));
			crit = true;
		}
		
		DamageSource damageSource = SpectrumDamageTypes.impaling(getWorld(), this, owner);
		if (attacked.damage(damageSource, damage)) {
			if (attacked instanceof LivingEntity livingAttacked) {
				if (owner instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingAttacked, owner);
					EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingAttacked);
				}
				
				this.onHit(livingAttacked);
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
		
		this.setVelocity(this.getVelocity().multiply(-1, -1, -1));
		travelingTicks = 0;
		
		setPropelled(false);
		if (owner != null) {
			rebound(owner.getPos(), 0.105, 0.15);
		}
	}
	
	private void applyInertiaEffects(ItemStack stack) {
		var inertia = EnchantmentHelper.getLevel(SpectrumEnchantments.INERTIA, stack);
		if (inertia > 0) {
			damageMult += inertia * 0.1675F;
			if (velMult < 2) {
				velMult += 0.1F;
			}
		}
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		var pos = blockHitResult.getBlockPos();
		var state = getWorld().getBlockState(pos);
		var stack = getTrackedStack();
		var channeling = EnchantmentHelper.getLevel(Enchantments.CHANNELING, stack);
		var damage = adjustDamage(getDamage(stack), channeling);
		var damageSource = SpectrumDamageTypes.impaling(getWorld(), this, getOwner());
		
		var slime = state.isOf(Blocks.SLIME_BLOCK);
		var bounce = (state.isOf(Blocks.SLIME_BLOCK) || state.getHardness(getWorld(), pos) >= 25F) && !state.isIn(BlockTags.PLANKS) && !state.isIn(BlockTags.DIRT);
		var boost = getVelocity().length() < 1 || slime ? 1.4 : 0.9;
		
		if (isPropelled() && bounce && getVelocity().lengthSquared() > 1) {
			switch (blockHitResult.getSide().getAxis()) {
				case X -> setVelocity(getVelocity().multiply(-boost, boost, boost));
				case Y -> setVelocity(getVelocity().multiply(boost, -boost, boost));
				case Z -> setVelocity(getVelocity().multiply(boost, boost, -boost));
			}
			playSound(SpectrumSoundEvents.METAL_TAP, 1, 1.5F);
			applyChannelingAOE(channeling, damage, null, damageSource);
			travelingTicks = 0;
			return;
		}
		
		if (!isRebounding() && !isPropelled() && bounce) {
			travelingTicks = 0;
			rebound(getOwner().getPos(), 0.105, 0.15);
			playSound(SpectrumSoundEvents.METAL_TAP, 1, 1.5F);
			return;
		}
		
		super.onBlockHit(blockHitResult);
		if (dataTracker.get(HIT) || isNoClip())
			return;
		
		if (isPropelled()) {
			applyChannelingAOE(channeling, damage * 2F, null, damageSource);
		}
		setRebounding(false);
		setPropelled(false);
		dataTracker.set(HIT, true);
		jiggleTicks = 0;
		jiggleIntensity = 4;
	}
	
	private float adjustDamage(float damage, int channeling) {
		damage *= damageMult * (channeling > 0 ? 0.75F : 1F);
		return damage;
	}
	
	private void applyChannelingAOE(int channeling, float damage, @Nullable Entity except, DamageSource damageSource) {
		if (channeling > 0 && !getWorld().isClient()) {
			var world = (ServerWorld) getWorld();
			var hitbox = calculateBoundingBox().expand(2.5 + channeling * 1.5);
			var entities = getWorld().getOtherEntities(this, hitbox);
			float spreadingDamage = damage * (1 - 1F / (channeling + 2F));
			var anyHit = false;
			for (Entity entity : entities) {
				if (entity instanceof LivingEntity living && living != except && living != getOwner()) {
					if (living.damage(damageSource, spreadingDamage / (Math.max(entity.distanceTo(this) / 2F, 1)))) {
						for (int i = 0; i < 8; i++) {
							world.spawnParticles(ParticleTypes.ENCHANTED_HIT,
									living.getParticleX(1.25),
									living.getY() + living.getHeight() * random.nextFloat(),
									living.getParticleZ(1.25),
									1 + random.nextInt(2), 0, random.nextFloat() / 6F, 0, 0);
						}
						anyHit = true;
					}
				}
			}
			
			if (anyHit) {
				for (int i = 0; i < 10 * channeling; i++) {
					world.spawnParticles(ParticleTypes.GLOW,
							getParticleX(1),
							getY() + getHeight() * random.nextFloat(),
							getParticleZ(1),
							1 + random.nextInt(2), 0, random.nextFloat() + 0.25F, 0, 0);
				}
				
				world.playSound(null, getPos().x, getPos().y, getPos().z, SpectrumSoundEvents.ELECTRIC_DISCHARGE, SoundCategory.PLAYERS, 1F, 0.6F + random.nextFloat() * 0.2F, 0);
			}
		}
	}
	
	@Nullable
	@Override
	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return ProjectileUtil.getEntityCollision(
				this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit
		);
	}
	
	private float getDamage(ItemStack stack) {
		return (float) stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
				.stream()
				.mapToDouble(EntityAttributeModifier::getValue)
				.sum();
	}
	
	@Override
	public void age() {
	}
	
	public boolean isPropelled() {
		return dataTracker.get(PROPELLED);
	}
	
	public boolean isRebounding() {
		return dataTracker.get(REBOUND);
	}
	
	public void setPropelled(boolean propelled) {
		dataTracker.set(PROPELLED, propelled);
	}
	
	public void setRebounding(boolean rebounding) {
		dataTracker.set(REBOUND, rebounding);
	}
	
	public void rebound(Vec3d target, double xMod, double yMod) {
		setRebounding(true);
		
		var yPos = this.getPos();
		var heightDif = Math.abs(yPos.y - target.y);
		var velocity = target.subtract(yPos);
		var finalMult = (velMult - 1) / 2 + 1;
		
		yMod = Math.max(0.0725, yMod * (1 - (heightDif * 0.024)));
		
		this.setVelocity(velocity.multiply(xMod, yMod, xMod).multiply(finalMult).add(0, 0.3, 0));
		this.setYaw(-getYaw());
		this.setPitch(-getPitch());
		this.velocityModified = true;
		this.velocityDirty = true;
	}
	
	@Override
	public void remove(RemovalReason reason) {
		var rootStack = getRootStack();
		if (!rootStack.isEmpty()) {
			SpectrumItems.DRACONIC_TWINSWORD.markReserved(rootStack, false);
		}
		super.remove(reason);
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(HIT, false);
		this.dataTracker.startTracking(PROPELLED, false);
		this.dataTracker.startTracking(REBOUND, false);
		this.dataTracker.startTracking(MAX_PIERCE, 0);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.dataTracker.set(HIT, nbt.getBoolean("hit"));
		setTrackedStack(ItemStack.fromNbt(nbt));
		setPropelled(nbt.getBoolean("propelled"));
		setRebounding(nbt.getBoolean("rebounding"));
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		getTrackedStack().writeNbt(nbt);
		nbt.putBoolean("hit", this.dataTracker.get(HIT));
		nbt.putBoolean("propelled", isPropelled());
		nbt.putBoolean("rebounding", isRebounding());
	}
	
	public void setMaxPierce(int pierce) {
		this.dataTracker.set(MAX_PIERCE, pierce);
	}
	
	public int getMaxPierce() {
		return this.dataTracker.get(MAX_PIERCE);
	}
	
	private ItemStack getRootStack() {
		if (getOwner() instanceof PlayerEntity player) {
			var rootStack = DraconicTwinswordItem.findThrownStack(player, uuid);
			return rootStack;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void onPlayerCollision(PlayerEntity player) {
	
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		return tryPickup(player) ? ActionResult.SUCCESS : ActionResult.FAIL;
	}
	
	@Override
	protected boolean tryPickup(PlayerEntity player) {
		if (player != getOwner()) {
			player.damage(getDamageSources().thorns(this), 20);
			playSound(SoundEvents.ENCHANT_THORNS_HIT, 1, 0.9F + random.nextFloat() * 0.2F);
			return false;
		}
		
		var rootStack = DraconicTwinswordItem.findThrownStack(player, uuid);
		if (!rootStack.isEmpty()) {
			if (this.getWorld().isClient())
				return true;
			
			SpectrumItems.DRACONIC_TWINSWORD.markReserved(rootStack, false);
			player.sendPickup(this, 1);
			player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1);
			discard();
			return true;
		} else {
			discard();
		}
		return false;
	}
	
	@Nullable
	@Override
	public ItemEntity dropStack(ItemStack stack) {
		return null;
	}
	
	@Nullable
	@Override
	public ItemEntity dropStack(ItemStack stack, float yOffset) {
		return null;
	}
}