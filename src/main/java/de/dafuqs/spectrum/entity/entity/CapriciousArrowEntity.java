package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class CapriciousArrowEntity extends PersistentProjectileEntity {

	public static final float DAMAGE_MODIFIER = 1.25F;

	public CapriciousArrowEntity(EntityType<CapriciousArrowEntity> entityType, World world) {
		super(entityType, world);
	}

	public CapriciousArrowEntity(World world, LivingEntity owner) {
		super(SpectrumEntityTypes.CAPRICIOUS_ARROW, owner, world);
	}

	public CapriciousArrowEntity(World world, double x, double y, double z) {
		super(SpectrumEntityTypes.CAPRICIOUS_ARROW, x, y, z, world);
	}
	
	@Override
	public void applyEnchantmentEffects(LivingEntity entity, float damageModifier) {
		super.applyEnchantmentEffects(entity, damageModifier);
		setDamage(getDamage() * DAMAGE_MODIFIER);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.getWorld().isClient()) {
			if (!this.isOnGround() || this.getWorld().getTime() % 8 == 0) {
				spawnParticles(1);
			}
		}
	}
	
	private void spawnParticles(int amount) {
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		LivingEntity livingEntityToResetHurtTime = null;
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
	}
	
	protected static void pullEntityClose(Entity shooter, Entity entityToPull, double pullStrength) {
		double d = shooter.getX() - entityToPull.getX();
		double e = shooter.getY() - entityToPull.getY();
		double f = shooter.getZ() - entityToPull.getZ();
		
		double pullStrengthModifier = 1.0;
		if (entityToPull instanceof LivingEntity livingEntity) {
			pullStrengthModifier = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
		}
		
		Vec3d additionalVelocity = new Vec3d(d * pullStrength, e * pullStrength + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * pullStrength).multiply(pullStrengthModifier);
		entityToPull.addVelocity(additionalVelocity.x, additionalVelocity.y, additionalVelocity.z);
	}
	
	@Override
	protected ItemStack asItemStack() {
		return null;
	}
	
	/**
	 * Glass Arrows pass through translucent blocks as if it were air
	 */
	@Override
	protected void onCollision(HitResult hitResult) {
		HitResult.Type type = hitResult.getType();
		if (type == HitResult.Type.BLOCK) {
			BlockPos hitPos = ((BlockHitResult) hitResult).getBlockPos();
			BlockState state = this.getWorld().getBlockState(hitPos);
			if (state.isTransparent(this.getWorld(), hitPos)) {
				return;
			}
		}
		super.onCollision(hitResult);
	}
	
	/**
	 * Glass Arrows pass through water almost effortlessly
	 */
	@Override
	protected float getDragInWater() {
		return 0.1F;
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
	}
	
}
