package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DragonTalonEntity extends BidentBaseEntity {
	
	private static final TrackedData<Boolean> HIT = DataTracker.registerData(DragonTalonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	public DragonTalonEntity(World world) {
		this(SpectrumEntityTypes.DRAGON_TALON, world);
	}
	
	public DragonTalonEntity(EntityType<? extends TridentEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		var pos = blockHitResult.getBlockPos();
		var state = getWorld().getBlockState(pos);
		
		if (state.isOf(Blocks.SLIME_BLOCK) && getVelocity().lengthSquared() > 1) {
			switch (blockHitResult.getSide().getAxis()) {
				case X -> setVelocity(getVelocity().multiply(-1, 1, 1));
				case Y -> setVelocity(getVelocity().multiply(1, -1, 1));
				case Z -> setVelocity(getVelocity().multiply(1, 1, -1));
			}
			playSound(SpectrumSoundEvents.METAL_HIT, 1, 1.5F);
			return;
		}
		
		super.onBlockHit(blockHitResult);
		if (dataTracker.get(HIT) || isNoClip())
			return;
		
		dataTracker.set(HIT, true);
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity attacked = entityHitResult.getEntity();
		if (attacked.getType() == EntityType.ENDERMAN) {
			return;
		}
		
		float f = 2.0F;
		if (attacked instanceof LivingEntity livingAttacked) {
			f *= (getDamage(getTrackedStack()) + EnchantmentHelper.getAttackDamage(getTrackedStack(), livingAttacked.getGroup()));
		}
		
		Entity owner = this.getOwner();
		if (attacked.damage(SpectrumDamageTypes.impaling(getWorld(), this, owner), f)) {
			if (attacked instanceof LivingEntity livingAttacked) {
				if (owner instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingAttacked, owner);
					EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingAttacked);
				}
				
				this.onHit(livingAttacked);
			}
		}
		
		((TridentEntityAccessor) this).spectrum$setDealtDamage(true);
		recall();
		this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
		float g = 1.0F;
		
		this.playSound(SpectrumSoundEvents.IMPALING_HIT, g, 1.0F);
	}
	
	private float getDamage(ItemStack stack) {
		return (float) stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
				.stream()
				.mapToDouble(EntityAttributeModifier::getValue)
				.sum();
	}
	
	@Override
	protected void onHit(LivingEntity target) {
		if (getOwner() == null)
			return;
		
		var owner = getOwner();
		var difMod = 4F;
		var airborne = !owner.isOnGround();
		var sneaking = owner.isSneaking();
		var inertia = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.INERTIA, getTrackedStack(), owner);
		
		if (sneaking)
			difMod *= 3;
		
		if (airborne)
			difMod /= 2;
		
		if (inertia > 0) {
			difMod *= inertia * 1.5F + 1;
		}
		
		var sizeDif = getVolumeDif(target, difMod);
		yoink(target, getOwner().getPos(), 0.25 * sizeDif, 0.175);
		
		if (airborne)
			yoink(owner, target.getPos(), 0.125 / sizeDif, 0.16);
	}
	
	private float getVolumeDif(LivingEntity target, float pullMod) {
		var ownerBox = getOwner().getBoundingBox();
		var targetBox = target.getBoundingBox();
		float ownerVolume = (float) (ownerBox.getXLength() * ownerBox.getYLength() * ownerBox.getZLength());
		float targetVolume = (float) (targetBox.getXLength() * targetBox.getYLength() * targetBox.getZLength());
		
		return Math.max(Math.min(ownerVolume / (targetVolume / pullMod), 0.8F), 0.5F);
	}
	
	public void recall() {
		var owner = getOwner();
		if (dataTracker.get(HIT) && !isNoClip()) {
			yoink(owner, getPos(), 0.125, 0.165);
		}
		
		if (EnchantmentHelper.getLevel(Enchantments.CHANNELING, getTrackedStack()) > 0 && owner != null) {
			if (!getWorld().isClient()) {
				var world = (ServerWorld) getWorld();
				for (int i = 0; i < 10; i++) {
					world.spawnParticles(ParticleTypes.GLOW,
							getParticleX(1),
							getY() + getHeight() * random.nextFloat(),
							getParticleZ(1),
							1 + random.nextInt(2), 0, random.nextFloat() + 0.25F, 0, 0);
				}
				
				world.playSound(null, getPos().x, getPos().y, getPos().z, SpectrumSoundEvents.ELECTRIC_DISCHARGE, SoundCategory.AMBIENT, 1F, 0.6F + random.nextFloat() * 0.2F, 0);
			}
			remove(RemovalReason.DISCARDED);
			return;
		}
		
		getDataTracker().set(TridentEntityAccessor.spectrum$getLoyalty(), (byte) 4);
		setNoClip(true);
	}
	
	public void yoink(@Nullable Entity yoinked, Vec3d target, double xMod, double yMod) {
		if (yoinked == null)
			return;
		
		var yPos = yoinked.getPos();
		var heightDif = Math.abs(yPos.y - target.y);
		var velocity = target.subtract(yPos);
		var sneaking = yoinked.isSneaking();
		var bonusMod = 1f;
		
		if (yoinked instanceof LivingEntity livingYoink) {
			bonusMod /= Optional.ofNullable(livingYoink.getStatusEffect(SpectrumStatusEffects.DENSITY))
					.map(effect -> effect.getAmplifier() + 2).orElse(1);
			bonusMod *= Optional.ofNullable(livingYoink.getStatusEffect(SpectrumStatusEffects.LIGHTWEIGHT))
					.map(effect -> (effect.getAmplifier() + 2) / 1.5F).orElse(1F);
		}
		
		if (!yoinked.isOnGround()) {
			yMod += 0.05;
			xMod -= 0.015;
		}
		
		yMod = Math.max(0.0725, yMod * (1 - (heightDif * 0.024)));
		
		xMod *= bonusMod;
		yMod *= bonusMod;
		
		if (yoinked == getOwner() && yPos.y > target.y && !sneaking)
			yMod = 0;
		
		yoinked.setVelocity(velocity.multiply(xMod, yMod, xMod).add(0, sneaking ? 0 : 0.25, 0));
		yoinked.fallDistance = 0F;
		yoinked.velocityModified = true;
		yoinked.velocityDirty = true;
	}
	
	@Override
	public void age() {
		if (!getRootStack().isEmpty())
			return;
		
		var life = ((PersistentProjectileEntityAccessor) this).getLife() + 1;
		((PersistentProjectileEntityAccessor) this).setLife(life);
		if (life >= 1200) {
			this.discard();
		}
	}
	
	@Override
	public void remove(RemovalReason reason) {
		var rootStack = getRootStack();
		if (!rootStack.isEmpty()) {
			SpectrumItems.DRAGON_TALON.markReserved(rootStack, false);
		}
		super.remove(reason);
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(HIT, false);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.dataTracker.set(HIT, nbt.getBoolean("hit"));
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("hit", this.dataTracker.get(HIT));
	}
	
	private ItemStack getRootStack() {
		if (getOwner() instanceof PlayerEntity player) {
			var rootStack = DragonTalonItem.findThrownStack(player, uuid);
			return rootStack;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	protected boolean tryPickup(PlayerEntity player) {
		var rootStack = DragonTalonItem.findThrownStack(player, uuid);
		if (!rootStack.isEmpty()) {
			SpectrumItems.DRAGON_TALON.markReserved(rootStack, false);
			return true;
		} else if (player == getOwner()) {
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
