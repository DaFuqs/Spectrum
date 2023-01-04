package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.entity.SpectrumTrackedDataHandlerRegistry;
import de.dafuqs.spectrum.items.tools.GlassArrowItem;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GlassArrowEntity extends PersistentProjectileEntity {
	
	private static final TrackedData<GlassArrowItem.Variant> VARIANT = DataTracker.registerData(GlassArrowEntity.class, SpectrumTrackedDataHandlerRegistry.GLASS_ARROW_VARIANT);
	public static final float DAMAGE_MODIFIER = 1.25F;
	
	
	public GlassArrowEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	public GlassArrowEntity(World world, LivingEntity owner) {
		super(SpectrumEntityTypes.GLASS_ARROW, owner, world);
	}
	
	public GlassArrowEntity(World world, double x, double y, double z) {
		super(SpectrumEntityTypes.GLASS_ARROW, x, y, z, world);
		setDamage(getDamage() * DAMAGE_MODIFIER);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.world.isClient) {
			if(!this.onGround || world.getTime() % 2 == 0) {
				spawnParticles(1);
			}
		}
	}
	
	@Override
	public boolean hasNoGravity() {
		return this.getVelocity().lengthSquared() > 0.1F;
	}
	
	private void spawnParticles(int amount) {
		DefaultParticleType particleType = null;
		switch (this.getVariant()) {
			case MALACHITE -> {
				particleType = SpectrumParticleTypes.LIME_CRAFTING;
			}
			case TOPAZ -> {
				particleType = SpectrumParticleTypes.CYAN_CRAFTING;
			}
			case AMETHYST -> {
				particleType = SpectrumParticleTypes.MAGENTA_CRAFTING;
			}
			case CITRINE -> {
				particleType = SpectrumParticleTypes.YELLOW_CRAFTING;
			}
			case ONYX -> {
				particleType = SpectrumParticleTypes.BLACK_CRAFTING;
			}
			case MOONSTONE -> {
				particleType = SpectrumParticleTypes.WHITE_CRAFTING;
			}
		}
		
		if(particleType != null) {
			for (int j = 0; j < amount; ++j) {
				this.world.addParticle(particleType, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0, 0, 0);
			}
		}
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		// additional effects depending on arrow type
		// mundane glass arrows do not have additional effects
		switch (getVariant()) {
			case TOPAZ -> {
				// TODO
			}
			case AMETHYST -> {
				if(!this.world.isClient) {
					Entity entity = entityHitResult.getEntity();
					entity.setFrozenTicks(200);
				}
			}
			case ONYX -> {
				if(!this.world.isClient && this.getOwner() != null) {
					Entity entity = entityHitResult.getEntity();
					pullEntityClose(this.getOwner(), entity, 0.2);
				}
			}
			case MOONSTONE -> {
				MoonstoneBlast.create(world, this, null, this.getX(), this.getY(), this.getZ(), 4);
			}
		}
		
		super.onEntityHit(entityHitResult);
		this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.75F, 0.9F + world.random.nextFloat() * 0.2F);
		this.remove(RemovalReason.DISCARDED);
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		if(getVariant() == GlassArrowItem.Variant.MOONSTONE) {
			MoonstoneBlast.create(world, this, null, this.getX(), this.getY(), this.getZ(), 4);
		}
	}
	
	protected static void pullEntityClose(Entity shooter, Entity entityToPull, double pullStrength) {
		double d = shooter.getX() - entityToPull.getX();
		double e = shooter.getY() - entityToPull.getY();
		double f = shooter.getZ() - entityToPull.getZ();
		
		double pullStrengthModifier = 1.0;
		if(entityToPull instanceof LivingEntity livingEntity) {
			pullStrengthModifier = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
		}
		
		Vec3d additionalVelocity = new Vec3d(d * pullStrength, e * pullStrength + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * pullStrength).multiply(pullStrengthModifier);
		entityToPull.addVelocity(additionalVelocity.x, additionalVelocity.y, additionalVelocity.z);
	}
	
	protected void moonstoneExplosion(Vec3d position) {
		this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0, 0, 0);
		
		
	}
	
	@Override
	protected ItemStack asItemStack() {
		return dataTracker.get(VARIANT).getStack();
	}
	
	/**
	 * Glass Arrows pass through translucent blocks as if it were air
	 */
	@Override
	protected void onCollision(HitResult hitResult) {
		HitResult.Type type = hitResult.getType();
		if (type == HitResult.Type.BLOCK) {
			BlockPos hitPos = ((BlockHitResult) hitResult).getBlockPos();
			BlockState state = this.world.getBlockState(hitPos);
			if(state.isTranslucent(world, hitPos)) {
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
		this.dataTracker.startTracking(VARIANT, GlassArrowItem.Variant.MALACHITE);
	}
	
	public void setVariant(GlassArrowItem.Variant variant) {
		this.dataTracker.set(VARIANT, variant);
		
		if(variant == GlassArrowItem.Variant.CITRINE) {
			setPunch(5);
		}
	}
	
	public GlassArrowItem.Variant getVariant() {
		return this.dataTracker.get(VARIANT);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("Variant", this.getVariant().toString());
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setVariant(GlassArrowItem.Variant.fromString(nbt.getString("Variant")));
	}
	
}
